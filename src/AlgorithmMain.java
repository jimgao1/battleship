import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AlgorithmMain {
	
	/*
	 * 		Algorithm Configuration
	 */
	
	public static final int gridSizeX = 10;
	public static final int gridSizeY = 10;
	
	public static final int shipCount = 5;
	public static final int[][] shipSize = {
		{2, 1}, {3, 1}, {3, 1}, {4, 1}, {5, 1}
	};
	
	public static final String[] outcomes = {
		"Miss", "Hit", "Sank"
	};
	
	
	/*
	 * 		Algorithm Variables
	 */
	
	/*
	 * 		int[][] gridState: the status of the cells in the grid
	 * 
	 * 		0 				Default
	 * 		1 ~ n + 1		Hit (with ship i, current round)
	 * 		-1 				Hit (previous round)
	 * 		-2				Miss
	 */
	
	public static int[][] gridState = new int[gridSizeX][gridSizeY];
	public static int[][] opponentState = new int[gridSizeX][gridSizeY];
	
	public static double[][] gridProbability = new double[gridSizeX][gridSizeY];
	
	public static boolean[] shipSank = new boolean[shipCount];
	
	public static boolean killModeEngaged = false;
	
	public static void init(){
		for (int i=0; i<gridSizeX; i++)
			for (int j=0; j<gridSizeY; j++){
				gridState[i][j] = 0;
				gridProbability[i][j] = 0;
			}
		
		for (int i=0; i<shipCount; i++)
			shipSank[i] = false;
		
		KillAlgorithm.init();
	}
	
	public static void mark(int x, int y, int newStatus){
		gridState[x][y] = newStatus;
		HuntAlgorithm.recalculate();
		
	}
	
	public static int[] getSuggestedCoordinates(){
		int maxLocationX = -1, maxLocationY = -1;
		
		if (killModeEngaged){
			System.out.println("[INFO]\t***KILL MODE ENGAGED***");
			
			KillAlgorithm.recalculate();
			
			double maxProb = -1;
			
			for (int i = 0; i < AlgorithmMain.gridSizeX; i++)
				for (int j = 0; j < AlgorithmMain.gridSizeY; j++)
					if (AlgorithmMain.gridProbability[i][j] > maxProb && AlgorithmMain.gridState[i][j] == 0){
						maxProb = AlgorithmMain.gridProbability[i][j];
						maxLocationX = i; 
						maxLocationY = j;
					}
			
			System.out.println("[INFO]\tSuggested Location (" + (maxLocationX + 1) + ", " + (maxLocationY + 1) + ") ");
			JOptionPane.showMessageDialog(null, "Suggested Location (" + (maxLocationX + 1) + ", " + (maxLocationY + 1) + ") ");
		} else {
			HuntAlgorithm.recalculate();
			
			double maxProb = -1;
			
			for (int i = 0; i < AlgorithmMain.gridSizeX; i++)
				for (int j = 0; j < AlgorithmMain.gridSizeY; j++)
					if (AlgorithmMain.gridProbability[i][j] > maxProb && AlgorithmMain.gridState[i][j] == 0){
						maxProb = AlgorithmMain.gridProbability[i][j];
						maxLocationX = i; 
						maxLocationY = j;
					}
			
			System.out.println("[INFO]\tSuggested Location (" + (maxLocationX + 1) + ", " + (maxLocationY + 1) + ") ");
			JOptionPane.showMessageDialog(null, "Suggested Location (" + (maxLocationX + 1) + ", " + (maxLocationY + 1) + ") ");
		}
		
		return new int[]{maxLocationX, maxLocationY};
	
	}
	
	public static void updateSuggestedLocation(){
		
	}

	public static void run() {
		
			
	}
}

