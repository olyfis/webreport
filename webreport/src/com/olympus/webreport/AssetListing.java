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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NodeList;

import com.microsoft.sqlserver.jdbc.StringUtils;
import com.olympus.infolease.contract.ContractData;
import com.olympus.olyutil.Olyutil;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/assetlist")
public class AssetListing  extends HttpServlet {
	// Service method of servlet
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res  = null;
	static NodeList  node  = null;
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\assetList.sql";
	// static String sqlFileAsset = "C:\\Java_Dev\\props\\sql\\assetValByContract.sql";
	//static String sqlFileAsset = "C:\\Java_Dev\\props\\sql\\q1.sql";
	
	/****************************************************************************************************************************************************/
	public static ArrayList<String> getDbData(String id, String sqlQueryFile, String booked, String qType) throws IOException {
		FileInputStream fis = null;
		FileReader fr = null;
		String s = new String();
		String sep = "";
        StringBuffer sb = new StringBuffer();
        ArrayList<String> strArr = new ArrayList<String>();
		try {
			fis = new FileInputStream(propFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties connectionProps = new Properties();
		connectionProps.load(fis);
		 
		fr = new FileReader(new File(sqlQueryFile));
		
		// be sure to not have line starting with "--" or "/*" or any other non alphabetical character
		BufferedReader br = new BufferedReader(fr);
		while((s = br.readLine()) != null){
		      sb.append(s);
		       
		}
		br.close();
		//displayProps(connectionProps);
		String query = new String();
		query = sb.toString();	
		//System.out.println( query);	 
		try {
			con = Olyutil.getConnection(connectionProps);
			if (con != null) {
				//System.out.println("Connected to the database");
				statement = con.prepareStatement(query);
				
				//System.out.println("***^^^*** contractID=" + contractID);
				statement.setString(1, id);
				sep = ";";
				/*
				if (qType.equals("asset")) {
					statement.setString(1, booked);
					statement.setString(2, id);
					sep = ";";
					
				} else if (qType.equals("contract")) {
					statement.setString(1, id);
					sep = ":";
				}
				*/
				 
				res = Olyutil.getResultSetPS(statement);		 	 
				strArr = Olyutil.resultSetArray(res, sep);			
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
	/****************************************************************************************************************************************************/
	/****************************************************************************************************************************************************/
    /****************************************************************************************************************************************************/
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idVal = "";
		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<String> errorArr = new ArrayList<String>();
		
		String paramName = "id";
		String paramValue = request.getParameter(paramName);
		//if ((paramValue != null && !paramValue.isEmpty()) && paramValue.equals("id")) {
		if ((paramValue != null && !paramValue.isEmpty())) {	
			 idVal= paramValue.trim();
			// System.out.println("*** idVal:" + idVal + "--");			 
		}
		String dispatchJSP = "/assetlist.jsp";
		strArr = getDbData(idVal, sqlFile, "", "Asset");
		//System.out.println("*** arrSz:" + strArr.size()+ "--");	
		//Olyutil.printStrArray(strArr);
		request.getSession().setAttribute("strArr", strArr);
		String formUrl = "formUrl";
		String formUrlValue = "/webreport/assetlistexcel " ;
		request.getSession().setAttribute(formUrl, formUrlValue);
		request.getRequestDispatcher(dispatchJSP).forward(request, response);

		 /*
		if (contracts.size() > 0) {
			booked = contracts.get(0).getDateBooked();
			errorArr =  validateAssets(idVal, booked);
		
		
		
			//request.getSession().setAttribute("assetErrs",jsonErrArr);
			request.getSession().setAttribute("assetErrs",errorArr);
			for (int k = 0; k < contracts.size(); k++) {
				//System.out.println("*** !!! *** contractID:" + contracts.get(k).getContractID());
				 contractErrs = ValidateChecks.validateContract( contracts.get(k)); 
				
			}
			request.getSession().setAttribute("contractErrs", contractErrs);
			//Olyutil.printHashMap(contractErrs);
			// re-direct to JSP page
			request.getRequestDispatcher(dispatchJSP).forward(request, response);
		} else {
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.write("<h3>Error occurred with Contract: " + paramValue + "</h3>");
			
		}
		*/
	}
	

}
