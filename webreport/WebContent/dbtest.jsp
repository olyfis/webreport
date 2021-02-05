<%--

--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.*"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="com.olympus.olyutil.Olyutil"%>

 
<%@page contentType="text/html" pageEncoding="UTF-8"%>

 <%!
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res  = null;
 
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	 
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\getcontractid.sql";
	
	public static ArrayList<String> getDbData() throws IOException {
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
		System.out.println("Query=" + query);	 
		try {
			con = Olyutil.getConnection(connectionProps);
			if (con != null) {
				System.out.println("Connected to the database");
				statement = con.prepareStatement(query);
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
						System.out.println("CV=" + columnValue + "--");
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

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container">
            <div class="jumbotron text-center">
                <h1>Populate Dropdown List with database using jquery ajax in jsp</h1>
                <p><a style="color:red;" href="https://www.youtube.com/dharmeshmourya" target="_blank">For more videos subscribe to my channel!</a></p>
            </div>
            <br>
            <form id="form">
                <table class="table table-hover">
                    <tr>
                        <td>Contract</td>
                        <td>
                            <select class="form-control" id="country_id">
                                <option disabled="" value="" selected="">Select Contract</option>
                                <%
                                String item = "";
                                ArrayList<String> strArr = null;
                                strArr = getDbData();
                                for (int i = 0; i < strArr.size(); i++) {
                        	  		item = strArr.get(i); 
                        	  		//System.out.println("ITEM=" + item + "--");
                                %>
                                 <option value="<%=item%>"><%=item%></option>
                                 
                                
                                <%
                                    }
                                %>
                            </select>
                        </td>
                    </tr>

                 

                    <tr>
                        <td><input type="button" id="submit" value="Submit"></td>
                    </tr>
                </table>
            </form>
            <div style="color:red;" id="error"></div><!-- error message will display here. --->
            <div id="success"></div><!-- output message will display here. --->
        </div>
    </body>
</html>
<script>
    $(document).ready(function () {
        $("#country_id").on("change", function () {
            var country_id = $("#country_id").val();//id of country select box of index.jsp page;
                $.ajax({
                    url: "state.jsp",//your jsp page name
                    data: {country_id: country_id},//sending request to state.jsp page.
                    method: "POST",//HTTP method.
                    success: function (data)
                    {
                        $("#state_id").html(data);//output or response will display in state select box.
                    }
                });
        });
    });
</script>
<script type="text/javascript">
    $(document).ready(function () {
        $("#submit").on("click",function () {
            var country_id = $("#country_id").val();//id of country select box of index.jsp page;
            var state_id = $("#state").val();//coming value from state.jsp page.
            var city_name = $("#city").val();//coming value from city.jsp page.
            
            // check if country select box have blank or null value.
            if (country_id === ""||country_id===null)
            {
                $("#error").html("All fields are mandatory.");//this message will display in error div.
            }
            else
            {
                $("#error").html("");//show blank message when all select box doesn't have any null value.
                $.ajax({
                    url:"DisplayText",//your servlet or jsp page name.
                    method:"POST",//HTTP method.
                    data:{country_id:country_id,state_id:state_id,city_name:city_name},//sending request to DisplayText.java page.
                    success:function(data)
                    {
                        $("#success").html(data);//output or response will display in success div.
                        $("#form").trigger("reset");//this will reset the form.
                    }
                });
            }
        });
    });
</script>