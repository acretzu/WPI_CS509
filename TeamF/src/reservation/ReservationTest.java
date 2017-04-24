package reservation;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.ArrayList;

import org.junit.Test;

public class ReservationTest {

	// Class variables
	ArrayList<String> list;
	Reservation reservation;
	String test_flight;
	int max_available_seats;
	
	@BeforeClass
	public static void start() {
		System.out.println("ReservationTest has started.");
	}
	
	@Before
	public void initializeVariables() {
		list = new ArrayList<String>();
		reservation = new Reservation();
		test_flight = "6362"; // Known good flight
		max_available_seats = 17;
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
		list.add(test_flight);
		assertTrue("Coach seat on flight " +test_flight + " has not been successfully reserved!", reservation.reserveCoach(list));
	}
	
	@Test
	public void ReserveFirstClass() {
		list.add(test_flight);
		assertTrue("FirstClass seat on flight " + test_flight + " has not been successfully reserved!", reservation.reserveFirstClass(list));
	}
	
	@Test
	public void ReserveUntilFull() {
		list.add(test_flight);
		
		for(int i = 0; i < max_available_seats; i++) {
			assertTrue("FirstClass seat on flight " + test_flight + " has not been successfully reserved!", reservation.reserveFirstClass(list));
		}
		assertFalse("Reserving more seats than available!", reservation.reserveFirstClass(list));
	}
	
	@AfterClass
	public static void complete() {
		System.out.println("ReservationTest has completed.");
	}
}

