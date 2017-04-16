package flights;

import org.w3c.dom.Element;
import xml.XmlParser;

/**
 * 
 * @author acretzu
 *
 */
public class FlightsContainer extends XmlParser {
	
	public Flight createFlight(Element elementFlight){
		 String flight_model;
		 String flight_time;
		 String flight_number;
		 String dep_code;
		 String dep_time;
		 String arr_code;
		 String arr_time;
		 int num_first_seats;
		 double price_first;
		 int num_coach_seats;
		 double price_coach;
		 
		/**
		 * Instantiate an empty Flight object
		 */
		Flight flight = new Flight();
		

		// The Flight element has attributes of "Airplane" , FlightTime and Number



		flight_model = elementFlight.getAttributeNode("Airplane").getValue();
		flight_number = elementFlight.getAttributeNode("Number").getValue();
		flight_time = elementFlight.getAttributeNode("FlightTime").getValue();
		
		// The Departure, Arrival, Seating are child elements 
		Element elementDeparture;
		Element elementCode;
		Element elementTime;
		elementDeparture  =(Element) elementFlight.getElementsByTagName("Departure").item(0);
		elementCode = (Element)elementDeparture.getElementsByTagName("Code").item(0);
		elementTime = (Element)elementDeparture.getElementsByTagName("Time").item(0);
		dep_code = getCharacterDataFromElement(elementCode);
		dep_time = getCharacterDataFromElement(elementTime);
		
		Element elementArrival;
		elementArrival = (Element)elementFlight.getElementsByTagName("Arrival").item(0);
		elementCode = (Element)elementArrival.getElementsByTagName("Code").item(0);
		elementTime = (Element)elementArrival.getElementsByTagName("Time").item(0);
		arr_code = getCharacterDataFromElement(elementCode);
		arr_time = getCharacterDataFromElement(elementTime);
		
		Element elementSeating;
		Element elementFirstClass;
		Element elementCoach;
		elementSeating = (Element)elementFlight.getElementsByTagName("Seating").item(0);
		elementFirstClass = (Element)elementSeating.getElementsByTagName("FirstClass").item(0);
		num_first_seats = Integer.parseInt(getCharacterDataFromElement(elementFirstClass));
		
		price_first = Double.parseDouble(elementFirstClass.getAttributeNode("Price").getValue().replace("$", "").replace(",", ""));
		
		elementCoach =  (Element)elementSeating.getElementsByTagName("Coach").item(0);
		num_coach_seats = Integer.parseInt(getCharacterDataFromElement(elementCoach));
		price_coach = Double.parseDouble(elementCoach.getAttributeNode("Price").getValue().replace("$", "").replace(",", ""));
		
		/**
		 * Update the Flight object with values from XML node
		 */
		flight.set_flight_model(flight_model);
		flight.set_flight_number(flight_number);
		flight.set_flight_time(flight_time);
		flight.set_arr_code(arr_code);
		flight.set_arr_time(arr_time);
		flight.set_dep_code(dep_code);
		flight.set_dep_time(dep_time);
		flight.set_first_class(num_first_seats);
		flight.set_first_price(price_first);
		flight.set_coach(num_coach_seats);
		flight.set_coach_price(price_coach);
		return flight;
	}
	public double get_total_price(Flight[] flights){
		double total_coach_price = 0.0;
		double total_first_price = 0.0;
		double res = 0.0;
		for (Flight flight : flights){
			total_coach_price = total_coach_price + flight.get_coach_price();
			total_first_price = total_first_price + flight.get_first_price();
		}
		return res = total_coach_price + total_first_price;
	}
	

	/**
	 *  This class is meant to carry any common functionality for departingFlightsContainer and arrivingFlightsContainer
	 */


}
