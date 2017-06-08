package com.techelevator.campground.model;

import java.time.LocalDate;
import java.util.List;

public interface CampgroundDAO {

	public List<Campground> getAllCampgrounds(Park parkChoice);
	public Campground getCampgroundById(Long campgroundId);
}
