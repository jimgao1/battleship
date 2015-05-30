import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class BattleshipSP{
	
	public static class AIThread extends Thread{
		
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
		}
		
	}
	
	public BattleshipSP(){
		
	}
	
}
