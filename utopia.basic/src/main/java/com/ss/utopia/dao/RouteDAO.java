package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.model.Route;

public class RouteDAO extends BaseDAO<Route> {

	public RouteDAO(Connection conn) {
		super(conn);
	}

	@Override
	public List<Route> extractData(ResultSet rs) throws SQLException {
		List<Route> routes = new ArrayList<Route>();
		AirportDAO airportReader = new AirportDAO(conn);
		DurationDAO durationReader = new DurationDAO(conn);
		
		while (rs.next()) {
			Route route = new Route();
			route.setId(Integer.parseInt(rs.getString("id")));
			route.setOrigin(airportReader.readAirportByCode(rs.getString("origin_id")));
			route.setDestination(airportReader.readAirportByCode(rs.getString("destination_id")));
			route.setDuration(durationReader.readDuration(route.getId()));
			routes.add(route);
		}
		return routes;
	}
	
	public Integer getNextID() throws SQLException {
		return readAll("route").size() + 1;
	}
	
	public void addRoute(Route route) throws SQLException {
		save("insert into route values (?, ?)", 
				new Object[] {route.getOrigin(), route.getDestination()});
		save("insert into route_duration values (?, ?)",
				new Object[] {getNextID(), route.getDuration().toHours()});
	}
	
	public List<Route> readAllRoutes() throws SQLException {
		return readAll("route");
	}
	
	public Route readRouteByID(Integer id) throws SQLException {
		return read("select * from route where id = ?", new Object[] {id}).get(0);
	}
	
	public void updateRoute(Route route) throws SQLException {
		save("update route set origin_id = ?, desination_id = ? where id = ?", 
				new Object[] {route.getOrigin().getCode(), route.getDestination().getCode(), route.getId()});
		save("update route_duration set hours = ? where route_id = ?", 
				new Object[] {route.getDuration().toHours(), route.getId()});
	}
	
	public void deleteRoute(Route route) throws SQLException {
		Object[] id = {route.getId()};
		save("delete from route_duration where route_id = ?", id);
		save("delete from route where route_id = ?", id);
	}
	
	

	private class DurationDAO extends BaseDAO<Duration> {

		public DurationDAO(Connection conn) {
			super(conn);
		}

		@Override
		public List<Duration> extractData(ResultSet rs) throws SQLException {
			List<Duration> durations = new ArrayList<Duration>();
			while (rs.next()) {
				Duration duration = Duration.ofHours(Long.parseLong(rs.getString("hours")));
				durations.add(duration);
			}
			return durations;
		}
		
		public Duration readDuration(Integer routeID) throws SQLException {
			return read("select hours from route_duration where route_id = ?",
					new Object[] {routeID}).get(0);
		}
		
	}
}
