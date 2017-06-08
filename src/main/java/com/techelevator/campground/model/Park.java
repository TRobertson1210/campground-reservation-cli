package com.techelevator.campground.model;

import java.time.LocalDate;

public class Park {

	private Long id;
	private String name;
	private String location;
	private LocalDate establishDate;
	private int area;
	private int annualVisitorCount; 
	private String description;
	
	public void printParkInfo() {
		System.out.println(getName() + " National Park");
		System.out.println("Location: " + getLocation());
		System.out.println("Established: " + getEstablishDate());
		System.out.println("Area: " + getArea() + " km^2");
		System.out.println("Annual Visitors: " + getAnnualVisitorCount());
		System.out.println();
		System.out.println(getDescription());
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public LocalDate getEstablishDate() {
		return establishDate;
	}
	public void setEstablishDate(LocalDate establishDate) {
		this.establishDate = establishDate;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public int getAnnualVisitorCount() {
		return annualVisitorCount;
	}
	public void setAnnualVisitorCount(int annualVisitorCount) {
		this.annualVisitorCount = annualVisitorCount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
