<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.OutputStream"%>   
    
<%@ page import="java.io.File"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import = "java.io.*,java.util.*, javax.servlet.*" %>
<%@ page import = "javax.servlet.http.*" %>
<%@ page import = "com.olympus.olyutil.*" %>


<%@ page import =  "com.olympus.infolease.contract.ContractData"  %>
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
 
</div>

 
 <%@include  file="includes/header.html" %>
  <h3><%=title%></h3>
 <%!

 
 /*************************************************************************************************************************************************/	


/*************************************************************************************************************************************************/	


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
    ArrayList<String> list3 = new ArrayList<String>();
    ArrayList<String> err2= new ArrayList<String>();
    ArrayList<String> err3= new ArrayList<String>();
    ArrayList<String> errArr = new ArrayList<String>();
    ArrayList<String> errArr2 = new ArrayList<String>();
    
    list = (ArrayList<String>) session.getAttribute("strArr");
    err2 = (ArrayList<String>) session.getAttribute("errorArr2");
    err3 = (ArrayList<String>) session.getAttribute("errorArr3");
    
    
    /*************************************************************************************************************************************************/	
    //out.println("SZ=" + list3.size() + "--");
	 
	 list3 = (ArrayList<String>) session.getAttribute("errorArr1");
	 
/*	 
	 for (String s : list3) {
		 
		 out.println("<h5>******^^^^^^^********* S: " + s + "	 </h5>"); 
	 }
*/
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
    out.println("<h5><B>NOTE: All cells in Yellow must be checked manually. Red cells denote an error.</b></h5>");
    
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
	String color3 = "#EC7063";
	String color4 = "#d9d9d9";
	String colorErr = "";
	String style2 = "font-family: sans-serif; color: white; font-size: 10pt";
	String style4 = "font-family: sans-serif; color: black; font-size: 10pt";
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
	      	} else {
	      		colorErr = "#e3e9f2";
	      		if ((j <= 33 && j >= 30) || j== 8){
	        		colorErr = "#e5dd80";
	      		}
	      		
	      		
	      	}
 
	        
	        trow += "<td bgcolor=\"  " + colorErr  + "\" >" + pair.getValue()  + " </td>  ";
	        colorErr = "";
	        //out.println("******");
	        int mod = (j % 7);
	        //System.out.println("%%%%%%% J=" +  j + " Mod " + mod);
			if ((mod) == 6 || j == 39) {
				%>
				 
				<tr bgcolor=" <%=color2%>"    style=" <%=style2%>  ">
			 
				<% 
				; 
				out.println(thead); 
				//System.out.println("**************** FN:" + thead  + "--");
			   
				if ( j== 39  ) {
					 
					 
			 
					out.println("<td>&nbsp</td>");
					out.println("<td>&nbsp</td>");
				}
				out.println("</tr>");
				%>
				<!--   out.println("<tr >"  ); -->
				<tr bgcolor="#e3e9f2">
				<% 
				out.println(trow); 
				 
				if ( j== 39  ) {
					 
				 
					out.println("<td>&nbsp</td>");
					out.println("<td>&nbsp</td>");
					 
				}
				 
				out.println("</tr>");
				thead = "";
				trow = "";
			} else {
				
			}
			//System.out.println("%%%%%%% J=" +  j );
			
			j++;
	}
	      	
    out.println("</table><BR><BR>"); 
    //for (String key : errs.keySet()) {
		//System.out.println("Key:" + key + "--");
		//System.out.println("Value: " + errs.get(key).getBoolean(key));
		//System.out.println("**************** Value:" + errs.get(key.toString())   + "--");
	//}
    
    /*************************************************************************************************************************************************/	
   
    
    // Display Asset Errors
  	list = (ArrayList<String>) session.getAttribute("assetErrs");
  	//System.out.println("**************** list SZ:" + list.size() );
  	//out.println("**** SZ=" + list.size() + "--");
  	 if (list.size() > 0) {
	
	 	out.println("<table  border=1 width=\"600\"  bgcolor=" + color4 +  "style=" + style4 + "  >");
	 	out.println("<th width=\"80%\">                 Asset Errors            </th> </tr>");

		for (int k = 0; k < list.size(); k++) {
			xDataItem = list.get(k);
			String token_list[] = xDataItem.split(";");
			for (int x = 0; x < token_list.length; x++) {
				%>
				<tr bgcolor=" <%=color4%>"    style=" <%=style4%>  ">
				<td width="80%">
		 
				<!--  out.println("  <tr bgcolor=" + color1 +  "style=" + style1 + "  ><td width=\"80%\">" +  token_list[x] + "</td></tr>");	 -->
			
				<%=token_list[x]%>
				
				</td>
				<%
					//System.out.println("**************** Err=" + token_list[x]);
						}
						}

					 	//out.println("</tr></table>"); 
					 	out.println("</table>"); 
					 
				  	 }
				  	
					 
					 
				  	/* 
				  	errArr = (ArrayList<String>) session.getAttribute("errorArr1");
				  	out.println("SZ=" + errArr.size() + "--");
				  	//out.println("<h6>Errors: " + errArr.get(0) + "	 </h6>");
				 	
				  	
				  	
				  	 if (errArr.size() > 0) {
				  		for (int k = 0; k < errArr.size(); k++) {
				  	  		//out.println("<h6>Errors: " + errArr.get(k) + "	 </h6>");	
				  	  	}	 
				  	 }
				 */
				  	 
				  	 
					
				  			/*************************************************************************************************************************************************/
					for (String s : list3) {
						out.println("<h5>" + s + "	 </h5>");
					}

					for (String e2 : err2) {
						out.println("<h5>" + e2 + "	 </h5>");
					}
					for (String e3 : err3) {
						out.println("<h5>" + e3 + "	 </h5>");
					}
					
					/*************************************************************************************************************************************************/

					String line = "";
					errs = (Map<String, Boolean>) session.getAttribute("contractErrs");

					/*************************************************************************************************************************************************/
					//loadHashMap(strArr, hdrArr );
					String formUrl = "http://cvyhj3a27:8181/webreport/validatecontract.jsp";
				%>
 
  <form name=" formReturn"   method="get" action="http://cvyhj3a27:8181/webreport/validatecontract.jsp" \>
 	<!--  <input class="btn"  type="button"   value="New Contract"/>  -->
 	<button class="btn" type="submit"   value="Submit">New Contract</button>
 </form>
</body>
</html>