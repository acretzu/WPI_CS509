package gui;

import java.util.Scanner;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Container;

import javax.swing.*;//JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import javax.swing.JOptionPane;
import airport.AirportContainer;
import controller.Controller;
import flights.DepartingFlightsContainer;
import flights.ArrivingFlightsContainer;
import flights.Flight;
import airplane.AirplaneContainer;
import airplane.Airplane;
import userinput.UserInput;
import java.util.ArrayList;
import airport.Airport;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.joda.time.DateTime;

import sorting.SortingClass;

import Trip.Trip;
import flights.FlightsContainer;
/**
 * This class creates the GUI window, which is then used to pass on user input
 * to appropriate classes so flight search, reserve and sort functions can be performed
 * based on user input.
 * to  to search for flights,
 * sort flights, and reserve flights
 * @author Ilya Lifshits
 * @version 1.0
 * @since 2017-02-15
 * 
 *
 */
public class GUI extends JFrame {
	
	//GUI object initialisation 
    private JTextField desField = new JTextField("BOS");
    private JTextField depField = new JTextField("SFO");
    private JTextField depDateTF = new JTextField("05/15/2017");
    private JTextField retDateTF = new JTextField("05/16/2017");
     
    private JRadioButton RndTripRb = new JRadioButton("Round Trip");
    private JRadioButton OneWayRb = new JRadioButton("One Way");
    private JRadioButton  NoStopRb= new JRadioButton("None-stop");
    private JRadioButton  OneStopRb= new JRadioButton("One Stop");
    private JRadioButton  TwoStopRb= new JRadioButton("Two Stops");
    private JRadioButton  FirstClassRb= new JRadioButton("First Class");
    private JRadioButton  EconomyClassRb= new JRadioButton("Economy Class");
    private JRadioButton  SortByPriceRb= new JRadioButton("Price");
    private JRadioButton  SortByTravelTimeRb= new JRadioButton("Travel Time");
    private JRadioButton  SearchByArrDate = new JRadioButton("Search by Arrival Date");
    private JRadioButton  SearchByDepDate = new JRadioButton("Search by Departure Date");
     
   
    private JButton searchButton = new JButton("Search");
    private JButton clearSearchButton = new JButton("Restart Search");
    private JButton sortButton =  new JButton("Sort");
    private JButton reserveButton =  new JButton("Reserve");
    private JButton detailsButton = new JButton("Details");;
    private JLabel depFlightsLabel = new JLabel("Departure Flights");
    private JLabel retFlightsLabel = new JLabel("Return Flights");
    
    
    
    private JLabel deplb = new JLabel("Departure Airport    ");
    private JLabel destlb = new JLabel("Destination Airport    ");
    private JLabel depDateTFlb = new JLabel("Departure Date    ");
    private JLabel retDateTFlb = new JLabel("Return Date    ");
   
    
    //panel1 holds dept/arr airport and dept/return date controls 
    private JPanel panel1 = new JPanel(new GridBagLayout());
	private JPanel panel2 = new JPanel(new GridBagLayout());
	private JPanel panel3 = new JPanel(new GridBagLayout());
    private JPanel panelRb = new JPanel(new GridBagLayout());
	private JPanel panel4 = new JPanel(new GridBagLayout());
	
    private boolean roundTrip = true;
    private String numStops = "1";
	
    
    
    private ArrayList<String> airportNames;
    private ArrayList<String> airportCodes;
   
    JComboBox depList ;
	JComboBox arrList ;
    JList searchResultsDep;
    JList searchResultsRet;
    JList flightDepDetails;
    JList flightRetDetails;
    
    DefaultListModel<String> modelDep; //model to hold Departure flights to be populated in departure list
    DefaultListModel<String> modelRet;//model to hold Returning flights to be populated in departure list
    DefaultListModel<String> flightDetailModel; //model to hold returning flight details to be populated in returning details list
    
    
    ArrayList<Flight> depFlights;
    ArrayList<Airport> airports;
    ArrayList<ArrayList<Flight>> flightListDep = new ArrayList<ArrayList<Flight>>();
    ArrayList<ArrayList<Flight>> flightListRet = new ArrayList<ArrayList<Flight>>();
	
    
    JScrollPane searchResultsDepScrollPane;
    JScrollPane searchResultsRetScrollPane;
    
    JScrollPane detailsDepScrollPane;
    JScrollPane detailsRetScrollPane;
    
    boolean firstClass; //user selection for flight class
    boolean sortByPrice;
    boolean sortByTime;
    
	int numberOfStops;
	ArrayList<Flight> reserveDepFlightList = new ArrayList<Flight>();
	ArrayList<Flight> reserveRetFlightList = new ArrayList<Flight>();
	flights.FlightsContainer flightContrainer = new flights.FlightsContainer();
	reservation.Reservation reserve = new reservation.Reservation();
	
	Controller controller = new Controller();
    Trip trip = new Trip();
    sorting.SortingClass sort= new sorting.SortingClass();
	
	public GUI(){
		
		
		super("User Interface");
		
		firstClass = false;
		sortByPrice = false;
		sortByTime = true;
		
		numberOfStops = 1;
		panel2.setSize(400, 400);
		panel1.setSize(400, 400);
		panelRb.setSize(400, 400);
		
		airportNames = new  ArrayList<String>();
		airportCodes = new ArrayList<String>();
		
		airports = new ArrayList<Airport>();
		airports = controller.getAirports();
		depFlights = new ArrayList<Flight>();
		
		
		
		depList = new JComboBox();
		arrList = new JComboBox();
		searchResultsDep = new JList();
		searchResultsRet = new JList();
		flightDepDetails = new JList();
		flightRetDetails = new JList();
		//searchResultsDep.setPreferredSize(new Dimension(250, 80));
		searchResultsDep.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		modelDep = new DefaultListModel<String>();
		modelRet = new DefaultListModel<String>();
		
		searchResultsDep.setModel(modelDep);
		searchResultsRet.setModel(modelRet);
		
		flightDepDetails.setModel(modelDep);
		flightRetDetails.setModel(modelRet);
		
		String holdName = new String();
		for(int i = 0; i < airports.size();i++)
		{
			holdName = airports.get(i).name() + "_" + airports.get(i).code();
			depList.addItem(holdName);
			arrList.addItem(holdName);
		}
		
		GridBagConstraints c = new GridBagConstraints();
///////////Radio button panel
    
   
		
    c.gridx = 0; 
    c.gridy = 5;
    panel3.add(depList, c);
    RndTripRb.setSelected(true);
    panelRb.add(RndTripRb,c);

    c.gridy = 10;
    panelRb.add(OneWayRb,c);
 
    c.gridy = 15;
    panelRb.add(NoStopRb,c);
 
    c.gridy = 20;
    OneStopRb.setSelected(true);
    panelRb.add(OneStopRb,c);
    
    c.gridx = 0; 
    c.gridy = 25;
    panelRb.add(TwoStopRb,c);
    
    c.gridx = 0; 
    c.gridy = 30;
    panelRb.add(FirstClassRb,c);
    
    c.gridx = 0; 
    c.gridy = 35;
    panelRb.add(EconomyClassRb,c);
    EconomyClassRb.setSelected(true);
    //////////Panel2        
     c.anchor=GridBagConstraints.WEST;//left align components after this point
/////////Panel1
        c.gridx = 0; 
        c.gridy = 10;
        panel1.add(deplb,c);

        c.gridx = 0; 
        c.gridy = 15;
        panel1.add(destlb,c);
     
        c.gridx = 0; 
        c.gridy = 22;
        panel1.add(depDateTFlb,c);
     
        c.gridx = 0; 
        c.gridy = 25;
        panel1.add(retDateTFlb,c);
     
        
        c.gridx = 20;
        c.gridy = 10;
        panel1.add(depList,c);

        c.gridy = 15;
        panel1.add(arrList,c);
        

        c.gridx = 0;
        c.gridy = 21;
        SearchByDepDate.setSelected(true);
        panel1.add(SearchByDepDate,c);
        
        c.gridx = 20;
        c.gridy = 21;
        panel1.add(SearchByArrDate,c);
        SearchByArrDate.setSelected(false);
        
        c.gridy = 22;
        panel1.add(depDateTF,c);
        c.gridx = 25;
        c.gridx = 20;
        c.gridy = 25;
        panel1.add(retDateTF,c);

        /////////////////PANEL 3
        c.gridx = 0;
        c.gridy = 0;
        panel3.add(searchButton, c);
        
        
        c.gridx = 0;
        c.gridy = 1;
        clearSearchButton.setEnabled(false);
        panel3.add(clearSearchButton, c);
        
        c.gridx = 0;
        c.gridy = 3;
        panel3.add(depFlightsLabel, c);
        
        c.gridx = 0;
        c.gridy = 21;
        panel3.add(retFlightsLabel, c);
        
        c.gridx = 120;
        c.gridy = 20;
        panel3.add(flightDepDetails, c);
        
        c.gridy = 25;
        panel3.add(flightRetDetails, c);
        
        c.gridx = 0;
        c.gridy = 30;
        panel3.add(reserveButton, c);
        
        c.gridx = 5;
        c.gridy = 15;
        panel4.add(sortButton, c);
        
        c.gridx = 10;
        c.gridy = 15;
        panel4.add(SortByTravelTimeRb, c);
        
        c.gridx = 10;
        c.gridy = 20;
        panel4.add(SortByPriceRb, c);
        
        
        c.gridx = 0;
        c.gridy = 20;
        
        searchResultsDepScrollPane = new JScrollPane(searchResultsDep);
        panel3.add(searchResultsDepScrollPane, c);
        
        searchResultsRetScrollPane = new JScrollPane(searchResultsRet);
        c.gridy = 25;
        panel3.add(searchResultsRetScrollPane, c);
        
        detailsDepScrollPane = new JScrollPane(flightDepDetails);
        detailsRetScrollPane = new JScrollPane(flightRetDetails);
        
        c.gridx = 5;
        c.gridy = 20;
        
        panel3.add(detailsDepScrollPane, c);
        
        searchResultsDepScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);		
        searchResultsDepScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
        searchResultsDepScrollPane.setPreferredSize(new Dimension(400,250));
        detailsDepScrollPane.setPreferredSize(new Dimension(400,250));
        
        c.gridy = 25;
        panel3.add(detailsRetScrollPane, c);
        detailsRetScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);		
        detailsRetScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
       
        detailsRetScrollPane.setPreferredSize(new Dimension(400,250));

        searchResultsRetScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);		
        searchResultsRetScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
        searchResultsRetScrollPane.setPreferredSize(new Dimension(400,250));

        
        SortByTravelTimeRb.setSelected(true);
        
        thehandler handler = new thehandler();
        SelectionListener selectionListener = new SelectionListener();
        this.getContentPane().add(panel1, BorderLayout.WEST);
        //this.getContentPane().add(panel2, BorderLayout.EAST);
        this.getContentPane().add(panelRb, BorderLayout.CENTER);
        
        this.getContentPane().add(panel3, BorderLayout.SOUTH);
        this.getContentPane().add(panel4, BorderLayout.EAST);
         
        

        
        OneWayRb.addActionListener(handler);
        RndTripRb.addActionListener(handler);
        TwoStopRb.addActionListener(handler);
        OneStopRb.addActionListener(handler);
        TwoStopRb.addActionListener(handler);
        NoStopRb.addActionListener(handler);
        searchButton.addActionListener(handler);
        detailsButton.addActionListener(handler);
        SortByPriceRb.addActionListener(handler);
        SortByTravelTimeRb.addActionListener(handler);
        FirstClassRb.addActionListener(handler);
        EconomyClassRb.addActionListener(handler);
        sortButton.addActionListener(handler);
        reserveButton.addActionListener(handler);
        SearchByDepDate.addActionListener(handler);
        SearchByArrDate.addActionListener(handler);
        clearSearchButton.addActionListener(handler);
        searchResultsDep.addListSelectionListener(selectionListener);
        searchResultsRet.addListSelectionListener(selectionListener);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1150, 850);
        

	}
	
	/**
	 * populates GUI departurelist or returnlist object with summary of each trip
	 * returned from search results
	 * @param flightList list of flights to be populated in departureList or returnList with
	 * @param populateReturnList true if returnList is to be populated,
	 * false if departureList is to be populated
	 */
	private void populateSeachResultsList(ArrayList<ArrayList<Flight>> flightList, boolean populateReturnList)
	{
		int stopNumber=0;
		String builtFlightString="";
		modelDep = new DefaultListModel<String>(); 
		modelRet = new DefaultListModel<String>(); 
		String stopNumberS = "";
		double totalPrice = 0;
		String totalPriceS = "";
		String departTime;
		DateTime depDateTime = new DateTime();
		DateTime retDateTime = new DateTime();
		for(int i = 0; i<flightList.size(); i++)
		{
			
			stopNumber = flightList.get(i).size()-1;
			stopNumberS = Integer.toString(flightList.get(i).size()-1);
			totalPrice = sort.get_total_price( flightList.get(i), firstClass);
			totalPriceS = Double.toString((Math.round(totalPrice*100D))/100D);
			depDateTime = flightList.get(i).get(0).get_dep_time_local();
			retDateTime = flightList.get(i).get(stopNumber).get_arr_time_local();
				
				builtFlightString = "Stops #: " + stopNumberS +
						" Total Price: " + totalPriceS +
						"  Leave at: " + getFormated(depDateTime.getHourOfDay()) + ":" 
						+ getFormated(depDateTime.getMinuteOfHour()) + 
						"  Arrive at: " + getFormated(retDateTime.getHourOfDay()) + ":" 
						+ getFormated(retDateTime.getMinuteOfHour()) + 
						" Total travel time...";
			for(int j = 0; j < flightList.get(i).size(); j++)
			{
			//	System.out.println(flightList.get(i).get(j).toString());
			}
			if(!populateReturnList)
			{
				modelDep.addElement(builtFlightString);
			}else
			{
				modelRet.addElement(builtFlightString);
			}
			builtFlightString = "";
		}
		
		//System.out.println("\n next flight");
	
		
		if(!populateReturnList)
		{
			searchResultsDep.removeAll();
			if(modelDep.isEmpty())
			{
				modelDep.addElement("No flights found");
			}
			searchResultsDep.setModel(modelDep);	
			System.out.println("Number of departing flights found " 
			+ Integer.toString(flightList.size()) + "\n");
		}else
		{
			searchResultsRet.removeAll();
			if(modelRet.isEmpty())
			{
				modelRet.addElement("No flights found");
			}
			searchResultsRet.setModel(modelRet);
			
			System.out.println("Number of return flirghts found " 
					+ Integer.toString(flightList.size()) + "\n");
		}
		
	}
	
	/**
	 * Converts numbers to strings if the number is less than ten 
	 * adds 0 at the front of the number for better formatting 
	 * @param convertInt integer to be converted to string 
	 * @return formatedTime, integer converted to string with 0 added at 
	 * the front if integer is less than 10
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
 * populates GUI trip details
 * @param flightList list of list of flights(list of trips) 
 * @param flightListIndex index of the trip to display details about
 * @param populateReturnDetailsList true if populating details about return flight
 * falls if populating details about departure flights
 */
	private void populateDetailsList(ArrayList<ArrayList<Flight>>  flightList, 
			int flightListIndex,boolean populateReturnDetailsList)
	{
		String Depart = "Depart: ";
		String TimeDep = "at: ";
		String DateDep = "on: ";
		String Arive = "Arrive: ";
		String TimeArr = "at: ";
		String DateArr = "on: ";
		String PlaneType = "Plane type: ";
		String FlightTime = "Flight time: ";
		String FlightNumber = "Flight number: ";
		String NumberOfFistClassSeats = "Number of first class seats left: ";
		String NumberOfCouachSeats = "Number of coach seats left: ";
						
		ArrayList<Flight> DepFlightTrip =  new ArrayList<Flight>();
		ArrayList<Flight> RetFlightTrip =  new ArrayList<Flight>();
		
		for(int j = 0; j < flightList.get(flightListIndex).size(); j++)
		{
			 Depart = "Depart: " + flightList.get(flightListIndex).get(j).get_dep_code();
			 flightDetailModel.addElement(Depart);
			 TimeDep = "at: " + getFormated(flightList.get(flightListIndex).get(j).get_dep_time_local().getHourOfDay())
					 +":" + getFormated(flightList.get(flightListIndex).get(j).get_dep_time_local().getMinuteOfHour());
			 flightDetailModel.addElement(TimeDep);
			 DateDep = "on: " + getFormated(flightList.get(flightListIndex).get(j).get_dep_time_local().getMonthOfYear())
					 + "/" + getFormated(flightList.get(flightListIndex).get(j).get_dep_time_local().getDayOfMonth()) 
					 +  "/" + flightList.get(flightListIndex).get(j).get_dep_time_local().getYear(); 		 
			 flightDetailModel.addElement(DateDep);
			 Arive = "Arrive: " + flightList.get(flightListIndex).get(j).get_arr_code();
			 flightDetailModel.addElement(Arive);
			 TimeArr = "at: " + getFormated(flightList.get(flightListIndex).get(j).get_arr_time_local().getHourOfDay())
					 + ":" + getFormated(flightList.get(flightListIndex).get(j).get_arr_time_local().getMinuteOfHour());
			 flightDetailModel.addElement(TimeArr);
			 DateArr = "on: " + getFormated(flightList.get(flightListIndex).get(j).get_arr_time_local().getMonthOfYear())
				 + "/" + getFormated(flightList.get(flightListIndex).get(j).get_arr_time_local().getDayOfMonth()) 
				 +  "/" + flightList.get(flightListIndex).get(j).get_arr_time_local().getYear(); 		 
				 flightDetailModel.addElement(DateDep);
		
			 PlaneType = "Plane type: " + flightList.get(flightListIndex).get(j).get_flight_model();
			 flightDetailModel.addElement(PlaneType);
			 FlightTime = "Flight time: " + flightList.get(flightListIndex).get(j).get_flight_time_hour_min();
			 flightDetailModel.addElement(FlightTime);
			 FlightNumber = "Flight number: " + flightList.get(flightListIndex).get(j).get_flight_number();
			 flightDetailModel.addElement(FlightNumber);
			 flightDetailModel.addElement("      ");
			 if(!populateReturnDetailsList)
			 {  
				 DepFlightTrip.add(flightList.get(flightListIndex).get(j));
			 }else
			 {
				 RetFlightTrip.add(flightList.get(flightListIndex).get(j));		 
			 }
		}
		
		if(!populateReturnDetailsList)
		{
			flightDetailModel.addElement("Total price: $" + Double.toString(Math.round(sort.get_total_price(DepFlightTrip, firstClass)*100D)/100D));
			
			System.out.println("Selection Made: " + searchResultsDep.getSelectedValue());
			flightDepDetails.setModel(flightDetailModel);
		}else
		{
			flightDetailModel.addElement("Total price: $" + Double.toString(Math.round(sort.get_total_price(RetFlightTrip, firstClass)*100D)/100D));
			
			System.out.println("Selection Made: " + searchResultsRet.getSelectedValue());
			flightRetDetails.setModel(flightDetailModel);
		}
		
	}
	/**
	 * Looks at the flights selected by the user and returns a list
	 * of strings representing all flight numbers the user wants to 
	 * reserve
	 * @param roundTrip represents if the user is reserving roundTrip
	 * flight or not
	 * @return ArrayList<String> which holds all flight numbers for 
	 * user selected to reserve
	 */
	private ArrayList<String> getReserveFlightList(boolean roundTrip)
	{
		
		ArrayList<String> reserveList = new ArrayList<String>();
		int tripIndexDep = searchResultsDep.getSelectedIndex();
		
		ArrayList<Flight> reserveDepTrip = new ArrayList<Flight>();
		reserveDepTrip = flightListDep.get(tripIndexDep);
		
		ArrayList<Flight> reserveRetTrip = new ArrayList<Flight>();
		int tripIndexRet;
		
		if(roundTrip)
		{
			tripIndexRet = searchResultsRet.getSelectedIndex();
			reserveRetTrip = flightListRet.get(tripIndexRet);
		
		}
		
		for(int i = 0; i < reserveDepTrip.size(); i++ )
		{
			reserveList.add(reserveDepTrip.get(i).get_flight_number());
		}
		for(int i = 0; i < reserveRetTrip.size(); i++ )
		{
			reserveList.add(reserveRetTrip.get(i).get_flight_number());
		}
				
		return reserveList;
	}
	
	/**
	 * disables all gui controls used for changing search parameters enables 
	 * clearSearchButton
	 */
	private void disableAllSearchControls()
	{
		RndTripRb.setEnabled(false);
		OneWayRb.setEnabled(false);
		NoStopRb.setEnabled(false);
		OneStopRb.setEnabled(false);
		TwoStopRb.setEnabled(false);
		FirstClassRb.setEnabled(false);
		EconomyClassRb.setEnabled(false);
		depDateTF.setEnabled(false);
		retDateTF.setEnabled(false);
		searchButton.setEnabled(false);
		depList.setEnabled(false);
		arrList.setEnabled(false);
		SearchByArrDate.setEnabled(false);
		SearchByDepDate.setEnabled(false);
		clearSearchButton.setEnabled(true);
	}
	
	/**
	 * enables all gui controls used for changing search parameters disables 
	 * clearSearchButton
	 */
	private void enableAllSearchControls()
	{
		RndTripRb.setEnabled(true);
		OneWayRb.setEnabled(true);
		NoStopRb.setEnabled(true);
		OneStopRb.setEnabled(true);
		TwoStopRb.setEnabled(true);
		FirstClassRb.setEnabled(true);
		EconomyClassRb.setEnabled(true);
		depDateTF.setEnabled(true);
		retDateTF.setEnabled(true);
		searchButton.setEnabled(true);
		depList.setEnabled(true);
		arrList.setEnabled(true);
		SearchByArrDate.setEnabled(true);
		SearchByDepDate.setEnabled(true);
		clearSearchButton.setEnabled(false);
		
	}
	/**
	 * clears all search results displayed in the GUI
	 */
	private void clearSearchResults()
	{
		searchResultsDep.setModel(new  DefaultListModel<String>());
		searchResultsRet.setModel(new  DefaultListModel<String>());
		flightDepDetails.setModel(new  DefaultListModel<String>());
		flightRetDetails.setModel(new  DefaultListModel<String>());
	}
	/**
	 * Checks if specified date is within allowed range
	 * @param checkDate array of 3 strings where:
	 * String[0] is month of the date to be checked 
	 * String[1] is day of the date to be checked
	 * String[2] is year of the date to be checked
	 * 
	 * @return false if month!=5, 0>day>30, year!=2017 else
	 * return true
	 */
	private boolean isDateValid(String[] checkDate)
	{
		int month = Integer.parseInt(checkDate[0]);
		int date = Integer.parseInt(checkDate[1]);
		int year = Integer.parseInt(checkDate[2]);
		
		if(year!=2017)
		{
			JOptionPane.showMessageDialog(null,"Year must be 2017\n",
					"Error",JOptionPane.WARNING_MESSAGE);
			return false;

		}else if(month != 5)
		{
			JOptionPane.showMessageDialog(null,"Month must be 5",
					"Error",JOptionPane.WARNING_MESSAGE);
			return false;

		}if(date > 31 || date < 1)
		{
			JOptionPane.showMessageDialog(null,"Date must be between 1 and 30",
					"Error",JOptionPane.WARNING_MESSAGE);
			return false;

		}
		
		return true;
	}
	/**
	 * Handles selectionchange event for search result lists
	 * @author Ilya Lifshits
	 *
	 */
	 private class SelectionListener implements ListSelectionListener {
         public void valueChanged(ListSelectionEvent event) {
            
         	
         	if (!event.getValueIsAdjusting()){
          
         		if(event.getSource() == searchResultsDep)
         		{
	         		JList source = (JList)event.getSource();
	         		if(!searchResultsDep.isSelectionEmpty())
	                 {
	                 
		            		String selected = source.getSelectedValue().toString();
		                    flightDetailModel = new DefaultListModel<String>();
		    				
		                    int selectionIndex = searchResultsDep.getSelectedIndex();
		                	
		                    populateDetailsList(flightListDep, selectionIndex, false);
		                    
	                 }
         		}else if(event.getSource() == searchResultsRet)
         		{
         			JList source = (JList)event.getSource();
            		if(!searchResultsRet.isSelectionEmpty())
                    {
                    
	            		String selected = source.getSelectedValue().toString();
	                    flightDetailModel = new DefaultListModel<String>();
	    				
	                    
	                    int selectionIndex = searchResultsRet.getSelectedIndex();
	                	
	                    populateDetailsList(flightListRet, selectionIndex, true);
	                    
                    }
         		}
             }
         }
     }
	/**
	 * Handles all GUI events except for events for list selection which is handled by 
	 * SelectionListener
	 * @author Ilya Lifshits
	 *
	 *
	 */
	private class thehandler implements ActionListener{
		
		public void actionPerformed(ActionEvent event){
		
			if (event.getSource() == searchButton) 
			{
				searchResultsDep.clearSelection();
				flightDepDetails.clearSelection();
				modelDep = new DefaultListModel<String>();
				modelRet = new DefaultListModel<String>();
				searchResultsDep.setModel(modelDep);
				searchResultsRet.setModel(modelDep);
				String[] parseArrAirport;
				String destinationAirport;
				String depdate;
				
				String[] parseDepDate;
				String[] parseDepAirport;
				String departureAirport;
				parseDepDate = depDateTF.getText().split("/");
				parseDepAirport = depList.getSelectedItem().toString().split("_");
				departureAirport = parseDepAirport[1];
				depdate = parseDepDate[2] + "_" + parseDepDate[0] + "_"+ parseDepDate[1];
				
				
				String[] parseRetDate;
				String retdate;
				parseRetDate = retDateTF.getText().split("/");
				parseArrAirport = arrList.getSelectedItem().toString().split("_");
				destinationAirport = parseArrAirport[1];
				retdate = parseRetDate[2] + "_" + parseRetDate[0] + "_"+ parseRetDate[1];
				
				if(!isDateValid(parseRetDate) || !isDateValid(parseDepDate))
				{
					return;
				}
				if(departureAirport.equals(destinationAirport))
				{
					JOptionPane.showMessageDialog(null,"Departure airport can not be \n"
							+ " the same as arrival airport",
															"Error",JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				
				disableAllSearchControls();
				
				System.out.println("Searching for flights from... \n" + departureAirport
						+ " tp " +  destinationAirport);
			
				if(SearchByDepDate.isSelected())
				{
					flightListDep = trip.getFlightOptionsByDeparting(departureAirport,
						destinationAirport, depdate, Integer.toString(numberOfStops),
						firstClass);
				}else
				{
					flightListDep = trip.getFlightOptionsByArrival(departureAirport,
							destinationAirport, depdate, Integer.toString(numberOfStops),
							firstClass);
				}
				
				sorting.SortingClass sorts;
				populateSeachResultsList(flightListDep, false);
				
				if(roundTrip)
				{
					if(SearchByDepDate.isSelected())
					{
						flightListRet = trip.getFlightOptionsByDeparting(destinationAirport,
								departureAirport, retdate, Integer.toString(numberOfStops),
								firstClass);
					}else
					{
						flightListRet = trip.getFlightOptionsByArrival(destinationAirport,
								departureAirport, retdate, Integer.toString(numberOfStops),
								firstClass);
					}
					populateSeachResultsList(flightListRet, true);
					
					if(flightListDep.isEmpty() || flightListRet.isEmpty())
					{
						modelDep.removeAllElements();
						modelDep.addElement("No round trip flights found");
						searchResultsDep.setModel(modelDep);
						
						modelRet.removeAllElements();
						modelRet.addElement("No round trip flights found");
						searchResultsRet.setModel(modelRet);
						
						
					}
				}
				
				

			}else if(event.getSource() == OneWayRb)
			{
				OneWayRb.setSelected(true);
				RndTripRb.setSelected(false);
				roundTrip = false;
				searchResultsRet.setVisible(false);
				flightRetDetails.setVisible(false);
				searchResultsRet.setEnabled(false);
				flightRetDetails.setEnabled(false);
				
			}else if(event.getSource() == RndTripRb)
			{
				OneWayRb.setSelected(false);
				RndTripRb.setSelected(true);
				roundTrip = true;
				searchResultsRet.setVisible(true);
				flightRetDetails.setVisible(true);
				searchResultsRet.setEnabled(true);
				flightRetDetails.setEnabled(true);
				
			}else if(event.getSource() == NoStopRb)
			{
				NoStopRb.setSelected(true);
				OneStopRb.setSelected(false);
				TwoStopRb.setSelected(false);
				numberOfStops = 0;
				
			}else if(event.getSource() == OneStopRb)
			{
				OneStopRb.setSelected(true);
				NoStopRb.setSelected(false);
				TwoStopRb.setSelected(false);
				numberOfStops = 1;
			}else if(event.getSource() == TwoStopRb)
			{
				TwoStopRb.setSelected(true);
				OneStopRb.setSelected(false);
				NoStopRb.setSelected(false);
				numberOfStops = 2;
			}else if(event.getSource() == SortByTravelTimeRb)
			{
				SortByTravelTimeRb.setSelected(true);
				SortByPriceRb.setSelected(false);
				sortByPrice = false;
				sortByTime = true;
			}else if(event.getSource() == SortByPriceRb)
			{
				SortByPriceRb.setSelected(true);
				SortByTravelTimeRb.setSelected(false);
				sortByPrice = true;
				sortByTime = false;
			}else if(event.getSource() == FirstClassRb) 
			{
				EconomyClassRb.setSelected(false);
				FirstClassRb.setSelected(true);
				firstClass = true;
			}else if(event.getSource() == EconomyClassRb)
			{
				FirstClassRb.setSelected(false);
				EconomyClassRb.setSelected(true);
				firstClass = false;
			}else if(event.getSource() == sortButton)
			{
				if(sortByPrice && !firstClass)
				{
					flightListDep = sort.sortedFlights_Coach_Price(flightListDep);
					populateSeachResultsList(flightListDep, false);
					
					flightListRet = sort.sortedFlights_Coach_Price(flightListRet);
					populateSeachResultsList(flightListRet, true);
					
				}else if(sortByPrice && firstClass)
				{
					flightListDep = sort.sortedFlights_FirstClass_Price(flightListDep);
					populateSeachResultsList(flightListDep, false);
					
					flightListRet = sort.sortedFlights_FirstClass_Price(flightListRet);
					populateSeachResultsList(flightListRet, true);
				}else 
				{
					flightListDep = sort.sortedFlight_FlightTime(flightListDep);
					populateSeachResultsList(flightListDep, false);
					
					flightListRet = sort.sortedFlight_FlightTime(flightListRet);
					populateSeachResultsList(flightListRet, true);
				}
				
			}else if(event.getSource() == reserveButton)
			{
				if(firstClass)
				{
					if(!searchResultsDep.isSelectionEmpty())
					{
						if(roundTrip && !searchResultsRet.isSelectionEmpty())
						{
							reserve.reserveFirstClass(getReserveFlightList(roundTrip));
							
						}else
						{
							JOptionPane.showMessageDialog(null,"Returning trip must be selcted \n",
									"Error",JOptionPane.WARNING_MESSAGE);
						}
					}else
					{
						JOptionPane.showMessageDialog(null,"Departing trip must be selcted",
								"Error",JOptionPane.WARNING_MESSAGE);
						return;
					}
					
				}
				else
				{
					if(!searchResultsDep.isSelectionEmpty())
					{
						if(roundTrip && !searchResultsRet.isSelectionEmpty())
						{
							reserve.reserveCoach(getReserveFlightList(roundTrip));
							
						}else
						{
							JOptionPane.showMessageDialog(null,"Returning trip must be selcted \n",
									"Error",JOptionPane.WARNING_MESSAGE);
						}
					}else
					{
						JOptionPane.showMessageDialog(null,"Departing trip must be selcted",
								"Error",JOptionPane.WARNING_MESSAGE);
						return;
					}
					
						
					
				}
			}else if(event.getSource() == SearchByArrDate)
			{
				depDateTFlb.setText("Arrival Date");
				SearchByDepDate.setSelected(false);
			}else if(event.getSource() == SearchByDepDate)
			{
				depDateTFlb.setText("Departure Date");
				SearchByArrDate.setSelected(false);
			}else if(event.getSource() == clearSearchButton)
			{
				enableAllSearchControls();
				clearSearchResults();
				
			}
			
			
		}
	}
}
