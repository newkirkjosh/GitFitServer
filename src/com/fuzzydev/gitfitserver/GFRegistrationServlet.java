package com.fuzzydev.gitfitserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String jsonData = req.getParameter("userData");
		Gson gson = new Gson();
		User user = gson.fromJson(jsonData, User.class);

		Entity userEntity = new Entity(user.getEmail(),"user");
		mapUserPropertiesToEntityFromUser(userEntity,user);
		
		int validationCheck = validateUserRegistration(user);
		if(validationCheck == VALID_REGISTRATION){
			datastore = DatastoreServiceFactory.getDatastoreService();
			datastore.put(userEntity);
		}
		resp.addIntHeader("validation", validationCheck);
	}

	private int validateUserRegistration(User user){
		Query query = new Query("user");
	    List<Entity> existingUsers  = new ArrayList<Entity>();
	    existingUsers = datastore.prepare(query).asList(null);
	    
	    if(!existingUsers.isEmpty()){
	    	for(Entity temp : existingUsers){
	    		if(user.getID() == temp.getProperty("id")){
	    			return USER_ID_ALREADY_EXISTS;    		}
	    		if(user.getEmail() == temp.getProperty("email")){
	    			return USER_EMAIL_ALREADY_REGISTERED;
	    		}
	    	}
	    }
		return VALID_REGISTRATION;
	}
	private void mapUserPropertiesToEntityFromUser(Entity userEntity, User user) {
		userEntity.setProperty("firstName",
				user.getFirstName() != null ? user.getFirstName() : "");
		userEntity.setProperty("lastName",
				user.getLastName() != null ? user.getLastName() : "");
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
		private String email;

		User() {// No-Arg-Constructor

		}

		public User(String firstName) {
			this.firstName = firstName;
		}

		public String getID() {
			return ID;
		}

		public void setID(String iD) {
			ID = iD;
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

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	}
}
