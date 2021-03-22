package utopia_flights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import com.ss.utopia.dao.AirportDAO;
import com.ss.utopia.dao.FlightDAO;
import com.ss.utopia.dao.RouteDAO;
import com.ss.utopia.dao.TicketDAO;
import com.ss.utopia.model.Airport;
import com.ss.utopia.model.Flight;
import com.ss.utopia.model.Route;
import com.ss.utopia.model.Ticket;
import com.ss.utopia.service.ServerUtil;

public class TestReadDAO {

	private ServerUtil test;
	
	@Test
	public void connectionTest() throws ClassNotFoundException, SQLException {
		try {
			test = new ServerUtil();
		} catch (FileNotFoundException e) {
			assertTrue(false);
		}
		
		Connection conn = test.getConnection();
		PreparedStatement testQuery = conn.prepareStatement("SELECT * FROM airport WHERE iata_id = 'SAC'");
		ResultSet rs = testQuery.executeQuery();
		rs.next();
		assertEquals("Sacramento", rs.getString("city"));
	}
	
	@Test
	public void airportDAOTest() throws SQLException, ClassNotFoundException, FileNotFoundException {
		test = new ServerUtil();
		AirportDAO tester = new AirportDAO(test.getConnection());
		Airport expected = new Airport();
		expected.setCode("SAC");
		Airport result = tester.getAirportByCode("SAC");
		assertEquals(expected, result);
		assertEquals("Sacramento", result.getCity());
	}
	
	@Test
	public void routeDAOTest() throws SQLException, ClassNotFoundException, FileNotFoundException {
		test = new ServerUtil();
		RouteDAO tester = new RouteDAO(test.getConnection());
		Route result = tester.readRouteByID(1);
		assertEquals("Sacramento", result.getOrigin().getCity());
		assertEquals("Los Angeles", result.getDestination().getCity());
	}
	
	@Test
	public void ticketDAOTest() throws SQLException, ClassNotFoundException, FileNotFoundException {
		test = new ServerUtil();
		TicketDAO tester = new TicketDAO(test.getConnection());
		List<Ticket> result = tester.readTicketsById(1);
	//	assertEquals(2, result.size());
		result = tester.readTicketsByFlightId(3);
	//	assertEquals(3, result.size());
		result = tester.readTicketsByPassengerId(9);
		assertEquals(2, result.size());
	}
	
	@Test
	public void FlightDAOTest() throws SQLException, ClassNotFoundException, FileNotFoundException {
		test = new ServerUtil();
		FlightDAO tester = new FlightDAO(test.getConnection());
		Route testRoute = new Route();
		testRoute.setId(3);
		Flight result = tester.readFlightById(1);
		assertEquals(testRoute, result.getRoute());
		assertEquals(10, tester.readAllFlights().size());
	}
}
