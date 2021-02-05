package com.olympus.webreport;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.*;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import com.olympus.olyutil.Olyutil;
import com.olympus.util.JButils;
import org.w3c.dom.NodeList;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
 
 

// Run: http://localhost:8181/webreport/getchart?cType=SBS
@WebServlet("/GetChartData")
public class GetChartData extends HttpServlet {

/************************************************************************************************************************************************/
	public static int strToInt( String str ){
	    int i = 0;
	    int num = 0;
	    boolean isNeg = false;

	    //Check for negative sign; if it's there, set the isNeg flag
	    if (str.charAt(0) == '-') {
	        isNeg = true;
	        i = 1;
	    }

	    //Process each character of the string;
	    while( i < str.length()) {
	        num *= 10;
	        num += str.charAt(i++) - '0'; //Minus the ASCII code of '0' to get the value of the charAt(i++).
	    }

	    if (isNeg)
	        num = -num;
	    return num;
	}
/************************************************************************************************************************************************/
	
//method to print array
public static void printName(ArrayList<String> line_arr){

		// iterating ArrayList
		for (String str : line_arr)
			System.out.println("DATA:" + str + "---");

		// System.out.println(names[index]);
}
/************************************************************************************************************************************************/

public static void printHashMap(Map mp) {
    Iterator it = mp.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry pair = (Map.Entry)it.next();
        System.out.println(pair.getKey() + " = " + pair.getValue());
        //it.remove(); // avoids a ConcurrentModificationException
    }
    
    System.out.println("********^^^^************\n");
}

/************************************************************************************************************************************************/


public static JSONArray hashMaptoJSON(Map mp) {
	
	JSONArray jArr = new JSONArray();
	JSONObject jObj = new JSONObject();
    Iterator it = mp.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry pair = (Map.Entry)it.next();
        jObj.put(pair.getKey().toString(), pair.getValue());
		 
        //System.out.println(pair.getKey() + " = " + pair.getValue());
        //it.remove(); // avoids a ConcurrentModificationException
    }
    jArr.put(jObj);
    return(jArr);
}

/************************************************************************************************************************************************/
public static JSONArray hashMaptoJSONAppOwner(Map mp) {
	
	JSONArray appOwner = new JSONArray();
	Iterator it = mp.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry pair = (Map.Entry)it.next();
        appOwner.put(makeJobjApp(pair.getKey().toString(), pair.getValue().toString()));
        //System.out.println(pair.getKey() + " = " + pair.getValue());
        //it.remove(); // avoids a ConcurrentModificationException
    }
	 return(appOwner);
}
/************************************************************************************************************************************************/

public static JSONArray hashMaptoJSONpercent(Map mp) {
	
	JSONArray json_arr = new JSONArray();
	Map<String, String> treeMap = new TreeMap<String, String>(mp);
	
	//Iterator it = mp.entrySet().iterator();
	
	Iterator it = treeMap.entrySet().iterator();
	
    while (it.hasNext()) {
        Map.Entry pair = (Map.Entry)it.next();  
        json_arr .put(makeJobj("label", pair.getKey().toString(), "count",  pair.getValue().toString()));
        //System.out.println("***********************" + pair.getKey() + " = " + pair.getValue());
        //it.remove(); // avoids a ConcurrentModificationException
    }
    //System.out.println("^^^^^^^^^^^^^^\n");
	 return(json_arr );
}
/************************************************************************************************************************************************/
public static JSONObject makeJobjApp(String appOwner, String count){
	   JSONObject jobj = new JSONObject();
	   jobj.put("appOwner", appOwner);
	   jobj.put("count", count);
	   return jobj ;
	} 
/************************************************************************************************************************************************/

public static JSONObject makeJobj(String key1, String val1, String key2, String val2){
	   JSONObject jobj = new JSONObject();
	   jobj.put(key1, val1);
	   jobj.put(key2, val2);
	   return jobj ;
	} 

/************************************************************************************************************************************************/
public static JSONArray getXMLdata(String cType) {
		
		String status = null;
		String cts_flag = null;
		String status_t = null;
		String percentShipped = null;
		String appOwner = null;
		String lps25 = "0% to 25%";
		String lps50 = "26% to 50%";
		String lps75 = "51% to 75%";
		String lps100 = "76% to 100%";
		String None = "None";
		
		//String equipCost = null;
		String equipCost_t = null;
		String percentShipped_t = null;
		Map<String, Integer> freq = new Hashtable<>(); 
		Map<String, Integer> percentfreq = new Hashtable<>(); 
	 
		//Map<String, Integer> percentcount = new Hashtable<>(); 
		
		int limit = 95;
		int rel_count = 0;
		int pend_count = 0;
		String outData = null;
		String outDataLine = null;
		Double equipCost = 0.00;
		int count = 0;
		int app_cnt = 0;
		
		int rec_cnt = 0;
		int none_cnt = 0;
		int ordsub_cnt = 0;
		int docrec_cnt = 0;
		int docinfo_cnt = 0;
		int credapp_cnt = 0;
		int app_percent_cnt = 0;
		int lps_25_cnt = 0;
		int lps_50_cnt = 0;
		int lps_75_cnt = 0;
		int lps_100_cnt = 0;
		
		Double ordSubTotal = 0.00;
		Double docRecTotal = 0.00;
		Double docMITotal = 0.00;
		Double docCATotal = 0.00;
		
		JSONArray json = new JSONArray();
		JSONObject obj = new JSONObject();
		ArrayList<String> lines = new ArrayList<String>();
		ArrayList<String> xData = new ArrayList<String>();
		
		//System.out.println("^^^^^^^ In getXMLdata() -> cTypeValue=" + cType);
		try {
		    File fXmlFile = new File("d:\\Pentaho\\Kettle\\Dashboard\\XML\\dashboard_NF\\sharepoint_NF_V1.xml");
		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    Document doc = dBuilder.parse(fXmlFile);
		    doc.getDocumentElement().normalize();

		    //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		    NodeList nList = doc.getElementsByTagName("SharePt_r");
		    //System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				 System.out.println("\nCurrent Element :" + nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					 
						
				 
					
					//Olyutil.isNullStr(str);
					 
					status_t = eElement.getElementsByTagName("CO_UD_CO_APP_STATUS_TBDESC").item(0).getTextContent();
					status = status_t.trim();
					System.out.println("\n *********** Status:" + status + "--");
					 
					percentShipped = eElement.getElementsByTagName("Percent_Ship").item(0).getTextContent();
					appOwner = eElement.getElementsByTagName("AppOwner").item(0).getTextContent();
					equipCost_t = eElement.getElementsByTagName("CO_equipmentCost").item(0).getTextContent();
					cts_flag = eElement.getElementsByTagName("CTS_DECISION_CODE_FKEY_1").item(0).getTextContent();
					rec_cnt++;
					percentShipped_t = percentShipped.replace("%", "");
					int percent = 0;
					if (!percentShipped_t.isEmpty()) {
						percent = Integer.parseInt(percentShipped_t);
						//System.out.println("\n --- Percent=" + percent + "--");
					}
					
					//System.out.println("******* EquipCost=" + equipCost_t);
					
				
					if (!equipCost_t.isEmpty()) {
						equipCost_t = equipCost_t.replaceAll("[^0-9]", "");
						//System.out.println("******* After EquipCost=" + equipCost_t);
						equipCost = Double.valueOf(equipCost_t);
					}					
					//System.out.println("\n --- cType=" + cType + "--");
					if (cType.matches("SBS")) { // Shipped by Status
						//System.out.println("\nStatus:" + status + "-- Percent Shipped:" + percent + "--");
						if ((status.matches("Order Released")) && (percent >= limit)) {
							rel_count++;
							 //System.out.println("\n******* System.out.println("\nStatus:" + status + " -- Percent Shipped:" + percent + "--");Status:" + status + " -- Percent Shipped:" + percent + "--");
						}
						if ((status.matches("Pending Booking") || cts_flag.matches("Y")) ) {
							pend_count++;
							//System.out.println("\nCts_flag=" + cts_flag + " Status:" + status + "-- Percent Shipped:" + percent + "--");
						}
					} else if (cType.equals("LHP")) { // Leases in House Processing
						
						//if ((status.matches("Pending Booking")) && (percent >= limit)) { status:" + status + "Cost_t:" + equipCost_t + "--"  + "Cost:" + equipCost + "--"    );
						if (status.matches("Order Submitted")) {
							ordSubTotal =  ordSubTotal + equipCost;
							ordsub_cnt++;
							//System.out.println("\nstatus:" + status + "-- Cost:" + equipCost  + "--"    + "-- TCost:" + docCATotal  + "--" );
						} else  if (status.matches("Docs Received - Review")) {
							docRecTotal =  docRecTotal + equipCost;
							docrec_cnt++;
						} else  if (status.matches("Docs Back - Missing Information")) {
							docMITotal =  docMITotal + equipCost;
							docinfo_cnt++;
						} else  if (status.matches("Docs Back - Need Credit Approval")) {
							credapp_cnt++;
							
							
							docCATotal =  docCATotal + equipCost;
							//System.out.println("\nstatus:" + status + "-- Cost:" + equipCost  + "--"    + "-- TCost:" + docCATotal  + "--" );
							
						}		 
					} else if (cType.equals("SBO")) { // Shipped by Owner
						if ((percent >= limit)) {
							//System.out.println("\nApp_owner:" + appOwner + "-- Percent Shipped:" + percent  + "--");
							app_cnt = freq.containsKey(appOwner) ? freq.get(appOwner) : 0;
							freq.put(appOwner, app_cnt + 1);
							
						}					 
					} else if (cType.equals("LPS")) { // Leases Processed Shipped by Status
						if (percentShipped.isEmpty()) {
							percentShipped = "None";
							percent = 0;
							none_cnt++;
							/*
							none_cnt = percentfreq.containsKey(None) ? percentfreq.get(None) : 0;
							percentfreq.put(None, none_cnt + 1);
							*/
						}
						
					
					 
						if (percent <= 25) {
							lps_25_cnt = percentfreq.containsKey(lps25) ? percentfreq.get(lps25) : 0;
							percentfreq.put(lps25, lps_25_cnt + 1);
						} else if (percent > 25 &&   percent <= 50) {
							lps_50_cnt = percentfreq.containsKey(lps50) ? percentfreq.get(lps50) : 0;
							percentfreq.put(lps50, lps_50_cnt + 1);	
						} else if (percent > 50 &&   percent <= 75) {
							lps_75_cnt = percentfreq.containsKey(lps75) ? percentfreq.get(lps75) : 0;
							percentfreq.put(lps75, lps_75_cnt + 1);
						} else if (percent > 75 &&   percent <= 100) {
							lps_100_cnt = percentfreq.containsKey(lps100) ? percentfreq.get(lps100) : 0;
							percentfreq.put(lps100, lps_100_cnt + 1);
						}
						 
						
						
					}
				}
			}
			//System.out.println("\nPending:" + pend_count + " -- Released:" + rel_count + "--");
			//System.out.println("\nOS:" + ordSubTotal + " -- Rec:" + docRecTotal + "--"   + " -- MI:" + docMITotal + "--"  + " -- CA:" + docCATotal + "--" );
			try {
				if (cType.matches("SBS")) { // Shipped by Status
					obj.put("pending", pend_count);
					obj.put("release", rel_count);
					json.put(obj);
				} else if (cType.equals("LHP")) { // Leases in House Processing
					obj.put("ordsubmit", ordSubTotal);
					obj.put("Order_Submitted", ordsub_cnt);
					
					obj.put("Docs_Received", docrec_cnt);
					obj.put("docreceived", docRecTotal);
					
					obj.put("missinginfo", docMITotal);
					obj.put("Doc_Missing_Info", docinfo_cnt);
					obj.put("creditapproval", docCATotal);
					obj.put("Need_Credit_Approval", credapp_cnt);
					
					json.put(obj);
					 //System.out.println("\nDocRec:" + docrec_cnt + " -- DocInfo:" + docinfo_cnt + "--"   + " -- OrdSub:" + ordsub_cnt + "--"  + " -- CA:" + credapp_cnt + "--" );

				} else if (cType.equals("SBO")) { // // Shipped by Owner
					//printHashMap(freq);
					//json = hashMaptoJSON(freq);
					
					json =  hashMaptoJSONAppOwner(freq);

				} else if (cType.equals("LPS")) { //
					//printHashMap(percentfreq);
					json = hashMaptoJSONpercent(percentfreq);
				 
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("\nREC_CNT:" + rec_cnt  + "-- None:" + none_cnt);
		return json;
		
}		
/************************************************************************************************************************************************/
public static void displayJsonArray(JSONArray jsonArr) {
	System.out.println("in displayJsonArray\n");
	Iterator<Object> iterator = jsonArr.iterator();
	// Set<String> keys = jsonObject.keySet();
	while (iterator.hasNext()) {

		Object obj = iterator.next();
		if (obj instanceof JSONObject) {
			Set<String> keys = ((JSONObject) obj).keySet();
			System.out.println("%%%%%%%%% KEYS %%%%%%%%" + keys.toString() + "keyNum=" +  keys.size());
			for (String key : keys) {
				// System.out.println(key + ":" + jsonObject.get(key));
				System.out.println("*******Key: " + key + " -> " + ((JSONObject) obj).get(key));
			}
			// System.out.println("*******" + ((JSONObject) obj).get("CUST_ID"));
		}
	}
}
/************************************************************************************************************************************************/
/************************************************************************************************************************************************/
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse res) 
		throws ServletException, IOException {
	
	PrintWriter out = res.getWriter();
	JSONArray jsonArr = new JSONArray();
	
	String cType = "cType";
	String cTypeValue = req.getParameter(cType);
	//System.out.println("^^^^^^^ cTypeValue=" + cTypeValue);
	 
	jsonArr = getXMLdata(cTypeValue);
	// printName(xData);
	 // displayJsonArray(jsonArr);
	// System.out.println(jsonArr.toString());
	out.write(jsonArr.toString());
	 
	
	
}
 
/************************************************************************************************************************************************/

}
