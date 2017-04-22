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
		f.arr_code = "BOS";
		f.dep_code = "SAN";
		f.arr_time = "2017 May 10 18:52 GMT";
		f.dep_time = "2017 May 10 17:33 GMT";
		DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy/MM/dd/hh:mm:ss a");
		System.out.println(dtfOut.print(f.get_dep_time_local()));
		System.out.println(f.get_dep_time_local().getHourOfDay());
	}
	
	@Test
	public void testRead(){
		HashMap<String, Integer> map = new HashMap<>();
		AirportCodeTimeZone p = new AirportCodeTimeZone();
		try {
			map = p.readFile(p.file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(map.get("ATL"));
	}
}