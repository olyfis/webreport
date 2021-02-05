<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<style><%@include file="includes/css/table.css"%></style>


<% 
  	String title =  "Olympus FIS Dashboard Charts"; 
	 
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
 
<style type="text/css">
	#chart-containerSBS {
		width: 800px;
		height: auto;
	}
#chart-containerSBO {
		width: 1000px;
		height: auto;
	}

#chart-containerLHP {
		width: 800px;
		height: auto;
	}
	
	#chart-containerLPS {
		width: 1000px;
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
 <script type="text/javascript" src="includes/js/chartjs/getappdataSBS.js"></script>
 
 
 <script type="text/javascript" src="includes/js/chartjs/getappdataLPS.js"></script>
 <script type="text/javascript" src="includes/js/chartjs/getappdataLHP.js"></script>
 

</head>
<body>
	<!-- 
 



  
<div id="chart-containerSBO">
	 <canvas id="mycanvasSBO" ></canvas>	
</div>
  
 <BR>
 
<div id="chart-containerSBS">
	 <canvas id="mycanvasSBS" ></canvas>	
</div>
 
-->

	<table border="2">
  <tr>
    <th class="b" > >95% Shipped by Owner</th>
    <th class="b"> >95% Shipped by Status</th>
  </tr>
  <tr>
    <td>
   		<div id="chart-containerSBO">
	 		<canvas id="mycanvasSBO" ></canvas>	
		</div>
    
    </td>
    <td>
		<div id="chart-containerSBS">
	 		<canvas id="mycanvasSBS" ></canvas>	
		</div>
</td>
  </tr>
</table>
  
<BR>
	<table border="2">
  <tr>
  <th class="b" > Leases Processed - Shipping Status</th>
    <th class="b" >Leases in House - Processing Status</th>
    
  </tr>
  <tr>
    <td>
   		<div id="chart-containerLPS">
	 		<canvas id="mycanvasLPS" ></canvas>	
		</div>
    
    </td>
    <td>
		<div id="chart-containerLHP">
	 		<canvas id="mycanvasLHP" ></canvas>	
		</div>
</td>
  </tr>
</table>

</body>
</html>