/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.controller;

import java.util.List;

import com.vaadin.server.VaadinSession;

import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_User;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;

public class LoginController {

	// static
	public static String USER_SESSION_ATTRIBUTE = "user";

	// membervariables
	private User user;
	private DBController dbController;
	
	public LoginController(){
		this.dbController = DBController.getInstance();
		System.out.println("Test");
	}

	/**
	 * Function logs an user in with the given username and password
	 * 
	 * @param username
	 * @param password
	 * 
	 * @return true if user logged in, false if user was not found with the
	 *         username and password
	 */
	public boolean loginWithUser(String username, String password) {
		if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
			if (checkIfUserExists(username) && checkPasswordForUsername(password)) {
				VaadinSession.getCurrent().setAttribute(USER_SESSION_ATTRIBUTE, username);
				return true;
			}
		}
		return false;
	}

	/**
	 * Function checks if the user exists in the Database. If the user exists
	 * the membervariable 'user' is set with this user.
	 * 
	 * @param username
	 * @return true if user exists, false if he doesn't exist
	 */
	private boolean checkIfUserExists(String username) {

		List<User> users = this.dbController.selectUserBy(Table_User.COLUMN_USERNAME, username);

		if (users != null && !users.isEmpty()) {
			this.user = users.get(0);
			return true;
		}
		return false;
	}

	/**
	 * Function checks if the entered password is valid for the user
	 * 
	 * @param password
	 * @return true if password is valid, false if the password is false for the
	 *         user
	 */
	private boolean checkPasswordForUsername(String password) {
		// TODO: password to hash
		if (this.user != null && password.equals(this.user.getPassword())) {
			return true;
		}
		return false;
	}

	/**
	 * Function checks if a user is logged in in the current vaadin session
	 * 
	 * @return
	 */
	public boolean isUserLoggedInOnSession() {
		if (VaadinSession.getCurrent().getAttribute(USER_SESSION_ATTRIBUTE) != null) {
			return true;
		}
		return false;
	}

	/**
	 * Function removes the logged in user from the session.
	 */
	public void logout() {
		VaadinSession.getCurrent().setAttribute(USER_SESSION_ATTRIBUTE, null);
	}
}
