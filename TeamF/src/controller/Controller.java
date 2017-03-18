package controller;

import airport.Airport;
import airport.AirportContainer;
import flights.DepartingFlightsContainer;
import flights.ArrivingFlightsContainer;
import flights.Flight;
import airplane.AirplaneContainer;
import airplane.Airplane;
import java.util.ArrayList;

public class Controller {
	
	//
	// Class variables
	//
	
	// Containers 
	private AirportContainer airports;
	private DepartingFlightsContainer depFlights;
	private AirplaneContainer airplanes;
	private ArrivingFlightsContainer arrFlights;
	
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
		DepartingFlightsContainer depFlights = new DepartingFlightsContainer();		
		depFlights.parseDepartingFlightsFromSever("BOS", "2017_05_10");
		for (Flight flight : depFlights.getContainer()){
			System.out.println(flight.toString());
		}
//		
//		// Create airplane container
//		AirplaneContainer airplanes = new AirplaneContainer();		
//		airplanes.parseAirplanesFromSever();
		
		// Create Arriving flights
		ArrivingFlightsContainer arrFlights = new ArrivingFlightsContainer();		
		arrFlights.parseArrivingFlightsFromSever("BOS", "2017_05_10");
		for (Flight flight : arrFlights.getContainer()){
			System.out.println(flight.toString());
		}
	}
	
}
