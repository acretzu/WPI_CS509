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
	private Double minimumLayoverTime;
	private Double maximumLayoverTime;
	
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
	
	public void setLayoverRange(Double minimum, Double maximum)
	{
		minimumLayoverTime = minimum;
		maximumLayoverTime = maximum;
	}
	
	/*
	 * check if there are seats left on a given flight
	 * 
	 * 
	 * @param Flight currentFlight -- The flight we are examining for available seats
	 * 
	 * @param Boolean firstClass -- true if the class of seat we are looking for is first class
	 * 	-- false if it is Coach
	 * 
	 * @return Boolean -- true if there is an available seat on the current flight in the requested class
	 * 	-- false otherwise
	 */
	public Boolean seatsLeft(Flight currentFlight, Boolean firstClass)
	{
		int seats, seatsBooked;
		String model;
		Airplane plane = new Airplane();
		model = currentFlight.get_flight_model();
		plane = airplanes.getAirplaneModelFromContainer(model);

		if (firstClass)
		{
			seatsBooked = currentFlight.get_first_class();
			seats = plane.firstClass();
		}
		else
		{
			seatsBooked = currentFlight.get_coach();
			seats = plane.coachClass();
		}
		
		if ((seats - seatsBooked) > 0)
			return true;
		else
			return false;
	}
	
	/*
	 * checks to see if two flights lie within the layover time range
	 * <p>
	 * 	looks at two flights that are landing and taking off from the same airport
	 * 	to see if the departing flight leaves within the acceptable layover times 
	 * 	based on the arriving flights landing time.
	 * 
	 *  @param Flight arriving -- the flight incoming into current airport
	 *  
	 *  @param Flight departing -- the flight leaving the current airport
	 *  
	 *  @return -- Boolean -- true if the departing flight takes off within 
	 *  	the layover range from the arriving flight's landing. False if not
	 */
	public Boolean flightCanBeStopover(Flight arriving, Flight departing)
	{
		String[] arr_time = arriving.get_arr_time().split("[ :]"),
				dep_time = departing.get_dep_time().split("[ :]");
		double arr_time_num, dep_time_num, time_between = 0; // resolve to integers for easy comparison
		arr_time_num = Double.valueOf(arr_time[3]) + Double.valueOf(arr_time[4])/60;
		dep_time_num = Double.valueOf(dep_time[3]) + Double.valueOf(dep_time[4])/60;
		if (Integer.valueOf(dep_time[2]) > Integer.valueOf(arr_time[2]))
			dep_time_num += 24;
		time_between = dep_time_num - arr_time_num;
		/*
		 * year, month, day, hour, minute, time type
		 */
		
		if (!(departing.get_dep_code().equals(arriving.get_arr_code())))
			return false; // not at the right airport
		if (time_between >= minimumLayoverTime && time_between <= maximumLayoverTime) // want to change these to variables that can be set by the GUI
			return true; // valid stopover time
		return false;
	}
	
	public Boolean doSameDay(String arrival_time, String original_time)
	{
		String[] arr_time = arrival_time.split("[ :]"), org_time = original_time.split("_");
		int hour = Integer.valueOf(arr_time[3]), minute = Integer.valueOf(arr_time[4]),
				day = Integer.valueOf(arr_time[2]), org_day = Integer.valueOf(org_time[2]);
		if (day > org_day)
			return false; // lands the next day
		if (hour == 23 && minute > 30)
			return false; // lands after 11:30 PM -- not enough time for a layover before next day
		return true;
	}
	
	public Boolean goToNextDay(String arrival_time, String original_time) // should we query for the next day as well
	{
		String[] arr_time = arrival_time.split("[ :]"), org_time = original_time.split("_");
		int hour = Integer.valueOf(arr_time[3]), day = Integer.valueOf(arr_time[2]), 
				org_day = Integer.valueOf(org_time[2]);
		if (day > org_day)
			return true; // first plane leaves the day before this plane lands
		if (hour >= 20)
			return true; // stopover can carry into the next day
		return false;
	}
	
	public String nextDay(String date)
	{
		String[] parsed = date.split("_");
		int day = Integer.valueOf(parsed[2]) + 1;
		parsed[2] = String.valueOf(day);
		
		return (parsed[0] + "_" + parsed[1] + "_" +parsed[2]);
	}
	
	/**
	 * Query the server for a list of flights and returns ArrayList<ArrayList<Flight>> of 
	 *   flights with up to specified number of stopovers matching inputs
	 *   The airplane container in controller should be instantiated first
	 *   The layover range should be instantiated first
	 * @return ArrayList of arriving flights
	 */
	public ArrayList<ArrayList<Flight>> getFlightOptionsByDeparting(String departureAirport,
				String destinationAirport, String date, String numStops,
				Boolean firstClass)
	{
		ArrayList<ArrayList<Flight>> flightOptions;
		flightOptions = new ArrayList<ArrayList<Flight>>();
		ArrayList<Flight> depFlightsFirst, depFlightsSecond;
		depFlightsFirst = new ArrayList<Flight>();
		int count = 0, i, j;
		
		//info for second leg (and third)
		// splitting up into each day will save performance by limiting number of necessary queries to server
		ArrayList<String> secondAirports;
		ArrayList<String> dayTwoAirports;
		secondAirports = new ArrayList<String>();
		dayTwoAirports = new ArrayList<String>();
		
		depFlightsFirst.addAll(getDepartingFlights(departureAirport, date));
		
		for (i = 0; i < depFlightsFirst.size(); i++)
		{
			if (!seatsLeft(depFlightsFirst.get(i), firstClass))
				continue;
			if (depFlightsFirst.get(i).get_arr_code().equals(destinationAirport))// parse out the arrival airport
			{
				flightOptions.add(new ArrayList<Flight>());
				flightOptions.get(count).add(depFlightsFirst.get(i));	// add the direct flights
				count++;
			}
			else if (Integer.valueOf(numStops) > 0)
			{
				if (doSameDay(depFlightsFirst.get(i).get_arr_time(), date) &&
						!(secondAirports.contains(depFlightsFirst.get(i).get_arr_code())))
					secondAirports.add(depFlightsFirst.get(i).get_arr_code());
				if (goToNextDay(depFlightsFirst.get(i).get_arr_time(), date) &&
						!dayTwoAirports.contains(depFlightsFirst.get(i).get_arr_code()))
					dayTwoAirports.add(depFlightsFirst.get(i).get_arr_code());
			}
		}
		
		if (Integer.valueOf(numStops) == 0)
			return flightOptions;
		
		depFlightsSecond = new ArrayList<Flight>();
		String tomorrow = nextDay(date);
		
		for (j = 0; j < secondAirports.size(); j++)
		{
			depFlightsSecond.addAll(getDepartingFlights(secondAirports.get(j), date)); // query for all the second legs
		}
		for (j = 0; j < dayTwoAirports.size(); j++)
		{
			depFlightsSecond.addAll(getDepartingFlights(dayTwoAirports.get(j), tomorrow)); // query for all the second legs
		}
		
		// in case any other queries need to be made
		ArrayList<String> thirdAirports;
		ArrayList<String> dayTwoThirdAirports;
		thirdAirports = new ArrayList<String>();
		dayTwoThirdAirports = new ArrayList<String>();
		ArrayList<ArrayList<Flight>> twoLegs;
		twoLegs = new ArrayList<ArrayList<Flight>>(); // for two leg flights with valid stopovers that do not arrive at the right destination
		int index = 0;
		
		for (i = 0; i < depFlightsFirst.size(); i++) // for one stopover
		{
			// all first leg flights
			if (depFlightsFirst.get(i).get_arr_code().equals(destinationAirport))
				continue; // already in the options list
			
			for (j = 0; j < depFlightsSecond.size(); j++)
			{
				if (flightCanBeStopover(depFlightsFirst.get(i), depFlightsSecond.get(j)))
				{
					if (!seatsLeft(depFlightsSecond.get(i), firstClass))
						continue;
					if (depFlightsSecond.get(j).get_arr_code().equals(departureAirport))
						continue; // dont want to go back to the first airport
					if (depFlightsSecond.get(j).get_arr_code().equals(destinationAirport))// parse out the arrival airport
					{
						flightOptions.add(new ArrayList<Flight>());
						flightOptions.get(count).add(depFlightsFirst.get(i));	// add the first     -- why does this give flights from the wrong airport...?
						flightOptions.get(count).add(depFlightsSecond.get(j));	// and second legs to a single entry
						count++;
					}
					else if (Integer.valueOf(numStops).equals(2)) // set up for third flight
					{
						// create a new array space for this combo 
						twoLegs.add(new ArrayList<Flight>());
						twoLegs.get(index).add(depFlightsFirst.get(i));
						twoLegs.get(index).add(depFlightsSecond.get(j));
						index++;
						
						if (doSameDay(depFlightsSecond.get(j).get_arr_time(), date) &&
								!secondAirports.contains(depFlightsSecond.get(j).get_arr_code()) &&
								!thirdAirports.contains(depFlightsSecond.get(j).get_arr_code()))
							thirdAirports.add(depFlightsSecond.get(j).get_arr_code());
						if (goToNextDay(depFlightsSecond.get(j).get_arr_time(), date) &&
								!dayTwoAirports.contains(depFlightsSecond.get(j).get_arr_code()) && 
								!dayTwoThirdAirports.contains(depFlightsSecond.get(j).get_arr_code()))
							dayTwoThirdAirports.add(depFlightsSecond.get(j).get_arr_code());
						// check if more queries need to be done
					}
				}
			}
		}
		
		if (Integer.valueOf(numStops) == 1)
			return flightOptions;
		
		for (j = 0; j < thirdAirports.size(); j++)
		{
			depFlightsSecond.addAll(getDepartingFlights(thirdAirports.get(j), date)); // query for all the second legs
		}
		for (j = 0; j < dayTwoThirdAirports.size(); j++)
		{
			depFlightsSecond.addAll(getDepartingFlights(dayTwoThirdAirports.get(j), tomorrow)); // query for all the second legs
		}
		
		for (i = 0; i < twoLegs.size(); i++)
		{
			if (twoLegs.get(i).get(1).get_arr_code().equals(destinationAirport)) // get(1) is the current location?
				continue; // already in the list
			
			for (j = 0; j < depFlightsSecond.size(); j++)
			{
				if (!depFlightsSecond.get(j).get_arr_code().equals(destinationAirport))
					continue; // doesn't end at the right airport -- move on
				if (flightCanBeStopover(twoLegs.get(i).get(1), depFlightsSecond.get(j)))
				{
					// third leg is valid and brings us to destination -- add to list
					flightOptions.add(new ArrayList<Flight>());
					flightOptions.get(count).addAll(twoLegs.get(i));
					flightOptions.get(count).add(depFlightsSecond.get(j));
					count++;
				}
			}
		}
		
		// create a new list of flights with one stopover already contained here and loop on the size of that
		// will be very similar to the last loop
		
		return flightOptions;
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