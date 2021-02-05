package com.olympus.webreport;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.olympus.olyutil.Olyutil;

@WebServlet("/testdb")
public class TestDB extends HttpServlet {
	
static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	
	static String sqlFile1 = "C:\\Java_Dev\\props\\sql\\RateCheck.sql";
	static String sqlFile2 = "C:\\Java_Dev\\props\\sql\\FinalRateCheck.sql";
	static String sqlFile3 = "C:\\Java_Dev\\props\\sql\\purchOptionMatch.sql";
	static String sqlFile4 = "C:\\Java_Dev\\props\\sql\\upfrontTaxCheck.sql";
	static String sqlFile5 = "C:\\Java_Dev\\props\\sql\\miscBillableFlagErrCheck.sql";
	
	static String sqlFileTest = "C:\\Java_Dev\\props\\sql\\Daily_commencement_report_V4.sql";
	
	static String sqlFile6 = "C:\\Java_Dev\\props\\sql\\taxRateChanges.sql";
	
	/*****************************************************************************************************************************************************/
	public static ArrayList<String> getDataStrArr(Connection conn, String sqlFileName,  String bookDate ) throws IOException, SQLException {
		ArrayList<String> strArr = new ArrayList<String>();
		String query = null;
		query = ValidateContractChecks2.getQuery(sqlFileName);
		strArr = ValidateContractChecks2.getDbData(conn, sqlFileName, query, bookDate);	
		//System.out.println("**** Q=" + query);	
		
		return strArr;
	}
	/*****************************************************************************************************************************************************/
	public static ArrayList<String> rateChk(ArrayList<String> strArr ) throws IOException, SQLException {
	 
		ArrayList<String> errArr = new ArrayList<String>();
		String id = null;
	 
		String calcRate = null;
		String amt = null;
				
		// iterating ArrayList
		for (String str : strArr) { // iterating ArrayList
			// System.out.println("**** Str=" + str);		 
			String[] items = str.split(":");
			id = items[0];
			calcRate = items[4];
			amt = items[5];
			if (  calcRate.equals(amt)) {
				System.out.println("**** calcRate=" + calcRate + "-- AMT=" + amt + "--");
				errArr.add(str);
			}	
		}		
		return errArr;
	}
	/****************************************************************************************************************************************************/
	public static ArrayList<String> finalRateChk(ArrayList<String> strArr ) throws IOException, SQLException {
		 
		ArrayList<String> errArr = new ArrayList<String>();
		String id = null;
		String asset = null;
		double calcUnits = 0.0;
		double units = 0.0;
		double diff = 0.0;
			
		for (String str : strArr) { // iterating ArrayList
			// System.out.println("**** Str=" + str);		 
			String[] items = str.split(":");
			id = items[0];
			asset = items[1];
			units = Double.valueOf(items[2]);
			calcUnits = Double.valueOf(items[3]);
			diff = units - calcUnits;		
			//System.out.println("**** DIFF=" + diff + "--- calcUnits=" + calcUnits + "-- units=" + units + "--");
			if ( diff != 1.0) {
				System.out.println("**** calcUnits=" + calcUnits + "-- units=" + units + "--");
				errArr.add(str);
			}	
		}		
		return errArr;
	}
	/****************************************************************************************************************************************************/
	public static ArrayList<String> purchOptChk(ArrayList<String> strArr ) throws IOException, SQLException {
		 
		ArrayList<String> errArr = new ArrayList<String>();
		String id = null;
		String asset = null;
		String po1 = null;
		String poDB = null;
				
		for (String str : strArr) { // iterating ArrayList
			// System.out.println("**** Str=" + str);		 
			String[] items = str.split(":");
			id = items[0];
			asset = items[1];
			po1 = items[2];
			poDB = items[3];
			if ( !  po1.equals(poDB)) {
				System.out.println("**** PO1=" + po1 + "-- poDB=" + poDB + "--");
				errArr.add(str);
			}	
		}		
		return errArr;
	}
	
	
	/****************************************************************************************************************************************************/
	public static ArrayList<String> upFrontTaxChk(ArrayList<String> strArr ) throws IOException, SQLException {
		 
		ArrayList<String> errArr = new ArrayList<String>();
		String id = null;
		String equipDesc = null;
		String model = null;
		double aOrigCost = 0.0;
		String stateDesc = null;
		String taxCode = null;
	
		for (String str : strArr) { // iterating ArrayList
			//System.out.println("**** Str=" + str);		 
			String[] items = str.split(":");
			id = items[0];
			equipDesc = items[2];
			model = items[3];
			aOrigCost = Double.valueOf(items[5]);
			stateDesc = items[8];
			taxCode = items[9];
			
			//System.out.println("**** equipDesc=" + equipDesc + "-- model=" + model + "--"  + "-- aOrigCost=" + aOrigCost + "--"  + "-- stateDesc=" + stateDesc + "--"  + "-- taxCode=" + taxCode + "--"       ) ;		 
			if (((equipDesc.equals("UPFRONT TAX") || model.equals("UPFRONT TAX"))  &&  aOrigCost > 0.0 )) {
				//System.out.println("***^^^**** equipDesc=" + equipDesc + "-- model=" + model + "--"  + "-- aOrigCost=" + aOrigCost + "--"  + "-- stateDesc=" + stateDesc + "--"  + "-- taxCode=" + taxCode + "--"       ) ;
				errArr.add("UpFrontTax_aOrigCost <> 0:" + str);
				System.out.println("***^^^**** UpFrontTax_aOrigCost <> 0 ");
			}	
			if ((equipDesc.equals("ILLINOIS") && (! taxCode.equals("001"))) )  {
				errArr.add("Illinois and TaxCode <> 001:" + str);
				System.out.println("***^^^**** Illinois TaxCode <> 001");
			}
			if (!(equipDesc.equals("ILLINOIS") && (! taxCode.equals("100"))) )  {
				errArr.add("Not Illinois and TaxCode <> 100:" + str);
				System.out.println("***^^^**** Not Illinois and TaxCode <> 100");
			}
		}		
		return errArr;
	}
	
	/****************************************************************************************************************************************************/
	public static ArrayList<String> miscBillableFlagChk(ArrayList<String> strArr ) throws IOException, SQLException {
		ArrayList<String> errArr = new ArrayList<String>();
		String id = null;
		String billFlag = null;
		
		 
		for (String str : strArr) { // iterating ArrayList
			//System.out.println("**** Str=" + str);		 
			String[] items = str.split(":");
			id = items[0];
			billFlag = items[1];
			
			if (billFlag.equals("0")) {
				errArr.add(str);
				System.out.println("***^^^**** Bill Flag == 0");
			}
			
		}
		 
		return errArr;
	}
	
	/****************************************************************************************************************************************************/
	
	
	/****************************************************************************************************************************************************/
	
	
	/****************************************************************************************************************************************************/
	public static ArrayList<String> getDbData(Connection conn, String sqlFile, String query ) throws IOException, SQLException  {
		
		//Connection conn = null;
		FileReader fr = null;
		String s = new String();
        StringBuffer sb = new StringBuffer();
        ArrayList<String> strArr = new ArrayList<String>();
        //Statement statement = null;
         PreparedStatement statement;
		ResultSet res  = null;
		
		//conn = getDbConn(propFile);
		if (conn != null) {
			//System.out.println("Connected to the database");
			statement = conn.prepareStatement(query);
			
			//System.out.println("***^^^*** Param1=" + param1);
			//statement.setString(1, param1);
			res = Olyutil.getResultSetPS(statement);		 	 
			strArr = Olyutil.resultSetArray(res, ";");			
		}
		return strArr;
	}
	
	/****************************************************************************************************************************************************/
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection conn = null;
		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<String> errArr = new ArrayList<String>();
		String query = null;
		String paramName = "bookDate";
		String bookDate = request.getParameter(paramName);
		
		 //String bookDate = "2019-07-11";
		try {
			conn = GetDbConnection.getDbConn(propFile);
			query = GetDbConnection.getQuery(sqlFile1);
			//System.out.println("***^^^*** query=" + query);
		/*	
			strArr = getDataStrArr(conn, sqlFile1, bookDate);
			if (strArr.size() > 0) {
				errArr = rateChk(strArr);
				Olyutil.printStrArray(errArr);
				
			}
			strArr.clear();
			errArr.clear();
			
			if (strArr.size() > 0) {
				strArr = getDataStrArr(conn, sqlFile2, bookDate);
				errArr = finalRateChk(strArr);
				Olyutil.printStrArray(errArr);
			}
			strArr.clear();
			errArr.clear();
			if (strArr.size() > 0) {
				strArr = getDataStrArr(conn, sqlFile3, bookDate);
				errArr = purchOptChk(strArr);
				Olyutil.printStrArray(errArr);
			}
			strArr.clear();
			errArr.clear();
			if (strArr.size() > 0) {
				strArr = getDataStrArr(conn, sqlFile4, bookDate);
				errArr = upFrontTaxChk(strArr);
				Olyutil.printStrArray(errArr);
			}
			strArr.clear();
			errArr.clear();
			if (strArr.size() > 0) {
				strArr = getDataStrArr(conn, sqlFile5, bookDate);
				errArr = miscBillableFlagChk(strArr);
				Olyutil.printStrArray(errArr);

			}
			strArr.clear();
			errArr.clear();
			*/
			System.out.println("********************************************************************************************************************"); 
			query = ValidateContractChecks2.getQuery(sqlFileTest);
			strArr = getDbData(conn, sqlFileTest, query);
			Olyutil.printStrArray(strArr);
			System.out.println("********************************************************************************************************************"); 
			System.out.println(query);
			/*`(
			
			strArr = ValidateContractChecks2.getDbData(conn, sqlFile, query, bookDate);
			Olyutil.printStrArray(strArr);
			System.out.println("********************************************************************************************************************"); 
			query = ValidateContractChecks2.getQuery(sqlFile2);
			strArr = ValidateContractChecks2.getDbData(conn, sqlFile, query, bookDate);
			Olyutil.printStrArray(strArr);
			System.out.println("********************************************************************************************************************"); 
			
			strArr = getDataStrArr(conn, sqlFile3, query, bookDate);	
			errArr = purchOptChk(strArr);
			//Olyutil.printStrArray(strArr);
			
			
			
			System.out.println("********************************************************************************************************************"); 
			query = ValidateContractChecks2.getQuery(sqlFile4);
			strArr = ValidateContractChecks2.getDbData(conn, sqlFile, query, bookDate);
			Olyutil.printStrArray(strArr);
			System.out.println("********************************************************************************************************************"); 
			query = ValidateContractChecks2.getQuery(sqlFile5);
			strArr = ValidateContractChecks2.getDbData(conn, sqlFile, query, bookDate);
			Olyutil.printStrArray(strArr);
			System.out.println("********************************************************************************************************************"); 
			query = ValidateContractChecks2.getQuery(sqlFile6);
			strArr = ValidateContractChecks2.getDbData(conn, sqlFile, query, bookDate);
			Olyutil.printStrArray(strArr);
			System.out.println("********************************************************************************************************************"); 
			*/
			conn.close();
			
		} catch (IOException | SQLException e) { e.printStackTrace(); }
		

	}
		
	 
	
	
}
