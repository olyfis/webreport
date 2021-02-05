
package com.olympus.webreport;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import org.apache.http.impl.client.HttpClients;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;
 
import org.json.JSONArray;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.olympus.webreport.*;
public class Main {

	static private String USERNAME = null;
	static private String PASSWORD = null;
	static private String LOGINURL = null;
	static private String GRANTSERVICE = null;
	static private  String CLIENTID = null;
	static private String CLIENTSECRET = null;
	private static String REST_ENDPOINT = null;
    private static String API_VERSION = null;
    private static String baseUri;

   // private static Header oauthHeader;
   // private static Header prettyPrintHeader = new BasicHeader("X-PrettyPrint", "1");
    private static String leadId ;
    private static String leadFirstName;
    private static String leadLastName;
    private static String leadCompany;
    private static String leadEmail;
    private static String leadPhone;
    private static String leadPostal;
    /**********************************************************************************************************/  	
    public static boolean isNumeric(String str) {
		return str.matches("[+-]?\\d*(\\.\\d+)?");
	}
    /**********************************************************************************************************/  
    public static void getProperties(String props) {
		FileInputStream fis = null;
		Properties keyProps = new Properties();
		
		try {
			fis = new FileInputStream(props);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
		try {
			keyProps.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		USERNAME = (String) keyProps.get("USERNAME");
		PASSWORD = (String) keyProps.get("PASSWORD");
		CLIENTID = (String) keyProps.get("CLIENTID");
		CLIENTSECRET = (String) keyProps.get("CLIENTSECRET");
		GRANTSERVICE = (String) keyProps.get("GRANTSERVICE");
		LOGINURL = (String) keyProps.get("LOGINURL");
		REST_ENDPOINT = (String) keyProps.get("REST_ENDPOINT");
		API_VERSION = (String) keyProps.get("API_VERSION");
	}

	public static void main(String[] args) {
		
		SFLeadQuery lq = new SFLeadQuery();
		
		String token = new String();	
		String loginURL = new String();
		String uri = new String();	
		String props = "C:\\Java_Dev\\props\\salesforce\\key.properties";
		//System.out.println("\n_______________ Lead QUERY _______________");
		getProperties(props);
	
		  System.out.println("**** CLIENTID=" + CLIENTID);
		  System.out.println("**** CLIENTSECRET=" + CLIENTSECRET);
		  System.out.println("**** USERNAME=" + USERNAME);
		  System.out.println("**** PASSWORD=" + PASSWORD);
		  System.out.println("**** LOGINURL=" + LOGINURL);
		  System.out.println("**** GRANTSERVICE=" + GRANTSERVICE);
		  System.out.println("**** REST_ENDPOINT=" + REST_ENDPOINT);
		System.out.println("**** API_VERSION=" + API_VERSION);
	/*
		// Assemble the login request URL
		loginURL = LOGINURL + GRANTSERVICE + "&client_id=" + CLIENTID + "&client_secret=" + CLIENTSECRET
				+ "&username=" + USERNAME + "&password=" + PASSWORD;
		// System.out.println(loginURL);
		token = lq.getAccessToken(loginURL);
		System.out.println("Access token: " + token);
		// end getting oAuth token
 		
		//uri = baseUri + "/query?q=Select+Id,FirstName,LastName,Company,Email,Phone,PostalCode+From+Lead+where+id=+'00Q3C000001npDbUAI'";
		uri = baseUri + "/query?q=Select+Id,FirstName,LastName,Company,Email,Phone,PostalCode+From+Lead";	
		System.out.println("URI:" + uri);
		lq.queryLeads(uri);
		// createLeads();
		// updateLeads();
		// queryLeads();	
		 
		 */
		
		String n = ".123456";
		System.out.println("IsNumeric: " + isNumeric(n));
		
		
	} // End Main
	
	
	
	
}
