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
	/**
	* Class variables
	*/
	
	// Containers 
	private AirportContainer airports;
	private DepartingFlightsContainer depFlights;
	private AirplaneContainer airplanes;
	private ArrivingFlightsContainer arrFlights;
	
	// GUI Information
	Boolean round_trip;
	Integer stopovers;
	String departure_airport;
	String arrival_airport;
	String departure_year;
	String departure_month;
	String departure_day;	
	String arrival_year;
	String arrival_month;
	String arrival_day;
	
	public ArrayList<Airport> getAirports() {
		airports.parseAirportsFromSever();
		return airports.getContainer();
	}
	
	public ArrayList<Flight> getDepartingFlights(String airport, String date) {
		depFlights.parseDepartingFlightsFromSever(airport, date);
		return depFlights.getContainer();
	}
	
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
		
		// Create airplane container
		AirplaneContainer airplanes = new AirplaneContainer();		
		airplanes.parseAirplanesFromSever();
		
		// Create Arriving flights
		ArrivingFlightsContainer arrFlights = new ArrivingFlightsContainer();		
		arrFlights.parseArrivingFlightsFromSever("BOS", "2017_05_10");
	}
	
}
