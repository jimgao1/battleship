/*
 * 	[AlgorithmMain.java]
 * 
 * 	Author: Philip Huang, Jim Gao, Joseph Zhang
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;

public class AlgorithmMain extends JFrame implements ActionListener{

	/*
	 * The game rules, displayed when the game starts
	 */
	public static final String rules = "Welcome to Battleship!\r\nHere are the rules of the single player mode game:" + ""
			+ "\r\nYou should place your ships into your 10 by 10 grid; ships cannot be out of bounds.\r\nThere are 5 ships"
			+ " that should be placed, there are the Carrier (length of 5), the Battleship (length of 4), the Submarine "
			+ "(length of 3), the Cruiser (length of 3), and the Destroyer (length of 2).\r\nThen you can enter the "
			+ "coordinates of the location you want to hit in your opponent\u2019s grid, our program would tell you if it is "
			+ "a miss or a hit/sank of a ship. \r\nThe player that first sinks all five of ships of his opponent will take the "
			+ "victory.\r\n\r\nHere are the rules of the multiplayer/LAN mode:\r\nConnect the local network using client and "
			+ "server mode.\r\nYou should place your ships into your 10 by 10 grid; ships cannot be out of bounce.\r\n"
			+ "There are 5 ships that should be placed, there are the Carrier (length of 5), the Battleship (length of 4), "
			+ "the Submarine (length of 3), the Cruiser (length of 3), and the Destroyer (length of 2).\r\nThen you can "
			+ "enter the coordinates of the location you want to hit in your opponent\u2019s grid, our program would tell "
			+ "you if it is a miss or a hit/sank of a ship. \r\nThe player that first sinks all five of ships of his opponent "
			+ "will take the victory.\r\n";
	
	public static final String bannerBattleship = "  ____        _   _   _           _     _       \r\n |  _ \\      | | | | | | "
			+ "        | |   (_)      \r\n | |_) | __ _| |_| |_| | ___  ___| |__  _ _ __  \r\n |  _ < / _` | __| __| |/ _ \\/ __| \'"
			+ "_ \\| | \'_ \\ \r\n | |_) | (_| | |_| |_| |  __/\\__ \\ | | | | |_) |\r\n |____/ \\__,_|\\__|\\__|_|\\___||___/_| |_|"
			+ "_| .__/ \r\n                                         | |    \r\n                                         |_|    ";
	
	public static final String bannerTeamNull = "01010100 01100101 01100001 01101101 00100000 01001110 01010101 01001100 01001100";
	

	/*
	 * Algorithm Configuration
	 */

	public static final int gridSizeX = 10;
	public static final int gridSizeY = 10;

	public static final int shipCount = 5;
	public static final int[][] shipSize = { { 2, 1 }, { 3, 1 }, { 3, 1 },
			{ 4, 1 }, { 5, 1 } };

	public static final String[] outcomes = { "Miss", "Hit", "Sank" };

	public static final String[] shipNames = { "Destroyer", "Submarine",
			"Cruiser", "Battleship", "Carrier" };

	public static int[] shipState = new int[5];

	public static int playerHit = 0, playerSank = 0;
	public static int computerHit = 0, computerSank = 0;


	/*
	 * 0 for default 1 for hit 2 for sank
	 */

	/*
	 * Algorithm Variables
	 */

	/*
	 * int[][] gridState: the status of the cells in the grid
	 * 
	 * 0 Default 1 Hit (current round) 2 Hit (previous round) 3 Miss
	 */

	public static int[][] gridState = new int[gridSizeX][gridSizeY];
	public static double[][] gridProbability = new double[gridSizeX][gridSizeY];

	public static boolean[] shipSank = new boolean[shipCount];
	public static boolean killModeEngaged = false;
	public static int counter = 0;

	public static int[][] computerGrid = new int[10][10];
	/*
	 * GUI Elements
	 * 
	 * The GUI consists of 4 panels.
	 * 		1. The grid panel
	 * 		2. The side panel
	 * 		3. The controls panel
	 * 		4. The comments panel
	 */
	public static JButton[][] buttonGrid = new JButton[10][10]; 
	
	public static JPanel gridPanel = new JPanel();
	public static JPanel statusPanel = new JPanel();
	public static JPanel controlPanel = new JPanel();
	public static JPanel sidePanel = new JPanel();
	
	public static JLabel statusLabel = new JLabel("");
	
	public static JButton startGame = new JButton("Start Game");
	public static JButton endGame = new JButton("End Game");
	public static JButton restartGame = new JButton("Restart Game");
	
	/*
	 * 	Function that writes to a file, where the file is named
	 * 	using the current date, and it saves to the current directory. 
	 */
	public static void printToFile(){
		try{
			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
			String fileName = dateFormat.format(new Date());
			
			PrintWriter writer = new PrintWriter(new FileWriter(new File(fileName + ".txt")));
			
			writer.println(bannerBattleship);
			writer.println("\n");
			writer.println(bannerTeamNull);
			
			writer.println("Computer Grid Placement: ");
			for (int i=0; i<10; i++){
				for (int j=0; j<10; j++)
					writer.print(computerGrid[j][i] + "  ");
				writer.print("\r\n");
			}
			
			String message = "\r\nGame Statistics: ";
			
			message += "Computer made " + computerHit + " shots.\r\n";
			message += "Computer sank " + computerSank + " ships.\r\n";
			
			message += "You made " + playerHit + " shots.\r\n";
			message += "You sank " + playerSank + " ships.\r\n";
			
			writer.println(message);
			
			writer.close();
			
		} catch (IOException ex){
			
		}
	}
	
	/*
	 * Function that generates a random placement, which then was
	 * returned, and applied to the computer grid.
	 */
	public int[][] generateRandomPlacement() {
		int[][] randomGrid = new int[10][10];

		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				randomGrid[i][j] = 0;

		for (int i = 0; i < AlgorithmMain.shipCount; i++) {
			int pX = (int) (Math.random() * 10), pY = (int) (Math.random() * 10);
			boolean vert = (Math.random() >= 0.5);
			boolean valid = true;

			if (vert) {
				if (pY + AlgorithmMain.shipSize[i][0] - 1 < 10) {
					for (int j = pY; j < pY + AlgorithmMain.shipSize[i][0]; j++)
						if (randomGrid[pX][j] != 0) {
							valid = false;
							break;
						}
				} else {
					valid = false;
				}

				if (valid) {
					for (int j = pY; j < pY + AlgorithmMain.shipSize[i][0]; j++)
						randomGrid[pX][j] = i + 1;
				} else {
					i--;
					continue;
				}
			} else {
				if (pX + AlgorithmMain.shipSize[i][0] - 1 < 10) {
					for (int j = pX; j < pX + AlgorithmMain.shipSize[i][0]; j++)
						if (randomGrid[j][pY] != 0) {
							valid = false;
							break;
						}
				} else {
					valid = false;
				}

				if (valid) {
					for (int j = pX; j < pX + AlgorithmMain.shipSize[i][0]; j++)
						randomGrid[j][pY] = i + 1;
				} else {
					i--;
					continue;
				}
			}
		}

		System.out.println("[INFO]\tGenerated Opponent Placement");
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++)
				System.out.printf("%d ", randomGrid[j][i]);
			System.out.println();
		}

		return randomGrid;

	}
	
	/*
	 * 	The constructor of the current class
	 */
	public AlgorithmMain(){
		this.setLocation(100, 100);
		this.setSize(650, 500);
		this.setLayout(new BorderLayout());
		this.setTitle("Battleship Singleplayer");
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		try {
			UIManager.setLookAndFeel(new PlasticLookAndFeel());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		gridPanel.setLayout(new GridLayout(10, 10));
		gridPanel.setPreferredSize(new Dimension(400, 400));
		
		for (int i=0; i<10; i++){
			for (int j=0; j<10; j++){
				buttonGrid[i][j] = new JButton();
				buttonGrid[i][j].setActionCommand(i + " " + j);
				buttonGrid[i][j].addActionListener(this);
				buttonGrid[i][j].setEnabled(false);
				buttonGrid[i][j].setPreferredSize(new Dimension(40, 40));
				gridPanel.add(buttonGrid[i][j]);
			}
		}
		
		this.add(gridPanel, BorderLayout.CENTER);
		
		/*
		 * 	side panel construction
		 */
		
		sidePanel.setLayout(new GridLayout(2, 1));
		sidePanel.setPreferredSize(new Dimension(150, 400));
		
		controlPanel.setLayout(new GridLayout(3, 1));
		
		startGame.addActionListener(this);
		startGame.setActionCommand("start");
		
		endGame.addActionListener(this);
		endGame.setActionCommand("end");
		endGame.setEnabled(false);
		
		restartGame.addActionListener(this);
		restartGame.setActionCommand("restart");
		restartGame.setEnabled(false);
		
		controlPanel.add(startGame);
		controlPanel.add(endGame);
		controlPanel.add(restartGame);
		
		statusLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		
		statusPanel.setLayout(new GridLayout(1, 1));
		statusPanel.add(statusLabel);
		
		sidePanel.add(statusPanel);
		sidePanel.add(controlPanel);
	
		this.add(sidePanel, BorderLayout.EAST);
		
		this.repaint();
		this.revalidate();
		
		/*
		 * Generates the random grid, and then applies to
		 * the computer one. 
		 */
		computerGrid = generateRandomPlacement();
		
	}
	
	/*
	 * 	Initializes the current grid
	 */
	public static void init() {
		for (int i = 0; i < gridSizeX; i++)
			for (int j = 0; j < gridSizeY; j++) {
				gridState[i][j] = 0;
				gridProbability[i][j] = 0;
			}

		for (int i = 0; i < shipCount; i++)
			shipSank[i] = false;
	}

	public static void mark(int x, int y, int newStatus) {
		gridState[x][y] = newStatus;
		HuntAlgorithm.recalculate();
		
	}
	
	/*
	 * 	A method that judges the win/lose condition, and then
	 * 	prompts the user about the results. 
	 */
	public static void endGame(){
		boolean computerWin = false;
		boolean tie = false;
		
		if (computerSank == 5 || playerSank == 5){
			if (computerSank == 5){
				computerWin = true;
			} else {
				computerWin = false;
			}
		} else {
			if (computerHit > playerHit){
				computerWin = true;
			} else if (computerHit < playerHit){
				computerWin = false;
			} else {
				if (computerSank > playerSank){
					computerWin = true;
				} else if (computerSank < playerSank){
					computerWin = false;
				} else {
					tie = true;
				}
			}
		}
		
		String message = "<html>";
		
		if (tie){
			message += "<h1>It is a tie.</h1><br>";
		} else if (computerWin){
			message += "<h1>You lost the game.</h1><br>";
		} else {
			message += "<h1>You won the game.</h1><br>";
		}
		
		message += "Computer made " + computerHit + " shots.<br>";
		message += "Computer sank " + computerSank + " ships.<br>";
		
		message += "You made " + playerHit + " shots.<br>";
		message += "You sank " + playerSank + " ships.<br>";
		
		message += "<b>Thank you for using the program. </b>";
		
		JOptionPane.showMessageDialog(null, message);
		
		printToFile();
		
		System.exit(0);
		
	}

	/*
	 * 	The main AI algorithm. Using the probability grid, where it 
	 * 	gets the most likely locations. 
	 */
	public static void nextTurn() {
		counter++;
		
		if (counter >= 50){
			endGame();
		}
		
		if (playerSank >= 5 || computerSank >= 5){
			endGame();
		}
		
		/*
		 * Update the game status label
		 */
		
		String status = "<html><h2>Single Player</h2><br><h3>Round #" + (counter + 1) + "</h3></html>";
		statusLabel.setText(status);
		
		
		int maxLocationX = -1;
		int maxLocationY = -1;
		if (killModeEngaged) {
			System.out.println("[INFO]\t***KILL MODE ENGAGED***");

			KillAlgorithm.recalculate();

			double maxProb = -1.0D;
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					if ((gridProbability[i][j] > maxProb)
							&& (gridState[i][j] == 0)) {
						maxProb = gridProbability[i][j];
						maxLocationX = i;
						maxLocationY = j;
					}
				}
			}
			System.out.println("[INFO]\tSuggested Location ("
					+ (maxLocationX + 1) + ", " + (maxLocationY + 1) + ") ");
			JOptionPane.showMessageDialog(null, "Suggested Location ("
					+ (maxLocationX + 1) + ", " + (maxLocationY + 1) + ") ");
		} else {
			HuntAlgorithm.recalculate();

			double maxProb = -1.0D;
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					if ((gridProbability[i][j] > maxProb)
							&& (gridState[i][j] == 0)) {
						maxProb = gridProbability[i][j];
						maxLocationX = i;
						maxLocationY = j;
					}
				}
			}
			System.out.println("[INFO]\tSuggested Location ("
					+ (maxLocationX + 1) + ", " + (maxLocationY + 1) + ") ");
			JOptionPane.showMessageDialog(null, "Suggested Location ("
					+ (maxLocationX + 1) + ", " + (maxLocationY + 1) + ") ");
		}
		String s = (String) JOptionPane.showInputDialog(null,
				"What is the result? ", "", 3, null, outcomes, "");
		if (killModeEngaged) {
			if (s.equals("Sank")) {
				gridState[maxLocationX][maxLocationY] = 1;
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 10; j++) {
						if (gridState[i][j] == 1) {
							gridState[i][j] = 2;
						}
					}
				}
				
				computerSank ++;
				computerHit++;

				String ship = (String) JOptionPane.showInputDialog(null,
						"Which ship is sank?", "",
						JOptionPane.QUESTION_MESSAGE, null, shipNames, "");

				int shipID = -1;
				for (int i = 0; i < 5; i++)
					if (shipNames[i].equals(ship))
						shipID = i;

				shipState[shipID] = 2;

				killModeEngaged = false;
				for (int i = 0; i < 5; i++)
					if (shipState[i] == 1)
						killModeEngaged = true;
			} else if (s.equals("Hit")) {
				gridState[maxLocationX][maxLocationY] = 1;
				String ship = (String) JOptionPane.showInputDialog(null,
						"Which ship is hit?", "", JOptionPane.QUESTION_MESSAGE,
						null, shipNames, "");
				int shipID = -1;
				for (int i = 0; i < 5; i++)
					if (shipNames[i].equals(ship))
						shipID = i;
				
				computerHit ++;

				shipState[shipID] = 1;
				killModeEngaged = true;

			} else if (s.equals("Miss")) {
				gridState[maxLocationX][maxLocationY] = 3;
			}
		} else if (s.equals("Hit")) {
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					if (gridState[i][j] == 1) {
						gridState[i][j] = 2;
					}
				}
			}
			
			computerHit++;
			gridState[maxLocationX][maxLocationY] = 1;
			String ship = (String) JOptionPane.showInputDialog(null,
					"Which ship is hit?", "", JOptionPane.QUESTION_MESSAGE,
					null, shipNames, "");
			int shipID = -1;
			for (int i = 0; i < 5; i++)
				if (shipNames[i].equals(ship))
					shipID = i;

			shipState[shipID] = 1;
			killModeEngaged = true;
		} else {
			gridState[maxLocationX][maxLocationY] = 3;
		}
		
		/*
		 * 	Re-enable the buttons, except those have been hit, and missed
		 */
		for (int i=0; i<10; i++)
			for (int j=0; j<10; j++)
				if (buttonGrid[i][j].getBackground() == Color.BLUE ||
						buttonGrid[i][j].getBackground() == Color.RED){
					buttonGrid[i][j].setEnabled(false);
				} else {
					buttonGrid[i][j].setEnabled(true);
				}
	}


	/*
	 * Action listener for the program. 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("start")) {
			JOptionPane.showMessageDialog(null, rules);
			String s = JOptionPane.showInputDialog(null, "Please enter your name.");
			
			JOptionPane.showMessageDialog(null, "Hi, " + s + ", the game starts now.");
			
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					buttonGrid[i][j].setEnabled(true);
				}
			}
			
			endGame.setEnabled(true);
			restartGame.setEnabled(true);
			return;
		} else if (e.getActionCommand().equals("end")) {
			endGame();
			return;
		} else if (e.getActionCommand().equals("restart")) {
			new AlgorithmMain();
			this.dispose();
			return;
		}
		int gridX = Integer.parseInt(e.getActionCommand().split(" ")[0]);
		int gridY = Integer.parseInt(e.getActionCommand().split(" ")[1]);
		
		if (computerGrid[gridX][gridY] == 0){
			buttonGrid[gridX][gridY].setBackground(Color.blue);
			JOptionPane.showMessageDialog(null, "You Missed.");
			
			
		} else {
			buttonGrid[gridX][gridY].setBackground(Color.RED);
			
			int c = 0;
			for (int i=0; i<10; i++)
				for (int j=0; j<10; j++)
					if (computerGrid[i][j] == computerGrid[gridX][gridY])
						c += 1;
			
			playerHit += 1;
			
			if (c == 1){
				JOptionPane.showMessageDialog(null, "You sank the " + shipNames[computerGrid[gridX][gridY] - 1]);
				playerSank += 1;
			}else{
				JOptionPane.showMessageDialog(null, "You hit the " + shipNames[computerGrid[gridX][gridY] - 1]);
				
			}
			
			computerGrid[gridX][gridY] = 0;
		}

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				buttonGrid[i][j].setEnabled(false);
			}
		}

		nextTurn();
	}
	
	public static void main(String[] args) {
		new AlgorithmMain();
	}
}
