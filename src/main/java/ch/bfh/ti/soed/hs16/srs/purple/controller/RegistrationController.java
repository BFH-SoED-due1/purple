/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.controller;

import ch.bfh.ti.soed.hs16.srs.purple.model.User;
import ch.bfh.ti.soed.hs16.srs.purple.model.User.UserRole;
import ch.bfh.ti.soed.hs16.srs.purple.view.RegistrationView;

public class RegistrationController {
	private RegistrationView registrationView;
	private User user;
	private DBController dbController;

	public RegistrationController(RegistrationView registrationView, DBController dbController){
		this.dbController = dbController;
		this.registrationView = registrationView;
	}

	/**
	 * Registers the user in the database.
	 *
	 * @param username - The name the user likes to have.
	 * @param password - The password associated with the given username.
	 * @param emailAddress - The email of the user.
	 * @param role - The specific role this user should have.
	 * @param firstname - Firstname
	 * @param lastname - Lastname
	 *
	 * @return User - The registered User
	 * */
	public User registerNewUser(String lastname, String firstname, String emailAddress, String username, String password, UserRole role){
		// TODO: bind this method to a "register"-event
		return dbController.createUser(lastname,firstname,emailAddress,username,password,role);
	}

}
