package com.techelevator.campground.model.jdbc;

import static org.junit.Assert.assertEquals;

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
import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.Site;

public class JDBCReservationDAOTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCReservationDAO dao;

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
		dao = new JDBCReservationDAO(dataSource);
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void testBookReservation() {
		LocalDate fromDate = LocalDate.of(2017, 06, 8);
		LocalDate toDate = LocalDate.of(2017, 06, 9);
		
		Reservation reservation = dao.bookReservation("Test Reservation", Long.valueOf(1), fromDate, toDate);
		
		assertEquals("Test Reservation", reservation.getName());
	}
	

	@Test
	public void testMapRowToReservation() {
		JdbcTemplate template = new JdbcTemplate(dataSource);
		SqlRowSet results = template.queryForRowSet("SELECT * FROM reservation WHERE name = 'Test Family'");
		LocalDate fromDate = LocalDate.of(1952, 1, 1);
		LocalDate toDate = LocalDate.of(2107, 12, 31);
		LocalDate createDate = LocalDate.of(1951, 1, 1);
		
		
		if(results.next()) {
			assertEquals(Long.valueOf(1), dao.mapRowToReservation(results).getId());
			assertEquals("Test Family", dao.mapRowToReservation(results).getName());
			assertEquals(fromDate, dao.mapRowToReservation(results).getFromDate());
			assertEquals(toDate, dao.mapRowToReservation(results).getToDate());
			assertEquals(createDate, dao.mapRowToReservation(results).getCreateDate());
		}
	}

}
