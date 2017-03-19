package gui;

import java.util.Scanner;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;


import airport.AirportContainer;
import flights.DepartingFlightsContainer;
import flights.ArrivingFlightsContainer;
import flights.Flight;
import airplane.AirplaneContainer;
import airplane.Airplane;
import java.util.ArrayList;
import airport.Airport;

public class Main {

	public static void main(String [] args) {
		/*Scanner reader = new Scanner(System.in);  // Reading from System.in
		System.out.println("Enter destination airport: ");
		String dest = reader.nextLine(); // Scans the next token of the input as an int.
		System.out.println(dest+"test");
		System.out.println("Enter destination airport: ");
		int n = reader.nextInt(); // Scans the next token of the input as an int.
		
		System.out.println(n);
		
		
		
		String fn = JOptionPane.showInputDialog("Enter First Number");
		String sn = JOptionPane.showInputDialog("Enter Second Number");
		
		int num1 = Integer.parseInt(fn);
		int num2 = Integer.parseInt(sn);
		
		
		int sum = num1 + num2;
		JOptionPane.showMessageDialog(null, "The sum is" + sum, "Sum", JOptionPane.PLAIN_MESSAGE);*/
		
		
		GUI Gui = new GUI();
		Gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Gui.setVisible(true);
	
	
		 
	}
	
	}
