import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class BattleshipSP{
	
	public static class AIThread extends Thread{
		
		/*
		 * 	Algorithm Constants
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
		 * 	Grid Variables
		 */
		public static int[][] gridState = new int[gridSizeX][gridSizeY];
		public static int[][] opponentState = new int[gridSizeX][gridSizeY];
		public static double[][][] killProb = new double[5][10][10];
		public static boolean[] shipSank = new boolean[shipCount];
		
		/*
		 * 	Socket objects
		 */
		
		public ServerSocket serverSock;
		public Socket sock;
		
		public BufferedReader reader;
		public PrintWriter writer;
		
		public AIThread(){}
		
		public void run(){
			try{
				serverSock = new ServerSocket(8001);
				
				sock = serverSock.accept();
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				writer = new PrintWriter(sock.getOutputStream(), true);
				
			} catch (IOException ex){
				System.out.println("[ERROR]\tAI Server Error: " + ex.getLocalizedMessage());
				JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
			}
			
			while(true){
				int maxLocationX = -1, maxLocationY = -1;
				
				
			}
		}
		
	}
	
	public BattleshipSP(){
		new AIThread();
		
		BattleshipMP multi = new BattleshipMP("localhost", 8001, true);
	}
	
}
