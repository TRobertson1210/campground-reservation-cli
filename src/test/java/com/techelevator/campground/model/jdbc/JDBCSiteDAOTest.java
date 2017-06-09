package com.techelevator.campground.model.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.Site;


public class JDBCSiteDAOTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCSiteDAO dao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground_test");
		dataSource.setUsername("postgres");

		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dataSource.destroy();
	}

	@Before
	public void setUp() throws Exception {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		dao = new JDBCSiteDAO(dataSource);
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void testGetAllAvailableSites() {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		ParkDAO parkDAO = new JDBCParkDAO(dataSource);
		Map<String, Park> expectedParkMap = parkDAO.getAvailableParks();
		CampgroundDAO campgroundDAO = new JDBCCampgroundDAO(dataSource);
		Park expectedPark = expectedParkMap.get("TEST PARK");
		List<Campground> campgroundList = campgroundDAO.getAllCampgrounds(expectedPark);
		SqlRowSet results = template.queryForRowSet("SELECT * FROM campground WHERE name = 'CAMPGROUND A'");
		Campground expectedCampground = campgroundList.get(0);
		results = template.queryForRowSet("SELECT * FROM site");
		List<Site> expectedSites = new ArrayList();
		LocalDate fromDate = LocalDate.of(2017, 06, 8);
		LocalDate toDate = LocalDate.of(2017, 06, 9);

		while(results.next()){
			expectedSites.add(dao.mapRowToSite(results));
		}
		assertEquals(Long.valueOf(1), dao.getAllAvailableSites(expectedCampground.getId(), fromDate, toDate).get(0).getId());
	}


	@Test
	public void testMapRowToSite() {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		SqlRowSet results = template.queryForRowSet("SELECT * FROM site WHERE site_id = 1");

		if(results.next()) {
			assertEquals(Long.valueOf(1), dao.mapRowToSite(results).getId());
			assertEquals(1, dao.mapRowToSite(results).getSiteNumber());
			assertEquals(10, dao.mapRowToSite(results).getMaxOccupancy());
			assertTrue(dao.mapRowToSite(results).isAccessible());
			assertEquals(0, dao.mapRowToSite(results).getMaxRVLength());
			assertTrue(dao.mapRowToSite(results).isUtilities());
		}
	}

}
