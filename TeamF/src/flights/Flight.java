package flights;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;



public class Flight {
	 String flight_model;
	 String flight_time;
	 String flight_number;
	 String dep_code;
	 String dep_time;
	 String arr_code;
	 String arr_time;
	 int num_first_seats;
	 double price_first;
	 int num_coach_seats;
	 double price_coach;
	 Date arr_local_time;
	 Date dep_local_time;
	 
	 public Flight(){
		 flight_model = "";
		 flight_time = "";
		 flight_number = "";
		 dep_code = "";
		 dep_time = "";
		 arr_code = "";
		 arr_time = "";
		 num_first_seats = 0;
		 num_coach_seats = 0;
		 price_first = 0.0;
		 price_coach = 0.0;
	 }
	 public Flight(String f_model, String f_time, String f_number, 
			 String d_code, String d_time, String a_code, String a_time, String n_first, 
			 String p_first, String n_coach, String p_coach){
		 
		 flight_model = f_model;
		 flight_time = f_time;
		 flight_number = f_number;
		 dep_code = d_code;
		 dep_time = d_time;
		 arr_code = a_code;
		 arr_time = a_time;
		 num_first_seats = Integer.parseInt(n_first);
		 price_first = Integer.parseInt(p_first);
		 num_coach_seats = Integer.parseInt(n_coach);
		 price_coach = Integer.parseInt(p_coach);
		
	 }
	 
	 public Flight(String f_model, String f_time, String f_number, 
			 String d_code, String d_time, String a_code, String a_time, int n_first, 
			 double p_first, int n_coach, double p_coach){
		 
		 flight_model = f_model;
		 flight_time = f_time;
		 flight_number = f_number;
		 if (!isValidCode(d_code)) 
				throw new IllegalArgumentException(d_code);
		 if (!isValidCode(a_code)) 
				throw new IllegalArgumentException(a_code);
		 dep_code = d_code;
		 dep_time = d_time;
		 arr_code = a_code;
		 arr_time = a_time;
		 num_first_seats = n_first;
		 price_first = p_first;
		 num_coach_seats = n_coach;
		 price_coach = p_coach;
	 }
	 
	 public boolean isValidCode (String code) {
			// If we don't have a 3 character code it can't be valid valid
			if ((code == null) || (code.length() != 3))
				return false;
			return true;
	}
	
	
	public void set_flight_model(String model){
		this.flight_model = model;
	}
	public void set_flight_time(String time){
		this.flight_time = time;
	}
	public void set_flight_number(String number){
		this.flight_number = number;
	}
	public void set_dep_code(String d_code){
		this.dep_code = d_code;
	}
	public void set_dep_time(String d_time){
		this.dep_time = d_time;
		
	}
	public void set_arr_code(String a_code){
		this.arr_code = a_code;
	}
	public void set_arr_time(String a_time){
		this.arr_time = a_time;
	}
	public void set_first_class(int num_first){
		this.num_first_seats = num_first;
	}
	public void set_coach(int num_coach){
		this.num_coach_seats = num_coach;
	}
	public void set_first_price(double price){
		this.price_first = price;
	}
	public void set_coach_price(double price){
		this.price_coach = price;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("FlightNumber: " + flight_number).append(", ");
		sb.append("AirplaneModel: " + flight_model).append(", ");
		sb.append("FlightTime: " + flight_time).append(", ");
		sb.append("Departure ").append("(AirportCode: ").append(dep_code).append("; " + "DepartureTime: "+ dep_time + "), ");
		sb.append("Arrival ").append("(AirportCode: " + arr_code + "; " + "ArrivalTime: " + arr_time + "), ");
		sb.append("SeatingFirstClass ").append("(SeatsReserved: " + num_first_seats + "; " + "Price:" + "$"+ price_first + "), ");
		sb.append("SeatingCoach ").append("(SeatsReserved: " + num_coach_seats + "; " + "Price:" + "$"+ price_coach + ") ");
		
		return sb.toString();
	}
	public void converAllTimeToLocal(){
		arrTimeStringToDate(this.arr_time);
		depTimeStringToDate(this.dep_time);
		long arr_offset = getOffSetTime(this.arr_code);
		long dep_offset = getOffSetTime(this.dep_code);
		long p = 3600000;
		this.arr_local_time.setTime(this.arr_local_time.getTime() + p * (arr_offset + 1));
		this.dep_local_time.setTime(this.dep_local_time.getTime() + p * (dep_offset + 1));
	}
	
	private void arrTimeStringToDate(String dateStr){
		dateStr = dateStr.replaceAll("\\s", "/");
		System.out.println(dateStr);
		DateFormat df = new SimpleDateFormat("yyyy/MMM/dd/HH:mm",Locale.US);
		try {
			Date localDate = df.parse(dateStr);
			this.arr_local_time = localDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	private void depTimeStringToDate(String dateStr){
		dateStr = dateStr.replaceAll("\\s", "/");
		System.out.println(dateStr);
		DateFormat df = new SimpleDateFormat("yyyy/MMM/dd/HH:mm",Locale.forLanguageTag("english"));
		try {
			Date localDate = df.parse(dateStr);
			this.dep_local_time = localDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	private int getOffSetTime(String coder){
		HashMap<String, Integer> map = new HashMap<>();
		AirportCodeTimeZone p = new AirportCodeTimeZone();
		try {
			map = p.readFile(p.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map.get(coder);
	}
	public String get_arr_code(){
		return this.arr_code;
	}
	public String get_dep_code(){
		return this.dep_code;
	}
	public int get_first_class(){
		return this.num_first_seats;
	}
	public int get_coach(){
		return this.num_coach_seats;
	}
	public double get_coach_price(){
		return this.price_coach;
	}
	public double get_firstClass_price(){
		return this.price_first;
	}
	public String get_arr_time(){
		return this.arr_time;
	}
	public String get_dep_time(){
		return this.dep_time;
	}
	public String get_arr_time_only(){
		int length = this.arr_time.split(" ").length;
		return this.arr_time.split(" ")[length - 1] +" " + this.arr_time.split(" ")[length - 2];
	}
	public String get_arr_day_only(){
		return this.arr_time.split(" ")[0] + " " +this.arr_time.split(" ")[1] + " " +this.arr_time.split(" ")[2];
	}
	public String get_dep_time_only(){
		int length = this.dep_time.split(" ").length;
		return this.dep_time.split(" ")[length - 1] +" " + this.dep_time.split(" ")[length - 2];
	}
	public String get_dep_day_only(){
		return this.dep_time.split(" ")[0] + " " +this.dep_time.split(" ")[1] + " " +this.dep_time.split(" ")[2];
	}
	public String get_flight_model(){
		return this.flight_model;
	}
	public String get_flight_number(){
		return this.flight_number;
	}
	public double get_first_price(){
		return this.price_first;
	}
	public double get_flight_time(){
		return Double.parseDouble(this.flight_time);
	}
	public String get_flight_time_hour_min(){
		Double flightTime = Double.parseDouble(this.flight_time);
		String flight_time_string;
		int flight_time_hours;
		int flight_time_min;
		
		flight_time_hours = (int) (flightTime / 60);
		flight_time_min = (int) (flightTime % 60);
		flight_time_string = getFormated(flight_time_hours) + ":" + getFormated(flight_time_min);
		
		return flight_time_string;
	}
	/**
	 * 
	 * @param convertInt integer to be converted to string 
	 * @return formatedTime integer converted to string with 0 added at the front if less than 10
	 */
	private String getFormated(int convertInt)
	{
		String formatedTime;
		
		if(convertInt < 10)
		{
			formatedTime = "0" + Integer.toString(convertInt);
		}else
		{
			formatedTime = Integer.toString(convertInt);
		}
		return formatedTime;
	} 
	/**
	 * Get this flight's local arrival time 
	 * 
	 * 
	 * @return DateTime (joda time), so you can use getHourOfDay()to get hour, and e.t.c
	 */
	public DateTime get_arr_time_local(){
		converAllTimeToLocal();
		DateTime arrLocalTime = new DateTime(this.arr_local_time);
		return arrLocalTime;
	}
	/**
	 * Get this flight's local departure time 
	 * 
	 * 
	 * @return DateTime (joda time), so you can use getHourOfDay()to get hour, and e.t.c
	 */
	public DateTime get_dep_time_local(){
		converAllTimeToLocal();
		DateTime depLocalTime = new DateTime(this.dep_local_time);
		return depLocalTime;
	}
	/**
	 * print the departure time with format 
	 * ("yyyy/MM/dd/hh:mm:ss a")
	 * for example: 2015/01/15/02:33:01 am
	 */
	public void print_dep_local_time(){
		DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy/MM/dd/hh:mm:ss a");
		System.out.println(dtfOut.print(this.get_dep_time_local()));
	}
	/**
	 * print the arrival time with format 
	 * ("yyyy/MM/dd/hh:mm:ss a")
	 * for example: 2015/01/15/02:33:01 am
	 */
	public void print_arr_local_time(){
		DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy/MM/dd/hh:mm:ss a");
		System.out.println(dtfOut.print(this.get_arr_time_local()));
	}
}
