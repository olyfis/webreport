package com.olympus.webreport;



import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.*;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.http.client.methods.HttpPost;
 
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.Header;
 
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;
 
import org.json.JSONArray;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetFileDateSF  extends HttpServlet{
	
	public static FileTime getCreationTime(File file) throws IOException {
	    Path p = Paths.get(file.getAbsolutePath());
	    BasicFileAttributes view
	        = Files.getFileAttributeView(p, BasicFileAttributeView.class)
	                    .readAttributes();
	    FileTime fileTime=view.creationTime();
	    //  also available view.lastAccessTine and view.lastModifiedTime
	    return fileTime;
	  }
 
 public static FileTime getModifyTime(File file) throws IOException {
	    Path p = Paths.get(file.getAbsolutePath());
	    BasicFileAttributes view
	        = Files.getFileAttributeView(p, BasicFileAttributeView.class)
	                    .readAttributes();
	    FileTime fileTime=view.lastModifiedTime();
	    //  also available view.lastAccessTine and view.lastModifiedTime
	    return fileTime;
	  }
 
 public static String getFileDate(File file) throws IOException {
	    Path p = Paths.get(file.getAbsolutePath());
	    BasicFileAttributes view
	        = Files.getFileAttributeView(p, BasicFileAttributeView.class)
	                    .readAttributes();
	    FileTime fileTime=view.lastModifiedTime();
	    
	    String fileDate = (String) new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format( getModifyTime(file).toMillis());
	    //  also available view.lastAccessTine and view.lastModifiedTime
	    return fileDate;
	  }
 

 
	public String returnLastModifyDate() throws IOException {
		String date1 = null;
	    
		try { 
		File file = new File("D:/tomcat-9.0.10/apache-tomcat-9.0.10/webapps/fisAssetServlet/salesforce/dashsf.html");
	    date1 = getFileDate(file);
		return date1;	
		
		} catch (NoSuchFileException e) {
			
			return "Date not available.";
		}
	}
	/*****************************************************************************************************/
// Service method
	/*****************************************************************************************************/
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		
		//out.write("Test doGet() \n");
		out.write(returnLastModifyDate() + "\n");
		}
/**********************************************************************************************************/  
	 
	@Override
	public void init() throws ServletException {
		//System.out.println("Servlet " + this.getServletName() + " has started");
	}
/**********************************************************************************************************/  

	@Override
	public void destroy() {
		//System.out.println("Servlet " + this.getServletName() + " has stopped");
	}
}
