<%@page import="com.FCI.SWE.Models.HashTagEntity"%>
<%@page import="com.FCI.SWE.Models.PostEntity"%>
<%@page import="com.FCI.SWE.Models.UserEntity"%>
<%@page import="com.FCI.SWE.Services.*"%>
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
com.google.appengine.api.datastore.Key,
com.google.appengine.api.datastore.KeyFactory,
com.FCI.SWE.Controller.Connection,
org.json.simple.parser.*"%>
<%@ page language="java" contentType="text/html; charset=windows-1256"
	pageEncoding="windows-1256"%>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title>Home</title>
</head>

<body>
<div align="center"><a href="/social/profile/"><b>Profile</b></a></div>
<div align="right"><a href="/social/LogOut/"><b>Log Out</b></a></div>
<div align="center" style="color:#C80000"><h3>Welcome: ${it.name}</h3></div>
<%
	UserEntity user = UserEntity.currentUser;
	user.getFriendRequests();
	int num = user.numOfFriendRequest;
%>
<div style="color:#C80000"><a href="/social/showFriendRequests/"><b>Show Friend Requests</b></a> (<%out.print(num);%>) </div> 
<div><a href="/social/sendAMessage/"><b>Send a message</b></a></div>
<form action="/social/sendFriendRequest" method="post">
	<div align="left">
	Send friend request to: <input type="text" name="recieverMail" placeholder="Enter reciever mail" />
	<input type="submit" value="Send my request">
	</div>
</form>
<form action="/social/searchTimeline" method="post" >
	<div align="left">
	Search for a user: <input id="5" type="text" name="userMail" placeholder="Enter user mail" />
	<input type="submit" value="Search" >
	</div>
</form><br>
<%
	HashTagEntity m = new HashTagEntity("asdsd");
		m.addHashTag();
		ArrayList<String> hash = m.getHashTags();
		for(int j=0;j<hash.size();j++){
			out.println(hash.get(j));
			%><br>
		<%}
 %>
</body>
</html>