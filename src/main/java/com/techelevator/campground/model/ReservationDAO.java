package com.techelevator.campground.model;

import java.time.LocalDate;

public interface ReservationDAO {

	public Long bookReservation(String campId, LocalDate arrivalDate, LocalDate departureDate);
}
