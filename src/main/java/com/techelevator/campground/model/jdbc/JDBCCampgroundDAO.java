package com.techelevator.campground.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Site;

public class JDBCCampgroundDAO implements CampgroundDAO {
	
	private JdbcTemplate template;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Campground> getAllCampgrounds(Long parkId) {
		List<Campground> allCampgrounds = new ArrayList<>();
		String sqlAllCampgrounds = "SELECT * FROM campground WHERE park_id = ?";
		SqlRowSet results = template.queryForRowSet(sqlAllCampgrounds, parkId);
		while(results.next()) {
			Campground campground = mapRowToCampground(results);
			allCampgrounds.add(campground);
		}
		return allCampgrounds;
	}
	
	public Campground mapRowToCampground(SqlRowSet results) {
		Campground campground;
		campground = new Campground();
		campground.setId(results.getLong("campground_id"));
		campground.setName(results.getString("name"));
		campground.setOpenMonth(results.getString("open_from_mm"));
		campground.setCloseMonth(results.getString("close_from_mm"));
		campground.setDailyFee(results.getBigDecimal("daily_fee"));
		
		return campground;
	}

}
