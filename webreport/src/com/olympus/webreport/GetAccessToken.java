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
	 
	import org.apache.http.entity.StringEntity;
	import org.apache.http.impl.client.HttpClients;
	import org.apache.http.message.BasicHeader;
	import org.apache.http.Header;
	 
	import org.apache.http.HttpResponse;
	import org.apache.http.HttpStatus;
	import org.apache.http.util.EntityUtils;
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

	public class GetAccessToken  extends HttpServlet{
		
		static String USERNAME = null;
		static String PASSWORD = null;
		static String LOGINURL = null;
		static String GRANTSERVICE = null;
		static String CLIENTID = null;
		static String CLIENTSECRET = null;
		static String REST_ENDPOINT = null;
		static String API_VERSION = null;
		static String baseUri = null;
		  
		static String leadId;
		
		
	/**********************************************************************************************************/  
		public static void getProperties(String propFile) {
			FileInputStream fis = null;
			Properties keyProps = new Properties();

			try {
				fis = new FileInputStream(propFile);
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
		
	/*****************************************************************************************************/
	// getBody method
	/*****************************************************************************************************/
		private static String getBody(InputStream inputStream) {
		    String result = "";
		    try {
		        BufferedReader in = new BufferedReader(
		                new InputStreamReader(inputStream)
		        );
		        String inputLine;
		        while ( (inputLine = in.readLine() ) != null ) {
		            result += inputLine;
		            result += "\n";
		        }
		        in.close();
		    } catch (IOException ioe) {
		        ioe.printStackTrace();
		    }
		    return result;
		}

		/*********************************************************************************************************************************/
	//method to print array
		/*********************************************************************************************************************************/
		public static void printStrArray(ArrayList<String> strArr, String tag) {
			for (String str : strArr) { // iterating ArrayList
				System.out.println(tag + str);
			}
			// System.out.println(names[index]);
		}

		/*****************************************************************************************************/
		// Get Access oAuth2 token
		/**********************************************************************************************************/
		public static String getAccessToken(String loginURL) {
			String loginAccessToken = null;

			HttpClient httpclient = HttpClients.createDefault();
			// Login requests must be POSTs
			HttpPost httpPost = new HttpPost(loginURL);
			HttpResponse response = null;

			try {
				// Execute the login POST request
				response = httpclient.execute(httpPost);
			} catch (ClientProtocolException cpException) {
				// Handle protocol exception
			} catch (IOException ioException) {
				// Handle system IO exception
			}

			// verify response is HTTP OK
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				System.out.println("Error authenticating to Force.com: " + statusCode);
				// Error is in EntityUtils.toString(response.getEntity())
				return "No Connection";
			}
			String getResult = null;
			try {
				getResult = EntityUtils.toString(response.getEntity());
			} catch (IOException ioException) {
				// Handle system IO exception
			}
			JSONObject jsonObject = null;
			String loginInstanceUrl = null;

			try {
				jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();
				loginAccessToken = jsonObject.getString("access_token");
				loginInstanceUrl = jsonObject.getString("instance_url");
			} catch (JSONException jsonException) {
				// Handle JSON exception
			}
			/*
			System.out.println(response.getStatusLine());
			System.out.println("Successful login");
			System.out.println("  instance URL: " + loginInstanceUrl);
			System.out.println("  access token/session ID: " + loginAccessToken);
		*/	
			// New code
			baseUri = loginInstanceUrl + REST_ENDPOINT + API_VERSION;
			Header oauthHeader = new BasicHeader("Authorization", "OAuth " + loginAccessToken);
			/*
			 * System.out.println("oauthHeader1: " + oauthHeader); System.out.println("\n" +
			 * response.getStatusLine()); System.out.println("Successful login");
			 * System.out.println("instance URL: " + loginInstanceUrl);
			 */
			// System.out.println("access token/session ID: " + loginAccessToken);
			System.out.println("***** baseUri: " + baseUri);
			// release connection

			httpPost.releaseConnection();

			return loginAccessToken;
		}

		/*****************************************************************************************************/
	// Service method
		/*****************************************************************************************************/
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

			String loginURL = new String();
			String token = new String();

			String propFile = "C:\\Java_Dev\\props\\salesforce\\key.properties";
			// System.out.println("\n_______________ Lead QUERY _______________");
			getProperties(propFile);
			/*
			System.out.println("**** CLIENTID=" + CLIENTID);
			System.out.println("**** CLIENTSECRET=" + CLIENTSECRET);
			System.out.println("**** USERNAME=" + USERNAME);
			System.out.println("**** PASSWORD=" + PASSWORD);
			System.out.println("**** LOGINURL=" + LOGINURL);
			System.out.println("**** GRANTSERVICE=" + GRANTSERVICE);
			System.out.println("**** REST_ENDPOINT=" + REST_ENDPOINT);
			System.out.println("**** API_VERSION=" + API_VERSION);
			 */
			// Assemble the login request URL
			loginURL = LOGINURL + GRANTSERVICE + "&client_id=" + CLIENTID + "&client_secret=" + CLIENTSECRET + "&username="
					+ USERNAME + "&password=" + PASSWORD;
			// System.out.println(loginURL);
			token = getAccessToken(loginURL);
			System.out.println("Access token: " + token);
			// end getting oAuth token
	 		
			/*--------------------------------------------------------------------------------------------------------------------------------------*/

			/*--------------------------------------------------------------------------------------------------------------------------------------*/

		
				ArrayList<String> strArr = new ArrayList<String>();
			   
				PrintWriter out = res.getWriter();
				res.setContentType("text/plain");
				out.write(token);
			 
			 /*
				String paramName = "date";
				String paramValue = req.getParameter(paramName);
				//System.out.println("pName=" + paramName + " pVal=" + paramValue + "--");
				strArr = getBookingData(paramValue);
				//JUtils jutil = new JUtils();
				jutil.printStrArray(strArr);
				
				req.getSession().setAttribute("strArr", strArr);
				req.getSession().setAttribute(paramName, paramValue);
		        req.getRequestDispatcher("/gettoken.jsp").forward(req, res);	 
				 //System.out.println("Exit Servlet " + this.getServletName() + " in doGet() ");
				*/
				
			}
	/**********************************************************************************************************/  
		 
		@Override
		public void init() throws ServletException {
			System.out.println("Servlet " + this.getServletName() + " has started");
		}
	/**********************************************************************************************************/  

		@Override
		public void destroy() {
			System.out.println("Servlet " + this.getServletName() + " has stopped");
		}
}

	
	
	
	

