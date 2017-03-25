package gui;

import java.util.Scanner;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Container;

import javax.swing.*;//JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import airport.AirportContainer;
import controller.Controller;
import flights.DepartingFlightsContainer;
import flights.ArrivingFlightsContainer;
import flights.Flight;
import airplane.AirplaneContainer;
import airplane.Airplane;
import java.util.ArrayList;
import airport.Airport;


public class GUI extends JFrame {
	
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

    private JButton searchButton;
    private JButton sortButton;
    private JRadioButton  SortByPriceRb= new JRadioButton("Price");
    private JRadioButton  SortByTravelTimeRb= new JRadioButton("Travel Time");
	
    private JLabel deplb = new JLabel("Departure Airport    ");
    private JLabel destlb = new JLabel("Destination Airport    ");
    private JLabel depDatelb = new JLabel("Departure Date    ");
    private JLabel arrDatelb = new JLabel("Return Date    ");
    private JLabel depTimeMinlb = new JLabel("Dept. Time Min ");
    private JLabel depTimeMaxlb = new JLabel("Dept. Time Max   ");
    private JLabel arrTimeMinlb = new JLabel("Arr. Time  Min  ");
    private JLabel arrTimeMaxlb = new JLabel("Arr. Time  Max  ");
	
    private JPanel panel1 = new JPanel(new GridBagLayout());
	
    private JPanel panel2 = new JPanel(new GridBagLayout());
	
    private JPanel panel3 = new JPanel(new GridBagLayout());
    
	private JPanel panelRb = new JPanel(new GridBagLayout());
	
	private JPanel panel4 = new JPanel(new GridBagLayout());
	
    private String tripType = "roundtrip";
    private String numStops = "1";
	
    
    Controller controller = new Controller();
   
    private String[] userInput;
   
    private ArrayList<String> airportNames;
    private ArrayList<String> airportCodes;
   
    JComboBox depList ;
	JComboBox arrList ;
    JList searchResults;
    DefaultListModel<String> model;
    
    ArrayList<Flight> depFlights;
    ArrayList<Airport> airports;
    
    
    JScrollPane scrollPane;
	public GUI(){
		
		super("User Interface");
		 searchButton = new JButton("Search");
	     sortButton  =  new JButton("Sort");
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
		searchResults = new JList();
		//searchResults.setPreferredSize(new Dimension(250, 80));
		searchResults.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		model = new DefaultListModel<String>();
		searchResults.setModel(model);
		
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

        c.gridx = 0;
        c.gridy = 10;
        panel3.add(searchButton, c);
        
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
        
        scrollPane = new JScrollPane(searchResults);
        panel3.add(scrollPane, c);
        
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);		
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
        
        scrollPane.setPreferredSize(new Dimension(800,250));
        
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
        SortByPriceRb.addActionListener(handler);
        SortByTravelTimeRb.addActionListener(handler);
        

      
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      
        this.setSize(950, 500);
		

	}
	
	private class thehandler implements ActionListener{
		
		public void actionPerformed(ActionEvent event){
			String departure = "";
			String destination = "";
			
			if(event.getSource() == depField)
				departure=String.format("field 1: %s", event.getActionCommand());
			else if(event.getSource() == desField)
				destination=String.format("field 2: %s", event.getActionCommand());
			else if (event.getSource() == searchButton) 
			{
				departure =  depList.getSelectedItem().toString();
				destination = arrList.getSelectedItem().toString();
				
				model = new DefaultListModel<String>();
				
				
			
				String[] parseDate;
				String[] parseDepAirport;
				
				
				parseDate = depDate.getText().split("/");
				parseDepAirport = departure.split("_");
				System.out.println("Searching for flights from... \n" + parseDepAirport[1]	);
				
				depFlights = controller.getDepartingFlights(parseDepAirport[1], 
						parseDate[2] + "_" + parseDate[0]+ "_" + parseDate[1]);
			
				searchResults.removeAll();
				for(int i = 0; i<depFlights.size(); i++)
				{
					model.addElement(depFlights.get(i).toString());
					System.out.println(depFlights.get(i).toString());
					
				}
				System.out.println("Number of flights found: " + depFlights.size());
				
				searchResults.setModel(model);
				
				//	testFlight = depFlights.get(1);   
				
			//	model.addElement(depFlights.get(0).toString());
			//	searchResults.setModel(model);
				
			}else if(event.getSource() == OneWayRb)
			{
				RndTripRb.setSelected(false);
				tripType = "oneway";
			}else if(event.getSource() == RndTripRb)
			{
				OneWayRb.setSelected(false);
				tripType = "roundtrip";
				
			}else if(event.getSource() == NoStopRb)
			{
				OneStopRb.setSelected(false);
				TwoStopRb.setSelected(false);
				numStops = "0";
				
			}else if(event.getSource() == OneStopRb)
			{
				NoStopRb.setSelected(false);
				TwoStopRb.setSelected(false);
				numStops = "1";
			}else if(event.getSource() == TwoStopRb)
			{
				OneStopRb.setSelected(false);
				NoStopRb.setSelected(false);
				numStops = "2";
			}else if(event.getSource() == SortByTravelTimeRb)
			{
				SortByPriceRb.setSelected(false);
			}else if(event.getSource() == SortByPriceRb)
			{
				SortByTravelTimeRb.setSelected(false);
			}
			
			
			
				
		
			
		}
			}
	public String[] getUserInput()
	{
		userInput[0] = desField.getText();
		userInput[1] = depField.getText();
		userInput[2] = depDate.getText();
		userInput[3] = arrDate.getText();
		userInput[4] = depTimeMin.getText();
		userInput[5] = depTimeMax.getText();
		userInput[6] = arrTimeMin.getText();
		userInput[7] = arrTimeMax.getText();
		userInput[8] = tripType;
		userInput[9] = numStops;

		
		return userInput;
		
	}

	
	
}
