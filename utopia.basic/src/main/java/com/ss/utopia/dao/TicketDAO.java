package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.model.Flight;
import com.ss.utopia.model.Ticket;

public class TicketDAO extends BaseDAO<Ticket>{

	public TicketDAO(Connection conn) {
		super(conn);
	}

	@Override
	public List<Ticket> extractData(ResultSet rs) throws SQLException {
		List<Ticket> tickets = new ArrayList<Ticket>();
		FlightBookingsDAO ticketFlightReader = new FlightBookingsDAO(conn);
		SeatClassDAO ticketSeatReader = new SeatClassDAO(conn);
		
		while (rs.next()) {
			Ticket ticket = new Ticket();
			ticket.setId(Integer.parseInt(rs.getString("id")));
			ticket.setConfirmation(rs.getString("confirmation_code"));
			ticket.setActive(Integer.parseInt(rs.getString("is_active")) > 0);
			List<Flight> flights = ticketFlightReader.readFlightsByTicket(ticket.getId());
			ticket.setTicket(new ArrayList<Flight>());
			for (Flight flight : flights) {
				List<Flight> runningFlights = ticket.getTicket();
				runningFlights.add(flight);
				ticket.setTicket(runningFlights);
			}
			ticket.setSeatClass(ticketSeatReader.readByTicketId(ticket.getId()));
			tickets.add(ticket);
		}
		
		return tickets;
	}

	public List<Ticket> readAllTickets() throws SQLException {
		return read("select * from booking", new Object[] {});
	}
	
	public Ticket readTicketById(Integer id) throws SQLException {
		List<Ticket> tickets = read("select * from booking where id = ?", new Object[] {id});
		if (tickets.size() > 0)
			return tickets.get(0);
		else
			return null;
	}
	
	public Integer getNextId() throws SQLException {
		return readAllTickets().size() + 1;
	}
	
	public Integer addNewTicket(Ticket ticket) throws SQLException {
		Integer ticketId = getNextId();
		ticket.setId(ticketId);
		save("insert into booking (confirmation_code) values (?)", new Object[] {ticket.getConfirmation()});
		for (Flight flight : ticket.getTicket()) {
			save("insert into flight_bookings values (?, ?)", new Object[] {ticket.getId(), flight.getId()});
		}
		return ticketId;
	}
	
	public void updateTicket(Ticket ticket) throws SQLException {
		save("delete from flight_bookings where booking_id = ?", new Object[] {ticket.getId()});
		for (Flight flight : ticket.getTicket()) {
			save("insert into flight_bookings values (?, ?)",
					new Object[] {flight.getId(), ticket.getId()});
		}
		Integer active = (ticket.isActive()) ? 1 : 0;
		save("update bookings set confirmation_code = ?, isActive = ?", 
				new Object[] {ticket.getConfirmation(), active});
	}
	
	public void deleteTicket(Ticket ticket) throws SQLException {
		save("delete from flight_bookings where booking_id = ?", new Object[] {ticket.getId()});
		save("delete from booking where id = ?", new Object[] {ticket.getId()});
	}
	
	private class FlightBookingsDAO extends BaseDAO<Flight> {

		public FlightBookingsDAO(Connection conn) {
			super(conn);
		}

		@Override
		public List<Flight> extractData(ResultSet rs) throws SQLException {
			List<Flight> flights = new ArrayList<Flight>();
			while (rs.next()) {
				Flight flight = new Flight();
				flight.setId(Integer.parseInt(rs.getString("flight_id")));
				flights.add(flight);
			}
			return flights;
		}
		
		public List<Flight> readFlightsByTicket(Integer ticketID) throws SQLException {
			return read("select * from flight_bookings where booking_id = ?", new Object[] {ticketID});
		}
	}
	
	private class SeatClassDAO extends BaseDAO<String> {

		public SeatClassDAO(Connection conn) {
			super(conn);
		}

		@Override
		public List<String> extractData(ResultSet rs) throws SQLException {
			List<String> flightClass = new ArrayList<String>();
			while (rs.next()) {
				String seatClass = rs.getString("name");
				flightClass.add(seatClass);
			}
			return flightClass;
		}
		
		public String readByTicketId(Integer ticketId) throws SQLException {
			System.out.println(ticketId);
			List<String> result = read("select s.name from booking_seats as sb join seat_class as s where sb.booking_id = ? and sb.seat_id = s.id",
					new Object[] {ticketId});
			if (result != null)
				return result.get(0);
			else return null;
		}
		
	}
}
