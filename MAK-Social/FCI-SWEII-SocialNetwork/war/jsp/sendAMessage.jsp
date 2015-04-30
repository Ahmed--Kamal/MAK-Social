
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.FCI.SWE.Services.*"%>
<%@page import="com.FCI.SWE.Models.*"%>
<%@ page import="java.util.*"%>
<%@ page
	import="java.net.URI , 
javax.ws.rs.client.Client , 
javax.ws.rs.client.ClientBuilder , 
javax.ws.rs.client.WebTarget , 
javax.ws.rs.core.MediaType , 
javax.ws.rs.core.Response ,
javax.ws.rs.core.UriBuilder , 
org.glassfish.jersey.client.ClientConfig ,
org.json.simple.JSONObject,
com.FCI.SWE.Controller.Connection,
org.json.simple.parser.*"%>
<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>
<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1256">
<title>Message</title>
</head>
<body>
<%
	/*MessageEntity msg = new MessageEntity("OK\nLOL", "Ahmed2");
	String x = msg.getSenderMessage();
	out.println(x);*/
 %>
<form action="/social/sendMessage" method="post">
	<div align="center">
		Send to: <input type="text" name="receiverMail" placeholder="Enter reciever mail"  value="${it.receiver}"/><br>
		<textarea rows="5" cols="30" name="senderMessage" placeholder="Enter Message here"></textarea><br>
		<input type="submit" value="Send my message">
  	</div>
</form>
<div align="center" style="color:#C80000"><h4>${it.sent}</h4></div>
<div align="center" style="color:#F00000"><h3>OLD Chat</h3></div>
<div align="center">
	<textarea name="oldChat" rows="7" cols="30">"${it.Messages}"</textarea>
</div>
</body>
</html>