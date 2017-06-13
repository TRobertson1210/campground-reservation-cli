package com.techelevator.campground;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.Reservation;
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
	//private static final String PARK_MENU_OPTION_SEARCH_FOR_RESERVATION = "Search for Reservation";
	private static final String[] PARK_MENU_OPTIONS = new String[] { PARK_MENU_OPTION_ALL_CAMPGROUNDS,
			/*PARK_MENU_OPTION_SEARCH_FOR_RESERVATION,*/
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
	private Park selectedPark = null;

	//This is what runs first when the program is executed.  
	public static void main(String[] args) {
		CampgroundCLI application = new CampgroundCLI();
		application.run();
	}

	/*
	 * The CONSTRUCTOR for the CampgroundCLI application. Here we are creating a Menu object,
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
	 * The run() method executes the start up of the program application. Here we are displaying a method
	 * that shows a banner of the application name; creating a map to hold the list of parks data being retrieved from the 
	 * campgrounds database; then displays a list of the parks as choices for the user to select.
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
				 * The user's choice of parks is associated to the park_id so we can carry the park_id through 
				 * the program, using the selectedPark variable. This will allow us to call relative information 
				 * connected to this park_id.
				 */
				selectedPark = parks.get(choice);
				handleParkInfoScreen();
			}	
		}
	}

	//Handle Methods are executed based on the user's choice
	/*
	 * handleParkInfoScreen displays the park information details specific to the park_id that 
	 * is passed through the selectedPark variable.
	 * 
	 * In addition, we are displaying the next set of options for the user to continue through the process
	 * or return to previous screen.
	 */
	private void handleParkInfoScreen() {
		printHeading("Park Information Screen");
		selectedPark.printParkInfo();
		String choice = (String)menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
		if(choice.equals(PARK_MENU_OPTION_ALL_CAMPGROUNDS)) {
			handleListAllCampgrounds();
		} /*else if(choice.equals(PARK_MENU_OPTION_SEARCH_FOR_RESERVATION)) {
			handleSearchForAvailableSites();
		}*/
	}
	List<Campground> campgroundList = null; //created outside for use in multiple handles

	/*
	 * handleListAllCampgrounds displays a list of all campgrounds associated with the selectedPark
	 */
	private void handleListAllCampgrounds() {
		printHeading("Park Campgrounds");
		campgroundList = campgroundDAO.getAllCampgrounds(selectedPark);
		//System.out.println("ID\tName\t\tOpen\tClose\tDaily Fee");
		System.out.printf("%-5s %-20s %-10s %-10s %-10s %n", "ID", "Name", "Open", "Close", "Daily Fee");
		for(Campground element : campgroundList) {
			element.printCampgroundDetails();
		}
		String choice = (String)menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);
		if(choice.equals(CAMPGROUND_MENU_OPTION_SEARCH_RESERVATION)) {
			handleSearchForAvailableSites();
		} else {
			handleParkInfoScreen();
		}
	}
	
	

	private void handleSearchForAvailableSites() {
		//Variables to store values for reservation
		LocalDate arrivalDate = null;
		LocalDate departureDate = null;
		Long campgroundId = null;
		Long siteId = null;
		
		printHeading("Search for Campground Sites");
		String campgroundSelect = getUserInput("Which campground number (enter 0 to cancel)?");
		campgroundId = Long.valueOf(campgroundSelect);
		int campgroundSelectInt = Integer.parseInt(campgroundSelect);
		if (campgroundSelectInt <= campgroundList.size()) {
			if (campgroundSelectInt > 0) {
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
						if(departureDate.isBefore(arrivalDate)) {
							System.out.println("Please make sure your departure date is after your arrival.");
							departureDate = null;
						}
					} catch (DateTimeParseException e) {
						System.out.println("Incorrect date format");
					}
				} while (departureDate == null);

				List<Site> availableSites= siteDAO.getAllAvailableSites(campgroundId, arrivalDate, departureDate);
				Campground campground = campgroundDAO.getCampgroundById(campgroundId);
				if(campground == null) {
					System.out.println("Campground not found!");
					return;
				}
				System.out.println("Results Matching Your Search Criteria");
				//System.out.println("Site No.\t MaxOccup.\t Accessible?\t Max RV Length\t Utilities\t Total Price");
				System.out.printf("%-10s %-15s %-15s %-15s %-15s %-10.2f %n", "Site No.", "MaxOccup.", "Accessible?", "Max RV Length", "Utilities", "Total Price");
				Long daysBetween = ChronoUnit.DAYS.between(arrivalDate, departureDate);
				BigDecimal daysBetweenBD = new BigDecimal(daysBetween);
				if(availableSites.isEmpty()) {
					System.out.println("There are no campsites available for that date range.");
					handleSearchForAvailableSites();

				} else {
					for(Site element : availableSites) {
						System.out.printf("%-10s %-15s %-15s %-15s %-15s $%-10s %n", element.getId(), element.getMaxOccupancy(), element.isAccessible(), element.getMaxRVLength(), 
								element.isUtilities(), (daysBetweenBD.multiply(campground.getDailyFee())));
						//System.out.println(element.getId() + "\t" + element.getMaxOccupancy() +"\t" + element.isAccessible() +"\t"+ element.getMaxRVLength() +"\t"+ element.isUtilities() +"\t"+ (daysBetweenBD.multiply(campground.getDailyFee())));
					}
					handleMakeReservation(arrivalDate, departureDate, siteId);
				}
			} else {
				handleListAllCampgrounds();
			}
		} else {
			System.out.println("\n*** "+campgroundSelect+" is not a valid option for this park. ***\n");
			handleSearchForAvailableSites();
		}

	}

	private void handleMakeReservation(LocalDate arrivalDate, LocalDate departureDate, Long siteId) {
		String siteSelect = getUserInput("Which site should be reserved? (enter 0 to cancel)?");
		siteId = Long.valueOf(siteSelect);
		if(siteId > 0) {
			String reservedNameString = getUserInput("What name to reserve site under?");
			Reservation reservationInfo = reservationDAO.bookReservation(reservedNameString, siteId, arrivalDate, departureDate);

			System.out.println("Your reservation has been made. Please copy your confirmation number for your records: " + reservationInfo.getId());
		} else {
			handleListAllCampgrounds();
		}

	}


	//Toolbox Methods
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

