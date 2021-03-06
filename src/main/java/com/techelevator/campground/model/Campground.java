package com.techelevator.campground.model;

import java.math.BigDecimal;

public class Campground {

	private Long id;
	private String name;
	private String openMonth;
	private String closeMonth;
	private BigDecimal dailyFee;
	
	public void printCampgroundDetails() {
		//System.out.println("Name\tOpen\tClose\tDaily Fee");
		//System.out.println("#" + getId() + "\t" + getName() + "\t\t"+ getOpenMonth() + "\t" + getCloseMonth() + "\t$" + getDailyFee());
		System.out.printf("%-5s %-20s %-10s %-10s $%-10.2f %n", getId(), getName(), getOpenMonth(), getCloseMonth(), getDailyFee());
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
	public String getOpenMonth() {
		return openMonth;
	}
	public void setOpenMonth(String openMonth) {
		this.openMonth = openMonth;
	}
	public String getCloseMonth() {
		return closeMonth;
	}
	public void setCloseMonth(String closeMonth) {
		this.closeMonth = closeMonth;
	}
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}
	
	
}
