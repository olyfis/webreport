<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 pageEncoding="ISO-8859-1"%>
 <%@ page import="java.util.ArrayList"%>
 <%@ page import="java.net.InetAddress"%>
 
 <%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.*"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="com.olympus.olyutil.Olyutil"%>
 
 
 
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

 <!--  Call servlet on load
<jsp:include page="/sfquery" flush="true" />
<c:out value="${strArrID}"></c:out>
 
  <c:forEach items="${strArrID}" var="id">
            ${id.id} <br />
        </c:forEach>
 -->
 
 <%!
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res  = null;
 
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	 
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\getcontractid.sql";
	
	public static ArrayList<String> getDbData(String startDate) throws IOException {
		FileInputStream fis = null;
		FileReader fr = null;
		String s = new String();
        StringBuffer sb = new StringBuffer();
        ArrayList<String> strArr = new ArrayList<String>();
		try {
			fis = new FileInputStream(propFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties connectionProps = new Properties();
		connectionProps.load(fis);
		 
		fr = new FileReader(new File(sqlFile));
		
		// be sure to not have line starting with "--" or "/*" or any other non alphabetical character
		BufferedReader br = new BufferedReader(fr);
		while((s = br.readLine()) != null){
		      sb.append(s);
		       
		}
		br.close();
		//displayProps(connectionProps);
		String query = new String();
		query = sb.toString();	
		//System.out.println("Query=" + query);	 
		try {
			con = Olyutil.getConnection(connectionProps);
			if (con != null) {
				//System.out.println("Connected to the database");
				 
				statement = con.prepareStatement(query);
				statement.setString(1, startDate);
				
				ResultSet rs = statement.executeQuery();		
				ResultSetMetaData rsmd = rs.getMetaData();
				int numColumns = rsmd.getColumnCount();
				int columnsNumber = rsmd.getColumnCount();
				String outDataLine = "";
				String sep = ":";
				//res = Olyutil.getResultSetPS(statement);
				//System.out.println("numCol=" +  numColumns  + "Col=" +  columnsNumber);
				if (!rs.next()) {                            //if rs.next() returns false                                           //then there are no rows.
				    System.out.println("No records found");
				} else {
				    do {
				        // Get data from the current row and use it
				        for (int i = 1; i <= columnsNumber; i++) {
						if (i > 1) {
							// System.out.print(";");
							outDataLine += sep;
						}
						 String columnValue = rs.getString(i);
						//System.out.println("CV=" + columnValue + "--");
						//outDataLine += columnValue;
						strArr.add(columnValue);
				 }
				        
				    } while (rs.next());
				}
		}
	
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return strArr;
	}
 
 %>
<!--  ********************************************************************************************************************************************************** --> 
 <%
 
 
 
 String at = request.getParameter("atype");
 String startDate = request.getParameter("date2");
 //System.out.println("Date=" + startDate + "--");
 ArrayList<String> list = new ArrayList<String>();
    	//out.println("atype=" + at);

    String hostname = InetAddress.getLocalHost().getHostName();
    
    	if (at != null) {

    		if (at.equals("15")) { // 

    			//out.println("<b>ID:</b> <input type=\"text\" id=\"id\" value=101-0014274-005 />");
    			//out.println("<b>ID:</b> <input type=\"text\"  name=\"id\"  value=\"XXX-XXXXXXX-XXX\" />");
    			%>
    			<!--  
    			<select class="form-control" name="id" id="id">
                <option   value="0" selected="">Select Contract</option>
                <option   value="101-0008984-008"  >101-0008984-008</option>
                </select>

-->


				<select class="form-control" name="id" id="id">
					<option  value="" selected="">Select Contract to Validate</option>
					 
					<%
						 
								String item = "";
								ArrayList<String> strArr = null;
								strArr = getDbData(startDate);
								for (int i = 0; i < strArr.size(); i++) {
									item = strArr.get(i);
									//System.out.println("ITEM=" + item + "--");
					%>
					<option value="<%=item%>"><%=item%></option>
			
			
					<%
						}
					%>
				</select>

	<%
		} else {
				out.println("Click Run");
			}
		}
		//out.println( "<input type=\"text\" id=\"id\" value="   + session.getAttribute("atype") + "/>");
		//out.println( "<input type=\"text\" id=\"id\" value="   + id + "/>");
	%>
    
 </body>
 </html>