package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.model.Airplane;

public class AirplaneDAO extends BaseDAO<Airplane> {

	public AirplaneDAO(Connection conn) {
		super(conn);
	}

	@Override
	public List<Airplane> extractData(ResultSet rs) throws SQLException {
		
		List<Airplane> planes = new ArrayList<Airplane>();
		PlaneTypeDAO capacityReader = new PlaneTypeDAO(conn);
		
		while (rs.next()) {
			Airplane plane = new Airplane();
			plane.setId(Integer.parseInt(rs.getString("id")));
			int typeID = Integer.parseInt(rs.getString("type_id"));
			plane.setMaxCapacity(capacityReader.readCapacity(typeID));
			planes.add(plane);
		}
		return planes;
	}
	
	public Integer getNextID() throws SQLException {
		return readAll("airplane").size() + 1;
	}
	
	public List<Airplane> readAllPlanes() throws SQLException {
		return readAll("airplane");
	}
	
	public Airplane readPlaneByID(Integer id) throws SQLException {
		return read("select * from airplane where id = ?", new Object[] {id}).get(0);
	}
	
	public Integer updateCapacities(Integer capacity) throws SQLException {
		PlaneTypeDAO capacityDAO = new PlaneTypeDAO(conn);
		List<Integer[]> capacities = capacityDAO.read("select * from airplane_type where max_capacity = ?", 
				new Object[] {capacity});
		
		Integer typeId;
		if (capacities.size() == 0) {
			typeId = capacityDAO.addType(capacity);
		} else {
			typeId = capacities.get(0)[0];
		}
		return typeId;
	}
	
	public void addAirplane(Airplane plane) throws SQLException {
		Integer typeId = updateCapacities(plane.getMaxCapacity());
		save("insert into airplane values (?, ?)", new Object[] {plane.getId(), typeId});
	}
	
	public void updateAirplane(Airplane plane) throws SQLException {
		save("update airplane set type_id = ? where id = ?", 
				new Object[] {updateCapacities(plane.getMaxCapacity()), plane.getId()});
	}
	
	public void deleteAirplane(Airplane plane) throws SQLException {
		save("delete from airplane where id = ?", new Object[] {plane.getId()});
	}
	
	
	private class PlaneTypeDAO extends BaseDAO<Integer[]> {

		public PlaneTypeDAO(Connection conn) {
			super(conn);
		}
		
		@Override
		public List<Integer[]> extractData(ResultSet rs) throws NumberFormatException, SQLException {
			List<Integer[]> capacities = new ArrayList<Integer[]>();
			while (rs.next()) {
				Integer[] capacity = new Integer[2];
				capacity[0] = Integer.parseInt(rs.getString("id"));
				capacity[1] = Integer.parseInt(rs.getString("max_capacity"));
				capacities.add(capacity);
			}
			return capacities;
		}
		
		public Integer readCapacity(Integer typeID) throws SQLException {
			return read("select id, max_capacity from airplane_type where id = ?",
					new Object[] {typeID}).get(0)[1];
		}
		
		public Integer getNextId() throws SQLException {
			return readAll("airplane_type").size() + 1;
		}
		
		public Integer addType(Integer capacity) throws SQLException {
			Integer id = getNextId();
			save("insert into airplane_type values (?, ?)", new Object[] {id, capacity});
			return id;
		}
	}
}
