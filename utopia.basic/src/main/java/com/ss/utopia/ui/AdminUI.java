package com.ss.utopia.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import javax.print.attribute.standard.Destination;

import com.ss.utopia.model.Airport;
import com.ss.utopia.model.Flight;
import com.ss.utopia.model.Route;
import com.ss.utopia.service.AdminService;
import com.ss.utopia.service.Util;

public class AdminUI {

	private Util utility;
	private AdminService service = new AdminService();
	private Scanner scan;
	
	public void menuOne(Scanner scanner) throws FileNotFoundException, SQLException {
		scan = scanner;
		menuOne();
	} 
	
	public void menuOne() throws FileNotFoundException, SQLException {
		System.out.println("\n\nSELECT AN ADMIN FUNCTION?");
		System.out.println("1) Add/Update/Delete/Read/Adjust Seats Flights");
		System.out.println("2) Add/Update/Delete/Read Tickets");
		System.out.println("3) Add/Update/Delete/Read Airports");
		System.out.println("4) Over-ride a Cancellation");
		System.out.println("5) Leave Admin Center");
		Integer input = null;
		try {
			input = scan.nextInt();
			switch (input) {
			case 1:
				selectFlight(service.readFlights());
				break;
			case 2:
				ticketMenu();
				break;
			case 3:
				airportMenu();
				break;
			case 4:
				cancelMenu();
				break;
			case 5:
				return;
			default:
				selectionError("one");
			}
		} catch (Exception e) {
			String text = scan.nextLine().toLowerCase();
			if (text.contains("flight"))
				selectFlight(service.readFlights());
			else if (text.contains("quit"))
				return;
			else if (text.contains("ticket"))
				ticketMenu();
			else if (text.contains("airport"))
				airportMenu();
			else if (text.contains("cancel"))
				cancelMenu();
			else 
				selectionError("one");
		}
		menuOne();
	}
	
	public void errorMessage() {
		System.out.println("\n\n");
		System.out.println("Please enter one of the given options");
	}
	
	public void selectionError(String menu) throws FileNotFoundException, SQLException {
		errorMessage();
		if ("one".equals(menu))
			menuOne();
		else if ("flight".equals(menu))
			selectFlight(service.readFlights());
	}
	
	public void menuError(Flight flight) throws FileNotFoundException, SQLException {
		errorMessage();
		flightMenu(flight);
	}
	
	public void selectFlight(List<Flight> flights) throws FileNotFoundException, SQLException {
		List<String> flightStr = service.view(flights.toArray(new Flight[flights.size()]));
		
		System.out.println("\n\n");
		System.out.println("0) CREATE NEW FLIGHT");
		if(flights != null) {
			for (String flight : flightStr) {
				System.out.println(flight);
			}
		}

		Integer input = null;
		System.out.print("Please make a selection or enter 'quit' to return to menu: ");
		String in=null;
		try {
			in = scan.next();
			input = Integer.parseInt(in);
			Flight flight = service.flightById(input);
			if (flight != null) {
				flightMenu(flight);
			}
			else if (input == 0)
				updateFlight(flight);
			else {
				selectionError("flight");
			}
		} catch (NumberFormatException e) {
			if ("quit".equals(in))
				menuOne();
			else
				selectionError("flight");
		}
	}
	
	public void flightMenu(Flight flight) throws FileNotFoundException, SQLException {
		System.out.println();
		System.out.println("1) Read flight");
		System.out.println("2) Update flight");
		System.out.println("3) Update flight seats");
		System.out.println("4) Delete flight");
		System.out.println("5) Quit to previous");
		System.out.print("\nEnter the number corresponding to what you'd like to do: ");
		Integer input = null;
		boolean quit = false;
		try {
			input = scan.nextInt();
			scan.nextLine();
			switch (input) {
			case 1:
				displayFlight(flight);
				break;
			case 2:
				updateFlight(flight);
				break;
			case 3:
				adjustSeats(flight);
				break;
			case 4:
				
			case 5:
				quit = true;
				break;
			default:
				menuError(flight);
			}
		} catch (Exception e) {
			menuError(flight);
		} 
		if (quit)
			selectFlight(service.readFlights());
		else 
			flightMenu(flight);
	}
	
	public void displayFlight(Flight flight) {
		System.out.println(service.displayFlight(flight));
		System.out.println("Press enter to return to previous");
		try {
			System.in.read();
		} catch (IOException e) {
			System.out.println("Something is wrong with the console!");
			System.out.println("Please restart the program and try again");
			while (true) {}
		}

	}
	
	public void inputError(String block) throws FileNotFoundException, SQLException {
		System.out.println("Input was not valid. Please press enter to try again.");
		if ("flight".equals(block))
			updateFlight(new Flight());
	}
	
	public void updateFlight(Flight flight) throws FileNotFoundException, SQLException {
		Flight copy=null;
		if (flight == null) {
			flight = new Flight();
			flight.setRoute(new Route());
			System.out.println("\n\nCREATE FLIGHT");
		}
		else {
			copy = flight;
			System.out.println("\n\nYou have chosen to update the flight with id: " + flight.getId() +
					" and Origin: " + service.view(new Airport[] {flight.getRoute().getOrigin()}) +
					" and Destination: " + service.view(new Airport[] {flight.getRoute().getOrigin()}));
		}
		System.out.println("Enter 'quit' at any prompt to cancel operation");
		
		StringBuffer input = new StringBuffer();
		
		System.out.print("Please enter Destination Airport and City");
		if (flight.getId() != null) {
			System.out.print(" or enter N/A for no change");
		}
		System.out.println(":");
		input.append(scan.nextLine());
		if (input.toString().equals("quit")) {
			return;
		} else if (input.toString().equals("N/A")) {
			if (flight.getId() == null)
				inputError("flight");
		}
		else {
			Airport destination = new Airport();
			destination.setCode(input.toString().split(", ")[0]);
			destination.setCity(input.toString().split(", ")[1]);
			if (!service.readAirports().contains(destination))
				service.addAirport(destination);
			flight.getRoute().setDestination(destination);
		}
		
		System.out.print("Please enter Departure Airport and City");
		if (flight.getId() != null) {
			System.out.print(" or enter N/A for no change");
		}
		System.out.println(":");
		input.append(scan.nextLine());
		if (input.toString().equals("quit")) {
			return;
		} else if (input.toString().equals("N/A")) {
			if (flight.getId() == null)
				inputError("flight");
		}
		else {
			Airport departure = new Airport();
			departure.setCode(input.toString().split(", ")[0]);
			departure.setCity(input.toString().split(", ")[1]);
			if (!service.readAirports().contains(departure))
				service.addAirport(departure);
			flight.getRoute().setOrigin(departure);
		}
		
		
		if (flight.getId() == null) {
			flight.setStartTime(LocalDateTime.parse("1990-01-01T00:00:01"));
			flight.setEndTime(LocalDateTime.parse("1990-01-01T00:00:02"));
		}
		
		
		input.delete(0, input.length());
		System.out.print("Please enter Departure Date");
		if (flight.getId() != null) {
			System.out.print(" or enter N/A for no change");
		}
		System.out.println(":");
		if (input.toString().equals("quit")) {
			flight = copy;
			return;
		} else if (input.toString().equals("N/A")) {}
		else {
			flight.setStartTime(LocalDateTime.of(LocalDate.parse(input), flight.getStartTime().toLocalTime()));
 		}
		
		input.delete(0, input.length());
		System.out.println("Please enter new Departure Time or enter N/A for no change:");
		input.append(scan.nextLine());
		if (input.toString().equals("quit")) {
			flight = copy;
			return;
		} else if (input.toString().equals("N/A")) {}
		else {
			flight.setStartTime(LocalDateTime.of(flight.getStartTime().toLocalDate(), LocalTime.parse(input)));
		}
		
		input.delete(0, input.length());
		System.out.println("Please enter new Arrival Date or enter N/A for no change:");
		input.append(scan.nextLine());
		if (input.toString().equals("quit")) {
			flight = copy;
			return;
		} else if (input.toString().equals("N/A")) {}
		else {
			flight.setEndTime(LocalDateTime.of(LocalDate.parse(input), flight.getEndTime().toLocalTime()));
 		}
		
		input.delete(0, input.length());
		System.out.println("Please enter new Arrival Time or enter N/A for no change:");
		input.append(scan.nextLine());
		if (input.toString().equals("quit")) {
			flight = copy;
			return;
		} else if (input.toString().equals("N/A")) {}
		else {
			flight.setEndTime(LocalDateTime.of(flight.getEndTime().toLocalDate(), LocalTime.parse(input)));
		}
		
		service.updateFlight(flight);
		System.out.println("Changes made were successful!");
	}
	
	public void readFlight(Flight flight) {
		System.out.println(service.displayFlight(flight));
		System.out.println("Press enter to return to previous");
		try {
			System.in.read();
		} catch (IOException e) {
			System.out.println("Something is wrong with the console!");
			System.out.println("Please restart the program and try again");
			while (true) {}
		}
	}
	
	public void adjustSeats(Flight flight) throws FileNotFoundException, SQLException {
		EmpUI borrow = new EmpUI();
		borrow.addSeats(flight);
	}
	
	public void ticketMenu() {
		
	}
	
	public void airportMenu() {
		
	}
	
	public void cancelMenu() {
		
	}
}
