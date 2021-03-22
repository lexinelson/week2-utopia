package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.model.Flight;
import com.ss.utopia.model.Route;

public class RouteDAO extends BaseDAO<Route> {

	public RouteDAO(Connection conn) {
		super(conn);
	} 

	@Override
	public List<Route> extractData(ResultSet rs) throws SQLException {
		List<Route> routes = new ArrayList<Route>();
		AirportDAO airportReader = new AirportDAO(conn);
		
		while (rs.next()) {
			Route route = new Route();
			route.setId(Integer.parseInt(rs.getString("id")));
			route.setOrigin(airportReader.getAirportByCode(rs.getString("origin_id")));
			route.setDestination(airportReader.getAirportByCode(rs.getString("destination_id")));
			route.setDuration(Duration.ofHours(Long.parseLong(rs.getString("hours"))));
			routes.add(route);
		}
		return routes;
	}
	
	public Integer getNextID() throws SQLException {
		return readAllRoutes().size() + 1;
	}
	
	public void addRoute(Route route) throws SQLException {
		route.setId(getNextID());
		save("insert into route values (?, ?, ?)", 
				new Object[] {route.getId(), route.getOrigin().getCode(), route.getDestination().getCode()});
		save("insert into route_duration values (?, ?)",
				new Object[] {route.getId(), route.getDuration().toHours()});
	}
	
	public List<Route> readAllRoutes() throws SQLException {
		return read("select r.id, r.origin_id, r.destination_id, d.hours from route as r join route_duration as d"
				+ " on r.id = d.route_id", new Object[] {});
	}
	
	public Route readRouteByID(Integer id) throws SQLException {
		List<Route> routes = read("select r.id, r.origin_id, r.destination_id, d.hours from route as r join route_duration as d"
				+ " on r.id = d.route_id where r.id = ?", new Object[] {id});
		if (routes.size() > 0)
			return routes.get(0);
		else return null;
	}
	
	public Route readRouteByFlightId(Flight flight) throws SQLException {
		List<Route> result = read("select r.id, r.origin_id, r.destination_id, d.hours from route as r" +
				" join route_duration as d on r.id = d.route_id join flight on r.id = flight.route_id" +
				" where flight.id = ?", new Object[] {flight.getId()});
		if (result.size() > 0)
			return result.get(0);
		else return null;
	}
	
	public void updateRoute(Route route) throws SQLException {
		save("update route set origin_id = ?, destination_id = ? where id = ?", 
				new Object[] {route.getOrigin().getCode(), route.getDestination().getCode(), route.getId()});
		save("update route_duration set hours = ? where route_id = ?", 
				new Object[] {route.getDuration().toHours(), route.getId()});
	}
	
	public void deleteRoute(Route route) throws SQLException {
		Object[] id = {route.getId()};
		save("delete from route_duration where route_id = ?", id);
		save("delete from route where id = ?", id);
	}
}
