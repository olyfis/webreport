package com.olympus.webreport;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
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
import javax.servlet.http.HttpSession;

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
import java.util.Properties;
import java.util.Set;
import com.olympus.olyutil.Olyutil;
import org.w3c.dom.NodeList;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/rapportcpoexcel")
public class RapportCPOExcel  extends HttpServlet{
	
	/*********************************************************************************************************************************************/

	public static String formatDate(String dateVal ) throws IOException { 
		String dateMyFormat = "";
 
		SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); 
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date dateFromUser = fromUser.parse(dateVal); // Parse it to the exisitng date pattern and return Date type
              dateMyFormat = myFormat.format(dateFromUser); // format it to the date pattern you prefer
            //System.out.println("DF=" + dateMyFormat); // outputs : 2009-05-19
        } catch (Exception e) {
            e.printStackTrace();
        }	
		return(dateMyFormat); 
	}
	/**
	 * @throws IOException *******************************************************************************************************************************************/
	public static void loadWorkSheet(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<String> strArr, int rowNum, String sep) throws IOException {
		String[] strSplitArr = null;

		//System.out.println("************* strArr=" + strArr.toString());
		 
		for (String str : strArr) { // iterating ArrayList
			Row row = sheet.createRow(rowNum++);
			strSplitArr = Olyutil.splitStr(str, sep);
			int colNum = 0;
			for (String token : strSplitArr) {
				Cell cell = row.createCell(colNum);
				if (token instanceof String) {
					if (colNum == 16 || colNum == 17) {
						 
						String fDate = formatDate(token);
						cell.setCellValue((String) fDate);
					} else {
						cell.setCellValue((String) token.replaceAll("null", ""));
					}
					
				}
				colNum++;
			}
		}
	}
	
	
	/*********************************************************************************************************************************************/
	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String headerFilenameBRSummary = "C:\\Java_Dev\\props\\headers\\rapportcpoHdr.txt";
		
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = Olyutil.readInputFile(headerFilenameBRSummary);
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		// System.out.println("Date=" + dateStamp);
		String FILE_NAME = "Rapport_CPO_Report_" + dateStamp + ".xlsx";
		HttpSession session = req.getSession();
		ArrayList<String> strArr = new ArrayList<String>();
		strArr = (ArrayList<String>) session.getAttribute("strArr");
		
		//Olyutil.printStrArray(strArr);
		/*****************************************************************************************************************************************/
		// Create Excel file on client
		
	 
		WriteExcel writeExcel = new WriteExcel();
		workbook = writeExcel.newWorkbook();
		sheet = writeExcel.newWorkSheet(workbook, "Rapport Booking CPO");
		writeExcel.loadHeader(workbook, sheet, headerArr);
		//System.out.println("** Call loadWorkSheet");
		 loadWorkSheet(workbook, sheet, strArr, 1, ";");
		//BufferedInputStream in = null; 
		try {
			// HttpServletResponse response = getResponse(); // get ServletResponse
			res.setContentType("application/vnd.ms-excel"); // Set up mime type
			res.addHeader("Content-Disposition", "attachment; filename=" + FILE_NAME);
			OutputStream out2 = res.getOutputStream();
			workbook.write(out2);
			out2.flush();

		//********************************************************************************************************************************
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
 
	}

}
