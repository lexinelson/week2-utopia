package com.ss.utopia.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.ss.utopia.model.Flight;
import com.ss.utopia.model.Ticket;
import com.ss.utopia.service.GuestService;

public class GuestUI {

	private HomeUI home;
	private Scanner scan;
	private GuestService service = new GuestService();
	private Integer ticketId;

	public void menuOne(Scanner scanner) throws FileNotFoundException, SQLException { 
		scan = scanner;
		menuOne();
	}

	public void menuOne() throws FileNotFoundException, SQLException {
		if (ticketId == null)
			ticketId = verification();
		System.out.println("\n\nWhat would you like to do?");
		System.out.println("1) Book a Ticket");
		System.out.println("2) Cancel an Upcoming Trip");
		System.out.println("3) Quit to previous");
		
		try {
			Integer input = scan.nextInt();
			switch (input) {
			case 1:
				bookMenuOne();
				break;
			case 2:
				cancelMenu();
				break;
			case 3:
				return;
			default:
				menuOneError();
			}
		} catch (InputMismatchException e) {
			String text = scan.nextLine().toLowerCase();
			if (text.contains("book"))
				bookMenuOne();
			else if (text.contains("cancel"))
				cancelMenu();
			else if (text.contains("quit"))
				return;
			else {
				menuOneError();
			}
		}
	}
	
	public void menuOneError() throws FileNotFoundException, SQLException {
		System.out.println("\nPlease enter one of the given options.");
		menuOne();
	}

	public Integer verification() throws FileNotFoundException, SQLException {
		boolean valid = false;
		Ticket ticket = null;
		do {
			System.out.println("Enter a valid ticket confirmation or traveler id");
			String input = scan.next();
			Integer num=null;
			try {
				num = Integer.parseInt(input);
				ticket = service.findTicket(num);
				if (ticket != null)
					valid = true;
				else
					System.out.println("\nThat id number does not exist\n");
			} catch (NumberFormatException e) {
				ticket = service.findTicket(input.toString().toUpperCase());
				if (ticket != null)
					valid = true;
				else
					System.out.println("\nThat confirmation code does not exist\n");
			}
		} while (!valid);
		return ticket.getPassengerId();
	}
	
	public void bookMenuOne() throws FileNotFoundException, SQLException {
		Ticket ticket = service.findTicket(ticketId);
		List<Flight> flights = service.getPossibleFlights(ticket);
		List<String> flightsStr = service.viewPossibleFlights(ticket);
		
		System.out.println("\n\n");
		if(flightsStr != null) {
			for (String flight : flightsStr) {
				System.out.println(flight);
			}
		}
		
		Integer input = null;
		System.out.print("Please enter the flight number you'd like to book or quit to return to menu: ");
		String in=null;
		try {
			in = scan.next();
			input = Integer.parseInt(in);
			Flight f = service.fetchFlight(input);
			Flight flight = (flights.contains(f)) ? f : null;
			if (flight != null) {
				bookMenuTwo(flight);
			}
			else {
				flightMenuError("booking");
			}
		} catch (NumberFormatException e) {
			if ("quit".equals(in))
				menuOne();
			else
				flightMenuError("booking");
		}
	}
	
	public void flightMenuError(String block) throws FileNotFoundException, SQLException {
		System.out.println("\n\nNot a valid flight number, press enter to try again");
		try {
			System.in.read();
		} catch (IOException e) {
			System.out.println("Something is wrong with the console!");
			System.out.println("Please restart the program and try again");
			while (true) {}
		}
		if ("booking".equals(block))
			bookMenuOne();
		else if ("cancel".equals(block))
			cancelMenu();
	}
	
	public void inputError() {
		System.out.println("Invalid input, press enter to try again");
		try {
			System.in.read();
		} catch (IOException e) {
			System.out.println("Something is wrong with the console!");
			System.out.println("Please restart the program and try again");
			while (true) {}
		}
	}
	
	public void displayFlight(Flight flight, String block) throws FileNotFoundException, SQLException {
		System.out.println(service.displayFlight(flight));
		System.out.println("Press enter to return");
		try {
			System.in.read();
		} catch (IOException e) {
			System.out.println("Something is wrong with the console!");
			System.out.println("Please restart the program and try again");
			while (true) {}
		}
		if ("booking".equals(block))
			bookMenuTwo(flight);
		else if ("cancel".equals(block))
			cancelMenuTwo(flight);
	}
	
	public void bookMenuTwo(Flight flight) throws FileNotFoundException, SQLException {
		System.out.println("\nPick the seat you want to book a ticket for");
		System.out.println("1) VIEW FLIGHT DETAILS");
		System.out.println("2) First Class");
		System.out.println("3) Business Class");
		System.out.println("5) Economy Class");
		System.out.println("4) Quit to previous");
		System.out.print("\nEnter the number to make your selection: ");
		Integer input = null;
		boolean quit = false;
		Integer seatClass = null;
		try {
			input = scan.nextInt();
			scan.nextLine();
			switch (input) {
			case 1:
				displayFlight(flight, "booking");
				return;
			case 2:
				seatClass = 1;
				break;
			case 3:
				seatClass = 2;
				break;
			case 4:
				seatClass = 3;
			case 5:
				quit = true;
				break;
			default:
				inputError();
			}
		} catch (Exception e) {
			inputError();
		} 
		if (quit)
			menuOne();
		else if (input == null || input > 5 || input < 0) {
			bookMenuTwo(flight);
		} else {
			Ticket ticket = service.findTicket(ticketId);
			ticket.setSeatId(seatClass);
			service.bookTicket(flight, ticket);
			System.out.println("Congratulations! You're booked for this flight!\n\n");
			menuOne();
		}
	}
	
	public void cancelMenu() throws FileNotFoundException, SQLException {
		Ticket ticket = service.findTicket(ticketId);
		List<Flight> flights = service.getBookedFlights(ticket);
		List<String> flightsStr = service.viewBookedFlights(ticket);
		
		System.out.println("\n\n");
		if(flightsStr != null) {
			for (String flight : flightsStr) {
				System.out.println(flight);
			}
		}
		
		Integer input = null;
		System.out.print("Please enter the flight number for the ticket you'd like to cancel,\nor 'quit' to return to the menu: ");
		String in=null;
		try {
			in = scan.next();
			input = Integer.parseInt(in);
			Flight f = service.fetchFlight(input);
			Flight flight = (flights.contains(f)) ? f : null;
			if (flight != null) {
				cancelMenuTwo(flight);
			}
			else {
				flightMenuError("cancel");
			}
		} catch (NumberFormatException e) {
			if ("quit".equals(in))
				menuOne();
			else
				flightMenuError("cancel");
		}
	}
	
	public void cancelMenuTwo(Flight flight) throws FileNotFoundException, SQLException {
		System.out.println("\n\nPlease ensure and confirm that you'd like to cancel this flight");
		System.out.println("1) VIEW FLIGHT DETAILS");
		System.out.println("2) Confirm cancellation");
		System.out.println("3) Quit to previous");
		Integer input = null;
		boolean quit = false;
		Integer seatClass = null;
		try {
			input = scan.nextInt();
			scan.nextLine();
			switch (input) {
			case 1:
				displayFlight(flight, "cancel");
				return;
			case 2:
				seatClass = 1;
				break;
			case 3:
				quit = true;
				break;
			default:
				inputError();
			}
		} catch (Exception e) {
			inputError();
		} 
		if (quit)
			menuOne();
		else if (input != 2) {
			cancelMenuTwo(flight);
		} else {
			Ticket ticket = service.findTicket(ticketId);
			ticket.setFlightId(flight.getId());
			service.cancelTicket(ticket);
			System.out.println("You're ticket has been cancelled.\n\n");
			menuOne();
		}
	}

}
