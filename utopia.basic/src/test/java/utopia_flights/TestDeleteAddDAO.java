package utopia_flights;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.ss.utopia.dao.AirplaneDAO;
import com.ss.utopia.dao.AirportDAO;
import com.ss.utopia.dao.FlightDAO;
import com.ss.utopia.dao.RouteDAO;
import com.ss.utopia.dao.TicketDAO;
import com.ss.utopia.model.Airplane;
import com.ss.utopia.model.Airport;
import com.ss.utopia.model.Flight;
import com.ss.utopia.model.Route;
import com.ss.utopia.model.Ticket;
import com.ss.utopia.service.ServerUtil;

public class TestDeleteAddDAO {
	private ServerUtil util;
	private Connection conn = null;
	
	@Test
	public void testAirportDAO() throws ClassNotFoundException, SQLException, FileNotFoundException {
		util = new ServerUtil();
		conn = util.getConnection();
		Integer airportCount = 0;
		Airport original = null;
		AirportDAO test;
		try {
			test = new AirportDAO(conn);
			airportCount = test.getAllAirports().size();
			original = test.readAirportByCode("SAC");
			test.deleteAirport(original);
			conn.commit();
			assertEquals(airportCount - 1, test.getAllAirports().size());
			test.addAirport(original);
			conn.commit();
			assertEquals(airportCount, (Integer) test.getAllAirports().size());
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
			assertEquals(false, true);
		} finally {
			if (conn != null)
				conn.close();
		}
	}
	
	@Test
	public void testRouteDAO() throws ClassNotFoundException, SQLException, FileNotFoundException {
		util = new ServerUtil();
		conn = util.getConnection();
		Integer routeCount = 0;
		Route original = null;
		RouteDAO test;
		try {
			test = new RouteDAO(conn);
			routeCount = test.readAllRoutes().size();
			original = test.readRouteByID(10);
			test.deleteRoute(original);
			conn.commit();
			assertEquals((Integer) (routeCount - 1), (Integer) test.readAllRoutes().size());
			test.addRoute(original);
			conn.commit();
			assertEquals((Integer) routeCount, (Integer) test.readAllRoutes().size());
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
			assertEquals(true, false);
		} finally {
			if (conn != null)
				conn.close();
		}
	}
	
	@Test
	public void testTicketDAO() throws ClassNotFoundException, SQLException, FileNotFoundException {
		util = new ServerUtil();
		conn = util.getConnection();
		Integer ticketCount = 0;
		Ticket original = null;
		TicketDAO test;
		try {
			test = new TicketDAO(conn);
			ticketCount = test.readAllTickets().size();
			original = test.readTicketById(10);
			test.deleteTicket(original);
			conn.commit();
			assertEquals((Integer) (ticketCount - 1), (Integer) test.readAllTickets().size());
			test.addNewTicket(original);
			conn.commit();
			assertEquals((Integer) ticketCount, (Integer) test.readAllTickets().size());
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
			assertEquals(true, false);
		} finally {
			if (conn != null)
				conn.close();
		}
	}
	
	@Test
	public void testFlightDAO() throws ClassNotFoundException, SQLException, FileNotFoundException {
		util = new ServerUtil();
		conn = util.getConnection();
		Integer flightCount = 0;
		Flight original = null;
		FlightDAO test;
		try {
			test = new FlightDAO(conn);
			flightCount = test.readAllFlights().size();
			original = test.readFlightById(10);
			test.deleteFlight(original);
			conn.commit();
			assertEquals((Integer) (flightCount - 1), (Integer) test.readAllFlights().size());
			test.addFlight(original);
			conn.commit();
			assertEquals((Integer) flightCount, (Integer) test.readAllFlights().size());
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
			assertEquals(true, false);
		} finally {
			if (conn != null)
				conn.close();
		}
	}
	
	@Test
	public void testPassengerDAO() throws ClassNotFoundException, SQLException, FileNotFoundException {
		util = new ServerUtil();
		conn = util.getConnection();
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			if (conn != null)
				conn.close();
		}
		
		util = new ServerUtil();
		conn = util.getConnection();
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			if (conn != null)
				conn.close();
		}
	}

}
