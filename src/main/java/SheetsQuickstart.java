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
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

//Java
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SheetsQuickstart
{
    private static final String APPLICATION_NAME = "EXCEL LEADERBOARD FFS";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY + " " + DriveScopes.DRIVE_METADATA_READONLY);
    //private static final List<String> SCOPES_DRIVE = Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "client_secret_703587737148-ko5v1p4f27k6dj02l2p77o096j849g8f.apps.googleusercontent.com.json";

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
    
    public static float CalcHours(List<List<Object>> values)
    {
    	float hours = 0;
    	
    	for(int x = 0; x < values.size(); x++)
    	{
    		List<Object> val = values.get(x);
    		
    		String convert = val.toString();
    		convert = convert.substring(1, convert.length()-1);
    		
    		float con = 0;
    		try
    		{
    			con = Float.parseFloat(convert);
    		}
    		catch(NumberFormatException e)
    		{
    			con = 0;
    		}
    		
    		
    		hours += con;
    	}
    	
    	return hours/60;
    }
    
    public static void main(String... args) throws IOException, GeneralSecurityException
    {
    	//Class Data!F7:F
    	//class data = the sheet
    	//! = spacer
    	//F7:f = from f7 through all of f
    	
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1iFp5VTm5K12vopmJ5r8nU-3qyjmzxQ0bFkzrDZqkWYs";
        final String range = "F6:F";
        
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        /*ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        
        List<List<Object>> values = response.getValues();
        
        System.out.println(CalcHours(values));*/
        
        
        
        
        
        
        // Build a new authorized API client service.        
        Drive driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
        		.setApplicationName(APPLICATION_NAME)
        		.build();
        
        // Print the names and IDs for up to 10 files.
        FileList result = driveService.files().list()
                //.setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .setQ("'1YO4j2-zK1rRh9NJxRTeueOgMD2TDvqFA' in parents")
                .execute();
        List<File> files = result.getFiles();
                
        if (files == null || files.isEmpty())
        {
            System.out.println("No files found.");
        }
        else
        {
            System.out.println("Files:");
            for (File file : files)
            {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
                
                //get values
                ValueRange response = service.spreadsheets().values()
                        .get(file.getId(), range)
                        .execute();
                
                List<List<Object>> values = response.getValues();
                
                System.out.println(CalcHours(values));
            }
        }
    }
}