package com.techelevator.campground.model.jdbc;

import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO {
	
	private JdbcTemplate template;

	public JDBCParkDAO(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}

	@Override
	public Map<String, Park> getAvailableParks() {
		Map<String, Park> availableParks = new TreeMap<>();
		String sqlGetAvailableParks = "SELECT * FROM park";
		SqlRowSet results = template.queryForRowSet(sqlGetAvailableParks);
		while(results.next()) {
			Park park = mapRowToPark(results);
			availableParks.put(results.getString("name"), park);
		}
		return availableParks;
	}
	
	public Park mapRowToPark(SqlRowSet results) {
		Park park;
		park = new Park();
		park.setId(results.getLong("park_id"));
		park.setName(results.getString("name"));
		park.setLocation(results.getString("location"));
		park.setEstablishDate(results.getDate("establish_date").toLocalDate());
		park.setArea(results.getInt("area"));
		park.setAnnualVisitorCount(results.getInt("visitors"));
		park.setDescription(results.getString("description"));
		
		return park;
	}
	
	

}
