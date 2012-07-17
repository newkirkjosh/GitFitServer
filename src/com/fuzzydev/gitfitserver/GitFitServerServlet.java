package com.fuzzydev.gitfitserver;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class GitFitServerServlet extends HttpServlet {
	private static final Logger log = Logger
			.getLogger(GitFitServerServlet.class.getName());

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

		Key userKey = KeyFactory.createKey(user.firstName, "user");
		Entity userEntity = new Entity(userKey);
		mapUserPropertiesToEntity(user, userEntity);
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		//datastore.put(userEntity);
	}

	private void mapUserPropertiesToEntity(User user, Entity userEntity) {
		userEntity.setProperty("firstName",
				user.getFirstName() != null ? user.getFirstName() : "");
		userEntity.setProperty("lastName",
				user.getLastName() != null ? user.getLastName() : "");
		userEntity.setProperty("userType",
				user.getUserType() != null ? user.getUserType() : "");
		userEntity.setProperty("weight",
				user.getWeight() != null ? user.getWeight() : "");
		userEntity.setProperty("height",
				user.getHeight() != null ? user.getHeight() : "");
		userEntity.setProperty("email",
				user.getEmail() != null ? user.getEmail() : "");
		userEntity.setProperty("id", user.getID() != null ? user.getID() : "");
	}

	static class User {

		private String ID;
		private String userType;
		private String firstName;
		private String lastName;
		private String weight;
		private String height;
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

		public String getWeight() {
			return weight;
		}

		public void setWeight(String weight) {
			this.weight = weight;
		}

		public String getHeight() {
			return height;
		}

		public void setHeight(String height) {
			this.height = height;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	}
}
