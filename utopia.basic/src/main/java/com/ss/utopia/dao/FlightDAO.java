package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.model.Flight;

public class FlightDAO extends BaseDAO<Flight> {

	public FlightDAO(Connection conn) {
		super(conn);
	}

	@Override
	public List<Flight> extractData(ResultSet rs) throws SQLException {
		List<Flight> flights = new ArrayList<Flight>();
		RouteDAO routeReader = new RouteDAO(conn);
		
		while (rs.next()) {
			Flight flight = new Flight();
			flight.setId(rs.getInt("id"));
			flight.setMaxCapacity(rs.getInt("max_capacity"));
			flight.setRoute(routeReader.readRouteByID(rs.getInt("route_id")));
			
			flight.setStartTime(LocalDateTime.parse(rs.getString("departure_time").replace(" ", "T")));
			flight.setEndTime(flight.getStartTime().plus(flight.getRoute().getDuration()));
			
			Integer[] seats = new Integer[3];
			
			for (int i = 0; i < seats.length; i++) {
				ResultSet rs2 = prepare("select capacity from flight_seats where flight_id = ? and seat_id = ?",
						new Object[] {flight.getId(), i+1}).executeQuery();
				while (rs2.next()) {
					seats[i] = rs2.getInt("capacity");
				}
			}
			flight.setSeats(seats);
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
		save("update booking as b join flight_bookings as fb on "
				+ "b.id = fb.booking_id and fb.flight_id = ? set is_active = 0", id);
		save("delete from flight where id = ?", id);
	}
	
	public void updateFlight(Flight flight) throws SQLException {
		for (int i = 0; i < 3; i++) {
			save("update flight_seats set capacity = ? where flight_id = ? and seat_id = ?", 
					new Object[] {flight.getSeats()[i], flight.getId(), i + 1});
		}
		
		Integer seatsTaken = 0;
		ResultSet rs = prepare("select * from flight_bookings where flight_id = ?", 
				new Object[] {flight.getId()}).executeQuery();
		while(rs.next()) {
			seatsTaken++;
		}
		
		save("update route_duration set hours = ? where route_id = ?",
				new Object[] {flight.getRoute().getDuration().toHours(), flight.getRoute().getId()});
		save("update flight set route_id = ?, departure_time = ?, reserved_seats = ? where id = ?", 
				new Object[] {flight.getRoute().getId(), flight.getStartTime().toString().replace(" ", "T"),
						seatsTaken, flight.getId()});
	}
	
	public void addFlight(Flight flight) throws SQLException {
		save("insert into flight values (?, ?, 5, ?, 0, 400.00)",
				new Object[] {flight.getId(), flight.getRoute().getId(), 
						flight.getStartTime().toString().replace("T", " ")});
	}
}
