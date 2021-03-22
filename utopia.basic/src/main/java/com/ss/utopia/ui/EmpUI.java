package com.ss.utopia.ui;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import com.ss.utopia.model.Flight;
import com.ss.utopia.service.EmployeeService;

public class EmpUI {
	
	private EmployeeService service = new EmployeeService();
	private HomeUI home;
	
	public void menuOneError() {
		System.out.println("\n\n\n\n\n");
		System.out.println("Please enter the number corresponding to what you'd like to do");
		menuOne();
	}

	public void menuOne() {
		home = new HomeUI();
		System.out.println("What would you like to do?");
		System.out.println("1) Enter flights you manage");
		System.out.println("2) Quit to previous");
		Scanner scan = new Scanner(System.in);
		Integer input = null;
		try {
			input = scan.nextInt();
			switch (input) {
			case 1:
				menuTwo();
				break;
			case 2:
				home.menu();
				break;
			default:
				menuOneError();
			}
		} catch (Exception e) {
			menuOneError();
		} finally {
			scan.close();
		}
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
		
		if(flights != null) {
			for (String flight : flights) {
				System.out.println(flight);
			}
		}
		
		Scanner scan = new Scanner(System.in);
		Integer input = null;
		System.out.print("Please select the flight number you'd like to manage: ");
		try {
			input = scan.nextInt();
			menuThree(service.fetchFlight(input));
		} catch (Exception e) {
			menuTwoError();
		}
	}
	
	public void menuTwoError() {
		System.out.println("\n\nNot a valid flight number, please try again");
		menuTwo();
	}
	
	public void menuThreeError(Flight f) {
		System.out.println("Invalid input, please try again");
		menuThree(f);
	}
	
	public void menuThree(Flight flight) {
		System.out.println();
		System.out.println("1) View more details about the flight");
		System.out.println("2) Update details of the flight");
		System.out.println("3) Add seats to flight");
		System.out.println("4) Quit to previous");
		System.out.print("\nEnter the number corresponding to what you'd like to do: ");
		Scanner scan = new Scanner(System.in);
		Integer input = null;
		try {
			input = scan.nextInt();
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
				menuTwo();
			default:
				menuThreeError(flight);
			}
		} catch (Exception e) {
			menuThreeError(flight);
		} finally {
			scan.close();
		}
	}
	
	public void displayFlight(Flight flight) {
		System.out.println(service.displayFlight(flight));
		System.out.println("Press enter to return to previous");
		Scanner scan = new Scanner(System.in);
		scan.nextLine();
		menuThree(flight);
	}
	
	public void updateFlight(Flight flight) {
		System.out.println("You have chosen to update the flight with id: " + flight.getId() +
				" and Origin: " + service.displayAirport(flight.getRoute().getOrigin()) +
				" and Destination: " + service.displayAirport(flight.getRoute().getDestination()));
		System.out.println("Enter 'quit' at any prompt to cancel operation");
		
		Scanner scan = new Scanner(System.in);
		StringBuffer input = new StringBuffer();
		
		System.out.println("Please enter new Destination Airport and City or enter N/A for no change:");
		input.append(scan.nextLine());
		if (input.toString().equals("quit")) {
			menuThree(flight);
		} else if (input.toString().equals("N/A")) {}
		else {
			flight.getRoute().getDestination().setCode(input.toString().split(", ")[0]);
			flight.getRoute().getDestination().setCity(input.toString().split(", ")[1]);
		}
		input.delete(0, input.length());
		
		System.out.println("Please enter new Departure Date or enter N/A for no change:");
		input.append(scan.nextLine());
		if (input.toString().equals("quit")) {
			menuThree(flight);
		} else if (input.toString().equals("N/A")) {}
		else {
			System.out.println("Please enter new Departure Time or enter N/A for no change:");
			input.append(scan.nextLine());
			if (input.toString().equals("quit")) {
				menuThree(flight);
			} else if (input.toString().equals("N/A")) {}
			else {
				input.append("T"+scan.nextLine());
			}
			try {
				flight.setStartTime(LocalDateTime.parse(input));
			} catch (Exception e) {
				System.out.println("Date and time format must be YYYY-MM-DD and 00:00:00 respectively");
				System.out.println("No change was made");
			}
 		}
		
		input.delete(0, input.length());
		
		System.out.println("Please enter new Arrival Date or enter N/A for no change:");
		input.append(scan.nextLine());
		if (input.toString().equals("quit")) {
			menuThree(flight);
		} else if (input.toString().equals("N/A")) {}
		else {
			System.out.println("Please enter new Arrival Time or enter N/A for no change:");
			input.append(scan.nextLine());
			if (input.toString().equals("quit")) {
				menuThree(flight);
			} else if (input.toString().equals("N/A")) {}
			else {
				input.append("T"+scan.nextLine());
			}
			try {
				flight.setEndTime(LocalDateTime.parse(input));
			} catch (Exception e) {
				System.out.println("Date and time format must be YYYY-MM-DD and 00:00:00 respectively");
				System.out.println("No change was made");
			}
 		}
		
		try {
			service.updateFlight(flight);
		} catch (Exception e) {
			System.out.println("Something went wrong changing the flight. no changes were made");
		}
		
		menuThree(flight);
	}
	
	public void addSeats(Flight flight) {
		System.out.println("Pick the Seat Class you want to add seats of, to your flight:");
		System.out.println("1) First Class");
		System.out.println("2) Business Class");
		System.out.println("3) Economy Class");
		
	}
}
