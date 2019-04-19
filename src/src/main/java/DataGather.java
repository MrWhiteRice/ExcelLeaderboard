package src.main.java;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import com.google.api.services.drive.model.File;
import com.google.api.services.sheets.v4.model.ValueRange;

public class DataGather
{
    final String range = "C6:F";
    public boolean restart;
    public boolean updateDrive;
    public boolean done;
	
	public void GetData() throws IOException, GeneralSecurityException
	{
		if(!updateDrive)
		{
			//System.out.println();
			return;
		}
		
		System.out.println("starting");
		updateDrive = false;
		restart = false;
		done = false;
		
		//GraphWindow.WaitScreen();
    	//GraphWindow.ta.setText(""); /////FIX
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
        		
        		System.out.println(x);
        		
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
                
                //GraphWindow.students.add(GetStudentData(values, files.get(x).getName()));
                
                //String text = files.get(x).getName() + ": " + String.format("%.2f", (GraphWindow.students.get(x).totalTime/60)) + " Hours" + finalText;
                //GraphWindow.ta.setText(GraphWindow.ta.getText() + text);
                //System.out.println(text);
                
                //GraphWindow.f.repaint();
                //GraphWindow.f.pack();
            }
        	
        	System.out.println("Done!");
        	//GraphWindow.DoneScreen();
        	done = true;
        }
	}
	
	public StudentData GetStudentData(List<List<Object>> values, String name)
    {
		String[] dates = new String[values.size()];
		String[] startTimes = new String[values.size()];;
		String[] endTimes = new String[values.size()];;
		String[] times = new String[values.size()];;
		float totalTime = 0;
    	
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
    		
    		dates[x] = split[0];
    		startTimes[x] = split[1];
    		endTimes[x] = split[2];
    		times[x] = split[3];
    		
    		try
    		{
    			totalTime += Float.parseFloat(split[3]);
    		}
    		catch(NumberFormatException e)
    		{
    			continue;
    		}
    	}
    	
    	return new StudentData(dates, startTimes, endTimes, times, name, dates.length, totalTime);
    }
}
