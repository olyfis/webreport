<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.File"%>
<%@ page import="java.util.ArrayList"%>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Olympus FIS Web Report Authentication Page</title>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.9.1/jquery.tablesorter.min.js"></script>
<!--  <script type="text/javascript" src="includes/js/tablesort.js"></script> -->
<style><%@include file="includes/css/reports.css"%></style>
<style><%@include file="includes/css/table.css"%></style>
<style><%@include file="includes/css/header.css"%></style>
<style><%@include file="includes/css/login.css"%></style>
<!-- ********************************************************************************************************************************************************* -->
</head>
<!-- ********************************************************************************************************************************************************* -->
<body style="font-family: sans-serif; color: black; font-size: 1;">
 <%@include  file="includes/header.html" %>
<div style="padding-left:20px">
  
  
   
</div>
<!-- ********************************************************************************************************************************************************* -->
	<form method="post" action="j_security_check">
		<div class="box">
		<br>
			<h1>FIS Web Reports</h1>
			<label>  <b>Username</b> </label>
			<input type="text" name="j_username"  onblur="field_blur(this, 'j_username');" class="email" value="fisrep" />
			<label>  <b>Password</b> </label>
			<input type="password" name="j_password"  onblur="field_blur(this, 'j_password');" class="email" />
			
			<input type="submit" value="Sign In" class="btn" />

<!-- 
			<input type="text" name="j_username" size="20" >
			<input type="password" name="j_password" size="20" >
			 
				onFocus="field_focus(this, 'email');"
				onblur="field_blur(this, 'email');" class="email" /> <input
				type="password" name="email" value="email"
				onFocus="field_focus(this, 'email');"
				onblur="field_blur(this, 'email');" class="email" /> 
			
				<input type="password" name="j_password" size="20" >
				<a href="#"><div class="btn">Sign In</div></a>
			-->

		</div>
		<!-- End Box -->

	</form>
</body>
</html>