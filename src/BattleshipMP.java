import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BattleshipMP extends JFrame implements ActionListener, Runnable{

	public static final String[] shipNames = {
		"Destroyer", "Submarine", "Cruiser", "Battleship", "Carrier"
	};
	
	/*
	 * 		TCP/IP stuff
	 */
	public ServerSocket serverSock;
	
	public Socket sock;
	public BufferedReader reader;
	public PrintWriter writer;
	
	
	/*
	 * 		GUI components
	 */
	
	public JButton[][] opponentButtonGrid = new JButton[10][10];
	public JButton[][] playerButtonGrid = new JButton[10][10];

	public JPanel pnlPlayer = new JPanel();
	public JPanel pnlOpponent = new JPanel();
	public JPanel pnlControls = new JPanel();
	public JPanel pnlHeader = new JPanel();

	public JLabel lblStatus = new JLabel("");
	public JLabel scoreBoard = new JLabel();

	public JLabel lblLeft = new JLabel("Opponent's Grid", JLabel.CENTER);
	public JLabel lblRight = new JLabel("Your Grid", JLabel.CENTER);
	public JLabel lblCenter = new JLabel("Controls", JLabel.CENTER);
	
	/*
	 * 		Game Information
	 */
	
	/*
	 * 	~~~GRID LEGEND~~~
	 * 
	 * 		0			Default
	 * 		1 ~ n+1		Hit
	 * 		-1			Previous Turn
	 * 		-2			Miss
	 */
	
	public int[][] playerGrid = new int[10][10];
	
	public int[][] playerStatusGrid = new int[10][10];
	public int[][] opponentStatusGrid = new int[10][10];
	
	public boolean playerTurn;
	
	public boolean firstRun = true;
	
	/*
	 * 	Win/Lose indicator
	 */
	public int playerSank = 0;
	public int enemySank = 0;
	
	/*
	 * 	Score board indicator
	 */
	
	public int roundCount = 0;
	public String scoreBoardString = "";
	
	/*
	 * 	Constructor as SERVER
	 */
	public BattleshipMP(int port){
		try{
			serverSock = new ServerSocket(port);
			
			JOptionPane.showMessageDialog(null, "<html>Waiting for client to connect. IP: <b>" + this.getIpAddress() + "</b></html>");
			
			sock = serverSock.accept();
			reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			writer = new PrintWriter(sock.getOutputStream(), true);
			
			System.out.println("[INFO]\tConnection Established. IP:" + sock.getInetAddress());
			JOptionPane.showMessageDialog(null, "Player Connected. IP: " + sock.getInetAddress());
			playerTurn = true;
			
			this.initGUI();
			this.generateRandomPlacement();
		
		} catch (IOException ex){
			System.out.println("[ERROR]\tConnection Error. " + ex.getLocalizedMessage());
			JOptionPane.showMessageDialog(null, "Connection cannot be established.");
			this.dispose();
		}
	}
	
	/*
	 * 	Constructor as CLIENT
	 */
	public BattleshipMP(String serverIP, int port){
		try{
			sock = new Socket(serverIP, port);
			
			reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			writer = new PrintWriter(sock.getOutputStream(), true);
			
			System.out.println("[INFO]\tConnection Established (CLIENT). IP: " + sock.getInetAddress());
			playerTurn = false;
			
			this.initGUI();
			this.generateRandomPlacement();
		} catch (IOException ex){
			System.out.println("[ERROR]\tConnection Error. " + ex.getLocalizedMessage());
			JOptionPane.showMessageDialog(null, "Connection cannot be established.");
			this.dispose();
		}
	}
	
	public String getIpAddress() { 
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()&&inetAddress instanceof Inet4Address) {
                        String ipAddress=inetAddress.getHostAddress().toString();
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            
        }
        return null; 
	}
	
	public void initGUI(){
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
				opponentButtonGrid[i][j].setFont(new Font("Courier New", Font.BOLD, 14));
				
				if (!playerTurn)
					opponentButtonGrid[i][j].setEnabled(false);
				
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
				playerButtonGrid[i][j].setFont(new Font("Courier New", Font.BOLD, 14));
				playerButtonGrid[i][j].setEnabled(false);
				
				pnlPlayer.add(playerButtonGrid[i][j]);
			}
		}

		this.add(pnlPlayer, BorderLayout.EAST);

		pnlControls.setLayout(new GridLayout(1, 1));
		
		scoreBoard.setFont(new Font("Courier New", Font.PLAIN, 15));

		pnlControls.add(scoreBoard);
		this.add(pnlControls, BorderLayout.CENTER);

		lblStatus.setFont(new Font("Courier New", Font.BOLD, 16));
		this.add(lblStatus, BorderLayout.SOUTH);
		
		this.repaint();
		this.revalidate();
		
		nextTurn();
	}
	
	public void generateRandomPlacement() {
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
		
		for (int i=0; i<10; i++)
			for (int j=0; j<10; j++)
				if (randomGrid[i][j] >= 1){
					playerButtonGrid[i][j].setBackground(Color.LIGHT_GRAY);
					playerButtonGrid[i][j].setText(Integer.toString(randomGrid[i][j]));
				}
		
		playerGrid = randomGrid;
		
		this.repaint();
		this.revalidate();
	}
	
	public void updateScoreBoard(){
		scoreBoardString = "<html>";
		scoreBoardString += "<h1>" + (playerTurn ? "Your Turn" : "Enemy's Turn") + "</h1><br>";
		scoreBoardString += "<b>Rounds: </b>" + this.roundCount + "<br>";
		scoreBoardString += "Player sunk <b>" + this.playerSank + "</b> ships.<br>";
		scoreBoardString += "Opponent sunk <b>" + this.enemySank + "</b> ships.<br>";
		scoreBoardString += "</html>";
		
		scoreBoard.setText(scoreBoardString);
		this.repaint();
		this.revalidate();
	}
	
	public void nextTurn(){
		
		System.out.println("\n[INFO]\tNew Round Started. PlayerTurn = " + playerTurn);
		
		if (playerTurn){
			updateScoreBoard();
			
			
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					opponentButtonGrid[i][j].setEnabled(false);
					
					if (opponentStatusGrid[i][j] == -2){
						opponentButtonGrid[i][j].setIcon(new ImageIcon("water.png"));
					} else if (opponentStatusGrid[i][j] == -1 || 
										opponentStatusGrid[i][j] >= 1){
						opponentButtonGrid[i][j].setIcon(new ImageIcon("fire.png"));
					} else {
						opponentButtonGrid[i][j].setEnabled(true);
					}
				}
			}
			
			
		} else {
			
			updateScoreBoard();
			
			if (firstRun){
				JOptionPane.showMessageDialog(null, "Connected to server");
				firstRun = false;
			}
			
			lblStatus.setText("Enemy's Turn");
			this.repaint();
			this.revalidate();
			
			System.out.println("[INFO]\tWaiting for server....");
			
			String cmd = "";
			
			try{
				cmd = reader.readLine();
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Socket read error.");
				System.out.println("[ERROR]\tSocket read error: " + ex.getLocalizedMessage());
			}
			
			System.out.println("[INFO]\tEnemy Command: " + cmd);
			System.out.println("[INFO]\tCommand Length: " + cmd.length());
			
			int hitLocationX = Integer.parseInt(cmd.split(" ")[0]);
			int hitLocationY = Integer.parseInt(cmd.split(" ")[1]);			
			
			System.out.println("[INFO]\thitLocationX = " + hitLocationX + ", hitLocationY = " + hitLocationY);
			
			if (playerGrid[hitLocationX][hitLocationY] == 0){
				writer.println("miss");
				writer.flush();
				System.out.println("[INFO]\tEnemy Missed");
				playerButtonGrid[hitLocationX][hitLocationY].setIcon(new ImageIcon("water.png"));
				playerButtonGrid[hitLocationX][hitLocationY].setText("");
			} else {
				writer.println(Integer.toString(playerGrid[hitLocationX][hitLocationY]));
				writer.flush();
				System.out.println("[INFO]\tEnemy Hit");
				
				
				int shipID = playerGrid[hitLocationX][hitLocationY];
				int shipCount = 0;

				for (int i = 0; i < 10; i++)
					for (int j = 0; j < 10; j++)
						if (playerGrid[i][j] == shipID)
							shipCount++;
				
				if (shipCount == 1){
					writer.println("true");
					System.out.println("[INFO]\tThe ship was sank.");
					enemySank += 1;
					if (enemySank >= 5){
						System.out.println("[INFO]\tYou have lost the game. ");
						JOptionPane.showMessageDialog(null, "You have lost the game.");
						this.dispose();
					}
				} else {
					writer.println("false");
				}
				
				playerGrid[hitLocationX][hitLocationY] = -1;
				
				playerButtonGrid[hitLocationX][hitLocationY].setIcon(new ImageIcon("fire.png"));
				playerButtonGrid[hitLocationX][hitLocationY].setText("");
			}
			
			playerTurn = true;
			nextTurn();
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		int locationX = Integer.parseInt(e.getActionCommand().split(" ")[0]);
		int locationY = Integer.parseInt(e.getActionCommand().split(" ")[1]);
		
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				opponentButtonGrid[i][j].setEnabled(false);
		
		writer.println(e.getActionCommand() + "");
		writer.flush();
		System.out.println("[INFO]\tSent to other host: "+ e.getActionCommand());
		
		String cmd = "";
		
		try{
			cmd = reader.readLine();
		} catch (IOException ex){
			JOptionPane.showMessageDialog(null, "Socket read error.");
			System.out.println("[ERROR]\tSocket read error: " + ex.getLocalizedMessage());
		}
		
		if (cmd.equals("miss")){
			System.out.println("[INFO]\tServer Response: miss");
			opponentButtonGrid[locationX][locationY].setIcon(new ImageIcon("water.png"));
			opponentButtonGrid[locationX][locationY].setBackground(Color.BLUE);
			JOptionPane.showMessageDialog(null, "It was a MISS");
		} else {

			/*
			 * 		Get if the ship is sunk
			 */
			
			String strSunk = "";
			
			try{
				strSunk = reader.readLine();
			} catch (IOException ex){
				JOptionPane.showMessageDialog(null, "Socket read error.");
				System.out.println("[ERROR]\tSocket read error: " + ex.getLocalizedMessage());
			}
			
			boolean sunk = strSunk.equals("true");
			
			int shipID = Integer.parseInt(cmd);
			System.out.println("[INFO]\tServer Response: hit");
			System.out.println("[INFO]\tShip Hit: " + shipID);
			System.out.println("[INFO]\tSunk? " + sunk);
			opponentButtonGrid[locationX][locationY].setIcon(new ImageIcon("fire.png"));
			opponentButtonGrid[locationX][locationY].setBackground(Color.RED);
			
			if (sunk)
				JOptionPane.showMessageDialog(null, "You sank the " + shipNames[shipID - 1]);
			else
				JOptionPane.showMessageDialog(null, "You hit the " + shipNames[shipID - 1]);
			
			if (sunk) playerSank += 1;
			if (playerSank >= 5){
				System.out.println("[INFO]\tYou have won the game.");
				JOptionPane.showMessageDialog(null, "You have won the game");
				this.dispose();
			}
		}

		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				opponentButtonGrid[i][j].setEnabled(false);
		
		playerTurn = false;
		updateScoreBoard();
		nextTurn();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
}
