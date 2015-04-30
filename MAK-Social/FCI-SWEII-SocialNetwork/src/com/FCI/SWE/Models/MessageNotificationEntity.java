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
public class MessageNotificationEntity {
	public static Boolean saveNewMessageNotification(String sender, String receiver)
	{
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		try{
			Entity notifyMessage = new Entity("MessageNotification");
			notifyMessage.setProperty("sender", sender);
			notifyMessage.setProperty("receiver", receiver);
			datastore.put(notifyMessage);
		}
		finally
		{
			if (txn.isActive())
		        txn.rollback();
		}
		return true;
	}
	public static List<String> getSenders(String receiver)
	{
		List<String> senders = new ArrayList<String>();
		DatastoreService datastore	= DatastoreServiceFactory.getDatastoreService();
		Query gaeQuery = new Query("MessageNotification");
		PreparedQuery pq = datastore.prepare(gaeQuery);
		String sender;
		for(Entity  entity : pq.asIterable())
		{
			if(entity.getProperty("receiver").toString().equals(receiver))
			{
				sender = entity.getProperty("sender").toString();
				if(!senders.contains(sender))
					senders.add(sender);
			}
		}
		return senders;
	}
	public static boolean deleteMessageNotification(Key key){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		try
		{
			datastore.delete(key);
			txn.commit();
		}
		finally
		{
			if (txn.isActive()) {
				txn.rollback();
			}
		}
		return true;
	}
}
