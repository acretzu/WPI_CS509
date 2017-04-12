package userinput;

import java.util.ArrayList;

public class UserInput {
	String depAirport = "";
	String arrAirport = "";
	String depDate = "";
	String returnDate = "";
	String tripType = "roundTrip";
	String classType = "economy";
	int stopNumber = 1;
	boolean oneWay = false;
	boolean roundTrip = true;
	boolean firstClass = false;
	boolean economyClass = true;
	public ArrayList<String> flightList = new ArrayList<String>();
	public void setTripType(String type) 
	{
		tripType = type;
	}
	public String getTripType(String type)
	{
		return tripType;
	}
	
	public void setNumberOfStops(int numberofstops)
	{
		stopNumber = numberofstops;
	}
	public int getNumberOfStops()
	{
		return stopNumber;
	}
	public void setClassType(String classtype)
	{
		classType = classtype;
	}
	public String setNumberOfStops()
	{
		return classType;
	}
	
	
	
}

