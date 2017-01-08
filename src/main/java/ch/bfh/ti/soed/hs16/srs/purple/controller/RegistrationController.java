/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.controller;

import java.util.List;

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
	 *            - The view of the registration
	 */
	public RegistrationController(RegistrationView registrationView) {
		this.dbController = DBController.getInstance();
		this.registrationView = registrationView;
	}

	/**
	 * Registers an user in the database.
	 *
	 * @param user
	 *            - The user to register
	 */
	public void registerNewUser(User user) {
		// TODO: bind this method to a "register"-event
		this.dbController.insertNewUser(user.getFirstName(), user.getLastName(), user.getEmailAddress(),
				user.getUsername(), user.getPassword(), user.getFunction(), user.getRole());
	}

	/**
	 * Function returns all functions for the registration view.
	 *
	 * @return all functions for the registration view.
	 */
	public List<Function> getAllFunctions() {
		return this.dbController.selectAllFunctions();
	}

	/**
	 * Function updates an user
	 *
	 * @param user
	 *            user that will be updated
	 */
	public void updateUser(User user) {
		// this.dbController.
	}
}
