package airport;

// Imports
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import xml.XmlParser;
import java.util.ArrayList;

/**
 * 
 * @author Cretzu, Adrian
 * @since 2017-02-01
 *
 */
public class AirportContainer extends XmlParser {
	
	// Class variables
	private ArrayList<Airport> list;

	/*
	 * Constructor
	 */
	public AirportContainer() {
		list = new ArrayList<Airport>();
	}
	
	
	/**
	 *  Creates an airport from the given XML node
	 *  
	 *  @param The node of the airport
	 *  @return The airport class that was created from the XML node
	 */
	private Airport createAirport(Node nodeAirport) {
		String name;
		String code;
		double latitude;
		double longitude;
		
		/**
		 * Instantiate an empty Airport object
		 */
		Airport airport = new Airport();
		
		// The airport element has attributes of Name and 3 character airport code
		Element elementAirport = (Element) nodeAirport;
		name = elementAirport.getAttributeNode("Name").getValue();
		code = elementAirport.getAttributeNode("Code").getValue();
		
		// The latitude and longitude are child elements
		Element elementLatLng;
		elementLatLng = (Element)elementAirport.getElementsByTagName("Latitude").item(0);
		latitude = Double.parseDouble(getCharacterDataFromElement(elementLatLng));
		
		elementLatLng = (Element)elementAirport.getElementsByTagName("Longitude").item(0);
		longitude = Double.parseDouble(getCharacterDataFromElement(elementLatLng));
		
		/**
		 * Update the Airport object with values from XML node
		 */
		airport.name(name);
		airport.code(code);
		airport.latitude(latitude);
		airport.longitude(longitude);
		
		return airport;
	}
	
	/**
	 * 
	 * Populates the list of valid airports from the WPI server.
	 * 
	 */
	public void parseAirportsFromSever() throws NullPointerException {		
		
		// Send transaction to server and build a document from the result
		Document docAirports = buildDocument(doQuery("?team=" + teamName + "&action=list&list_type=airports"));
		
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
	}
	
	/**
	 * Return a list of airports
	 * 
	 * @return The list of airports
	 */
	public ArrayList<Airport> getContainer() {
		return list;
	}
}
