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
		AirplaneDAO planeReader = new AirplaneDAO(conn);
		FlightSeatDAO sectionReader = new FlightSeatDAO(conn);
		TicketDAO ticketReader = new TicketDAO(conn);
		
		while (rs.next()) {
			Flight flight = new Flight();
			flight.setId(Integer.parseInt(rs.getString("id")));
			flight.setRoute(routeReader.readRouteByID(Integer.parseInt(rs.getString("route_id"))));
			flight.setPlane(planeReader.readPlaneByID(Integer.parseInt(rs.getString("airplane_id"))));
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
	
	public List<Flight> readAllFlights() throws SQLException {
		return read("select * from flight", new Object[] {});
	}

	public Flight readFlightById(Integer id) throws SQLException {
		List<Flight> flights = read("select * from flight where id = ?", new Object[] {id});
		if (flights.size() > 0)
			return flights.get(0);
		else
			return null;
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
