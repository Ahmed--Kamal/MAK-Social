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

import javax.mail.Session;
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
import com.google.apphosting.utils.config.ClientDeployYamlMaker.Request;

/**
 * This class contains REST services, also contains action function for web
 * application
 * 
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 *
 */
@Path("/")
@Produces("text/html")
public class UserController {
	/**
	 * Action function to render Signup page, this function will be executed
	 * using url like this /rest/signup
	 * 
	 * @return sign up page
	 */
	@GET
	@Path("/signup")
	public Response signUp() {
		return Response.ok(new Viewable("/jsp/register")).build();
	}

	/**
	 * Action function to render home page of application, home page contains
	 * only signup and login buttons
	 * 
	 * @return enty point page (Home page of this application)
	 */
	@GET
	@Path("/")
	public Response index() {
		return Response.ok(new Viewable("/jsp/entryPoint")).build();
	}

	/**
	 * Action function to render login page this function will be executed using
	 * url like this /rest/login
	 * 
	 * @return login page
	 */
	@GET
	@Path("/login")
	public Response login() {
		return Response.ok(new Viewable("/jsp/login")).build();
	}

	/**
	 * Action function to response to signup request, This function will act as
	 * a controller part and it will calls RegistrationService to make
	 * registration
	 * 
	 * @param uname
	 *            provided user name
	 * @param email
	 *            provided user email
	 * @param pass
	 *            provided user password
	 * @return Status string
	 */
	@POST
	@Path("/response")
	public Response response(@FormParam("uname") String uname,
			@FormParam("email") String email, @FormParam("password") String pass) {
		//"http://fci-swe-apps.appspot.com/rest/RegistrationService"
		String serviceUrl = "http://localhost:8888/rest/RegistrationService";
		String urlParameters = "uname=" + uname + "&email=" + email
				+ "&password=" + pass;
		String retJson = Connection.connect(serviceUrl, urlParameters, 
				"POST", "application/x-www-form-urlencoded;charset=UTF-8");
		try 
		{			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("OK")){
				//return "Registered Successfully";
				String success = "Registered Successfully, now login";
				return Response.ok(new Viewable("/jsp/login", success)).build();
			}
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
		/*
		 * UserEntity user = new UserEntity(uname, email, pass);
		 * user.saveUser(); return uname;
		 */
		String failed = "Registration failed try again!";
		return Response.ok(new Viewable("/jsp/register", failed)).build();
	}

	/**
	 * Action function to response to login request. This function will act as a
	 * controller part, it will calls login service to check user data and get
	 * user from datastore
	 * 
	 * @param uname
	 *            provided user name
	 * @param pass
	 *            provided user password
	 * @return Home page view
	 */
	@POST
	@Path("/home")
	@Produces("text/html")
	public Response home(@FormParam("uname") String uname,
			@FormParam("password") String pass) {
		String serviceUrl = "http://localhost:8888/rest/LoginService";
		String urlParameters = "uname=" + uname + "&password=" + pass;
		String retJson = Connection.connect(serviceUrl, 
				urlParameters, "POST", "application/x-www-form-urlencoded;charset=UTF-8");
		
		try{
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return null;
			Map<String, String> map = new HashMap<String, String>();
			UserEntity user = UserEntity.getUser(object.toJSONString());
			map.put("name", user.getName());
			map.put("email", user.getEmail());
			return Response.ok(new Viewable("/jsp/home", map)).build();
		}
		 catch (ParseException e) {
			e.printStackTrace();
		}
		/*
		 * UserEntity user = new UserEntity(uname, email, pass);
		 * user.saveUser(); return uname;
		 */
		return null;

	}
	
	@GET
	@Path("/showFriendRequests")
	public Response showFriendRequest() {
		return Response.ok(new Viewable("/jsp/showFriendRequests")).build();
	}
	
	@GET
	@Path("/LogOut")
	public Response logOut()
	{
		String logOutServiceUrl = "http://localhost:8888/rest/LogOutService";
		String retMyJson = Connection.connect(
				logOutServiceUrl, "", "POST", "application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser jParser = new JSONParser();
		JSONObject object;
		try {
			object = (JSONObject) jParser.parse(retMyJson);
			if(object.get("Status").equals("OK"))
				return Response.ok(new Viewable("/jsp/entryPoint")).build();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@POST
	@Path("/sendFriendRequest")
	public Response sendFriendRequest(@FormParam("recieverMail") String recieverMail)
	{
		String serviceUrl = "http://localhost:8888/rest/SendFriendRequestService";
		String urlParameters = "recieverMail=" + recieverMail;
		String retMyJson = Connection.connect(serviceUrl, urlParameters, 
				"POST", "application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser jParser = new JSONParser();
		Object object;
		try
		{
			object = jParser.parse(retMyJson);
			JSONObject obj = (JSONObject) object;
			if(obj.get("Status").equals("Failed"))
				return null;
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", UserEntity.currentUser.getName());
			map.put("email", UserEntity.currentUser.getEmail());
			return Response.ok(new Viewable("/jsp/home", map)).build();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@POST
	@Path("/acceptFriendRequest")
	public Response acceptFriendRequest(@FormParam("friendRequestMail") String friendRequestMail)
	{
		String urlParameters = "friendRequestMail=" + friendRequestMail;
		String serviceUrl = "http://localhost:8888/rest/acceptFriendRequestService";
		String retJson = Connection.connect("http://localhost:8888/rest/acceptFriendRequestService", urlParameters,
				"POST", "application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser parser = new JSONParser();
        Object obj;
        try {
        	obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return Response.ok(new Viewable("/jsp/showFriendRequests", "Failed")).build();
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", UserEntity.currentUser.getName());
			map.put("email", UserEntity.currentUser.getEmail());
			return Response.ok(new Viewable("/jsp/home", map)).build();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
	}
	
	@POST
	@Path("/searchTimeline")
	public Response searchTimeline(@FormParam("userMail") String userMail)
	{
		String urlParameters = "userMail=" + userMail;
		String serviceUrl = "http://localhost:8888/rest/searchTimelineService";
		String retJson = Connection.connect("http://localhost:8888/rest/searchTimelineService", urlParameters,
				"POST", "application/x-www-form-urlencoded;charset=UTF-8");
		JSONParser parser = new JSONParser();
        Object obj;
        try {
        	obj = parser.parse(retJson);
			JSONObject object = (JSONObject) obj;
			if (object.get("Status").equals("Failed"))
				return Response.ok(new Viewable("/jsp/home")).build();
			return Response.ok(new Viewable("/jsp/userTimeline", object.get("posts"))).build();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
		return null;
	}
}