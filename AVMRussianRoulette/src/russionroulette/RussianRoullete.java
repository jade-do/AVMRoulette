package russionroulette;
import java.io.*;
import java.util.Scanner;
import java.util.Random;

import javax.*;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.*;

/* 
 *  PROGRAM INSTRUCTIONS
 *  
 *  a/ When the "Spin Wheel Button" frame appears, 
 *     you have to resize it for the "Let's Spin" button to appear.
 *     
 *     Whenever you start a new round, 
 *     you have to resize the "Spin WHeel Button Frame"
 *     for the "Let's Spin" button to appear.
 *     
 *  b/ When the Spin Results are displayed, click "OK" to close the "Results" dialog
 *     do not close the "Spin Wheel Button" frame,
 *     continue playing your next round.
 *     
 *     Only close the "Spin Wheel Button" frame when you are finished with the program.
 */

public class RussianRoullete {
	
	public static Scanner scan = new Scanner(System.in);
	static JFrame frame;
	public static int spinNum;
	public static double betAdded;
	public static double bettingAmount;
	public static BankRoll bankroll;
	public static boolean win;
	
	public static void main(String[] args) {
		char choice;
		boolean win = true;
		
		bankroll = new BankRoll();
		
		/* Prompt the user for an initial amount of money / the user's bank roll */
		System.out.println("Welcome to Roulette! Enter your bankroll: ");
		bankroll.update(scan.nextDouble());
		System.out.println("You currently have USD " + bankroll.retrieve() + " in your account!");
		
		loop: do {		
			// Prompt user to make the 4 different bets or Quit the program
			System.out.println("Select the bet you want to make: \n"
					+ "(A) Red/Black\t1 to 1\n"
					+ "(B) Single Number\t35 to 1\n"
					+ "(C) 1st 12, 2nd 12, 3rd 12: (1-12)/(13-24)/(25-36)\t2 to 1\n"
					+ "(D) Low (1-18) / High (19-36)\t1 to 1\n" 
					+ "(Q) Quit");
			choice = scan.next().toLowerCase().charAt(0);
				
			// Implement the bets 
			switch(choice) {
			case 'a':
				promptBet();
				redBlack();
				break;
			case 'b':
				promptBet();
				singleNumber();
				break;
			case 'c':
				promptBet();
				sectionTwelve();
				break;
			case 'd':
				promptBet();
				lowHigh();
				break;
			case 'q':	
				System.out.println("Thanks for playing Roulette! Exiting now");
				break loop;
			default:
				System.out.println("Please make a valid choice");
				continue loop;	
			}
			
			if (bankroll.retrieve() <= 0) {
				System.out.println("You've run out of money. Exiting.");
				break loop;
			}
			
		} while (choice != 'q');
	}
	
	public static void promptBet() {
		
		System.out.println("Enter your betting amount: ");
		bettingAmount = scan.nextDouble();
		while(bettingAmount > bankroll.retrieve()) {
			System.out.println("Your betting amount cannot exceed your bankroll balance: USD" + bankroll.retrieve()
				+ "\n Please enter your new betting amount: ");
			bettingAmount = scan.nextDouble();
		}
		bankroll.deduct(bettingAmount);	
		
		return;
		
	}
	
	public static void redBlack() {
		char choiceChar;
		int choiceInt = 0;
		
		do {
			System.out.println("Enter (R)ed or (B)lack: ");
			choiceChar = scan.next().toLowerCase().charAt(0);
		} while (choiceChar != 'r' && choiceChar != 'b');
		
		// Odds are black, evens are red
		// 00 and 0 are neither black nor red
		if (choiceChar == 'b') {
			choiceInt = 1;
		}

		spinNum = spinWheel();
		if (spinNum == 0 || spinNum == -1) {
			win = false;
		} else if((spinNum % 2)==(choiceInt % 2)) {
			win = true;
		} else {
			win = false;
		}
		
		if (win) {
			betAdded = bettingAmount;
			bankroll.add(bettingAmount+betAdded);
		} else {
			betAdded = 0;
		}
		
		buttonSimulation();
		
		return;
	}
	
	public static void singleNumber() {
		int choiceInt;
		
		do {
			System.out.println("Enter the number you want to bet on (Between -1 and 36; -1 for 00) ");		
			choiceInt = scan.nextInt();
		} while (choiceInt < -1  || choiceInt > 36);
		
		spinNum = spinWheel();
		
		if (spinNum == choiceInt) {
			win = true;
			betAdded = bettingAmount*35;
			bankroll.add(bettingAmount + betAdded);
		} else {
			win = false;
			betAdded = 0;
		}
		
		buttonSimulation();
		return;
	}
	
	public static void sectionTwelve() {
		int choiceInt;
		
		win = false;
		do {
			System.out.println("Enter the section number you want to bet on:\n" 
					+"(1) 1st 12 (1-12)\n"
					+"(2) 2nd 12 (13-24)\n"
					+"(3) 3rd 12 (25-36)");
			choiceInt = scan.nextInt();			
		} while (choiceInt != 1 && choiceInt != 2 && choiceInt != 3);
		
		spinNum = spinWheel();

		switch(choiceInt) {
		case 1:
			if (spinNum >= 1 && spinNum <= 12) {
				win = true;
			} 
			break;
		case 2:
			if (spinNum >= 13 && spinNum <= 24) {
				win = true;
			}			
			break;
		case 3:
			if (spinNum >= 25 && spinNum <= 36) {
				win = true;
			}				
			break;
		default:
			break;
		}
		
		if (win) {
			betAdded = bettingAmount*2;
			bankroll.add(bettingAmount + betAdded);
		}
		
		buttonSimulation();
		
		return;
	}
	
	public static void lowHigh() {
		char choiceChar;
		win = false;
		
		do {
			System.out.println("Enter the section you want to bet on:\n" 
					+"(L) Low (1-18)\n"
					+"(H) High (19-36)");
			choiceChar = scan.next().toLowerCase().charAt(0);
		} while (choiceChar != 'l' && choiceChar != 'h');
		spinNum = spinWheel();
		
		switch(choiceChar) {
		case 'l':
			if (spinNum >= 1 && spinNum <= 18) {
				win = true;
			}
			break;
		case 'h':
			if (spinNum >= 19 && spinNum <= 35) {
				win = true;
			}
			break;
		default:
			break;
		}
		
		if(win) {
			betAdded = bettingAmount;
			bankroll.add(bettingAmount + betAdded);
		}
		
		buttonSimulation();

		return;
	}
	
	public static int spinWheel() {
		Random rand = new Random();
		int number;
				
		number = rand.nextInt(38); // Return a random number between 0 and 37
		number -= 1; // Number is between -1 and 36

		return number;
	}
	
	public static void buttonSimulation() {
		frame = new JFrame("Spin Wheel Button");
		frame.setSize(600, 600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.add(panel);
				
		JButton button = new JButton("Let's Spin!");
		button.setSize(150,150);
		button.setVisible(true);	
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane optionPane = new JOptionPane();
				JDialog dialog = optionPane.createDialog("Results");
				JLabel text1;
				if (win) {
					text1 = new JLabel("The spinning wheel stopped at: " + spinNum
										+ " You've won extra: " + betAdded, JLabel.CENTER);
				} else {
					text1 = new JLabel("The spinning wheel stopped at: " + spinNum
							+ " You've lost your bet of USD : " + bettingAmount, JLabel.CENTER);
				}
				JLabel text2 = new JLabel("Your current bank banlance is: USD " + bankroll.retrieve(), JLabel.CENTER);
				
				dialog.add(text1, BorderLayout.CENTER);
				dialog.add(text2, BorderLayout.SOUTH);
				
				dialog.setSize(550, 550);
				dialog.setLocationRelativeTo(frame);
				dialog.setVisible(true);
			}
		});
		panel.add(button);
	}

}
