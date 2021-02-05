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
// RUN: http://localhost:8181/webreport/contractchkrange?date=2019-05-07
@WebServlet("/contractchkrange")
public class ContractReportRange extends HttpServlet  {

	//Service method of servlet
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res  = null;
	static NodeList  node  = null;
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\contractvalidate.sql";
	static String sqlIdFile = "C:\\Java_Dev\\props\\sql\\getcontractid_range.sql";

	//static String sqlFile = "C:\\Java_Dev\\props\\sql\\getcontracts.sql";

	/****************************************************************************************************************************************************/
	public static ArrayList<String> getDbDataRange(String startDate,  String endDate,  String sqlSrc) throws IOException {
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
		// System.out.println("*** ID:" + id + " -- arrSz:" + strArr.size()+ "--");	
		 //Olyutil.printStrArray(strArr);
		 
		 if ( strArr.size() <= 0) {
			 return 0;
		 }
		contracts = loadData(strArr );
		for (int k = 0; k < contracts.size(); k++) {
			System.out.println("*** Validating --> contractID:" + contracts.get(k).getContractID());
			 contractErrs = ValidateChecks.validateContract( contracts.get(k)); 
			 
			 //if (contractErrs.size() > 0) {
				 System.out.println("*****^^^^^^***** ID:" + id + " -- contractErrsSz:" + contractErrs.size()+ "--");	 
			// }
		}
		// System.out.println("*** ID:" + id + " -- contractErrsSz:" + contractErrs.size()+ "--");	
		for (String key : contractErrs.keySet()) {
			if (contractErrs.get(key.toString()).equals(true)) {
				errStatus = true;
				errCount++;
				//System.out.println("***!!!*** Validating --> contractID:" + id +  " **** Key: " + key + " -> Value: " + contractErrs.get(key.toString()));
			}
			
		}
		//contractErrs.clear(); 
		
		//System.out.println("************************************************ End *********************************************************************");
		return errCount;
	}
	
	
	
	/****************************************************************************************************************************************************/
	//RUN: http://localhost:8181/webreport/contractchkrange?startDate=2019-07-01&endDate=2019-07-24
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String dispatchJSP = "/valerrors.jsp";
		String sDate = "";
		String eDate = "";
		JSONArray jsonArr = new JSONArray();
		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<String> idArr = new ArrayList<String>();
		ArrayList<String> errArr = new ArrayList<String>();
		Map<String, Boolean> contractErrs = null; 
		boolean errStat = false;
		List<ContractData> contracts;
		PrintWriter out = response.getWriter();
		response.setContentType("text/JSON");
		 
		String paramName = "startDate";
		String paramValue = request.getParameter(paramName);
		
		String endDate = "endDate";
		String endDateValue = request.getParameter(endDate);
		
		
		int errCount = 0;
		if ((paramValue != null && !paramValue.isEmpty())) {	
			 sDate= paramValue.trim();
			// System.out.println("*** sDate:" + sDate + "--");			 
		}
		
		if ((endDateValue != null && !endDateValue.isEmpty())) {	
			 eDate= endDateValue.trim();
			  //System.out.println("*** eDate:" + eDate + "--");			 
		}
		
		
		idArr = getDbDataRange(sDate, eDate, sqlIdFile);
		//Olyutil.printStrArray(idArr);
		
		
		String id = "";
		String bookDate = "";
		for (String id_t : idArr) { // iterating ArrayList
			 //System.out.println("*** idVal:" + id + "--");		
			String[] line = id_t.split(":");
			id = line[0];
			bookDate = line[1];
			errCount = validateContractID(id);
			if (errCount > 0) {
				errArr.add(id + ":" + errCount + ":" + bookDate );
				errCount = 0;
			}
	
		}
		String url = "http://cvyhj3a27:8181/webreport/valboth?id=";
		for (String errID : errArr) {
			//System.out.println("*** Error Occurred in ContractID: " + errID + "--");
			JSONObject obj = new JSONObject();
			String[] items = errID.split(":");
			obj.put("date", items[2]);
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
		request.getSession().setAttribute("jsonArr", jsonArr);
		
		
		request.getRequestDispatcher(dispatchJSP).forward(request, response);

	} // End doGet
	

	
	/****************************************************************************************************************************************************/

	
	
	
	/****************************************************************************************************************************************************/




	
}

