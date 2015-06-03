/*
 * 	[HuntAlgorithm.java]
 * 
 * 	Author: Philip Huang, Jim Gao, Joseph Zhang
 * 	Purpose: The hunt algorithm, used when the computer is UNCERTAIN about 
 * 			the user's placement, and is searching for the most probable
 * 			location for ship placement
 */

public class HuntAlgorithm{
	
	
	/*
	 * 	The main and ONLY method of this class.
	 * 
	 * 	Consults the grid from BattleshipSP, and makes the corresponding calculations
	 * 	about the probability of placement in a specific cell
	 */
	public static void recalculate(){
		
		double[][] prob = new double[BattleshipSP.gridSizeX][BattleshipSP.gridSizeY];
		
		//Array Initialization
		for (int i = 0; i < BattleshipSP.gridSizeX; i++)
			for (int j = 0; j < BattleshipSP.gridSizeY; j++)
				prob[i][j] = 0;
		
		/*
		 * 	Goes through each cell, and then iterates through all the possible
		 * 	ship placements. Then increments 1 for the possible locations, forming
		 * 	the final ship probability grid.
		 */
		for (int cX = 0; cX < BattleshipSP.gridSizeX; cX++){
			for (int cY = 0; cY < BattleshipSP.gridSizeY; cY++){
				
				for (int p = 0; p < BattleshipSP.shipCount; p++){
					int lX = cX + BattleshipSP.shipSize[p][0] - 1, 
							lY = cY + BattleshipSP.shipSize[p][1] - 1;
					
					if (lX >= BattleshipSP.gridSizeX || lY >= BattleshipSP.gridSizeY) continue;
					
					boolean areaOccupied = false;
					
					for (int i = cX; i <= lX; i++)
						if (BattleshipSP.gridState[i][cY] != 0)
							areaOccupied = true;
					
					if (areaOccupied) continue;
					
					for (int i = cX; i <= lX; i++)
						prob[i][cY] += 1;
				}
				
				for (int p = 0; p < BattleshipSP.shipCount; p++){
					int lX = cX + BattleshipSP.shipSize[p][1] - 1, 
							lY = cY + BattleshipSP.shipSize[p][0] - 1;
					
					if (lX >= BattleshipSP.gridSizeX || lY >= BattleshipSP.gridSizeY) continue;
					
					boolean areaOccupied = false;
					
					for (int i = cY; i <= lY; i++)
						if (BattleshipSP.gridState[cX][i] != 0)
							areaOccupied = true;
					
					if (areaOccupied) continue;
					
					for (int i = cY; i <= lY; i++)
						prob[cX][i] += 1;
				}
				
			}
			
			
		}
		
		BattleshipSP.gridProbability = prob;
		
		/*
		 * 	Prints the final probability chart for debugging purposes
		 */
		System.out.println("[DEBUG]\tGrid Probability Updated");
		for (int i = 0; i < BattleshipSP.gridSizeY; i++){
			for (int j = 0; j < BattleshipSP.gridSizeX; j++)
				System.out.printf("%d\t", (int)BattleshipSP.gridProbability[j][i]);
			System.out.println();
		}
		
		System.out.println("\n\n");
	}
	
}
