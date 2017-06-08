package com.techelevator.campground.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {

	private JdbcTemplate template;

	public JDBCSiteDAO(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Site> getAllAvailableSites(String name, LocalDate fromDate, LocalDate toDate) {
		List<Site> allAvailableSites = new ArrayList<>();
		String sqlGetCampgroundId = "SELECT campground_id FROM campground WHERE name = ?";
		SqlRowSet results = template.queryForRowSet(sqlGetCampgroundId, name);
		Long campgroundId = results.getLong("campground_id");
		
		String sqlGetParkId = "SELECT park_id FROM campground WHERE name = ?";
		results = template.queryForRowSet(sqlGetParkId, name);
		Long parkId = results.getLong("park_id");
		
		String sqlAllAvailableSites = "SELECT site_number FROM reservation JOIN site ON site.site_id = reservation.site_id "
				+ "JOIN campground ON campground.campground_id = site.campground_id "
				+ "WHERE park_id = ? AND campground_id = ? AND ? NOT BETWEEN reservation.from_date AND reservation.to_date AND ? NOT BETWEEN resercation.from_date AND reservation.to_date";
		results = template.queryForRowSet(sqlAllAvailableSites, parkId, campgroundId, fromDate, toDate);
		while(results.next()) {
			Site site = mapRowToSite(results);
			allAvailableSites.add(site);
		}
		return allAvailableSites;
	}
	
	public Site mapRowToSite(SqlRowSet results) {
		Site site;
		site = new Site();
		site.setId(results.getLong("site_id"));
		site.setSiteNumber(results.getInt("site_number"));
		site.setMaxOccupancy(results.getInt("site_occupancy"));
		site.setAccessible(results.getBoolean("accessible"));
		site.setMaxRVLength(results.getInt("max_rv_length"));
		site.setUtilities(results.getBoolean("utilities"));
		
		return site;
	}

}
