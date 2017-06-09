package com.techelevator.campground.model.jdbc;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Park;

public class JDBCParkDAOTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCParkDAO dao;

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
		dao = new JDBCParkDAO(dataSource);

	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void testGetAvailableParks() {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		SqlRowSet results = template.queryForRowSet("SELECT * FROM park WHERE name = 'TEST PARK'");
		Map<String, Park> expectedPark = new TreeMap<>();


		if(results.next()){
			expectedPark.put(dao.mapRowToPark(results).getName(), dao.mapRowToPark(results));
		}
		assertEquals(expectedPark.size(), dao.getAvailableParks().size());
	}

	@Test
	public void testMapRowToPark() {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		SqlRowSet results = template.queryForRowSet("SELECT * FROM park WHERE name = 'TEST PARK'");
		LocalDate expectedDate = LocalDate.of(2017, 6, 8);

		if(results.next()) {
			assertEquals("TEST PARK", dao.mapRowToPark(results).getName());
			assertEquals("Ohio", dao.mapRowToPark(results).getLocation());
			assertEquals(expectedDate, dao.mapRowToPark(results).getEstablishDate());
			assertEquals(0, dao.mapRowToPark(results).getArea());
			assertEquals(10, dao.mapRowToPark(results).getAnnualVisitorCount());
			assertEquals("This is a test park.", dao.mapRowToPark(results).getDescription());
		}
	}

}
