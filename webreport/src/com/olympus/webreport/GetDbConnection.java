package com.olympus.webreport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import com.olympus.olyutil.Olyutil;

public class GetDbConnection {
	/****************************************************************************************************************************************************/
	public static Connection getDbConn(String propFile) throws IOException {
		Connection conn = null;
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(propFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {

			Properties connectionProps = new Properties();
			connectionProps.load(fis);

			conn = Olyutil.getConnection(connectionProps);
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return conn;
	} // End getDbConn

	/****************************************************************************************************************************************************/

	public static ArrayList<String> getDbData(Connection conn, String sqlFile, String query, String param1)
			throws IOException, SQLException {

		// Connection conn = null;
		FileReader fr = null;
		String s = new String();
		StringBuffer sb = new StringBuffer();
		ArrayList<String> strArr = new ArrayList<String>();
		// Statement statement = null;
		PreparedStatement statement;
		ResultSet res = null;

		// conn = getDbConn(propFile);
		if (conn != null) {
			// System.out.println("Connected to the database");
			statement = conn.prepareStatement(query);

			// System.out.println("***^^^*** Param1=" + param1);
			statement.setString(1, param1);
			res = Olyutil.getResultSetPS(statement);
			strArr = Olyutil.resultSetArray(res, ":");
		}
		return strArr;
	}

	/****************************************************************************************************************************************************/
	public static String getQuery(String sqlFile) throws IOException, SQLException {

		String s = new String();
		StringBuffer sb = new StringBuffer();
		String query = new String();
		FileReader fr = null;
		fr = new FileReader(new File(sqlFile));

		// be sure to not have line starting with "--" or "/*" or any other non
		// alphabetical character
		BufferedReader br = new BufferedReader(fr);
		while ((s = br.readLine()) != null) {
			sb.append(s);

		}
		br.close();
		// displayProps(connectionProps);
		query = sb.toString();
		// System.out.println("Query=" + query);

		return query;
	}

	/****************************************************************************************************************************************************/

}
