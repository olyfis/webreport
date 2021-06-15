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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
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
import com.olympus.asset.AssetValData;
import com.olympus.infolease.contract.ContractData;
import com.olympus.olyutil.Olyutil;

	@WebServlet("/valboth")
public class ValidateContractAsset extends HttpServlet {
/*
		 Purchase Options	
01	$1.00 Buyout
02	FMV - 36 Months
03	10% Fixed Purchase Option
04	FMV - 12 Months
05	FMV - 24 Months
06	FMV - 48 Months
07	FMV - 60 Months
08	FMV
09	Balloon Payment
10	Loan
11	Special Purchase Option
12	Fixed Purchase Option
13	Installment / PO Sale
14	FMV With CAP
15	Variable Pmt Non EBO
16	Mixed Purchase Options
17	Subscriptions
18	EOT Return
19	Stipulated Value
20	Mixed Purchase Opt. w. Cap

 
*/
		 
		static String sqlFile1 = "C:\\Java_Dev\\props\\sql\\RateCheck.sql";
		static String sqlFile2 = "C:\\Java_Dev\\props\\sql\\FinalRateCheck.sql";
		static String sqlFile3 = "C:\\Java_Dev\\props\\sql\\purchOptionMatch_V2.sql";
		static String sqlFile4 = "C:\\Java_Dev\\props\\sql\\upfrontTaxCheck.sql";
		static String sqlFile5 = "C:\\Java_Dev\\props\\sql\\miscBillableFlagErrCheck.sql";
		static String sqlFile6 = "C:\\Java_Dev\\props\\sql\\taxRateChanges.sql";
		static String sqlFile7 = "C:\\Java_Dev\\props\\sql\\PremiumProtection.sql";
		
		// Added new check 2021-05-18 -- JB
		static String sqlFile8 = "C:\\Java_Dev\\props\\sql\\BO-EUA_Check.sql";

		// Added new check 2021-06-10 -- JB
		static String sqlFile9 = "C:\\Java_Dev\\props\\sql\\PPT_PassThru.sql";

		
		// Service method of servlet
					static Statement stmt = null;
					static Connection con = null;
					static ResultSet res  = null;
					static NodeList  node  = null;
					static String s = null;
					static private PreparedStatement statement;
					static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
					static String modelDataFile = "C:\\Java_Dev\\props\\ContractValidation\\modelData.csv";
					static String sqlFileContract = "C:\\Java_Dev\\props\\sql\\contractvalidate.sql";
					 static String sqlFileAsset = "C:\\Java_Dev\\props\\sql\\assetValByContract.sql";
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
								if (qType.equals("asset")) {
									statement.setString(1, booked);
									statement.setString(2, id);
									sep = ";";
									
								} else if (qType.equals("contract")) {
									statement.setString(1, id);
									sep = ":";
								}
								
								 
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
					public static ArrayList<String> validateAssets(String idVal, String bookDate) throws IOException {
						ArrayList<String> assetArr = new ArrayList<String>();
						List<AssetValData> assets;
						JSONArray jsonArr = new JSONArray();
						JSONObject jsonObj;
						String err = "";
						ArrayList<String> errArr = new ArrayList<String>();
						assetArr = getDbData(idVal, sqlFileAsset, bookDate, "asset");
						//System.out.println("***%%%*** validateAssets(): assetArrSz:" + assetArr.size()+ "--");	
						//Olyutil.printStrArray(assetArr);
						assets = ValidateChecks.loadData(assetArr);
						//System.out.println("*** !!! *** ID:" + idVal + "-- numAssets:" + assets.size() );
						for (int k = 0; k < assets.size(); k++) {
							 //System.out.println("*** !!! *** ID:" + assets.get(k).getContract_no() + " -- Cust:"  + assets.get(k).getCust_name()     ); 
							// jsonObj = ValidateChecks.validateAssetChk( assets.get(k));
							err = ValidateChecks.validateAssetChk( assets.get(k));
							if (! err.equals("noErrors")) {
								errArr.add(err);
							}
						 }	 
						return errArr;
					}
					
					/****************************************************************************************************************************************************/
					public static  HashMap<String, String> buildModelMap(ArrayList<String> strArr) throws IOException {
						HashMap<String, String> rtnMap = new HashMap<String, String>();
						
						for (String str : strArr) {
							if (! Olyutil.isNullStr(str)) {
								rtnMap.put(str, str);
							}	
						}
						return(rtnMap);
					}
					/****************************************************************************************************************************************************/
					public static void printMap(Map mp) {
					    Iterator it = mp.entrySet().iterator();
					    int sz = mp.size();
					    
					    while (it.hasNext()) {
					        Map.Entry pair = (Map.Entry)it.next();
					        System.out.println("Map Size= " + sz + " -- " + pair.getKey() + " = " + pair.getValue());
					        
					    }
					}
					/****************************************************************************************************************************************************/

					@Override
					protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
						String idVal = "";
						ArrayList<String> strArr = new ArrayList<String>();
						ArrayList<String> modelArr = new ArrayList<String>();
						ArrayList<String> rtnArr3 = new ArrayList<String>();
						ArrayList<String> errorArr = new ArrayList<String>();
						ArrayList<String> errorArr1 = new ArrayList<String>();
						ArrayList<String> errorArr2 = new ArrayList<String>();
						ArrayList<String> errorArr3 = new ArrayList<String>();
						String errorRtn = null;
						JSONArray jsonErrArr = new JSONArray();
						Map<String, Boolean> contractErrs = null; ; 
						String booked = "";
						List<ContractData> contracts;
						String paramName = "id";
						String paramValue = request.getParameter(paramName);
						String jFlag = "jflag";
						String jFlagValue = request.getParameter(jFlag);
						HashMap<String, String> modelMap = new HashMap<String, String>();
						modelArr = Olyutil.readInputFile(modelDataFile);
						modelMap = buildModelMap(modelArr);
						//printMap(modelMap);
						//String paramName2 = "bookDate";
						//String startDate = request.getParameter(paramName2);
						ArrayList<String> rtnArr = new ArrayList<String>();
						//ArrayList<String> errArr = new ArrayList<String>();
						Connection conn = null;
						String query = null;
						//if ((paramValue != null && !paramValue.isEmpty()) && paramValue.equals("id")) {
						if ((paramValue != null && !paramValue.isEmpty())) {	
							 idVal= paramValue.trim();
							// System.out.println("*** idVal:" + idVal + "--");			 
						}
						String dispatchJSP = "/validatecontractresults.jsp";
						strArr = getDbData(idVal, sqlFileContract, "", "contract");
						//System.out.println("*** arrSz:" + strArr.size()+ "--");	
						contracts = loadData(strArr );
						//Olyutil.printStrArray(strArr);
						//System.out.println("*** contractsSz:" + contracts.size());
						//System.out.println("ID=" + idVal   + "-- Booked Date:" + booked + "-- Contract:" + contracts.get(0).getContractID() );
						request.getSession().setAttribute("strArr", strArr);
						request.getSession().setAttribute("contracts", contracts);
						request.getSession().setAttribute("contracts", contracts);
						request.getSession().setAttribute("modelMap", modelMap);
						if (contracts.size() > 0) {
							booked = contracts.get(0).getDateBooked();
							errorArr =  validateAssets(idVal, booked);
							//System.out.println("*** !!! *** BOOKED:" + booked +"--");
						
						
							//request.getSession().setAttribute("assetErrs",jsonErrArr);
							request.getSession().setAttribute("assetErrs",errorArr);
							for (int k = 0; k < contracts.size(); k++) {
								//System.out.println("*** !!! *** contractID:" + contracts.get(k).getContractID());
								 contractErrs = ValidateChecks.validateContract( contracts.get(k)); 
								
							}
							
							
							/*************************************************************************************************************************************************/		
							try { // process bundled checks
								conn = ValidateContractChecks2.getDbConn(propFile);
								// System.out.println("***^^^*** query=" + query);
								//String bookDate = "2019-07-16";
								rtnArr = ValidateChecksBundle.getDataStrArr(conn, sqlFile1, booked);
								 //Olyutil.printStrArray(rtnArr);
								// System.out.println("*** rtnARR SZ:" + rtnArr.size());
								if (rtnArr.size() > 0) {
									errorRtn = ValidateChecksBundle.rateChk(rtnArr);
									if (! isNullStr(errorRtn)) {
										errorArr1.add(errorRtn);
									}
									// Olyutil.printStrArray(rtnArr);		
								}
								rtnArr.clear();
								rtnArr = ValidateChecksBundle.getDataStrArr(conn, sqlFile2, booked);
								if (rtnArr.size() > 0) {
									errorRtn = ValidateChecksBundle.finalRateChk(rtnArr);
									// Olyutil.printStrArray(rtnArr);		 
									if (! isNullStr(errorRtn)) {
										errorArr1.add(errorRtn);
									}
								}
								rtnArr.clear();
								rtnArr = ValidateChecksBundle.getDataStrArr(conn, sqlFile3, booked);
								//System.out.println("*** ValidateContractsAsset: purchOptCheck rtnArrSZ=" + rtnArr.size() + "--");
								
								if (rtnArr.size() > 0) {
									errorArr2 = ValidateChecksBundle.purchOptChk(rtnArr, modelMap, idVal);
									// Olyutil.printStrArray(rtnArr);	 
								}
								rtnArr.clear();
								
								rtnArr = ValidateChecksBundle.getDataStrArr(conn, sqlFile4, booked);
								//System.out.println("*** upFrontTaxChk -> idVal:" + idVal + "--");		
								if (rtnArr.size() > 0) {
									errorArr3 = ValidateChecksBundle.upFrontTaxChk(rtnArr, idVal);	
									 //Olyutil.printStrArray(rtnArr);	 
								}
								rtnArr.clear();
								
								rtnArr = ValidateChecksBundle.getDataStrArr(conn, sqlFile5, booked);
								if (rtnArr.size() > 0) {
									errorRtn = ValidateChecksBundle.miscBillableFlagChk(rtnArr);
									if (! isNullStr(errorRtn)) {
										errorArr1.add(errorRtn);
									}
									 //Olyutil.printStrArray(rtnArr);	 
								}
								rtnArr.clear();
								
								
								  rtnArr = ValidateChecksBundle.getDataStrArr(conn, sqlFile7, "NONE");
								  
								 // System.out.println("*** premiumChk: SZ=" + rtnArr.size() + "--");
								if (rtnArr.size() > 0) {
									errorRtn = ValidateChecksBundle.premiumChk(rtnArr, idVal);
									if (! isNullStr(errorRtn)) {
										errorArr1.add(errorRtn);
									}
									 //Olyutil.printStrArray(rtnArr);	 
								}
								rtnArr.clear();
								/***************************************************************************************************************/

								 // Added new check 2021-05-18 -- Residual check -- JB
								
								//System.out.println("*** Begin running sqlFile8 --  idVal="  + idVal + "--");
								rtnArr = ValidateChecksBundle.getDataStrArr(conn, sqlFile8, idVal);
								
								//System.out.println("*** sqlFile8 rtnARR SZ:" + rtnArr.size());
								
								if (rtnArr.size() > 0) {
									errorRtn = ValidateChecksBundle.descResidualChk(rtnArr);
									if (! Olyutil.isNullStr(errorRtn)) {
										errorArr1.add(errorRtn);
									}
									  //Olyutil.printStrArray(rtnArr);
								}
								rtnArr.clear();
								/***************************************************************************************************************/
								// Added new check 2021-06-10 --  check -- JB
								
								//System.out.println("*** Begin running sqlFile8 --  idVal="  + idVal + "--");
								rtnArr = ValidateChecksBundle.getDataStrArr(conn, sqlFile9, idVal);
								
								//System.out.println("*** sqlFile9 rtnARR SZ:" + rtnArr.size());
								if (rtnArr.size() > 0) {
									errorRtn = ValidateChecksBundle.pptPassThruChk(rtnArr);
									if (! Olyutil.isNullStr(errorRtn)) {
										errorArr1.add(errorRtn);
									}
									  //Olyutil.printStrArray(rtnArr);
								}
								
								rtnArr.clear();
								/***************************************************************************************************************/

								
								
								 
							} catch (IOException | SQLException e) {
								e.printStackTrace();
							}
							//Olyutil.printStrArray(errorArr1);
							
							//request.setAttribute("errorArr1", errorArr1);
							request.getSession().setAttribute("errorArr1", errorArr1);
							request.getSession().setAttribute("errorArr2", errorArr2);
							request.getSession().setAttribute("errorArr3", errorArr3);
							
							
							//request.getSession().setAttribute("rtnArr", rtnArr);
							
							
							//System.out.println("***^^^*** errorArr:" + errorArr.size() + "--");
							//System.out.println("***^^^*** errorArr1:" + errorArr1.size() + "--");
							//System.out.println("***^^^*** errorArr2:" + errorArr2.size() + "--");
							//System.out.println("***^^^*** errorArr3:" + errorArr3.size() + "--");
							
							request.getSession().setAttribute("contractErrs", contractErrs);
							//Olyutil.printHashMap(contractErrs);
							// re-direct to JSP page
							
							if (! isNullStr(jFlagValue)) {
								//System.out.println("*** JFLAG=" + jFlagValue + "--");
							}

							request.getRequestDispatcher(dispatchJSP).forward(request, response);
							/*************************************************************************************************************************************************/		
								
						} else {
							PrintWriter out = response.getWriter();
							response.setContentType("text/html");
							out.write("<h3>Error occurred with Contract: " + paramValue + "</h3>");
							
						}

					
					}
					
					
				
				
				

}
