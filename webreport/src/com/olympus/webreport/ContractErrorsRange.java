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
import java.util.ArrayList;
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

import com.olympus.olyutil.Olyutil;

//RUN: http://localhost:8181/webreport/errorrange?startDate=2019-07-23&endDate=2019-07-24
@WebServlet("/errorrange")
public class ContractErrorsRange extends HttpServlet {

	// Service method of servlet
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res = null;
	static NodeList node = null;
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\contractvalidate.sql";
	static String sqlIdFile = "C:\\Java_Dev\\props\\sql\\getcontractid_range.sql";
	/****************************************************************************************************************************************************/
	public static boolean isNullStr(String str) {
	
		if (str== null  ||  str.equals("null") )  {	
			return true;
		}
		return false;
	}
	/****************************************************************************************************************************************************/
	public static ArrayList<String> getDbDataRange(String startDate, String endDate, String sqlSrc) throws IOException {
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

		fr = new FileReader(new File(sqlSrc));

		// be sure to not have line starting with "--" or "/*" or any other non
		// alphabetical character
		BufferedReader br = new BufferedReader(fr);
		while ((s = br.readLine()) != null) {
			sb.append(s);

		}
		br.close();
		// displayProps(connectionProps);
		String query = new String();
		query = sb.toString();
		// System.out.println("Query=" + query);
		try {
			con = Olyutil.getConnection(connectionProps);
			if (con != null) {
				// System.out.println("Connected to the database");
				statement = con.prepareStatement(query);

				// System.out.println("***^^^*** contractID=" + contractID);
				statement.setString(1, startDate);
				statement.setString(2, endDate);
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
	/****************************************************************************************************************************************************/

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String dispatchJSP = "/valerrorsrange.jsp";
		String sDate = "";
		String eDate = "";
		JSONArray jsonArr = new JSONArray();
		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<String> idArr = new ArrayList<String>();
		ArrayList<String> errArr = new ArrayList<String>();
		Map<String, Boolean> contractErrs = null;
		boolean errStat = false;
		PrintWriter out = response.getWriter();
		response.setContentType("text/JSON");

		String paramName = "startDate";
		String paramValue = request.getParameter(paramName);

		String endDate = "endDate";
		String endDateValue = request.getParameter(endDate);
		String jFlag = "jflag";
		String jFlagValue = request.getParameter(jFlag);
		

		int errCount = 0;
		if ((paramValue != null && !paramValue.isEmpty())) {
			sDate = paramValue.trim();
			//System.out.println("*** sDate:" + sDate + "--");
		}
		if ((endDateValue != null && !endDateValue.isEmpty())) {
			eDate = endDateValue.trim();
			//System.out.println("*** eDate:" + eDate + "--");
		}

		idArr = getDbDataRange(sDate, eDate, sqlIdFile);
		//Olyutil.printStrArray(idArr);
		String id = "";
		String bookDate = "";
		for (String id_t : idArr) { // iterating ArrayList			
			String[] line = id_t.split(":");
			id = line[0];
			bookDate = line[1];
			System.out.println("*** ID:" + id + "-- bookDate:" + bookDate + "--");	
		}
		
		if (! isNullStr(jFlagValue)) {
			System.out.println("***^^^*** JFLAG=" + jFlagValue + "--");
		}


	}

}
