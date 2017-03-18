package controller;

import airport.Airport;
import airport.AirportContainer;
import flights.DepartingFlightsContainer;
import flights.ArrivingFlightsContainer;
import flights.Flight;
import airplane.AirplaneContainer;
import airplane.Airplane;

public class Controller {
	
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
