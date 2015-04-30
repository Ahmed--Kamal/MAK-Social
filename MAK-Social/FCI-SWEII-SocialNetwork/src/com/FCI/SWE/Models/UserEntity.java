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
 * this class for handling user data in the datastore
 */
public class UserEntity {
	private String name;
	private String email;
	private String password;
	private String visited;
	public int numOfFriendRequest = 0;
	public static UserEntity currentUser = null;

	/**
	 * Constructor accepts user data
	 * 
	 * @param name
	 *            user name
	 * @param email
	 *            user email
	 * @param password
	 *            user provided password
	 */
	public UserEntity(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
		
	}
	/**
	 * Getter
	 * @return user name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Getter
	 * @return user mail
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * Getter
	 * @return use password
	 */
	public String getPass() {
		return password;
	}
	/**
	 * Constructor 
	 */
	public UserEntity()
	{
		name="";
		email="";
		password="";
	}
	public static UserEntity getCurrentUser()
	{
		return null;
	}
	/**
	 * this method set the visited mail that current user visited
	 * @param visited the mail of visited user by current user
	 */
	public void setVisited(String visited)
	{
		this.visited = visited;
	}
	/**
	 * Getter
	 * @return get the visited mail that current user visited
	 */
	public String getVisited()
	{
		return this.visited;
	}
	/**
	 * 
	 * This static method will form UserEntity class using json format contains
	 * user data
	 * 
	 * @param json
	 *            String in json format contains user data
	 * @return Constructed user entity
	 */
	public static UserEntity getUser(String json) {

		JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(json);
			return new UserEntity(object.get("name").toString(), object.get(
					"email").toString(), object.get("password").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 
	 * This static method will form UserEntity class using user name and
	 * password This method will serach for user in datastore
	 * 
	 * @param name
	 *            user name
	 * @param pass
	 *            user password
	 * @return Constructed user entity
	 */

	public static UserEntity getUser(String name, String pass) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		for (Entity entity : pq.asIterable()) {
			System.out.println(entity.getProperty("name").toString());
			if (entity.getProperty("name").toString().equals(name)
					&& entity.getProperty("password").toString().equals(pass)) {
				UserEntity returnedUser = new UserEntity(entity.getProperty(
						"name").toString(), entity.getProperty("email")
						.toString(), entity.getProperty("password").toString());
				return returnedUser;
			}
		}

		return null;
	}

	/**
	 * This method will be used to save user object in datastore
	 * 
	 * @return boolean if user is saved correctly or not
	 */
	public Boolean saveUser(UserEntity user) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("users");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		if(user.name.equals("") || user.email.equals("") || user.password.equals(""))
			return false;
		try{
			Entity employee = new Entity("users", list.size() + 1);
	
			employee.setProperty("name", user.name);
			employee.setProperty("email", user.email);
			employee.setProperty("password", user.password);
			datastore.put(employee);
		txn.commit();
		}finally{
			if (txn.isActive()) {
		        txn.rollback();
		    }
		}
		return true;
	}
	/**
	 * 
	 * @param recieverMail the mail of the request receiver
	 * @return true if friendRequest is saved
	 */
	public Boolean saveFriendRequest(String recieverMail)
	{
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		try
		{
			
			Entity employee = new Entity("friendRequests");
			employee.setProperty("sender", this.email);
			employee.setProperty("receiver", recieverMail);
			datastore.put(employee);
			txn.commit();
		}
		finally{
			if (txn.isActive()) {
		        txn.rollback();
		    }
		}
		return true;
	}
	/**
	 * This method open friendRequests table and return list of requests mail
	 * @return emailRequests that holds all requests for the current user
	 */
	public List<String> getFriendRequests()
	{
		numOfFriendRequest = 0;
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		UserEntity user = new UserEntity();
		user = user.currentUser;
		Query gaeQuery = new Query("friendRequests");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<String> emailRequests = new ArrayList<String>();
		for(Entity entity : pq.asIterable())
		{
			if (entity.getProperty("receiver").toString().equals(user.email)) {
				emailRequests.add(entity.getProperty("sender").toString());
				numOfFriendRequest++;
			}
		}
		return emailRequests;
	}
	
	/**
	 * this method open friendRequests table and add sender and receiver mail
	 * @param friendRequestMail holds the request sender mail
	 * @return the request key
	 */
	public Key addFriend(String friendRequestMail)
	{
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		boolean found = false;
		Query gaeQuery = new Query("friendRequests");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		Key key = null;
		for (Entity entity : pq.asIterable()) {
			if (entity.getProperty("sender").toString().equals(friendRequestMail)) {
				key = entity.getKey();
				found = true;
			}
		}
		if(found == false)
			return key;
		try
		{
			UserEntity user = new UserEntity();
			user = user.currentUser;
			Entity employee = new Entity("friendsMails");
			employee.setProperty("first", friendRequestMail);
			employee.setProperty("Second", user.email);
			datastore.put(employee);
			txn.commit();
		}
		finally{
			if (txn.isActive()) {
		        txn.rollback();
		    }
		}
		return key;
	}
	/**
	 * this method removes the friend request by its key
	 * @param key holds the friend request key
	 */
	public void removeRecord(Key key) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		
		try {
		
		datastore.delete(key);
		txn.commit();
		}finally{
			if (txn.isActive()) {
		        txn.rollback();
		    }
		}
	}
	/**
	 * this method opens userPost table and return user post by taking its mail
	 * @param mail holds the mail of specific user
	 * @return the posts of the giving mail of user
	 */
	public List<String> getUserPosts(String mail)
	{
		setVisited(mail);
		List<String> posts = new ArrayList<String>();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("userPost");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		try {
			for (Entity entity : pq.asIterable())
			{
				if ((entity.getProperty("creator").equals(mail) && entity.getProperty("privacy").equals("public")))
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
}
