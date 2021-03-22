package utopia_flights;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.Test;

import com.ss.utopia.dao.AirportDAO;
import com.ss.utopia.dao.FlightDAO;
import com.ss.utopia.dao.RouteDAO;
import com.ss.utopia.dao.TicketDAO;
import com.ss.utopia.model.Airport;
import com.ss.utopia.model.Flight;
import com.ss.utopia.model.Route;
import com.ss.utopia.model.Ticket;
import com.ss.utopia.service.Util;

public class TestDeleteAddDAO {
	private Util util;
	private Connection conn = null;
	
	@Test
	public void testAirportDAO() throws ClassNotFoundException, SQLException, FileNotFoundException {
		util = new Util();
		conn = util.getConnection();
		Integer airportCount = 0;
		Airport example = new Airport();
		example.setCode("UNI");
		example.setCity("Universe");
		
		AirportDAO test;
		try {
			test = new AirportDAO(conn);
			Integer total = test.getAllAirports().size();
			test.addAirport(example);
			assertEquals(total + 1, test.getAllAirports().size());
			test.deleteAirport(example);
			assertEquals(total, (Integer) test.getAllAirports().size());
			conn.commit();
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
		util = new Util();
		conn = util.getConnection();
		RouteDAO test;
		Route example = new Route();
		Airport origin = new Airport();
		Airport destination = new Airport();
		origin.setCode("PND");
		destination.setCode("LAX");
		example.setOrigin(origin);
		example.setDestination(destination);
		example.setDuration(Duration.ofHours((long) 4));
		try {
			test = new RouteDAO(conn);
			Integer total = test.readAllRoutes().size();
			test.addRoute(example);
			assertEquals(total + 1, test.readAllRoutes().size());
			test.deleteRoute(example);
			assertEquals(total, (Integer) test.readAllRoutes().size());
			conn.commit();
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
		util = new Util();
		conn = util.getConnection();
		Ticket example = new Ticket();
		TicketDAO test;
		example.setId(11);
		example.setConfirmationCode("LJLDFBNUDHSD");
		example.setFlightId(5);
		example.setPassengerId(11);
		example.setSeatId(2);
		example.setPassengerName("Lexi Nelson");
		example.setActive(true);
		try {
			test = new TicketDAO(conn);
			Integer total = test.readAllTickets().size();
			test.addNewTicket(example);
			assertEquals(total + 1, test.readAllTickets().size());
			test.deleteTicket(example);
			assertEquals(total, (Integer) test.readAllTickets().size());
			conn.commit();
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
		util = new Util();
		conn = util.getConnection();
		Flight example = new Flight();
		Route r = new Route();
		FlightDAO test;
		example.setId(11);
		example.setMaxCapacity(115);
		r.setId(5);
		example.setRoute(r);
		example.setStartTime(LocalDateTime.now());
		example.setSeats(new Integer[] {10, 15, 20});
		try {
			test = new FlightDAO(conn);
			int count = test.readAllFlights().size();
			test.addFlight(example);
			assertEquals(count + 1, test.readAllFlights().size());
			test.deleteFlight(example);
			assertEquals(count, test.readAllFlights().size());
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
		util = new Util();
		conn = util.getConnection();
		Flight original = null;
		FlightDAO test;
		try {
			test = new FlightDAO(conn);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
			assertEquals(true, false);
		} finally {
			if (conn != null)
				conn.close();
		}
	}

}
