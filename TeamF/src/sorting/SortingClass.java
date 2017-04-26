package sorting;

// Imports
import flights.Flight;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
/**
 * 
 * @author hmanso02
 *
 */
public class SortingClass {
	
	/**
	 *  Takes in a list of lists of coach flights and sorts them by price 
	 *   
	 * @param ArrayList of lists of flights 
	 * @return ArrayList of lists of flights
	 */
	public ArrayList<ArrayList<Flight>> sortedFlights_Coach_Price (ArrayList<ArrayList<Flight>> flights){
		Collections.sort(flights, new Comparator<ArrayList<Flight>>() {
			@Override
			public int compare(ArrayList<Flight> arg0, ArrayList<Flight> arg1) {
				// TODO Auto-generated method stub
				double sum0 = 0;
				double sum1 = 0;
				for (int i = 0; i < arg0.size(); i++) {
				    sum0 = arg0.get(i).get_coach_price() + sum0;
				}
				for (int i = 0; i < arg1.size(); i++) {
				    sum1 = arg1.get(i).get_coach_price() + sum1;
				}
		        if (sum0 > sum1) {
		             return 1;
		         }
		        else if (sum0 < sum1) {
		            return -1;
		        }
				return 0;
			}
		});
		return flights;
	}
	
	/**
	 *  Takes in a list of lists of first class flights and sorts them by price  
	 *   
	 * @param ArrayList of lists of flights 
	 * @return ArrayList of lists of flights
	 */
	public ArrayList<ArrayList<Flight>> sortedFlights_FirstClass_Price (ArrayList<ArrayList<Flight>> flights){
		Collections.sort(flights, new Comparator<ArrayList<Flight>>() {
			@Override
			public int compare(ArrayList<Flight> arg0, ArrayList<Flight> arg1) {
				// TODO Auto-generated method stub
				double sum0 = 0;
				double sum1 = 0;
				for (int i = 0; i < arg0.size(); i++) {
				    sum0 = arg0.get(i).get_first_price() + sum0;
				}
				for (int i = 0; i < arg1.size(); i++) {
				    sum1 = arg1.get(i).get_first_price() + sum1;
				}
		        if (sum0 > sum1) {
		             return 1;
		         }
		        else if (sum0 < sum1) {
		            return -1;
		        }
				return 0;
			}
		});
		return flights;
	}
	
	/**
	 *  Takes in a list of lists of  flights and sorts them by duration 
	 *   
	 * @param ArrayList of lists of flights 
	 * @return ArrayList of lists of flights
	 */	
	public ArrayList<ArrayList<Flight>> sortedFlight_FlightTime (ArrayList<ArrayList<Flight>> flights){
		Collections.sort(flights, new Comparator<ArrayList<Flight>>() {
			@Override
			public int compare(ArrayList<Flight> arg0, ArrayList<Flight> arg1) {
				// TODO Auto-generated method stub
				DateTime earliestDep1 = arg0.get(0).get_dep_time_local();
				DateTime earliestDep2 = arg1.get(0).get_dep_time_local();
				DateTime latestArr1 = arg0.get(0).get_arr_time_local();
				DateTime latestArr2 = arg1.get(0).get_arr_time_local();
				for (int i = 0; i < arg0.size(); i++) {
				    if(arg0.get(i).get_arr_time_local().compareTo(latestArr1) > 0){
				    	latestArr1 = arg0.get(i).get_arr_time_local();
				    }
				    if(arg0.get(i).get_dep_time_local().compareTo(earliestDep1) < 0){
				    	earliestDep1 = arg0.get(i).get_dep_time_local();
				    }
				}
				for (int i = 0; i < arg1.size(); i++) {
					if(arg1.get(i).get_arr_time_local().compareTo(latestArr2) > 0){
				    	latestArr2 = arg1.get(i).get_arr_time_local();
				    }
					if(arg1.get(i).get_dep_time_local().compareTo(earliestDep2) < 0){
						earliestDep2 = arg1.get(i).get_dep_time_local();
				    }
				}
		        if (Integer.parseInt(Seconds.secondsBetween(earliestDep1, latestArr1).toString()) > Integer.parseInt(Seconds.secondsBetween(earliestDep2, latestArr2).toString())) {
		             return 1;
		         }
		        else if (Integer.parseInt(Seconds.secondsBetween(earliestDep1, latestArr1).toString()) < Integer.parseInt(Seconds.secondsBetween(earliestDep2, latestArr2).toString())) {
		            return -1;
		        }
				return 0;
			}
		});
		return flights;
	}
	/**
	 *  Takes in a list of flights and a bool indicating whether it is first class. Returns a
	 *  double for the total price of the flights.   
	 *   
	 * @param ArrayList of lists of flights 
	 * @return ArrayList of lists of flights
	 */
	public static double get_total_price(ArrayList<Flight> flights, boolean firstClass){
		double total_coach_price = 0.0;
		double total_first_price = 0.0;
		double res = 0.0;
		for (int i = 0; i < flights.size(); i++){
			total_coach_price = total_coach_price + flights.get(i).get_coach_price();
			total_first_price = total_first_price + flights.get(i).get_first_price();
		}
		if(firstClass)
		{
			return res = total_first_price;
		}else
		{
			return res =  total_coach_price;
		}
		
	}
	/**
	 *  Takes in a list flights and return a string for the total travel time.  
	 *   
	 * @param ArrayList of lists of flights 
	 * @return ArrayList of lists of flights
	 */
	public static String get_total_travel_time (ArrayList<Flight> flights){
		DateTime earliestDep1 = flights.get(0).get_dep_time_local();
		DateTime latestArr1 = flights.get(0).get_arr_time_local();
		for (int i = 0; i < flights.size(); i++) {
		    if(flights.get(i).get_arr_time_local().compareTo(latestArr1) > 0){
		    	latestArr1 = flights.get(i).get_arr_time_local();
		    }
		    if(flights.get(i).get_dep_time_local().compareTo(earliestDep1) < 0){
		    	earliestDep1 = flights.get(i).get_dep_time_local();
		    }
		}
		Interval interval1 = new Interval(earliestDep1,latestArr1);
		Integer hours = (int) interval1.toDuration().getStandardMinutes() / 60;
		Integer mins = (int) interval1.toDuration().getStandardMinutes() % 60;
		return  Integer.toString(hours) + ":" +  Integer.toString(mins);
	}
	
}
