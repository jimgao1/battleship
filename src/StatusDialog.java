
import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class StatusDialog extends JDialog{
	
	public StatusDialog(JFrame parent, String title, String content){
		super(parent, title);
		
		this.setLocation(200, 200);
		this.setSize(500, 300);
		this.setResizable(false);
		this.setTitle("");
		this.setLayout(new BorderLayout());
		this.setModal(true);
		
		JTextArea message = new JTextArea();
		message.setText(content);
		message.setFont(new Font("Courier New", Font.PLAIN, 14));
		
		this.add(message);
		
		this.setVisible(true);
		
		this.repaint();
		this.revalidate();
	}
	
}
