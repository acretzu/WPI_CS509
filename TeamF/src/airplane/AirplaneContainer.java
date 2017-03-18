package airplane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import xml.XmlParser;
import java.util.ArrayList;

public class AirplaneContainer extends XmlParser {
// Class variables
	private ArrayList<Airplane> list;
	
	/*
	 * Constructor
	 */
	public AirplaneContainer() {
		list = new ArrayList<Airplane>();
	}
	
	
	/**
	 * Populates the list of valid airports from the WPI server.
	 */
	public void parseAirplanesFromSever() throws NullPointerException {
		
		System.out.println(doQuery("?team=" + teamName + "&action=list&list_type=airplanes"));
		
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
	public ArrayList<Airplane> getContainer() {
		return list;
	}
}
