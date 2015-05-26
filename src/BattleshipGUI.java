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
import javax.swing.JPanel;

public class BattleshipGUI extends JFrame {

	public static class PlayerGridListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int locationX = (int) Double.parseDouble(e.getActionCommand()
					.split(" ")[0]);
			int locationY = (int) Double.parseDouble(e.getActionCommand()
					.split(" ")[1]);

			System.out.println("[INFO]\t Player Grid Action: "
					+ e.getActionCommand());
		}

	}

	public static class OpponentGridListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int locationX = (int) Double.parseDouble(e.getActionCommand()
					.split(" ")[0]);
			int locationY = (int) Double.parseDouble(e.getActionCommand()
					.split(" ")[1]);

			System.out.println("[INFO]\t Opponent Grid Action: "
					+ e.getActionCommand());

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

	public BattleshipGUI() {

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

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				opponentGrid[i][j] = new JButton();
				opponentGrid[i][j].setActionCommand(i + " " + j);
				opponentGrid[i][j].addActionListener(lisOpponent);
				pnlOpponent.add(opponentGrid[i][j]);
			}
		}

		this.add(pnlOpponent, BorderLayout.WEST);

		pnlPlayer.setLayout(new GridLayout(10, 10));
		pnlPlayer.setPreferredSize(new Dimension(400, 400));

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				playerGrid[i][j] = new JButton();
				playerGrid[i][j].setActionCommand(i + " " + j);
				playerGrid[i][j].addActionListener(lisPlayer);
				pnlPlayer.add(playerGrid[i][j]);
			}
		}

		this.add(pnlPlayer, BorderLayout.EAST);

		pnlControls.setLayout(new GridLayout(8, 1));
		pnlControls.add(new JButton(""));

		this.add(pnlControls, BorderLayout.CENTER);

		lblStatus.setFont(new Font("Courier New", Font.BOLD, 16));
		this.add(lblStatus, BorderLayout.SOUTH);
	}

	public void updateGrid() {
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++) {
				if (AlgorithmMain.gridState[i][j] == 0) {
					// DO NOTHING HERE
				} else if (AlgorithmMain.gridState[i][j] == -1) {
					playerGrid[i][j].setBackground(Color.black);
				} else if (AlgorithmMain.gridState[i][j] == -2) {
					playerGrid[i][j].setBackground(Color.red);
				} else {
					playerGrid[i][j].setBackground(Color.red);
				}

				if (AlgorithmMain.opponentState[i][j] == 0) {
					// DO NOTHING HERE
				} else if (AlgorithmMain.opponentState[i][j] == -1) {
					opponentGrid[i][j].setBackground(Color.black);
				} else if (AlgorithmMain.opponentState[i][j] == -2) {
					opponentGrid[i][j].setBackground(Color.red);
				} else {
					opponentGrid[i][j].setBackground(Color.red);
				}
			}
		}
	}
}