package com.olympus.webreport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

@WebServlet("/BookingSummaryDetail")
public class BookingSummaryDetail extends HttpServlet {
	
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
		//System.out.println("%%%%%%%%%%%%%%%%%%%%% Returning RS");
		return (rs);
	}
//************************************************************************************************************************************************			

	public static ArrayList<String> getData( String connProp, String sqlFile, String sep) throws IOException {
		BookingReport br2 = new BookingReport();
		ArrayList<String> strArr = new ArrayList<String>();
		String result = null;
		JButils jutil = new JButils();
		FileInputStream fis = null;
		
 
			String hostname = InetAddress.getLocalHost().getHostName();
			//System.out.println("******************************************** 	Begin getData    *******************************************");
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
				// System.out.println("Connected to the database");
				 //System.out.println("***^^^^***Q=" + query2);
				
//************************************************************************************************************************************************			
							
	 
				//date = br2.getCurrentDate().toString();
				//System.out.println("DATE=" + date + "--" );	
				//System.out.println("startDate=" + startDate);
				//System.out.println("endDate=" + endDate);
				
				statement = con.prepareStatement(query2);	
				
				//statement.setString(1, startDate);
				//statement.setString(2, endDate);
				//System.out.println("%%%%%% SDATE=" + startDate + "--" + "EDATE=" + endDate + "--" + "SEP=" + sep);
				
				res = getResultSetPS(statement);	 
				strArr = resultSetArray(res, sep);	
				
				//System.out.println("%%%%% arrSize=" + strArr.size() + " ---- startDate:"  + startDate  + " ---- endDate:"  + endDate  );
				//System.out.println("%%%%% arrSize=" + strArr.size());
				
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
		//System.out.println("******************************************** 	End getData    *******************************************");
		return strArr;
	}
	
	/*****************************************************************************************************/
	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
		
		JButils jutil = new JButils();
	
		String headerFilenameBRSummary = "C:\\Java_Dev\\props\\headers\\BRSummary_Detail_header.txt";
		
		
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		
		String dispatchJSP = null;
		String connProp = null;
		String sqlFile = null;
		String sep = null;
		String contentType = "application/ms-excel";
		
		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = jutil.readInputFile(headerFilenameBRSummary);
		Date date = jutil.getCurrentDate();
		String dateStamp = date.toString();
		//System.out.println("Date=" + dateStamp);
		 String FILE_NAME = "Rapport_Booking_Detail_" + dateStamp  + ".xlsx";
		
		/*
		for (String str : headerArr) { // iterating ArrayList
			System.out.println("Display Header: "  + str);	
		}
		 */
		//PrintWriter out = res.getWriter();
		//res.setContentType("text/plain");
		
		
		
		// out.write(" in Service method doGet() ");

		String mType = "mType";
		String mTypeValue = req.getParameter(mType);
		req.getSession().setAttribute(mType, mTypeValue);
		
		//System.out.println("^^^^^^^ mTypeValue=" + mTypeValue);


		
		//jutil.printStrArray(strArr);
		if (mTypeValue.equals("RPD")) {
			connProp = "C:\\Java_Dev\\props\\connectionRapport.prop";
			//sqlFile = "C:\\Java_Dev\\props\\sql\\booking_summary_detail.sql";
			sqlFile = "C:\\Java_Dev\\props\\sql\\booking_detail.sql";
			dispatchJSP = "/bookingdetail.jsp";
			sep = ":";
		} 
		 /*
		System.out.println("connProp=" + connProp);
		System.out.println("sqlFile=" + sqlFile);
		
		System.out.println("mType=" + mType);
		
		System.out.println("sep=" + sep);
		System.out.println("Call getData");
		
		*/
		strArr = getData(connProp, sqlFile, sep);
		
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^ strArr=" + strArr.toString());
		System.out.println("^^^^^^^^^^^ strArr returned");
		 
		
/*****************************************************************************************************************************************/
		
 /*
		// Create Excel file on client
		WriteExcel writeExcel = new WriteExcel();
		workbook = writeExcel.newWorkbook();
		sheet = writeExcel.newWorkSheet(workbook, "Rapport Booking Summary");
		writeExcel.loadHeader(workbook, sheet, headerArr);
		//System.out.println("** Call loadWorkSheet");
		writeExcel.loadWorkSheet(workbook, sheet, strArr, 1);
		//BufferedInputStream in = null; 
		   
  
		try {		 
 	
			// HttpServletResponse response = getResponse(); // get ServletResponse
			res.setContentType("application/vnd.ms-excel"); // Set up mime type
			res.addHeader("Content-Disposition", "attachment; filename=" + FILE_NAME);
			OutputStream out2 = res.getOutputStream();
			workbook.write(out2);
			out2.flush();


		
		//********************************************************************************************************************************
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	*/
		//req.getSession().setAttribute("strArr", strArr);
		// req.getSession().setAttribute(paramName, paramValue);
		//req.getRequestDispatcher(dispatchJSP).forward(req, res);
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







