package sorting;

// Imports
import flights.Flight;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**
 * 
 * @author hmanso02
 *
 */
public abstract class SortingClass {
	
	/**
	 *  Takes in a list of lists of coach flights and sorts them by price 
	 *   
	 * @param ArrayList of lists of flights 
	 * @return ArrayList of lists of flights
	 */
	protected ArrayList<ArrayList<Flight>> sortedFlights_Coach_Price (ArrayList<ArrayList<Flight>> flights){
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
	protected ArrayList<ArrayList<Flight>> sortedFlights_FirstClass_Price (ArrayList<ArrayList<Flight>> flights){
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
	protected ArrayList<ArrayList<Flight>> sortedFlight_FlightTime (ArrayList<ArrayList<Flight>> flights){
		Collections.sort(flights, new Comparator<ArrayList<Flight>>() {
			@Override
			public int compare(ArrayList<Flight> arg0, ArrayList<Flight> arg1) {
				// TODO Auto-generated method stub
				double sum0 = 0;
				double sum1 = 0;
				for (int i = 0; i < arg0.size(); i++) {
				    sum0 = arg0.get(i).get_flight_time() + sum0;
				}
				for (int i = 0; i < arg1.size(); i++) {
				    sum1 = arg1.get(i).get_flight_time() + sum1;
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
}
