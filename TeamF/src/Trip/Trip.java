package Trip;

import java.util.ArrayList;

import airplane.Airplane;
import airplane.AirplaneContainer;
import flights.ArrivingFlightsContainer;
import flights.DepartingFlightsContainer;
import flights.Flight;

public class Trip {
	private AirplaneContainer airplanes;
	private DepartingFlightsContainer depFlights;
	private ArrivingFlightsContainer arrFlights;
	private Double minimumLayoverTime;
	private Double maximumLayoverTime;
	private Boolean firstClassSearch;
	
	public Trip() {
		airplanes = new AirplaneContainer();
		airplanes.parseAirplanesFromServer();
		depFlights = new DepartingFlightsContainer();
		arrFlights = new ArrivingFlightsContainer();
		setLayoverRange(0.5, 4.0);
	}
	
	public void setLayoverRange(Double minimum, Double maximum)
	{
		minimumLayoverTime = minimum;
		maximumLayoverTime = maximum;
	}
	
	private void setFirstClass(Boolean firstClass)
	{
		firstClassSearch = firstClass;
	}
	
	/*
	 * check if there are seats left on a given flight
	 * <p>
	 * Looks at total seats of a particular class on the given airplane the flight uses 
	 * 	and compares it to the total seats already booked to see if there are seats of the
	 * 	particular class remaining
	 * 
	 * @param Flight currentFlight -- The flight we are examining for available seats
	 * 
	 * @return Boolean -- true if there is an available seat on the current flight in the requested class
	 * 	-- false otherwise
	 */
	public Boolean seatsLeft(Flight currentFlight)
	{
		int seats, seatsBooked;
		String model;
		Airplane plane = new Airplane();
		model = currentFlight.get_flight_model();
		plane = airplanes.getAirplaneModelFromContainer(model);

		if (firstClassSearch)
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
	
	/*
	 * Looks at a given arrival time to see if there is a possibility of
	 * 	a valid stopover on the same day as was originally collected
	 * 
	 * @param String arrival_time
	 */
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
	
	/*
	 * Query the server for a list of flights and then return a list of 
	 * 	Flights with seats left
	 */
	public ArrayList<Flight> getDepartingFlights(String airport, String date) {
		ArrayList<Flight> results = new ArrayList<Flight>();
		ArrayList<Flight> temp = new ArrayList<Flight>();
		depFlights.parseDepartingFlightsFromSever(airport, date);
		temp.addAll(depFlights.getContainer());
		
		for (int i = 0; i < temp.size(); i++)
		{
			if (seatsLeft(temp.get(i)))
				results.add(temp.get(i));
		}
		
		return results;
	}
	
	/**
	 * Query the server for a list of flights and returns ArrayList<ArrayList<Flight>> of 
	 *   flights with up to specified number of stopovers matching inputs
	 *   The airplane container in controller should be instantiated first
	 *   The layover range should be instantiated first
	 *   
	 * @param String 
	 * @return ArrayList of arriving flights
	 */
	public ArrayList<ArrayList<Flight>> getFlightOptionsByDeparting(String departureAirport,
				String destinationAirport, String date, String numStops,
				Boolean firstClass)
	{
		setFirstClass(firstClass);
		
		ArrayList<ArrayList<Flight>> flightOptions;
		flightOptions = new ArrayList<ArrayList<Flight>>();
		ArrayList<Flight> depFlightsFirst, depFlightsSecond;
		depFlightsFirst = new ArrayList<Flight>();
		ArrayList<ArrayList<Flight>> twoLegs;
		int i, j; // count keeps track of the number of results, while i and j are for loops
		ArrayList<String> secondAirports,  dayTwoAirports, thirdAirports, dayTwoThirdAirports;
		secondAirports = new ArrayList<String>();
		dayTwoAirports = new ArrayList<String>();
		String tomorrow = nextDay(date);
		
		depFlightsFirst.addAll(getDepartingFlights(departureAirport, date));
		
		for (i = 0; i < depFlightsFirst.size(); i++) {
			if (depFlightsFirst.get(i).get_arr_code().equals(destinationAirport)) { // parse out the arrival airport
				flightOptions.add(new ArrayList<Flight>());
				flightOptions.get(flightOptions.size() - 1).add(depFlightsFirst.get(i));	// add the direct flights to the end of the list
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
		
		for (j = 0; j < secondAirports.size(); j++) {
			depFlightsSecond.addAll(getDepartingFlights(secondAirports.get(j), date)); // query for all the second legs
		}
		for (j = 0; j < dayTwoAirports.size(); j++) {
			depFlightsSecond.addAll(getDepartingFlights(dayTwoAirports.get(j), tomorrow)); // query for all the second legs
		}
		
		thirdAirports = new ArrayList<String>();
		dayTwoThirdAirports = new ArrayList<String>();
		twoLegs = new ArrayList<ArrayList<Flight>>(); // for two leg flights with valid stopovers that do not arrive at the right destination
		
		for (i = 0; i < depFlightsFirst.size(); i++) // for first stopover
		{
			if (depFlightsFirst.get(i).get_arr_code().equals(destinationAirport))
				continue; // already in the options list
			
			for (j = 0; j < depFlightsSecond.size(); j++)
			{
				if (flightCanBeStopover(depFlightsFirst.get(i), depFlightsSecond.get(j)))
				{
					if (depFlightsSecond.get(j).get_arr_code().equals(departureAirport))
						continue; // dont want to go back to the first airport
					if (depFlightsSecond.get(j).get_arr_code().equals(destinationAirport))// parse out the arrival airport
					{
						flightOptions.add(new ArrayList<Flight>());
						flightOptions.get(flightOptions.size() - 1).add(depFlightsFirst.get(i));	// add the first     -- why does this give flights from the wrong airport...?
						flightOptions.get(flightOptions.size() - 1).add(depFlightsSecond.get(j));	// and second legs to a single entry
					}
					else if (Integer.valueOf(numStops).equals(2)) // set up for third flight
					{
						// create a new array space for this combo 
						twoLegs.add(new ArrayList<Flight>());
						twoLegs.get(twoLegs.size() - 1).add(depFlightsFirst.get(i));
						twoLegs.get(twoLegs.size() - 1).add(depFlightsSecond.get(j));
						
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
		int queryAgainDayOne = 0;
		int queryAgainDayTwo = 0;
		for (j = 0; j < thirdAirports.size(); j++) {
			depFlightsSecond.addAll(getDepartingFlights(thirdAirports.get(j), date)); // query for all the second legs
			queryAgainDayOne++;
		}
		for (j = 0; j < dayTwoThirdAirports.size(); j++) {
			depFlightsSecond.addAll(getDepartingFlights(dayTwoThirdAirports.get(j), tomorrow)); // query for all the second legs
			queryAgainDayTwo++;
		}
		
		for (i = 0; i < twoLegs.size(); i++) {
			if (twoLegs.get(i).get(1).get_arr_code().equals(destinationAirport)) // get(1) is the current location?
				continue; // already in the list
			
			for (j = 0; j < depFlightsSecond.size(); j++) {
				if (!depFlightsSecond.get(j).get_arr_code().equals(destinationAirport))
					continue; // doesn't end at the right airport -- move on
				if (flightCanBeStopover(twoLegs.get(i).get(1), depFlightsSecond.get(j))) { // third leg is valid and brings us to destination -- add to list
					flightOptions.add(new ArrayList<Flight>());
					flightOptions.get(flightOptions.size() - 1).addAll(twoLegs.get(i));
					flightOptions.get(flightOptions.size() - 1).add(depFlightsSecond.get(j));
				}
			}
		}
		return flightOptions;
	}
}
