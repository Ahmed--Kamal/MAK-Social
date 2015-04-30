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
<title>Profile</title>
</head>
<body>
	<form action="/social/writePost" method="post">
		<div align="center">
		<h3>Write a post</h3>
		<textarea rows="2" cols="40" name="post" placeholder="Write post"></textarea><br>
		<select name="privacy" > 
			<option value="public" >public</option>
			<option value="friend">friend</option>
			<option value="private">private</option>
		</select>
		<input type="text" name="feeling" placeholder="Feeling"><br><br>
		<input type="submit"  value="Post">
		
		</div>
	</form>
	<%
		PostEntity post = new PostEntity();
		List<String> posts = post.getPosts();
		UserEntity user = UserEntity.currentUser;
		String name = user.getName();
		for(int i=posts.size()-4;i>=0;i-=4){
		if(posts.get(i+1).length() > 0)
		{
			posts.set(i,"Feeling: "+posts.get(i+1)+'\n'+posts.get(i));
		}
		String s = String.format("<div align='left'><form method='GET'><textarea >"+posts.get(i)+"</textarea> <a href='http://localhost:8888/rest/increaseLikesService/?id="+posts.get(i+3)+"'><br><b>like </b></a> ("+posts.get(i+2)+") likes <br><a href='http://localhost:8888/rest/sharePostService/?id2="+posts.get(i+3)+"'><b>Share</b></a></form></div>");
		out.println(s);
		}
		
	 %>
</body>
</html>