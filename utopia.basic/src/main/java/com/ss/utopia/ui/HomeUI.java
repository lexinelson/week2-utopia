package com.ss.utopia.ui;

import java.util.Scanner;

public class HomeUI {
	
	private EmpUI emp;
	private AdminUI admin; 
	private GuestUI guest;
	private Scanner scan;
	
	public HomeUI(Scanner scan) {
		this.scan = scan;
	}
	
	public void menu() {
		System.out.println();
		System.out.println("Welcome to the Utopia Airlines Management System. "
				+ "Which category of a user are you");
		System.out.println("1) Employee");
		System.out.println("2) Admin");
		System.out.println("3) Guest");
		Integer input = null;
		Boolean invalid = false;
		try {
			input = scan.nextInt();
			switch (input) {
			case 1:
				emp = new EmpUI();
				emp.menuOne(scan);
				break;
			case 2:
				admin = new AdminUI();
				admin.menuOne(scan);
				break;
			case 3:
				guest = new GuestUI();
				guest.menuOne(scan);
				break;
			default:
				errorMessage();
			}
		} catch (Exception e) {
			errorMessage();
			scan.next();
		}
		menu();
	}
	
	private void throwback() {
		menu();
	}
	
	private void errorMessage() {
		System.out.println("\n\n");
		System.out.println("Please enter the number corresponding to your status");
	}

}
