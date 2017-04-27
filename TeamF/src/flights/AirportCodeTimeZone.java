package flights;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This class is used to retrieve offset based on code.
 * 
 * @author Yupeng Su
 * @since 2017-03-20
 *
 */


public class AirportCodeTimeZone {
	
	URL url = getClass().getResource("airportOffsetTime.txt");
	File file = new File(url.getPath());
	
	/**
	 * read file and save to hash map 
	 * @param File type of file relative path
	 * @return HashMap of airport code as key, and offset time as entry
	 */
	public static HashMap<String, Integer> readFile(File file) throws IOException {
	    final HashMap<String, Integer> keyHolder = new HashMap<>();
	    try (BufferedReader br = new BufferedReader(new InputStreamReader(
	            new FileInputStream(file), "UTF-16"))) {
	        for (String line; (line = br.readLine()) != null;) {
	            // processing the line.
	             String[] Contents = line.split(COMMA_DELIMETER);
	             keyHolder.put(Contents[0], Integer.parseInt(Contents[1]));
	             
	        }
	    }
	    return keyHolder;
	}
	private static final String COMMA_DELIMETER = ",";
	
}
	
	
	
