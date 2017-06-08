package com.techelevator.campground.model.jdbc;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate template;

	public JDBCReservationDAO(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}
	@Override
	public Long bookReservation(String campId, LocalDate arrivalDate, LocalDate departureDate) {
		Long campLongId = Long.valueOf(campId);
		
		
		
		
		String sqlInsertStatement = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date) VALUES "
				+ "(?, ?, ?, ?, NOW()) RETURNING reservation_id";
		SqlRowSet results = template.queryForRowSet(sqlInsertStatement, , name, , , );
		return reservationId;
	}

}
