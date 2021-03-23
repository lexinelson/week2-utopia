package com.ss.utopia.ui;

import java.util.Scanner;

import com.ss.utopia.service.GuestService;

public class GuestUI {
	
	private HomeUI home;
	private Scanner scan;
	private GuestService service;

	public void menuOne(Scanner scanner) { 
		scan = scanner;
		home = new HomeUI(scan);
	}
}
