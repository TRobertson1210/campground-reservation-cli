package com.techelevator.campground.model.jdbc;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate template;

	public JDBCReservationDAO(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}
	@Override
	public Reservation bookReservation(String name, Long siteId, LocalDate arrivalDate, LocalDate departureDate) {
		String sqlInsertStatement = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date) VALUES "
				+ "(?, ?, ?, ?, NOW())";
		template.update(sqlInsertStatement, siteId, name, arrivalDate, departureDate);
		String sqlGetReservationInfo = "SELECT * FROM reservation WHERE name = ? AND from_date = ? AND to_date = ?";
		SqlRowSet results = template.queryForRowSet(sqlGetReservationInfo, name, arrivalDate, departureDate);
		Reservation reservation = null;
		if(results.next()){
			reservation = mapRowToReservation(results);
		}
		return reservation;
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
