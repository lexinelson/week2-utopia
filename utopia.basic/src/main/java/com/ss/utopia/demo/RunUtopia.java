package com.ss.utopia.demo;

import java.util.Scanner;

import com.ss.utopia.ui.HomeUI;

public class RunUtopia {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		HomeUI home = new HomeUI(scan);
		home.menu();
	}
}
