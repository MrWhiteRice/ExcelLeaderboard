import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GraphWindow
{
	public static JFrame f; //Main frame
	public static JTextArea ta; // Text area
    private JScrollPane sbrText; // Scroll pane for text area
    private JButton btnQuit; // Quit Program
    private JLabel wait;
    
    private ArrayList<StudentData> students = new ArrayList<StudentData>();
        
	public GraphWindow() throws IOException, GeneralSecurityException 
	{
		// Create Frame
		f = new JFrame("Excell Leaderboard");
		f.getContentPane().setLayout(new FlowLayout());
		f.setResizable(false);
		f.setPreferredSize(new Dimension(1920/2, 1080/2));
		
		wait = new JLabel("");
		
		// Create Scrolling Text Area in Swing
		ta = new JTextArea("", 10, 50);
		ta.setLineWrap(true);
		ta.setEditable(false);
		
		sbrText = new JScrollPane(ta);
        sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sbrText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		       
        // Create Get Button
        btnQuit = new JButton("Refresh");
        btnQuit.addActionListener
        (
    		new ActionListener()
    		{
    			public void actionPerformed(ActionEvent e)
    			{
    				System.out.println("Ping!");
    				//GetData();
				}
    		}
		);
	}
	
	void GetData()
	{
		WaitScreen();
		
		//GetData
        try
		{
			DataGather.GetData(ta, f);
		}
        catch(IOException e){}
        catch(GeneralSecurityException e){}
        
        DoneScreen();
	}
	
	public void launchFrame() // Create Layout
	{
		// Close when the close button is clicked
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Add text area and button to frame
        f.getContentPane().add(sbrText);
        f.getContentPane().add(btnQuit);
        f.getContentPane().add(wait);
        
        //Display Frame
        f.pack(); // Adjusts frame to size of components
        f.setVisible(true);
        
        //centre location
        f.setLocationRelativeTo(null);
        
        WaitScreen();
    }
	
	public void WaitScreen()
	{
		wait.setText("Please wait while we gather the data... :)");
		f.repaint();
		f.pack();
	}
	
	public void DoneScreen()
	{
		wait.setText("Thanks for waiting! :)");
		f.repaint();
		f.pack();
	}
	
	public class StudentData
	{
		public String[] dates;
		public String[] times;
	}
}