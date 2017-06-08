package com.techelevator.campground.model;

import java.util.Map;

public interface ParkDAO {
	
	public Map<String, Park> getAvailableParks();

}
