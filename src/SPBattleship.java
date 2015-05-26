
public class SPBattleship {
	
	public static BattleshipGUI mainBattleshipGUI;
	
	public static int[][] playerPlacement = new int[10][10];
	public static int[][] opponentPlacement = new int[10][10];
	
	public static BattleshipGUI mainGUI = new BattleshipGUI();
	
	public void generateRandomPlacement(){
		for (int i=0; i<10; i++)
			for (int j=0; j<10; j++)
				opponentPlacement[i][j] = 0;
		
		for (int i=0; i<AlgorithmMain.shipCount; i++){
			int pX = (int)(Math.random() * 10), pY = (int)(Math.random() * 10);
			boolean vert = (Math.random() >= 0.5);
			boolean valid = true;
			
			if (vert){
				if (pY + AlgorithmMain.shipSize[i][0] - 1 < 10){
					for (int j=pY; j<pY + AlgorithmMain.shipSize[i][0]; j++)
						if (opponentPlacement[pX][j] != 0){
							valid = false;
							break;
						}
				} else {
					valid = false;
				}
				
				if (valid){
					for (int j=pY; j<pY + AlgorithmMain.shipSize[i][0]; j++)
						opponentPlacement[pX][j] = i + 1;
				} else {
					i--;
					continue;
				}
			} else {
				if (pX + AlgorithmMain.shipSize[i][0] - 1 < 10){
					for (int j=pX; j<pX + AlgorithmMain.shipSize[i][0]; j++)
						if (opponentPlacement[j][pY] != 0){
							valid = false;
							break;
						}
				} else {
					valid = false;
				}
				
				if (valid){
					for (int j=pX; j<pX + AlgorithmMain.shipSize[i][0]; j++)
						opponentPlacement[j][pY] = i + 1;
				} else {
					i--;
					continue;
				}
			}
		}
		
		System.out.println("[INFO]\tGenerated Opponent Placement");
		for (int i=0; i<10; i++){
			for (int j=0; j<10; j++)
				System.out.printf("%d ", opponentPlacement[j][i]);
			System.out.println();
		}
	}
	
	public SPBattleship(){
		generateRandomPlacement();
		
	}
}
