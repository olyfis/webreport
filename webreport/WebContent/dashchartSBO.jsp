<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<% 
  	String title =  "Olympus FIS Dashboard Charts"; 
	 
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
 
<style type="text/css">
	#chart-container {
		width: 800px;
		height: auto;
	}

</style>
<title><%=title%></title>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.9.1/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="includes/js/tableFilter.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="includes/js/chartjs/Chart.js"></script>
<script type="text/javascript" src="includes/js/chartjs/Chart.min.js"></script>
<script type="text/javascript" src="includes/js/chartjs/getappdataSBO.js"></script>
 <script type="text/javascript" src="includes/js/chartjs/getappdata.js"></script>

</head>
<body>
  
<div id="chart-container">
	 <canvas id="mycanvasSBO" ></canvas>	
</div>
 	
 <BR>
 
 

			</td>
  </tr>
 
</table>



</body>
</html>