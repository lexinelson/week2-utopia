package com.ss.utopia.service;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.model.Flight;
import com.ss.utopia.model.Ticket;

public class GuestService {

	private AdminService privelage = new AdminService();
	private Util utility;
	
	public List<String> viewFlights(List<Flight> flights) throws FileNotFoundException, SQLException {
		utility = new Util();
		List<String> display = new ArrayList<String>();
		for (Flight flight : flights) {
			display.add(flight.getId() + ") "+utility.displayRoute(flight.getRoute()));
		}
		return display;
	}
	
	public String displayFlight(Flight f) {
		return utility.displayFlight(f); 
	} 
	
	public List<String> viewBookedFlights(Ticket ticket) throws FileNotFoundException, SQLException {
		List<Flight> flights = getBookedFlights(ticket);
		return viewFlights(flights);
	}
	
	public List<Flight> getBookedFlights(Ticket ticket) throws FileNotFoundException, SQLException {
		List<Flight> flights = privelage.readFlights();
		for (Flight flight : flights) {
			if (!ticket.getFlightId().equals(flight.getId()))
				flights.remove(flight);
		}
		return flights;
	}
	
	public List<String> viewPossibleFlights(Ticket ticket) throws FileNotFoundException, SQLException {
		List<Flight> flights = getPossibleFlights(ticket);
		return viewFlights(flights);
	}
	
	public List<Flight> getPossibleFlights(Ticket ticket) throws FileNotFoundException, SQLException {
		List<Flight> flights = privelage.readFlights();
		for (int i = 0; i < flights.size(); i++) {
			Flight flight = flights.get(i);
			if (ticket.getFlightId().equals(flight.getId()))
				flights.remove(i);
		}
		return flights;
	}
	
	public Flight fetchFlight(Integer id) throws FileNotFoundException, SQLException {
		return privelage.flightById(id);
	}
	
	public void cancelTicket(Ticket ticket) throws FileNotFoundException, SQLException {
		ticket.setActive(false);
		privelage.updateTicket(ticket); 
	}
	
	public void bookTicket(Flight flight, Ticket ticket)
			throws FileNotFoundException, SQLException {
		ticket.setFlightId(flight.getId());
		ticket.setActive(true);
		privelage.addFlightToTicket(ticket);
	}
	
	public String generateCode() {
		StringBuffer code = new StringBuffer();
		for (int i = 0; i < 10; i++) {
			code.append((char) (40 + (int)(Math.random() * 30)));
		}
		return code.toString();
	}
	
	public Ticket findTicket(String confirmation) throws FileNotFoundException, SQLException {
		return privelage.ticketsByConfirmation(confirmation);
	}
	
	public Ticket findTicket(Integer id) throws FileNotFoundException, SQLException {
		List<Ticket> tickets = privelage.readTickets();
		for (Ticket tick : tickets) {
			if (tick.getPassengerId().equals(id)) {
				return tick;
			}
		}
		return null;
	}
}
