package flights;

import java.io.IOException;
import java.util.HashMap;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class flightsTest {
	@Test
	public void testConvert(){
		Flight f = new Flight();
		f.arr_code = "ORD";
		f.dep_code = "SAN";
		f.arr_time = "2017 May 10 14:23 GMT";
		f.dep_time = "2017 May 10 5:45 GMT";
		f.print_dep_local_time();
	}
	
	
	@Test
	public void testRead(){
		HashMap<String, Integer> map = new HashMap<>();
		AirportCodeTimeZone p = new AirportCodeTimeZone();
		try {
			map = AirportCodeTimeZone.readFile(p.file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}