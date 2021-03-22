package com.ss.utopia.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.utopia.model.Airport;

public class AirportDAO extends BaseDAO<Airport> {

	public AirportDAO(Connection conn) {
		super(conn);
	} 

	@Override
	public List<Airport> extractData(ResultSet rs) throws SQLException {
		List<Airport> airports = new ArrayList<Airport>();
		while (rs.next()) {
			Airport airport = new Airport();
			airport.setCode(rs.getString("iata_id"));
			airport.setCity(rs.getString("city"));
			airports.add(airport);
		}
		return airports;
	}
	
	public void addAirport(Airport airport) throws SQLException {
		save("insert into airport values (?, ?)", 
				new Object[] {airport.getCode(), airport.getCity()});
	}
	
	public List<Airport> getAllAirports() throws SQLException {
		return readAll("airport");
	}
	
	public Airport readAirportByCode(String code) throws SQLException {
		List<Airport> airports = read("Select * from airport where iata_id = ?", new Object[] {code});
		if (airports.size() > 0)
			return airports.get(0);
		else return null;
	}

	public void updateAirport(Airport airport) throws SQLException {
		save("update airport set city = ? where iata_id = ?", 
				new Object[] {airport.getCity(), airport.getCode()});
	}
	
	public void deleteAirport(Airport airport) throws SQLException {
		save("delete from airport where iata_id = ?",
				new Object[] {airport.getCode()});
	}
}
