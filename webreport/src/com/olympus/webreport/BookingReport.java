

package com.olympus.webreport;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import com.olympus.util.JButils;
import org.w3c.dom.NodeList;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/BookingReport")
public class BookingReport extends HttpServlet {
	
/**********************************************************************************************************/	
	// Service method of servlet
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res  = null;
	static NodeList  node  = null;
	static String s = null;
	static private PreparedStatement statement;
/**********************************************************************************************************/	
	
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
	
	private static java.sql.Date getCurrentDate() {
	    java.util.Date today = new java.util.Date();
	    return new java.sql.Date(today.getTime());
	}
	
//************************************************************************************************************************************************			
	
	public static ResultSet getResultSetPS( PreparedStatement stmt) throws SQLException {
		ResultSet rs = stmt.executeQuery();
	
		return (rs);
	}
//************************************************************************************************************************************************			

	public static ArrayList<String> getData(String startDate, String endDate, String connProp, String sqlFile, String sep) throws IOException {
		BookingReport br2 = new BookingReport();
		ArrayList<String> strArr = new ArrayList<String>();
		String result = null;
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
		
	
		
		//System.out.println("Q=" + query2);
		//System.out.println(  query2);
		try {
			con = getConnection(connectionProps);
			if (con != null) {
				//System.out.println("Connected to the database");
				//System.out.println("***^^^^***Q=" + query2);
				
//************************************************************************************************************************************************			
							
				
				//System.out.println("**********Q=" + query2);
				//date = br2.getCurrentDate().toString();
				//System.out.println("DATE=" + date + "--" );	
				//System.out.println("startDate=" + startDate);
				//System.out.println("endDate=" + endDate);
				
				statement = con.prepareStatement(query2);	
				
				statement.setString(1, startDate);
				statement.setString(2, endDate);
				//System.out.println("SDATE=" + startDate + "--" + "EDATE=" + endDate + "--" + "SEP=" + sep);
				
				res = getResultSetPS(statement);	 
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
	
	/*****************************************************************************************************/
	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
		String dispatchJSP = null;
		String connProp = null;
		String sqlFile = null;
		String sep = null;
		
		ArrayList<String> strArr = new ArrayList<String>();
		PrintWriter out = res.getWriter();
		res.setContentType("text/plain");
		// out.write(" in Service method doGet() ");

		String mType = "mType";
		String mTypeValue = req.getParameter(mType);
		//System.out.println("^^^^^^^ mTypeValue=" + mTypeValue);
		// no parameters at this time
		String startDate = "startDate";
		String startDateValue = req.getParameter(startDate);
		String endDate = "endDate";
		String endDateValue = req.getParameter(endDate);
		
		
		
		String formUrl = "formUrl";
		String formUrlValue = "/webreport/bookexcel?startDate=" + startDateValue + "&endDate=" + endDateValue + "&mType=" + mTypeValue;
		req.getSession().setAttribute(startDate, startDateValue);
		
		
		req.getSession().setAttribute(endDate, endDateValue);
		req.getSession().setAttribute(mType, mTypeValue);
		req.getSession().setAttribute(formUrl, formUrlValue);
		//System.out.println("BR^^^ startDate=" + startDateValue + "--");
		//System.out.println("BR^^^ endDate=" + endDateValue + "--");
		
		
		//JButils jutil = new JButils();
		//jutil.printStrArray(strArr);
		if (mTypeValue.equals("RPB")) {
			connProp = "C:\\Java_Dev\\props\\connectionRapport.prop";
			sqlFile = "C:\\Java_Dev\\props\\sql\\rapbooking_range.sql";
			dispatchJSP = "/booking.jsp";
			sep = ";";
		} else if (mTypeValue.equals("RPS")) {
			connProp = "C:\\Java_Dev\\props\\connectionRapport.prop";
			sqlFile = "C:\\Java_Dev\\props\\sql\\booking_summary.sql";
			dispatchJSP = "/booksummary.jsp";
			sep = ":";
		} else if (mTypeValue.equals("ILB")) {
			connProp = "C:\\Java_Dev\\props\\connection.prop";
			sqlFile = "C:\\Java_Dev\\props\\sql\\il_booking_range.sql";
			dispatchJSP = "/ilbooking.jsp";
			sep = ":";
		}
		 /*
			System.out.println("connProp=" + connProp);
			System.out.println("sqlFile=" + sqlFile);
			System.out.println("startDateValue=" + startDateValue);
			System.out.println("endDateValue=" + endDateValue);
			System.out.println("sep=" + sep);
		*/
		strArr = getData(startDateValue, endDateValue, connProp, sqlFile, sep);
		
		req.getSession().setAttribute("strArr", strArr);
		// req.getSession().setAttribute(paramName, paramValue);
		req.getRequestDispatcher(dispatchJSP).forward(req, res);
		// System.out.println("Exit Servlet " + this.getServletName() + " in doGet() ");
	}
/**********************************************************************************************************/  
 
	@Override
	public void init() throws ServletException {
		System.out.println("Servlet " + this.getServletName() + " has started");
	}
	@Override
	public void destroy() {
		System.out.println("Servlet " + this.getServletName() + " has stopped");
	}
} // End Class







