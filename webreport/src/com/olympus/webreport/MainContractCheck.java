package com.olympus.webreport;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.olympus.olyutil.Olyutil;

public class MainContractCheck {
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\RateCheck.sql";
	
	
	public static void main(String[] args) throws SQLException {
		Connection conn = null;
		ArrayList<String> strArr = new ArrayList<String>();
		String query = null;
		
		try {
			conn = ValidateContractChecks2.getDbConn(propFile);
			query = ValidateContractChecks2.getQuery(sqlFile);
			strArr = ValidateContractChecks2.getDbData(conn, sqlFile, query, "2019-07-16");
			Olyutil.printStrArray(strArr);
			
			
		} catch (IOException e) { e.printStackTrace(); }
		

	}

}
