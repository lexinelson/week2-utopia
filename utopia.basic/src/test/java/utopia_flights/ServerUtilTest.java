package utopia_flights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.ss.utopia.service.ServerUtil;

public class ServerUtilTest {

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
}
