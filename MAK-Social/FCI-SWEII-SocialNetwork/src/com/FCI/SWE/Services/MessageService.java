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

import com.FCI.SWE.Models.MessageEntity;
import com.FCI.SWE.Models.UserEntity;
import com.google.appengine.api.datastore.Transaction;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class MessageService {
	/**
	 * this service will be called to send sender message to receiver and save it in the datastore
	 * @param receiverMail holds the receiver mail
	 * @param senderMessage holds the sender mail who sent the message
	 * @return object that holds data in json format
	 */
	@POST
	@Path("/sendMessageService")
	public String sendMessage(@FormParam("receiverMail")String receiverMail, @FormParam("senderMessage")String senderMessage)
	{
		JSONObject object = new JSONObject();
		if (UserEntity.currentUser == null) 
			object.put("Status", "Failed");
		else
		{
			MessageEntity msgEntity = new MessageEntity(senderMessage, receiverMail);
			msgEntity.saveAMessage();
			String messages = msgEntity.getSenderMessage();
			object.put("Status", "OK");
			object.put("Chat", messages);
		}
		return object.toString();
	}
}
