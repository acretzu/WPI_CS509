package flights;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;

public class DepartingFlightsContainer extends FlightsContainer {
	// Class variables
	private ArrayList<Flight> list;
	
	/*
	 * Constructor
	 */
	public DepartingFlightsContainer() {
		list = new ArrayList<Flight>();
	}
	
	
	/**
	 * Populates the list of valid airports from the WPI server.
	 */
	public void parseDepartingFlightsFromSever(String airport, String date) throws NullPointerException {
		list.removeAll(list);
		Document docFlights = buildDocument(doQuery("?team=" + teamName + "&action=list&list_type=departing&airport=" + airport +"&day=" + date));
		NodeList nodesFlights = docFlights.getElementsByTagName("Flight");
		for (int i = 0; i < nodesFlights.getLength(); i++) {
			Element elementFlight = (Element) nodesFlights.item(i);
			Flight flight = this.createFlight(elementFlight);
			list.add(flight);
		}
	
	}
	
	/**
	 * Return a list of airports
	 * 
	 * @return The list of airports
	 */
	public ArrayList<Flight> getContainer() {
		return list;
	}
}
