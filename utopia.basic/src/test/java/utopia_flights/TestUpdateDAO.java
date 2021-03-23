package utopia_flights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

public class TestUpdateDAO {
	private Util util;
	private Connection conn = null;
	
	@Test
	public void testAirportDAO() throws ClassNotFoundException, SQLException, FileNotFoundException {
		util = new Util();
		conn = util.getConnection();
		AirportDAO test;
		Airport sample;
		Airport expected = new Airport();
		expected.setCode("SAC");
		expected.setCity("Vancouver");
		try {
			test = new AirportDAO(conn);
			sample = test.getAirportByCode("SAC");
			test.updateAirport(expected);
			assertEquals(expected.getCity(), test.getAirportByCode("SAC").getCity());
			test.updateAirport(sample);
			assertEquals(sample.getCity(), test.getAirportByCode("SAC").getCity());
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
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
		AirportDAO testHelp;
		Route sample;
		Route expected = new Route();
		expected.setId(1);
		expected.setDuration(Duration.ofHours(4));
		try {
			test = new RouteDAO(conn);
			testHelp = new AirportDAO(conn);
			expected.setOrigin(testHelp.getAirportByCode("SAC"));
			expected.setDestination(testHelp.getAirportByCode("MSP"));
			sample = test.readRouteByID(1);
			test.updateRoute(expected);
			assertEquals(expected.getDestination(), test.readRouteByID(1).getDestination());
			test.updateRoute(sample);
			assertEquals(sample.getDestination(), test.readRouteByID(1).getDestination());
			
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			if (conn != null)
				conn.close();
		}
	}
	
	@Test
	public void testTicketDAO() throws ClassNotFoundException, SQLException, FileNotFoundException {
		util = new Util();
		conn = util.getConnection();
		TicketDAO test;
		Ticket sample;
		Ticket expected = new Ticket();
		expected.setId(1);
		expected.setFlightId(5);
		expected.setActive(false);
		expected.setSeatId(2);
		expected.setConfirmationCode("ABHLSFUHLS");
		expected.setPassengerId(1);
		expected.setPassengerName("Lexi Nelson");
		try {
			test = new TicketDAO(conn);
			sample = test.readTicketsById(1).get(0);
			test.updateTicket(expected);
			assertFalse(test.readTicketsById(1).get(0).isActive());
			test.updateTicket(sample);
			assertTrue(test.readTicketsById(1).get(0).isActive());
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			if (conn != null)
				conn.close();
		}
		
		util = new Util();
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
	
	@Test
	public void testFlightDAO() throws ClassNotFoundException, SQLException, FileNotFoundException {
		util = new Util();
		conn = util.getConnection();
		FlightDAO test;
		RouteDAO testHelp;
		Flight sample;
		Flight expected = new Flight();
		expected.setId(1);
		expected.setMaxCapacity(105);
		expected.setStartTime(LocalDateTime.now());
		expected.setSeats(new Integer[] {10, 40, 50});
		try {
			test = new FlightDAO(conn);
			testHelp = new RouteDAO(conn);
			sample = test.readFlightById(1);
			expected.setRoute(testHelp.readRouteByID(2));
			expected.setEndTime(expected.getStartTime().plus(expected.getRoute().getDuration()));
			test.updateFlight(expected);
			assertEquals((Integer) 2, test.readFlightById(1).getRoute().getId());
			test.updateFlight(sample);
			assertEquals((Integer) 13, test.readFlightById(1).getRoute().getId());
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			if (conn != null)
				conn.close();
		}
	}
}
