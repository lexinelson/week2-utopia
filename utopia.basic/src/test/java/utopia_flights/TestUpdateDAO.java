package utopia_flights;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.ss.utopia.dao.AirplaneDAO;
import com.ss.utopia.model.Airplane;
import com.ss.utopia.service.ServerUtil;

public class TestUpdateDAO {
	private ServerUtil util;
	private Connection conn = null;
	
	@Test
	public void testAirportDAO() throws ClassNotFoundException, SQLException, FileNotFoundException {
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
	
	@Test
	public void testRouteDAO() throws ClassNotFoundException, SQLException, FileNotFoundException {
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
	
	@Test
	public void testTicketDAO() throws ClassNotFoundException, SQLException, FileNotFoundException {
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
	
	@Test
	public void testFlightDAO() throws ClassNotFoundException, SQLException, FileNotFoundException {
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
