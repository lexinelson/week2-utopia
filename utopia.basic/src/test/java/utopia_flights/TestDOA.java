package utopia_flights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.ss.utopia.dao.AirplaneDAO;
import com.ss.utopia.dao.AirportDAO;
import com.ss.utopia.dao.RouteDAO;
import com.ss.utopia.model.Airplane;
import com.ss.utopia.model.Airport;
import com.ss.utopia.model.Route;
import com.ss.utopia.service.ServerUtil;

public class TestDOA {

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
	public void airplaneDAOTest() throws SQLException, ClassNotFoundException, FileNotFoundException {
		test = new ServerUtil();
		AirplaneDAO tester = new AirplaneDAO(test.getConnection());
		Airplane plane = tester.readPlaneByID(1);
		assertEquals((Integer) 1, plane.getId());
		assertEquals((Integer) 125, plane.getMaxCapacity());
	}
	
	@Test
	public void airportDAOTest() throws SQLException, ClassNotFoundException, FileNotFoundException {
		test = new ServerUtil();
		AirportDAO tester = new AirportDAO(test.getConnection());
		Airport expected = new Airport();
		expected.setCode("SAC");
		Airport result = tester.readAirportByCode("SAC");
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
}
