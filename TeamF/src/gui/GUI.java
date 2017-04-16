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


import Trip.Trip;

public class GUI extends JFrame {
	
	
	public UserInput userInput = new UserInput();
	//GUI object initialisation 
    private JTextField desField = new JTextField("BOS");
    private JTextField depField = new JTextField("SFO");
    private JTextField depDate = new JTextField("05/15/2017");
    private JTextField arrDate = new JTextField("12/27/2017");
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
    
    
    private JButton searchButton = new JButton("Search");
    private JButton sortButton =  new JButton("Sort");
    private JButton detailsButton = new JButton("Details");;
    
    
    
    private JLabel deplb = new JLabel("Departure Airport    ");
    private JLabel destlb = new JLabel("Destination Airport    ");
    private JLabel depDatelb = new JLabel("Departure Date    ");
    private JLabel arrDatelb = new JLabel("Return Date    ");
    private JLabel depTimeMinlb = new JLabel("Dept. Time Min ");
    private JLabel depTimeMaxlb = new JLabel("Dept. Time Max   ");
    private JLabel arrTimeMinlb = new JLabel("Arr. Time  Min  ");
    private JLabel arrTimeMaxlb = new JLabel("Arr. Time  Max  ");
	
    
    //panel1 holds dept/arr airport and dept/return date controls 
    private JPanel panel1 = new JPanel(new GridBagLayout());
	
    private JPanel panel2 = new JPanel(new GridBagLayout());
	
    private JPanel panel3 = new JPanel(new GridBagLayout());
    
	private JPanel panelRb = new JPanel(new GridBagLayout());
	
	private JPanel panel4 = new JPanel(new GridBagLayout());
	
    private String tripType = "roundtrip";
    private String numStops = "1";
	
    
    Controller controller = new Controller();
    Trip trip = new Trip();
   // private String[] userInput;
   
    private ArrayList<String> airportNames;
    private ArrayList<String> airportCodes;
   
    JComboBox depList ;
	JComboBox arrList ;
    JList searchResultsDep;
    JList searchResultsRet;
    JList flightDepDetails;
    JList flightRetDetails;
    
    DefaultListModel<String> model;
    DefaultListModel<String> flightDepDetailModel;
    DefaultListModel<String> flightRetDetailModel;
    
    ArrayList<Flight> depFlights;
    ArrayList<Airport> airports;
    ArrayList<ArrayList<Flight>> flightList = new ArrayList<ArrayList<Flight>>();
	
    
    JScrollPane searchResultsDepScrollPane;
    JScrollPane searchResultsRetScrollPane;
    
    JScrollPane detailsRetScrollPane;
    JScrollPane detailsDepScrollPane;
    
    Boolean firstClass;
	int numberOfStops;
	public GUI(){
		
		super("User Interface");
		
	
		firstClass = false;
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
		model = new DefaultListModel<String>();
		searchResultsDep.setModel(model);
		searchResultsRet.setModel(model);
		
		flightDepDetails.setModel(model);
		flightRetDetails.setModel(model);
		
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
        c.gridx = 0; 
        c.gridy = 10;
        panel2.add(depTimeMinlb,c);

        c.gridx = 0; 
        c.gridy = 15;
        panel2.add(depTimeMaxlb,c);
     
        c.gridx = 0; 
        c.gridy = 20;
        panel2.add(arrTimeMinlb,c);
     
        c.gridx = 0; 
        c.gridy = 25;
        panel2.add(arrTimeMaxlb,c);
     
        
        c.gridx = 20;
        c.gridy = 10;
        panel2.add(depTimeMin,c);
        
        c.gridy = 15;//change the y location
        panel2.add(depTimeMax,c);

        c.gridy = 20;//change the y location
        panel2.add(arrTimeMin,c);

        c.gridy = 25;//change the y location
        panel2.add(arrTimeMax,c);
/////////Panel1
        c.gridx = 0; 
        c.gridy = 10;
        panel1.add(deplb,c);

        c.gridx = 0; 
        c.gridy = 15;
        panel1.add(destlb,c);
     
        c.gridx = 0; 
        c.gridy = 20;
        panel1.add(depDatelb,c);
     
        c.gridx = 0; 
        c.gridy = 25;
        panel1.add(arrDatelb,c);
     
        
        c.gridx = 20;
        c.gridy = 10;
        panel1.add(depList,c);

        c.gridy = 15;
        panel1.add(arrList,c);
        

        c.gridy = 20;
        panel1.add(depDate,c);

        c.gridy = 25;
        panel1.add(arrDate,c);

        /////////////////PANEL 3
        c.gridx = 0;
        c.gridy = 0;
        panel3.add(searchButton, c);
       // c.gridx = 120;
      //  c.gridy = 10;
      //  panel3.add(detailsButton, c);
        c.gridx = 120;
        c.gridy = 20;
        panel3.add(flightDepDetails, c);
        
        c.gridy = 25;
        panel3.add(flightRetDetails, c);
        
        
        
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
        
        searchResultsDep.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
               
            	
            	if (!event.getValueIsAdjusting()){
             
            		JList source = (JList)event.getSource();
            		if(!searchResultsDep.isSelectionEmpty())
                    {
                    
            		String selected = source.getSelectedValue().toString();
                    flightDepDetailModel = new DefaultListModel<String>();
    				String buitFlightString  = "";
                    
                    int selectionIndex = searchResultsDep.getSelectedIndex();
                	
    					
    					for(int j = 0; j < flightList.get(selectionIndex).size(); j++)
    					{
    						buitFlightString = buitFlightString + "  Depart: " + flightList.get(selectionIndex).get(j).get_dep_code() +
    								"  at " + flightList.get(selectionIndex).get(j).get_dep_time() +
    								"  Arrive: " + flightList.get(selectionIndex).get(j).get_arr_code() + 
    								"  at " + flightList.get(selectionIndex).get(j).get_arr_time();
    						flightDepDetailModel.addElement(buitFlightString);
        					buitFlightString = "";
        					
    					}
    					
    					
    					/*buitFlightString = buitFlightString + "  Dep Arpt:" + flightList.get(i).get(j).get_dep_code() +
    								"  Dep Time:" + flightList.get(i).get(j).get_dep_time() +
    								"  Arr Arpt:" + flightList.get(i).get(j).get_arr_code() + 
    								"  Arr Time:" + flightList.get(i).get(j).get_arr_time();
    					
    					*/
    			    
                    /*
                    flightDetailModel.addElement("Departing Airport:" + flightList.get(searchResultsDep.getSelectedIndex()).get(0).get_dep_code());
    				flightDetailModel.addElement("Departing Time:" + flightList.get(searchResultsDep.getSelectedIndex()).get(0).get_dep_time());
    				flightDetailModel.addElement("Arriving Airport:" + flightList.get(searchResultsDep.getSelectedIndex()).get(0).get_arr_code());
    				flightDetailModel.addElement("Arriving Time:" + flightList.get(searchResultsDep.getSelectedIndex()).get(0).get_arr_time());
    				*/
    				System.out.println("Selection Made: " + searchResultsDep.getSelectedValue());
    				flightDepDetails.setModel(flightDepDetailModel);
                    }
    	
                }
            }
        });
      
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      
        this.setSize(1150, 800);
		

	}

	private class thehandler implements ActionListener{
		
		public void actionPerformed(ActionEvent event){
		
			
			//if(event.getSource() == depField)
				//departure=String.format("field 1: %s", event.getActionCommand());
		//	else if(event.getSource() == desField)
			//	destination=String.format("field 2: %s", event.getActionCommand());
		//	else 
			if (event.getSource() == searchButton) 
			{
				searchResultsDep.clearSelection();
				flightDepDetails.clearSelection();
				model = new DefaultListModel<String>();
				
				 
			
				String[] parseDepDate;
				String[] parseDepAirport;
				String[] parseArrDate;
				String[] parseArrAirport;
				String departureAirport;
				String destinationAirport;
				String date;
				
				parseDepDate = depDate.getText().split("/");
				parseDepAirport = depList.getSelectedItem().toString().split("_");
				departureAirport = parseDepAirport[1];
				
				parseArrDate = arrDate.getText().split("/");
				parseArrAirport = arrList.getSelectedItem().toString().split("_");
				destinationAirport = parseArrAirport[1];
				date = parseDepDate[2] + "_" + parseDepDate[0] + "_"+ parseDepDate[1];
				
				System.out.println("Searching for flights from... \n" + parseDepAirport[1]	);
				
				
			
				flightList= trip.getFlightOptionsByDeparting(departureAirport,
						destinationAirport, date, Integer.toString(numberOfStops),
						firstClass);
				 
				//depFlights = controller.getDepartingFlights(parseDepAirport[1], 
				//		parseArrDate[2] + "_" + parseArrDate[0]+ "_" + parseArrDate[1]);
				
				int stopNumber =0;
				String buitFlightString="";
				if(searchResultsDep.getComponentCount() >0)
				{
				searchResultsDep.removeAll();
				}
				for(int i = 0; i<flightList.size(); i++)
				{
					
					stopNumber = flightList.get(i).size()-1;
				//	for(int j = 0; j < flightList.get(i).size(); j++)
					{
						buitFlightString = buitFlightString + "  Depart: " + flightList.get(i).get(0).get_dep_code() +
								"  at " + flightList.get(i).get(0).get_dep_time() +
								"  Arrive: " + flightList.get(i).get(stopNumber).get_arr_code() + 
								"  at " + flightList.get(i).get(stopNumber).get_arr_time() + " with " + numberOfStops + " stops";
					for(int j = 0; j < flightList.get(i).size(); j++)
					{
						System.out.println(flightList.get(i).get(j).toString());
					}
					}
					model.addElement(buitFlightString);
					buitFlightString = "";
					System.out.println("\n next flight");
										/*buitFlightString = buitFlightString + "  Dep Arpt:" + flightList.get(i).get(j).get_dep_code() +
								"  Dep Time:" + flightList.get(i).get(j).get_dep_time() +
								"  Arr Arpt:" + flightList.get(i).get(j).get_arr_code() + 
								"  Arr Time:" + flightList.get(i).get(j).get_arr_time();
					
					*/
				}
				System.out.println("Number of flights found: " + flightList.size());
				
				searchResultsDep.setModel(model);
				
				//	testFlight = depFlights.get(1);   
				
			//	model.addElement(depFlights.get(0).toString());
			//	searchResultsDep.setModel(model);
				
			}else if(event.getSource() == OneWayRb)
			{
				OneWayRb.setSelected(true);
				RndTripRb.setSelected(false);
				userInput.setTripType("oneway");
				
			}else if(event.getSource() == RndTripRb)
			{
				OneWayRb.setSelected(false);
				RndTripRb.setSelected(true);
				userInput.setTripType("roundtrip");
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
			}else if(event.getSource() == SortByPriceRb)
			{
				SortByPriceRb.setSelected(true);
				SortByTravelTimeRb.setSelected(false);
				
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
			}
			
			/*else if(event.getSource() == detailsButton)
			{
				flightDetailModel = new DefaultListModel<String>();
				flightDetailModel.addElement("Departing Airport:" + depFlights.get(searchResultsDep.getSelectedIndex()).get_dep_code());
				flightDetailModel.addElement("Departing Time:" + depFlights.get(searchResultsDep.getSelectedIndex()).get_dep_time());
				flightDetailModel.addElement("Arriving Airport:" + depFlights.get(searchResultsDep.getSelectedIndex()).get_arr_code());
				flightDetailModel.addElement("Arriving Time:" + depFlights.get(searchResultsDep.getSelectedIndex()).get_arr_time());
				
				System.out.println("Selection Made: " + searchResultsDep.getSelectedValue());
				flightDetails.setModel(flightDetailModel);
			}*/
			
			
			
				
		
			
		}
			}
	


}
