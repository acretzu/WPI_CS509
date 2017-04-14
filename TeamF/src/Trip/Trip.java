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
	 * Looks at a given arrival or departure time to see if there is a possibility of
	 * 	a valid stopover on the same day as was originally collected
	 * 
	 * @param String time -- time the flight lands if searchByDepartingDate or takes off if not
	 * 
	 * @param String original_time -- original date that we queried on
	 * 
	 * @param Boolean searchByDepartingDate -- true if we are searching by departing date, false if by arrival date
	 * 
	 * @return true if there is a possibility of a valid layover on the same day, false if not
	 */
	public Boolean doSameDay(String time, String original_time, Boolean searchByDepartingDate)
	{
		String[] arr_time = time.split("[ :]"), org_time = original_time.split("_");
		double hour = Double.valueOf(arr_time[3]), minute = Double.valueOf(arr_time[4]),
				day = Double.valueOf(arr_time[2]), org_day = Double.valueOf(org_time[2]);
		hour = hour + (minute/60.0);
		
		if (searchByDepartingDate)
		{
			if (day > org_day)
				return false; // lands the next day or takes of the day before (arrival)
		
			if ((hour + minimumLayoverTime) > 24)
				return false; // lands after 11:30 PM -- not enough time for a layover before next day
		}
		else // search backwards by arriving date flow
		{
			if (day < org_day)
				return false; // takes off the day before
			
			if ((hour - minimumLayoverTime) < 0)
				return false;
		}
		return true;
	}
	
	/*
	 * checks to see if the day after the original departure needs to be queried for the next leg
	 * 	of the flight based on stopover times
	 * 
	 * @param String arrivalTime -- arrival time from a Flight
	 * 
	 * @param String originalTime -- departure date year_month_day
	 * 
	 * @return Boolean true if the stopover suggests we should query for the next day, false if not
	 */
	public Boolean goToNextDay(String arrivalTime, String originalTime) // should we query for the next day as well
	{
		String[] arr_time = arrivalTime.split("[ :]"), org_time = originalTime.split("_");
		double hour = Double.valueOf(arr_time[3]), minute = Double.valueOf(arr_time[4]),
				day = Double.valueOf(arr_time[2]), org_day = Double.valueOf(org_time[2]);
		hour = hour + (minute/60.0);
		if (day > org_day)
			return true; // first plane leaves the day before this plane lands
		if (hour + maximumLayoverTime >= 24)
			return true; // stopover can carry into the next day
		return false;
	}
	
	/*
	 * checks to see if the day after the original departure needs to be queried for the next leg
	 * 	of the flight based on stopover times
	 * 
	 * @param String arrivalTime -- arrival time from a Flight
	 * 
	 * @param String originalTime -- departure date year_month_day
	 * 
	 * @return Boolean true if the stopover suggests we should query for the next day, false if not
	 */
	public Boolean goToPreviousDay(String departureTime, String originalTime) // should we query for the next day as well
	{
		String[] arr_time = departureTime.split("[ :]"), org_time = originalTime.split("_");
		double hour = Double.valueOf(arr_time[3]), minute = Double.valueOf(arr_time[4]),
				day = Double.valueOf(arr_time[2]), org_day = Double.valueOf(org_time[2]);
		hour = hour + (minute/60.0);
		if (day < org_day)
			return true; // last plane leaves the day after the current flight takes off
		if (hour - maximumLayoverTime <= 0)
			return true; // stopover can carry into the next day
		return false;
	}
	
	/*
	 * Returns the next day for an xml query in the case that the total trip lasts into the day after departure
	 * 
	 * @param String date from a Flight
	 * 
	 * @return String date of the next day in the correct format for XML query
	 */
	public String nextDay(String date)
	{
		String[] parsed = date.split("_");
		int day = Integer.valueOf(parsed[2]) + 1;
		parsed[2] = String.valueOf(day);
		
		return (parsed[0] + "_" + parsed[1] + "_" +parsed[2]);
	}
	
	/*
	 * Returns the previous day for an xml query in the case that the total trip lasts into the day after departure
	 * 
	 * @param String date from a Flight
	 * 
	 * @return String date of the previous day in the correct format for XML query
	 */
	public String previousDay(String date)
	{
		String[] parsed = date.split("_");
		int day = Integer.valueOf(parsed[2]) - 1;
		parsed[2] = String.valueOf(day);
		
		return (parsed[0] + "_" + parsed[1] + "_" +parsed[2]);
	}
	
	/*
	 * Query the server for a list of flights leaving on a given day
	 *  and then return a list of Flights with seats left
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
	
	/*
	 * Query the server for a list of flights arriving on a given day
	 *  and then return a list of flights with seats left
	 */
	public ArrayList<Flight> getArrivingFlights(String airport, String date) {
		ArrayList<Flight> results = new ArrayList<Flight>();
		ArrayList<Flight> temp = new ArrayList<Flight>();
		arrFlights.parseArrivingFlightsFromSever(airport, date);
		temp.addAll(arrFlights.getContainer());
		
		for (int i = 0; i < temp.size(); i++)
		{
			if (seatsLeft(temp.get(i)))
				results.add(temp.get(i));
		}
		
		return results;
	}
	
	/**
	 * Query the server for an appropriate list of flights 
	 *   
	 *   Performs a breadth first search for flights that fulfill the input parameters
	 *   	 specifically while leaving the departure airport by the given date and
	 *   	while trying to limit the number of unnecessary queries on the server
	 *   
	 * @param String departureAirport -- code of the airport the trip is leaving from
	 * 
	 * @param String destinationAirport -- code of the airport the trip is arriving at
	 * 
	 * @param String date -- that the trip will leave in the form year_month_day
	 * 
	 * @param String numStops -- number from 0 to 2 that gives the maximum number of stopovers allowed
	 * 
	 * @param Boolean firstClass -- true if we are looking for first class flights, false if coach
	 * 
	 * @return ArrayList<ArrayList> of flights that fulfill the request
	 */
	public ArrayList<ArrayList<Flight>> getFlightOptionsByDeparting(String departureAirport,
				String destinationAirport, String date, String numStops,
				Boolean firstClass)
	{
		setFirstClass(firstClass);
		
		ArrayList<ArrayList<Flight>> flightOptions;  // return data
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
				if (doSameDay(depFlightsFirst.get(i).get_arr_time(), date, true) &&
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
						
						if (doSameDay(depFlightsSecond.get(j).get_arr_time(), date, true) &&
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

		for (j = 0; j < thirdAirports.size(); j++) {
			depFlightsSecond.addAll(getDepartingFlights(thirdAirports.get(j), date)); // query for all the second legs
		}
		for (j = 0; j < dayTwoThirdAirports.size(); j++) {
			depFlightsSecond.addAll(getDepartingFlights(dayTwoThirdAirports.get(j), tomorrow)); // query for all the second legs
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
	
	/**
	 * Query the server for an appropriate list of flights 
	 *   
	 *   Performs a breadth first search for flights that fulfill the input parameters
	 *   	specifically while trying to get to the destination by the given date and
	 *   	while trying to limit the number of unnecessary queries on the server
	 *   
	 * @param String departureAirport -- code of the airport the trip is leaving from
	 * 
	 * @param String destinationAirport -- code of the airport the trip is arriving at
	 * 
	 * @param String date -- that the trip will leave in the form year_month_day
	 * 
	 * @param String numStops -- number from 0 to 2 that gives the maximum number of stopovers allowed
	 * 
	 * @param Boolean firstClass -- true if we are looking for first class flights, false if coach
	 * 
	 * @return ArrayList<ArrayList> of flights that fulfill the request
	 */
	public ArrayList<ArrayList<Flight>> getFlightOptionsByArrival(String departureAirport,
				String destinationAirport, String date, String numStops,
				Boolean firstClass)
	{
		setFirstClass(firstClass);
		
		ArrayList<ArrayList<Flight>> flightOptions, optionsToBuildFrom,  // return data and a placeholder list to keep track of options that are not complete yet
			optionsToBuildFromNew;
		ArrayList<String> queryAirports, queryAirportsPreviousDay, alreadyQueried, alreadyQueriedPreviousDay;
		ArrayList<Flight> currentOptions;
		String yesterday = previousDay(date);
		int i, j, k;
		
		flightOptions = new ArrayList<ArrayList<Flight>>();
		optionsToBuildFrom = new ArrayList<ArrayList<Flight>> ();
		optionsToBuildFromNew = new ArrayList<ArrayList<Flight>> ();
		queryAirports = new ArrayList<String>();
		queryAirportsPreviousDay = new ArrayList<String>();
		alreadyQueried = new ArrayList<String>();
		alreadyQueriedPreviousDay = new ArrayList<String>();
		currentOptions = new ArrayList<Flight>();
		
		queryAirports.add(destinationAirport);
		alreadyQueriedPreviousDay.add(destinationAirport); // dont need to query for a flight that arrives at the destination airport the day before the target
		
		for (i = 0; i <= Integer.valueOf(numStops); i++) // breadth first loop -- num stops should never be greater than 2
		{
			for (j = 0; j < queryAirports.size(); j++) // get a list of flights on the target date from current list of airports that need querying
			{
				currentOptions.addAll(getArrivingFlights(queryAirports.get(j), date));
				alreadyQueried.add(queryAirports.get(j));
			}
			for (j = 0; j < queryAirportsPreviousDay.size(); j++) // get a list of flights on that arrive the day before target date from the required list of airports
			{
				currentOptions.addAll(getArrivingFlights(queryAirportsPreviousDay.get(j), yesterday));
				alreadyQueriedPreviousDay.add(queryAirportsPreviousDay.get(j));
			}
			
			queryAirports.clear(); // clear to prep for next query
			queryAirportsPreviousDay.clear();
			
			// go through current list
			for (j = 0; j < currentOptions.size(); j++)
			{
				if (i == 0) // first stop special case
				{
					if (currentOptions.get(j).get_dep_code().equals(departureAirport))
					{
						flightOptions.add(new ArrayList<Flight>());
						flightOptions.get(flightOptions.size() - 1).add(currentOptions.get(j)); // this is a valid result
					}
					else
					{
						optionsToBuildFromNew.add(new ArrayList<Flight>());
						optionsToBuildFromNew.get(optionsToBuildFromNew.size() - 1).add(currentOptions.get(j));
					}
					continue;
				}
				for (k = 0; k < optionsToBuildFrom.size(); k++) // next stop
				{
					if (!flightCanBeStopover(currentOptions.get(j), optionsToBuildFrom.get(k).get(0)))//optionsToBuildFrom.get(k).size() - 1)))
						continue;
					
					if (currentOptions.get(j).get_dep_code().equals(departureAirport)) // valid result
					{
						flightOptions.add(new ArrayList<Flight>());
						flightOptions.get(flightOptions.size() - 1).add(currentOptions.get(j));
						flightOptions.get(flightOptions.size() - 1).addAll(optionsToBuildFrom.get(k));
						continue;
					}
					optionsToBuildFromNew.add(new ArrayList<Flight>());
					optionsToBuildFromNew.get(optionsToBuildFromNew.size() - 1).add(currentOptions.get(j));
					optionsToBuildFromNew.get(optionsToBuildFromNew.size() - 1).addAll(optionsToBuildFrom.get(k));
				}
			}
			
			if (Integer.valueOf(numStops) == i)
				return flightOptions;
			
			// else prep for next query
			optionsToBuildFrom.clear();
			optionsToBuildFrom.addAll(optionsToBuildFromNew);
			optionsToBuildFromNew.clear();
			
			// build the list of airports to query from
			for (j = 0; j < optionsToBuildFrom.size(); j++)
			{
				if (doSameDay(optionsToBuildFrom.get(j).get(0).get_dep_time(), date, false) &&
						!alreadyQueried.contains(optionsToBuildFrom.get(j).get(0).get_dep_code()) &&
						!queryAirports.contains(optionsToBuildFrom.get(j).get(0).get_dep_code()))
				{
					queryAirports.add(optionsToBuildFrom.get(j).get(0).get_dep_code());  // add airport to list that will need to be queried on next run
				}
				if (goToPreviousDay(optionsToBuildFrom.get(j).get(0).get_dep_time(), date) &&
						!alreadyQueriedPreviousDay.contains(optionsToBuildFrom.get(j).get(0).get_dep_code()) &&
						!queryAirportsPreviousDay.contains(optionsToBuildFrom.get(j).get(0).get_dep_code()))
				{
					queryAirportsPreviousDay.add(optionsToBuildFrom.get(j).get(0).get_dep_code());  // add airport to list that will need to be queried on next run
				}
			}
			
			if (i == 0)
				currentOptions.clear(); // wont reuse any of these as they already end at the destination
		}
		/*ArrayList<Flight> arrFlightsFirst, arrFlightsSecond;
		arrFlightsFirst = new ArrayList<Flight>();
		ArrayList<ArrayList<Flight>> twoLegs;
		int i, j; // count keeps track of the number of results, while i and j are for loops
		ArrayList<String> secondAirports,  previousDayAirports, thirdAirports, previousDayThirdAirports;
		secondAirports = new ArrayList<String>();
		previousDayAirports = new ArrayList<String>();
		String tomorrow = nextDay(date);
		
		arrFlightsFirst.addAll(getArrivingFlights(destinationAirport, date));
		
		for (i = 0; i < arrFlightsFirst.size(); i++) {
			if (arrFlightsFirst.get(i).get_dep_code().equals(departureAirport)) {
				flightOptions.add(new ArrayList<Flight>());
				flightOptions.get(flightOptions.size() - 1).add(arrFlightsFirst.get(i));	// add the direct flights to the end of the list
			}
			else if (Integer.valueOf(numStops) > 0)
			{
				if (doSameDay(arrFlightsFirst.get(i).get_dep_time(), date, false) &&
						!(secondAirports.contains(arrFlightsFirst.get(i).get_dep_code())))
					secondAirports.add(arrFlightsFirst.get(i).get_arr_code());
				if (goToPreviousDay(arrFlightsFirst.get(i).get_arr_time(), date) &&
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
						
						if (doSameDay(depFlightsSecond.get(j).get_arr_time(), date, false) &&
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
		}*/
		return flightOptions;
	}
}
