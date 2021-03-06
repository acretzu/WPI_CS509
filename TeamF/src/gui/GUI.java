package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;//JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JOptionPane;
import airport.AirportContainer;
import airport.Airport;
import flights.Flight;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.joda.time.DateTime;
import Trip.Trip;

/**
 * Creates the GUI and all the gui objects, passes user information to trip class for 
 * flight searches, uses sorting class to sort search results and display them
 * 
 * @author Ilya Lifshits
 * @version 1.0
 * @since 2017-02-15
 * 
 *
 */
public class GUI extends JFrame {
	
	sorting.SortingClass sort= new sorting.SortingClass();
	
	//GUI object initialisation 
    private JTextField depDateTF = new JTextField("05/15/2017");
    private JTextField retDateTF = new JTextField("05/16/2017");
    private JTextField depTimeMin = new JTextField("12:00pm");
    private JTextField depTimeMax = new JTextField("1:00pm");
    private JTextField arrTimeMin = new JTextField("12:00pm");
    private JTextField arrTimeMax = new JTextField("2:00pm");
     
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
    private JLabel depTimeMinlb = new JLabel("Dept. Time Min ");
    private JLabel depTimeMaxlb = new JLabel("Dept. Time Max   ");
    private JLabel arrTimeMinlb = new JLabel("Arr. Time  Min  ");
    private JLabel arrTimeMaxlb = new JLabel("Arr. Time  Max  ");
    private JLabel statuslb = new JLabel("");
    
    //panel1 holds dept/arr airport and dept/return date controls 
    private JPanel panel1 = new JPanel(new GridBagLayout());
	private JPanel panel3 = new JPanel(new GridBagLayout());
    private JPanel panelRb = new JPanel(new GridBagLayout());
	private JPanel panel4 = new JPanel(new GridBagLayout());
	
    private boolean roundTrip = true;
    Trip trip = new Trip();
    private ArrayList<String> airportNames;
    private ArrayList<String> airportCodes;
   
    JComboBox depList ;
	JComboBox arrList ;
    JList searchResultsDep;
    JList searchResultsRet;
    JList flightDepDetails;
    JList flightRetDetails;
    
    DefaultListModel<String> flightSearchResultModel;//model to hold search result information
    DefaultListModel<String> flightDetailModel; //model to hold flight detail information 
    
    ArrayList<Flight> depFlights;
    ArrayList<Airport> airports;
    ArrayList<ArrayList<Flight>> flightListDep = new ArrayList<ArrayList<Flight>>();
    ArrayList<ArrayList<Flight>> flightListRet = new ArrayList<ArrayList<Flight>>();
	    
    JScrollPane searchResultsDepScrollPane;
    JScrollPane searchResultsRetScrollPane;
    JScrollPane detailsDepScrollPane;
    JScrollPane detailsRetScrollPane;
    
    boolean firstClass;
    boolean sortByPrice;
    boolean sortByTime;
    
	int numberOfStops;
	ArrayList<Flight> reserveDepFlightList = new ArrayList<Flight>();
	ArrayList<Flight> reserveRetFlightList = new ArrayList<Flight>();
	flights.FlightsContainer flightContrainer = new flights.FlightsContainer();
	reservation.Reservation reserve = new reservation.Reservation();	
	private AirportContainer airport_container;
	
	public GUI(){
		
		
		super("User Interface");
		
		firstClass = false;
		sortByPrice = false;
		sortByTime = true;
		
		numberOfStops = 1;
		panel1.setSize(400, 400);
		panelRb.setSize(400, 400);
		
		airportNames = new  ArrayList<String>();
		airportCodes = new ArrayList<String>();
		
		airports = new ArrayList<Airport>();
		airport_container = new AirportContainer();
		airport_container.parseAirportsFromSever();
		airports = airport_container.getContainer();
		depFlights = new ArrayList<Flight>();
		
		depList = new JComboBox();
		arrList = new JComboBox();
		searchResultsDep = new JList();
		searchResultsRet = new JList();
		flightDepDetails = new JList();
		flightRetDetails = new JList();
		searchResultsDep.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		flightSearchResultModel = new DefaultListModel<String>();
		searchResultsDep.setModel(flightSearchResultModel);
		searchResultsRet.setModel(flightSearchResultModel);
		
		flightDepDetails.setModel(flightSearchResultModel);
		flightRetDetails.setModel(flightSearchResultModel);
		
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
    
/////////Panel1 hold user input fields and labels 
    
    	c.anchor=GridBagConstraints.WEST;//left align components after this point
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
     
        c.gridx = 0; 
        c.gridy = 26;
        panel1.add(statuslb,c);
       
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
	 * populates GUI searchResultsDep or searchResultsRet object with summary of each trip
	 * that's returned from search results
	 * @param flightList list of flights to populate departureList or returnList with
	 * @param populateReturnList true if returnList is to be populated,
	 * false if departureList is to be populated
	 */
	private void populateSeachResultsList(ArrayList<ArrayList<Flight>> flightList, boolean populateReturnList)
	{
		int stopNumber=0;
		String builtFlightString="";
		flightSearchResultModel = new DefaultListModel<String>(); 
		String stopNumberS = "";
		double totalPrice = 0;
		String totalPriceS = "";
		String totalTravelTime = "";
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
			totalTravelTime =	sort.get_total_travel_time(flightList.get(i));
				builtFlightString = "Stops #: " + stopNumberS +
						" Total Price: " + totalPriceS +
						" Total travel time: " + totalTravelTime +
						"  Leave at: " + getFormated(depDateTime.getHourOfDay()) + ":" +
						getFormated(depDateTime.getMinuteOfHour()) + 
						"  Arrive at: " + getFormated(retDateTime.getHourOfDay()) + ":" +
						getFormated(retDateTime.getMinuteOfHour());
						
			for(int j = 0; j < flightList.get(i).size(); j++)
			{
			//	System.out.println(flightList.get(i).get(j).toString());
			}
			if(!populateReturnList)
			{
				flightSearchResultModel.addElement(builtFlightString);
			}else
			{
				flightSearchResultModel.addElement(builtFlightString);
			}
			builtFlightString = "";
		}
		
		//System.out.println("\n next flight");
	
		
		if(!populateReturnList)
		{
			searchResultsDep.removeAll();
			if(flightSearchResultModel.isEmpty())
			{
				flightSearchResultModel.addElement("No flights found");
			}
			searchResultsDep.setModel(flightSearchResultModel);	
			System.out.println("Number of departing flights found " 
			+ Integer.toString(flightList.size()) + "\n");
		}else
		{
			searchResultsRet.removeAll();
			if(flightSearchResultModel.isEmpty())
			{
				flightSearchResultModel.addElement("No flights found");
			}
			searchResultsRet.setModel(flightSearchResultModel);
			
			System.out.println("Number of return flirghts found " 
					+ Integer.toString(flightList.size()) + "\n");
		}
		
	}
	
	/**
	 * 
	 * converts integer to string with 0 added at the front if integer is less than 10
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
 * populates either flightDepDetails or flightRetDetails gui list objects with 
 * details about the singleTrip that was selected by the user in 
 * searchResultsDep or searchResultsRet lists.
 * @param singleTrip list of flights representing the trip about which
 * details are to be displayed
 * @param populateReturnDetailsList true if populating details about return flight
 * falls if populating details about departure flights
 */
	private void populateDetailsList(ArrayList<Flight>  singleTrip, 
			boolean populateReturnDetailsList)
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
		
		for(int j = 0; j < singleTrip.size(); j++)
		{
			 Depart = "Depart: " + singleTrip.get(j).get_dep_code();
			 flightDetailModel.addElement(Depart);
			 TimeDep = "at: " + getFormated(singleTrip.get(j).get_dep_time_local().getHourOfDay())
					 +":" + getFormated(singleTrip.get(j).get_dep_time_local().getMinuteOfHour());
			 flightDetailModel.addElement(TimeDep);
			 DateDep = "on: " + getFormated(singleTrip.get(j).get_dep_time_local().getMonthOfYear())
					 + "/" + getFormated(singleTrip.get(j).get_dep_time_local().getDayOfMonth()) 
					 +  "/" + singleTrip.get(j).get_dep_time_local().getYear(); 		 
			 flightDetailModel.addElement(DateDep);
			 Arive = "Arrive: " + singleTrip.get(j).get_arr_code();
			 flightDetailModel.addElement(Arive);
			 TimeArr = "at: " + getFormated(singleTrip.get(j).get_arr_time_local().getHourOfDay())
					 + ":" + getFormated(singleTrip.get(j).get_arr_time_local().getMinuteOfHour());
			 flightDetailModel.addElement(TimeArr);
			 DateArr = "on: " + getFormated(singleTrip.get(j).get_arr_time_local().getMonthOfYear())
				 + "/" + getFormated(singleTrip.get(j).get_arr_time_local().getDayOfMonth()) 
				 +  "/" + singleTrip.get(j).get_arr_time_local().getYear(); 		 
				 flightDetailModel.addElement(DateDep);
		
			 PlaneType = "Plane type: " + singleTrip.get(j).get_flight_model();
			 flightDetailModel.addElement(PlaneType);
			 FlightTime = "Flight time: " + singleTrip.get(j).get_flight_time_hour_min();
			 flightDetailModel.addElement(FlightTime);
			 FlightNumber = "Flight number: " + singleTrip.get(j).get_flight_number();
			 flightDetailModel.addElement(FlightNumber);
			 flightDetailModel.addElement("      ");
			 if(!populateReturnDetailsList)
			 {  
				 DepFlightTrip.add(singleTrip.get(j));
			 }else
			 {
				 RetFlightTrip.add(singleTrip.get(j));		 
			 }
		}
		
		if(!populateReturnDetailsList)
		{
			flightDetailModel.addElement("Total price: $" + Double.toString(Math.round(sort.get_total_price(DepFlightTrip, firstClass)*100D)/100D));
			flightDetailModel.addElement("Total travel time: " + sort.get_total_travel_time(DepFlightTrip));
			
			System.out.println("Selection Made: " + searchResultsDep.getSelectedValue());
			flightDepDetails.setModel(flightDetailModel);
		}else
		{
			flightDetailModel.addElement("Total price: $" + Double.toString(Math.round(sort.get_total_price(RetFlightTrip, firstClass)*100D)/100D));
			flightDetailModel.addElement("Total travel time: " + sort.get_total_travel_time(RetFlightTrip));
			
			System.out.println("Selection Made: " + searchResultsRet.getSelectedValue());
			flightRetDetails.setModel(flightDetailModel);
		}
		
	}
	
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
	private String getReserveListAsString(ArrayList<String> reserveList)
	{
		String reserveListString="";
		
		for(int i = 0; i<reserveList.size();i++)
		{
			reserveListString=reserveListString + "flight #: " + reserveList.get(i)+"\n";
		}
		
		return reserveListString;
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
		reserveButton.setEnabled(true);
		clearSearchButton.setEnabled(false);
		
	}
	/**
	 * clears all search result list objects in the GUI
	 */
	private void clearSearchResults()
	{
		searchResultsDep.setModel(new  DefaultListModel<String>());
		searchResultsRet.setModel(new  DefaultListModel<String>());
		flightDepDetails.setModel(new  DefaultListModel<String>());
		flightRetDetails.setModel(new  DefaultListModel<String>());
	}
	/**
	 * receives a date in form of String[3] checks that the date,month,and year are valid
	 * @param checkDate String[3] where String[0] is month String[1] is date String[2] is year
	 * @return true if dates are valid false if not
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

		}else if(month > 12 || month < 1 )
		{
			JOptionPane.showMessageDialog(null,"Month must be between 1 and 12 inclusive",
					"Error",JOptionPane.WARNING_MESSAGE);
			return false;

		}else if((date > 31 || date < 1) && SearchByDepDate.isSelected())
		{
			JOptionPane.showMessageDialog(null,"Date must be between 1 and 31 inclusive",
					"Error",JOptionPane.WARNING_MESSAGE);
			return false;

		}else if((date > 31 || date < 1) && !SearchByDepDate.isSelected())
		{
			JOptionPane.showMessageDialog(null,"Date must be between 1 and 31 inclusive",
					"Error",JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
	/**
	 * handler for list selection change
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
	         			if(searchResultsDep.getSelectedValue().toString() == "No flights found")
		         		{
		         			return;
		         		}
		         		
	                 
		            		String selected = source.getSelectedValue().toString();
		                    flightDetailModel = new DefaultListModel<String>();
		    				
		                    int selectionIndex = searchResultsDep.getSelectedIndex();
		                	
		                    populateDetailsList(flightListDep.get(selectionIndex), false);
		                    
	                 }
         		}else if(event.getSource() == searchResultsRet)
         		{
         			JList source = (JList)event.getSource();
            		if(!searchResultsRet.isSelectionEmpty())
                    {
            			if(searchResultsRet.getSelectedValue().toString() == "No flights found")
		         		{
		         			return;
		         		}
		         		
            			if(searchResultsRet.getSelectedValue().toString() == "No flights found")
    	         		{
    	         			return;
    	         		}
    	         		
	            		String selected = source.getSelectedValue().toString();
	                    flightDetailModel = new DefaultListModel<String>();
	    				
	                    
	                    int selectionIndex = searchResultsRet.getSelectedIndex();
	                	
	                    populateDetailsList(flightListRet.get(selectionIndex), true);
	                    
                    }
         		}
             }
         }
     }
	/**
	 *  handles all GUI events except for list selection which is handled by 
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
				flightSearchResultModel = new DefaultListModel<String>();
				searchResultsDep.setModel(flightSearchResultModel);
				searchResultsRet.setModel(flightSearchResultModel);
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
				if(Integer.parseInt(parseRetDate[1]) < Integer.parseInt(parseDepDate[1]))
				{
					JOptionPane.showMessageDialog(null,"Departure date cannot be after return date",
															"Error",JOptionPane.WARNING_MESSAGE);
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
				statuslb.setText("Searching...");
				
				System.out.println("Searching for flights from... \n" + departureAirport
						+ " to " +  destinationAirport);
			
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
						flightSearchResultModel.removeAllElements();
						flightSearchResultModel.addElement("No flights found");
						searchResultsDep.setModel(flightSearchResultModel);
						
						flightSearchResultModel.removeAllElements();
						flightSearchResultModel.addElement("No flights found");
						searchResultsRet.setModel(flightSearchResultModel);
						
						
					}
				}
				
				statuslb.setText("Done Searching");
				

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
				Object[] options = {"Confirm", "Cancel"};
				if(firstClass)
				{
					if(!searchResultsDep.isSelectionEmpty())
					{
						if(searchResultsDep.getSelectedValue().toString() == "No flights found")
		         		{
							JOptionPane.showMessageDialog(null,"No flights to reserve \n",
									"Error",JOptionPane.WARNING_MESSAGE);
							return;
		         		}
		         		
						if(roundTrip && searchResultsRet.isSelectionEmpty())
						{
							JOptionPane.showMessageDialog(null,"Returning trip must be selcted \n",
									"Error",JOptionPane.WARNING_MESSAGE);
							return;
							
						}
						
			 				if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, 
			 						"Would you like to confirm booking of the following flights? \n" + 
			 				getReserveFlightList(roundTrip), "Confirm",JOptionPane.YES_NO_OPTION))
			 				{
			 					if(reserve.reserveFirstClass(getReserveFlightList(roundTrip)))
			 					{
			 						JOptionPane.showMessageDialog(null,"The following flights have been reserved \n" 
									+ getReserveListAsString(getReserveFlightList(roundTrip)),
									"Reserved",JOptionPane.INFORMATION_MESSAGE);
			 						reserveButton.setEnabled(false);
			 						
			 					}else
			 					{
			 						JOptionPane.showMessageDialog(null,"Flights failed to book please try again \n", 
											"Filed to reserve!",JOptionPane.WARNING_MESSAGE);
					 						reserveButton.setEnabled(true);
					 						return;
			 					}
								
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
						if(roundTrip && searchResultsRet.isSelectionEmpty())
						{
							JOptionPane.showMessageDialog(null,"Returning trip must be selcted \n",
									"Error",JOptionPane.WARNING_MESSAGE);
							return;
						}
						if(searchResultsDep.getSelectedValue().toString() == "No flights found")
		         		{
							JOptionPane.showMessageDialog(null,"No flights to reserve \n",
									"Error",JOptionPane.WARNING_MESSAGE);
							return;
		         		}
						if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, 
		 						"Would you like to confirm booking of the following flights? \n" + 
		 				getReserveFlightList(roundTrip), "Confirm",JOptionPane.YES_NO_OPTION))
		 				{
		 					if(reserve.reserveCoach(getReserveFlightList(roundTrip)))
		 					{
		 						JOptionPane.showMessageDialog(null,"The following flights have been reserved \n" 
								+ getReserveListAsString(getReserveFlightList(roundTrip)),
								"Reserved",JOptionPane.INFORMATION_MESSAGE);
		 						reserveButton.setEnabled(false);
		 						
		 					}else
		 					{
		 						JOptionPane.showMessageDialog(null,"Flights failed to book please try again \n", 
										"Failed to reserve!",JOptionPane.WARNING_MESSAGE);
				 						return;
		 					}
							
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
				depDateTFlb.setText("Departure Date");
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
