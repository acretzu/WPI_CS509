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
	
	/**
	 * check if there are seats left on a given flight
	 * 
	 * <p>Looks at total seats of a particular class on the given airplane the flight uses 
	 * 	and compares it to the total seats already booked to see if there are seats of the
	 * 	particular class remaining
	 * 
	 * @param currentFlight -- The flight we are examining for available seats
	 * 
	 * @return true if there is an available seat on the current flight in
	 * the requested class, else false
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
	
	/**
	 * checks to see if two flights lie within the layover time range
	 * 
	 * <p>looks at two flights that are landing and taking off from the same airport
	 * 	to see if the departing flight leaves within the acceptable layover times 
	 * 	based on the arriving flights landing time.
	 * 
	 *  @param arriving The flight incoming into current airport
	 *  
	 *  @param departing The flight leaving the current airport
	 *  
	 *  @return true if the departing flight takes off within the layover range
	 *   from the arriving flight's landing, else false
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
		
		if (!(departing.get_dep_code().equals(arriving.get_arr_code())))
			return false; // not at the right airport
		if (time_between >= minimumLayoverTime && time_between <= maximumLayoverTime) // want to change these to variables that can be set by the GUI
			return true; // valid stopover time
		return false;
	}
	
	/**
	 * Looks at a given arrival or departure time to see if there is a possibility of
	 * 	a valid stopover on the same day as was originally collected
	 * 
	 * @param time Time the flight lands if searchByDepartingDate or takes off if not
	 * 
	 * @param original_time Original date that we queried on
	 * 
	 * @param searchByDepartingDate True if we are searching by departing date, false if by arrival date
	 * 
	 * @return true if there is a possibility of a valid layover on the same day, else false
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
	
	/**
	 * checks to see if the day after the original departure needs to be queried for the next leg
	 * 	of the flight based on stopover times
	 * 
	 * @param arrivalTime Arrival time from a Flight
	 * 
	 * @param originalTime Departure date year_month_day
	 * 
	 * @return true if the stopover suggests we should query for the next day, else false
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
	
	/**
	 * checks to see if the day after the original departure needs to be queried for the next leg
	 * 	of the flight based on stopover times
	 * 
	 * @param arrivalTime Arrival time from a Flight
	 * 
	 * @param originalTime Departure date of trip year_month_day
	 * 
	 * @return true if the stopover suggests we should query for the next day, else false
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
	
	/**
	 * Returns the next day for an xml query in the case that the total trip lasts into the day after departure
	 * 
	 * @param date Date from a Flight
	 * 
	 * @return date Date of the next day in the correct format for XML query
	 */
	public String nextDay(String date)
	{
		String[] parsed = date.split("_");
		int day = Integer.valueOf(parsed[2]) + 1;
		parsed[2] = String.valueOf(day);
		
		return (parsed[0] + "_" + parsed[1] + "_" +parsed[2]);
	}
	
	/**
	 * Returns the previous day for an xml query in the case that the total trip lasts into the day after departure
	 * 
	 * @param date Date from a Flight
	 * 
	 * @return date Date of the previous day in the correct format for XML query
	 */
	public String previousDay(String date)
	{
		String[] parsed = date.split("_");
		int day = Integer.valueOf(parsed[2]) - 1;
		parsed[2] = String.valueOf(day);
		
		return (parsed[0] + "_" + parsed[1] + "_" +parsed[2]);
	}
	
	/**
	 * Returns the integer value of the departure day of the current flight
	 * 
	 * @param current Flight being looked at
	 * 
	 * @return integer value of the day in the departure date
	 * 
	 */
	private int getDepDayInt(Flight current)
	{
		int day;
		String[] parseDate = current.get_dep_day_only().split(" ");
		day = Integer.valueOf(parseDate[2]);		
		return day;
	}
	
	/**
	 * Returns the integer value of the departure hour of the current flight
	 * 
	 * @param current Flight being looked at
	 * 
	 * @return integer value of the hour in the departure date
	 * 
	 */
	private int getDepHourInt(Flight current)
	{
		int hour;
		String[] parseTime = current.get_dep_time_only().split("[ :]");
		hour = Integer.valueOf(parseTime[1]);
		return hour;
	}
	
	/**
	 * Look at a list of flights with GMT times and return a list of flights that depart on the target date in local time
	 * 
	 * @param options List of flights from the departure airport
	 * 
	 * @param date Departure date
	 * 
	 * @return list of flights that depart from the airport on the local date
	 */
	private ArrayList<Flight> getFlightsFromLocalDayDeparting(ArrayList<Flight> options, String date)
	{
		ArrayList<Flight> rightDayList;
		rightDayList = new ArrayList<Flight>();
		String[] parsed = date.split("_");
		int hourOffset = (options.get(0).getOffSetTime(options.get(0).get_dep_code()) + 1),
				day = Integer.valueOf(parsed[2]), GMTDay, GMTHour;
		
		for (int i = 0; i < options.size(); i++) // add to list if departs during the local day
		{
			GMTDay = getDepDayInt(options.get(i));
			GMTHour = getDepHourInt(options.get(i));

			if (GMTDay == day) 
			{
				if (GMTHour <= (24 - hourOffset))
					rightDayList.add(options.get(i));
			}
			else if (GMTDay == (day - 1))
			{
				if (GMTHour >= (24 - hourOffset))
					rightDayList.add(options.get(i));
			}
		}
		
		return rightDayList;
	}
	
	/**
	 * Returns the integer value of the arrival day of the current flight
	 * 
	 * @param current Flight being looked at
	 * 
	 * @return integer value of the day in the arrival date
	 * 
	 */
	private int getArrDayInt(Flight current)
	{
		int day;
		String[] parseDate = current.get_arr_day_only().split(" ");
		day = Integer.valueOf(parseDate[2]);		
		return day;
	}
	
	/**
	 * Returns the integer value of the arrival hour of the current flight
	 * 
	 * @param current Flight being looked at
	 * 
	 * @return integer value of the hour in the arrival date
	 * 
	 */
	private int getArrHourInt(Flight current)
	{
		int hour;
		String[] parseTime = current.get_arr_time_only().split("[ :]");
		hour = Integer.valueOf(parseTime[1]);
		return hour;
	}
	
	/**
	 * Look at a list of flights with GMT times and return a list of flights that arrive on the target date in local time
	 * 
	 * @param options List of flights to the arrival airport
	 * 
	 * @param date Arrival date
	 * 
	 * @return list of flights that arrive at the airport on the local date
	 */
	private ArrayList<Flight> getFlightsFromLocalDayArriving(ArrayList<Flight> options, String date)
	{
		ArrayList<Flight> rightDayList;
		rightDayList = new ArrayList<Flight>();
		String[] parsed = date.split("_");
		int hourOffset = (options.get(0).getOffSetTime(options.get(0).get_arr_code()) + 1),
				day = Integer.valueOf(parsed[2]), GMTDay, GMTHour;
		
		for (int i = 0; i < options.size(); i++) // add to list if departs during the local day
		{
			GMTDay = getArrDayInt(options.get(i));
			GMTHour = getArrHourInt(options.get(i));

			if (GMTDay == day) 
			{
				if (GMTHour <= (24 - hourOffset))
					rightDayList.add(options.get(i));
			}
			else if (GMTDay == (day - 1))
			{
				if (GMTHour >= (24 - hourOffset))
					rightDayList.add(options.get(i));
			}
		}
		
		return rightDayList;
	}
	
	/**
	 * Query the server for a list of flights leaving on a given day
	 *  and then return a list of Flights with seats left
	 *  
	 *  @param airport The three character airport string
	 *  @param date GMT date of the departure
	 *  @return list of flights leaving the departure airport on the given date
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
	 * Query the server for a list of flights arriving on a given day
	 *  and then return a list of flights with seats left
	 *  
	 *  @param airport The three character airport string
	 *  @param date GMT date of the arrival
	 *  @return list of flights landing at the arrival airport on the given date
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
	 *  <p> Performs a breadth first search for flights that fulfill the input parameters
	 *   specifically while leaving the departure airport by the given date and
	 *   while trying to limit the number of unnecessary queries on the server
	 *   
	 * @param departureAirport Code of the airport the trip is leaving from
	 * 
	 * @param destinationAirport code of the airport the trip is arriving at
	 * 
	 * @param date Date that the trip will leave in the form year_month_day
	 * 
	 * @param numStops Number from 0 to 2 that gives the maximum number of stopovers allowed
	 * 
	 * @param firstClass True if we are looking for first class flights, false if coach
	 * 
	 * @return List of flights that fulfill the request
	 */
	public ArrayList<ArrayList<Flight>> getFlightOptionsByDeparting(String departureAirport,
				String destinationAirport, String date, String numStops,
				Boolean firstClass)
	{
		setFirstClass(firstClass);
		
		ArrayList<ArrayList<Flight>> flightOptions, optionsToBuildFrom,  // return data and a placeholder list to keep track of options that are not complete yet
			optionsToBuildFromNew;
		ArrayList<String> queryAirports, queryAirportsNextDay, alreadyQueried, alreadyQueriedNextDay, queryAirportsYesterday, alreadyQueriedYesterday;
		ArrayList<Flight> currentOptions;
		String tomorrow = nextDay(date), yesterday = previousDay(date);
		int i, j, k;
		
		flightOptions = new ArrayList<ArrayList<Flight>>();
		optionsToBuildFrom = new ArrayList<ArrayList<Flight>> ();
		optionsToBuildFromNew = new ArrayList<ArrayList<Flight>> ();
		queryAirports = new ArrayList<String>();
		queryAirportsNextDay = new ArrayList<String>();
		alreadyQueried = new ArrayList<String>();
		alreadyQueriedNextDay = new ArrayList<String>();
		queryAirportsYesterday = new ArrayList<String>(); // account for GMT to local transition
		alreadyQueriedYesterday = new ArrayList<String>();
		currentOptions = new ArrayList<Flight>();
		
		queryAirports.add(departureAirport);
		alreadyQueriedNextDay.add(departureAirport); // dont need to query for a flight that leaves at the departure airport the day after the target
		
		for (i = 0; i <= Integer.valueOf(numStops); i++) // breadth first loop -- num stops should never be greater than 2
		{
			for (j = 0; j < queryAirports.size(); j++) // get a list of flights on the target date from current list of airports that need querying
			{
				currentOptions.addAll(getDepartingFlights(queryAirports.get(j), date));
				alreadyQueried.add(queryAirports.get(j));
			}
			for (j = 0; j < queryAirportsNextDay.size(); j++) // get a list of flights on that depart the day after target date from the required list of airports
			{
				currentOptions.addAll(getDepartingFlights(queryAirportsNextDay.get(j), tomorrow));
				alreadyQueriedNextDay.add(queryAirportsNextDay.get(j));
			}
			for (j = 0; j < queryAirportsYesterday.size(); j++)
			{
				currentOptions.addAll(getDepartingFlights(queryAirportsYesterday.get(j), yesterday));
				alreadyQueriedYesterday.add(queryAirportsYesterday.get(j));
			}
			
			queryAirports.clear(); // clear to prep for next query
			queryAirportsNextDay.clear();
			queryAirportsYesterday.clear();
			
			if (i == 0)
				currentOptions = getFlightsFromLocalDayDeparting(currentOptions, date);
			
			// go through current list
			for (j = 0; j < currentOptions.size(); j++)
			{
				if (i == 0) // first stop special case
				{
					if (currentOptions.get(j).get_arr_code().equals(destinationAirport))
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
					if (!flightCanBeStopover(optionsToBuildFrom.get(k).get(optionsToBuildFrom.get(k).size() - 1), currentOptions.get(j)))
						continue;
					
					if (currentOptions.get(j).get_arr_code().equals(destinationAirport)) // valid result
					{
						flightOptions.add(new ArrayList<Flight>());
						flightOptions.get(flightOptions.size() - 1).addAll(optionsToBuildFrom.get(k));
						flightOptions.get(flightOptions.size() - 1).add(currentOptions.get(j));
						continue;
					}
					optionsToBuildFromNew.add(new ArrayList<Flight>());
					optionsToBuildFromNew.get(optionsToBuildFromNew.size() - 1).addAll(optionsToBuildFrom.get(k));
					optionsToBuildFromNew.get(optionsToBuildFromNew.size() - 1).add(currentOptions.get(j));
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
				if (doSameDay(optionsToBuildFrom.get(j).get(optionsToBuildFrom.get(j).size() - 1).get_arr_time(), date, true) &&
						!alreadyQueried.contains(optionsToBuildFrom.get(j).get(optionsToBuildFrom.get(j).size() - 1).get_arr_code()) &&
						!queryAirports.contains(optionsToBuildFrom.get(j).get(optionsToBuildFrom.get(j).size() - 1).get_arr_code()))
				{
					queryAirports.add(optionsToBuildFrom.get(j).get(optionsToBuildFrom.get(j).size() - 1).get_arr_code());  // add airport to list that will need to be queried on next run
				}
				if (goToNextDay(optionsToBuildFrom.get(j).get(optionsToBuildFrom.get(j).size() - 1).get_arr_time(), date) &&
						!alreadyQueriedNextDay.contains(optionsToBuildFrom.get(j).get(optionsToBuildFrom.get(j).size() - 1).get_arr_code()) &&
						!queryAirportsNextDay.contains(optionsToBuildFrom.get(j).get(optionsToBuildFrom.get(j).size() - 1).get_arr_code()))
				{
					queryAirportsNextDay.add(optionsToBuildFrom.get(j).get(optionsToBuildFrom.get(j).size() - 1).get_arr_code());  // add airport to list that will need to be queried on next run
				}
				//experimental
				if (doSameDay(optionsToBuildFrom.get(j).get(optionsToBuildFrom.get(j).size() - 1).get_arr_time(), yesterday, true) && // adjust for GMT offset
						!alreadyQueriedYesterday.contains(optionsToBuildFrom.get(j).get(optionsToBuildFrom.get(j).size() -1).get_arr_code()) &&
						!queryAirportsYesterday.contains(optionsToBuildFrom.get(j).get(optionsToBuildFrom.get(j).size() - 1).get_arr_code()))
				{
					queryAirportsYesterday.add(optionsToBuildFrom.get(j).get(optionsToBuildFrom.get(j).size() - 1).get_arr_code());
				}
			}
			
			if (i == 0)
				currentOptions.clear(); // wont reuse any of these as they already start at the departure
		}
		return flightOptions;
	}
	
	/**
	 * Query the server for an appropriate list of flights 
	 *   
	 *   <p>Performs a breadth first search for flights that fulfill the input parameters
	 *   specifically while trying to get to the destination by the given date and
	 *   while trying to limit the number of unnecessary queries on the server
	 *   
	 * @param departureAirport Code of the airport the trip is leaving from
	 * 
	 * @param destinationAirport Code of the airport the trip is arriving at
	 * 
	 * @param date Date that the trip will leave in the form year_month_day
	 * 
	 * @param numStops Number from 0 to 2 that gives the maximum number of stopovers allowed
	 * 
	 * @param firstClass True if we are looking for first class flights, false if coach
	 * 
	 * @return List of flights that fulfill the request
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
		//alreadyQueriedPreviousDay.add(destinationAirport); // dont need to query for a flight that arrives at the destination airport the day before the target
		queryAirportsPreviousDay.add(destinationAirport);
		
		
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
			
			if (i == 0)
				currentOptions = getFlightsFromLocalDayArriving(currentOptions, date); // take the two GMT days queried and translate to one local day
			
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
		return flightOptions;
	}
}
