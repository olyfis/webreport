<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.File"%>
<%@ page import="java.util.ArrayList"%>
 

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Olympus FIS Booking Summary Report</title>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.9.1/jquery.tablesorter.min.js"></script>
<!--  <script type="text/javascript" src="includes/js/tablesort.js"></script> -->
<style><%@include file="includes/css/reports.css"%></style>
<style><%@include file="includes/css/table.css"%></style>
<style><%@include file="includes/css/header.css"%></style>
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
	
	var getDetails = function(id){
		var myID = document.getElementById(id).value;
		//alert("****** myID=" + myID + " ID=" + id);
		//alert("in Quote" + myID + " --- id=" +id);
		window.open("http://cvyhj3a27:8181/webreport/getdetails?appKey=" + myID, 'popUpWindow','height=1000,width=1200,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no, status=yes' );
		//window.open("http://cvyhj3a27:8181/fisAssetServlet/readxml?appID=" + myID);
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
  <h3>FIS Booking Summary Page</h3>
  
  
  <% 
  String start =  (String) session.getAttribute("startDate");
String end =  (String) session.getAttribute("endDate");
out.println("<h5>Report Date: " + start     + " through " + end + "</h5>");
  
  
  %>

</div>
<!-- ********************************************************************************************************************************************************* -->
<%!  

String formUrl = null;


public String TableHeader(){
	String thead = null;
	String color1 = "#5DADE2";
	String style1 = "font-family: sans-serif; color: white;";	  
	//thead = "<tr bgcolor=" + color1 +  "style=" + style1 + ">";
	thead = "<tr class=\"b\" "  +  "style=" + style1 + ">";
	//thead += "<th class=\"b\">App Number</th>";
	
	thead += "<th>Status</th>";
	thead += "<th>Contract ID</th>";
	thead += "<th>App Key</th>";
	thead += "<th>App Details</th>";
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
out.println("<h6>  </h6><input class=\"btn\"  id=\"search\" type=\"text\" placeholder=\"Enter Text to Filter...\" >");
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
%>


 <form name="excelForm" enctype="multipart/form-data" method="get" action=" <%=formUrl%> " \>
 
 
<% 
out.println("<input type=\"hidden\"  name=\"startDate\" value=\"" + startDate  +  "\" >" );
out.println("<input type=\"hidden\"  name=\"endDate\" value=\"" + endDate  +  "\" >" );
out.println("<input type=\"hidden\"  name=\"mType\" value=\"" + mType  +  "\" >" );
out.println("<input type=\"submit\" value=\"Save Excel File\" class=\"btn\" /> ");
out.println("</form>");
 
/***************************************************************************************************************************************/
//out.println("<input type=\"submit\" value=\"Save Booking Details\" class=\"btn\" /> ");

/***************************************************************************************************************************************/
	//String  s1  = (String) session.getAttribute("APP_ID"); 
String xDataItem = null;
String color1 = "plum";
String style1 = "font-family: sans-serif; color: white;";
String rowEven = "#D7DBDD";
String rowOdd = "AEB6BF";

//retrieve your list from the request, with casting 
ArrayList<String> list = new ArrayList<String>();
String rowColor = null;
list = (ArrayList<String>) session.getAttribute("strArr");
//out.println("listSize=" + list.size());
	if (list.size() > 0 ) {
		out.println("<table class=\"tablesorter\" border=1> <thead> <tr>");
		out.println(TableHeader());
		out.println("</tr></thead>");
		out.println("<tbody id=\"report\">");
	
		String  appKeyValue = "";
		for (int i = 0; i < list.size(); i++) {
			rowColor = (i % 2 == 0) ? rowEven : rowOdd;
			out.println("<tr bgcolor=" + rowColor + ">");
	
			xDataItem = list.get(i);
			String token_list[] = xDataItem.split(":");
			
			for (int x = 0; x < token_list.length; x++) {
				
				if ( x == 2) {
					appKeyValue =  token_list[x];
							out.println("<td class=\"odd\">" );
	%>
	
	   

       
					<form enctype="multipart/form-data" method="post">
						<input type="hidden" id="{APP_CONTRACT_NUMBER}" value="JJJJJJJ" /> 
						<!--  <input type="hidden" id="{APP_CONTRACT_NUMBER2}" value="<%= appKeyValue  %>" />   -->
						
						<input type="hidden" id="<%= appKeyValue  %>" value="<%= appKeyValue  %>" />
						<!--  Need unique ID value -->
						<input class="btn3"  type="button" onclick="getQuote(document.getElementById('<%= appKeyValue  %>').id)" value="<%= appKeyValue  %>"/> 
				
					</form>

	<%
						out.println("</td>");
				 } else if (x == 3) {
				 out.println("<td class=\"odd\"> " );
					
					%>
					
					   

				   
					<form enctype="multipart/form-data" method="post">
						<input type="hidden" id="{APP_CONTRACT_NUMBER}" value="JJJJJJJ" /> 
						<!--  <input type="hidden" id="{APP_CONTRACT_NUMBER2}" value="<%= appKeyValue  %>" />   -->
						
						<input type="hidden" id="<%= appKeyValue  %>" value="<%= appKeyValue  %>" />
						<!--  Need unique ID value -->
						<input class="btn3"  type="button" onclick="getDetails(document.getElementById('<%= appKeyValue  %>').id)" value="Details"/> 
				
					</form>
				   

	<%
					
					
					 out.println("</td>");
			out.println("<td class=\"odd\">" + token_list[x] + "</td>");
	
				} else {
					
					
						out.println("<td class=\"odd\">" + token_list[x] + "</td>");
					}
			} // end inner for
				out.println("</tr>");
		} // end outer for

			out.println("</tbody></table> ");

		} else {
			out.println("No Asset data to display." + "<br>");
		}
	%>

<!-- ********************************************************************************************************************************************************* -->	


</body>
</html>