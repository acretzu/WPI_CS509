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
        // Try to get a list of airport	
		Airports airports = resSys.getAirports(teamName);
		for (Airport airport : airports) {
			System.out.println(airport.toString());
		}
	}
}
