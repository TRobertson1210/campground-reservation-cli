package com.techelevator.campground.model;

import java.time.LocalDate;
import java.util.List;

public interface SiteDAO {

	public List<Site> getAllAvailableSites(Long campgroundId, LocalDate fromDate, LocalDate toDate);
}
