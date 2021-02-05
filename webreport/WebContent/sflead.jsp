<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.*"%>
 

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Olympus FIS Salesforce Lead Query Result Set</title>

<style><%@include file="includes/css/reports.css"%></style>
<style><%@include file="includes/css/header.css"%></style>

<!-- ********************************************************************************************************************************************************* -->

<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.9.1/jquery.tablesorter.min.js"></script>

<script>
	/*
$(function(){
          $("#dash").tablesorter({widgets: ['zebra']});
        });
	*/


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
  <h3>FIS Salesforce Lead Query Page</h3>
</div> <BR>
<!-- ********************************************************************************************************************************************************* -->
<%!  
public String TableHeader(){
	String thead = null;
	String color1 = "#5DADE2";
	String style1 = "font-family: sans-serif; color: white;";	  
	//thead = "<tr bgcolor=" + color1 +  "style=" + style1 + ">";
	thead = "<tr class=\"b\" "  +  "style=" + style1 + ">";
	//thead += "<th class=\"b\">App Number</th>";
	thead += "<th>Lead ID</th>";
	thead += "<th class=\"c\">First Name</th>";
	thead += "<th>Last Name</th>";
	

	
	thead += "<th>Postal Code</th>";
	thead += "<th>Phone</th>";
	thead += "<th>Email</th>";
	thead += "<th>Company</th>";
	thead += "</tr>";	
	return thead;
} 

/****************************************************************************************************************************************************************/
public void displayHash(Hashtable hashtable) {	
	Enumeration names = null;
	String key = null;

	names = hashtable.keys();
	while (names.hasMoreElements()) {
		key = (String) names.nextElement();
		System.out.println("Key:" + key + " -> Value:" + hashtable.get(key));
	}
}
/****************************************************************************************************************************************************************/
public void displayHtmlTable(Hashtable hashtable) {	
	

}
/****************************************************************************************************************************************************************/



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
String  s1  = (String) session.getAttribute("id"); 

//retrieve your list from the request, with casting 
//ArrayList<String> list = new ArrayList<String>();

Hashtable hashtable = new Hashtable();
String rowColor = null;
hashtable = (Hashtable) session.getAttribute("hash");
displayHash(hashtable);
displayHtmlTable(hashtable);
//out.println("listSize:" + hashtable.size()   + "   ID:" + s1);



if (hashtable.size() > 0 ) {
	
	String dataItem = null;
	String color1 = "plum";
	String style1 = "font-family: sans-serif; color: white;";
	out.println("<table border=\"1\" class=\"a\"> ");
	out.println("<tr bgcolor=" + color1 +  "style=" + style1 + ">" );
	out.println(TableHeader());
 
	String rowEven = "#D7DBDD";
	//String rowOdd = "AEB6BF";
	
	Enumeration names = null;
	Enumeration key2 = null;
	String key = null;

	names = hashtable.keys();
	int i = 1;
	//out.println("<tr bgcolor=" + rowColor +  ">");
	
	out.println("<tr bgcolor=" + rowEven +  ">");
	while (names.hasMoreElements()) {
		key = (String) names.nextElement();
		if (key.equals("attributes")){
			continue;
		}
		
		//rowColor = (i++ % 2 == 0) ? rowEven : rowOdd;
		dataItem =  (String) hashtable.get(key).toString();
		// out.println("<td class=\"odd\">"   + dataItem + "</td>"  );
		 out.println("<td>"   + dataItem + "</td>"  );
		 //System.out.println("****** Key:" + key + " -> Value:" + hashtable.get(key) + "   ************ Data:" + dataItem);
	}
	out.println("</tr></table>");
	
	
} else {
	out.println( "No data to display." + "<br>");

}

//out.println("listSize:" +  "   ID:" + s1 );
//System.out.println("******** arrSize=" + list.size() + " ---- ID:"  + s1);
/*
	if (list.size() > 0 ) {
		String xDataItem = null;
		String color1 = "plum";
		String style1 = "font-family: sans-serif; color: white;";
		out.println("<table border=\"1\" class=\"a\"> ");
		 
		out.println("<tr bgcolor=" + color1 +  "style=" + style1 + ">" );
		out.println(TableHeader());
	 
		String rowEven = "#D7DBDD";
		String rowOdd = "AEB6BF";
		for(int i = 0; i < list.size(); i++) { 
	 
				rowColor = (i % 2 == 0) ? rowEven : rowOdd;
			out.println("<tr bgcolor=" + rowColor +  ">");
			xDataItem = list.get(i);
			//out.println( strArr  );
			//out.println( "<br>");
			String token_list[] = xDataItem.split(":");	
			 for(int x=0; x <token_list.length; x++) {
				 out.println("<td class=\"odd\">"   + token_list[x] + "</td>"  );			        
			}
			 out.println("</tr>");
		}	
		 
		out.println("</table> ");
		
	} else {
		out.println( "No data to display." + "<br>");
	}
*/
%>
<!-- ********************************************************************************************************************************************************* -->	
</body>
</html>