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
<title>Timeline</title>
</head>
<body>

<%
	UserEntity user = UserEntity.currentUser;
	String visited = user.getVisited();
	out.println(visited);
	List<String> posts = user.getUserPosts(visited);
	for(int i=posts.size()-4;i>=0;i-=4){
		if(posts.get(i+1).length() > 0)
		{
			posts.set(i,"Feeling: "+posts.get(i+1)+'\n'+posts.get(i));
		}
		String s = String.format("<div align='left'><form method='GET'><textarea >"+posts.get(i)+"</textarea> <a href='http://localhost:8888/rest/increaseUserLikesService/?id="+posts.get(i+3)+"'><br><b>like </b></a> ("+posts.get(i+2)+") likes <br><a href='http://localhost:8888/rest/sharePostService/?id2="+posts.get(i+3)+"'><b>Share</b></a></form></div>");
		out.println(s);
		}
%>
</body>
</html>