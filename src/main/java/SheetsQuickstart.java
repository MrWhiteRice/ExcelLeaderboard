//Google API
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

//Sheets API
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

//Drive API
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

//Java
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class SheetsQuickstart
{
    private static final String APPLICATION_NAME = "EXCEL LEADERBOARD BY WHITERICE";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    final static String range = "C6:F";
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY + " " + DriveScopes.DRIVE_METADATA_READONLY);
    //private static final List<String> SCOPES_DRIVE = Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException
    {    	
        // Load client secrets.
        InputStream in = SheetsQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
    
    public static void main(String... args) throws IOException, GeneralSecurityException
    {
    	//GraphWindow win = new GraphWindow();
        //win.launchFrame();
    	
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        //Date date1=formatter1.parse(sDate1); sdate1 = 12/2/2019

        // Build a new authorized API client service.
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        
        Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
        		.setApplicationName(APPLICATION_NAME)
        		.build();
        
        // get all the files in drive
        FileList result = driveService.files().list()
                .setFields("nextPageToken, files(id, name)")
                .setQ("'1YO4j2-zK1rRh9NJxRTeueOgMD2TDvqFA' in parents")
                .execute();
        List<File> files = result.getFiles();
        
        if(files != null || !files.isEmpty())
        {
        	//cycle through all files
        	for(File file : files)
            {
                System.out.printf("%s \n", file.getName());
                
                //get values
                ValueRange response = service.spreadsheets().values()
                        .get(file.getId(), range)
                        .execute();
                
                List<List<Object>> values = response.getValues();
                
                System.out.println(GetTotalTime(values));
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
    		//System.out.println(convert);
    		
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