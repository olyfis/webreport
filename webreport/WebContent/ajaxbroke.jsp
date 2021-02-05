<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 pageEncoding="ISO-8859-1"%>
 <%@ page import="java.util.ArrayList"%>
 <%@ page import="java.net.InetAddress"%>
  <link rel="stylesheet" href="includes/css/calendar.css" />
    <script type="text/javascript" src="includes/scripts/pureJSCalendar.js"></script>
    


 <html>
 <head>
 <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
 <!-- 
<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>jQuery UI Datepicker - Default functionality</title>
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script>
  $( function() {
    $( "#datepicker" ).datepicker();
  } );
  
  $( function() {
	    $( "#datepicker2" ).datepicker();
	  } );
  
  </script>
  
  -->
 </head>
 <body>

 <!--  
<jsp:include page="/sfquery" flush="true" />
<c:out value="${strArrID}"></c:out>
 
  <c:forEach items="${strArrID}" var="id">
            ${id.id} <br />
        </c:forEach>
 -->
 <%
 
 
 
 String at = request.getParameter("atype");
 ArrayList<String> list = new ArrayList<String>();
    	//out.println("atype=" + at);

    String hostname = InetAddress.getLocalHost().getHostName();
    
    	if (at != null) {

    		if (at.equals("15")) { // NBVa report

    			//out.println("<b>ID:</b> <input type=\"text\" id=\"id\" value=101-0014274-005 />");
    			out.println("<b>ID:</b> <input type=\"text\"  name=\"id\"  value=\"XXX-XXXXXXX-XXX\" />");

    		} else if (at.equals("35")) { // asset report
    			out.println("<b>Date:</b> <input type=\"text\"  name=\"id\"  value=\"XXX-XXXXXXX-XXX\" />");
    		}  else if (at.equals("40")) { // 
    			out.print("<b>Lead ID:</b>");
    			out.print( "<tr> <td>");
    			out.print("<tr> <td class=\"table_heading\"></td> " );
    			list = (ArrayList<String>) session.getAttribute("strArrID");
    			out.print("<select name=\"leadID\" >" );
    			out.print("<option value=\"0\">Select Lead</option>" );
    			System.out.println("******** arrSize=" + list.size()); 			
    			if (! list.isEmpty()) {
	    			for(int i = 0; i < list.size(); i++) {  
	    				out.print("<option value=" + list.get(i)   +  ">" +list.get(i)  +  "</option>" );
	    			}
    			}			
    			out.print("</select></tr> </table></td> </tr>   </table> ");
    			out.println("<input type=\"hidden\"  name=\"getID\"  value=\"40\" />");
    			
    		}  
    	    else {
    			out.println("Click Run");
    		}
    	}
    	//out.println( "<input type=\"text\" id=\"id\" value="   + session.getAttribute("atype") + "/>");
    	//out.println( "<input type=\"text\" id=\"id\" value="   + id + "/>");
    %>
    
 </body>
 </html>