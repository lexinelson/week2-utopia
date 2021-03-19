package com.ss.utopia.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class ServerUtil {
	private final String driver = "com.mysql.cj.jdbc.Driver";
	private final String credPath = "C:\\Work\\credentials.txt";
	
	private String url, username, password;

	public ServerUtil() throws FileNotFoundException {
		File creds = new File(credPath);
		Scanner scan = new Scanner(creds);
		url = scan.nextLine();
		username = scan.nextLine();
		password = scan.nextLine();
		scan.close();
	}
	
	public Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName(driver);
		
		Connection conn = DriverManager.getConnection(url, username, password);
		return conn;
	}

}
