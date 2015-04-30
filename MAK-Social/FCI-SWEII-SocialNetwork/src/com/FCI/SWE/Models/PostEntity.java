package com.FCI.SWE.Models;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
/**
 * 
 * @author Ahmed
 * this class handles user posts and its like and share 
 */
public class PostEntity {
	private String postContent, privacy, feeling;
	private int like;
	/**
	 * Constructor
	 */
	public PostEntity()
	{
	}
	/**
	 * Constructor that initialize the postContent, privacy and feeling
	 * @param postContent that has the user post
	 * @param privacy has the post privacy
	 * @param feeling that has the user feeling
	 */
	public PostEntity(String postContent, String privacy, String feeling)
	{
		this.postContent = postContent;
		this.feeling = feeling;
		this.privacy = privacy;
		this.like = 0;
	}
	/**
	 * Getter
	 * @return user postContent
	 */
	public String getPost()
	{
		return this.postContent;
	}
	/**
	 * Getter
	 * @return post privacy
	 */
	public String getPrivacy()
	{
		return this.privacy;
	}
	/**
	 * Getter
	 * @return post feeling
	 */
	public String getFeeling()
	{
		return this.feeling;
	}
	/**
	 * this method opens userPost table to add the user post
	 * @return true if post is added to userPost table
	 */
	public Boolean writePost()
	{
		UserEntity user = UserEntity.currentUser;
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("userPost");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		try {
			Entity writeMyPost = new Entity("userPost", list.size()+1);
			writeMyPost.setProperty("creator", user.getEmail());
			writeMyPost.setProperty("post", this.postContent);
			writeMyPost.setProperty("privacy", this.privacy);
			writeMyPost.setProperty("feeling", this.feeling);
			writeMyPost.setProperty("like", this.like);
			datastore.put(writeMyPost);
			txn.commit();
		}finally{
			if (txn.isActive()) {
		        txn.rollback();
		    }
		}
		return true;
	}
	/**
	 * this method opens the userPost table and get all current user posts
	 * @return the user posts 
	 */
	public List<String> getPosts()
	{
		List<String> posts = new ArrayList<String>();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		UserEntity user = UserEntity.currentUser;
		try {
			Query gaeQuery = new Query("userPost");
			PreparedQuery pq = datastore.prepare(gaeQuery);
			for (Entity entity : pq.asIterable())
			{
				if ((entity.getProperty("creator").equals(user.getEmail()) ))
				{
					posts.add(entity.getProperty("post").toString());
					posts.add(entity.getProperty("feeling").toString());
					posts.add(entity.getProperty("like").toString());
					posts.add(entity.getKey().toString());
				}
			}
			
			txn.commit();
			return posts;
		}finally{
			if (txn.isActive()) {
		        txn.rollback();
		    }
		}
	}
	/**
	 * this method increases the post likes
	 * @param key the post key
	 * @return true if post likes is increased
	 */
	public Boolean increaseLikse(String key)
	{
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		try {
			Query gaeQuery = new Query("userPost");
			PreparedQuery pq = datastore.prepare(gaeQuery);
			for (Entity entity : pq.asIterable())
			{
				if ((entity.getKey().toString().equals(key)))
				{
					int k = Integer.parseInt(entity.getProperty("like").toString());
					k++;
					entity.setProperty("like", k);
					datastore.put(entity);
					txn.commit();
					break;
				}
			}
		}
		finally{
			if (txn.isActive()) {
		        txn.rollback();
		    }
		}
		return true;
	}
	/**
	 * 
	 * @param key
	 * @return true if post is shared
	 */
	public Boolean sharePost(String key)
	{
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		try {
			Query gaeQuery = new Query("userPost");
			PreparedQuery pq = datastore.prepare(gaeQuery);
			for (Entity entity : pq.asIterable())
			{
				if ((entity.getKey().toString().equals(key)))
				{
					this.postContent = entity.getProperty("post").toString();
					this.feeling = entity.getProperty("feeling").toString();
					this.like = 0;
					this.privacy = "friend";
					writePost();
				}
			}
		}
		finally{
			if (txn.isActive()) {
		        txn.rollback();
		    }
		}
		return true;
	}
}
