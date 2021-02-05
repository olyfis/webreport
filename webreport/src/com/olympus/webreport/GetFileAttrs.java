package com.olympus.webreport;
 

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;

public class GetFileAttrs {

	public static FileTime getCreationTime(File file) throws IOException {
	    Path p = Paths.get(file.getAbsolutePath());
	    BasicFileAttributes view
	        = Files.getFileAttributeView(p, BasicFileAttributeView.class)
	                    .readAttributes();
	    FileTime fileTime=view.creationTime();
	    //  also available view.lastAccessTime and view.lastModifiedTime
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
	    
	    String fileDate = (String) new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(GetFileAttrs.getModifyTime(file).toMillis());
	    //  also available view.lastAccessTine and view.lastModifiedTime
	    return fileDate;
	  }
 

	  public static void main(String[] args) throws IOException {
	    //File file = new File("c:/temp/config.json");
	    String date1 = null;
	    
	    File file = new File("D:/Kettle/XML/dashboard.html");
	    date1 = GetFileAttrs.getFileDate(file);
	    
	    System.out.println(file + " ***** Last update: " + date1);
	    /*
	    System.out.println(file + " Created on: "
	        + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
	                   .format(GetFileAttrs.getCreationTime(file).toMillis()));
	    
	    System.out.println(file + " Last update: "
		        + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
		                   .format(GetFileAttrs.getModifyTime(file).toMillis()));
	    
	    //String date1 = (String) new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(GetFileAttrs.getModifyTime(file).toMillis());
	    String date1 = (String) new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(GetFileAttrs.getModifyTime(file).toMillis());
	    System.out.println(file + " ***** Last update: " + date1);
	    
	    */
	  }

}
