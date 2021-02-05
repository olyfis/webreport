package com.olympus.flash;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.olympus.olyutil.*;

//Run: http://localhost:8181/flash/getflash?repid=100
@WebServlet("/getflash")
public class GetFlashData extends HttpServlet {
	
	/****************************************************************************************************************************************************/

	/****************************************************************************************************************************************************************/
	public static FlashData loadFlashObj(String str) throws IOException {

		String[] splitStrArr  = null;
		FlashData fObj = new FlashData();
		
		//System.out.println("***^^^*** LINE=" + str);
		splitStrArr = Olyutil.splitStr(str, ";");
		fObj.setRep(splitStrArr[0]);
		fObj.setTargetType(splitStrArr[1]);
		double msales = Double.parseDouble(splitStrArr[2]); 
		fObj.setMtdSales(msales);	
		double mtgt = Double.parseDouble(splitStrArr[3]); 
		fObj.setMtdSales(mtgt);
		double mvar = Double.parseDouble(splitStrArr[4]); 
		fObj.setMtdSales(mvar);
		double mach = Double.parseDouble(splitStrArr[5]); 
		fObj.setMtdSales(mach);
		
		double qsales = Double.parseDouble(splitStrArr[6]); 
		fObj.setQtdSales(qsales);
		double qtgt = Double.parseDouble(splitStrArr[7]); 
		fObj.setQtdSales(qtgt);
		double qvar = Double.parseDouble(splitStrArr[8]); 
		fObj.setQtdSales(qvar);
		double qach = Double.parseDouble(splitStrArr[9]); 
		fObj.setQtdSales(qach);
		
		double ysales = Double.parseDouble(splitStrArr[10]); 
		fObj.setYtdSales(ysales);
		double ytgt = Double.parseDouble(splitStrArr[11]); 
		fObj.setYtdSales(ytgt);
		double yvar = Double.parseDouble(splitStrArr[12]); 
		fObj.setYtdSales(yvar);
		double yach = Double.parseDouble(splitStrArr[13]); 
		fObj.setYtdSales(yach);
	
		double ftgt = Double.parseDouble(splitStrArr[14]); 
		fObj.setFyTarget(ftgt);
		double fvar = Double.parseDouble(splitStrArr[15]); 
		fObj.setFyVariance(fvar);
		double fach = Double.parseDouble(splitStrArr[16]); 
		fObj.setFyAchieve(fach);
		

		return (fObj);
	}


	/****************************************************************************************************************************************************************/

	public static ArrayList<String> getFlashData(String csvFile) throws IOException {

		FlashData flashObj = new FlashData();	
		FileReader fr = null;
		String newLine = "";
		String[] newItems;
		String s = new String();
		StringBuffer sb = new StringBuffer();
		ArrayList<String> strArr = new ArrayList<String>();
		fr = new FileReader(new File(csvFile));
		BufferedReader br = new BufferedReader(fr);
		int k = 0;
		while ((s = br.readLine()) != null) {
			FlashData flashDataObj = new FlashData();
			if (s.contains("MTD-Sales") || s.contains("Target")) {
				continue;
			}
			sb.append(s);
			// System.out.println("***^^^*** LINE=" + s);
			strArr.add(k++, s);
		}
		br.close();
		// System.out.println("***^^^*** CSV=" + sb.toString());
		return (strArr);
	}
	/****************************************************************************************************************************************************************/
	public static ArrayList<FlashData> parseStrArray(ArrayList<String> strArr) throws IOException {
		ArrayList<FlashData> data = new ArrayList<FlashData>();
		//LinkedHashMap<String, FlashData> lhm = new LinkedHashMap<>();

		for (String str : strArr) { // iterating ArrayList
			//System.out.println("*********** str=" + str + "---");

			FlashData flashDataObj = new FlashData();
			flashDataObj = loadFlashObj(str); // load data to Array of CsvData
			if (flashDataObj != null) {
				data.add(flashDataObj);
			}
		}
		return (data);
	}	
	/****************************************************************************************************************************************************************/
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//LinkedHashMap  lhmRtn =  null;
		ArrayList<FlashData> flashArr = new ArrayList<FlashData>();
		
		//Map  lhmRtn =  null;
		 
		ArrayList<String> strArr = new ArrayList<String>();
		String csvFile = "C:\\Java_Dev\\props\\flash\\mdy_input.txt";
		PrintWriter out = response.getWriter();
		JSONArray jsonArr = new JSONArray();
		FlashData fObj = null;
		
		strArr = getFlashData(csvFile);
		flashArr = parseStrArray(strArr);
		int fSz = flashArr.size();
		System.out.println("SZ=" + fSz  + "--");
		
		for (int i = 0; i < fSz; i++) {			
			fObj = flashArr.get(i);
			try {
				Olyutil.displayObj(fObj);
				//displayObj(fObj);
				
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/****************************************************************************************************************************************************************/
	/****************************************************************************************************************************************************************/
	/****************************************************************************************************************************************************************/
	/****************************************************************************************************************************************************************/

}
