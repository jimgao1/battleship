import java.util.ArrayList;
import java.util.Scanner;

public class KillAlgorithm {

	/*
	 * int[][] killGrid: Status on killAlgorithm
	 * 
	 * 0 Default 1 Visited - Hit 2 Visited - Miss
	 */
	public static int[][] killGrid=new int[10][10];
	public static int[][] prob=new int[10][10];
	public static int[] shipLength={
		2,3,3,4,5
	};
	public static void init() {
		killGrid = new int[AlgorithmMain.gridSizeX][AlgorithmMain.gridSizeY];
		
	}

	public static int[] recalculate() {
		int[] ans=new int[2];
		killGrid=(int [][])AlgorithmMain.gridState.clone();
		Scanner c=new Scanner(System.in);
		int x,y,ind;
		System.out.println("x coord for last user input");
		x= c.nextInt();
		System.out.println("y coord for last user input");
		y=c.nextInt();
		ArrayList<Integer> shiporder=new ArrayList<Integer>();
		ind=shiporder.get(shiporder.size()-1);
		int u[]={1,-1,0,0};
		int v[]={0,0,1,-1};
		for(int i=0;i<4;i++)
		{	
			if((x+u[i]>9)||(x+u[i]<0)||(y+v[i]>9)||(y+v[i]<0)) continue;
			if(killGrid[x+u[i]][y+v[i]]==ind)
			{	
				ans[0]=x-u[i];
				ans[1]=y-v[i];
				return ans;
			}
		}
		for(int i=0;i<4;i++)
		{	
			boolean go=true;
			for(int j=1;j<=shipLength[ind];j++)
			{	
				if((x+u[i]*j>9)||(x+u[i]*j<0)||(y+v[i]*j>9)||(y+v[i]*j<0)) continue;
				if(!(killGrid[x+u[i]*j][y+v[i]*j]==0||killGrid[x+u[i]*j][y+v[i]*j]==ind))
				{	
					go=false;
					break;
				}
			}
			if(go){
				ans[0]=x+u[i];
				ans[1]=y+v[i]; 
				return ans;
			}
		}
		 return ans;
	}


}
