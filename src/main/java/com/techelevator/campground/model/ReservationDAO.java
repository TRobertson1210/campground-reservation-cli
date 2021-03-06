package com.techelevator.campground.model;

import java.time.LocalDate;

public interface ReservationDAO {

	public Reservation bookReservation(String name, Long siteId, LocalDate arrivalDate, LocalDate departureDate);
}
