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
		width: 1200px;
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
<script type="text/javascript" src="includes/js/chartjs/getappdataLPS.js"></script>



<script>
<!-- 
$(document).ready(function(){

    /* Get browser */
    $.browser.chrome = /chrome/.test(navigator.userAgent.toLowerCase());

    /* Detect Chrome */
    if($.browser.chrome){
        /* Do something for Chrome at this point */
        alert("You are using Chrome!");
        
        /* Finally, if it is Chrome then jQuery thinks it's 
           Safari so we have to tell it isn't */
        $.browser.safari = false;
    }

    /* Detect Safari */
    if($.browser.safari){
        /* Do something for Safari */
        alert("You are using Safari!");
    }

});


function myFunction() { 
    if((navigator.userAgent.indexOf("Opera") || navigator.userAgent.indexOf('OPR')) != -1 ) 
   {
       alert('Opera');
   }
   else if(navigator.userAgent.indexOf("Chrome") != -1 )
   {
       alert('Chrome');
   }
   else if(navigator.userAgent.indexOf("Safari") != -1)
   {
       alert('Safari');
   }
   else if(navigator.userAgent.indexOf("Firefox") != -1 ) 
   {
        alert('Firefox');
   }
   else if((navigator.userAgent.indexOf("MSIE") != -1 ) || (!!document.documentMode == true )) //IF IE > 10
   {
     alert('IE'); 
   }  
   else 
   {
      alert('unknown');
   }
   }
-->
</script>



</head>
<body>

<div id="chart-container">
	<!--  <canvas id="pie-chart" width="100" height="100"></canvas>  -->
	
	 <canvas id="mycanvasLPS" ></canvas>
	
</div>



</body>
</html>