package com.fuzzydev.gitfitserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator;

@SuppressWarnings("serial")
public class GFRegistrationServlet extends HttpServlet {
	
	DatastoreService datastore;
	private static final Logger log = Logger.getLogger(GFRegistrationServlet.class.getName());
	private static final int USER_ID_ALREADY_EXISTS = 1;
	private static final int USER_EMAIL_ALREADY_REGISTERED = 2;
	private static final int VALID_REGISTRATION = 0;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("TEST");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		 StringBuffer jsonData = new StringBuffer();
		  String line = null;
		  try {
		    BufferedReader reader = req.getReader();
		    while ((line = reader.readLine()) != null)
		      jsonData.append(line);
		  } catch (Exception e) {
		}
		  
		datastore = DatastoreServiceFactory.getDatastoreService();
  
		log.info(jsonData.toString());
		
		Gson gson = new Gson();

		User newUser = gson.fromJson(jsonData.toString(),User.class);
		Entity userEntity = new Entity("user",newUser.getEmail());
		mapUserPropertiesToEntityFromUser(userEntity,newUser);
		
		int validationCheck = validateUserRegistration(newUser);
		if(validationCheck == VALID_REGISTRATION){
			datastore.put(userEntity);
		}
		resp.addHeader("xValidation", String.valueOf(validationCheck));
	}

	private int validateUserRegistration(User user){
		Query query = new Query("user");
	  
		PreparedQuery pq = datastore.prepare(query);

		for (Entity result : pq.asIterable()) {
			if(user.getID().equalsIgnoreCase(result.getProperty("id").toString())){
				log.info("here");
    			return USER_ID_ALREADY_EXISTS;    		
    		}
    		if(user.getEmail().equalsIgnoreCase(result.getProperty("email").toString())){
    			return USER_EMAIL_ALREADY_REGISTERED;
    		}
		}	   
		return VALID_REGISTRATION;
	}
	private void mapUserPropertiesToEntityFromUser(Entity userEntity, User user) {
		userEntity.setProperty("firstName",
				user.getFirstName() != null ? user.getFirstName() : "");
		userEntity.setProperty("userType",
				user.getUserType() != null ? user.getUserType() : "");
		userEntity.setProperty("email",
				user.getEmail() != null ? user.getEmail() : "");
		userEntity.setProperty("id", user.getID() != null ? user.getID() : "");
	}

	static class User {

		private String ID;
		private String userType;
		private String firstName;
		private String lastName;
		private String password;
		private String email;

		User() {// No-Arg-Constructor

		}

		public String getID() {
			return ID;
		}

		public void setID(String iD) {
			ID = iD;
		}
		
		public void setPassword(String password){
			this.password = password;
		}
		
		public String getPassword(){
			return this.password;
		}

		public String getUserType() {
			return userType;
		}

		public void setUserType(String userType) {
			this.userType = userType;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	}
}