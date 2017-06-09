package com.techelevator.campground.model.jdbc;

import static org.junit.Assert.fail;

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
		JdbcTemplate template = new JdbcTemplate(dataSource);
		dao = new JDBCParkDAO(dataSource);
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void testGetAvailableParks() {
		SqlRowSet results = template.
		
		Map<String, Park> expectedPark = new TreeMap<>();
		expectedPark.put("TEST PARK", value)
		
		assertEquals(expectedPark.size(), dao.getAvailableParks().size());
	}

	@Test
	public void testMapRowToPark() {
		fail("Not yet implemented");
	}

}
