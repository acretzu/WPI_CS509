package controller;

import airport.Airport;
import airport.AirportContainer;

public class Controller {
	
	public static void main(String[] args) {		
		
        // Create the airports container	
		AirportContainer airports = new AirportContainer();
		
		// Parse the list of airports from the server
		airports.parseAirportsFromSever();
		
		// Print them out
		for (Airport airport : airports.getAirportList()) {
			System.out.println(airport.toString());
		}
	}
	
}
