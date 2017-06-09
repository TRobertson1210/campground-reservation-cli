package com.techelevator.campground;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;
import com.techelevator.campground.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.model.jdbc.JDBCParkDAO;
import com.techelevator.campground.model.jdbc.JDBCReservationDAO;
import com.techelevator.campground.model.jdbc.JDBCSiteDAO;
import com.techelevator.campground.view.Menu;


public class CampgroundCLI {

//Menu CONSTANTS to help navigate the user through the reservation process
	private static final String MAIN_MENU_OPTION_QUIT = "Quit";
	private static final String[] MAIN_MENU_OPTIONS = new String[] {  
			MAIN_MENU_OPTION_QUIT };

	private static final String MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";

	private static final String PARK_MENU_OPTION_ALL_CAMPGROUNDS = "Show all campgrounds";
	private static final String PARK_MENU_OPTION_SEARCH_FOR_RESERVATION = "Search for Reservation";
	private static final String[] PARK_MENU_OPTIONS = new String[] { PARK_MENU_OPTION_ALL_CAMPGROUNDS,
			PARK_MENU_OPTION_SEARCH_FOR_RESERVATION,
			MENU_OPTION_RETURN_TO_MAIN};

	private static final String CAMPGROUND_MENU_OPTION_SEARCH_RESERVATION = "Search for Available Reservations";
	private static final String CAMPGROUND_MENU_OPTION_PREVIOUS_MENU = "Return to Previous Screen";
	private static final String[] CAMPGROUND_MENU_OPTIONS = new String[] { CAMPGROUND_MENU_OPTION_SEARCH_RESERVATION,
			CAMPGROUND_MENU_OPTION_PREVIOUS_MENU};

	private static final String RESERVATION_MENU_OPTION_SELECT_CAMPGROUND = "Which campground (enter 0 to cancel)?";
	private static final String[] RESERVATION_MENU_OPTIONS = new String[] { RESERVATION_MENU_OPTION_SELECT_CAMPGROUND,
			MENU_OPTION_RETURN_TO_MAIN };
	
//Primary VARIABLES 	
	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private ReservationDAO reservationDAO;
	private SiteDAO siteDAO;

//This is what runs first when the program is executed.  
	public static void main(String[] args) {
		CampgroundCLI application = new CampgroundCLI();
		application.run();
	}

/*
 * The CONSTRUCTOR for the CampgroundCLI application. Here we creating a Menu object,
 * making a connection to the database, and creating DAO objects to be used later.
 */
	public CampgroundCLI() {
		this.menu = new Menu(System.in, System.out);

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		parkDAO = new JDBCParkDAO(dataSource);
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);
		siteDAO = new JDBCSiteDAO(dataSource);
	}

/*
 * The run() method that executes the start up of the program application. Here we are displaying a method
 * that shows a banner of the application name; uses a map to hold the list of parks data being retrieves from the 
 * campgrounds database;then displays a list of the parks as choices for the user to select.
 */
	public void run() {
		displayApplicationBanner();
		
		Map<String, Park> parks = parkDAO.getAvailableParks();
		Set<String> parkSet = parks.keySet();
		Object[] parkOptionArray = new Object[parkSet.size()+1];
		Object[] parkSetArray = parkSet.toArray();
		for(int i = 0; i < parkSet.size(); i++) {
			parkOptionArray[i] = parkSetArray[i];
		}
		parkOptionArray[parkSet.size()] = "Quit";

		while(true) {
			printHeading("View Parks Interface");
			String choice = (String)menu.getChoiceFromOptions(parkOptionArray);
			if(choice.equals("Quit")) {
				System.exit(0);
			} else {
				/*
				 * The user's choice of park is associated to the park_id so we can carry the park_id through 
				 * the program, using the selectedPark variable, so we can call relative information specific 
				 * to this park_id.
				 */
				Park selectedPark = parks.get(choice);
				handleParkInfoScreen(selectedPark);
			}	
		}
	}

//Handle Methods execute based on the user's choice
	/*
	 * This handle runs code that will display the park information details specific to the park_id that 
	 * is passed through the selectedPark variable.
	 * 
	 * In addition, we are displaying the next set of options for the user to continue through the process
	 * or return to previous screen.
	 */
	private void handleParkInfoScreen(Park selectedPark) {
		printHeading("Park Information Screen");
		selectedPark.printParkInfo();
		String choice = (String)menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
		if(choice.equals(PARK_MENU_OPTION_ALL_CAMPGROUNDS)) {
			handleListAllCampgrounds(selectedPark);
		} else if(choice.equals(PARK_MENU_OPTION_SEARCH_FOR_RESERVATION)) {
			handleSearchForAvailableSites();
		}
	}

	private void handleListAllCampgrounds(Park selectedPark) {
		printHeading("Park Campgrounds");
		List<Campground> campgroundList = campgroundDAO.getAllCampgrounds(selectedPark);
		for(Campground element : campgroundList) {
			element.printCampgroundDetails();
		}
		String choice = (String)menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);
		if(choice.equals(CAMPGROUND_MENU_OPTION_SEARCH_RESERVATION)) {
			handleSearchForAvailableSites();
		} else {
			handleParkInfoScreen(selectedPark);
		}
	}

	private void handleSearchForAvailableSites() {
		printHeading("Search for Campground Sites");
		String campgroundSelect = getUserInput("Which campground number (enter 0 to cancel)?");
		Long campgroundId = Long.valueOf(campgroundSelect);
		LocalDate arrivalDate = null;
		LocalDate departureDate = null;
		do {
			try {
				String arrivalDateString = getUserInput("What is the arrival date YYYY-MM-DD");
				arrivalDate = stringToLocalDate(arrivalDateString);
			} catch (DateTimeParseException e) {
				System.out.println("Incorrect date format");
			}
		} while(arrivalDate == null);
		do {
			try {
				String departureDateString = getUserInput("What is the departure date YYYY-MM-DD");
				departureDate = stringToLocalDate(departureDateString);
			} catch (DateTimeParseException e) {
				System.out.println("Incorrect date format");
			}
		} while (departureDate == null);
		
		//Find a way to break out if there are no available campsites
		List<Site> availableSites= siteDAO.getAllAvailableSites(campgroundId, arrivalDate, departureDate);
		Campground campground = campgroundDAO.getCampgroundById(campgroundId);
		System.out.println("Results Matching Your Search Criteria");
		System.out.println("Site No.\tMaxOccup.\tAccessible?\tMax RV Length\tUtiltiy\tCost");
		if(availableSites.isEmpty()) {
			System.out.println("There are no campsites available for that date range.");
			
		} else {
			for(Site element : availableSites) {
				System.out.println(element.getId() + "\t" + element.getMaxOccupancy() + element.isAccessible() + element.getMaxRVLength() + element.isUtilities() + campground.getDailyFee());
			}
		}

	}

	private void handleMakeReservation() {
		
	}

	private LocalDate stringToLocalDate(String string) {
		LocalDate localDate = LocalDate.parse(string);
		return localDate;
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

		System.out.println(" _   _       _   _                   _   _____           _");        
		System.out.println("| \\ | |     | | (_)                 | | |  __ \\         | |");        
		System.out.println("|  \\| | __ _| |_ _  ___  _ __   __ _| | | |__) |_ _ _ __| | _____");  
		System.out.println("| . ` |/ _` | __| |/ _ \\| '_ \\ / _` | | |  ___/ _` | '__| |/ / __|"); 
		System.out.println("| |\\  | (_| | |_| | (_) | | | | (_| | | | |  | (_| | |  |   <\\__ \\"); 
		System.out.println("|_|_\\_|\\__,_|\\__|_|\\___/|_| |_|\\__,_|_| |_|   \\__,_|_|  |_|\\_\\___/"); 
		System.out.println(" / ____|                                                    | |    ");
		System.out.println("| |     __ _ _ __ ___  _ __   __ _ _ __ ___  _   _ _ __   __| |___ ");
		System.out.println("| |    / _` | '_ ` _ \\| '_ \\ / _` | '__/ _ \\| | | | '_ \\ / _` / __|");
		System.out.println("| |___| (_| | | | | | | |_) | (_| | | | (_) | |_| | | | | (_| \\__ \\");
		System.out.println(" \\_____\\__,_|_| |_| |_| .__/ \\__, |_|  \\___/ \\__,_|_| |_|\\__,_|___/");
		System.out.println("                      | |     __/ |                                ");
		System.out.println("                      |_|    |___/                                 ");

	}
}

