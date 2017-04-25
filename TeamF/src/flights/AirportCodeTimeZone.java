package flights;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;




public class AirportCodeTimeZone {
	
	URL url = getClass().getResource("airportOffsetTime.txt");
	File file = new File(url.getPath());
	public static HashMap<String, Integer> readFile(File file) throws IOException {
	    final HashMap<String, Integer> keyHolder = new HashMap<>();
	    try (BufferedReader br = new BufferedReader(new InputStreamReader(
	            new FileInputStream(file), "UTF-16"))) {
	        for (String line; (line = br.readLine()) != null;) {
	            // processing the line.
	             String[] Contents = line.split(COMMA_DELIMETER);
	             //System.out.printf("%s %s \n", Contents[0], Contents[1]);
	             keyHolder.put(Contents[0], Integer.parseInt(Contents[1]));
	             
	        }
	    }
	    return keyHolder;
	}
	private static final String COMMA_DELIMETER = ",";
	
}
	
	
	
