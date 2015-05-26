import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GridGUI extends JFrame{
	
	public GridGUI(){
		this.setTitle("");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocation(100, 100);
		this.setSize(600, 600);
		this.setResizable(false);
		this.setLayout(new GridLayout(AlgorithmMain.gridSizeX, AlgorithmMain.gridSizeY));
		this.setVisible(true);
		
		double maxIntensity = Integer.MIN_VALUE;
		
		for (int i = 0; i < AlgorithmMain.gridSizeX; i++)
			for (int j = 0; j < AlgorithmMain.gridSizeY; j++)
				maxIntensity = Math.max(maxIntensity, AlgorithmMain.gridProbability[i][j]);
		
		for (int i = 0; i < AlgorithmMain.gridSizeX; i++)
			for (int j = 0; j < AlgorithmMain.gridSizeY; j++){
				JButton b = new JButton();
				
				int intensity = (int) ((AlgorithmMain.gridProbability[i][j] / maxIntensity) * 127);
				b.setBackground(new Color(intensity, intensity, intensity));
				
				this.add(b);
			}
		
		this.repaint();		
		this.revalidate();
	}
}
