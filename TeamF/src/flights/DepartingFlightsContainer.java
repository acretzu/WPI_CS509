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
		
		System.out.println(doQuery("?team=" + teamName + "&action=list&list_type=departing&airport=" + airport +"&day=" + date));
		
		/*
		// Send transaction to server and build a document from the result
		Document docAirports = buildDocument(doQuery());
		
		// Create a list of nodes from the document
		NodeList nodesAirports = docAirports.getElementsByTagName("Airport");
	
		// Loop through each node and create an 'Airport' class from the data, then add it to the list
		for (int i = 0; i < nodesAirports.getLength(); i++) {
			Element elementAirport = (Element) nodesAirports.item(i);
			Airport airport = createAirport (elementAirport);
			
			if (airport.isValid()) {
				list.add(airport);
			}
		}
		*/
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
