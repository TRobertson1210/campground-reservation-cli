package com.techelevator.campground.model;

public class Site {

	private Long id;
	private int siteNumber;
	private int maxOccupancy;
	private boolean accessible;
	private int maxRVLenth;
	private boolean utilities;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getSiteNumber() {
		return siteNumber;
	}
	public void setSiteNumber(int siteNumber) {
		this.siteNumber = siteNumber;
	}
	public int getMaxOccupancy() {
		return maxOccupancy;
	}
	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	public boolean isAccessible() {
		return accessible;
	}
	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}
	public int getMaxRVLenth() {
		return maxRVLenth;
	}
	public void setMaxRVLenth(int maxRVLenth) {
		this.maxRVLenth = maxRVLenth;
	}
	public boolean isUtilities() {
		return utilities;
	}
	public void setUtilities(boolean utilities) {
		this.utilities = utilities;
	}
	
}
