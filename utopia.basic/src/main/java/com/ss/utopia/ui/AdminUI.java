package com.ss.utopia.ui;

import java.util.Scanner;

import com.ss.utopia.service.AdminService;

public class AdminUI {

	private HomeUI home;
	private AdminService service = new AdminService();
	private Scanner scan;
	
	public void menuOne(Scanner scanner) {
		scan = scanner;
	} 
}
