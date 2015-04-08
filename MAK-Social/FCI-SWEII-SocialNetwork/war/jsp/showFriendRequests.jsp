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
<title>Friendship Requests</title>
</head>
<body>
	<%
		String urlParameters = "";

		String retJson = Connection.connect(
					"http://localhost:8888/rest/getFriendRequestsService",
				urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");

		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;

			if (object.get("Status").equals("OK")) {
				Integer i = 0;
				while (object.containsKey("friendRequest" + i.toString())) {
					String s = String
							.format("<div align='center'><form action='/social/acceptFriendRequest' method='POST'>Accept : <input type='hidden' name='friendRequestMail' value=%s><input type='submit' value='%s'></form></div>",
									object.get("friendRequest" + i.toString()),object.get("friendRequest" + i.toString()));
					out.print(s);
					i++;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	%>
</body>
</html>