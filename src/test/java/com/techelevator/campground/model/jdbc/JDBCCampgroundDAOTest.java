package com.techelevator.campground.model.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
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
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;

public class JDBCCampgroundDAOTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCCampgroundDAO dao;
	
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
		dao = new JDBCCampgroundDAO(dataSource);
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void testGetAllCampgrounds() {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		ParkDAO parkDAO = new JDBCParkDAO(dataSource);
		Map<String, Park> expectedParkMap = parkDAO.getAvailableParks();
		Park expectedPark = expectedParkMap.get("TEST PARK");
		SqlRowSet results = template.queryForRowSet("SELECT * FROM campground WHERE name = 'CAMPGROUND A'");
		List<Campground> expectedCampground = new ArrayList<>();
		

		if(results.next()){
			expectedCampground.add(dao.mapRowToCampground(results));
		}
		assertEquals(expectedCampground.size(), dao.getAllCampgrounds(expectedPark).size());
	}
	

	@Test
	public void testGetCampgroundById() {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		SqlRowSet results = template.queryForRowSet("SELECT * FROM campground WHERE name = 'CAMPGROUND A'");
		Campground campground = null;
		if(results.next()) {
			campground = dao.mapRowToCampground(results);
		}
		
		assertEquals(campground.getName(), dao.getCampgroundById(campground.getId()).getName());
	}

	@Test
	public void testMapRowToCampground() {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		SqlRowSet results = template.queryForRowSet("SELECT * FROM campground WHERE name = 'CAMPGROUND A'");
		BigDecimal dailyFee = new BigDecimal("500.0");
		
		if(results.next()) {
			assertEquals(Long.valueOf(1), dao.mapRowToCampground(results).getId());
			assertEquals("CAMPGROUND A", dao.mapRowToCampground(results).getName());
			assertEquals("01", dao.mapRowToCampground(results).getOpenMonth());
			assertEquals("12", dao.mapRowToCampground(results).getCloseMonth());
			assertEquals(dailyFee, dao.mapRowToCampground(results).getDailyFee());
		}
	}

}
