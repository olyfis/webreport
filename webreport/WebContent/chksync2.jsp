<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="org.json.*"%>

    <% 
  	String title =  "Olympus FIS Check for Sync Errors"; 
    String formUrl =  (String) session.getAttribute("formUrl");
	JSONArray jsonArr = new JSONArray();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title><%=title%></title>
<!--  <link href="includes/appstyle.css" rel="stylesheet" type="text/css" /> 

<style><%@include file="includes/css/reports.css"%></style>
<style><%@include file="includes/css/table.css"%></style>
-->
<style><%@include file="includes/css/header.css"%></style>
<style><%@include file="includes/css/menu.css"%></style>
 <link rel="stylesheet" href="includes/css/calendar.css" />
    <script type="text/javascript" src="includes/scripts/pureJSCalendar.js"></script>








<!-- ********************************************************************************************************************************************************* -->

</head>
<body>
    
    
 <%@include  file="includes/header.html" %>

      
<!--   <img src="includes\images\logo.jpg" alt="logo"  height="100" width="225" align="right"> -->


<div style="padding-left:20px">
  <h3><%=title%></h3>
</div>

<BR>

<h5>This page will provide status on Rapport Sync issues..</h5>

  
 <%
 //ArrayList<String> list = (ArrayList<String>) session.getAttribute("strArr");
 JSONArray list = (JSONArray) session.getAttribute("jsonArr");
 String jFlagValue = (String) session.getAttribute("jflag");
int lsz = list.length();
		
System.out.println("***!!!!*** JFLAG=" + jFlagValue);
	
 
		 
%>
 
<BR>

<!--  action = servlet to call: http://localhost:8181/webreport/olrent?id=2019-08-31   -->
	 

<table class="a" width="40%"  border="1" cellpadding="1" cellspacing="1">
  <tr> <th class="theader"> <%=title%></th> </tr>
  <tr>
    <td class="table_cell">
    <!--  Inner Table -->
    <table class="a" width="100%"  border="1" cellpadding="1" cellspacing="1">
  <tr>
  
 
        <td width="40" valign="bottom" class="c">  
      <%
      if (lsz > 0 )	{ 
			out.println("<h3>Possible Sync Error:  " +   lsz  +" records in queue.</h3> ");	 
      } else {
    	  out.println("<h3>No Sync Errors at this time.  " );	 
      }
      %>
        
	</td>

    <td  valign="bottom" class="a">
    <!--  
    <form name="excelForm" enctype="multipart/form-data" method="get" action="<%=formUrl%> " \>
    
    <input type="submit" value="Save Excel File" class="btn" /> 
    </form>
    -->
	 
	</td>
    
    
   </tr></table>
    
    
    </td>
  </tr>
</table>


 
</body>
</html>