<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<dir>
    <b>Upload files here.</b><br/>
    <form name="uploadform" enctype="multipart/form-data" action="./handleUpload.jsp" method="post">
        Email address or other identifying characteristic: <input name="userinfo" type="text"><br/><br/>
        Send these files:<br/>
        <p><input name="userfile[1]" type="file"/></p>
        <p><input name="userfile[2]" type="file"/></p>
        <p><input name="userfile[3]" type="file"/></p>
        <p><input name="userfile[4]" type="file"/></p>
        <p><input name="userfile[5]" type="file"/></p>
        <p>Comments: <input name="comment" type="textarea" width="500" /></p>
        <p><input type="submit" name="sendbutton" value="Send File(s)" onclick="startTimer()"/>
    </form>
    <br><p>
</dir>

</body>
</html>