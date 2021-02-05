package com.olympus.contract.validation;



	
	import java.io.BufferedReader;
	import java.io.File;
	import java.io.FileInputStream;
	import java.io.FileNotFoundException;
	import java.io.FileReader;
	import java.io.IOException;
	import java.io.PrintWriter;
	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
	import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
	import javax.servlet.annotation.WebServlet;
	import javax.servlet.http.HttpServlet;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;

	import org.json.JSONArray;
	import org.json.JSONObject;
	import org.w3c.dom.NodeList;

	import com.olympus.olyutil.Olyutil;

	//RUN: http://localhost:8181/webreport/reportlist?startDate=2020-03-06&endDate=2020-03-10&jflag=skip
	//RUN: http://localhost:8181/webreport/reportlistrange?startDate=2020-04-02&endDate=2020-04-07
	@WebServlet("/reportlistrange")
	public class ValidateContractRange extends HttpServlet {

		// Service method of servlet
		static Statement stmt = null;
		
		static NodeList node = null;
		static String s = null;
		
		static Connection con = null;
		static ResultSet res = null;
		static private PreparedStatement statement;
		
		static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
		static String sqlFile = "C:\\Java_Dev\\props\\sql\\contractvalidate.sql";
		static String sqlIdFile = "C:\\Java_Dev\\props\\sql\\getcontractid_range.sql";
		
		/****************************************************************************************************************************************************/
		// String dateFmt = formatDate("yyyy-MM-dd");
		// String dateFmt = formatDate("yyyy-MM-dd hh:mm:ss.SSS");
		
		public static String formatDate(String format) throws IOException {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			Date date = new Date();
			return(dateFormat.format(date));
		}
		/****************************************************************************************************************************************************/

		
		
		/****************************************************************************************************************************************************/
		public static ArrayList<String> getDbDataRange(String startDate, String endDate, String sqlSrc) throws IOException {
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

			fr = new FileReader(new File(sqlSrc));

			// be sure to not have line starting with "--" or "/*" or any other non
			// alphabetical character
			BufferedReader br = new BufferedReader(fr);
			while ((s = br.readLine()) != null) {
				sb.append(s);

			}
			br.close();
			// displayProps(connectionProps);
			String query = new String();
			query = sb.toString();
			// System.out.println("Query=" + query);
			try {
				con = Olyutil.getConnection(connectionProps);
				if (con != null) {
					// System.out.println("Connected to the database");
					statement = con.prepareStatement(query);

					// System.out.println("***^^^*** contractID=" + contractID);
					statement.setString(1, startDate);
					statement.setString(2, endDate);
					res = Olyutil.getResultSetPS(statement);
					strArr = Olyutil.resultSetArray(res, ":");
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

		/****************************************************************************************************************************************************/
		public static JSONArray getJSONData(ArrayList<String> errArr, String sep ) throws IOException {
			JSONArray jsonArr = new JSONArray();
			String[] strSplitArr = null;
			String[] keys = new String[] {"bookDate", "ID", "errNum", "url"};
			for (String item : errArr) { // iterating ArrayList
				strSplitArr = Olyutil.splitStr(item, sep);
				
			}
			
			if (errArr.size() > 0) {
				for (String data : errArr) {
					//System.out.println("***^^^*** Item=" + data);
					// String data2 = data.replaceAll("null", "");
					strSplitArr = Olyutil.splitStr(data, sep);
					int k = 0;
					JSONObject cellObj = new JSONObject();
					int i = 0;
					for (String token : strSplitArr) {
						int l = strSplitArr.length;
						String token2 = token.replaceAll("null", "");
						cellObj.put(keys[i++], token2);
						// System.out.println("SZ=" + l + "ID=" + strSplitArr[0] );	
						
					}
					jsonArr.put(cellObj);
				}
			}
			
			return(jsonArr);
		}
		
		/****************************************************************************************************************************************************/

		/****************************************************************************************************************************************************/
		public static void displayJsonArray(JSONArray jArr) {

			//System.out.println("jArrSZ=" + jArr.length() );
			for (int i = 0; i < jArr.length(); i++) {
				JSONObject row = jArr.getJSONObject(i);

				if (row instanceof JSONObject) {
					Set<String> keys = ((JSONObject) row).keySet();

					// System.out.println("%%%%%%%%% KEYS %%%%%%%%" + keys.toString() + "keyNum=" +
					// keys.size());
					for (String key : keys) {
						// System.out.println(key + ":" + jsonObject.get(key));
						System.out.println("******* displayJsonArray():  Key: " + key + " -> " + ((JSONObject) row).get(key));
					}
				}
			}
		}
		/****************************************************************************************************************************************************/
		public static void writeJson( HttpServletResponse response, JSONArray jsonArr ) throws IOException {
			PrintWriter out = response.getWriter();
	     	//response.setContentType("application/json");
	     	//response.setCharacterEncoding("UTF-8");
	     	int jaSZ = jsonArr.length();
	     	boolean endBracket = false;
	     	//System.out.println("********************************** JRtnArrSZ=" + jaSZ);
	     	out.write("[");
	     	if (jaSZ > 0) { // Data in JSONArray
	     		 // write initial array bracket
	     		endBracket = true;
	     		for (int i = 0; i < jsonArr.length(); i++) {
					JSONObject row = jsonArr.getJSONObject(i);

					if (row instanceof JSONObject) {
						Set<String> keys = ((JSONObject) row).keySet();
						out.write(jsonArr.getJSONObject(i).toString());
						if (i < (jsonArr.length() - 1)) {
							out.write(",");
							out.println();
							
						}
		
					}
	     		} // end for
	     		//if (endBracket) {
					//out.write("]");
				//}
	     		
	     	}
	     	out.write("]");	
		}

		/****************************************************************************************************************************************************/

		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String sqlFile1 = "C:\\Java_Dev\\props\\sql\\RateCheck.sql";
			Connection conn= null;
			JSONArray jRtnArr =  new JSONArray();
		 
			String dispatchJSP = "/valerrorsrange.jsp";
			String sDate = "";
			String eDate = "";
			String today = "";
			String qDate = "";
			JSONArray jsonArr = new JSONArray();
			ArrayList<String> strArr = new ArrayList<String>();
			ArrayList<String> idArr = new ArrayList<String>();
			ArrayList<String> errArr = new ArrayList<String>();
			Map<String, Boolean> contractErrs = null;
			boolean errStat = false;
			PrintWriter out = response.getWriter();
			response.setContentType("text/JSON");
			
			String todayDate = "today";
			String dateValue = request.getParameter(todayDate);

			String useDate = "useDate";
			String useValue = request.getParameter(useDate);
			
			String paramName = "startDate";
			String paramValue = request.getParameter(paramName);

			String endDate = "endDate";
			String endDateValue = request.getParameter(endDate);
			String jFlag = "jflag";
			String jFlagValue = request.getParameter(jFlag);
			String jVal = "";
			
			Properties connProps = new Properties();
			Timestamp timestampB = new Timestamp(System.currentTimeMillis());
			try {
				connProps = Olyutil.getPropertiesObj(propFile);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if ((dateValue != null && !dateValue.isEmpty())) {
				today = dateValue.trim();
				//System.out.println("*** sDate:" + sDate + "--");
			}
			
			
			
			int errCount = 0;
			if ((paramValue != null && !paramValue.isEmpty())) {
				sDate = paramValue.trim();
				//System.out.println("*** sDate:" + sDate + "--");
			}
			if ((endDateValue != null && !endDateValue.isEmpty())) {
				eDate = endDateValue.trim();
				System.out.println("*** eDate:" + eDate + "--");
			}
			if ((jFlagValue != null && !jFlagValue.isEmpty())) {
				jVal = jFlagValue.trim();
				//System.out.println("*** jVal:" + jVal + "--");
			}
			
			if (today.equals("today")) {
				System.out.println("*** Setting today's date for query");
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				today = dateFormat.format(date);
				
				sDate = today;
				eDate = today;
				jVal = "json";
				System.out.println("Today's Date: " + today + "-- sDate=" + sDate + "-- eDate=" + eDate); //2019-09-20 
			}
		
			if ((useValue != null && !useValue.isEmpty())) {
				qDate = useValue.trim();
				sDate = qDate;
				eDate = qDate;
				jVal = "json";
				
				System.out.println("*** qDate:" + qDate + "--");
			}
			idArr = getDbDataRange(sDate, eDate, sqlIdFile);
			System.out.println("Begin Print -- Sz=" + idArr.size());
			 Olyutil.printStrArray(idArr);
			 System.out.println("End Print");
			String id = "";
			String bookDate = "";
			/*
			try {		 
				conn = Olyutil.getConnection(connProps);
				if (conn != null) {
					 //System.out.println("Connected to the database");				 
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
			*/
			String errStr = "";
			
			
			
		   
			for (String id_t : idArr) { // iterating ArrayList			
				String[] line = id_t.split(":");
				id = line[0];
				bookDate = line[1];
			
				try { // need open and close conn because of cursors issue
					conn = Olyutil.getConnection(connProps);
					if (conn == null) {
						System.out.println("Error: Could not establish connection to Database.");
						return;
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (id.length() > 0) {
					System.out.println("*** ID:" + id + "-- bookDate:" + bookDate + "--");
					 // if (id.equals("101-0017457-005") || id.equals("501-0017871-001")) { // 501-0017871-001
						
						try {
							errStr = ValidateRangeContractChk.doValidation(conn, id, bookDate);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//System.out.println("*** ID:" + id + "-- bookDate:" + bookDate + "-- errStrSZ=" + errStr.length());
	
						
						if (!Olyutil.isNullStr(errStr)) {
							errArr.add(errStr);
						}
						errStr = "";
						//strArr.clear();
					
					//} // end if id check
				} // end if len check
				try {
					conn.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} // end for
			
		/*
			for (String e : errArr) {
				System.out.println("Err=:" + e);	
			}
			*/
			Timestamp timestampE = new Timestamp(System.currentTimeMillis());
			// System.out.println("Begin:" + timestampB);
		    //System.out.println("End:" + timestampE);
			String dateFmt = formatDate("yyyy-MM-dd hh:mm:ss.SSS");
			if (jVal.equals("json")) {
				jRtnArr = getJSONData(errArr, ";");
				//displayJsonArray(jRtnArr); // code is valid
				writeJson(response, jRtnArr ); // write JSON 
				System.out.println("*** jVal:" + jVal + "-- Return JSON Array");
				 System.out.println("Begin:" + timestampB);
				 System.out.println("End:" + timestampE);
				
				//System.out.println("***************************************************************************************************************");
			} else {
				System.out.println("End:" + dateFmt);
				request.getSession().setAttribute("errArr", errArr);
				request.getRequestDispatcher(dispatchJSP).forward(request, response);
			
			}
		}

	}


