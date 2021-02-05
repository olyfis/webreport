package com.olympus.webreport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.olympus.util.JButils;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**********************************************************************************************************/	

public class GetQuoteData  extends HttpServlet {
	static String sep = ":";
	/**********************************************************************************************************/	
	// Service method of servlet
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res  = null;
	static NodeList  node  = null;
	static String s = null;
	static private PreparedStatement statement;

/**********************************************************************************************************/	
	/**********************************************************************************************************/	

	/**********************************************************************************************************/	
	/***********************************************************************************************************************************/
	// method to print array
	public static void printStrArray(ArrayList<String> strArr) {

		for (String str : strArr) { // iterating ArrayList
			System.out.println("*** DATA:" + str + "---");
		}
		// System.out.println(names[index]);
	}

	/***********************************************************************************************************************************/

	/**********************************************************************************************************/	
	//************************************************************************************************************************************************			
		public static Connection getConnection(Properties connectionProps ) throws SQLException {
			Connection conn = null;
			
			String url = null;
			String myHost = (String) connectionProps.get("myHost");
			String myAccount = (String) connectionProps.get("myAccount");
			String uid = (String) connectionProps.get("userID");
			String pw = (String) connectionProps.get("passWord");
			String dbClass = (String) connectionProps.get("dbClass");
			String dbmsType = (String) connectionProps.get("dbmsType");
			String passWord = pw.trim();
			String userID = uid.trim();
	/*
			System.out.println("dbmsType: " + dbmsType + "--");
			System.out.println("dbClass: " + dbClass + "--");
			System.out.println("myHost: " + myHost + "--");
			System.out.println("userID: " + userID + "--");
			System.out.println("passWord: " + passWord + "--");
	*/
			try {	
				if (dbmsType.equals("UNIDATA")) {
					Class.forName(dbClass);
					//Class.forName("com.rs.u2.jdbc.UniJDBCDriver");
					 url = "jdbc:rs-u2://" + myHost + "/" + myAccount + ";" + "dbmstype=" + dbmsType; // generate URL
					conn = DriverManager.getConnection(url, userID, passWord);
					//System.out.println("getConnection: URL: " + url);
				} else if (dbmsType.equals("rapport")) {
					Class.forName(dbClass);
					 url = "jdbc:sqlserver://" + myHost + ":1433;" + "databaseName=rapport;user=Rapport;password=rapport;"; // generate URL
					conn = DriverManager.getConnection(url, userID, passWord);
				}
				
				
				if (conn == null) {
					System.out.println(" %%% Returned null connection");
				} else {
					//System.out.println("Connected - > URL: " + url);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return conn;
		}
		
		
		
	//************************************************************************************************************************************************			
		
	public static ArrayList<String> resultSetArray(ResultSet rs, String sep) throws SQLException {

		ArrayList<String> arrStr = new ArrayList<String>();
		ArrayList<String> xData = new ArrayList<String>();
		String outDataLine = "";
		ResultSetMetaData rsmd = rs.getMetaData();
		int numColumns = rsmd.getColumnCount();
		int columnsNumber = rsmd.getColumnCount();
		while (rs.next()) {
			for (int i = 1; i <= columnsNumber; i++) {
				if (i > 1) {
					// System.out.print(sep);
					outDataLine += sep;
				}
				String columnValue = rs.getString(i);
				outDataLine += columnValue;
			}
			// System.out.println(outDataLine);
			arrStr.add(outDataLine);
			// System.out.println("");
			outDataLine = "";
		}
		return arrStr;
	}

/**********************************************************************************************************/	
public static ArrayList<String> getData(String appKey, String connProp, String sqlFile, String sep) throws IOException {


		BookingReport br2 = new BookingReport();
		ArrayList<String> strArr = new ArrayList<String>();

		JButils jutil = new JButils();
		FileInputStream fis = null;

		String hostname = InetAddress.getLocalHost().getHostName();
		try {
			fis = new FileInputStream(connProp);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Properties connectionProps = new Properties();
		String s = new String();
		StringBuffer sb = new StringBuffer();

		try {
			connectionProps.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FileReader fr = new FileReader(new File(sqlFile));
		// be sure to not have line starting with "--" or "/*" or any other non
				// alphabetical character
				BufferedReader br = new BufferedReader(fr);
				while ((s = br.readLine()) != null) {
					sb.append(s);
				}
				br.close();

				String query2 = new String();
				query2 = sb.toString();

				//System.out.println(  query2);
				try {
					con = getConnection(connectionProps);
					if (con != null) {

						statement = con.prepareStatement(query2);	
						
						statement.setString(1, appKey);
					
						//System.out.println("SDATE=" + startDate + "--" + "EDATE=" + endDate + "--" + "SEP=" + sep);
						
						res = jutil.getResultSetPS(statement);	 
						strArr = resultSetArray(res, sep);			
						//System.out.println("**** arrSize=" + strArr.size() + " ---- startDate:"  + startDate  + " ---- endDate:"  + endDate  );
		//************************************************************************************************************************************************
						 //result = jutil.displayResults(res);

					} else {
						System.out.println("**** NOT Connected to the database");
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
					} // end finally try
				}	
				
				return strArr;
	
	}

	/**********************************************************************************************************/


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
		
		String connProp = null;
		String sqlFile = null;
		//PrintWriter out = res.getWriter();
		res.setContentType("text/plain");

		String appKey = "appKey";
		String appKeyValue = req.getParameter(appKey);
		
	
		
		if (appKeyValue != null) {
			//out.write("Parameter " + paramName + " found");
			//System.out.println("PARAM:" + appKeyValue + "---");
			res.getWriter().println("****PARMVal=" + appKeyValue + "---<BR>");
			connProp = "C:\\Java_Dev\\props\\connectionRapport.prop";
			sqlFile = "C:\\Java_Dev\\props\\sql\\quote_num.sql";
		}

		//out.close();
		// call with app_ID=101-0017172-001
		ArrayList<String> strArr = getData(appKeyValue, connProp, sqlFile, sep);
		//printStrArray(strArr);
		req.getSession().setAttribute(appKey, appKeyValue);
		req.getSession().setAttribute("strArr", strArr);
		req.getRequestDispatcher("/getquote.jsp").forward(req, res);

		return;
		
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
