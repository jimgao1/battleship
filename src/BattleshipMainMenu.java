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

		singlePlayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new SPBattleship();
			}

		});

		localMultiplayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		lanMultiplayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String aboutString = "<html><h1>Battleship</h1><h2><br>By: Philip Huang, Jim Gao, Joseph Zhang</h2></html>";
				JOptionPane.showMessageDialog(null, aboutString);
			}

		});

		exit.addActionListener(new ActionListener() {

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