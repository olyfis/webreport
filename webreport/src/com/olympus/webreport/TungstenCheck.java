package com.olympus.webreport;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.NodeList;

import com.microsoft.sqlserver.jdbc.StringUtils;
import com.olympus.infolease.contract.ContractData;
import com.olympus.olyutil.Olyutil;

@WebServlet("/tungsten")
 
public class TungstenCheck extends HttpServlet {
	
	// Service method of servlet
		static Statement stmt = null;
		static Connection con = null;
		static ResultSet res  = null;
		static NodeList  node  = null;
		static String s = null;
		static private PreparedStatement statement;
		static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
		
		static String sqlFile = "C:\\Java_Dev\\props\\sql\\tungsten.sql";
		
		
		/****************************************************************************************************************************************************/

		
		public static String getLeadDate(String today, String lead ) throws IOException {
			
			//System.out.println("Date before Addition: "+oldDate);
			//Specifying date format that matches the given date
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			try{
			   //Setting the date to the given date
			   c.setTime(sdf.parse(today));
			} catch(ParseException e){
				e.printStackTrace();
			 }
			   
			//Number of Days to add
			
			 int leadDays = Integer.parseInt(lead);
			c.add(Calendar.DAY_OF_MONTH, leadDays);  
			
			
			//Date after adding the days to the given date
			String newDate = sdf.format(c.getTime());  
			//Displaying the new Date after addition of Days
			//System.out.println("Commencement Date:" + today  +  " -- Add Lead:" + lead + " -- Date after Addition: "+newDate);
			
			return newDate;
		}
		/****************************************************************************************************************************************************/
		public static ArrayList<String> getDbData() throws IOException {
			FileInputStream fis = null;
			FileReader fr = null;
			String s = new String();
	        StringBuffer sb = new StringBuffer();
	        ArrayList<String> strArr = new ArrayList<String>();
			try {
				fis = new FileInputStream(propFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Properties connectionProps = new Properties();
			connectionProps.load(fis);
			 
			fr = new FileReader(new File(sqlFile));
			
			// be sure to not have line starting with "--" or "/*" or any other non alphabetical character
			BufferedReader br = new BufferedReader(fr);
			while((s = br.readLine()) != null){
			      sb.append(s);
			       
			}
			br.close();
			//displayProps(connectionProps);
			String query = new String();
			query = sb.toString();	
			//System.out.println("Query=    " + query);	 
			try {
				con = Olyutil.getConnection(connectionProps);
				if (con != null) {
					//System.out.println("Connected to the database");
					statement = con.prepareStatement(query);
					
					//System.out.println("***^^^*** contractID=" + contractID);
					//statement.setString(1, id);
					res = Olyutil.getResultSetPS(statement);		 	 
					strArr = Olyutil.resultSetArray(res, ":");			
				}		
			} catch (SQLException se) {
				se.printStackTrace();
			} finally {
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			return strArr;
		}
 		
		/****************************************************************************************************************************************************/
		public static JSONArray expirationCheck(ArrayList<String> strArr ) throws IOException {
			//Olyutil.printStrArray(strArr);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String currentDate =  dateFormat.format(date);
			String expireDate = "";
			Date date1 = null;
			Date date2 = null;
			ArrayList<String> expArr = new ArrayList<String>();
			JSONArray jsonArr = new JSONArray();
			 

			expireDate = getLeadDate(currentDate, "90");
			//System.out.println("Current Date:" + currentDate + "-- ExpireDate:" + expireDate + "--" );
			
			for (String str : strArr) { // iterating ArrayList
				JSONObject obj = new JSONObject();
				String[] items = str.split(":");
				//System.out.println(items[0] + ":" + items[1] + ":" + items[2] + ":" + items[3] + ":" + items[4]   );
				try {
					  date1 =  dateFormat.parse(items[2]);
					  date2 = dateFormat.parse(expireDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (date1.compareTo(date2) >= 0) {
			            //System.out.println("Date1 is after Date2 -- OK");
			    } else {
			    	 String err1 = "ContractID: " + items[0] + " is under 90 day window: " + items[2] ;
			    	 //System.out.println("ContractID: " + items[0] + " is under 90 day window: " + items[2] + "--");
			    	 //expArr.add(err1);
			   
					obj.put("contract", items[0]);
					obj.put("customer", items[3]);
					obj.put("expDate", items[2]);
					obj.put("po", items[1]);
					obj.put("invCode", items[4]);
					obj.put("term", items[5]);
					obj.put("city", items[6]);
					obj.put("state", items[7]);
					obj.put("zip", items[8]);
					obj.put("visn", items[9]);
					obj.put("rep", items[10]);
					jsonArr.put(obj);
			    }
				//System.out.println("***** compDate=" + items[2] + "--");
			}
			 
			return jsonArr;
		}	

		/****************************************************************************************************************************************************/

		/****************************************************************************************************************************************************/
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			//String idVal = "";
			ArrayList<String> strArr = new ArrayList<String>();
			ArrayList<String> eArr = new ArrayList<String>();
			JSONArray jsonArr = new JSONArray();
			strArr = getDbData();
			jsonArr = expirationCheck( strArr  );
			
			PrintWriter out = response.getWriter();
			response.setContentType("text/plain");
			//out.write(" in Service method doGet() ");
			
			 /*
			for (String str : eArr) {
				out.println(str);
				
			}
			*/
			
			
			for(int k = 0 ; k < jsonArr.length() ; k++) { 
			 
				if  (k == 0 ) {
					out.write("[");
				}	
			 		
				if  (k == (jsonArr.length() -1) ) {
					out.write(jsonArr.getJSONObject(k).toString());			
				} else {
					out.write(jsonArr.getJSONObject(k).toString() + ",");	
				}	
			}
			 out.write("]");
		
		}
		
	

}
