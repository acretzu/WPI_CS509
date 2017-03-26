package flights;

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
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
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
		sb.append("SeatingFirstClass ").append("(RemainingSeats: " + num_first_seats + "; " + "Price:" + "$"+ price_first + "), ");
		sb.append("SeatingCoach ").append("(RemainingSeats: " + num_coach_seats + "; " + "Price:" + "$"+ price_coach + ") ");

		return sb.toString();
	}
	public String get_arr_code(){
		return this.arr_code;
	}
	public int get_first_class(){
		return this.num_first_seats;
	}
	public int get_coach(){
		return this.num_coach_seats;
	}
	public String get_flight_model(){
		return this.flight_model;
	}
}
