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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.PathParam;

import org.glassfish.jersey.server.mvc.Viewable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.FCI.SWE.Models.MessageEntity;
import com.FCI.SWE.Models.PostEntity;
import com.FCI.SWE.Models.UserEntity;
import com.google.appengine.api.datastore.Transaction;

@Path("/")
@Produces("text/html")
public class PostService {
	
	@POST
	@Path("/writePostService")
	public String writePost(@FormParam("post")String post, @FormParam("privacy")String privacy,
			@FormParam("feeling")String feeling)
	{
		JSONObject object = new JSONObject();
		if (UserEntity.currentUser == null)
			object.put("Status", "Failed");
		else
		{
			PostEntity myPost = new PostEntity(post, privacy, feeling);
			myPost.writePost();
			object.put("Status", "OK");
		}
		return object.toString();
	}
	
	@GET
	@Path("/increaseLikesService")
	public Response increaseLikes(@QueryParam("id") String key)
	{
		PostEntity post = new PostEntity();
		post.increaseLikse(key);
		return Response.ok(new Viewable("/jsp/profile")).build();
	}
	
	@GET
	@Path("/increaseUserLikesService")
	public Response increaseUserLikes(@QueryParam("id") String key)
	{
		PostEntity post = new PostEntity();
		post.increaseLikse(key);
		return Response.ok(new Viewable("/jsp/userTimeline")).build();
	}
	
	@GET
	@Path("/sharePostService")
	public Response sharePost(@QueryParam("id2") String key)
	{
		PostEntity post = new PostEntity();
		post.sharePost(key);
		return Response.ok(new Viewable("/jsp/profile")).build();
	}
}
