import java.util.LinkedList;
import java.util.Queue;

public class KillAlgorithm {
	
	/*
	 * 	int[][] killGrid: Status on killAlgorithm
	 * 
	 * 			0		Default
	 * 			1		Visited - Hit
	 * 			2		Visited - Miss
	 */
	public static int[][] killGrid;
	
	public static int[][] gridProb;
	
	public static void init(){
		killGrid = new int[AlgorithmMain.gridSizeX][AlgorithmMain.gridSizeY];
	}
	
	public static void recalculate(){
		
		KillAlgorithm.killGrid = (int[][])AlgorithmMain.gridState.clone();
		
		int[][] prob = new int[AlgorithmMain.gridSizeX][AlgorithmMain.gridSizeY];
		
		for (int i = 0; i < AlgorithmMain.gridSizeX; i++)
			for (int j = 0; j < AlgorithmMain.gridSizeY; j++)
				prob[i][j] = 0;
		
		for (int cX = 0; cX < AlgorithmMain.gridSizeX; cX++){
			for (int cY = 0; cY < AlgorithmMain.gridSizeY; cY++){
				
				for (int p = 0; p < AlgorithmMain.shipCount; p++){
					if (!AlgorithmMain.shipSank[p]){
						int lX = cX + AlgorithmMain.shipSize[p][0] - 1, 
								lY = cY + AlgorithmMain.shipSize[p][1] - 1;
						
						if (lX >= AlgorithmMain.gridSizeX || lY >= AlgorithmMain.gridSizeY) continue;
						
						boolean hitIncluded = false;
						boolean missIncluded = false;
						
						for (int i = cX; i <= lX; i++){
							if (killGrid[i][cY] == 1)
								hitIncluded = true;
							
							if (killGrid[i][cY] == 2){
								missIncluded = true;
								break;
							}
						}
						
						if (!hitIncluded || missIncluded) continue;
						
						for (int i = cX; i <= lX; i++)
							prob[i][cY] +=1 ;
					}
				}
				
				for (int p = 0; p < AlgorithmMain.shipCount; p++){
					if (!AlgorithmMain.shipSank[p]){
						int lX = cX + AlgorithmMain.shipSize[p][1] - 1, 
								lY = cY + AlgorithmMain.shipSize[p][0] - 1;
						
						if (lX >= AlgorithmMain.gridSizeX || lY >= AlgorithmMain.gridSizeY) continue;
						
						boolean hitIncluded = false;
						boolean missIncluded = false;
						
						for (int i = cY; i <= lY; i++){
							if (killGrid[cX][i] == 1)
								hitIncluded = true;
							
							if (killGrid[cX][i] == 2){
								missIncluded = true;
								break;
							}
						}
						
						if ((!hitIncluded) || missIncluded) continue;
						
						for (int i = cY; i <= lY; i++)
							prob[cX][i] += 1;
					}
				}
				
			}
			
		}
		
		gridProb = prob;
		
		System.out.println("[DEBUG]\tGrid Probability Updated");
		for (int i = 0; i < AlgorithmMain.gridSizeY; i++){
			for (int j = 0; j < AlgorithmMain.gridSizeX; j++)
				System.out.printf("%d[%d]\t", (int)AlgorithmMain.gridProbability[j][i], AlgorithmMain.gridState[j][i]);
			System.out.println();
		}
		
		System.out.println("\n\n");
	}
	
}
