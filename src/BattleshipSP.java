import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BattleshipSP extends JFrame implements ActionListener{
	
	public static final int winHits = 2 + 3 + 3 + 4 + 5;
	
	public static int[][] playerGrid = new int[10][10];
	public static int[][] opponentGrid = new int[10][10];
	
	public static int[][] playerGridState = new int[10][10];
	public static int[][] opponentGridState = new int[10][10];
	
	public static int playerHits = 0, computerHits = 0;
	public static int turnCount = 0;
	public static boolean playerTurn = false;
	
	public JButton[][] opponentButtonGrid = new JButton[10][10];
	public JButton[][] playerButtonGrid = new JButton[10][10];

	public JPanel pnlPlayer = new JPanel();
	public JPanel pnlOpponent = new JPanel();
	public JPanel pnlControls = new JPanel();
	public JPanel pnlHeader = new JPanel();

	public JLabel lblStatus = new JLabel("This is the current status");

	public JLabel lblLeft = new JLabel("Opponent", JLabel.CENTER);
	public JLabel lblRight = new JLabel("Player", JLabel.CENTER);
	public JLabel lblCenter = new JLabel("Controls", JLabel.CENTER);
	
	public BattleshipSP(){
		this.setTitle("Battleship");
		this.setSize(1200, 500);
		this.setVisible(true);
		this.setLayout(new BorderLayout(20, 5));
		this.setLocation(100, 100);
		this.setResizable(false);

		pnlHeader.setLayout(new BorderLayout());

		lblLeft.setFont(new Font("Courier New", Font.BOLD, 16));
		lblRight.setFont(new Font("Courier New", Font.BOLD, 16));
		lblCenter.setFont(new Font("Courier New", Font.BOLD, 16));

		pnlHeader.add(lblLeft, BorderLayout.WEST);
		pnlHeader.add(lblRight, BorderLayout.EAST);
		pnlHeader.add(lblCenter, BorderLayout.CENTER);

		this.add(pnlHeader, BorderLayout.NORTH);

		pnlOpponent.setLayout(new GridLayout(10, 10));
		pnlOpponent.setPreferredSize(new Dimension(400, 400));

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				opponentButtonGrid[i][j] = new JButton();
				opponentButtonGrid[i][j].setActionCommand(i + " " + j);
				opponentButtonGrid[i][j].addActionListener(this);
				pnlOpponent.add(opponentButtonGrid[i][j]);
			}
		}

		this.add(pnlOpponent, BorderLayout.WEST);

		pnlPlayer.setLayout(new GridLayout(10, 10));
		pnlPlayer.setPreferredSize(new Dimension(400, 400));

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				playerButtonGrid[i][j] = new JButton();
				playerButtonGrid[i][j].setActionCommand(i + " " + j);
				
				pnlPlayer.add(playerButtonGrid[i][j]);
			}
		}

		this.add(pnlPlayer, BorderLayout.EAST);

		pnlControls.setLayout(new GridLayout(8, 1));
		pnlControls.add(new JButton(""));

		this.add(pnlControls, BorderLayout.CENTER);

		lblStatus.setFont(new Font("Courier New", Font.BOLD, 16));
		this.add(lblStatus, BorderLayout.SOUTH);
		
		this.repaint();
		this.revalidate();
		
		generateRandomPlacement();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int locationX = (int) Double.parseDouble(e.getActionCommand()
				.split(" ")[0]);
		int locationY = (int) Double.parseDouble(e.getActionCommand()
				.split(" ")[1]);
		
		System.out.println("Button click x = " + locationX + ", y = " + locationY);

		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				opponentButtonGrid[i][j].setEnabled(false);
		
		if (BattleshipSP.opponentGrid[locationX][locationY] >= 1){
			BattleshipSP.opponentGridState[locationX][locationY] = BattleshipSP.opponentGrid[locationX][locationY];
		} else {
			BattleshipSP.opponentGridState[locationX][locationY] = -2;
		}
		
		
		nextTurn();
	}
	
	public void generateRandomPlacement() {
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				opponentGrid[i][j] = 0;

		for (int i = 0; i < AlgorithmMain.shipCount; i++) {
			int pX = (int) (Math.random() * 10), pY = (int) (Math.random() * 10);
			boolean vert = (Math.random() >= 0.5);
			boolean valid = true;

			if (vert) {
				if (pY + AlgorithmMain.shipSize[i][0] - 1 < 10) {
					for (int j = pY; j < pY + AlgorithmMain.shipSize[i][0]; j++)
						if (opponentGrid[pX][j] != 0) {
							valid = false;
							break;
						}
				} else {
					valid = false;
				}

				if (valid) {
					for (int j = pY; j < pY + AlgorithmMain.shipSize[i][0]; j++)
						opponentGrid[pX][j] = i + 1;
				} else {
					i--;
					continue;
				}
			} else {
				if (pX + AlgorithmMain.shipSize[i][0] - 1 < 10) {
					for (int j = pX; j < pX + AlgorithmMain.shipSize[i][0]; j++)
						if (opponentGrid[j][pY] != 0) {
							valid = false;
							break;
						}
				} else {
					valid = false;
				}

				if (valid) {
					for (int j = pX; j < pX + AlgorithmMain.shipSize[i][0]; j++)
						opponentGrid[j][pY] = i + 1;
				} else {
					i--;
					continue;
				}
			}
		}

		System.out.println("[INFO]\tGenerated Opponent Placement");
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++)
				System.out.printf("%d ", opponentGrid[j][i]);
			System.out.println();
		}
	}
	
	
	public void nextTurn(){
		
		playerTurn = !playerTurn;
		
		System.out.println("New Turn: " + playerTurn);
		
		/*
		 * 	Check if someone has won the game
		 */
		if (playerHits == winHits || computerHits == winHits){
			if (playerHits == winHits){
				JOptionPane.showMessageDialog(null, "You beat the computer.");
			} else {
				JOptionPane.showMessageDialog(null, "You are defeated by the computer.");
			}
			
			return;
		}
		
		if (playerTurn){
			/*
			 * 	Enable all the grid except previous hit ones
			 */
			
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					opponentButtonGrid[i][j].setEnabled(false);
					
					if (opponentGridState[i][j] == 0){			//Default state
						opponentButtonGrid[i][j].setEnabled(true);
					} else if (opponentGridState[i][j] == -1 || opponentGridState[i][j] >= 1){		//Hit
						opponentButtonGrid[i][j].setBackground(Color.red);
						opponentButtonGrid[i][j].setEnabled(false);
					} else {			//Miss
						opponentButtonGrid[i][j].setBackground(Color.black);
						opponentButtonGrid[i][j].setEnabled(false);
					}
				}
			}
		} else {
			int[] suggestedLocation = AlgorithmMain.getSuggestedCoordinates();
			int locationX = suggestedLocation[0], locationY = suggestedLocation[1];
			
			if (playerGrid[locationX][locationY] >= 1 && playerGridState[locationX][locationY] == 0){
				playerGridState[locationX][locationY] = playerGrid[locationX][locationY];
				playerButtonGrid[locationX][locationY].setBackground(Color.red);
			} else {
				playerGridState[locationX][locationY] = -2;
				playerButtonGrid[locationX][locationY].setBackground(Color.BLACK);
			}
			
			/*
			 * 		update the thing here
			 */
		}
	}
	
	public void run(){
		nextTurn();
	}
}
 