<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.OutputStream"%>   
    
<%@ page import="java.io.File"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import = "java.io.*,java.util.*, javax.servlet.*" %>
<%@ page import = "javax.servlet.http.*" %>
<%@ page import = "com.olympus.olyutil.*" %>

<%@ page import = "org.json.JSONArray" %>
<%@ page import = "org.json.JSONObject" %>

<%@ page import =  "com.olympus.asset.AssetValData"  %>


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
  	 String title =  "Olympus FIS Validate Infolease Asset Data"; 	
	//String filePath = "C:\\Java_Dev\\props\\headers\\contractvalidate.txt";
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
 
</div>

 
 <%@include  file="includes/header.html" %>
  <h3><%=title%></h3>
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
	JSONArray assetErrs;
	 assetErrs = (JSONArray) session.getAttribute("assetErrs");
	/*
	for (int j = 0; j < contracts.size(); j++) {
		out.println("***^^^^^*** ContractID: " + contracts.get(j).getContractID() +"--");
	}
	*/
 /*************************************************************************************************************************************************/	

	//Map<String, Boolean> errs = new Hashtable<>();
	//errs = (Map<String, Boolean>) session.getAttribute("contractErrs");
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
 	//      ArrayList<String> hdrArr = new ArrayList<String>();
 	///    ArrayList<String> strArr = new ArrayList<String>();
    //strArr1 = readHeader(filePath);
   ///      hdrArr = Olyutil.readInputFile(filePath);
    //printStrArray(hdrArr);
     /*************************************************************************************************************************************************/	
// Load Hash map with Column names and data from strArr

    
    
    
    /*************************************************************************************************************************************************/	
	//String token_list[];
    //out.println("listSize=" + list.size());
   
    
 
     /*************************************************************************************************************************************************/	
 
    //Olyutil.printHashMap(lhm);	
  
    int j = 0;
    String thead = null;
    String thead_r = null;
    String trow = null;
	String color2 = "#5DADE2";
	String color3 = "#EC7063";
	String colorErr = "";
	String line = "";
	for(int k = 0 ; k < assetErrs.length() ; k++) { 
		 
		if  (k == 0 ) {
			out.write("[");
		}	
	 	 line = assetErrs.getJSONObject(k).toString(8);
	 	// System.out.println(line);
	 	
	 	
	 	//out.write(line + "<BR>"); 
	 	 
	 	 
		if  (k == (assetErrs.length() -1) ) {
			//out.write(assetErrs.getJSONObject(k).toString(8));	
			out.write(line + "<BR>"); 
		} else {
			//out.write(assetErrs.getJSONObject(k).toString(8) + ",");	
			out.write(line + "," + "<BR>"); 
		}	
	 	 
	}
	 
	 if (assetErrs.length() > 0) {
			out.write("]");
		}
  
    
    /*************************************************************************************************************************************************/	
 
    %>
 
 
</body>
</html>