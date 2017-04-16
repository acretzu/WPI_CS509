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
 *
 */
public class Reservation extends QueryClass{
	
	public boolean reserveFirstClass(ArrayList<String> flights) {
		return reserveFlight(flights, "FirstClass");
	}
	
	public boolean reserveCoach(ArrayList<String> flights) {
		return reserveFlight(flights, "Coach");
	}
	
	/**
	 * 
	 */
	private boolean reserveFlight(ArrayList<String> flights, String seat) {
	//public boolean reserve() {
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document document = docBuilder.newDocument();
			Element rootFlights = document.createElement("Flights");
			document.appendChild(rootFlights);
			
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
			
			System.out.println("XML:\n" + getStringFromDocument(document));
			
			lock();
			sendHTTPReservation(getStringFromDocument(document));
			unlock();

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void resetServer() {
		doQuery("?team=" + teamName + "&action=resetDB");
	}
	 
	
	private String getStringFromDocument(Document doc) {
		try
	    {
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
	
	
	private void sendHTTPReservation(String xml) {
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
			System.out.println("Reserve Response Code : " + responseCode);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	
			//print result
			//System.out.println(response.toString());
	
			}
			catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		
	}
	
	public static void main(String[] args) {
		ArrayList<String> list = new ArrayList<String>();
		//list.add("12345");
		//list.add("54321");
		list.add("3487");
		
		Reservation reservation = new Reservation();
		reservation.reserveFlight(list, "FirstClass");
		//reservation.resetServer();
	}
}
