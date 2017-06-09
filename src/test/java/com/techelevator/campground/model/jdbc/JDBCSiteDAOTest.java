package com.techelevator.campground.model.jdbc;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;


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
		fail("Not yet implemented");
	}

	@Test
	public void testMapRowToSite() {
		fail("Not yet implemented");
	}

}
