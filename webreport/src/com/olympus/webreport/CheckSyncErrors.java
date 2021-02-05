package com.olympus.webreport;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NodeList;
import org.json.JSONArray;
import org.json.JSONObject;
import com.olympus.olyutil.Olyutil;

@WebServlet("/chksync")
public class CheckSyncErrors extends HttpServlet {
	/*****************************************************************************************************************************************/
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res = null;
	static NodeList node = null;
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\connectionRapport2.prop";
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\rapportSync.sql";

	/*****************************************************************************************************************************************/
	public static ArrayList<String> getDbData(Connection conn,  String query )
			throws IOException, SQLException {

		  
		FileReader fr = null;
		String s = new String();
		StringBuffer sb = new StringBuffer();
		ArrayList<String> strArr = new ArrayList<String>();
		// Statement statement = null;
		PreparedStatement statement;
		ResultSet res = null;

		  
		if (conn != null) {
			// System.out.println("Connected to the database");
			statement = conn.prepareStatement(query);

			// System.out.println("***^^^*** Param1=" + param1);
			//statement.setString(1, param1);
			res = Olyutil.getResultSetPS(statement);
			strArr = Olyutil.resultSetArray(res, ";");
		}
		return strArr;
	}
	/*****************************************************************************************************************************************/
	public static void dateFormat(String dateInString, java.sql.Timestamp timeStamp ) {
 
     
		
		Timestamp appDateTS = null;
		int cmpRtn = 0;
		try {
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		    Date parsedDate = dateFormat.parse(dateInString);
		    appDateTS = new java.sql.Timestamp(parsedDate.getTime());
		    cmpRtn = appDateTS.compareTo(timeStamp);
		} catch(Exception e) { //this generic but you can control another types of exception
		    e.printStackTrace(); 
		}
		System.out.println("***^^^*** TS=" + timeStamp + " -- appDateTS=" + appDateTS + " -- cmpRtn=" + cmpRtn );
	 
		 
	}

	/*****************************************************************************************************************************************/
	public static ArrayList<String> getDataStrArr(Connection conn, String sqlFileName )
			throws IOException, SQLException {
		ArrayList<String> strArr = new ArrayList<String>();
		String query = null;
		query = GetDbConnection.getQuery(sqlFileName);
		strArr = getDbData(conn,  query );
		// System.out.println("**** Q=" + query);
		
		return strArr;
	}
	/*****************************************************************************************************************************************/

	public static long dateFormat(String appDateTS, String timeStamp ) {	
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

	Date d1 = null;
	Date d2 = null;
	long diffSeconds = 0;
	long diffMinutes = 0;

	try {
		d1 = format.parse(appDateTS);
		d2 = format.parse(timeStamp);

		//in milliseconds
		long diff = d2.getTime() - d1.getTime();

		diffSeconds = diff / 1000 % 60;
		diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);

		if (diffMinutes > 7) {
			System.out.println("Sync exceeded timeframe: " + diffMinutes + " minutes -- Possible sync issue. ");
			return(diffSeconds);
		}
		
		//System.out.println("***^^^*** TS=" + timeStamp + " -- appDateTS=" + appDateTS   );
		//System.out.print(diffDays + " days, ");
		/*
		System.out.print(diffHours + " hours, ");
		System.out.print(diffMinutes + " minutes, ");
		System.out.println(diffSeconds + " seconds.");
		
			
		if (diffSeconds > 25) {
			//System.out.println("Sync exceeded timeframe: " + diffSeconds + " diffSeconds -- Possible sync issue. ");
			return(diffMinutes);
		}
	*/

	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return(0);
}

	/*****************************************************************************************************************************************/

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JSONArray jsonArr = new JSONArray();
		
		Connection conn = null;
		String query = null;
		ArrayList<String> strArr = new ArrayList<String>();
		Properties connProps = new Properties();
		String[] items = null;
		String appDate = "";
		PrintWriter out = response.getWriter();
		boolean isJson = false;
		//Date date1 = new java.util.Date();
		//System.out.println("***^^^*** Date=" + date1);
		String dispatchJSP = "/chksync.jsp";
		String sep = ";";
	 
 
		
		String paramName = "jflag";
		String jFlagValue = request.getParameter(paramName);
		
 
		request.getSession().setAttribute(paramName, jFlagValue);
		//System.out.println("***^^^*** JFLAG=" + jFlagValue);
		 
		long time = System.currentTimeMillis();
		Timestamp timestamp = new java.sql.Timestamp(time);
		long diffRtn = 0;
		 
		try {
			connProps = Olyutil.getPropertiesObj(propFile);
			query = GetDbConnection.getQuery(sqlFile);
			//System.out.println("***^^^*** query=" + query);
			conn = Olyutil.getConnection(connProps);
			strArr = getDataStrArr(  conn, sqlFile );
			String appID = "";
			String warnMsg = "";
			if (strArr.size() > 0) {
				//Olyutil.printStrArray(strArr);
				for (String str : strArr) {
					JSONObject obj = new JSONObject();
					 items = str.split(";");
					 appDate = items[3];
					 appID = items[2];
					 //System.out.println("***^^^*** APPDATE=" + appDate + "--");
					 //dateFormat(appDate, timestamp);
					 
					diffRtn = dateFormat(appDate, timestamp.toString());
					if (diffRtn > 0) {
						warnMsg = "appSync exceeded timeframe: " + diffRtn + " diffSeconds -- Possible sync issue. ";
						//System.out.println("appID=" + appID + "--" + warnMsg);
						obj.put("appID", appID);
						obj.put("warnMsg", warnMsg);
						jsonArr.put( obj);
					}
					 
					 
				}
			
				
				
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (  Olyutil.isNullStr(jFlagValue) ||    ! jFlagValue.equals("json")  ) {
			
			request.getSession().setAttribute("jsonArr", jsonArr);
			 // req.getSession().setAttribute(paramName, paramValue);
			request.getRequestDispatcher(dispatchJSP).forward(request, response);
			// System.out.println("Exit Servlet " + this.getServletName() + " in doGet() ");
			
		} else {
			for (int k = 0; k < jsonArr.length(); k++) {
				 
				if (k == 0) {
					isJson = true;
					out.write("[");
				}
				if (k == (jsonArr.length() - 1)) {
					out.write(jsonArr.getJSONObject(k).toString());
				} else {
					out.write(jsonArr.getJSONObject(k).toString() + ",");
				}
				// System.out.println("k=" + k + "Val:" + jsonArr.getJSONObject(k).toString() );
			}
			// String dispatchJSP = "/resultstest.jsp";
			
			if (isJson) {
				out.write("]");
			}
			
		}
		

	}
	/*****************************************************************************************************************************************/

}
