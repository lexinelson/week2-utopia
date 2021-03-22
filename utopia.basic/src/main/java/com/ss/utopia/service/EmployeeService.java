package com.ss.utopia.service;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.model.Airport;
import com.ss.utopia.model.Flight;
import com.ss.utopia.model.Route;

public class EmployeeService {
	
	private AdminService privelage = new AdminService();
	private Util utility;
	
	public String displayFlight(Flight f) {
		return utility.displayFlight(f);
	}
	
	public String displayAirport(Airport a) {
		return utility.displayAirport(a);
	}
	
	public List<String> viewFlights() throws FileNotFoundException, SQLException {
		utility = new Util();
		List<Flight> flights = privelage.readFlights();
		List<String> display = new ArrayList<String>();
		for (Flight flight : flights) {
			display.add(flight.getId() + ") "+utility.displayRoute(flight.getRoute()));
		}
		return display;
	}
	
	public Flight fetchFlight(Integer id) throws FileNotFoundException, SQLException {
		return privelage.flightById(id);
	}
	
	public void updateFlight(Flight flight) throws FileNotFoundException, SQLException {
		List<Route> routes = privelage.readRoutes();
		boolean routeExists = false;
		for (Route route : routes) {
			if (flight.getRoute().getOrigin().equals(route.getOrigin()) &&
					flight.getRoute().getDestination().equals(route.getDestination())) {
				flight.setRoute(route);
				routeExists = true;
			}
		}
		if (!routeExists) {
			privelage.addRoute(flight.getRoute());
		}
		privelage.updateFlight(flight);
	}

}
