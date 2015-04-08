<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Message</title>
</head>
<body>
<form action="/social/sendMessage" method="post">
	<div align="center">
		Send to: <input type="text" name="receiverMail" placeholder="Enter reciever mail" /><br>
		<textarea rows="5" cols="30" name="senderMessage" placeholder="Enter Message here"></textarea><br>
		<input type="submit" value="Send my message">
  	</div>
</form>
<div align="center" style="color:#C80000"><h4>${it.sent}</h4></div>
<div align="center" style="color:#C80000"><h4>${it.Messages}</h4></div>
</body>
</html>