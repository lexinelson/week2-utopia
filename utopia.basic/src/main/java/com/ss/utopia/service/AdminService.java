package com.ss.utopia.service;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.dao.AirportDAO;
import com.ss.utopia.dao.FlightDAO;
import com.ss.utopia.dao.RouteDAO;
import com.ss.utopia.dao.TicketDAO;
import com.ss.utopia.model.Airport;
import com.ss.utopia.model.Flight;
import com.ss.utopia.model.Route;
import com.ss.utopia.model.Ticket;

public class AdminService {
	
	private Util util;
	private Connection conn=null;
	
	public List<String> view(Flight[] flights) throws FileNotFoundException, SQLException {
		util = new Util();
		List<String> display = new ArrayList<String>();
		for (Flight flight : flights) {
			display.add(flight.getId() + ") "+util.displayRoute(flight.getRoute()));
		}
		return display;
	}
	
	public String displayFlight(Flight f) {
		return util.displayFlight(f); 
	} 
	
	public List<String> view(Airport[] airports) throws FileNotFoundException, SQLException {
		util = new Util();
		List<String> display = new ArrayList<String>();
		for (Airport airport : airports) {
			display.add(util.displayAirport(airport));
		}
		return display;
	}
	
	public List<String> view(Ticket[] tickets) throws FileNotFoundException, SQLException {
		util = new Util();
		List<String> display = new ArrayList<String>();
		for (Ticket ticket : tickets) {
			display.add(ticket.getId() + ") "+util.displayTicket(ticket));
		}
		return display;
	}
	
	public void addFlight(Flight flight) throws FileNotFoundException, SQLException {
		FlightDAO dao;
		util = new Util();
		try { 
			conn = util.getConnection();
			dao = new FlightDAO(conn);
			dao.addFlight(flight);
			conn.commit();
		} catch (Exception e) {
			System.out.println("Something went wrong adding the flight.");
		} finally {
			if (conn != null) 
				conn.close();
		}
		
	}
	
	public void addAirport(Airport airport) throws FileNotFoundException, SQLException {
		AirportDAO dao;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new AirportDAO(conn);
			dao.addAirport(airport);
			conn.commit();
		} catch (Exception e) {
			System.out.println("Something went wrong adding the airport.");
		} finally {
			if (conn != null)
				conn.close();
		}	
	}
	
	public void addRoute(Route route) throws FileNotFoundException, SQLException {
		RouteDAO dao;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new RouteDAO(conn);
			dao.addRoute(route);
			conn.commit();
		} catch (Exception e) {
			System.out.println("Something went wrong adding the route.");
		} finally {
			if (conn != null)
				conn.close();
		}	
	}
	
	public void addTicket(Ticket ticket) throws FileNotFoundException, SQLException {
		TicketDAO dao;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new TicketDAO(conn);
			dao.bookNewFlight(ticket);
			conn.commit();
		} catch (Exception e) {
			System.out.println("Something went wrong adding the ticket.");
		} finally {
			if (conn != null)
				conn.close();
		}	
	}
	
	public List<Flight> readFlights() throws FileNotFoundException, SQLException {
		FlightDAO dao;
		List<Flight> flights=null;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new FlightDAO(conn);
			flights = dao.readAllFlights();
			conn.commit();
		} catch (Exception e) {
			System.out.println("Error: something went wrong accessing the flights.");
		} finally {
			if (conn != null)
				conn.close();
		}	
		return flights;
	}
	
	public List<Route> readRoutes() throws FileNotFoundException, SQLException {
		RouteDAO dao;
		List<Route> routes=null;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new RouteDAO(conn);
			routes = dao.readAllRoutes();
			conn.commit();
		} catch (Exception e) {
			System.out.println("Error: something went wrong accessing routes");
		} finally {
			if (conn != null)
				conn.close();
		}	
		return routes;
	}
	
	public List<Airport> readAirports() throws FileNotFoundException, SQLException {
		AirportDAO dao;
		List<Airport> airports=null;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new AirportDAO(conn);
			airports = dao.getAllAirports();
			conn.commit();
		} catch (Exception e) {
			System.out.println("Error: something went wrong accessing the airports.");
		} finally {
			if (conn != null)
				conn.close();
		}	
		return airports;
	}
	
	public List<Ticket> readTickets() throws FileNotFoundException, SQLException {
		TicketDAO dao;
		List<Ticket> tickets=null;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new TicketDAO(conn);
			tickets = dao.readAllTickets();
			conn.commit();
		} catch (Exception e) {
			System.out.println("Error: something went wrong accessing the tickets.");
		} finally {
			if (conn != null)
				conn.close();
		}	
		return tickets;
	}
	
	public List<Ticket> readCancellations() throws FileNotFoundException, SQLException {
		List<Ticket> ticks = readTickets();
		List<Ticket> tickets = ticks;
		System.out.println(ticks.size());
		for (int i = 0; i < tickets.size(); i++) {
			if (tickets.get(i).isActive())
				ticks.remove(i);
		}
		return ticks;
	}
	
	public Flight flightById(Integer id) throws FileNotFoundException, SQLException {
		List<Flight> flights = readFlights();
		for (Flight flight : flights) {
			if (id.equals(flight.getId()))
				return flight;
		}
		return null;
	}
	
	public Route routeById(Integer id) throws FileNotFoundException, SQLException {
		List<Route> routes = readRoutes();
		for (Route route : routes) {
			if (id.equals(route.getId()))
				return route;
		}
		return null;
	}
	
	public Route routeByAirports(Route route) throws FileNotFoundException, SQLException {
		List<Route> routes = readRoutes();
		for (Route r : routes) {
			if (route.getDestination().getCode().equals(r.getDestination().getCode()) &&
					route.getOrigin().getCode().equals(r.getOrigin().getCode()))
				return r;
		}
		return null;
	}
	
	public Airport airportByCode(String code) throws FileNotFoundException, SQLException {
		List<Airport> airports = readAirports();
		for (Airport airport : airports) {
			if (code.equals(airport.getCode()))
				return airport;
		}
		return null;
	}
	
	public Ticket ticketsByConfirmation(String confirmation) throws FileNotFoundException, SQLException {
		List<Ticket> tickets = readTickets();
		for (Ticket ticket : tickets) {
			if (confirmation.equals(ticket.getConfirmationCode())) {
				return ticket;
			}
		}
		return null;
	}
	
	public void ticketCancellation(Ticket ticket) throws FileNotFoundException, SQLException {
		TicketDAO dao;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new TicketDAO(conn);
			dao.cancelBooking(ticket);
			conn.commit();
		} catch (Exception e) {
			System.out.println("Something went wrong cancelling the ticket.");
		} finally {
			if (conn != null)
				conn.close();
		}
	}
	
	public void addFlightToTicket(Ticket ticket) throws FileNotFoundException, SQLException {
		TicketDAO dao;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new TicketDAO(conn);
			dao.bookNewFlight(ticket);
			conn.commit();
		} catch (Exception e) {
			System.out.println("Something went wrong adding the flight.");
		} finally {
			if (conn != null)
				conn.close();
		}
	}
	
	public void updateFlight(Flight flight) throws FileNotFoundException, SQLException {
		FlightDAO dao;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new FlightDAO(conn);
			dao.updateFlight(flight);
			conn.commit();
		} catch (Exception e) {
			System.out.println("Something went wrong changing the flight.");
		} finally {
			if (conn != null)
				conn.close();
		}
		
	}
	
	public void updateAirport(Airport airport) throws FileNotFoundException, SQLException {
		AirportDAO dao;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new AirportDAO(conn);
			dao.updateAirport(airport);
			conn.commit();
		} catch (Exception e) {
			System.out.println("Something went wrong changing the airport.");
		} finally {
			if (conn != null)
				conn.close();
		}	
	}
	
	public void updateRoute(Route route) throws FileNotFoundException, SQLException {
		RouteDAO dao;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new RouteDAO(conn);
			dao.updateRoute(route);
			conn.commit();
		} catch (Exception e) {
			System.out.println("Something went wrong changing the route.");
		} finally {
			if (conn != null)
				conn.close();
		}	
	}
	
	public void updateTicket(Ticket ticket) throws FileNotFoundException, SQLException {
		TicketDAO dao;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new TicketDAO(conn);
			dao.updateTicket(ticket);
			conn.commit();
		} catch (Exception e) {
			System.out.println("Something went wrong changing the ticket.");
		} finally {
			if (conn != null)
				conn.close();
		}	
	}
	
	public void deleteFlight(Flight flight) throws FileNotFoundException, SQLException {
		FlightDAO dao;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new FlightDAO(conn);
			dao.deleteFlight(flight);
			conn.commit();
		} catch (Exception e) {
			System.out.println("Something went wrong deleting the flight.");
		} finally {
			if (conn != null)
				conn.close();
		}
		
	}
	
	public void deleteAirport(Airport airport) throws FileNotFoundException, SQLException {
		AirportDAO dao;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new AirportDAO(conn);
			dao.deleteAirport(airport);
			conn.commit();
		} catch (Exception e) {
			System.out.println("Something went wrong deleting the airport.");
		} finally {
			if (conn != null)
				conn.close();
		}	
	}
	
	public void deleteRoute(Route route) throws FileNotFoundException, SQLException {
		RouteDAO dao;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new RouteDAO(conn);
			dao.deleteRoute(route);
			conn.commit();
		} catch (Exception e) {
			System.out.println("Something went wrong deleting the route.");
		} finally {
			if (conn != null)
				conn.close();
		}	
	}
	
	public void deleteTicket(Ticket ticket) throws FileNotFoundException, SQLException {
		TicketDAO dao;
		util = new Util();
		try {
			conn = util.getConnection();
			dao = new TicketDAO(conn);
			dao.deleteTicket(ticket);
			conn.commit();
		} catch (Exception e) {
			System.out.println("Something went wrong deleting the ticket.");
		} finally {
			if (conn != null)
				conn.close();
		}	
	}
	
}

