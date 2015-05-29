import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class MapEditor extends JFrame implements ActionListener{
	
	public static final int[] shipSize = {2, 3, 3, 4, 5};
	public static final String[] shipNames = {
		"Destroyer", "Submarine", "Cruiser", "Battleship", "Carrier"
	};
	
	public JPanel pnlButtonGrid = new JPanel();
	
	public JButton[][] buttonGrid = new JButton[10][10];
	public JButton btnSubmit = new JButton("Submit Placement");
	
	public int[][] playerGrid = new int[10][10];
	public int currentPlacement = 1;
	
	public MapEditor(){
		init();
		
		this.setTitle("Map Editor");
		this.setSize(400, 300);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
		
		pnlButtonGrid.setLayout(new GridLayout(10, 10));
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				buttonGrid[i][j] = new JButton();
				buttonGrid[i][j].setFont(new Font("Courier New", Font.BOLD, 15));
				buttonGrid[i][j].setActionCommand(i + " " + j);
				buttonGrid[i][j].addActionListener(this);
				pnlButtonGrid.add(buttonGrid[i][j]);
			}
		}
		
		this.add(pnlButtonGrid, BorderLayout.CENTER);
		
		btnSubmit.setActionCommand("submit");
		btnSubmit.addActionListener(this);
		this.add(btnSubmit, BorderLayout.SOUTH);
		
		this.repaint();	
		this.revalidate();
	}
	
	public void init(){
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; j++)
				playerGrid[i][j] = 0;
	}
	
	public boolean validityCheck() {
		if (currentPlacement != 5)
			return false;
		
		int[] shipCount = new int[5];

		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 10; i++)
				if (playerGrid[i][j] != 0)
					shipCount[playerGrid[i][j] - 1] += 1;

		for (int i = 0; i < 5; i++)
			if (shipCount[i] != shipSize[i])
				return false;
		
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("submit")){
			if (validityCheck()){
				return;
			} else {
				JOptionPane.showMessageDialog(null, "Invalid Placement, try again bitch");
			}
		} else {
			int locationX = Integer.parseInt(e.getActionCommand().split(" ")[0]);
			int locationY = Integer.parseInt(e.getActionCommand().split(" ")[1]);
			
			String[] strOrientation = { "Vertical", "Horizontal" };
			
			int option = JOptionPane.showOptionDialog(null,
					"Please select ship orientation", "Ship Orientation", JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, strOrientation, strOrientation[0]);
			
			if (option == 0){
				int cY = locationY + shipSize[currentPlacement - 1] - 1;
				
				if (cY >= 10){
					JOptionPane.showMessageDialog(null, "Invalid Placement: OUT OF BOUNDS");
					return;
				}
				
				boolean visited = false;
				for (int i = locationY; i <= cY; i++)
					if (playerGrid[locationX][i] != 0) visited = true;
				
				if (visited){
					JOptionPane.showMessageDialog(null, "Invalid Placement: OVERLAP WITH EXISTING SHIP");
					return;
				}
				
				for (int i = locationY; i <= cY; i++) {
					playerGrid[locationX][i] = currentPlacement;
					buttonGrid[locationX][i].setBackground(Color.LIGHT_GRAY);
					buttonGrid[locationX][i].setText(Integer.toString(currentPlacement));
				}
				
				int response = JOptionPane.showOptionDialog(null,
						"Are you sure? ", "Confirmation",
						JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
						null, new String[] { "Yes", "No" }, "No");
				
				if (response == 0){
					currentPlacement += 1;
				} else {
					for (int i=locationY; i<=cY; i++){
						playerGrid[locationX][i] = 0;
						buttonGrid[locationX][i].setBackground(new JButton().getBackground());
						buttonGrid[locationX][i].setText("");
					}
				}
			} else {
				int cX = locationX + shipSize[currentPlacement - 1] - 1;
				
				if (cX >= 10){
					JOptionPane.showMessageDialog(null, "Invalid Placement: OUT OF BOUNDS");
					return;
				}
				
				boolean visited = false;
				for (int i = locationX; i <= cX; i++)
					if (playerGrid[i][locationY] != 0) visited = true;
				
				if (visited){
					JOptionPane.showMessageDialog(null, "Invalid Placement: OVERLAP WITH EXISTING SHIP");
					return;
				}
				
				for (int i = locationX; i <= cX; i++) {
					playerGrid[i][locationY] = currentPlacement;
					buttonGrid[i][locationY].setBackground(Color.LIGHT_GRAY);
					buttonGrid[i][locationY].setText(Integer.toString(currentPlacement));
				}
				
				int response = JOptionPane.showOptionDialog(null,
						"Are you sure? ", "Confirmation",
						JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
						null, new String[] { "Yes", "No" }, "No");
				
				if (response == 0){
					currentPlacement += 1;
				} else {
					for (int i=locationX; i<=cX; i++){
						playerGrid[i][locationY] = 0;
						buttonGrid[i][locationY].setBackground(new JButton().getBackground());
						buttonGrid[i][locationY].setText("");
					}
				}
			}
		}
	}
	
}
