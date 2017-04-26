package reservation;

import java.util.ArrayList;
//import flights.Flight;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringWriter;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import query.QueryClass;

/**
 * This class is used to lock the server, reserve a flight, and then unlock the server
 * 
 * @author Cretzu, Adrian
 * @since 2017-04-10
 *
 */
public class Reservation extends QueryClass{
	
	/**
	 * Calls the reserveFlight() method with First Class seating
	 * 
	 * @param flights An array list of flights to reserve
	 * @return True if successful, else false
	 */
	public boolean reserveFirstClass(ArrayList<String> flights) {
		return reserveFlight(flights, "FirstClass");
	}
	
	/**
	 * Calls the reserveFlight() method with Coach seating
	 * 
	 * @param flights An array list of flights to reserve
	 * @return True if successful, else false
	 */
	public boolean reserveCoach(ArrayList<String> flights) {		
		return reserveFlight(flights, "Coach");
	}
	
	/**
	 * Converts flights parameter into XML, locks the server, reserves the flight,
	 * and then unlocks the server.
	 * 
	 * @param flights An array of flights to reserve
	 * @param seat First Class or Coach seat
	 * @retrun True if successful, else false
	 */
	private boolean reserveFlight(ArrayList<String> flights, String seat) {
		// Local variables
		boolean success = false;
		boolean lock_success = false;
		
		try {
			// XML builder setup
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document document = docBuilder.newDocument();
			Element rootFlights = document.createElement("Flights");
			document.appendChild(rootFlights);
			
			// Loop thru the list and assemble the XML
			for (String flight : flights) {
				Element childFlight = document.createElement("Flight");
				rootFlights.appendChild(childFlight);
			
				Attr number = document.createAttribute("number");
				number.setValue(flight);
				childFlight.setAttributeNode(number);
			
				Attr seating = document.createAttribute("seating");
				seating.setValue(seat);
				childFlight.setAttributeNode(seating);
			}
			
			// Attempt to lock
			success = lock();
			lock_success = success;
		
			// Only reserve if locking was successful
			if(success) {
				success = success & sendHTTPReservation(getStringFromDocument(document));	
			}
			// Only unlock if locking was successful
			if(lock_success) {
				success =  success & unlock();
			}
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
			return false;
		}
		
		return success;
	}
	
	
	/**
	 * Resets the server
	 * 	 
	 */
	public void resetServer() {
		doQuery("?team=" + teamName + "&action=resetDB");
	}
	 
	/**
	 * Returns the DOM document in string form. 
	 * 
	 * @param doc The DOM document
	 * @return A string representing of the XML within the document
	 */
	private String getStringFromDocument(Document doc) {
		try
	    {
			// Transform the document XML into a String
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
	    }
	    catch(TransformerException ex)
	    {
	       ex.printStackTrace();
	       return null;
	    }
	}
	
	/**
	 * Makes a flight reservation to the server
	 * 
	 * @param xml The XML with included flights for reservation
	 * @return True if successful, else false
	 */
	private boolean sendHTTPReservation(String xml) {
		try {
			URL obj = new URL(mUrlBase);
			HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
	
			// Request Header
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", teamName);
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");			
	
			// Send post request
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
			writer.writeBytes("team=" + teamName + "&action=buyTickets&flightData=" + xml);
			writer.flush();
			writer.close();
	
			int responseCode = connection.getResponseCode();
			
			// Only print if we have an issue
			if(responseCode != 202) {
				System.out.println("Warning! Erroneous response code(sendHTTPReservation): " + responseCode);
				return false;
			}
			else
				return true;
	
			}
		
			// Catch exceptions
			catch (IOException e) {
				e.printStackTrace();
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

	}
	
}
