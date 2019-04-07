import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GraphWindow
{
	private JFrame f; //Main frame
	private JTextArea ta; // Text area
    private JScrollPane sbrText; // Scroll pane for text area
    private JButton btnQuit; // Quit Program
    
	public GraphWindow()
	{
		// Create Frame
		f = new JFrame("Excell Leaderboard");
		f.getContentPane().setLayout(new FlowLayout());
		f.setResizable(false);
		f.setPreferredSize(new Dimension(1920/2, 1080/2));
		
		
		
		
		
		
		
		
		
		
		
		// Create Scrolling Text Area in Swing
		ta = new JTextArea("", 10, 30);
		ta.setLineWrap(true);
		
		sbrText = new JScrollPane(ta);
        sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        sbrText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		       
        // Create Get Button
        btnQuit = new JButton("Get Data");
        btnQuit.addActionListener
        (
    		new ActionListener()
    		{
    			public void actionPerformed(ActionEvent e)
    			{
    				//System.exit(0);
    				if(btnQuit.getText() != "Refresh")
    				{
    					btnQuit.setText("Refresh");
    				}
    				
                }
    		}
		);
	}
	
	public void launchFrame() // Create Layout
	{
		// Add text area and button to frame
        f.getContentPane().add(sbrText);
        f.getContentPane().add(btnQuit);
        
        // Close when the close button is clicked
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        //Display Frame
        f.pack(); // Adjusts frame to size of components
        f.setVisible(true);
        
        //centre location
        f.setLocationRelativeTo(null);
    }
}