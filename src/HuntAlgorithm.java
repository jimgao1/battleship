

public class HuntAlgorithm{
	
	public static void recalculate(){
		
		double[][] prob = new double[AlgorithmMain.gridSizeX][AlgorithmMain.gridSizeY];
		
		for (int i = 0; i < AlgorithmMain.gridSizeX; i++)
			for (int j = 0; j < AlgorithmMain.gridSizeY; j++)
				prob[i][j] = 0;
		
		for (int cX = 0; cX < AlgorithmMain.gridSizeX; cX++){
			for (int cY = 0; cY < AlgorithmMain.gridSizeY; cY++){
				
				for (int p = 0; p < AlgorithmMain.shipCount; p++){
					int lX = cX + AlgorithmMain.shipSize[p][0] - 1, 
							lY = cY + AlgorithmMain.shipSize[p][1] - 1;
					
					if (lX >= AlgorithmMain.gridSizeX || lY >= AlgorithmMain.gridSizeY) continue;
					
					boolean areaOccupied = false;
					
					for (int i = cX; i <= lX; i++)
						if (AlgorithmMain.gridState[i][cY] != 0)
							areaOccupied = true;
					
					if (areaOccupied) continue;
					
					for (int i = cX; i <= lX; i++)
						prob[i][cY] += 1;
				}
				
				for (int p = 0; p < AlgorithmMain.shipCount; p++){
					int lX = cX + AlgorithmMain.shipSize[p][1] - 1, 
							lY = cY + AlgorithmMain.shipSize[p][0] - 1;
					
					if (lX >= AlgorithmMain.gridSizeX || lY >= AlgorithmMain.gridSizeY) continue;
					
					boolean areaOccupied = false;
					
					for (int i = cY; i <= lY; i++)
						if (AlgorithmMain.gridState[cX][i] != 0)
							areaOccupied = true;
					
					if (areaOccupied) continue;
					
					for (int i = cY; i <= lY; i++)
						prob[cX][i] += 1;
				}
				
			}
			
		}
		
		AlgorithmMain.gridProbability = prob;
		
		System.out.println("[DEBUG]\tGrid Probability Updated");
		for (int i = 0; i < AlgorithmMain.gridSizeY; i++){
			for (int j = 0; j < AlgorithmMain.gridSizeX; j++)
				System.out.printf("%d[%d]\t", (int)AlgorithmMain.gridProbability[j][i], AlgorithmMain.gridState[j][i]);
			System.out.println();
		}
		
		System.out.println("\n\n");
	}
	
}
