package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
		
		while (rs.next()) {
			Ticket ticket = new Ticket();
			ticket.setId(rs.getInt("id"));
			ticket.setFlightId(rs.getInt("flight_id"));
			ticket.setSeatId(rs.getInt("seat_id"));
			ticket.setPassengerId(rs.getInt("p_id"));
			ticket.setConfirmationCode(rs.getString("confirmation_code"));
			ticket.setActive(rs.getInt("is_active") > 0);
			ticket.setPassengerName(rs.getString("given_name") + " " + rs.getString("family_name"));
			tickets.add(ticket);
		}
		return tickets;
	}

	public List<Ticket> readAllTickets() throws SQLException {
		return read("select b.id, fb.flight_id, bs.seat_id, p.id as p_id, p.given_name, p.family_name,"
				+ " b.confirmation_code, b.is_active "+
				"from booking as b join flight_bookings as fb on b.id = fb.booking_id "+
				"join booking_seats as bs on b.id = bs.booking_id "+
				"join passenger as p on p.booking_id = b.id", new Object[] {});
	}
	
	public List<Ticket> readTicketsById(Integer id) throws SQLException {
		return read("select b.id, fb.flight_id, bs.seat_id, p.id as p_id, p.given_name, p.family_name, b.confirmation_code, b.is_active "+
				"from booking as b join flight_bookings as fb on b.id = fb.booking_id "+
				"join booking_seats as bs on b.id = bs.booking_id "+
				"join passenger as p on p.booking_id = b.id where b.id = ?", new Object[] {id});
	}
	
	public List<Ticket> readTicketsByFlightId(Integer id) throws SQLException {
		return read("select b.id, fb.flight_id, bs.seat_id, p.id as p_id, p.given_name, p.family_name,"
				+ " b.confirmation_code, b.is_active "+
				"from booking as b join flight_bookings as fb on b.id = fb.booking_id "+
				"join booking_seats as bs on b.id = bs.booking_id "+
				"join passenger as p on p.booking_id = b.id where fb.flight_id = ?", new Object[] {id});
	}
	 
	public List<Ticket> readTicketsByPassengerId(Integer id) throws SQLException {
		return read("select b.id, fb.flight_id, bs.seat_id, p.id as p_id, p.given_name, p.family_name,"
				+ " b.confirmation_code, b.is_active "+
				"from booking as b join flight_bookings as fb on b.id = fb.booking_id "+
				"join booking_seats as bs on b.id = bs.booking_id "+
				"join passenger as p on p.booking_id = b.id where p.id = ?", new Object[] {id});
	}
	
	public List<Ticket> readInactiveTickets() throws SQLException {
		return read("select b.id, fb.flight_id, bs.seat_id, p.id as p_id, p.given_name, p.family_name,"
				+ " b.confirmation_code, b.is_active "+
				"from booking as b join flight_bookings as fb on b.id = fb.booking_id "+
				"join booking_seats as bs on b.id = bs.booking_id "+
				"join passenger as p on p.booking_id = b.id where b.is_active = 0", new Object[] {});
	}
	
	public Integer getNextId() throws SQLException {
		return readAllTickets().size() + 1;
	}
	
	public void addNewTicket(Ticket ticket) throws SQLException {
		if (ticket.getId() == null || readAllTickets().contains(ticket)) {
			ticket.setId(getNextId());
		}
		Integer active = (ticket.isActive()) ? 1 : 0;
		save("insert into booking values (?, ?, ?)", new Object[] {ticket.getId(), active, ticket.getConfirmationCode()});
		save("insert into passenger values (?, ?, ?, ?, '1990-01-01', 'female', 'Somewhere')",
				new Object[] {ticket.getPassengerId(), ticket.getId(), ticket.getPassengerName().split(" ")[0],
						ticket.getPassengerName().split(" ")[1]});
		save("insert into flight_bookings values (?, ?)", new Object[] {ticket.getFlightId(), ticket.getId()});
		save("insert into booking_seats values (?, ?)", new Object[] {ticket.getId(), ticket.getSeatId()});
	}
	
	public void bookNewFlight(Ticket ticket) throws SQLException {
		boolean passengerExists=false;
		for (Ticket tick : readAllTickets()) {
			if (ticket.getPassengerId().equals(tick.getPassengerId()))
				passengerExists=true;
		}
		if (passengerExists)
			save("insert into flight_bookings values (?, ?)", new Object[] {ticket.getFlightId(), ticket.getId()});
		else
			addNewTicket(ticket);
	}
	
	public void cancelBooking(Ticket ticket) throws SQLException {
		save("delete from flight_bookings where booking_id = ? and flight_id = ?",
				new Object[] {ticket.getId(), ticket.getFlightId()});
	}
	
	public void updateTicket(Ticket ticket) throws SQLException {
		Integer active;
		if (ticket.isActive())
			active = 1;
		else active = 0;
		
		save("update booking set is_active = ? where id = ?", 
				new Object[] {active, ticket.getId()});
		save("update booking_seats set seat_id = ? where booking_id = ?",
				new Object[] {ticket.getSeatId(), ticket.getId()});
		save("update passenger set given_name = ?, family_name = ? where booking_id = ?",
				new Object[] {ticket.getPassengerName().split(" ")[0], ticket.getPassengerName().split(" ")[1], ticket.getId()});
	}
	
	public void deleteTicket(Ticket ticket) throws SQLException {
		save("delete from booking where id = ?", new Object[] {ticket.getId()});
	}
}
