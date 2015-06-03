/*
 * 	[KillAlgorithm.java]
 * 
 * 	Author: Philip Huang, Jim Gao, Joseph Zhang
 * 	Purpose: The hunt algorithm, used when the computer is SOMEWHAT CERTAIN 
 * 			about the user's placement, and is searching for the most probable
 * 			location around the existing hits
 */

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
	
	/*
	 * 	Consults the grid from BattleshipSP, and makes the corresponding calculations
	 * 	about the probability of placement in a specific cell
	 */
	public static void recalculate(){
		
		KillAlgorithm.killGrid = (int[][])BattleshipSP.gridState.clone();
		
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
		
		// loop through all the points
		for (int cX = 0; cX < BattleshipSP.gridSizeX; cX++){
			for (int cY = 0; cY < BattleshipSP.gridSizeY; cY++){
				
				
				for (int p = 0; p < BattleshipSP.shipCount; p++){
					if (!BattleshipSP.shipSank[p]){// if the ship has not been sank
						
						//horizontal placement
						int lX = cX + BattleshipSP.shipSize[p][0] - 1, 
								lY = cY + BattleshipSP.shipSize[p][1] - 1;
						
						//check out of bound
						if (lX >= BattleshipSP.gridSizeX || lY >= BattleshipSP.gridSizeY) continue;
						
						//declare boolean conditions to determine if the grid probability is viable
						boolean hitIncluded = false;
						boolean missIncluded = false;
						
						int nHitIncluded = 0;
						
						//check if the selected area has successful hit on the ship
						for (int i = cX; i <= lX; i++){
							if (killGrid[i][cY] == 1){
								hitIncluded = true;
								nHitIncluded += 1;
							}
							
						//check if the selected area has previous miss or sank ship
							if (killGrid[i][cY] == 2 || killGrid[i][cY] == 3){
								missIncluded = true;
								break;
							}
						}
						
						
						if (!hitIncluded || missIncluded) continue;
						
						// if the selected area contains successful hit on the ship but not previous hit or sank, then update
						for (int i = cX; i <= lX; i++)
							prob[i][cY] += nHitIncluded;
					}
				}
				
				for (int p = 0; p < BattleshipSP.shipCount; p++){
					if (!BattleshipSP.shipSank[p]){
						//vertical placement
						int lX = cX + BattleshipSP.shipSize[p][1] - 1, 
								lY = cY + BattleshipSP.shipSize[p][0] - 1;
						
						if (lX >= BattleshipSP.gridSizeX || lY >= BattleshipSP.gridSizeY) continue;
						
						//declare boolean conditions to determine if the grid probability is viable
						boolean hitIncluded = false;
						boolean missIncluded = false;
						
						int nHitIncluded = 0;
						
						//check if the selected area has successful hit on the ship
						for (int i = cY; i <= lY; i++){
							if (killGrid[cX][i] == 1){
								hitIncluded = true;
								nHitIncluded += 1;
							}
							
							//check if the selected area has previous miss or sank ship
							if (killGrid[cX][i] == 2 || killGrid[cX][i] == 3){
								missIncluded = true;
								break;
							}
						}
						
						if ((!hitIncluded) || missIncluded) continue;
						// if the selected area contains successful hit on the ship but not previous hit or sank, then update
						for (int i = cY; i <= lY; i++)
							prob[cX][i] += nHitIncluded;
					}
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
