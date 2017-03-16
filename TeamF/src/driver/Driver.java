/**
 * 
 */
package driver;

import airport.Airport;
import airport.Airports;
import dao.ServerInterface;

/**
 * @author blake
 *
 */
public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerInterface resSys = new ServerInterface();
		//if (args.length != 1) {
		//	System.err.println("usage: CS509.sample teamName");
		//	System.exit(-1);
		//	return;
		//}
		
		String teamName = "TeamF";
		// Try to get a list of airports
		System.out.println("Before getAirports!");
		Airports airports = resSys.getAirports(teamName);
		System.out.println("After getAirports!");
		for (Airport airport : airports) {
			System.out.println(airport.toString());
		}
	}
}
