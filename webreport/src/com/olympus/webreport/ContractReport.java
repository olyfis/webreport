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

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.NodeList;

import com.microsoft.sqlserver.jdbc.StringUtils;
import com.olympus.infolease.contract.ContractData;
import com.olympus.olyutil.Olyutil;

@WebServlet("/contractchk")
public class ContractReport extends HttpServlet  {

	//Service method of servlet
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res  = null;
	static NodeList  node  = null;
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\contractvalidate.sql";
	static String sqlIdFile = "C:\\Java_Dev\\props\\sql\\getcontractid.sql";

	//static String sqlFile = "C:\\Java_Dev\\props\\sql\\getcontracts.sql";

	/****************************************************************************************************************************************************/

	/****************************************************************************************************************************************************/
	public static ArrayList<String> getDbData(String id, String sqlSrc) throws IOException {
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
		
		// be sure to not have line starting with "--" or "/*" or any other non alphabetical character
		BufferedReader br = new BufferedReader(fr);
		while((s = br.readLine()) != null){
		      sb.append(s);
		       
		}
		br.close();
		//displayProps(connectionProps);
		String query = new String();
		query = sb.toString();	
		//System.out.println("Query=" + query);	 
		try {
			con = Olyutil.getConnection(connectionProps);
			if (con != null) {
				//System.out.println("Connected to the database");
				statement = con.prepareStatement(query);
				
				//System.out.println("***^^^*** contractID=" + contractID);
				statement.setString(1, id);
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

	public static ContractData loadContractObj(String str,  List<ContractData> contracts ) throws IOException {
		
		ContractData contract = new ContractData();
		String[] items = str.split(":");
		//System.out.println("*** itemSz:" + items.length + "--");
		//printStr(items );
		contract.setContractID(items[0]);
		contract.setCustName(items[1]);
		contract.setLeaseType(items[2]);
		contract.setDateBooked(items[3]);
		contract.setDateCommenced(items[4]);
		contract.setTerm(items[5]);
		contract.setTermDate(items[6]);
		contract.setLeadDays(items[7]);
		contract.setFirstPayDate(items[8]);
		contract.setLastPayDate(items[9]);
		contract.setEquipCost(items[10]);
		contract.setEquipCost14(items[11]);
		contract.setOutOfBalance(items[12]);
		contract.setScreen3Yield(items[13]);
		contract.setScreen10Yield(items[14]);
		contract.setScreen14Yield(items[15]);
		contract.setScreen3Residual(items[16]);
		contract.setTotalOLResidual(items[17]);
		contract.setScreen14Residual(items[18]);			
		contract.setPoNumber(items[19]);				
		contract.setPoExpireDate(items[20]);		
		contract.setPayOption(items[21]);
		contract.setInvoiceCode(items[22]);
		contract.setInvoiceFormat(items[23]);
		contract.setContractPurchOpt(items[24]);
		contract.setTotalCppProcs(items[25]);
		contract.setLcGracePeriod(items[26]);
		contract.setProgramType(items[27]);
		contract.setProductGroup(items[28]);
		contract.setCppFirstReadDate(items[29]);
		contract.setAch(items[30]);
		contract.setInsurStatus(items[31]);
		contract.setLateChargeExempt(items[32]);
		contract.setLateChargePercent(items[33]);	 	
		contract.setUseTax(items[34]);
		contract.setAmAddlEmail(items[35]);
		contract.setAlphaNum2(items[36]);
		contract.setUserDate1(items[37]);
		contract.setVaVisn(items[38]);
		contract.setUmTable1(items[39]);
		
		
		
		return contract;
	}	
	/****************************************************************************************************************************************************/
	public static List<ContractData> loadData(ArrayList<String> strArr ) throws IOException {
		
		List<ContractData> contracts = new ArrayList<ContractData>();
		//System.out.println("****SZ=" + strArr.size()  +"--");
		
		int j = 0;
		for (String str : strArr) { // iterating ArrayList
			ContractData contract;
			//System.out.println(str);
			contract = loadContractObj(str, contracts);
			contracts.add(contract);
			j++;
		}
		return contracts;
	}		
	/****************************************************************************************************************************************************/
	public static String getLeadDate(String oldDate, String lead ) throws IOException {
		
		//System.out.println("Date before Addition: "+oldDate);
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
	public static Map<String, Boolean> validateContract( ContractData contract  ) throws IOException {
		Map<String, Boolean> contractErrs = new Hashtable<>(); 
		
		String id = contract.getContractID();
		String idPrefix = id.substring(0, 3);
		//System.out.println("*** contractID:" + contract.getContractID() + " prefix:" + idPrefix);
		
		
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
		
		String firstPayDate = getLeadDate(contract.getDateCommenced(), "30" );

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
	 if (isNullStr( contract.getEquipCost14())) {
		 equipCost14 = 0.00;
		 
	 } else {
		 String ec14 =  contract.getEquipCost14().replace(",", ""); 
		  equipCost14 = Double.valueOf(ec14);
	 }
		
		
		
		
		
		
		
		double diffCost = equipCost - equipCost14;
		
		//System.out.println("**^^** S3=" + contract.getScreen3Residual() + "--");
		String s3r = contract.getScreen3Residual();
		if (isNullStr(s3r  )) {
			 s3r = "0";
		}
		
		double residual = Double.valueOf(s3r);
		//int residual = Integer.parseInt(contract.getScreen3Residual());
		int term = Integer.parseInt(contract.getTerm());
		//System.out.println("*** diffCost:" + diffCost + "--");
		
		
		if (diffCost != 0.0) {
			System.out.println("Error in Equipment costs by: " + diffCost);
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
		
		System.out.println("umTable1=" + umTable1 + "--");
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
		if (! isNullStr(y14) ) {
			y14d = Double.parseDouble(y14);
		} else {
			nullYield = true;
		}
		if (contract.getLeaseType().equals("XX"))  {
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
	public static int  validateContractID( String id) throws IOException {
		ArrayList<String> strArr = new ArrayList<String>();
		Map<String, Boolean> contractErrs = null; 
		List<ContractData> contracts;
		boolean errStatus = false;
		int errCount = 0;
		//System.out.println("************************************************ Begin *********************************************************************");
		//System.out.println("** Processing ID: " +  id + "--");
		strArr = getDbData(id, sqlFile);
		//System.out.println("*** ID:" + id + " -- arrSz:" + strArr.size()+ "--");	
		//Olyutil.printStrArray(strArr);
		contracts = loadData(strArr );
		for (int k = 0; k < contracts.size(); k++) {
			//System.out.println("*** Validating --> contractID:" + contracts.get(k).getContractID());
			 contractErrs = validateContract( contracts.get(k)); 
			 
			 //if (contractErrs.size() > 0) {
				// System.out.println("*** ID:" + id + " -- contractErrsSz:" + contractErrs.size()+ "--");	 
			// }
		}
		for (String key : contractErrs.keySet()) {
			if (contractErrs.get(key.toString()).equals(true)) {
				errStatus = true;
				errCount++;
				//System.out.println("*** Validating --> contractID:" + id +  " **** Key: " + key + " -> Value: " + contractErrs.get(key.toString()));
			}
			
		}
		//contractErrs.clear(); 
		
		//System.out.println("************************************************ End *********************************************************************");
		return errCount;
	}
	
	
	
	/****************************************************************************************************************************************************/
	//http://localhost:8181/webreport/contractchk?date=2019-05-07
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String dispatchJSP = "/validatecontractresults.jsp";
		String qVal = "";
		JSONArray jsonArr = new JSONArray();
		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<String> idArr = new ArrayList<String>();
		ArrayList<String> errArr = new ArrayList<String>();
		Map<String, Boolean> contractErrs = null; 
		boolean errStat = false;
		List<ContractData> contracts;
		PrintWriter out = response.getWriter();
		response.setContentType("text/JSON");
		 
		String paramName = "date";
		String paramValue = request.getParameter(paramName);
		
		String endDate = "endDate";
		String endDateValue = request.getParameter(endDate);
		
		
		int errCount = 0;
		if ((paramValue != null && !paramValue.isEmpty())) {	
			 qVal= paramValue.trim();
			 //System.out.println("*** qVal:" + qVal + "--");			 
		}
		idArr = getDbData(qVal, sqlIdFile);
		//Olyutil.printStrArray(idArr);
		
		for (String id : idArr) { // iterating ArrayList
			 //System.out.println("*** idVal:" + id + "--");		
			errCount = validateContractID(id);
			if (errCount > 0) {
				errArr.add(id + ":" + errCount);
				errCount = 0;
			}
	
		}
		String url = "http://cvyhj3a27:8181/webreport/ilchk?id=";
		for (String errID : errArr) {
			//System.out.println("*** Error Occurred in ContractID: " + errID + "--");
			JSONObject obj = new JSONObject();
			String[] items = errID.split(":");
			obj.put("date", paramValue);
			obj.put("contract", items[0]);
			obj.put("errors", items[1]);
			obj.put("url", url+items[0]);
			jsonArr.put(obj);
		}
		
		
		
		for(int k = 0 ; k < jsonArr.length() ; k++) { 
			//System.out.println("*** SZ=" + jsonArr.length() );
			if  (k == 0 ) {
				out.write("[");
			}	
		 		
			if  (k == (jsonArr.length() -1) ) {
				out.write(jsonArr.getJSONObject(k).toString(4));			
			} else {
				out.write(jsonArr.getJSONObject(k).toString(4) + ",");	
				
			}	
		}
		if (jsonArr.length() > 0) {
			out.write("]");
		}
		 
		

	} // End doGet
	

	
	/****************************************************************************************************************************************************/

	
	
	
	/****************************************************************************************************************************************************/




	
}
