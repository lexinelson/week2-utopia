package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.model.Flight;
import com.ss.utopia.model.SeatSection;
import com.ss.utopia.model.Ticket;

public class FlightDAO extends BaseDAO<Flight> {

	public FlightDAO(Connection conn) {
		super(conn);
	}

	@Override
	public List<Flight> extractData(ResultSet rs) throws SQLException {
		List<Flight> flights = new ArrayList<Flight>();
		RouteDAO routeReader = new RouteDAO(conn);
		FlightSeatDAO sectionReader = new FlightSeatDAO(conn);
		TicketDAO ticketReader = new TicketDAO(conn);
		
		while (rs.next()) {
			Flight flight = new Flight();
			flight.setId(Integer.parseInt(rs.getString("id")));
			flight.setRoute(routeReader.readRouteByID(rs.getInt("route_id")));
			
//			flight.setPlane(planeReader.readPlaneByID(Integer.parseInt(rs.getString("airplane_id"))));
			flight.setMaxCapacity(rs.getInt("capacity"));
			
			flight.setStartTime(LocalDateTime.parse(rs.getString("departure_time").replace(" ", "T")));
			flight.setEndTime(flight.getStartTime().plus(flight.getRoute().getDuration()));
			
			List<SeatSection> sections = sectionReader.findSectionsByFlightID(flight.getId());
			for (SeatSection sec : sections) {
				sec.setReserved(new ArrayList<Ticket>());
				sec.setFlight(flight);
			}
			flight.setSeats(sections);
			for (Ticket tick : ticketReader.readAllTickets()) {
				for (Flight f : tick.getTicket()) {
					if (flight.equals(f)) {
						for (SeatSection section : flight.getSeats()) {
							if (tick.getSeatClass().equals(section.getClassification())) {
								List<Ticket> sectionTickets = section.getReserved();
								sectionTickets.add(tick);
								section.setReserved(sectionTickets);
							}
						}
					}
				}
			}
			flights.add(flight);
		}
		return flights;
	}
	
	public Integer getNextId() throws SQLException {
		return readAllFlights().size() + 1;
	}
	
	public List<Flight> readAllFlights() throws SQLException {
		return read("select f.id, f.route_id, t.max_capacity, f.departure_time"
				+ " from flight as f join airplane as a on f.airplane_id = a.id "
				+ "join airplane_type as t on a.type_id = t.id", new Object[] {});
	}

	public Flight readFlightById(Integer id) throws SQLException {
		List<Flight> flights = read("select f.id, f.route_id, t.max_capacity, f.departure_time"
				+ " from flight as f join airplane as a on f.airplane_id = a.id "
				+ "join airplane_type as t on a.type_id = t.id where f.id = ?", new Object[] {id});
		if (flights.size() > 0)
			return flights.get(0);
		else
			return null;
	}
	
	public void deleteFlight(Flight flight) throws SQLException {
		Object[] id = {flight.getId()};
		save("delete from flight_seats where flight_id = ?", id);
		save("update booking as b join flight_bookings as fb on b.id = fb.booking_id and fb.flight_id = ? set is_active = 0", id);
		save("delete from flight_bookings where flight_id = ?", id);
		save("delete from flight where id = ?", id);
	}
	
	public void updateFlight(Flight flight) throws SQLException {
		save("delete from flight_bookings where flight_id = ?", new Object[] {flight.getId()});
		save("update booking as b join flight_bookings as fb"
				+ "	on b.id = fb.booking_id"
				+ " and fb.flight_id = ?"
				+ " set b.is_active = 0;",
				new Object[] {flight.getId()});
		
		Integer sum = 0;
		for (SeatSection section : flight.getSeats()) {
			for (Ticket ticket : section.getReserved()) {
				save("update booking set is_active = 1 from booking where id = ?", new Object[] {ticket.getId()});
				save("insert into flight_bookings values (?, ?)", new Object[] {flight.getId(), ticket.getId()});
			}
			save("update flight_seats set capacity = ? from flight_seats join seat_class as s where flight_id = ? and s.name = ?",
					new Object[] {section.getTotalSeats(), flight.getId(), section.getClassification()});
			sum += section.getTotalSeats();
		}
		save("update flight set route_id = ?, airplane_id = ?, departure_time = ?, reserved_seats = ?",
				new Object[] {flight.getRoute().getId(), flight.getPlane().getId(), flight.getStartTime().toString().replace("T", " "), sum});
	}
	
	public Integer addFlight(Flight flight) throws SQLException {
		flight.setId(getNextId());
		int seatSum = 0;
		for (SeatSection section : flight.getSeats()) {
			seatSum += section.getTotalSeats();
		}
		save("insert into flight values (?, ?, ?, ?, ?, ?)", new Object[] {flight.getId(), flight.getRoute().getId(), flight.getPlane().getId(),
				flight.getStartTime().toString().replace("T", " "), seatSum, 399.99});
		return flight.getId();
	}
	
	
	private class FlightSeatDAO extends BaseDAO<SeatSection> {

		public FlightSeatDAO(Connection conn) {
			super(conn);
			// TODO Auto-generated constructor stub
		}

		@Override
		public List<SeatSection> extractData(ResultSet rs) throws SQLException {
			List<SeatSection> sections = new ArrayList<SeatSection>();
			while (rs.next()) {
				SeatSection section = new SeatSection();
				section.setClassification(rs.getString("class"));
				section.setTotalSeats(Integer.parseInt(rs.getString("capacity")));
				sections.add(section);
			}
			return sections;
		}
		
		public List<SeatSection> findSectionsByFlightID(Integer flightID) throws SQLException {
			return read("select s.name as class, fs.capacity from flight_seats as fs join seat_class as s where fs.flight_id = ? AND fs.seat_id = s.id",
					new Object[] {flightID});
		}	
	}
}
