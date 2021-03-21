package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class BaseDAO<T> {
	
	protected Connection conn;

	public BaseDAO(Connection conn) {
		this.conn = conn;
	}  
	
	public PreparedStatement prepare(String sql, Object[] vals) 
			throws SQLException {
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		int count = 1;
		for(Object o: vals) {
			pstmt.setObject(count, o);
			count++;
		}
		return pstmt;
	}
	
	public void save(String sql, Object[] vals) throws SQLException {
		prepare(sql, vals).executeUpdate();
	}
	
	public List<T> readAll(String table) throws SQLException {
		return read("select * from ?", new String[] {table});
	}
	
	public List<T> read(String sql, Object[] vals) throws SQLException {
		return extractData(prepare(sql, vals).executeQuery());
	}

	abstract public List<T> extractData(ResultSet rs) throws SQLException;
}
