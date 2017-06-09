package com.techelevator.campground.model.jdbc;

import java.time.LocalDate;
import java.util.Calendar;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate template;

	public JDBCReservationDAO(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}
	@Override
	public Long bookReservation(String name, Long siteId, LocalDate arrivalDate, LocalDate departureDate) {
		String sqlInsertStatement = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date) VALUES "
				+ "(?, ?, ?, ?, NOW()) RETURNING reservation_id";
		SqlRowSet results = template.queryForRowSet(sqlInsertStatement, siteId, name, arrivalDate, departureDate);
		Long reservationId = null;
		if(results.next()){
			reservationId = results.getLong("reservation_id");
		}
		return reservationId;
	}
	
	public Reservation mapRowToReservation(SqlRowSet results) {
		Reservation reservation;
		reservation = new Reservation();
		
		reservation.setId(results.getLong("reservation_id"));
		reservation.setName(results.getString("name"));
		reservation.setFromDate(results.getDate("from_date").toLocalDate());
		reservation.setToDate(results.getDate("to_date").toLocalDate());
		reservation.setCreateDate(results.getDate("create_date").toLocalDate());
		
		return reservation;
	}
}
