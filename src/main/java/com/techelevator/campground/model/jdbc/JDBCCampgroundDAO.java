package com.techelevator.campground.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;

public class JDBCCampgroundDAO implements CampgroundDAO {
	
	private JdbcTemplate template;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Campground> getAllCampgrounds(Park parkChoice) {
		List<Campground> allCampgrounds = new ArrayList<>();
		Long parkId = parkChoice.getId();
		String sqlAllCampgrounds = "SELECT * FROM campground WHERE park_id = ?";
		SqlRowSet results = template.queryForRowSet(sqlAllCampgrounds, parkId);
		while(results.next()) {
			Campground campground = mapRowToCampground(results);
			allCampgrounds.add(campground);
		}
		return allCampgrounds;
	}
	
	public Campground getCampgroundById(Long campgroundId) {
		Campground campground = null;
		String sqlGetCampgroundById = "SELECT * FROM campground WHERE campground_id = ?";
		SqlRowSet results = template.queryForRowSet(sqlGetCampgroundById, campgroundId);
		if (results.next()) {
			campground = mapRowToCampground(results);
		}
		return campground;
	}
	
	public Campground mapRowToCampground(SqlRowSet results) {
		Campground campground;
		campground = new Campground();
		campground.setId(results.getLong("campground_id"));
		campground.setName(results.getString("name"));
		campground.setOpenMonth(results.getString("open_from_mm"));
		campground.setCloseMonth(results.getString("open_to_mm"));
		campground.setDailyFee(results.getBigDecimal("daily_fee"));
		
		return campground;
	}

}
