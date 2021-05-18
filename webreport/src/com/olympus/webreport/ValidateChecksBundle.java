
package com.olympus.webreport;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.olympus.olyutil.Olyutil;

public class ValidateChecksBundle {

	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	static String sqlFile1 = "C:\\Java_Dev\\props\\sql\\RateCheck.sql";
	static String sqlFile2 = "C:\\Java_Dev\\props\\sql\\FinalRateCheck.sql";
	static String sqlFile3 = "C:\\Java_Dev\\props\\sql\\purchOptionMatch_V2.sql";
	static String sqlFile3_BD = "C:\\Java_Dev\\props\\sql\\purchOptionMatchBookingDate_V1.sql";
	static String sqlFile4 = "C:\\Java_Dev\\props\\sql\\upfrontTaxCheck.sql";
	static String sqlFile5 = "C:\\Java_Dev\\props\\sql\\miscBillableFlagErrCheck.sql";
	static String sqlFile7 = "C:\\Java_Dev\\props\\sql\\PremiumProtection.sql";
	static String sqlFile8 = "C:\\Java_Dev\\props\\sql\\NBVAbuy\\BO-EUA_Check.sql";
	// static String sqlFile6 = "C:\\Java_Dev\\props\\sql\\taxRateChanges.sql";
	

	/*****************************************************************************************************************************************************/
	public static ArrayList<String> getDataStrArr(Connection conn, String sqlFileName, String bookDate)
			throws IOException, SQLException {
		ArrayList<String> strArr = new ArrayList<String>();
		String query = null;
		 //System.out.println("**** SQLFile=" +  sqlFileName    + "-- PARAM=" + bookDate + "-- Q=" + query);

		query = ValidateContractChecks2.getQuery(sqlFileName);
		strArr = ValidateContractChecks2.getDbData(conn, sqlFileName, query, bookDate);
		 //System.out.println("**** PARAM=" + bookDate + "-- Q=" + query);

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
	public static void printMap(Map mp) {
	    Iterator it = mp.entrySet().iterator();
	    int sz = mp.size();
	    
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println("*** Map Size= " + sz + " -- " + pair.getKey() + " = " + pair.getValue());
	        
	    }
	}
	/****************************************************************************************************************************************************/
	// Add code to check $1.00 buyout
	
	
	public static ArrayList<String> purchOptChk(ArrayList<String> strArr, HashMap<String, String> modelMap, String idVal) throws IOException, SQLException {

		ArrayList<String> errArr = new ArrayList<String>();
		String id = null;
		String asset = null;
		String assetPO = null; // asset PurchaseOpt
		String poCode = null;
		String del = null;
		String contractPO = null;
		String poContractDesc = null;
		String poAssetDesc = null;
		String model = null;
		String err = null;
		errArr.clear();
		String dollarBuyout = "$1.00 Buyout";
		
		//printMap(modelMap);
		for (String str : strArr) { // iterating ArrayList
			//System.out.println("**** Str=" + str);
			String[] items = str.split(":");
			id = items[0];
			asset = items[1];
			assetPO = items[2];
			contractPO = items[3];
			poCode = items[4];
			poContractDesc = items[5];
			poAssetDesc = items[6];
			model = items[7];
			//System.out.println("*^^ PCD=" + poContractDesc + "-- PAD=" + poAssetDesc + "-- Model=" + model + "--");
			// Fix code 20 -- 2021-02-05
			//System.out.println("*^^ValChkBndl^^*** ID="  + id + "--  ContractPurchOpt=" + po1 + "-- ContractPurchOptIL=" + poDB + "--");
			
			if (! idVal.equals(id)) {
				continue;
			}
			
			/*------------------------------------------------------------------------------------------------------------------------------------*/
			// Check Asset
			if (modelMap.containsKey(model)) {
				if (! poAssetDesc.equals(dollarBuyout)) {
					err = "AssetID: "+  asset + " is a consumable or soft asset, please code as $1.00 BuyOut.";
					errArr.add(err);
					// Contract check -- mixed or $1.00 buyout
					if (poContractDesc.equals(dollarBuyout)) { // check contract
						if (poCode.equals("01") || poCode.equals("11") || poCode.equals("16") || poCode.equals("20")) {
							
						} else {
							err = "AssetCheck: Contract contains assets with $1.00 BuyOut Purchase option, please correct contract purchase option.";
							errArr.add(err);
						}		
					}		
				}
				//System.out.println("***^^*** Model=" + model + "--PCD=" + poContractDesc + "-- PAD=" + poAssetDesc +   "--");
			}
			/*------------------------------------------------------------------------------------------------------------------------------------*/

			// Check Contract vs. Asset
			if (poAssetDesc.equals(dollarBuyout) ) {
				if (poCode.equals("01") || poCode.equals("11") || poCode.equals("16") || poCode.equals("20")) {
					
				} else {
					err = "Contract contains assets with $1.00 BuyOut Purchase option, please correct contract purchase option.";
					errArr.add(err);
				}		
			}
			
		
			/*------------------------------------------------------------------------------------------------------------------------------------*/

			if(contractPO.equals("20") || contractPO.equals("11") || contractPO.equals("16") ) {
				continue;
			}
			if (!assetPO.equals(contractPO)) {
				//System.out.println("**** ContractPurchOpt=" + po1 + "-- ContractPurchOptIL=" + poDB + "--");
				
				if (contractPO.equals("14")) {
					err = "Error: Purchase Option Check  -- ID: " + id + " -- Asset: " + asset + "  AssetPurchOpt: " + assetPO + " ContractPurchOptIL: "
							+ contractPO + " -- Code as: \" 20 -> Mixed Purchase Opt w/ cap\"";
				} else {
					err = "Error: Purchase Option Check  -- ID: " + id + " -- Asset: " + asset + "  AssetPurchOpt: " + assetPO + " ContractPurchOptIL: "
							+ contractPO;
				}
				
				
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
			System.out.println("***^^^*** ID=" + id + "-- ContractID=" + contractID + "--");
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
	public static String descResidualChk(ArrayList<String> strArr) throws IOException, SQLException {
		//System.out.println("***^^^**** running descResidualChk");
		String id = null;
		String billFlag = null;
		String err = null;

		for (String str : strArr) { // iterating ArrayList
			  //System.out.println("**** Str=" + str);
			String[] items = str.split(":");
			id = items[0];
			 
			if (! Olyutil.isNullStr(id)) {

				// System.out.println("***^^^**** Bill Flag == 0");
				err = "Error: Residual > 0 for 'B/O' or 'EUA' -- ID: " + id;
			}
		}
		return err;
	}
	
	/****************************************************************************************************************************************************/

	public static String premiumChk(ArrayList<String> strArr, String idVal) throws IOException, SQLException {

		String id = null;
		String code = null;
		String rent = null;
		String err = null;

		for (String str : strArr) { // iterating ArrayList
			//System.out.println("**** Str=" + str);
			String[] items = str.split(":");
			id = items[0];
			code = items[1];
			rent = items[2];
			//System.out.println("**** Premium Protection -> ID=" + id + "-- IDVAL=" + idVal + "-- Rent=" + rent + "--");
			
			if (id.equals(idVal) && rent.equals("0")) {
			 
				//System.out.println("**** Premium Protection -> ID=" + id + "-- IDVAL=" + idVal + "-- Rent=" + rent + "--");
				
				err = "Error: Premium Protection Check issue! -- ID: " + id + " Inc_In_Rent: " + rent;
			}
		}
		return err;
	}
	
	
	
	
	
	
	
}
/****************************************************************************************************************************************************/


