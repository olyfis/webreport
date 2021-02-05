<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.OutputStream"%>   
    
<%@ page import="java.io.File"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import = "java.io.*,java.util.*, javax.servlet.*" %>
<%@ page import = "javax.servlet.http.*" %>
<%@ page import = "com.olympus.olyutil.*" %>


<%@ page import =  "com.olympus.infolease.contract.ContractData"  %>


<%@ page import="java.sql.*"%>
<!--    add to web.xml -- change directory as needed
 <context-param> 
   <description>Location to store uploaded file</description> 
   <param-name>file-upload</param-name> 
   <param-value>
      D:\Pentaho\Kettle\RollOver\Upload\
   </param-value> 
</context-param>
-->

<% 
  	 String title =  "Olympus FIS Validate Infolease Contract Data"; 	
	String filePath = "C:\\Java_Dev\\props\\headers\\contractvalidate.txt";
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
 
<title><%=title%></title>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.9.1/jquery.tablesorter.min.js"></script>
<!--  <script type="text/javascript" src="includes/js/tablesort.js"></script> -->
<style><%@include file="includes/css/reports.css"%></style>
<style><%@include file="includes/css/table.css"%></style>
<style><%@include file="includes/css/header.css"%></style>
<script type="text/javascript" src="includes/js/tableFilter.js"></script>
<link rel="stylesheet" href="includes/css/calendar.css" />
<script>


$(function() {

  // call the tablesorter plugin
  $("table").tablesorter({
    theme: 'blue',
    // initialize zebra striping of the table
    widgets: ["zebra"],
    // change the default striping class names
    // updated in v2.1 to use widgetOptions.zebra = ["even", "odd"]
    // widgetZebra: { css: [ "normal-row", "alt-row" ] } still works
    widgetOptions : {
      zebra : [ "normal-row", "alt-row" ]
    }
  });

});	
		
	
    </script>
</head>
<body>

<div style="padding-left:20px">
  <h3><%=title%></h3>
</div>

 
 <%@include  file="includes/header.html" %>
 <%!
 
 /*************************************************************************************************************************************************/	
 public ArrayList<String> readHeader(String filePath) {
	 	String line = "";
	 	ArrayList<String> data = new ArrayList<String>();
	 	//System.out.println("****** in readHeader");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			StringBuilder sb = new StringBuilder();
			//System.out.println("****** read a line");
			line = reader.readLine();
			//out.println(sb.toString());
			//System.out.println("****** line" + line);
			data.add(line);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return data;
	}
	
/*************************************************************************************************************************************************/	

	//method to print array
	public static void printStrArray(ArrayList<String> strArr ) {
		for (String str : strArr) { // iterating ArrayList
			System.out.println("DATA:" + str + "---");
		}
		// System.out.println(names[index]);
	}
	
/*************************************************************************************************************************************************/	
public static Map<String, String> loadHashMap(ArrayList<String> strArr, ArrayList<String> hdrArr ) {
		Map<String, String> mp = new Hashtable<>();
	
		 
		for (int k = 0; k < hdrArr.size(); k++) {	
			//System.out.println(hdrArr.get(k) + "->" + strArr.get(k) + "--");
			mp.put(hdrArr.get(k), strArr.get(k));
		}

		return mp;
}
/*************************************************************************************************************************************************/	
    	public static LinkedHashMap loadLinkedHashMap(ArrayList<String> strArr, ArrayList<String> hdrArr ) {
    		LinkedHashMap mp = new LinkedHashMap();
	
		 
		for (int k = 0; k < hdrArr.size(); k++) {	
			//System.out.println(hdrArr.get(k) + "->" + strArr.get(k) + "--");
			mp.put(hdrArr.get(k), strArr.get(k));
		}

		return mp;
}
    	
/*************************************************************************************************************************************************/	

 
 public String TableHeader1(){
		String thead = null;
		String color1 = "#5DADE2";
		String style1 = "font-family: sans-serif; color: white;";	  
		//thead = "<tr bgcolor=" + color1 +  "style=" + style1 + ">";
		thead = "<tr class=\"b3\" "  +  "style=" + style1 + ">";
		//thead += "<th class=\"b\">App Number</th>";
		
		thead += "<th>Contract Number</th>";
		thead += "<th>Customer Name</th>";
		thead += "<th>Lease Type</th>";
		thead += "<th>Date Booked</th>";
		thead += "<th>Date Commenced</th>";
		thead += "<th>Term</th>";
		thead += "<th>Term Date</th>";
		
		thead += "</tr>";
		return thead;
 }	
 
 public String TableHeader2(){
		String thead = null;
		String color1 = "#5DADE2";
		String style1 = "font-family: sans-serif; color: white;";	  
		 
		thead = "<tr class=\"b3\" "  +  "style=" + style1 + ">";
		thead += "<th>Lead Days</th>";
		thead += "<th>First Pymt Date</th>";
		thead += "<th>Last Pymt Date</th>";
		thead += "<th>Equip. Cost</th>";
		thead += "<th>Screen 14 Equip. Cost</th>";
		thead += "<th>Out Of Balance</th>";
		thead += "<th>Screen 3 Yield</th>";
	
		thead += "</tr>";
		return thead;
 }
 
  
 public String TableHeader3(){
		String thead = null;
		String color1 = "#5DADE2";
		String style1 = "font-family: sans-serif; color: white;";	  
		 
		thead = "<tr class=\"b3\" "  +  "style=" + style1 + ">";
		thead += "<th>Screen 10 Yield</th>";
		thead += "<th>Screen 14 Yield</th>";
		thead += "<th>Screen 3 Residual</th>";
		thead += "<th>Total OL Residual</th>";
		thead += "<th>Screen 14 Residual</th>";
		thead += "<th>PO Number</th>";
		thead += "<th>PO Expire Date</th>";
		
		thead += "</tr>";
		return thead;
}
 public String TableHeader4(){
		String thead = null;
		String color1 = "#5DADE2";
		String style1 = "font-family: sans-serif; color: white;";	  
		 
		thead = "<tr class=\"b3\" "  +  "style=" + style1 + ">";
		thead += "<th>Pymt Option</th>";
		thead += "<th>Invoice Code</th>";
		thead += "<th>Invoice Format</th>";
		thead += "<th>Contract Purch. Option</th>";
		thead += "<th>Total CPP Procedures</th>";
		thead += "<th>LC Grace Period</th>";
		thead += "<th>Program Type</th>";
		
		thead += "</tr>";
		return thead;
}
 
 public String TableHeader5(){
		String thead = null;
		String color1 = "#5DADE2";
		String style1 = "font-family: sans-serif; color: white;";	  
		 
		thead = "<tr class=\"b3\" "  +  "style=" + style1 + ">";
		thead += "<th>Product Group</th>";
		thead += "<th>CPP First Read Date</th>";
		thead += "<th>ACH</th>";
		thead += "<th>Insurance Status</th>";
		thead += "<th>Late Chrg Exempt (Y/N)</th>";
		thead += "<th>Late Charge %</th>";
		thead += "<th> </th>";

		thead += "</tr>";
		return thead;
}
 
 %>
 
 
 
 <%
 
 

 
    	/*************************************************************************************************************************************************/	
    String xDataItem = null;
     String color1 = "plum";
     String style1 = "font-family: sans-serif; color: white;";
     String rowEven = "#D7DBDD";
     String rowOdd = "#AEB6BF";
     String errColor = "#EC7063";
     
     //retrieve your list from the request, with casting 

    String rowColor = null;
    ArrayList<String> list = new ArrayList<String>();
    list = (ArrayList<String>) session.getAttribute("strArr");

    /*************************************************************************************************************************************************/	
    
    /*************************************************************************************************************************************************/	
	List<ContractData> contracts;
	contracts = (List<ContractData>) session.getAttribute("contracts");
	/*
	for (int j = 0; j < contracts.size(); j++) {
		out.println("***^^^^^*** ContractID: " + contracts.get(j).getContractID() +"--");
	}
	*/
 /*************************************************************************************************************************************************/	

	Map<String, Boolean> errs = new Hashtable<>();
	errs = (Map<String, Boolean>) session.getAttribute("contractErrs");
	//out.println("<BR> errsSize=" + errs.size() + "<BR>");
	/*
	for (String key : errs.keySet()) {
		System.out.println("Key: " + key);
		//System.out.println("Value: " + errs.get(key).getBoolean(key));
		System.out.println("**************** Value: " + errs.get(key.toString()));
	}
	*/
   /*************************************************************************************************************************************************/	
 /*************************************************************************************************************************************************/	
 	ArrayList<String> hdrArr = new ArrayList<String>();
 	ArrayList<String> strArr = new ArrayList<String>();
    //strArr1 = readHeader(filePath);
    hdrArr = Olyutil.readInputFile(filePath);
    //printStrArray(hdrArr);
     /*************************************************************************************************************************************************/	
// Load Hash map with Column names and data from strArr

    
    
    
    /*************************************************************************************************************************************************/	
	//String token_list[];
    //out.println("listSize=" + list.size());
     
     if ( list.size() > 0 ) {   	 
		//out.println("<table class=\"tablesorter\" border=1>");	
    	for (int i = 0; i < list.size(); i++) {
    		xDataItem = list.get(i);
    		String token_list[] = xDataItem.split(":");
    		
    		
    		//out.println(TableHeader1());  // Row 1
    		for (int x = 0; x < token_list.length; x++) {
    			strArr.add(token_list[x]);
    			 
    		} // end inner for
    		 
    	} //end outer for
    		//out.println("</table>"); 
     } //end if
     /*************************************************************************************************************************************************/	
 	
 	LinkedHashMap lhm = new LinkedHashMap();
    			
    Map<String, String> mp2 = new Hashtable<>();
    mp2 = loadHashMap(strArr, hdrArr );
    //Olyutil.printHashMap(mp2);	
    lhm = loadLinkedHashMap(strArr, hdrArr );
    //Olyutil.printHashMap(lhm);	
    
    
    //Iterator it = mp2.entrySet().iterator();
    Iterator it = lhm.entrySet().iterator();
    
    int j = 0;
    String thead = null;
    String thead_r = null;
    String trow = null;
	String color2 = "#5DADE2";
	String colorErr = "";
	String style2 = "font-family: sans-serif; color: white; font-size: 10pt";
	
    out.println("<table class=\"tablesorter\" border=1>");	
    thead = "";
   thead_r = "<tr bgcolor=" + color1 +  "style=" + style2 + "  >";
  
   // thead_r ="<tr bgcolor=" + color1 + ">";
    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        
	        String key =  (String) pair.getKey();
	        
	        //System.out.println("%%%%%%%" +  j + " " + pair.getKey() + " = " + pair.getValue());
	        //int mod = (j % 7);
	        
	        //thead += "<th style=" +  style2 +    "    > " +   pair.getKey()  + "</th>";
	        thead += "<th style=" +  style2 +    "    > " +   key  + "</th>";
	        
	      // System.out.println("******  &&&&  ********** Key:" +  key + "---- errValue:" + errs.get(key.toString()));
	        
	   
	        	//if (( !  errs.get(key.toString().equals(null))  &&   errs.get(key).booleanValue() == true  )) {
	        		
	        		
	        if ((   errs.get(key) != null )  &&   errs.get(key).booleanValue() == true  )    {	
		       // System.out.println("***%%%*** Key:" + key + " ERRValue: " + errs.get(key.toString()));
		        colorErr = "#F55A48";
	      	}
 
	        
	        trow += "<td bgcolor=\" "  + colorErr  + "\" >" + pair.getValue()  + " </td>  ";
	        colorErr = "";
	        
	        int mod = (j % 7);
	        //System.out.println("%%%%%%% J=" +  j + " Mod " + mod);
			if ((mod) == 6 || j == 33) {
				%>
				 
				<tr bgcolor=" <%=color2%>"    style=" <%=style2%>  ">
			 
				<% 
				
				
				
				out.println(thead); 
				
			
				out.println("</tr>");
				
				out.println("<tr >"  );
				out.println(trow); 
				out.println("</tr>");
				thead = "";
				trow = "";
			} else {
				
			}
	        
	        
			j++;
	}	 
    out.println("</table>"); 
    for (String key : errs.keySet()) {
		//System.out.println("Key:" + key + "--");
		//System.out.println("Value: " + errs.get(key).getBoolean(key));
		//System.out.println("**************** Value:" + errs.get(key.toString())   + "--");
	}
    
    /*************************************************************************************************************************************************/	
  
    
    
    
    			
     /*************************************************************************************************************************************************/	
		//loadHashMap(strArr, hdrArr );
    %>
 
</body>
</html>