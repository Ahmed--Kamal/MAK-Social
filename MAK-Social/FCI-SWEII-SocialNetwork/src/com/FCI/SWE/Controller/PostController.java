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
public class PostController {
	
	@POST
	@Path("writePost")
	public Response sendMessage(@FormParam("post")String post, @FormParam("privacy")String privacy,
			@FormParam("feeling")String feeling)
	{
		String serviceUrl = "http://localhost:8888/rest/writePostService";
		String urlParameters = "post=" + post + "&privacy=" + privacy
				+ "&feeling=" + feeling;
		String retJson = Connection.connect(serviceUrl, urlParameters, "POST",
				"application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser jParser = new JSONParser();
		Object object1;
		
		try{
			object1 = jParser.parse(retJson);
			JSONObject object2 = (JSONObject) object1;
			String s="";
			Map<String, String> map = new HashMap<String, String>();
			String posted = "";
			if(object2.get("Status").equals("Failed")){
				posted = "Failed to write the post";
				return Response.ok(new Viewable("/jsp/profile", posted)).build();
			}
			posted = "Post has been posted";
			return Response.ok(new Viewable("/jsp/profile", posted)).build();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	@GET
	@Path("/profile")
	public Response showFriendRequest() {
		if (UserEntity.currentUser == null)
			return Response.ok(new Viewable("/jsp/entryPoint")).build();
		return Response.ok(new Viewable("/jsp/profile")).build();
	}
	
	@POST
	@Path("/increaseLikes")
	public Response increaseLikes()
	{
		System.out.println("looooooooooool");
		return Response.ok(new Viewable("/jsp/profile")).build();
	}
}
