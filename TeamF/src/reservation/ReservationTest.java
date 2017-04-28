package reservation;

import static org.junit.Assert.*;
import flights.*;
import org.junit.*;
import java.util.ArrayList;

import org.junit.Test;

public class ReservationTest {

	// Class variables
	DepartingFlightsContainer dep;
	ArrayList<String> list;
	Reservation reservation;
	String test_flight;
	
	@BeforeClass
	public static void start() {
		System.out.println("ReservationTest has started.");
	}
	
	@Before
	public void initializeVariables() {
		list = new ArrayList<String>();
		reservation = new Reservation();
		dep = new DepartingFlightsContainer();
	}
	
	@After
	public void resetServer(){
		reservation.resetServer();
	}
	
	@Test
	public void ReserveBadCoach() {
		list.add("thisisbad");
		assertFalse("Known bad flight has been incorrectly reserved!", reservation.reserveCoach(list));
	}
	
	@Test
	public void ReserveBadFirstClass() {					
		list.add("youcantseeme");		
		assertFalse("Known bad flight has been incorrectly reserved!", reservation.reserveFirstClass(list));		
	}

	@Test
	public void ReserveCoach() {		
		dep.parseDepartingFlightsFromSever("BOS", "2017_05_10");
		Flight temp = dep.getFlightByNumber("3311");
		System.out.println("Number of coach seats before = " + temp.get_coach());
		list.add(temp.get_flight_number());
		assertTrue("Coach seat on flight " + temp.get_flight_number() + " has not been successfully reserved!", reservation.reserveCoach(list));
		dep.parseDepartingFlightsFromSever("BOS", "2017_05_10");
		temp = dep.getFlightByNumber("3311");
		System.out.println("Number of coach seats after = " + temp.get_coach());
	}
	
	@Test
	public void ReserveFirstClass() {
		dep.parseDepartingFlightsFromSever("BOS", "2017_05_10");
		Flight temp = dep.getFlightByNumber("3311");
		System.out.println("Number of first class seats before = " + temp.get_first_class());
		list.add(temp.get_flight_number());
		assertTrue("FirstClass seat on flight " + temp.get_flight_number() + " has not been successfully reserved!", reservation.reserveFirstClass(list));
		dep.parseDepartingFlightsFromSever("BOS", "2017_05_10");
		temp = dep.getFlightByNumber("3311");
		System.out.println("Number of first class seats after = " + temp.get_first_class());
	}
	
	@Test
	public void ReserveUntilFull() {
		dep.parseDepartingFlightsFromSever("BOS", "2017_05_10");
		Flight temp = dep.getFlightByNumber("3311");
		System.out.println("Number of first class seats before = " + temp.get_first_class());
		list.add(temp.get_flight_number());
		for(int i = 0; i < 23; i++) {
			assertTrue("FirstClass seat on flight " + temp.get_flight_number() + " has not been successfully reserved!", reservation.reserveFirstClass(list));
		}
		dep.parseDepartingFlightsFromSever("BOS", "2017_05_10");
		temp = dep.getFlightByNumber("3311");
		System.out.println("Number of first class seats at maximum = " + temp.get_first_class());
		assertFalse("Reserving more seats than available!", reservation.reserveFirstClass(list));
		dep.parseDepartingFlightsFromSever("BOS", "2017_05_10");
		temp = dep.getFlightByNumber("3311");
		System.out.println("Number of first class seats after maximum = " + temp.get_first_class());
	}
	
	@AfterClass
	public static void complete() {
		System.out.println("ReservationTest has completed.");
	}
}

