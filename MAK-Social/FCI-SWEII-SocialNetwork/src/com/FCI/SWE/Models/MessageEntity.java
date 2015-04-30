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
 * this class handles message in the datastore
 */
public class MessageEntity {
	private String receiver, sender, message;
	/**
	 * Getter
	 * @return the required message
	 */
	public String getMessage() {
		return this.message;
	}
	/**
	 * Getter
	 * @return the receiver mail
	 */
	public String getReciever() {
		return this.receiver;
	}
	/**
	 * Getter
	 * @return the sender mail
	 */
	public String getSender() {
		return this.sender;
	}
	/**
	 * Constructor 
	 * @param message has the sender message
	 * @param receiver the receiver mail
	 */
	public MessageEntity(String message, String receiver)
	{
		this.message = message;
		this.receiver = receiver;
		sender = UserEntity.currentUser.getEmail();
	}
	/**
	 * This method saves the message in individualMessage table
	 * @return true if message saved successfully
	 */
	public Boolean saveAMessage()
	{
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		Query gaeQuery = new Query("individualMessage");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
		try {
			Entity senderMessage = new Entity("individualMessage", list.size()+1);
			senderMessage.setProperty("sender", this.sender);
			senderMessage.setProperty("receiver", this.receiver);
			senderMessage.setProperty("message", this.message);
			datastore.put(senderMessage);
			txn.commit();
		}finally{
			if (txn.isActive()) {
		        txn.rollback();
		    }
		}
		return true;
	}
	/**
	 * This method uses sender mail and receiver mail and get all messages between them
	 * @return the sender message
	 */
	public String getSenderMessage()
	{
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		try {
			Query gaeQuery = new Query("individualMessage");
			PreparedQuery pq = datastore.prepare(gaeQuery);
			String messages="";
			for (Entity entity : pq.asIterable())
			{
				if ((entity.getProperty("sender").equals(this.sender) && entity.getProperty("receiver").equals(this.receiver)))
						messages+=entity.getProperty("sender") + ": " + entity.getProperty("message").toString() + "\n";
				else if((entity.getProperty("sender").equals(this.receiver) && entity.getProperty("receiver").equals(this.sender)))
					messages+=entity.getProperty("sender") + ": " + entity.getProperty("message").toString() + "\n";
			}
			txn.commit();
			return messages;
		}finally{
			if (txn.isActive()) {
		        txn.rollback();
		    }
		}
	}
}
