package com.olympus.onelink;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
//import com.olympus.util.JButils;
import org.w3c.dom.NodeList;

import com.olympus.olyutil.Olyutil;

import java.sql.*;
import java.text.DateFormat;
import java.text.Format.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import com.olympus.asset.AssetData;
import com.olympus.csv.CsvData;


// To run: http://localhost:8181/webreport/OneLink
@WebServlet("/OneLink")
public class OneLink  extends HttpServlet {
	// Service method of servlet
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res  = null;
	static NodeList  node  = null;
	static String s = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
	static String csvFile = "C:\\Java_Dev\\props\\onelink\\csv\\onelink.csv";
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\onelink.sql";
	
	
	/****************************************************************************************************************************************************/

	public static void displayProps(Properties p) {
		Enumeration keys = p.keys();
		while (keys.hasMoreElements()) {
		    String key = (String)keys.nextElement();
		    String value = (String)p.get(key);
		    System.out.println(key + ": " + value);
		}	
	}
	/****************************************************************************************************************************************************/

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
		//System.out.println("Query=" + query);	 
		try {
			con = Olyutil.getConnection(connectionProps);
			if (con != null) {
				//System.out.println("Connected to the database");
				statement = con.prepareStatement(query);
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
	public static List<AssetData> setArrData(ArrayList<String> strArr)   {	
		List<AssetData> assets = new ArrayList<AssetData>();
		
		int j = 0;
		for (String str : strArr) { // iterating ArrayList
			//System.out.println(str);
			String[] items = str.split(":");
			AssetData asset = new AssetData();
			
			if (items[1].equals("B/O") ) {
				continue;
			}
			asset.setId(items[0]);
			asset.setModel(items[1]);
			asset.setSerialNum(items[2]);
			double rentalAmt = Double.valueOf(items[3]);
			asset.setRentalAmt(rentalAmt);
			double EquipPercent = Double.valueOf(items[4]);
			asset.setEquipPercent(EquipPercent);
			double miscAmt = Double.valueOf(items[5]);
			asset.setMiscAmt(miscAmt);
			asset.setAssetSet(false);
			assets.add(j++, asset);
			/*
			for(int i = 0; i < items.length; i++) {
			    System.out.println("Item[" + i + "]= "  + items[i]);
			}
			*/
		}
		return assets;
	}
	/****************************************************************************************************************************************************/
	public static String getAssetInfo( String partNum, List<AssetData> assets) {
		String serNum = "";
		String assetID = "";
		double equipPct = 0.0;
		double miscAmt = 0.0;
		double pptUplift = 0.0;
		double rentalAmt = 0.0;
		String result = "";
		String sep = ":";
		
		//System.out.println("***^^^*** in getCsvChanges -- assetList SZ=" + assetList.size() );
		 for(int j = 0 ; j < assets.size() ; j++) {
			 
			//System.out.println("***^^^*** isAsset=" +  assets.get(j).isAssetSet());
			  //System.out.println("***^^^*** partNum=" + partNum + "-- Model=" + assets.get(j).getModel() + "--" + "Flag=" + assets.get(j).isAssetSet() );
			 if (! assets.get(j).isAssetSet()   ) {		 
				//System.out.println("***^^^*** partNum=" + partNum + "-- Model=" + assets.get(j).getModel() + "--" + "Flag=" + assets.get(j).isAssetSet() );
				if ( partNum.equals( assets.get(j).getModel()) )  {
					assetID = assets.get(j).getId();
					serNum = assets.get(j).getSerialNum();
					equipPct = assets.get(j).getEquipPercent();
					miscAmt = assets.get(j).getMiscAmt();
					rentalAmt = assets.get(j).getRentalAmt();
					pptUplift = miscAmt * equipPct * .01;
					assets.get(j).setAssetSet(true);
					result = assetID + sep + serNum + sep +   rentalAmt + sep + pptUplift;
					return result;
					//csvLine.setPptUplift(Double.toString(pptUplift));  
					//csvLine.setSerialNumber(serNum);
					//csvLine.setRentAmt(Double.toString(rentalAmt)); 
					//System.out.println("***Begin Update:" + assets.get(j).getId() + " idx=" + j + "NewSN=" +csvLine.getSerialNumber() + "PPT=" + csvLine.getPptUplift());
					//System.out.println("***^^^*** PPTUplift=" + pptUplift);
				}	 	 
			 } 
		 }
		
		 return result;
	}
	
	/****************************************************************************************************************************************************/

 
	 public static String  loadCsvAsset( String  line,  List<AssetData> assets) {
		CsvData csvLine = new CsvData();
		
		String newLine = "";
		String assetInfo = "";
		String assetID = "";
		String serNum = "";
		String equipPct = "";
		String miscAmt = "";
		String pptUplift = "";
		String rentalAmt = "";
		String result = "";
		String sep = ",";	
	 
		//System.out.println("PN=" + items[8] + "-----" + assetInfo.toString());
		String[] items = line.split(",");
		assetInfo = getAssetInfo(items[8], assets);
		String[] item1 = assetInfo.split(":");
		assetID = item1[0];
		serNum = item1[1];
		rentalAmt = item1[2];
		pptUplift = item1[3]; 
		items[7] = serNum;
		items[15] = rentalAmt;
		items[17] = pptUplift;
		 
	 
		for (int i = 0; i < items.length; i++) {
			newLine += items[i] + sep;		 
		}
		//newLine += assetID;
		 return newLine;
	}
	/****************************************************************************************************************************************************/
	public static ArrayList<String> getCsvData(List<AssetData> assets) throws IOException {
		FileReader fr = null;
		String newLine = "";
		String[] newItems;
		String s = new String();
		StringBuffer sb = new StringBuffer();
		ArrayList<String> strArr = new ArrayList<String>();
		fr = new FileReader(new File(csvFile));
		CsvData csvAsset = new CsvData();
		// be sure to not have line starting with "--" or "/*" or any other non
		// alphabetical character
		BufferedReader br = new BufferedReader(fr);
		int k = 0;

		while ((s = br.readLine()) != null) {
			if (s.contains("Invoice")) {
				strArr.add(k++, s);
				continue;
			}
			sb.append(s);
			newLine = loadCsvAsset(s, assets); // load data to Array of CsvData classes

			// newLine = loadCsvAsset(s, assets);
			//System.out.println(newLine);
			strArr.add(k++, newLine);
			/*
			 * for (int i = 0; i < newItems.length; i++) {
			 * System.out.println("***^^^*** newCSV=" + newItems[i] ); }
			 */
	 
		}
		br.close();

		// System.out.println("***^^^*** CSV=" + sb.toString());
		return strArr;
	}
	/****************************************************************************************************************************************************/
	/****************************************************************************************************************************************************/
	
	
	/****************************************************************************************************************************************************/

	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// AssetData assetList = new AssetData();
		// List<AssetData> assetList = new ArrayList<AssetData>();
		// List<CsvData> csvList = new ArrayList<CsvData>();
		// List<CsvData> csvAssets = new ArrayList<CsvData>();
		List<AssetData> assets = new ArrayList<AssetData>();
		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<String> csvArr = new ArrayList<String>();
		// ArrayList<String> newCsvArr = new ArrayList<String>();
		// String sheetNameValue = null;
		// String reportNameValue = null;
		// String headerFile = null;

		final Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		//System.out.println("Date=" + dateStamp);
		strArr = getDbData();
		// Olyutil.printStrArray(strArr);
		assets = setArrData(strArr);
		//System.out.println("assetList SIZE:" + assets.size());
		/*
		 * for(int i = 0 ; i < assets.size() ; i++){ System.out.println("AssetID:" +
		 * assets.get(i).getId() ); System.out.println("Rate:" +
		 * assets.get(i).getEquipPercent() ); }
		 */
		csvArr = getCsvData(assets);
		//Olyutil.printStrArray(csvArr);
		
		for (String str : csvArr) { // iterating ArrayList
			System.out.println(str);
		}
		

		/*
		 * for(int i = 0 ; i < csvAssets.size() ; i++) { //System.out.println("CSV MFG:"
		 * + csvList.get(i).getManufacturer()); //System.out.println("CSV PartNum:" +
		 * csvList.get(i).getPartNumber()); System.out.println("CSV SN:" +
		 * csvAssets.get(i).getSerialNumber()); System.out.println("CSV Uplift:" +
		 * csvAssets.get(i).getPptUplift()); System.out.println("isAssetSet:" +
		 * assets.get(i).isAssetSet() ); }
		 * 
		 */
	}
}
