package controller;

import airport.Airport;
import airport.AirportContainer;
import flights.DepartingFlightsContainer;
import flights.ArrivingFlightsContainer;
import flights.Flight;
import airplane.AirplaneContainer;
import airplane.Airplane;
import java.util.ArrayList;
/**
 * 
 * @author acretzu
 *
 */
public class Controller {
	
	//
	// Class variables
	//
	
	// Containers 
	private AirportContainer airports;
	private DepartingFlightsContainer depFlights;
	private AirplaneContainer airplanes;
	private ArrivingFlightsContainer arrFlights;
	
	public Controller() {
		// Initialize container classes
		airports = new AirportContainer();
		depFlights = new DepartingFlightsContainer();
		arrFlights = new ArrivingFlightsContainer();
		airplanes = new AirplaneContainer();
	}
	
	/**
	 * Query the server for a list of airports and returns ArrayList<Airport>
	 * 
	 * @return ArrayList of airports
	 */
	public ArrayList<Airport> getAirports() {
		airports.parseAirportsFromSever();
		return airports.getContainer();
	}
	
	/**
	 * Query the server for a list of departing flights and returns ArrayList<Flight>
	 * 
	 * @return ArrayList of departing flights
	 */
	public ArrayList<Flight> getDepartingFlights(String airport, String date) {
		depFlights.parseDepartingFlightsFromSever(airport, date);
		return depFlights.getContainer();
	}
	
	/**
	 * Query the server for a list of arriving flights and returns ArrayList<Flight>
	 * 
	 * @return ArrayList of arriving flights
	 */
	public ArrayList<Flight> getArrivingFlights(String airport, String date) {
		arrFlights.parseArrivingFlightsFromSever(airport, date);
		return arrFlights.getContainer();
	}
	
	public void createAirplanes(){
		// Create the airports container	
		//AirplaneContainer 
		airplanes = new AirplaneContainer();
		// Parse the list of airports from the server
		airplanes.parseAirplanesFromServer();
	}
	
	public static void main(String[] args) {		
		
        // Create the airports container	
		AirportContainer airports = new AirportContainer();
		
		// Parse the list of airports from the server
		airports.parseAirportsFromSever();
		
		// Print them out
		for (Airport airport : airports.getContainer()) {
			System.out.println(airport.toString());
		}
		
		// Create Departing flights
		System.out.println("Departing Flights:");
		DepartingFlightsContainer depFlights = new DepartingFlightsContainer();		
		depFlights.parseDepartingFlightsFromSever("BOS", "2017_05_14");
		for (Flight flight : depFlights.getContainer()){
			System.out.println(flight.toString());
		}
		
		// Create airplane container
		AirplaneContainer airplanes = new AirplaneContainer();		
		airplanes.parseAirplanesFromServer();
		System.out.println("Airplanes:");
		for (Airplane airplane : airplanes.getContainer()){
			System.out.println(airplane.toString());
		}

		
		// Create Arriving flights
		System.out.println("Arriving Flights:");
		ArrivingFlightsContainer arrFlights = new ArrivingFlightsContainer();		
		arrFlights.parseArrivingFlightsFromSever("BOS", "2017_05_10");
		for (Flight flight : arrFlights.getContainer()){
			System.out.println(flight.toString());
		}
	}	
}
