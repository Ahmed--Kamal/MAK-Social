package com.FCI.SWE.Services;

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
import javax.ws.rs.PathParam;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.google.appengine.api.datastore.Transaction;

import com.FCI.SWE.Models.UserEntity;
import com.google.appengine.api.datastore.Key;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class UserService {
	
	/**
	 * Login Rest Service, this service will be called to make login process
	 * also will check user data and returns new user from datastore
	 * @param uname provided user name
	 * @param pass provided user password
	 * @return user in json format
	 */
	@POST
	@Path("/LoginService")
	public String loginService(@FormParam("uname") String uname,
			@FormParam("password") String pass) 
	{
		JSONObject object = new JSONObject();
		UserEntity user = UserEntity.getUser(uname, pass);
		if (user == null) {
			object.put("Status", "Failed");
		} else {
			UserEntity.currentUser = user;
			object.put("Status", "OK");
			object.put("name", user.getName());
			object.put("email", user.getEmail());
			object.put("password", user.getPass());
		}

		return object.toString();

	}
	
	@POST
	@Path("/LogOutService")
	public String logOutService()
	{
		JSONObject object = new JSONObject();
		UserEntity user = UserEntity.currentUser;
		if(user == null)
			object.put("Status", "Failed");
		else
		{
			object.put("Status", "OK");
			user = null;
		}
		return object.toString();
	}
	
	@POST
	@Path("/SendFriendRequestService")
	public String sendFriendRequest(@FormParam("recieverMail") String recieverMail)
	{
		JSONObject object = new JSONObject();
		UserEntity user = UserEntity.currentUser;
		if(user == null)
			object.put("Status", "Failed");
		else
		{
			object.put("Status", "OK");
			user.saveFriendRequest(recieverMail);
		}
		return object.toString();
	}
	
	@POST
	@Path("/getFriendRequestsService")
	public String getFriendRequest() {
		JSONObject object = new JSONObject();
		UserEntity user = UserEntity.currentUser;
		if (user == null) {
			object.put("Status", "Failed");

		} else {
			object.put("Status", "OK");
			List<String> requests = user.getFriendRequests();
			
			object.put("Size",requests.size());
			for (int i = 0; i < requests.size(); ++i) {
				object.put("friendRequest" + i, requests.get(i));
			}
		}
		return object.toString();
	}
	
	@POST
	@Path("/acceptFriendRequestService")
	public String acceptFriendRequestService(@FormParam("friendRequestMail") String friendRequestMail)
	{
		JSONObject object = new JSONObject();
		UserEntity user = new UserEntity();
		user = user.currentUser;
		if (user == null)
			object.put("Status", "Failed");
		else
		{
			object.put("Status", "OK");
			Key key;
			key = user.addFriend(friendRequestMail);
			user.removeRecord(key);
			if(key == null)
				object.put("state", "failed");
			else
				object.put("state", "ok");
		}
		return object.toString();
	}
	
	@POST
	@Path("/searchTimelineService")
	public String searchTimelineService(@FormParam("userMail") String userMail)
	{
		JSONObject object = new JSONObject();
		UserEntity user = new UserEntity();
		user = user.currentUser;
		if (user == null)
			object.put("Status", "Failed");
		else
		{
			List<String> posts = user.getUserPosts(userMail);
			object.put("Status", "OK");
			object.put("posts", posts);
			object.put("mail", userMail);
		}
		return object.toString();
	}
}