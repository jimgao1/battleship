/*
 * 	[BattleshipMainMenu.java]
 * 
 * 	Author: Philip Huang, Jim Gao, Joseph Zhang
 * 	Purpose: The launcher of the various modes, also the 
 * 			entry point of the program
 */


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;

public class BattleshipMainMenu extends JFrame {

	private static final long serialVersionUID = 2643227752180400125L;
	
	public static final String[] easterEggMessages = {
		"This feature is not implemented",
		"Seriously, it is not implemented",
		"Dude! No!!!",
		"There is nothing here, give up already.",
		"STOP PRESSING ME!!!!",
		"That's it. "
	};
	
	/*
	 * 	Secret counter for the easter egg, where when the user press on the 
	 * 	not implemented feature 5 times, there will be a surprise
	 */
	public int tCount = 0;

	public BattleshipMainMenu() {
		/*
		 * Remove window border
		 */
		this.setUndecorated(true);
		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

		this.setSize(400, 300);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);

		/*
		 * Centering the current window
		 */
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		this.setLocation(x, y);

		/*
		 * 	Using the third party GUI look and feel, looks much better than the
		 * 	Java metal look and feel
		 */
		try {
			UIManager.setLookAndFeel(new PlasticLookAndFeel());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		/*
		 * 	Constructing title bar
		 */
		JLabel titleLabel = new JLabel("Battleship");
		titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 55));
		
		/*
		 * 	Removes the window border
		 */
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setVerticalAlignment(SwingConstants.CENTER);

		this.add(titleLabel, BorderLayout.NORTH);

		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(5, 1));

		/*
		 * 	The buttons on the main panel
		 */
		JButton singlePlayer = new JButton("Single Player");
		JButton localMultiplayer = new JButton("Local Multiplayer");
		JButton lanMultiplayer = new JButton("LAN Multiplayer");
		JButton about = new JButton("About");
		JButton exit = new JButton("Exit");
		
		/*
		 * set font for the buttons
		 */
		singlePlayer.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		localMultiplayer.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		lanMultiplayer.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		about.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		exit.setFont(new Font("Times New Roman", Font.PLAIN, 20));

		/*
		 * 	ActionListeners
		 */
		
		/*
		 * Single Player, where a player is playing the AI.
		 */
		singlePlayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new BattleshipSP();
			}

		});

		/*
		 * 	This feature is not implemented, will implement in 
		 * 	next version or so. 
		 */
		localMultiplayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, easterEggMessages[tCount]);
				/*
				 * 	Uses the tCount at the beginning of the code, pops up
				 * 	easter egg when pressed 5 times
				 */
				if (tCount++ == 5){
					new EasterEgg();
				}
			}

		});

		/*
		 * 	Allows 2 people to play on a LAN
		 */
		lanMultiplayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * 	Letting the user choose whether they want to host a server, 
				 * 	or join someone else's game
				 */
				
				String[] modes = {"Client Mode", "Server Mode"};
				
				int option = JOptionPane.showOptionDialog(null,
						"client or server?", "mode", JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, modes, modes[0]);
				
				if (option == 1){
					/*
					 * 	Refer to the constructor of BattleshipMP
					 */
					
					new BattleshipMP(8000);
					
				} else {
					
					/*
					 *	Asking for the IP of the server from the user
					 */
					String serverIP = JOptionPane.showInputDialog(null, "Please enter the server IP");
					
					new BattleshipMP(serverIP, 8000, false);
				}
				
				BattleshipMainMenu.this.setVisible(false);
			}

		});
		
		/*
		 * action listener for about button
		 */
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String aboutString = "<html><h2>Battleship</h2><b><br>By: Philip Huang, Jim Gao, Joseph Zhang</b></html>";
				JOptionPane.showMessageDialog(null, aboutString);
			}

		});
		
		/*
		 * action listener for exit button
		 */
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}

		});
		
		// add buttons to the control Panel
		controlPanel.add(singlePlayer);
		controlPanel.add(localMultiplayer);
		controlPanel.add(lanMultiplayer);
		controlPanel.add(about);
		controlPanel.add(exit);
		
		//set layout for main menu
		this.add(controlPanel, BorderLayout.CENTER);

		this.repaint();
		this.revalidate();

	}
	
	public static void main(String[] args) {
		new BattleshipMainMenu();
	}

}
