<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
 

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Olympus FIS Booking Detail Report</title>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.9.1/jquery.tablesorter.min.js"></script>
<!--  <script type="text/javascript" src="includes/js/tablesort.js"></script> -->
<style><%@include file="includes/css/reports.css"%></style>
<style><%@include file="includes/css/table.css"%></style>
<style><%@include file="includes/css/header.css"%></style>
<link rel="stylesheet" href="includes/css/calendar.css" />
<script type="text/javascript" src="includes/js/tableFilter.js"></script>

<!-- ********************************************************************************************************************************************************* -->
<script>
  function openWin(myID) {
  
  
   myID2 = document.getElementById(b_app).value;

  alert("ID" + myID2);
  //window.open("http://cvyhj3a27:8181/fisAssetServlet/readxml?appID=" + myID2);
	}
	
	
	var call = function(id){
		var myID = document.getElementById(id).value;
		//alert("****** myID=" + myID + " ID=" + id);
		//window.open("http://cvyhj3a27:8181/fisAssetServlet/readxml?appID=
		window.open("http://localhost:8181/webreport/getquote?appKey=" + myID);
				
				
	}

	var getComments = function(id){
		var myID = document.getElementById(id).value;
		//alert("****** myID=" + myID + " ID=" + id);
		//alert(myID);
		//window.open("http://cvyhj3a27:8181/fisAssetServlet/readxml?appID=" + myID);
	}
	var getQuote = function(id){
		var myID = document.getElementById(id).value;
		//alert("****** myID=" + myID + " ID=" + id);
		//alert("in Quote" + myID + " --- id=" +id);
		window.open("http://cvyhj3a27:8181/webreport/getquote?appKey=" + myID, 'popUpWindow','height=500,width=800,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no, status=yes' );
		//window.open("http://cvyhj3a27:8181/fisAssetServlet/readxml?appID=" + myID);
	}

	var getExcel = function(urlValue){
		var formUrl = document.getElementById(urlValue).value;
		//alert("SD=" + startDate + "****** formUrl=" + formUrl + " \n***** urlValue=" + urlValue);
		//alert("in Quote" + myID + " --- id=" +id);
		window.open( formUrl, 'popUpWindow','height=500,width=800,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no, status=yes' );
	
	}
	
	
	
</script>
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
<!-- ********************************************************************************************************************************************************* -->
<body style="font-family: sans-serif; color: black; font-size: 1;">
 <%@include  file="includes/header.html" %>
 
 
 
<div style="padding-left:20px">
  <h3>FIS Booking Detail Page</h3>
  
  
  <% 
  String start =  (String) session.getAttribute("startDate");
String end =  (String) session.getAttribute("endDate");
//out.println("<h5>Report Date: " + start     + " through " + end + "</h5>");
  
  
  %>

</div>
<!-- ********************************************************************************************************************************************************* -->
<%!  

String formUrl = null;
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
/*
public String TableHeaderRow(String thead) {
	
	//String thead = null;
	String color1 = "#5DADE2";
	String style1 = "font-family: sans-serif; color: white;";	  
	//thead = "<tr bgcolor=" + color1 +  "style=" + style1 + ">";
	thead = "<tr class=\"b\" "  +  "style=" + style1 + ">";
	//thead += "<th class=\"b\">App Number</th>";
	
	
	return thead;	
}
*/
/*************************************************************************************************************************************************************/

public String TableHeader(){
	String thead = null;
	//String color1 = "#5DADE2";
	//String style1 = "font-family: sans-serif; color: white;";	  
	//thead = "<tr bgcolor=" + color1 +  "style=" + style1 + ">";
	//thead = "<tr class=\"b\" "  +  "style=" + style1 + ">";
	//thead += "<th class=\"b\">App Number</th>";
	thead += "<th>Status</th>";
	thead += "<th>Contract ID</th>";
	thead += "<th>App Key</th>";
	thead += "<th class=\"c\">Customer Name</th>";
	thead += "<th>Acct Code</th>";
	thead += "<th>Purch Option</th>";
	thead += "<th>Prog Type</th>";
	thead += "<th>Arrears</th>";
	thead += "<th>Branch</th>";
	thead += "<th class=\"c\">Rep Name</th>";
	thead += "<th>Payment Amount</th>";
	thead += "<th>Residual Amount</th>";
	
	thead += "<th>Product Line</th>";
	thead += "<th>Term</th>";
	thead += "<th>Equip Cost</th>";
	thead += "<th>List Price</th>";
	thead += "<th>ROI NOMPT</th>";
	thead += "<th>SCD Code</th>";
	thead += "<th>Local Code</th>";
	
	
	
	thead += "<th class=\"c\">Rep 2</th>";
	thead += "<th class=\"c\">Rep 3</th>";
	thead += "<th class=\"c\">Promo Desc</th>";
	
	thead += "<th>New Cust</th>";
	thead += "<th>Prod = 0070</th>";
	thead += "<th>Comp Conv</th>";
	thead += "<th>Booking Date</th>";

	thead += "</tr>";	
	return thead;
} 
%>
<!-- ********************************************************************************************************************************************************* -->
<script>
/*
	var data = ${jsonStr}
	for (var i=0; i<data.length; i++) {
		document.write(data[i] + <BR>)
	}
*/
</script>


<%
	//out.println("<h6>  </h6><input class=\"btn\"  id=\"search\" type=\"text\" placeholder=\"Enter Text to Filter...\" >");
String startDate =  (String) session.getAttribute("startDate");
String endDate =  (String) session.getAttribute("endDate");
String mType =  (String) session.getAttribute("mType");

//formUrl = "http://uspacv3as18:8181/webreport/bookexcel?startDate=" + startDate + "&endDate=" + endDate + "&mType=" + mType;
//String formUrl =  (String) session.getAttribute("formUrl");
/*
System.out.println("************** JSP startDate=" + startDate);
System.out.println("************** JSP endDate=" + endDate);
System.out.println("************** JSP mType=" + mType);
System.out.println("**************^^ JSP formUrl=" + formUrl);
*/
//formUrl = "/webreport/bookexcel";
 

String formUrl =  (String) session.getAttribute("formUrl");


  /***************************************************************************************************************************************/
  	//String  s1  = (String) session.getAttribute("APP_ID"); 
  String xDataItem = null;
  //String color1 = "plum";
  String style1 = "font-family: sans-serif; color: white;";
  String rowEven = "#D7DBDD";
  String rowOdd = "AEB6BF";

  //retrieve your list from the request, with casting 
  ArrayList<String> list = new ArrayList<String>();
  ArrayList<String> tokens = new ArrayList<String>();
  
  //String rowColor = null;
  list = (ArrayList<String>) session.getAttribute("strArr");
  //out.println("listSize=" + list.size());
  	if (list.size() > 0) {
  		for (int i = 0; i < list.size(); i++) {
	  		xDataItem = list.get(i);
			String token_list[] = xDataItem.split(":");
			for (int x = 0; x < token_list.length; x++) {
				tokens.add(token_list[x]);
				
			}
  		}		
  		//out.println("<table class=\"tablesorter\" border=1> <thead> <tr>");
  		
  		
  		out.println("<table class=\"tablesorter\" border=1>");
		String filePath = "C:\\Java_Dev\\props\\headers\\BR_Detail_headers.txt";
		ArrayList<String> headerArr = readHeader(filePath);

		HashMap<String, String> hmap = new HashMap<String, String>();
		for (int k = 0; k < headerArr.size(); k++) {
			//out.println("K=" + headerArr.get(k) + " V=" + tokens.get(k) + "<BR>");
			hmap.put(headerArr.get(k), tokens.get(k));
		}
		Map<String, String> treeMap = new TreeMap<String, String>(hmap);
		
		/* Display content using Iterator*/
	      Set set = treeMap.entrySet();
	      Iterator iterator = set.iterator();
	      int z = 1;
	      String thead = "";
	      String thead2 = "";
	      String tdata = "";
	      //out.println("<thead><tr>");
	      String color2;
		  String style2 = null;

		  color2 = "#5DADE2";
		  style2 = "font-family: sans-serif; color: white;";
		  //thead2 = "<thead><tr bgcolor=" + color2 +  "style=" + style2 + ">";
		  
		  thead2 = "<thead><tr class=\"b2\"  bgcolor=\"#5DADE2\"  >";
	      while(iterator.hasNext()) {
		       Map.Entry mentry = (Map.Entry)iterator.next();
		      //out.println("key is: "+ mentry.getKey() + " & Value is: ");
		       //out.println(mentry.getValue() + "<br>");
		       if (((z % 5) != 0)) {
		    	   thead += "<th>" + mentry.getKey() + " </th>";
		    	   tdata += "<td>" + mentry.getValue() + " </td>";
		       } else {
		    	   thead += "<th>" + mentry.getKey() + " </th>";
		    	   tdata += "<td>" + mentry.getValue() + " </td>";
		    	  // out.println("<thead><tr>" + thead + "</tr></thead>");
		    	   
		    	   out.println(thead2 + thead + "</tr></thead>");
		    	   out.println("<tr  bgcolor=\"#D7DBDD\">" + tdata + "</tr>");
		    	   thead = "";
		 	       tdata = "";
		    	   
		       }
		       z++;
	      }
	      out.println("</table>");
		
		/*
		String thead;
		thead = null;
		String color2;
		String style2 = null;

		color2 = "#5DADE2";
		style2 = "font-family: sans-serif; color: white;";
		//thead = "<tr bgcolor=" + color2 +  "style=" + style2 + ">";
		thead = "<tr class=\"b\" " + "style=" + style1 + ">";
		
		
		

		for (int k = 0; k < headerArr.size(); k++) {

			//out.println(TableHeaderRow(thead));
			//thead += "<th>";

			if (((k % 5) != 0)) {
				thead += "<th>" + headerArr.get(k) + " </th>";
				//thead +=  headerArr.get(k)  ;
			} else {
				//out.println("** " + headerArr.get(k) + "<br>");
				thead += "</tr><tr>";

				out.println(thead);
				System.out.println("** " + thead + "\n");
				thead = "";
			}

		}
		out.println("</table>");
		out.println("DISPLAY DATA HERE." + "Size=" + list.size() + "<br>");
*/
	} else {
		out.println("No Asset data to display." + "<br>");
	}
%>

<!-- ********************************************************************************************************************************************************* -->	


</body>
</html>