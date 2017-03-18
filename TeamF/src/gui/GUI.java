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

public class GUI extends JFrame {
	
    private JTextField desField = new JTextField("BOS");
    private JTextField depField = new JTextField("SFO");
    private JTextField depDate = new JTextField("12/20/2017");
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
	
    private JPanel panelRb = new JPanel(new GridBagLayout());
	
    private String tripType = "roundtrip";
    private String numStops = "1";
	
    public String[] userInput = new String[10];
    
	public GUI(){
		
		super("User Interface");
		
		panel2.setSize(400, 400);
		panel1.setSize(400, 400);
		panelRb.setSize(400, 400);
		this.setSize(300,200);

      
     
        GridBagConstraints c = new GridBagConstraints();
///////////Radio button panel
    
   

    c.gridx = 0; 
    c.gridy = 5;
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
        panel1.add(desField,c);
        
        c.gridy = 15;
        panel1.add(depField,c);

        c.gridy = 20;
        panel1.add(depDate,c);

        c.gridy = 25;
        panel1.add(arrDate,c);


        
        thehandler handler = new thehandler();
        this.getContentPane().add(panel1, BorderLayout.WEST);
        this.getContentPane().add(panel2, BorderLayout.EAST);
        this.getContentPane().add(panelRb, BorderLayout.NORTH);
   
        searchButton = new JButton("Search");
        OneWayRb.addActionListener(handler);
        RndTripRb.addActionListener(handler);
        TwoStopRb.addActionListener(handler);
        OneStopRb.addActionListener(handler);
        TwoStopRb.addActionListener(handler);
        NoStopRb.addActionListener(handler);
        
        searchButton.addActionListener(handler);
        
        this.add(searchButton, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      
        this.setSize(500, 350);
		

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
				departure = depField.getText();
				destination = desField.getText();
				System.out.println("Searching... \n");
				userInput = getUserInput();
				for(int i=0; i<10;i++)
				{
					System.out.println(userInput[i]);
					
				}
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
