package com.olympus.webreport;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.olympus.asset.AssetValData;
import com.olympus.infolease.contract.ContractData;
import com.olympus.olyutil.Olyutil;

public class ValidateChecks {
	
	public static AssetValData loadAssetObj(String str, List<AssetValData> assets) throws IOException {

		AssetValData asset = new AssetValData();
		String[] items = str.split(";");
		//System.out.println("*** itemSz:" + items.length + "--");
		//System.out.println("STR=" + str );
		//printStr(items );
		 asset.setContract_no(items[0]);
		 asset.setCust_name(items[1]);
		 asset.setBooking_date(items[2]);
		 asset.setActiv_date(items[3]);
		 asset.setContract_term(items[4]);
		 asset.setTerm_date(items[5]);
		 asset.setId(items[6]);
		 asset.setProduct_line(items[7]);
		 asset.setAsset_vendor(items[8]);
		 asset.setEquip_desc(items[9]);
		 asset.setModel(items[10]);
		 asset.setNew_used(items[11]);
		 asset.setEquip_list_price(items[12]);
		 asset.setOrig_cost(items[13]);
		 asset.setEquip_cost(items[14]);
		 asset.setCost_pct_flag(items[15]);
		 asset.setResid_amt(items[16]);
		 asset.setOl_residual(items[17]);
		 asset.setAm_purchase_option(items[18]);
		 asset.setPur_option(items[19]);
		 asset.setAsset_classification(items[20]);
		 asset.setAum_user_amt1(items[21]);
		 asset.setState_tax_code(items[22]);
		 asset.setMisc_state_tax_code(items[23]);
		 asset.setCnty_tax_code(items[24]);
		 asset.setMisc_cnty_tax_code(items[25]);
		 asset.setCity_tax_code(items[26]);
		 asset.setMisc_city_tax_code(items[27]); 
		 asset.setAu_tax_misc(items[28]);
		 asset.setIfrs_code(items[29]);	 
		//System.out.println("%%%%%%%%%%%%%%%%%%%% USER_AMT1=" + items[21] + "--"  +" ID="  + items[0] + "--   "   + "AssetID="   + items[6]  + "--"  );	 
		return  asset;
	}
	
	/****************************************************************************************************************************************************/
	public static boolean isNumeric(String str) {
	    return str.matches("[+-]?\\d*(\\.\\d+)?");
	}
	
	/****************************************************************************************************************************************************/
public static List<AssetValData> loadData(ArrayList<String> strArr ) throws IOException {
		
		List<AssetValData> assets = new ArrayList<AssetValData>();
		//System.out.println("**** in loadData() -- SZ=" + strArr.size()  +"--");
		
		int j = 0;
		for (String str : strArr) { // iterating ArrayList
			AssetValData asset;
			//System.out.println("Asset:" + str + "--");
			asset = loadAssetObj(str, assets);
			assets.add(asset);
			j++;
		}
		return assets;
	}	
/****************************************************************************************************************************************************/

	public static void printStr(String[] items ) throws IOException {
		
		for (int i = 0; i <items.length; i++) {
			System.out.println("*** item[" + i + "]=" + items[i] + "--");		
		}
		
	}
	/****************************************************************************************************************************************************/
	public static boolean isNullStr(String str) {
	
	if (str== null  ||  str.equals("null") )  {	
		return true;
	}
	return false;
	}
	/****************************************************************************************************************************************************/
	public static String getLeadDate(String oldDate, String lead ) throws IOException {
		//System.out.println("****-----***** Date before Addition: "+oldDate);
		//Specifying date format that matches the given date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try{
		   //Setting the date to the given date
		   c.setTime(sdf.parse(oldDate));
		}catch(ParseException e){
			e.printStackTrace();
		 }
		   
		//Number of Days to add
		
		 int leadDays = Integer.parseInt(lead);
		c.add(Calendar.DAY_OF_MONTH, leadDays);  
		
		
		//Date after adding the days to the given date
		String newDate = sdf.format(c.getTime());  
		//Displaying the new Date after addition of Days
		//System.out.println("Commencement Date:" + oldDate  +  " -- Add Lead:" + lead + " -- Date after Addition: "+newDate);
		
		return newDate;
	}
				

	/****************************************************************************************************************************************************/

	/****************************************************************************************************************************************************/

	public static Map<String, Boolean> validateContract( ContractData contract  ) throws IOException {
		Map<String, Boolean> contractErrs = new Hashtable<>(); 
		
		String id = contract.getContractID();
		String idPrefix = id.substring(0, 3);
		//System.out.println("*** contractID:" + contract.getContractID() + " prefix:" + idPrefix);
		
		double tolerance = 0.75;
		/*
		System.out.println("*** contractID:" + contract.getContractID());
		System.out.println("*** contractName:" + contract.getCustName());
		System.out.println("*** leaseType:" + contracts.getLeaseType());
		System.out.println("*** dateBooked:" + contracts.getDateBooked());
		System.out.println("*** dateCommenced:" + contracts.getDateCommenced());
		System.out.println("*** leadDays:" + contracts.getLeadDays());
		System.out.println("*** equipCost14:" + contracts.getEquipCost14());
		System.out.println("*** LateChrg%:" + contracts.getLateChargePercent());
		System.out.println("*** firstPayment:" + contracts.getFirstPayDate());
		*/
		//String firstPayDate = getLeadDate(contracts.getDateCommenced(), contracts.getLeadDays() );
		if (Olyutil.isNullStr(contract.getDateCommenced())) {
			System.out.println("*** Error with Commencement Date:");
			contractErrs.put("Date Commenced", true);
			
		}
		String firstPayDate = getLeadDate(contract.getDateCommenced(), "30" );
		/*
		System.out.println("*** dateBooked:" + contracts.getDateBooked());
		System.out.println("*** dateCommenced:" + contracts.getDateCommenced());
		System.out.println("*** newFirstPayment:" +  firstPayDate + "--");
		System.out.println("*** firstPayment:" + contracts.getFirstPayDate() + "--");
		*/
		
		/*
		if (! firstPayDate.equals(contract.getFirstPayDate())) {
			System.out.println("*** Error with firstPaymentDate: Validate");
			contractErrs.put("First Pymt Date", true);
		}
		*/
		
		//System.out.println("*** termDate:" + contracts.get(0).getTermDate());
		//System.out.println("*** LastPaymentDate:" + contracts.get(0).getLastPayDate());
		if (! contract.getTermDate().equals(contract.getLastPayDate())) {
			System.out.println("*** Error with LastPaymentDate: Validate");
			contractErrs.put("Last Pymt Date", true);
		}
		 //System.out.println("*** equipCost:" + contract.getEquipCost() + "--");
	 //System.out.println("*** equipCost14:" + contract.getEquipCost14() + "--");
		
	 double equipCost = 0.00;
	 double equipCost14 = 0.00;
	 if (isNullStr( contract.getEquipCost())) {
		 equipCost = 0.00;
	 } else {
		  equipCost = Double.valueOf(contract.getEquipCost());
	 }
	 //System.out.println("**^^** EC=" + contract.getEquipCost14() + "--");
	 if (isNullStr( contract.getEquipCost14())) {
		 equipCost14 = 0.00;
		 
	 } else {
		 String ec14 =  contract.getEquipCost14().replace(",", ""); 
		  equipCost14 = Double.valueOf(ec14);
	 }
		
		
		
		
		
		
		
		double diffCost = equipCost - equipCost14;
		//System.out.println("****  Equipment cost: " + equipCost + " equipCost14=" + equipCost14);
		//System.out.println("**^^** S3=" + contract.getScreen3Residual() + "--");
		String olTotalResidual = contract.getTotalOLResidual().replace(",", "");
		String screen14Residual = contract.getScreen14Residual().replace(",", "");
		String s3r = contract.getScreen3Residual();
		if (isNullStr(s3r  )) {
			 s3r = "0";
		}
		if (isNullStr(olTotalResidual  )) {
			olTotalResidual = "0";
		}
		if (isNullStr(screen14Residual  )) {
			screen14Residual = "0";
		}
		
		double screen14ResidualValue = Double.valueOf(screen14Residual);
		double olTotalResidualValue = Double.valueOf(olTotalResidual);
		double residual = Double.valueOf(s3r);
		
		
		//int residual = Integer.parseInt(contract.getScreen3Residual());
		int term = Integer.parseInt(contract.getTerm());
		//System.out.println("*** diffCost:" + diffCost + "--");
		
		//System.out.println("****  Equipment costs by: " + diffCost + " Tolerance=" + tolerance);
		if ((diffCost != 0.0  && Math.abs(diffCost) > tolerance)) {
			System.out.println("Error in Equipment costs by: " + diffCost + " Tolerance=" + tolerance);
			///System.out.println("***^^^*** equipCost:" + contract.getEquipCost() + "--");
			//contractErrs.put("Equip. Cost", true);
			contractErrs.put("Screen 14 Equip. Cost", true);
		}
		
		if ( ! contract.getOutOfBalance().equals("0")) {
			System.out.println("Error in outOfBalance: " + contract.getOutOfBalance() + " Should be 0.");
			contractErrs.put("Out Of Balance", true);
		}
		if ( ! contract.getLcGracePeriod().equals("85")) {
			System.out.println("Error in LC Grace Period: " + contract.getLcGracePeriod() + " Should be 85.");
			contractErrs.put("LC Grace Period", true);
		}
		if (  contract.getInvoiceCode().equals("S") &&  ! contract.getInvoiceFormat().equals("MLCPP") ) {
			System.out.println("Error in Invoice Code Format: " + contract.getInvoiceFormat() + " Should be MLCPP.");
			contractErrs.put("Invoice Format", true);	
		}
		
		
		
		/************************************************************************************************************************************/

		
		// New checks
		if (  contract.getInvoiceCode().equals("C") &&  ( contract.getProgramType().equals("0040") ||  contract.getProgramType().equals("0048"))) {
			if (contract.getInvoiceFormat().equals("ML-NO")) {
				String val = "OK";
			} else {
				System.out.println("Error in Invoice Code Format: " + contract.getInvoiceFormat() + " Should be ML-NO.");
				contractErrs.put("Invoice Format", true);	
			}
		} else if (  contract.getInvoiceCode().equals("C") &&  ! contract.getInvoiceFormat().equals("ML-IV") ) {
			System.out.println("Error in Invoice Code Format: " + contract.getInvoiceFormat() + " Should be ML-IV.");
			contractErrs.put("Invoice Format", true);	
		}
		
	 
		if (  contract.getInvoiceCode().equals("A") &&  ( contract.getProgramType().equals("0040") ||  contract.getProgramType().equals("0048"))) {
			if (contract.getInvoiceFormat().equals("MLNOA")) {
				String val = "OK";
			} else {
				System.out.println("Error in Invoice Code Format: " + contract.getInvoiceFormat() + " Should be MLNOA.");
				contractErrs.put("Invoice Format", true);	
			}
		} else if (  contract.getInvoiceCode().equals("A") &&  ! contract.getInvoiceFormat().equals("MLIVA") ) {
			System.out.println("Error in Invoice Code Format: " + contract.getInvoiceFormat() + " Should be MLIVA.");
			contractErrs.put("Invoice Format", true);	
		}
		
	
		/*************************************************************************************************************************************/
	
		 
		if (  idPrefix.equals("102") &&  ( !contract.getInvoiceCode().equals("S")   )) {
			System.out.println("Error in Invoice Code Format: " + contract.getInvoiceFormat() + " Should be S.");
			contractErrs.put("Invoice Code", true);	
		}
		 
		if (  idPrefix.equals("101") ) {
			if ( ! (( contract.getInvoiceCode().equals("A"))   ||  ( contract.getInvoiceCode().equals("C")) )     ) {
				System.out.println("Error in Invoice Code Format: " + contract.getInvoiceCode() + " Should be A or C");
				contractErrs.put("Invoice Code", true);	
			}
		}
		
		if (  idPrefix.equals("102") &&  ( !contract.getPayOption().equals("A")   )) {
			System.out.println("Error in Pymt Option: " + contract.getPayOption() + " Should be A.");
			contractErrs.put("Pymt Option", true);	
		}
		if (  idPrefix.equals("101") &&  ( !contract.getPayOption().equals("N")   )) {
			System.out.println("Error in Pymt Option: " + contract.getPayOption() + " Should be N.");
			contractErrs.put("Pymt Option", true);	
		}
		// Fix Po Expire check -- 2019-05-16
		if ( (contract.getProgramType().equals("0040") ||  contract.getProgramType().equals("0048")  )&&   contract.getPoExpireDate() == null ) {
			System.out.println("Error in PoExpire Date: " + contract.getPoExpireDate() + " Should not be null.");
			contractErrs.put("PO Expire Date", true);	
		}
		// Add VAcheck -- 2019-05-16
		if ( (contract.getProgramType().equals("0040") ||  contract.getProgramType().equals("0048")  )  && isNullStr(contract.getVaVisn() )  ) {
			  
			System.out.println("Error in VA_VISN: " + contract.getVaVisn()  + " Should not be null.");
			contractErrs.put("VA_VISN", true);	
			
		}
		
		
		if ( (contract.getProgramType().equals("0040") ||  contract.getProgramType().equals("0048")  )  && isNullStr(contract.getPoExpireDate() )  ) {
			  
			System.out.println("Error in PoExpire Date: " + contract.getPoExpireDate() + " Should not be null.");
			contractErrs.put("PO Expire Date", true);	
			
		}
		
		if ( (  contract.getProgramType().equals("0048") && contract.getLeaseType().equals("XX") )&&   contract.getPoNumber() == null ) {
			System.out.println("Error in Po Number: " + contract.getPoNumber() + " Should not be null.");
			contractErrs.put("PO Number", true);	
		}
// 2020-04-08 -- Fix insurance code null issue
		if (   Olyutil.isNullStr(contract.getInsurStatus()) ) {
			 
				//System.out.println("Error in Insurance code.");
				contractErrs.put("Insurance Status", true);
			 
			
		}
		
		/******************************************************* Begin Yield Check *************************************************************************/	
		// Fix yield check -- 2019-05-16	
		String y3 = contract.getScreen3Yield().toString();
		String y10 = contract.getScreen10Yield().toString();
		String y14 = contract.getScreen14Yield().toString();
		String umTable1 = contract.getUmTable1().toString();
		double y3d = 0.0;
		double y10d = 0.0;
		double y14d = 0.0;
		boolean nullYield = false;
		
		//System.out.println("umTable1=" + umTable1 + "--");
		// Get Yield values
		
		if (! isNullStr(y3) ) {
			y3d = Double.parseDouble(y3);
		} else {
			nullYield = true;	
		}
		if (! isNullStr(y10) ) {
			y10d = Double.parseDouble(y10);
		} else {
			nullYield = true;
		}
		
		//System.out.println("****** y14=" + y14 +"--");
		if (! isNullStr(y14) ) {
			y14d = Double.parseDouble(y14.replace("%", ""));
		} else {
			nullYield = true;
		}
		if (contract.getLeaseType().equals("TL") ) {
			if (screen14ResidualValue != residual) {
				//System.out.println("Error in Screen 3 Residual:  Should all be the same as Scree14 Residual.");
				contractErrs.put("Screen 14 Residual", true);
				contractErrs.put("Screen 3 Residual", true);
				
			}
		}
		
		
		if (contract.getLeaseType().equals("XX"))  {
			if (screen14ResidualValue != olTotalResidualValue) {
				System.out.println("Error in Total Residual:  Should all be the same as Scree14 Residual.");
				contractErrs.put("Screen 14 Residual", true);
				contractErrs.put("Total OL Residual", true);
			}
			
			
			if ((y3d == y10d)  && nullYield == false) {
				int ok = 0;
			} else {
				System.out.println("Error in Yield:  Should all be the same.");
				contractErrs.put("Screen 3 Yield", true);
				contractErrs.put("Screen 10 Yield", true);
				  if (! umTable1.equals("7"))  { 
					  contractErrs.put("Screen 14 Yield", true);
				  }	
			}
		} else { // Not XX				
			//System.out.println("***y3=" +y3 + "--  " + "10=" + y10 + "--  " + "y14=" +y14 + "--" );
			if ( contract.getScreen3Yield().equals(contract.getScreen10Yield()) &&  contract.getScreen3Yield().equals(contract.getScreen14Yield())) {
				int ok = 0;
			} else { 
				if ((y3d == y10d) && (y3d == y14d)   && nullYield == false    ) {
					int ok = 0;
				} else {	
					System.out.println("Error in Yield:  Should all be the same.");
					contractErrs.put("Screen 3 Yield", true);
					contractErrs.put("Screen 10 Yield", true);	
					contractErrs.put("Screen 14 Yield", true);	
				}	
			}
		} // End Else not xx

	/******************************************************* End Yield Check *************************************************************************/	
		
		if ( ( contract.getProgramType().equals("0040") ||  contract.getProgramType().equals("0039") || contract.getProgramType().equals("0048") ) && ( contract.getLeaseType().equals("TL")  || contract.getLeaseType().equals("LP")   ) ) {
			System.out.println("Error in LeaseType: " + contract.getLeaseType() + " Lease Type error -- TL or LP.");
			contractErrs.put("Lease Type", true);	
		}  
			
		if ( ( contract.getProgramType().equals("0040") ||  contract.getProgramType().equals("0039") || contract.getProgramType().equals("0048") ) && ( contract.getLeaseType().equals("XX")   ) ) {
			//System.out.println("LeaseType: OK -- PT= 0039 0040 0048  and LT=XX");	
		} else {

		
		
			if ( contract.getScreen3Residual().equals("0") &&   contract.getLeaseType().equals("LP") ) {
				//System.out.println("LeaseType: OK -- RES:0 && LP");		
			} else if ((residual > 0 && term <= 48)  &&  contract.getLeaseType().equals("TL") ) {
				//System.out.println("LeaseType: OK -- RES: < 0 && TL");
			} else if ((residual > 0 && term > 48)  &&  contract.getLeaseType().equals("LP") ) {
				//System.out.println("LeaseType: OK -- RES: < 0 && TL");
			}  else  {			
				System.out.println("Error in LeaseType: see log");
				contractErrs.put("Lease Type", true);				
			}	
		}

		if ( (residual > 0   &&   term > 48)) {				
			if ( contract.getLeaseType().equals("LP") && (! contract.getProgramType().equals("0040") || ! contract.getProgramType().equals("0048") ) ) {
				//System.out.println("LeaseType > 48: OK");
			}  else {
				System.out.println("Error in LeaseType: " + contract.getLeaseType() + " Should not be null.");
				System.out.println("***!!!! > 48 Res=" + residual + "--" + "Term=" + term + "--");	
				contractErrs.put("Lease Type", true);
			}
		}
		if ( (residual > 0   &&   term <= 48)) {				
				if ( contract.getLeaseType().equals("TL") && (! contract.getProgramType().equals("0040") || ! contract.getProgramType().equals("0048") ) ) {
					//System.out.println("LeaseType <= 48: OK");
				}  else {
					System.out.println("Error in LeaseType: " + contract.getLeaseType() + " Should not be null.");
					System.out.println("***!!!! <= 48 Res=" + residual + "--" + "Term=" + term + "--");	
					contractErrs.put("Lease Type", true);
			}
		}	

		if (idPrefix.equals("102") && contract.getUseTax().equals("D")) {
			String ok = "true";
		} else if (idPrefix.equals("101") && contract.getUseTax().equals("L")) {  
			String ok = "true";
		} else {
			System.out.println("Error in UseTax: " + contract.getUseTax() + " 102 -- Should be D or 101 -- Should be L"); 
			contractErrs.put("Use Tax", true);
		}
		
		//System.out.println("AddlEmail:" +  contract.getAmAddlEmail() + "--");
		if (idPrefix.equals("102") && (contract.getAmAddlEmail() == null ||  contract.getAmAddlEmail().equals("null")   )) {
			System.out.println("Error in Addition Email:  Should be not be Null"); 
			contractErrs.put("Addl Email", true);
		}
		int n = contract.getAlphaNum2().indexOf(',');
		
		
		
		if (idPrefix.equals("102") && (contract.getAlphaNum2() == null ||  contract.getAmAddlEmail().equals("null") ||  n != -1   )) {
			System.out.println("Error in Alpha num2:  Should be not be Null || have a comma"); 
			contractErrs.put("Alpha Num2", true);
		}
		
		
		//System.out.println("UserDate=" + contract.getUserDate1().toString() + "--");
		if (idPrefix.equals("102") && ! (contract.getUserDate1().equals(contract.getDateCommenced())  )) {
			System.out.println("Error in User Date1:  Should Equal Commencement date "); 
			contractErrs.put("User Date1", true);
		}
		
		
		return contractErrs;
		
	}
	
	/****************************************************************************************************************************************************/

	/****************************************************************************************************************************************************/

	//public static  JSONObject   validateAssetChk( AssetValData asset  ) throws IOException {
	public static  String   validateAssetChk( AssetValData asset  ) throws IOException {
		//System.out.println("----- in validateAssetChk --- ");
		String eStat = "";
		String e = "";
		Map<String, Boolean> assetErrs = new Hashtable<>(); 
		String[] model1 = {"BILLED & OUT", "ETA ROLLOVER", "FREIGHT", " INTERIM INT", "LEASE ADJ", "PROCESS FEE", "PROP TAX", "PROPERTY TAX", " UCB2500", "SERVICE", " FINANCED BUYOUT"}; 
		String[] assetVendor = {"0000430000", "0000470000", "0000490000", "0000500000", "0000510000", "0000580000", "0000610000", "0000630000", "0000680000", "0001170000" };
		String[] model2 = {"ESG-300", "WA90300W", "WA90004W"};
		String[] av45 = {"0000010000", "0000430000", "0000990000", "0000210000", "0001190000"};
		String[] av46 = {"0000040000", "0000470000", "0000980000", "0000210000"};
		String[] av65 = {"0000310000", "0000500000", "0000950000", "0000210000"};
		String[] av66 = {"0000440000", "0000510000", "0000960000", "0000210000"};
		String[] av67 = {"0000620000", "0000630000", "0000970000", "0000210000"};
		String[] av24 = {"0000670000", "0000680000", "0000920000"};
		String[] av25 = {"0000570000", "0000580000", "0000910000"};
		String[] av30 = {"0000200000", "0000490000", "0000940000"};
		String[] av35 = {"0000600000", "0000610000", "0000940000"};
		String[] av68 = {"0001160000", "0001170000", "0001180000", "0000210000"};
		String[] avUft = {"0000990000", "0000980000", "0000950000", "0000960000", "0000970000", "0000900000", "0000910000", "0000920000", "0000930000", "0000940000", "0001180000"};

		String errorStr = "";
		String errRtn = "";
		String contractID = asset.getContract_no();
		boolean error = false;
		String idPrefix = contractID.substring(0, 3);
		String assetID = asset.getId();
		JSONObject obj = new JSONObject();
		//System.out.println("*** TEST ");
		//errorStr = contractID + ";" + assetID;
		//errorStr =  assetID;
		// System.out.println("*** !!^^!! *** ID:" + asset.getContract_no() + " -- Cust:"  + asset.getCust_name()     ); 
		
		if (isNullStr(asset.getPur_option())) {
			e = "AssetID:"  + assetID  + " Error in PurOption: " + asset.getPur_option() + " Should not be null.";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("PUR_OPTION", "PUR_OPTION");
		}
		if (isNullStr(asset.getAsset_vendor())) {
			e = "AssetID:"  + assetID  + " Error in Asset Vendor: " + asset.getAsset_vendor() + " Should not be null.";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("ASSET_VENDOR", "ASSET_VENDOR");
		}
		
		//System.out.println("*** !!^^!! ***" + asset.getModel() + "--");
		 
		 
		boolean modelStat = Arrays.asList(model1).contains(asset.getModel());
		boolean vendorStat = Arrays.asList(assetVendor).contains(asset.getAsset_vendor());
		boolean modelStat2 = Arrays.asList(model2).contains(asset.getModel());
		if (modelStat && ! vendorStat) {
			e = "AssetID:"  + assetID  + " Error in Asset Vendor/Model: ";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("ASSET_VENDOR/Model", "ASSET_VENDOR/Model");
		}
		
		
		if (!(asset.getAsset_vendor().equals("0001190000")) &&  modelStat2  ) {
			e = "AssetID:"  + assetID  + " Error in ET Capital Equipment Vendor: ";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("ASSET_VENDOR2/Model2", "ASSET_VENDOR2/Model2");
		}
				
		//System.out.println("*** !!^^!! ***" + asset.getProduct_line() + "--    "  + "vendor:" +  asset.getAsset_vendor() + "--" );
 
		/*
		// Add fix for asset errors -- 2019-11-13
		if (asset.getProduct_line().equals("0045") ) {
			if (asset.getAsset_vendor().equals("0000010000")  || asset.getAsset_vendor().equals("0000430000")  ||  asset.getAsset_vendor().equals("0000990000")    ) {
				if (asset.getModel().equals("ESG-300") ||  asset.getModel().equals("WA90300W") ||  asset.getModel().equals("WA90004W")) {
					if ( asset.getAsset_vendor().equals("0000210000")   ) {
					  eStat = "noError";
					} else {
						eStat = "Error";
					}
				
				}
			}
		} 
		
		*/
		
		 
		if (asset.getProduct_line().equals("0045")     && !  Arrays.asList(av45).contains(asset.getAsset_vendor())) {
			e = "AssetID:"  + assetID  + " Error in Asset Vendor/ProductLine 0045: (Asset Vendor does not match Contract Product)";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("Asset Vendor/ProductLine 0045", "Asset Vendor/ProductLine 0045");	
		}
			
		if (asset.getProduct_line().equals("0046")     && !  Arrays.asList(av46).contains(asset.getAsset_vendor())) {
			e = "AssetID:"  + assetID  + " Error in Asset Vendor/ProductLine 0046: (Asset Vendor does not match Contract Product)";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("Asset Vendor/ProductLine 0046", "Asset Vendor/ProductLine 0046");	
		}
		
		if (asset.getProduct_line().equals("0065")     && !  Arrays.asList(av65).contains(asset.getAsset_vendor())) {
			e = "AssetID:"  + assetID  + " Error in Asset Vendor/ProductLine 0065: (Asset Vendor does not match Contract Product)";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("Asset Vendor/ProductLine 0065", "Asset Vendor/ProductLine 0065");	
		}
		if (asset.getProduct_line().equals("0066")     && !  Arrays.asList(av66).contains(asset.getAsset_vendor())) {
			e = "AssetID:"  + assetID  + " Error in Asset Vendor/ProductLine 0066: (Asset Vendor does not match Contract Product)";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("Asset Vendor/ProductLine 0066", "Asset Vendor/ProductLine 0066");	
		}
		
		if (asset.getProduct_line().equals("0067")     && !  Arrays.asList(av67).contains(asset.getAsset_vendor())) {
			System.out.println("*****  Error in Asset Vendor/ProductLine 0067 -- Asset Vendor:"  + asset.getAsset_vendor() );
			e = "AssetID:"  + assetID  + " Error in Asset Vendor/ProductLine 0067: (Asset Vendor does not match Contract Product)";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("Asset Vendor/ProductLine 0067", "Asset Vendor/ProductLine 0067");	
		}
		if (asset.getProduct_line().equals("0024")     && !  Arrays.asList(av24).contains(asset.getAsset_vendor())) {
			e = "AssetID:"  + assetID  + " Error in Asset Vendor/ProductLine 0024: (Asset Vendor does not match Contract Product)";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("Asset Vendor/ProductLine 0024", "Asset Vendor/ProductLine 0024");	
		}
		
		if (asset.getProduct_line().equals("0025")     && !  Arrays.asList(av25).contains(asset.getAsset_vendor())) {
			e = "AssetID:"  + assetID  + " Error in Asset Vendor/ProductLine 0025: (Asset Vendor does not match Contract Product)";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("Asset Vendor/ProductLine 0025", "Asset Vendor/ProductLine 0025");	
		}
		if (asset.getProduct_line().equals("0030")     && !  Arrays.asList(av30).contains(asset.getAsset_vendor())) {
			e = "AssetID:"  + assetID  + " Error in Asset Vendor/ProductLine 0030: (Asset Vendor does not match Contract Product)";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("Asset Vendor/ProductLine 0030", "Asset Vendor/ProductLine 0030");	
		}
		if (asset.getProduct_line().equals("0035")     && !  Arrays.asList(av35).contains(asset.getAsset_vendor())) {
			e = "AssetID:"  + assetID  + " Error in Asset Vendor/ProductLine 0035: (Asset Vendor does not match Contract Product)";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("Asset Vendor/ProductLine 0035", "Asset Vendor/ProductLine 0035");	
		}
		if (asset.getProduct_line().equals("0068")     && !  Arrays.asList(av35).contains(asset.getAsset_vendor())) {
			e = "AssetID:"  + assetID  + " Error in Asset Vendor/ProductLine 0068: (Asset Vendor does not match Contract Product)";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("Asset Vendor/ProductLine 0068", "Asset Vendor/ProductLine 0068");	
		}
		
		if (asset.getEquip_desc().equals("UPFRONT TAX") && !  Arrays.asList(avUft).contains(asset.getAsset_vendor()) ) {
			e = "AssetID:"  + assetID  + " Error in Asset Vendor/Upfront Tax ";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("Asset Vendor/Upfront Tax", "Asset Vendor/Upfront Tax");	
		}
		
		//System.out.println("*** !!^^!! ***" + asset.getEquip_desc() + "--    "  + "UFTax:" +  asset.getAu_tax_misc() + "--" );
		double equipCostd = 0.0;
		double origCostd = 0.0;
		double userAmt1 = 0.0;
		boolean ecNull = false;
		if (! isNullStr(asset.getEquip_cost()) ) {
			equipCostd = Double.parseDouble(asset.getEquip_cost());		
		} else  {
			ecNull = true;
		}
	
		if (! isNullStr(asset.getOrig_cost()) ) {
			origCostd = Double.parseDouble(asset.getOrig_cost());
		}   
		//System.out.println("*** !!^^!! *** equipCostd:" + equipCostd + "-- origCostd:" + origCostd + "--");
		if ((equipCostd == 0 && origCostd != 0 ) ||  (ecNull &&   origCostd != 0  )) 	{
			e = "AssetID:"  + assetID  + " Error in EquipCost/OrigCost: ";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("EquipCost/OrigCost", "EquipCost/OrigCost");	
		}
			
		if (idPrefix.equals("102") && asset.getAu_tax_misc().equals("0"))	 {
			e = "AssetID:"  + assetID  + " Error in AuTaxMisc:  should not be 0";
			System.out.println(e );	 
			errorStr +=   e + ";";
			error = true;
			//obj.put("AU_Tax_Misc", "AU_Tax_Misc");		
		}
		
		if ( ! asset.getCost_pct_flag().equals("1"))	 {
			e = "AssetID:"  + assetID  + " Error in Cost_PCT_Flag: should equal 1";
			System.out.println(e );
			errorStr +=   e + ";";
			error = true;
			//obj.put("Cost_PCT_Flag", "Cost_PCT_Flag");		
		}
		//System.out.println("***** AMT1=" + asset.getAum_user_amt1() + "--");

		if (! isNullStr(asset.getAum_user_amt1()) ) {
			userAmt1 = Double.parseDouble(asset.getAum_user_amt1());		
		}
		if (equipCostd == 0 && userAmt1 > 0) {
			e = "AssetID:"  + assetID  + " Error in user Amt 1";
			System.out.println(e);
			errorStr +=   e + ";";
			error = true;
			//obj.put("User_AMT1", "User_AMT1");		
		}
		
		if (asset.getEquip_desc().equals("UPFRONT TAX") && ((  ! isNullStr(asset.getOrig_cost()) &&  origCostd != 0 ))) {
			e = "AssetID:"  + assetID  + " Error in UPFRONT TAX/OrigCost:  ";
			System.out.println(e);
			errorStr +=   e + ";";		 
			error = true;
			//obj.put("UPFRONT TAX/OrigCost", "UPFRONT TAX/OrigCost");		
		}

		
		if (isNullStr(asset.getAsset_classification() )) {
			e = "AssetID:"  + assetID  + " Error in Asset_Classification: should not be null.";
			System.out.println(e);
			errorStr +=   e + ";";	
			error = true;
			//obj.put("Asset_Classification", "Asset_Classification");		
		}
		if ((equipCostd > 0 && asset.getIfrs_code().equals("OL"))  || (asset.getIfrs_code().equals("VA")  && userAmt1 == 0) ||  isNullStr(asset.getAum_user_amt1()  )  )  {
			e = "AssetID:"  + assetID  + " Error: Missing COGS";
			System.out.println(e );
			errorStr +=   e + ";";	
			error = true;
			//obj.put("IFRS Code", "IFRS Code");		
		}
		
		if (error != true) {
			return  "noErrors"; 
		} else {
			//obj.put("ContractID", asset.getContract_no());
			//obj.put("AssetID", asset.getId());
			
		}
	 
		return errorStr;
	}
	
	/****************************************************************************************************************************************************/
}
