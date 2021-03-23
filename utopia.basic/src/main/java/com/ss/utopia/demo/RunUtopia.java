package com.ss.utopia.demo;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Scanner;

import com.ss.utopia.ui.HomeUI;

public class RunUtopia {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		HomeUI home = new HomeUI(scan);
		try {
			home.menu();
		} catch (FileNotFoundException e) {
			System.out.println("This program requires connection to a local database called utopia.");
			System.out.println("Server access is hardcoded to connect locally on only the developers computer");
			System.out.println("To access it on your own system, contact the developer for instructions");
		} catch (SQLException e) {
			System.out.println("Unable to read the database. Perhaps it or this program has been changed.");
		}
	}
}
