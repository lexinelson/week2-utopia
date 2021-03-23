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
	
	public List<String> viewFlights() throws FileNotFoundException, SQLException {
		utility = new Util();
		List<Flight> flights = privelage.readFlights();
		List<String> display = new ArrayList<String>(); 
		for (Flight flight : flights) {
			display.add(flight.getId() + ") "+utility.displayRoute(flight.getRoute()));
		}
		return display;
	}
	
	public void cancelTicket(Ticket ticket) throws FileNotFoundException, SQLException {
		ticket.setActive(false);
		privelage.updateTicket(ticket);
	}
	
	public void bookTicket(Flight flight, String name, Integer id, Integer seat)
			throws FileNotFoundException, SQLException {
		Ticket ticket = new Ticket();
		ticket.setFlightId(flight.getId());
		ticket.setActive(true);
		ticket.setSeatId(seat);
		ticket.setPassengerName(name);
		ticket.setPassengerId(id);
		ticket.setConfirmationCode(generateCode());
		privelage.addTicket(ticket);
	}
	
	public String generateCode() {
		StringBuffer code = new StringBuffer();
		for (int i = 0; i < 10; i++) {
			code.append((char) (40 + (int)(Math.random() * 30)));
		}
		return code.toString();
	}
}
