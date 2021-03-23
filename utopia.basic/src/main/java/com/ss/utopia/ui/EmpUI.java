package com.ss.utopia.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.ss.utopia.model.Flight;
import com.ss.utopia.service.EmployeeService;

public class EmpUI {
	
	private EmployeeService service = new EmployeeService();
	private HomeUI home;
	private Scanner scan;
	
	public void menuOneError() {
		System.out.println("\n\n\n\n\n");
		System.out.println("Please enter the number corresponding to what you'd like to do");
	}
	
	public void menuOne() {
		System.out.println("\n\nWhat would you like to do?");
		System.out.println("1) Enter flights you manage");
		System.out.println("2) Quit to previous");
		Integer input = null;
		try {
			input = scan.nextInt();
			switch (input) {
			case 1:
				menuTwo();
				break;
			case 2:
				return;
			default:
				menuOneError();
			}
		} catch (Exception e) {
			menuOneError();
			scan.next();
		}
	}

	public void menuOne(Scanner scanner) {
		scan = scanner;
		menuOne();
	}
	
	public void menuTwo() {
		List<String> flights = null;
		try {
			flights = service.viewFlights();
		} catch (FileNotFoundException e) {
			System.out.println("Sorry, something seems to be wrong with the application");
			menuOne();
		} catch (SQLException e) {
			System.out.println("Sorry, I can't seem to get the flights right now");
			menuOne();
		}
		
		System.out.println("\n\n");
		if(flights != null) {
			for (String flight : flights) {
				System.out.println(flight);
			}
		}

		Integer input = null;
		System.out.print("Please enter the flight number you'd like to manage or quit to return to menu: ");
		String in=null;
		try {
			in = scan.next();
			input = Integer.parseInt(in);
			Flight flight = service.fetchFlight(input);
			if (flight != null) {
				menuThree(flight);
			}
			else {
				menuTwoError();
			}
		} catch (Exception e) {
			if ("quit".equals(in))
				menuOne();
			else
				menuTwoError();
		}
	}
	
	public void menuTwoError() {
		System.out.println("\n\nNot a valid flight number, press enter to try again");
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		menuTwo();
	}
	
	public void menuThreeError() {
		System.out.println("Invalid input, press enter to try again");
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void menuThree(Flight flight) {
		System.out.println();
		System.out.println("1) View more details about the flight");
		System.out.println("2) Update details of the flight");
		System.out.println("3) Add seats to flight");
		System.out.println("4) Quit to previous");
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
				addSeats(flight);
				break;
			case 4:
				quit = true;
				break;
			default:
				menuThreeError();
			}
		} catch (Exception e) {
			menuThreeError();
		} 
		if (quit)
			menuTwo();
		else 
			menuThree(flight);
	}
	
	public void displayFlight(Flight flight) {
		System.out.println(service.displayFlight(flight));
		System.out.println("Press enter to return to previous");
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateFlight(Flight flight) {
		Flight copy = flight;
		System.out.println("You have chosen to update the flight with id: " + flight.getId() +
				" and Origin: " + service.displayAirport(flight.getRoute().getOrigin()) +
				" and Destination: " + service.displayAirport(flight.getRoute().getDestination()));
		System.out.println("Enter 'quit' at any prompt to cancel operation");
		
		StringBuffer input = new StringBuffer();
		
		System.out.println("Please enter new Destination Airport and City or enter N/A for no change:");
		input.append(scan.nextLine());
		if (input.toString().equals("quit")) {
			return;
		} else if (input.toString().equals("N/A")) {}
		else {
			flight.getRoute().getDestination().setCode(input.toString().split(", ")[0]);
			flight.getRoute().getDestination().setCity(input.toString().split(", ")[1]);
		}
		
		input.delete(0, input.length());
		System.out.println("Please enter new Departure Date or enter N/A for no change:");
		input.append(scan.nextLine());
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
		
		try {
			service.updateFlight(flight);
			System.out.println("Changes made were successful!");
		} catch (Exception e) {
			System.out.println("Something went wrong changing the flight. no changes were made");
		}
	}
	
	public void addSeats(Flight flight) {
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
		try {
			service.updateFlight(flight);
		} catch (Exception e) {
			System.out.println("Error: Something went wrong with the application build.");
		}
	}
}
