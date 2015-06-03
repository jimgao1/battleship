import java.util.LinkedList;
import java.util.Queue;

public class KillAlgorithm {
	
	/*
	 * 	int[][] killGrid: Status on killAlgorithm
	 * 
	 * 			0		Default
	 * 			1		Visited - Hit
	 * 			2		Visited - Miss
	 * 
	 * 0 Default 1 Hit (current round) 2 Hit (previous round) 3 Miss
	 */
	public static int[][] killGrid;
	
	public static void init(){
		killGrid = new int[BattleshipSP.gridSizeX][BattleshipSP.gridSizeY];
	}
	
	public static void recalculate(){
		
		KillAlgorithm.killGrid = (int[][])BattleshipSP.gridState.clone();
		
		double[][] prob = new double[BattleshipSP.gridSizeX][BattleshipSP.gridSizeY];
		
		for (int i = 0; i < BattleshipSP.gridSizeX; i++)
			for (int j = 0; j < BattleshipSP.gridSizeY; j++)
				prob[i][j] = 0;
		
		for (int cX = 0; cX < BattleshipSP.gridSizeX; cX++){
			for (int cY = 0; cY < BattleshipSP.gridSizeY; cY++){
				
				for (int p = 0; p < BattleshipSP.shipCount; p++){
					if (!BattleshipSP.shipSank[p]){
						int lX = cX + BattleshipSP.shipSize[p][0] - 1, 
								lY = cY + BattleshipSP.shipSize[p][1] - 1;
						
						if (lX >= BattleshipSP.gridSizeX || lY >= BattleshipSP.gridSizeY) continue;
						
						boolean hitIncluded = false;
						boolean missIncluded = false;
						
						int nHitIncluded = 0;
						
						for (int i = cX; i <= lX; i++){
							if (killGrid[i][cY] == 1){
								hitIncluded = true;
								nHitIncluded += 1;
							}
							
							if (killGrid[i][cY] == 2 || killGrid[i][cY] == 3){
								missIncluded = true;
								break;
							}
						}
						
						if (!hitIncluded || missIncluded) continue;
						
						for (int i = cX; i <= lX; i++)
							prob[i][cY] += nHitIncluded;
					}
				}
				
				for (int p = 0; p < BattleshipSP.shipCount; p++){
					if (!BattleshipSP.shipSank[p]){
						int lX = cX + BattleshipSP.shipSize[p][1] - 1, 
								lY = cY + BattleshipSP.shipSize[p][0] - 1;
						
						if (lX >= BattleshipSP.gridSizeX || lY >= BattleshipSP.gridSizeY) continue;
						
						boolean hitIncluded = false;
						boolean missIncluded = false;
						
						int nHitIncluded = 0;
						
						for (int i = cY; i <= lY; i++){
							if (killGrid[cX][i] == 1){
								hitIncluded = true;
								nHitIncluded += 1;
							}
							
							if (killGrid[cX][i] == 2 || killGrid[cX][i] == 3){
								missIncluded = true;
								break;
							}
						}
						
						if ((!hitIncluded) || missIncluded) continue;
						
						for (int i = cY; i <= lY; i++)
							prob[cX][i] += nHitIncluded;
					}
				}
				
			}
			
		}
		
		BattleshipSP.gridProbability = prob;
		
		System.out.println("[DEBUG]\tGrid Probability Updated");
		for (int i = 0; i < BattleshipSP.gridSizeY; i++){
			for (int j = 0; j < BattleshipSP.gridSizeX; j++)
				System.out.printf("%d\t", (int)BattleshipSP.gridProbability[j][i]);
			System.out.println();
		}
		
		System.out.println("\n\n");
	}
	
}
