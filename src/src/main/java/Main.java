package src.main.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import javax.swing.SwingUtilities;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

public class Main
{
    public static final String APPLICATION_NAME = "EXCEL LEADERBOARD BY WHITERICE";
    public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    public static final String TOKENS_DIRECTORY_PATH = "tokens";
    //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
	public static NetHttpTransport HTTP_TRANSPORT = null;
	/**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY + " " + DriveScopes.DRIVE_METADATA_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/src/main/resources/credentials.json";
    
    public static Sheets sheets;
    public static Drive drive;
    public static FileList files;
    static GraphWindow win;
    
    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException
    {
        // Load client secrets.
        InputStream in = Main.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
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
	
    public static void main(String... args) throws GeneralSecurityException, IOException
    {
    	HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    	
    	// Build a new authorized API client service.
        sheets = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        
        drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
        		.setApplicationName(APPLICATION_NAME)
        		.build();
    	
        // get all the files in drive
        files = Main.drive.files().list()
                .setFields("nextPageToken, files(id, name)")
                .setQ("'1YO4j2-zK1rRh9NJxRTeueOgMD2TDvqFA' in parents")
                .execute();
        
        //create window
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
            	GraphWindow win = new GraphWindow();
            }
        });
        
        
        
        /*win = null;
        
        SwingUtilities.invokeLater(new Runnable()
        {
        	@Override
        	public void run()
        	{
				try
				{
					win = new GraphWindow();
				}
				catch(IOException | GeneralSecurityException e){}
				
        		//win.launchFrame();
        	}
        });*/
    }
}