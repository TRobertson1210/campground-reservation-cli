package com.techelevator.campground;

import java.util.List;
import java.util.Scanner;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.model.jdbc.JDBCParkDAO;
import com.techelevator.campground.model.jdbc.JDBCReservationDAO;
import com.techelevator.campground.view.Menu;


public class CampgroundCLI {
	
	private static final String MAIN_MENU_OPTION_ACADIA = "Acadia";
	private static final String MAIN_MENU_OPTION_ARCHES = "Arches";
	private static final String MAIN_MENU_OPTION_CUYAHOGA_NATIONAL_VALLEY_PARK = "Cuyahoga National Valley Park";
	private static final String MAIN_MENU_OPTION_QUIT = "Quit";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_ACADIA, 
																	 MAIN_MENU_OPTION_ARCHES, 
																	 MAIN_MENU_OPTION_CUYAHOGA_NATIONAL_VALLEY_PARK, 
																	 MAIN_MENU_OPTION_QUIT };

	private static final String MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";

	private static final String PARK_MENU_OPTION_ALL_CAMPGROUNDS = "Show all campgrounds";
	private static final String PARK_MENU_OPTION_SEARCH_FOR_RESERVATION = "Search for Reservation";
	private static final String[] PARK_MENU_OPTIONS = new String[] { PARK_MENU_OPTION_ALL_CAMPGROUNDS,
																		   PARK_MENU_OPTION_SEARCH_FOR_RESERVATION,
																		   MENU_OPTION_RETURN_TO_MAIN};
	
	private static final String CAMPGROUND_MENU_OPTION_SEARCH_RESERVATION = "Search for Available Reservations";
	private static final String[] CAMPGROUND_MENU_OPTIONS = new String[] { CAMPGROUND_MENU_OPTION_SEARCH_RESERVATION,
																	 MENU_OPTION_RETURN_TO_MAIN};
	
	private static final String RESERVATION_MENU_OPTION_SELECT_CAMPGROUND = "Which campground (enter 0 to cancel)?";
	private static final String[] RESERVATION_MENU_OPTIONS = new String[] { RESERVATION_MENU_OPTION_SELECT_CAMPGROUND,
																	 MENU_OPTION_RETURN_TO_MAIN };
	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private ReservationDAO reservationDAO;

	public static void main(String[] args) {
		CampgroundCLI application = new CampgroundCLI();
		application.run();
	}

	public CampgroundCLI() {
		this.menu = new Menu(System.in, System.out);
		
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		parkDAO = new JDBCParkDAO(dataSource);
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);
	}
	
	public void run() {
		displayApplicationBanner();	
		while(true) {
			printHeading("View Parks Interface");
			String choice = (String)menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(choice.equals(MAIN_MENU_OPTION_ACADIA)) {
				handleParkInfoScreen();
			} else if(choice.equals(MAIN_MENU_OPTION_ARCHES)) {
				handleEmployees();
			} else if(choice.equals(MAIN_MENU_OPTION_CUYAHOGA_NATIONAL_VALLEY_PARK)) {
				handleProjects();
			} else if(choice.equals(MAIN_MENU_OPTION_QUIT)) {
				System.exit(0);
			}
		}
	}

	private void handleParkInfoScreen() {
		printHeading("Park Information Screen");
		String choice = (String)menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
		if(choice.equals(PARK_MENU_OPTION_ALL_CAMPGROUNDS)) {
			handleListAllCampgrounds();
		} else if(choice.equals(PARK_MENU_OPTION_SEARCH_FOR_RESERVATION)) {
			handleSearchForReservation();
		}
	}

	private void handleAddDepartment() {
		printHeading("Add New Department");
		String newDepartmentName = getUserInput("Enter new Department name");
		Department newDepartment = departmentDAO.createDepartment(newDepartmentName);
		System.out.println("\n*** "+newDepartment.getName()+" created ***");
	}
	
	private void handleUpdateDepartmentName() {
		printHeading("Update Department Name");
		List<Department> allDepartments = departmentDAO.getAllDepartments();
		if(allDepartments.size() > 0) {
			System.out.println("\n*** Choose a Department ***");
			Department selectedDepartment = (Department)menu.getChoiceFromOptions(allDepartments.toArray());
			String newDepartmentName = getUserInput("Enter new Department name");
			departmentDAO.updateDepartmentName(selectedDepartment.getId(), newDepartmentName);
		} else {
			System.out.println("\n*** No results ***");
		}
	}

	private void handleListAllCampgrounds() {
		printHeading("All Departments");
		List<Department> allDepartments = departmentDAO.getAllDepartments();
		listDepartments(allDepartments);
	}

	private void handleSearchForReservation() {
		printHeading("Search For Reservation");
		String departmentSearch = getUserInput("Enter department name to search for");
		List<Reservation> reservations = reservationDAO.searchDepartmentsByName(departmentSearch);
		listDepartments(departments);
	}

	private void printHeading(String headingText) {
		System.out.println("\n"+headingText);
		for(int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
	
	private String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}

	private void displayApplicationBanner() {
		System.out.println(" ______                 _                         _____           _           _     _____  ____");
		System.out.println("|  ____|               | |                       |  __ \\         (_)         | |   |  __ \\|  _ \\");
		System.out.println("| |__   _ __ ___  _ __ | | ___  _   _  ___  ___  | |__) | __ ___  _  ___  ___| |_  | |  | | |_) |");
		System.out.println("|  __| | '_ ` _ \\| '_ \\| |/ _ \\| | | |/ _ \\/ _ \\ |  ___/ '__/ _ \\| |/ _ \\/ __| __| | |  | |  _ <");
		System.out.println("| |____| | | | | | |_) | | (_) | |_| |  __/  __/ | |   | | | (_) | |  __/ (__| |_  | |__| | |_) |");
		System.out.println("|______|_| |_| |_| .__/|_|\\___/ \\__, |\\___|\\___| |_|   |_|  \\___/| |\\___|\\___|\\__| |_____/|____/");
		System.out.println("                 | |             __/ |                          _/ |");
		System.out.println("                 |_|            |___/                          |__/");
		System.out.println();
	}
	}
}
