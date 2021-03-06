package com.ss.utopia.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import com.ss.utopia.model.Airport;
import com.ss.utopia.model.Flight;
import com.ss.utopia.model.Route;
import com.ss.utopia.model.Ticket;
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
				errorMessage();
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
				errorMessage();
		}
		menuOne();
	}

	public void errorMessage() {
		System.out.println("\n\n");
		System.out.println("Please enter one of the given options");
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
				errorMessage();
			}
		} catch (NumberFormatException e) {
			if ("quit".equals(in))
				return;
			else
				errorMessage();
		}
		selectFlight(service.readFlights());
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
				service.deleteFlight(flight);
				System.out.println("Flight deleted\n\n");
				quit = true;
				break;
			case 5:
				return;
			default:
				errorMessage();
			}
		} catch (Exception e) {
			errorMessage();
		} 
		if (quit)
			return;
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
		scan.nextLine();
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
			System.out.println("quit block");
			return;
		} else if (input.toString().equals("N/A")) {
			if (flight.getId() == null) {
				System.out.println("A new flight cannot be created without a destination airport");
				return;
			}
		}
		else {
			Airport destination = new Airport();
			destination.setCode(input.toString().split(", ")[0]);
			destination.setCity(input.toString().split(", ")[1]);
			if (!service.readAirports().contains(destination))
				service.addAirport(destination);
			flight.getRoute().setDestination(destination);
		}
		input.delete(0, input.length());
		System.out.print("Please enter Departure Airport and City");
		if (flight.getId() != null) {
			System.out.print(" or enter N/A for no change");
		}
		System.out.println(":");
		input.append(scan.nextLine());
		if (input.toString().equals("quit")) {
			return;
		} else if (input.toString().equals("N/A")) {
			if (flight.getId() == null) {
				System.out.println("A new flight cannot be created without a departure airport");
				return;
			}
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
			try {
				flight.setStartTime(LocalDateTime.parse("1990-01-01T00:00:01"));
				flight.setEndTime(LocalDateTime.parse("1990-01-01T00:00:02"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		input.delete(0, input.length());
		System.out.print("Please enter Departure Date");
		if (flight.getId() != null) {
			System.out.print(" or enter N/A for no change");
		}
		System.out.println(":");
		input.append(scan.nextLine());
		if (input.toString().equals("quit")) {
			flight = copy;
			return;
		} else if (input.toString().equals("N/A")) {
			if (flight.getId() == null) {
				System.out.println("A new flight cannot be created without a departure date");
				return;
			}
		}
		else {
			flight.setStartTime(LocalDateTime.of(LocalDate.parse(input), flight.getStartTime().toLocalTime()));
		}
		input.delete(0, input.length());
		System.out.println("Please enter new Departure Time or enter N/A for no change:");
		input.append(scan.nextLine());
		if (input.toString().equals("quit")) {
			flight = copy;
			return;
		}  else if (input.toString().equals("N/A")) {
			if (flight.getId() == null) {
				System.out.println("A new flight cannot be created without a departure time");
				return;
			}
		}
		else {
			flight.setStartTime(LocalDateTime.of(flight.getStartTime().toLocalDate(), LocalTime.parse(input)));
		}
		input.delete(0, input.length());
		System.out.println("Please enter new Arrival Date or enter N/A for no change:");
		input.append(scan.nextLine());
		if (input.toString().equals("quit")) {
			flight = copy;
			return;
		}  else if (input.toString().equals("N/A")) {
			if (flight.getId() == null) {
				System.out.println("A new flight cannot be created without an arrival date");
				return;
			}
		}
		else {
			flight.setEndTime(LocalDateTime.of(LocalDate.parse(input), flight.getEndTime().toLocalTime()));
		}
		System.out.println("Made it past arrival date entry");
		input.delete(0, input.length());
		System.out.println("Please enter new Arrival Time or enter N/A for no change:");
		input.append(scan.nextLine());
		if (input.toString().equals("quit")) {
			flight = copy;
			return;
		}  else if (input.toString().equals("N/A")) {
			if (flight.getId() == null) {
				System.out.println("A new flight cannot be created without an arrival time");
				return;
			}
		}
		else {
			flight.setEndTime(LocalDateTime.of(flight.getEndTime().toLocalDate(), LocalTime.parse(input)));
		}
		if (!service.readRoutes().contains(flight.getRoute()))
			service.addRoute(flight.getRoute());
		if (flight.getId() == null) {
			service.addFlight(flight);
			System.out.println("Flight added successfully");
		} else {
			service.updateFlight(flight);
			System.out.println("Changes made were successful!");
		}
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
		System.out.println("Pick the Seat Class you want to add seats of, to your flight:");
		System.out.println("1) First Class");
		System.out.println("2) Business Class");
		System.out.println("3) Economy Class");
		System.out.println("Enter quit at any time to cancel the operation");

		StringBuffer input = new StringBuffer();

		Integer seat = null;
		Boolean valid = true;
		do {
			input.append(scan.nextLine());
			try {
				seat = Integer.parseInt(input.toString());
				valid = true;
			} catch (NumberFormatException e) {
				if ("quit".equals(input.toString()))
					return;
				else {
					System.out.println("Please enter one of the given options or 'quit'\n");
					valid = false;
				}
			}
			input.delete(0, input.length());
		} while (!valid);

		Integer newSeats=null;
		do {
			Integer current = flight.getSeats()[seat-1];
			System.out.println("Existing Number of Seats: " + ((current!=null) ? current : 0));
			System.out.print("Enter a new number of seats: ");
			input.append(scan.nextLine());
			try {
				newSeats = Integer.parseInt(input.toString());
				int sum = 0;
				for (int i = 0; i < flight.getSeats().length; i++) {
					if (i != seat-1)
						sum += flight.getSeats()[i];
					else
						sum += newSeats;
				}
				valid = sum <= flight.getMaxCapacity();
				if (!valid) {
					System.out.println("\nSorry, the airplane for this flight has a maximum capacity of " + flight.getMaxCapacity());
					System.out.println("This entry goes over that by "+ (sum - flight.getMaxCapacity())+" seats.\n");
				}
			} catch (NumberFormatException e) {
				if ("quit".equals(input.toString()))
					return;
				else {
					System.out.println("Please enter a valid number of seats or 'quit'\n");
					valid = false;
				}
			}
		} while (!valid);
		if (newSeats != null) {
			flight.getSeats()[seat-1] = newSeats;
		}
		service.updateFlight(flight);	
	}

	public void ticketMenu() throws FileNotFoundException, SQLException {
		scan.nextLine();
		System.out.println("What would you like to do in Tickets?");
		System.out.println("1) Add New Ticket");
		System.out.println("2) Delete a ticket");
		System.out.println("3) Cancel a ticket");
		System.out.println("4) View tickets");
		String in = scan.nextLine();
		Integer input=null;
		try {
			input = Integer.parseInt(in);
		} catch (NumberFormatException e) {
			String text = in.toString().toLowerCase();
			if (text.contains("add"))
				input = 1;
			if (text.contains("delete"))
				input = 2;
			if (text.contains("cancel"))
				input = 3;
			if (text.contains("view"))
				input = 4;
		}
		boolean quit=true;
		switch(input) {
		case 1:
			Ticket t = new Ticket();
			System.out.print("\nADDING TICKET\n");
			System.out.print("Enter flight ID: ");
			t.setFlightId(scan.nextInt());
			scan.nextLine();
			System.out.print("Enter Passenger ID: ");
			t.setPassengerId(scan.nextInt());
			scan.nextLine();
			System.out.print("Enter Passenger Name: ");
			String name = scan.nextLine();
			if (!name.contains(" "))
				name += " LastName";
			System.out.print("Enter a unique confirmation code: ");
			t.setConfirmationCode(scan.nextLine());
			t.setActive(true);
			service.addTicket(t);
			break;
		case 2:
			Ticket ticket = selectTicket();
			if (ticket == null) return;
			service.deleteTicket(ticket);
			break;
		case 3:
			Ticket tick = selectTicket();
			if (tick == null) return;
			service.ticketCancellation(tick);
			break;
		case 4:
			readTicket(true);
			quit=false;
			break;
		default:
			System.out.println("Invalid input. Please try again\n");
			quit=false;
			return;
		}
		if (quit) return;
		else ticketMenu();
	}

	public void readTicket(boolean justRead) throws FileNotFoundException, SQLException {
		List<Ticket> tickets = service.readTickets();
		for (Ticket tick : tickets) {
			System.out.println(tick.getId() + ") "+tick.getConfirmationCode()+" - "+tick.getPassengerName());
		}
		if (justRead) {
			System.out.println("Please press enter to return");
			try {
				System.in.read();
			} catch (IOException e) {
				System.out.println("Something is wrong with the console!");
				System.out.println("Please restart the program and try again");
				while (true) {}
			}
		}
	}

	public Ticket selectTicket() throws FileNotFoundException, SQLException {
		readTicket(false);
		System.out.println("\nPlease make a selection by entering the confirmation code number, or 'quit' to return: ");
		String input = scan.nextLine();
		if (input.toLowerCase().contains("quit"))
			return null;
		Ticket ticket = service.ticketsByConfirmation(input);
		if (ticket == null) {
			System.out.println("Please enter only the confirmation number listed");
			return selectTicket();
		}
		else return ticket;
	}

	public void airportMenu() throws FileNotFoundException, SQLException {
		scan.nextLine();
		System.out.println("What would you like to do in Airports?");
		System.out.println("1) Add New Airport");
		System.out.println("2) Delete an airport");
		System.out.println("3) Update an airport");
		System.out.println("4) View airport");
		String in = scan.nextLine();
		Integer input=null;
		try {
			input = Integer.parseInt(in);
		} catch (NumberFormatException e) {
			String text = in.toString().toLowerCase();
			if (text.contains("add"))
				input = 1;
			if (text.contains("delete"))
				input = 2;
			if (text.contains("update"))
				input = 3;
			if (text.contains("view"))
				input = 4;
		}
		boolean quit=true;
		switch(input) {
		case 1:
			Airport a = new Airport();
			System.out.print("\nADDING AIRPORT\n");
			System.out.print("Enter three Character Code (XXX): ");
			a.setCode(scan.nextLine());
			System.out.print("Enter the city name for this airport: ");
			a.setCity(scan.nextLine());
			service.addAirport(a);
			break;
		case 2:
			Airport airport = selectAirport();
			if (airport == null) return;
			service.deleteAirport(airport);
			break;
		case 3:
			Airport aport = selectAirport();
			if (aport == null) return;
			System.out.println("\nPlease enter the airports new city:");
			aport.setCity(scan.nextLine());
			service.updateAirport(aport);
			break;
		case 4:
			readAirport(true);
			quit=false;
			break;
		default:
			System.out.println("Invalid input. Please try again\n");
			quit=false;
			return;
		}
		if (quit) return;
		else airportMenu();
	}

	public void readAirport(boolean justRead) throws FileNotFoundException, SQLException {
		List<Airport> airports = service.readAirports();
		for (Airport a : airports) {
			System.out.println(a.getCode() + " - " + a.getCity());
		}
		if(justRead) {
			System.out.println("Please press enter to return");
			try {
				System.in.read();
			} catch (IOException e) {
				System.out.println("Something is wrong with the console!");
				System.out.println("Please restart the program and try again");
				while (true) {}
			}
		}
	}

	public Airport selectAirport() throws FileNotFoundException, SQLException {
		readAirport(false);
		System.out.println("\nPlease make a selection by entering the three letter code, or 'quit' to return:");
		String input = scan.nextLine();
		if (input.toLowerCase().contains("quit"))
			return null;
		Airport airport = service.airportByCode(input);
		if (airport == null) {
			System.out.println("Please enter only the confirmation number listed");
			return selectAirport();
		}
		else return airport;
	}

	public Ticket selectCancellation() throws FileNotFoundException, SQLException {
		List<Ticket> tickets = service.readCancellations();
		for (Ticket tick : tickets) {
			System.out.println(tick.getId() + ") "+tick.getConfirmationCode()+" - "+tick.getPassengerName());
		}
		System.out.println("\nPlease make a selection by entering the confirmation code number, or 'quit' to return: ");
		String input = scan.nextLine();
		if (input.toLowerCase().contains("quit"))
			return null;
		Ticket ticket = service.ticketsByConfirmation(input);
		if (ticket == null) {
			System.out.println("Please enter only the confirmation number listed");
			return selectCancellation();
		}
		else return ticket;
	}

	public void cancelMenu() throws FileNotFoundException, SQLException {
		scan.nextLine();
		System.out.println("PLEASE SELECT A CANCELLATION TO OVERRIDE");
		Ticket ticket = selectCancellation();
		if (ticket == null) return;
		System.out.println("Enter a flight id to book this ticket on: ");
		Boolean isValid = true;
		String input;
		Integer id=null;
		do {
			input = scan.nextLine();
			try {
				id = Integer.parseInt(input);
				Flight flight = service.flightById(id);
				if (flight != null)
					isValid = true;
				else {
					System.out.println("Invalid input. Please try again or enter quit to go back");
					isValid = false;
				}
			} catch (NumberFormatException e) {
				if ("quit".equals(input))
					return;
				else {
					System.out.println("Invalid input. Please try again or enter quit to go back");
					isValid = false;
				}
			}
		} while (!isValid);
		ticket.setFlightId(id);
		ticket.setActive(true);
		System.out.println("Ticket successfully reactivated");
		service.addFlightToTicket(ticket);
		service.updateTicket(ticket);
	}
}
