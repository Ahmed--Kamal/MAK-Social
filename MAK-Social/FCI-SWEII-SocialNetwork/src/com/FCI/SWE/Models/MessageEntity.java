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
public class MessageEntity {
	private String receiver, sender, message;
	private long messageId;
	public String getMessage() {
		return this.message;
	}
	public String getReciever() {
		return this.receiver;
	}
	public String getSender() {
		return this.sender;
	}
	public void setMessageID(long id)
	{
		this.messageId = id;
	}
	public long getMessageID()
	{
		return this.messageId;
	}
	public MessageEntity(String message, String receiver)
	{
		this.message = message;
		this.receiver = receiver;
		sender = UserEntity.currentUser.getEmail();
	}
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
	public String getSenderMessage()
	{
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		try {
			Query gaeQuery = new Query("individualMessage");
			PreparedQuery pq = datastore.prepare(gaeQuery);
			List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
			String messages="";
			for (Entity entity : pq.asIterable())
			{
				if ((entity.getProperty("sender").equals(this.sender) && entity.getProperty("receiver").equals(this.receiver)))
						messages+=entity.getProperty("sender") + ": " + entity.getProperty("messages").toString() + "\n";
				else if((entity.getProperty("sender").equals(this.receiver) && entity.getProperty("reciever").equals(this.sender)))
					messages+=entity.getProperty("sender") + ": " + entity.getProperty("messages").toString() + "\n";
			}
			String retMessages="";
			for (int i = 0; i < messages.length(); ++i) {
				if (messages.charAt(i) == '\n') {
					retMessages += "<br>";
				} else {
					retMessages += messages.charAt(i);
				}
			}
			txn.commit();
			return retMessages;
		}finally{
			if (txn.isActive()) {
		        txn.rollback();
		    }
		}
	}
}
