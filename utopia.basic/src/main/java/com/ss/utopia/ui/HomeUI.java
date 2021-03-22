package com.ss.utopia.ui;

import java.util.Scanner;

public class HomeUI {
	
	private EmpUI emp;
	private AdminUI admin;
	private GuestUI guest;
	
	public void menu() {
		System.out.println();
		System.out.println("Welcome to the Utopia Airlines Management System. "
				+ "Which category of a user are you");
		System.out.println("1) Employee");
		System.out.println("2) Admin");
		System.out.println("3) Guest");
		Scanner scan = new Scanner(System.in);
		Integer input = null;
		try {
			input = scan.nextInt();
			switch (input) {
			case 1:
				emp = new EmpUI();
				emp.menuOne();
				break;
			case 2:
				admin = new AdminUI();
				admin.menuOne();
				break;
			case 3:
				guest = new GuestUI();
				guest.menuOne();
				break;
			default:
				errorMessage();
			}
		} catch (Exception e) {
			errorMessage();
		} finally {
			scan.close();
		}
	}
	
	private void errorMessage() {
		System.out.println("\n\n");
		System.out.println("Please enter the number corresponding to your status");
		menu();
	}

}
