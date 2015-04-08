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
 * <h1>User Entity class</h1>
 * <p>
 * This class will act as a model for user, it will holds user data
 * </p>
 *
 * @author Mohamed Samir
 * @version 1.0
 * @since 2014-02-12
 */
public class UserEntity {
	private String name;
	private String email;
	private String password;
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

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPass() {
		return password;
	}
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
	public List<String> getFriendRequests()
	{
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
			}
		}
		return emailRequests;
	}
	
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
	
}
