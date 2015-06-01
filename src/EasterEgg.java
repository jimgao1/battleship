import javax.swing.JFrame;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class EasterEgg{

	public static final int windowCount = 100;
	
	public static class TrollThread extends JFrame implements Runnable
	{
	  public int randomX()
	  {
	    return (int)(Math.random() * 1920.0D) + 1;
	  }
	  
	  public int randomY()
	  {
	    return (int)(Math.random() * 1080.0D) + 1;
	  }
	  
	  public TrollThread()
	  {
	    setSize(400, 200);
	    setDefaultCloseOperation(1);
	    setLayout(null);
	    setLocation(randomX(), randomY());
	    
	    JLabel l = new JLabel("TEAM NULL FTW");
	    l.setFont(new Font("Courier New", 1, 40));
	    l.setBounds(0, 0, 780, 180);
	    add(l);
	    
	    
	  }
	  
	  public void run()
	  {
	    setVisible(true);
	  }
	}
	
	public EasterEgg(){
		for (int i=0; i<windowCount; i++)
			new Thread(new TrollThread()).start();
	}

}
