/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.controller;

import ch.bfh.ti.soed.hs16.srs.purple.model.Function;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;
import ch.bfh.ti.soed.hs16.srs.purple.view.RegistrationView;

public class RegistrationController {
	
	// membervariables
	private RegistrationView registrationView;
	private User user;
	private DBController dbController;

	/**
	 * Constructor: RegistrationController
	 * 
	 * @param registrationView
	 */
	public RegistrationController(RegistrationView registrationView) {
		this.dbController = DBController.getInstance();
		this.registrationView = registrationView;
	}

	/**
	 * Registers an user in the database.
	 * 
	 * @param user
	 * @param role
	 */
	public void registerNewUser(User user, Function function) {
		// TODO: bind this method to a "register"-event
		dbController.insertNewUser(user.getFirstName(), user.getLastName(), user.getEmailAddress(), user.getUsername(),
				user.getPassword(), null, user.getRole());
	}
}
