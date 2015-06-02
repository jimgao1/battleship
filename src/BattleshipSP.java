import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class BattleshipSP {

	public static class AIThread extends Thread {

		public static final int gridSizeX = 10;
		public static final int gridSizeY = 10;

		public static final int shipCount = 5;
		public static final int[][] shipSize = { { 2, 1 }, { 3, 1 }, { 3, 1 },
				{ 4, 1 }, { 5, 1 } };

		public static final String[] outcomes = { "Miss", "Hit", "Sank" };

		public static final String[] shipNames = { "Destroyer", "Submarine",
				"Cruiser", "Battleship", "Carrier" };

		/*
		 * Algorithm Variables
		 */

		/*
		 * int[][] gridState: the status of the cells in the grid
		 * 
		 * 0 Default 1 Hit (current round) 2 Hit (previous round) 3 Miss
		 */

		public static int[][] gridState = new int[gridSizeX][gridSizeY];
		public static int[][] gridProbability = new int[gridSizeX][gridSizeY];

		public static boolean[] shipSank = new boolean[shipCount];
		public static boolean[] shipHit = new boolean[shipCount];
		public static boolean killModeEngaged = false;

		public static int[][] computerGrid = new int[10][10];
		public static boolean computerTurn = false;

		/*
		 * Socket objects
		 */

		public ServerSocket serverSock;
		public Socket sock;

		public BufferedReader reader;
		public PrintWriter writer;

		public AIThread() {
		}

		/*
		 * Method that generates a random ship placement, used in testing, as
		 * well as single player games.
		 */
		public void generateRandomPlacement() {
			int[][] randomGrid = new int[10][10];

			for (int i = 0; i < 10; i++)
				for (int j = 0; j < 10; j++)
					randomGrid[i][j] = 0;

			for (int i = 0; i < AlgorithmMain.shipCount; i++) {
				int pX = (int) (Math.random() * 10), pY = (int) (Math.random() * 10);
				boolean vert = (Math.random() >= 0.5);
				boolean valid = true;

				if (vert) {
					if (pY + AlgorithmMain.shipSize[i][0] - 1 < 10) {
						for (int j = pY; j < pY + AlgorithmMain.shipSize[i][0]; j++)
							if (randomGrid[pX][j] != 0) {
								valid = false;
								break;
							}
					} else {
						valid = false;
					}

					if (valid) {
						for (int j = pY; j < pY + AlgorithmMain.shipSize[i][0]; j++)
							randomGrid[pX][j] = i + 1;
					} else {
						i--;
						continue;
					}
				} else {
					if (pX + AlgorithmMain.shipSize[i][0] - 1 < 10) {
						for (int j = pX; j < pX + AlgorithmMain.shipSize[i][0]; j++)
							if (randomGrid[j][pY] != 0) {
								valid = false;
								break;
							}
					} else {
						valid = false;
					}

					if (valid) {
						for (int j = pX; j < pX + AlgorithmMain.shipSize[i][0]; j++)
							randomGrid[j][pY] = i + 1;
					} else {
						i--;
						continue;
					}
				}
			}

			System.out.println("[INFO]\tGenerated Opponent Placement");
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++)
					System.out.printf("%d ", randomGrid[j][i]);
				System.out.println();
			}

			computerGrid = randomGrid;

		}

		public void run() {
			try {
				serverSock = new ServerSocket(8001);

				sock = serverSock.accept();
				reader = new BufferedReader(new InputStreamReader(
						sock.getInputStream()));
				writer = new PrintWriter(sock.getOutputStream(), true);

			} catch (IOException ex) {
				System.out.println("[ERROR]\tAI Server Error: "
						+ ex.getLocalizedMessage());
				JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
			}
			try {
				while (true) {
					System.out.println("[INFO]\tAI: " + computerTurn);
					
					if (computerTurn) {
						int maxLocationX = -1;
						int maxLocationY = -1;
						if (killModeEngaged) {
							System.out.println("[INFO]\t***KILL MODE ENGAGED***");
							String s=(String)JOptionPane.showInputDialog(null,"Which ship is sank?","",JOptionPane.QUESTION_MESSAGE,null,outcomes,"");
							KillAlgorithm.recalculate();
							
							this.gridProbability = KillAlgorithm.gridProb;

							double maxProb = -1.0D;
							for (int i = 0; i < 10; i++) {
								for (int j = 0; j < 10; j++) {
									if ((gridProbability[i][j] > maxProb) && (gridState[i][j] == 0)) {
										maxProb = gridProbability[i][j];
										maxLocationX = i;
										maxLocationY = j;
									}
								}
							}
							System.out.println("[INFO]\tSuggested Location ("
									+ (maxLocationX + 1) + ", "
									+ (maxLocationY + 1) + ") ");
							JOptionPane.showMessageDialog(null,
									"Suggested Location (" + (maxLocationX + 1)
											+ ", " + (maxLocationY + 1) + ") ");
							
							writer.println(maxLocationX + " " + maxLocationY);
						} else {
							HuntAlgorithm.recalculate();
							
							this.gridProbability = HuntAlgorithm.huntProb;

							double maxProb = -1.0D;
							for (int i = 0; i < 10; i++) {
								for (int j = 0; j < 10; j++) {
									if ((gridProbability[i][j] > maxProb)
											&& (gridState[i][j] == 0)) {
										maxProb = gridProbability[i][j];
										maxLocationX = i;
										maxLocationY = j;
									}
								}
							}
							System.out.println("[INFO]\tSuggested Location ("
									+ (maxLocationX + 1) + ", "
									+ (maxLocationY + 1) + ") ");
							JOptionPane.showMessageDialog(null,
									"Suggested Location (" + (maxLocationX + 1)
											+ ", " + (maxLocationY + 1) + ") ");
							
							writer.println(maxLocationX + " " + maxLocationY);
						}

						String response = reader.readLine();
						String s = "";

						if (response.equals("miss")) {
							s = "Miss";
						} else {
							int shipID = Integer.parseInt(response);
							boolean sank = reader.readLine().equals("true");

							if (sank)
								s = "Sank";
							else
								s = "Hit";
						}

						if (killModeEngaged) {
							if (s.equals("Sank")) {
								gridState[maxLocationX][maxLocationY] = 1;
								for (int i = 0; i < 10; i++) {
									for (int j = 0; j < 10; j++) {
										if (gridState[i][j] == 1) {
											gridState[i][j] = 2;
										}
									}
								}
								killModeEngaged = false;
							} else if (s.equals("Hit")) {
								gridState[maxLocationX][maxLocationY] = 1;
							} else if (s.equals("Miss")) {
								gridState[maxLocationX][maxLocationY] = 3;
							}
						} else if (s.equals("Hit")) {
							for (int i = 0; i < 10; i++) {
								for (int j = 0; j < 10; j++) {
									if (gridState[i][j] == 1) {
										gridState[i][j] = 2;
									}
								}
							}
							gridState[maxLocationX][maxLocationY] = 1;
							killModeEngaged = true;
						} else {
							gridState[maxLocationX][maxLocationY] = 3;
						}
					} else {
						try {
							String loc = reader.readLine();
							
							System.out.println("[INFO]\tAI: player location: "+ loc);

							int locationX = Integer.parseInt(loc.split(" ")[0]);
							int locationY = Integer.parseInt(loc.split(" ")[1]);

							if (computerGrid[locationX][locationY] > 0) {
								writer.println(Integer
										.toString(computerGrid[locationX][locationY]));

								int c = 0;
								for (int i = 0; i < 10; i++)
									for (int j = 0; j < 10; j++)
										if (computerGrid[i][j] == computerGrid[locationX][locationY])
											c++;

								if (c == 1) {
									writer.println("true");
								} else {
									writer.println("false");
								}

								computerGrid[locationX][locationY] = 0;
							} else {
								writer.println("miss");
							}

						} catch (Exception ex) {
						}
					}

					computerTurn = !computerTurn;

				}

			} catch (Exception ex) {

			}

		}
	}

	public BattleshipSP() {
		new Thread(new AIThread()).start();
		
		
		BattleshipMP multi = new BattleshipMP("localhost", 8001, true);
	}

}
