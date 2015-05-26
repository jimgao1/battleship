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

public class BattleshipMain {
	
	public static BattleshipGUI mainBattleshipGUI;
	
	public static class BattleshipMainMenu extends JFrame{
		
		public BattleshipMainMenu(){
			/*
			 * 		Remove window border
			 */
			this.setUndecorated(true);
			this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
			
			this.setSize(400, 300);
			this.setLayout(new BorderLayout());
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setVisible(true);
			
			/*
			 * 		Centering the current window
			 */
			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		    int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		    this.setLocation(x, y);
			
			try {
				UIManager.setLookAndFeel(new PlasticLookAndFeel());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			JLabel titleLabel = new JLabel("Battleship");
			titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 55));
			titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
			titleLabel.setVerticalAlignment(SwingConstants.CENTER);
			
			this.add(titleLabel, BorderLayout.NORTH);
			
			JPanel controlPanel = new JPanel();
			controlPanel.setLayout(new GridLayout(5, 1));
			
			JButton singlePlayer = new JButton("Single Player");
			JButton localMultiplayer = new JButton("Local Multiplayer");
			JButton lanMultiplayer = new JButton("LAN Multiplayer");
			JButton about = new JButton("About");
			JButton exit = new JButton("Exit");
			
			singlePlayer.setFont(new Font("Times New Roman", Font.PLAIN, 20));
			localMultiplayer.setFont(new Font("Times New Roman", Font.PLAIN, 20));
			lanMultiplayer.setFont(new Font("Times New Roman", Font.PLAIN, 20));
			about.setFont(new Font("Times New Roman", Font.PLAIN, 20));
			exit.setFont(new Font("Times New Roman", Font.PLAIN, 20));
			
			singlePlayer.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					BattleshipMain.mainBattleshipGUI = new BattleshipGUI();
				}
				
			});
			
			localMultiplayer.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			lanMultiplayer.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			about.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					String aboutString = "<html><h1>Battleship</h1><h2><br>By: Philip Huang, Jim Gao, Joseph Zhang</h2></html>";
					JOptionPane.showMessageDialog(null, aboutString);
				}
			
			});
			
			exit.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}
				
			});
			
			controlPanel.add(singlePlayer);
			controlPanel.add(localMultiplayer);
			controlPanel.add(lanMultiplayer);
			controlPanel.add(about);
			controlPanel.add(exit);
			
			this.add(controlPanel, BorderLayout.CENTER);
			
			this.repaint();
			this.revalidate();
		}
		
	}
	
	public static class BattleshipGUI extends JFrame{
		
		public static class PlayerGridListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				int locationX = (int)Double.parseDouble(e.getActionCommand().split(" ")[0]);
				int locationY = (int)Double.parseDouble(e.getActionCommand().split(" ")[1]);
			
				System.out.println("[INFO]\t Player Grid Action: " + e.getActionCommand());
			}

		}
		
		public static class OpponentGridListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				int locationX = (int)Double.parseDouble(e.getActionCommand().split(" ")[0]);
				int locationY = (int)Double.parseDouble(e.getActionCommand().split(" ")[1]);
				
				System.out.println("[INFO]\t Opponent Grid Action: " + e.getActionCommand());
			
			}

		}
		
		public static JButton[][] opponentGrid = new JButton[10][10];
		public static JButton[][] playerGrid = new JButton[10][10];
		
		public static JPanel pnlPlayer = new JPanel();
		public static JPanel pnlOpponent = new JPanel();
		public static JPanel pnlControls = new JPanel();
		public static JPanel pnlHeader = new JPanel();
		
		public static JLabel lblStatus = new JLabel("This is the current status");
		
		public static JLabel lblLeft = new JLabel("Opponent", JLabel.CENTER);
		public static JLabel lblRight = new JLabel("Player", JLabel.CENTER);
		public static JLabel lblCenter = new JLabel("Controls", JLabel.CENTER);
		
		public static PlayerGridListener lisPlayer = new PlayerGridListener();
		public static OpponentGridListener lisOpponent = new OpponentGridListener();

		public BattleshipGUI(){
			
			this.setTitle("Battleship");
			this.setSize(1200, 500);
			this.setVisible(true);
			this.setLayout(new BorderLayout(20, 5));
			this.setLocation(100, 100);
			
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
			
			for (int i=0; i<10; i++){
				for (int j=0; j<10; j++){
					opponentGrid[i][j] = new JButton();
					opponentGrid[i][j].setActionCommand(i + " " + j);
					opponentGrid[i][j].addActionListener(lisOpponent);
					pnlOpponent.add(opponentGrid[i][j]);
				}
			}
			
			this.add(pnlOpponent, BorderLayout.WEST);
			
			pnlPlayer.setLayout(new GridLayout(10, 10));
			pnlPlayer.setPreferredSize(new Dimension(400, 400));
			
			for (int i=0; i<10; i++){
				for (int j=0; j<10; j++){
					playerGrid[i][j] = new JButton();
					playerGrid[i][j].setActionCommand(i + " " + j);
					playerGrid[i][j].addActionListener(lisPlayer);
					pnlPlayer.add(playerGrid[i][j]);
				}
			}
			
			this.add(pnlPlayer, BorderLayout.EAST);
			
			pnlControls.setLayout(new GridLayout(8, 1));
			pnlControls.add(new JButton("" ));
			
			this.add(pnlControls, BorderLayout.CENTER);
			
			lblStatus.setFont(new Font("Courier New", Font.BOLD, 16));
			this.add(lblStatus, BorderLayout.SOUTH);
		}
		
		public void updateGrid(){
			for (int i=0; i<10; i++)
				for (int j=0; j<10; j++){
					
				}
		}
	}
	
	public static void main(String[] args) {
		new BattleshipMainMenu();
		
	}
	
}
