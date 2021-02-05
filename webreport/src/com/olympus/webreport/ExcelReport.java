package com.olympus.webreport;
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
import com.olympus.util.JButils;
import org.w3c.dom.NodeList;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/ExcelReport")
public class ExcelReport extends HttpServlet {

/********************************************************************************************************/

	
/********************************************************************************************************/
	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
		
		String sheetNameValue = null;
		String reportNameValue = null;
		String headerFile = null;
		ArrayList<String> strArr = new ArrayList<String>(); 
		
		JButils jutil = new JButils();
		final Date date = JButils.getCurrentDate();
		String dateStamp = date.toString();
		//System.out.println("Date=" + dateStamp);
		//String headerName = "headerName";
		//String headerNameValue = req.getParameter(headerName);
		//req.getSession().setAttribute(headerName, headerNameValue);
		String reportName = "reportName";
		reportNameValue = req.getParameter(reportName);
		//req.getSession().setAttribute(reportName, reportNameValue);
		String sheetName = "sheetName";
		 sheetNameValue = req.getParameter(sheetName);
		//req.getSession().setAttribute(sheetName, sheetNameValue);
		String hdr = "hdr";
		String hdrValue = req.getParameter(hdr);
	 	 
		//String data = "strArr";
		//String strTok = req.getParameter(data);
		
	 
	 
		
		if (sheetNameValue == null || sheetNameValue.isEmpty() ) {
			sheetNameValue = "Sheet1";
		}
		if (reportNameValue == null || reportNameValue.isEmpty() ) {
			reportNameValue = "excelReport";
		}
		
		if (hdrValue.equalsIgnoreCase("EVG")) {
			headerFile = "EvergreenHeader.txt";
			
		}
		String headerFilename = "C:\\Java_Dev\\props\\headers\\" +  headerFile ;
		String FILE_NAME = reportNameValue + "_" + dateStamp  + ".xlsx";
		
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
	 
		String sep = null;
		String contentType = "application/ms-excel";
		
	
		
		ArrayList<String> headerArr = new ArrayList<String>();
		headerArr = jutil.readInputFile(headerFilename);
		//System.out.println("ExcelReport: ^^^^^^^ strTok=" + strTok);
		System.out.println("ExcelReport: ^^^^^^^ HDR=" + hdrValue);
		System.out.println("ExcelReport:^^^^^^^ HeaderArr=" + headerArr.toString());
		System.out.println("ExcelReport:^^^^^^^ sheetName=" + sheetNameValue);
		System.out.println("ExcelReport:^^^^^^^ reportName=" + reportNameValue);
		
		// Create Excel file on client
				WriteExcel writeExcel = new WriteExcel();
				workbook = writeExcel.newWorkbook();
				sheet = writeExcel.newWorkSheet(workbook, sheetNameValue);
				writeExcel.loadHeader(workbook, sheet, headerArr);
				//System.out.println("** Call loadWorkSheet");
				//    writeExcel.loadWorkSheet(workbook, sheet, strArr, 1);
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
	
/********************************************************************************************************/
	
	@Override
	public void init() throws ServletException {
		System.out.println("Servlet " + this.getServletName() + " has started");
	}
/********************************************************************************************************/

	@Override
	public void destroy() {
		System.out.println("Servlet " + this.getServletName() + " has stopped");
	}
} // End Class
	

