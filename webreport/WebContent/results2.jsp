<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ page import="java.net.InetAddress"%>
 

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Results</title>
</head>
<body>
<h4>Results Page</h4>




<%
	String at = request.getParameter("actiontype");
	String at2 = request.getParameter("getID");
    String hostname = InetAddress.getLocalHost().getHostName();

	//out.println("atype=" + at);
	//System.out.println("***^^^*** AT=" + at + "--");
	if (at != null) {

		if (at.equals("10")) {
			//String redirectURL = "http://cvyhj3a27:8181/evergreen/egreen";
			//String redirectURL = "http://" + hostname  + ":8181/webreport/evergreen";
			//String mType = request.getParameter("mType");
			//String excel = request.getParameter("EGEXL");
			String redirectURL = "http://" + hostname  + ":8181/reports/evergreenExcel" ;
			response.sendRedirect(redirectURL);

		} else if (at.equals("15")) {
			String id = request.getParameter("id");
			//out.println("ID:" + id + "--");
			String redirectURL = "http://" + hostname  + ":8181/webreport/nbva?id=" + id;
			response.sendRedirect(redirectURL);

		} else if (at.equals("5")) {
			String hostname2 = "cvyhj3a27";
			String redirectURL = "http://" + hostname2  + ":8181/webreport/flash.html";
			response.sendRedirect(redirectURL);
			
		} else if (at.equals("9")) {
			String hostname2 = "cvyhj3a27";
			String redirectURL = "http://" + hostname2  + ":8181/dashb/dashboard";
			response.sendRedirect(redirectURL);

		
		} else if (at.equals("99")) {
			String hostname2 = "cvyhj3a27";
			String redirectURL = "http://" + hostname2  + ":8181/penetration/displaypenetration.jsp";
			response.sendRedirect(redirectURL);

		
		} else if (at.equals("145")) {
			String hostname2 = "cvyhj3a27";
			String redirectURL = "http://" + hostname2  + ":8181/reports/olrentaccrued.jsp";
			response.sendRedirect(redirectURL);
		} else if (at.equals("115")) {
			String hostname2 = "cvyhj3a27";
			String redirectURL = "http://" + hostname2  + ":8181/reports/contracteot.jsp";
			response.sendRedirect(redirectURL);
		} else if (at.equals("150")) {
			String hostname2 = "cvyhj3a27";
			String redirectURL = "http://" + hostname2  + ":8181/nbvabuy/nbvagetasset.jsp";
			response.sendRedirect(redirectURL);
		} else if (at.equals("240")) {
			String hostname2 = "cvyhj3a27";
			String redirectURL = "http://" + hostname2  + ":8181/nbvabuy/nbvamenu.jsp";
			response.sendRedirect(redirectURL);
		}
		
		
		
		
		
		
		else if (at.equals("100")) {
			String hostname2 = "cvyhj3a27";
			String redirectURL = "http://" + hostname2  + ":8181/webreport/validatecontract.jsp";
			response.sendRedirect(redirectURL);

		
		}   else if (at.equals("26")) {
			String date = request.getParameter("date");
			 System.out.println("******************* DATE=" + date );
			String redirectURL = "http://" + hostname  + ":8181/webreport/ilbook?date=" + date;
			response.sendRedirect(redirectURL);
			
		}  else if (at.equals("38")  ) {
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String redirectURL = "http://" + hostname  + ":8181/webreport/rapportcpo?startDate=" + startDate + "&endDate=" + endDate;
			response.sendRedirect(redirectURL);
		
		
		
		} else if (at.equals("30") || at.equals("75")  || at.equals("25")   ) {
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String mType = request.getParameter("mType");
			
			String redirectURL = "http://" + hostname  + ":8181/webreport/book?startDate=" + startDate + "&endDate=" + endDate + "&mType=" + mType;
			response.sendRedirect(redirectURL);
			
		} else if (at.equals("35")) {
			String id = request.getParameter("id");
			out.println("ID:" + id + "--");
			String hostname2 = "cvyhj3a27";
			String redirectURL = "http://" + hostname2  + ":8181/fisAssetServlet/readxml?appID=" + id;
			response.sendRedirect(redirectURL);
			
		} else if (at.equals("250")) {
			String id = request.getParameter("id");
			//out.println("ID:" + id + "--");
			String hostname2 = "cvyhj3a27";
			String redirectURL = "http://" + hostname2  + ":8181/salesvalidation/salesval.jsp";
			response.sendRedirect(redirectURL);
			
		}
		
		
		
		else if (at.equals("40")) {
			String id = request.getParameter("leadID");
			out.println("leadID:" + id + "--");
			//String hostname2 = "cvyhj3a27";
			if (at2.equals("40")) {
				String redirectURL = "http://" + hostname  + ":8181/webreport/getlead?leadID=" + id ;
				response.sendRedirect(redirectURL);
			} else {
				String redirectURL = "http://" + hostname  + ":8181/webreport/sfquery" ;				
				response.sendRedirect(redirectURL);
			}	
		} else if (at.equals("50")) {
			String id = request.getParameter("leadID");
			out.println("leadID:" + id + "--");
			//String hostname2 = "cvyhj3a27";
			//String redirectURL = "http://" + hostname  + ":8181/webreport/gettoken?appID=" + id;
			String redirectURL = "http://" + hostname  + ":8181/webreport/getlead?leadID=" + id ;
			response.sendRedirect(redirectURL);	
		} else if (at.equals("45")) {
			String id = request.getParameter("id");
			//out.println("ID:" + id + "--");
			//String hostname2 = "cvyhj3a27";
			String redirectURL = "http://" + hostname  + ":8181/webreport/gettoken";
			response.sendRedirect(redirectURL);		
		} else if (at.equals("60")) {
			//String id = request.getParameter("id");
			//out.println("ID:" + id + "--");
			//String hostname2 = "cvyhj3a27";
			//String redirectURL = "http://" + hostname  + ":8181/webreport/lastship";
			String mType = request.getParameter("mType");
			String redirectURL = "http://" + hostname  + ":8181/reports/ilreport?mType=" + mType;
			
			response.sendRedirect(redirectURL);	
		} else if (at.equals("70")) {
			//String id = request.getParameter("id");
			//out.println("ID:" + id + "--");
			//String hostname2 = "cvyhj3a27";
			//String redirectURL = "http://" + hostname  + ":8181/webreport/ccaninfo";
			
			String mType = request.getParameter("mType");
			String redirectURL = "http://" + hostname  + ":8181/reports/ilreport?mType=" + mType;
			response.sendRedirect(redirectURL);	
		} else if (at.equals("85")) {
		 
			//out.println("ID:" + id + "--");
			//String hostname2 = "cvyhj3a27";
			
			//String redirectURL = "http://" + hostname  + ":8181/webreport/utilization" ;
			String mType = request.getParameter("mType");
			String redirectURL = "http://" + hostname  + ":8181/reports/ilreport?mType=" + mType;
			
			
			response.sendRedirect(redirectURL);	
		} else if ((at.equals("80")) || (at.equals("90")) || (at.equals("95")) ) {
			//String redirectURL = "http://" + hostname  + ":8181/webreport/snapshot";
			String mType = request.getParameter("mType");
			String redirectURL = "http://" + hostname  + ":8181/reports/ilreport?mType=" + mType;
			response.sendRedirect(redirectURL);	
		} else if (at.equals("125")   ) {
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			 
			
			String redirectURL = "http://" + hostname  + ":8181/webreport/reportlist?startDate=" + startDate + "&endDate=" + endDate ;
			response.sendRedirect(redirectURL);
			
		} else if (at.equals("135")   ) {
			//String id = request.getParameter("id");
			 
			String redirectURL = "http://" + hostname  + ":8181/webreport/validateasset.jsp"  ;
			response.sendRedirect(redirectURL);
		} else if (at.equals("140")   ) {
			//String id = request.getParameter("id");
			 
			String redirectURL = "http://" + hostname  + ":8181/webreport/chksync"  ;
			response.sendRedirect(redirectURL);
		} else if (at.equals("160")   ) {
			//String id = request.getParameter("id");		 
			String redirectURL = "http://" + hostname  + ":8181/reports/vapoexpire.jsp"  ;
			response.sendRedirect(redirectURL);
		} else if (at.equals("170")   ) {
			//String id = request.getParameter("id");		 
			String redirectURL = "http://" + hostname  + ":8181/reports/activecontracts"  ;
			response.sendRedirect(redirectURL);
		} else if (at.equals("300")   ) {
			//String id = request.getParameter("id");		 
			String redirectURL = "http://" + hostname  + ":8181/reports/amaster"  ;
			response.sendRedirect(redirectURL);
		} else if (at.equals("310")   ) {
			//String id = request.getParameter("id");		 
			String redirectURL = "http://" + hostname  + ":8181/reports/leasemaster"  ;
			response.sendRedirect(redirectURL);
		} else if (at.equals("130")   ) {
			//String id = request.getParameter("id");		 
			String redirectURL = "http://" + hostname  + ":8181/rest/ssbook"  ;
			response.sendRedirect(redirectURL);
		} 
		
		
		
		else if (at.equals("180")   ) {
			//String id = request.getParameter("id");		 
			String redirectURL = "http://" + hostname  + ":8181/booking/dailycommencement.jsp"  ;
			response.sendRedirect(redirectURL);
		} else if (at.equals("175")   ) {
			//String id = request.getParameter("id");		 
			String redirectURL = "http://" + hostname  + ":8181/reports/contractsdb"  ;
			response.sendRedirect(redirectURL);
		} else if (at.equals("210")   ) {
			//String id = request.getParameter("id");
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String redirectURL = "http://" + hostname  + ":8181/reports/orderrel?startDate=" + startDate + "&endDate=" + endDate ;
			response.sendRedirect(redirectURL);
		}
		
		
		
		
	}
%>
 
 

</body>
</html>