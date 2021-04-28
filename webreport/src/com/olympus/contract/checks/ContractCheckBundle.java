package com.olympus.contract.checks;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.olympus.olyutil.Olyutil;
import com.olympus.webreport.*;

public class ContractCheckBundle {

	static Connection conn = null;
	static ResultSet res = null;
	static private PreparedStatement statement;
	static String sqlFile1 = "C:\\Java_Dev\\props\\sql\\RateCheck.sql";
	static String sqlFile2 = "C:\\Java_Dev\\props\\sql\\FinalRateCheck.sql";
	static String sqlFile3 = "C:\\Java_Dev\\props\\sql\\purchOptionMatch.sql";
	static String sqlFile4 = "C:\\Java_Dev\\props\\sql\\upfrontTaxCheck.sql";
	static String sqlFile5 = "C:\\Java_Dev\\props\\sql\\miscBillableFlagErrCheck.sql";
	static String sqlFile6 = "C:\\Java_Dev\\props\\sql\\PremiumProtection.sql";
	/*
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	
	//static String sqlFile6a = "C:\\Java_Dev\\props\\sql\\taxRateChanges.sql";
*/
	/*****************************************************************************************************************************************************/
	public static ArrayList<String> getDataStrArr(Connection conn, String sqlFileName, String bookDate)
			throws IOException, SQLException {
		ArrayList<String> strArr = new ArrayList<String>();
		String query = null;
		query = GetDbConnection.getQuery(sqlFileName);
		strArr = GetDbConnection.getDbData(conn, sqlFileName, query, bookDate);
		// System.out.println("**** Q=" + query);

		return strArr;
	}
	/*****************************************************************************************************************************************************/
	public static String rateChk(ArrayList<String> strArr) throws IOException, SQLException {

		String id = null;
		String err = null;
		String calcRate = null;
		String amt = null;

		for (String str : strArr) { // iterating ArrayList
			// System.out.println("**** Str=" + str);
			String[] items = str.split(":");
			id = items[0];
			calcRate = items[4];
			amt = items[5];
			if (!calcRate.equals(amt)) {
				// System.out.println("**** calcRate=" + calcRate + "-- AMT=" + amt + "--");
				err = "Error: RateCheck amounts do not match! -- ID: " + id + ": Amount: " + amt;
			}
		}
		return err;
	}
	/****************************************************************************************************************************************************/
	public static String finalRateChk(ArrayList<String> strArr) throws IOException, SQLException {

		String id = null;
		String asset = null;
		double calcUnits = 0.0;
		double units = 0.0;
		double diff = 0.0;
		String err = null;

		for (String str : strArr) { // iterating ArrayList
			// System.out.println("**** Str=" + str);
			String[] items = str.split(":");
			id = items[0];
			asset = items[1];
			units = Double.valueOf(items[2]);
			calcUnits = Double.valueOf(items[3]);
			diff = units - calcUnits;
			// System.out.println("**** DIFF=" + diff + "--- calcUnits=" + calcUnits + "--
			// units=" + units + "--");
			if (diff != 1.0) {
				// System.out.println("**** calcUnits=" + calcUnits + "-- units=" + units +
				// "--");
				err = "Error: Final Rate Check Unit issue! -- ID: " + id + ": Units: " + units + " CalcUnits: "
						+ calcUnits;
			}
		}
		return err;
	}
	/****************************************************************************************************************************************************/
	public static ArrayList<String> purchOptChk(ArrayList<String> strArr) throws IOException, SQLException {

		ArrayList<String> errArr = new ArrayList<String>();
		String id = null;
		String asset = null;
		String po1 = null;
		String poDB = null;
		String err = null;
		errArr.clear();

		for (String str : strArr) { // iterating ArrayList
			System.out.println("**** Str=" + str);
			String[] items = str.split(":");
			id = items[0];
			asset = items[1];
			po1 = items[2];
			poDB = items[3];
			System.out.println("*^^^^*** PO1=" + po1 + "-- poDB=" + poDB + "--");
			if(poDB.equals("20")) {
				continue;
			}
			if (!po1.equals(poDB)) {
				
				err = "Error: Purchase Option Check  -- ID: " + id + "Asset: " + asset + "  PO: " + po1 + " poDB: "
						+ poDB;
				errArr.add(err);
			}
		}
		return errArr;
	}
	/****************************************************************************************************************************************************/
	public static ArrayList<String> upFrontTaxChk(ArrayList<String> strArr, String contractID) throws IOException, SQLException {

		ArrayList<String> errArr = new ArrayList<String>();
		String id = null;
		String equipDesc = null;
		String model = null;
		double aOrigCost = 0.0;
		String stateDesc = null;
		String taxCode = null;
		String err = null;
		errArr.clear();

		for (String str : strArr) { // iterating ArrayList
			// System.out.println("**** Str=" + str);
			String[] items = str.split(":");
			id = items[0];
			equipDesc = items[2];
			model = items[3];
			aOrigCost = Double.valueOf(items[5]);
			stateDesc = items[8];
			taxCode = items[9];
			//System.out.println("***^^^*** ID=" + id + "-- ContractID=" + contractID + "--");
			if (id.equals(contractID)) {

				// System.out.println("**** equipDesc=" + equipDesc + "-- model=" + model + "--"
				// + "-- aOrigCost=" + aOrigCost + "--" + "-- stateDesc=" + stateDesc + "--" +
				// "-- taxCode=" + taxCode + "--" ) ;
				//System.out.println("**** ID=" + id + "--");
				if (((equipDesc.equals("UPFRONT TAX") || model.equals("UPFRONT TAX")) && aOrigCost > 0.0)) {
					// System.out.println("***^^^**** equipDesc=" + equipDesc + "-- model=" + model
					// + "--" + "-- aOrigCost=" + aOrigCost + "--" + "-- stateDesc=" + stateDesc +
					// "--" + "-- taxCode=" + taxCode + "--" ) ;
					errArr.add("Error: UpFrontTax_aOrigCost <> 0: -- ID: " + id + " aOrigCost: " + aOrigCost);
					 //System.out.println("***^^^**** UpFrontTax_aOrigCost <> 0 ");
				}
				if ((equipDesc.equals("ILLINOIS") && (!taxCode.equals("001")))) {
					errArr.add("Error: Illinois and TaxCode <> 001: -- ID: " + id + " taxCode: " + taxCode);
					// System.out.println("***^^^**** Illinois TaxCode <> 001");
				}
				if (!(equipDesc.equals("ILLINOIS") && (!taxCode.equals("100")))) {
					errArr.add("Error: Not Illinois and TaxCode <> 100 -- ID: :" + id + " taxCode: " + taxCode);
					 //System.out.println("***^^^**** Not Illinois and TaxCode <> 100");
				}
			
			} else {
				continue;
			}
			
		}
		//System.out.println("***^^^*** upFrontTaxChk errorArr:" + errArr.size() + "--");
		return errArr;
	}
	/****************************************************************************************************************************************************/
	public static String miscBillableFlagChk(ArrayList<String> strArr) throws IOException, SQLException {

		String id = null;
		String billFlag = null;
		String err = null;

		for (String str : strArr) { // iterating ArrayList
			// System.out.println("**** Str=" + str);
			String[] items = str.split(":");
			id = items[0];
			billFlag = items[1];

			if (billFlag.equals("0")) {

				// System.out.println("***^^^**** Bill Flag == 0");
				err = "Error: Billable Flag Check issue! -- ID: " + id + " Bill Flag: " + billFlag;
			}
		}
		return err;
	}
	/****************************************************************************************************************************************************/
	
	public static  String doValidation(Connection conn, String id, String bookDate) throws IOException, SQLException {
		ArrayList<String> strArr = new ArrayList<String>();
		 String errStr = "";
		
		if (conn != null) {
			 //System.out.println("Connected to the database -- ID:" + id + "--");
			strArr = getDataStrArr(conn,  sqlFile1, bookDate);
			Olyutil.printStrArray(strArr);
			errStr = AssetContractCheck.runChecks(id, bookDate, strArr);
		}
		 
 
		return (errStr);
	}

} // End Class

/****************************************************************************************************************************************************/