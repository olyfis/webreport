 
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import = " org.json.JSONArray" %>
<%@ page import = " org.json.JSONObject" %>




<% 
  	String title =  "Olympus FIS Validate Infolease Contract Data Page"; 

%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title><%=title%></title>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.9.1/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="includes/js/tableFilter.js"></script>
<style><%@include file="includes/css/reports.css"%></style>
<style><%@include file="includes/css/table.css"%></style>
<style><%@include file="includes/css/header.css"%></style>
<link rel="stylesheet" href="includes/css/calendar.css" />

<!-- ********************************************************************************************************************************************************* -->
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

<%!String formUrl = null;
/*************************************************************************************************************************************************************/
public ArrayList<String> readHeader(String filePath) throws IOException {
	
	ArrayList<String> strArr = new ArrayList<String>();
	String header = null;
	BufferedReader reader = null;
	StringBuilder sb = null;
	String line = null;
	try {
	 	reader = new BufferedReader(new FileReader(filePath));
    	 sb = new StringBuilder();
    
	} catch (FileNotFoundException fex) {
		fex.printStackTrace();	
	}
	try { 
	    while((line = reader.readLine())!= null){
	    	strArr.add(line);
	    }	   
		reader.close();
	
	} catch (IOException ioe) {
		ioe.printStackTrace();
	}
	
	return strArr;	
}
/*************************************************************************************************************************************************************/
public String  buildHeader( JspWriter out2, ArrayList<String> dataArr   ) throws IOException {
	
	String header = "";
	String style = "b3";
	if (dataArr.size() > 0) {
		for (int k = 0; k < dataArr.size(); k++) {
			
			if (k == 0 || k == 1 || k == 2) {
				style = "b3";
			} else {
				style = "b4a";
			}
			//header += "<th class=\"b3\" >" + dataArr.get(k) + " </th>";
			header += "<th class=\" " + style + "  \" >" + dataArr.get(k) + " </th>";
			
		}
	}
	return header;
	
}
/*************************************************************************************************************************************************************/
public String  buildCells( JspWriter out, ArrayList<String> dataArr  ) throws IOException {
	String cells = "";
	String xDataItem = null;
	String color1 = "plum";
	String style1 = "font-family: sans-serif; color: white;";
	String rowEven = "#D7DBDD";
	String rowOdd = "AEB6BF";
	String excel = null;
	String rowColor = null;
	
	//t.println("<tr>");
	//cells = "<tr>";	
	String rowItem = "";
	


		 
		if (dataArr.size() > 0) {
			for (int k = 0; k < dataArr.size(); k++) {
				rowColor = (k % 2 == 0) ? rowEven : rowOdd;
				cells +="<tr bgcolor=" + rowColor + ">";
				xDataItem = dataArr.get(k);
				String token_list[] = xDataItem.split(";");
				for (int x = 0; x < token_list.length; x++) {
					//System.out.println("**** T=" + token_list[x] + "--");
					if (token_list[x].contains("http")) {
						//System.out.println("****!!!!!!!!!! T=" + token_list[x] + "--");
						cells += "<td class=\"odd\"> <a href=\"" + token_list[x] + "\"   target=\"_blank\"      >" + token_list[x] +  "</a></td>";
					} else {
						cells += "<td class=\"odd\">" + token_list[x] + "</td>";
					}
				}
				cells += "</tr >";
				
				//cells += "<td class=\"a\" >" + dataArr.get(k) + " </td>";
			}
		}
		 

		return cells;
	}

	/*************************************************************************************************************************************************************/

	/*************************************************************************************************************************************************************/%>

<div style="padding-left:20px">
<%@include  file="includes/header.html" %>

<h3><%=title%></h3>

<%
	String filePath = "C:\\Java_Dev\\props\\headers\\valerrHeader.txt";
	ArrayList<String> headerArr = readHeader(filePath);
  
    JSONArray jsonArr = new JSONArray();
    jsonArr = (JSONArray) session.getAttribute("jsonArr");
	
    ArrayList<String> errArr = new ArrayList<String>();
	
	errArr = (ArrayList<String>) session.getAttribute("errArr");
	
	
	
	
	
	
	
	
	
	//list = (ArrayList<String>) session.getAttribute("strArr");
	//list2.add("xx");
	
	//out.println("JARRSize=" + jsonArr.length());
	if (errArr.size() > 0) {
		/**********************************************************************************************************************************************************/
		// Output Table 
	%>	
		<!--  <input id="search" type="text" placeholder="Enter Text to Filter...">  -->
<h5></h5>

<%
		out.println("<table class=\"tablesorter\" border=\"1\"> <thead> <tr>");
		String header = buildHeader(out, headerArr); // build header from file
		out.println(header);
		out.println("</tr></thead>");
		out.println("<tbody id=\"report\">");
		String cells =  buildCells(out, errArr); // build data cells from file
		out.println(cells);
		out.println("</tbody></table>"); // Close Table
		/**********************************************************************************************************************************************************/
	} else {
		out.println("No Contract errors  to display." + "<br>");

	}
	 
%>

</body>
</html>