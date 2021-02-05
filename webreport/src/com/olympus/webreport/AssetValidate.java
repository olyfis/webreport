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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.NodeList;

import com.microsoft.sqlserver.jdbc.StringUtils;
//import com.olympus.asset.AssetData;
import com.olympus.asset.AssetValData;
import com.olympus.infolease.contract.ContractData;
//import com.olympus.infolease.contract.ContractData;
import com.olympus.olyutil.Olyutil;

/****************************************************************************************************************************************************/

@WebServlet("/assetchk")
public class AssetValidate extends HttpServlet {
	// Service method of servlet
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res = null;
	static NodeList node = null;
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";

	static String sqlFile = "C:\\Java_Dev\\props\\sql\\assetDataValidation.sql";

	/****************************************************************************************************************************************************/

	public static boolean isNullStr(String str) {

		if (str == null || str.equals("null")) {
			return true;
		}
		return false;
	}
	/****************************************************************************************************************************************************/
	public static void printStr(String[] items ) throws IOException {
		
		for (int i = 0; i <items.length; i++) {
			System.out.println("*** item[" + i + "]=" + items[i] + "--");		
		}
		
	}
	/****************************************************************************************************************************************************/
	public static ArrayList<String> getDbData(String id) throws IOException {
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
				statement.setString(1, id);
				res = Olyutil.getResultSetPS(statement);
				strArr = Olyutil.resultSetArray(res, ";");
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
	public static AssetValData loadAssetObj(String str, List<AssetValData> assets) throws IOException {

		AssetValData asset = new AssetValData();
		String[] items = str.split(";");
		//System.out.println("*** itemSz:" + items.length + "--");
		//System.out.println("STR=" + str );
		//printStr(items );
		 asset.setContract_no(items[0]);
		 asset.setCust_name(items[1]);
		 asset.setBooking_date(items[2]);
		 asset.setActiv_date(items[3]);
		 asset.setContract_term(items[4]);
		 asset.setTerm_date(items[5]);
		 asset.setId(items[6]);
		 asset.setProduct_line(items[7]);
		 asset.setAsset_vendor(items[8]);
		 asset.setEquip_desc(items[9]);
		 asset.setModel(items[10]);
		 asset.setNew_used(items[11]);
		 asset.setEquip_list_price(items[12]);
		 asset.setOrig_cost(items[13]);
		 asset.setEquip_cost(items[14]);
		 asset.setCost_pct_flag(items[15]);
		 asset.setResid_amt(items[16]);
		 asset.setOl_residual(items[17]);
		 asset.setAm_purchase_option(items[18]);
		 asset.setPur_option(items[19]);
		 asset.setAsset_classification(items[20]);
		 asset.setAum_user_amt1(items[21]);
		 asset.setState_tax_code(items[22]);
		 asset.setMisc_state_tax_code(items[23]);
		 asset.setCnty_tax_code(items[24]);
		 asset.setMisc_cnty_tax_code(items[25]);
		 asset.setCity_tax_code(items[26]);
		 asset.setMisc_city_tax_code(items[27]); 
		 asset.setAu_tax_misc(items[28]);
		 asset.setIfrs_code(items[29]);	 
		//System.out.println("%%%%%%%%%%%%%%%%%%%% USER_AMT1=" + items[21] + "--"  +" ID="  + items[0] + "--   "   + "AssetID="   + items[6]  + "--"  );	 
		return  asset;
	}

	/****************************************************************************************************************************************************/
	
	
	/****************************************************************************************************************************************************/
	public static List<AssetValData> loadData(ArrayList<String> strArr ) throws IOException {
		
		List<AssetValData> assets = new ArrayList<AssetValData>();
		//System.out.println("**** in loadData() -- SZ=" + strArr.size()  +"--");
		
		int j = 0;
		for (String str : strArr) { // iterating ArrayList
			AssetValData asset;
			//System.out.println("Asset:" + str + "--");
			asset = loadAssetObj(str, assets);
			assets.add(asset);
			j++;
		}
		return assets;
	}	
	/****************************************************************************************************************************************************/

	
	/****************************************************************************************************************************************************/

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idVal = "";
		String errorStr = "";
		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<String> errorArr = new ArrayList<String>();
		Map<String, Boolean> assetErrs = null;
		ArrayList<Map<String, Boolean>> assetErrsArr = new ArrayList<Map<String, Boolean>>();
		ArrayList<String> errArr = new ArrayList<String>();
		List<AssetValData> assets;
		String paramName = "id";
		String paramValue = request.getParameter(paramName);
		String errors = "";
		JSONArray jsonArr = new JSONArray();
		JSONObject jsonObj;
		if (!isNullStr(paramValue)) {
			idVal = paramValue.trim();
			// System.out.println("*** idVal:" + idVal + "--");
		}
		 
		strArr = getDbData(idVal);
		//Olyutil.printStrArray(strArr);
	
		assets = loadData(strArr );
		
		request.getSession().setAttribute("strArr", strArr);
		request.getSession().setAttribute("assets", assets);
		String dispatchJSP = "/validateassetresults.jsp";
		//System.out.println("*** arrSz:" + strArr.size()+ "--");
		//System.out.println("*** assetsSz:" + assets.size());
		 for (int k = 0; k < assets.size(); k++) {
			 //System.out.println("*** !!! *** ID:" + assets.get(k).getContract_no() + " -- Cust:"  + assets.get(k).getCust_name()     ); 
			 errorStr = ValidateChecks.validateAssetChk( assets.get(k));
			 
			 if (! errorStr.equals("noErrors")) {
					errArr.add(errorStr);
				}
			 
			 /*
			 if (! jsonObj.isEmpty()) {
				 jsonArr.put(jsonObj);	 
			 }
*/
			 //if ( ! errors.equals("noErrors")) {
				 //errArr.add(errors);
				
			 //}
		 }
		 
	
		// Olyutil.printStrArray(errArr);
		request.getSession().setAttribute("assetErrs",errArr);
		request.getRequestDispatcher(dispatchJSP).forward(request, response);
 
		
	}

	/****************************************************************************************************************************************************/

}
