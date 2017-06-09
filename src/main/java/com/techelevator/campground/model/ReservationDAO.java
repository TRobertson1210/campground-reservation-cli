package com.techelevator.campground.model;

import java.time.LocalDate;

public interface ReservationDAO {

	public Long bookReservation(String name, Long siteId, LocalDate arrivalDate, LocalDate departureDate);
}
