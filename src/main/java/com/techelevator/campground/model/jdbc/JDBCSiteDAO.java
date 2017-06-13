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
	public List<Site> getAllAvailableSites(Long campgroundId, LocalDate fromDate, LocalDate toDate) {
		List<Site> allAvailableSites = new ArrayList<>();

		SqlRowSet results;

		String sqlAllAvailableSites = "SELECT * FROM site WHERE campground_id = ? AND site_id NOT IN (SELECT site.site_id "
				+ "FROM reservation " + "JOIN site ON site.site_id = reservation.site_id "
				+ "JOIN campground ON campground.campground_id = site.campground_id " + "WHERE site.campground_id = ? "
				+ "AND ((?  BETWEEN reservation.from_date AND reservation.to_date) "
				+ "OR (?  BETWEEN reservation.from_date AND reservation.to_date) "
				+ "OR (reservation.from_date BETWEEN ? AND ?) " + "OR (reservation.to_date BETWEEN ? AND ?))) "
				+ "LIMIT 5";
		results = template.queryForRowSet(sqlAllAvailableSites, campgroundId, campgroundId, fromDate, toDate, fromDate,
				toDate, fromDate, toDate);
		while (results.next()) {
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
		site.setMaxOccupancy(results.getInt("max_occupancy"));
		site.setAccessible(results.getBoolean("accessible"));
		site.setMaxRVLength(results.getInt("max_rv_length"));
		site.setUtilities(results.getBoolean("utilities"));

		return site;
	}

}
