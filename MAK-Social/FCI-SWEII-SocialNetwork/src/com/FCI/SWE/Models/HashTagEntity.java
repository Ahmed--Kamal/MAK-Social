package com.FCI.SWE.Models;
import java.util.Date;
import java.util.HashMap;
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
import com.sun.org.apache.bcel.internal.generic.SWAP;
public class HashTagEntity {
	private String hashTag;
	private ArrayList<String> hashTags;
	private ArrayList<Integer> trends;
	public HashTagEntity(String hashTag)
	{
		this.hashTag = hashTag;
		hashTags = new ArrayList<String>();
		trends = new ArrayList<Integer>();
	}
	public ArrayList<String> getHashTags()
	{
		return this.hashTags;
	}
	public ArrayList<Integer> getTrends()
	{
		return this.trends;
	}
	public void addHashTag()
	{
		hashTags.clear();
		trends.clear();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Transaction txn = datastore.beginTransaction();
		try {
			Query gaeQuery = new Query("userPost");
			PreparedQuery pq = datastore.prepare(gaeQuery);
			String post="";
			for (Entity entity : pq.asIterable())
			{
				post = entity.getProperty("post").toString();
				String word="";
				for(int i=0;i<post.length();i++)
				{
					word="";
					if(post.charAt(i) == '#')
					{
						word+='#';
						for(int j=i+1;j<post.length();j++)
						{
							if((post.charAt(j) >= 'a' && post.charAt(j) <= 'z') || 
									(post.charAt(j) >= 'A' && post.charAt(j) <= 'Z') ){
								word+=post.charAt(j);
								i++;
							}
							else
								break;
						}
						i--;
						if(hashTags.contains(word))
						{
							int index = 0;
							for(int j=0;j<hashTags.size();j++)
							{
								if(hashTags.get(j).equals(word))
								{
									index = j;
									break;
								}
							}
							int num = trends.get(index);
							num+=1;
							trends.set(index, num);
						}
						else
						{
							hashTags.add(word);
							trends.add(1);
						}
					}
				}
				sortHashTags();
			}
			
			txn.commit();
		}finally{
			if (txn.isActive()) {
		        txn.rollback();
		    }
		}
	}
	public void sortHashTags()
	{
		int size = hashTags.size();
		for(int i=0;i<size;i++)
		{
			for(int j=i+1;j<size;j++)
			{
				if(trends.get(j) > trends.get(i))
				{
					int temp1;
					String temp2;
					temp1 = trends.get(j);
					trends.set(j, trends.get(i));
					trends.set(i, temp1);
					temp2 = hashTags.get(j);
					hashTags.set(j, hashTags.get(i));
					hashTags.set(i, temp2);
				}
			}
		}
	}
}
