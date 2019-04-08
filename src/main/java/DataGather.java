import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.swing.JTextArea;

import com.google.api.services.drive.model.File;
import com.google.api.services.sheets.v4.model.ValueRange;

public class DataGather
{
    final static String range = "C6:F";
    
	public static void GetData(JTextArea ta) throws IOException, GeneralSecurityException
	{
        //Date date1=formatter1.parse(sDate1); sdate1 = 12/2/2019
		
		//get all the files in the drive
        List<File> files = Main.files.getFiles();
        
        if(files != null || !files.isEmpty())
        {
        	//cycle through all files
        	for(File file : files)
            {
                //get values
                ValueRange response = Main.sheets.spreadsheets().values()
                        .get(file.getId(), range)
                        .execute();
                
                List<List<Object>> values = response.getValues();
                
                String text = file.getName() + ": " + GetTotalTime(values) + " Hours \n";
                ta.setText(ta.getText() + text);
            }
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
