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
public class PostEntity {
	private String postContent, privacy, feeling;
	private int like;
	public PostEntity()
	{
		
	}
	public PostEntity(String postContent, String privacy, String feeling)
	{
		this.postContent = postContent;
		this.feeling = feeling;
		this.privacy = privacy;
		this.like = 0;
	}
	public String getPost()
	{
		return this.postContent;
	}
	public String getPrivacy()
	{
		return this.privacy;
	}
	public String getFeeling()
	{
		return this.feeling;
	}
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
