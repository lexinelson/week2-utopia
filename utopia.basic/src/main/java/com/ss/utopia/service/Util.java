package com.ss.utopia.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import com.ss.utopia.model.Airport;
import com.ss.utopia.model.Flight;
import com.ss.utopia.model.Route;
import com.ss.utopia.model.Ticket;

public class Util {
	private final String driver = "com.mysql.cj.jdbc.Driver";
	private final String credPath = "C:\\Work\\credentials.txt";
	
	private String url, username, password;

	public Util() throws FileNotFoundException {
		File creds = new File(credPath);
		Scanner scan = new Scanner(creds);
		url = scan.nextLine();
		username = scan.nextLine();
		password = scan.nextLine();
		scan.close();
	}
	
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, username, password);
		conn.setAutoCommit(Boolean.FALSE);
		return conn;
	}

	public String displayFlight(Flight flight) {
		StringBuffer display = new StringBuffer("Departure Airport: " + displayAirport(flight.getRoute().getOrigin()) + " | " +
				"Arival Airport: " + displayAirport(flight.getRoute().getDestination()) + "\n" +
				"Departure Date: " + flight.getStartTime().toLocalDate().toString() + " | " +
				"Arrival Date: " + flight.getEndTime().toLocalDate().toString() + "\n" +
				"Departure Time: " + flight.getStartTime().toLocalTime().toString() + " | " +
				"Arrival Time: " + flight.getEndTime().toLocalTime().toString() + "\n\n" +
				"Available Seats by Class:\n");
		for (int i = 1; i <= 3; i++) {
			String seatClass = null;
			switch (i) {
			case 1:
				seatClass = "First";
				break;
			case 2:
				seatClass = "Business";
				break;
			case 3:
				seatClass = "Economy";
				break;
			} 
			if (flight.getSeats()[i-1] != null)
				display.append(i + ") "+seatClass+" -> " + flight.getSeats()[i-1] + "\n");
			else
				display.append(i + ") "+seatClass+" -> 0\n");
		}
		return display.toString();
	}
	
	public String displayAirport(Airport airport) {
		return airport.getCode() + ", " + airport.getCity();
	}
	
	public String displayRoute(Route route) {
		return displayAirport(route.getOrigin()) + " -> " + displayAirport(route.getDestination());
	}
	
	public String displayTicket(Ticket ticket) {
		return ticket.getConfirmationCode() + ": " + ticket.getPassengerName();
	}
}
