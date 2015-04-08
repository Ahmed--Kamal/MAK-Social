package com.FCI.SWE.Controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.Models.UserEntity;

@Path("/")
@Produces("text/html")
public class MessageController {
	
	@GET
	@Path("/sendAMessage")
	public Response showFriendRequest() {
		if (UserEntity.currentUser == null)
			return Response.ok(new Viewable("/jsp/entryPoint")).build();
		return Response.ok(new Viewable("/jsp/sendAMessage")).build();
	}
	
	@POST
	@Path("sendMessage")
	public Response sendMessage(@FormParam("receiverMail")String receiverMail, @FormParam("senderMessage")String senderMessage)
	{
		String serviceUrl = "http://localhost:8888/rest/sendMessageService";
		String urlParameters = "receiverMail=" + receiverMail + "&senderMessage=" + senderMessage;
		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser jParser = new JSONParser();
		Object object1;
		try{
			object1 = jParser.parse(retJson);
			JSONObject object2 = (JSONObject) object1;
			String s="";
			Map<String, String> map = new HashMap<String, String>();
			map.put("receiver", receiverMail);
			map.put("Messages", object2.get("Chat").toString());
			if(object2.get("Status").equals("Failed")){
				s = "Failed to send message";
				map.put("sent", s);
				return Response.ok(new Viewable("/jsp/sendAMessage", map)).build();
			}
			else{
				s = "Message has been sent";
				map.put("sent", s);
			}
				return Response.ok(new Viewable("/jsp/sendAMessage", map)).build();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
