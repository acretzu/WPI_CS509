package airplane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import airport.Airport;
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
	
	
	private Airplane createAirplane(Node nodeAirplane) {
		String manufac;
		String mdl;
		int first;
		int coach;
		
		Airplane airplane = new Airplane();
		
		Element elementAirplane = (Element) nodeAirplane;
		manufac = elementAirplane.getAttributeNode("Manufacturer").getValue();
		mdl = elementAirplane.getAttributeNode("Model").getValue();
		
		Element elementClass;
		elementClass = (Element)elementAirplane.getElementsByTagName("FirstClassSeats").item(0);
		first = Integer.parseInt(getCharacterDataFromElement(elementClass));
		
		elementClass = (Element)elementAirplane.getElementsByTagName("CoachSeats").item(0);
		coach = Integer.parseInt(getCharacterDataFromElement(elementClass));

		/**
		 * Update the Airport object with values from XML node
		 */
		airplane.manufacturer(manufac);
		airplane.model(mdl);
		airplane.firstClass(first);
		airplane.coachClass(coach);
		
		return airplane;
	}
	
	public void parseAirplanesFromSever() throws NullPointerException {
		Document docAirplanes = buildDocument(doQuery("?team=" + teamName + "&action=list&list_type=airplanes"));
		NodeList nodesAirplanes = docAirplanes.getElementsByTagName("Airplane");
		
		for (int i = 0; i < nodesAirplanes.getLength(); i++) {
			Element elementAirplane = (Element) nodesAirplanes.item(i);
			Airplane airplane = createAirplane (elementAirplane);
			list.add(airplane);
		}
	}
	
	public ArrayList<Airplane> getContainer() {
		return list;
	}
}
