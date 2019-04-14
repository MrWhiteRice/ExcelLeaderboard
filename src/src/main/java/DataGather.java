package src.main.java;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.google.api.services.drive.model.File;
import com.google.api.services.sheets.v4.model.ValueRange;

public class DataGather
{
    final static String range = "C6:F";
    public static boolean restart;
    public static boolean updateDrive = false;
	
	public static void GetData() throws IOException, GeneralSecurityException
	{
		if(!updateDrive)
		{
			System.out.println();
			return;
		}
		
		System.out.println("starting");
		updateDrive = false;
		restart = false;
		
    	GraphWindow.ta.setText("");
		//Date date1=formatter1.parse(sDate1); sdate1 = 12/2/2019
		
		//get all the files in the drive
        List<File> files = Main.files.getFiles();
        
        if(files != null)
        {
        	//cycle through all files
        	for(int x = 0; x < files.size(); x++)
            {
        		if(restart)
        		{
        			System.out.println("Restarting!");
        			return;
        		}
        		
        		String finalText = "";
        		if(x != files.size()-1)
        		{
        			finalText = " \n";
        		}
        		
                //get values
                ValueRange response = Main.sheets.spreadsheets().values()
                        .get(files.get(x).getId(), range)
                        .execute();
                
                List<List<Object>> values = response.getValues();
                
                String text = files.get(x).getName() + ": " + GetTotalTime(values) + " Hours" + finalText;
                GraphWindow.ta.setText(GraphWindow.ta.getText() + text);
                System.out.println(text);
                
                GraphWindow.f.repaint();
                GraphWindow.f.pack();
            }
        	
        	System.out.println("Done!");
        }
	}
    
	public static float GetTotalTime(List<List<Object>> values)
    {
		float hours = 0;
    	
    	for(int x = 0; x < values.size(); x++)
    	{
    		List<Object> val = values.get(x);
    		
    		if(val.toString() == "[]")
    		{
    			continue;
    		}
    		
    		String convert = val.toString();
    		convert = convert.substring(1, convert.length()-1);
    		
    		String[] split = convert.split(", ");
    		
    		if(split[0].isEmpty() || split.length < 4)
    		{
    			continue;
			}
    		
    		//0 = date
    		//1 = start time
    		//2 = end time
    		//3 = total minutes
    		
    		try
    		{
    			hours += Float.parseFloat(split[3]);
    		}
    		catch(NumberFormatException e)
    		{
    			continue;
    		}
    	}
    	
    	return hours/60;
    }
}
