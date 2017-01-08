/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */

package ch.bfh.ti.soed.hs16.srs.purple.controller;

import java.util.List;

import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_User;
import ch.bfh.ti.soed.hs16.srs.purple.model.Function;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;

public class UserProfileController {

	// membervariables
	private DBController dbController;

	/**
	 * Constructor
	 */
	public UserProfileController() {
		this.dbController = DBController.getInstance();
	}

	/**
	 * Function returns the user for the UserProfileView
	 * 
	 * @param username
	 *            current logged in username
	 * @return User-Object
	 */
	public User getUserForView(String username) {
		return this.dbController.selectUserBy(Table_User.COLUMN_USERNAME, username).get(0);
	}

	/**
	 * Function updates an user.
	 * 
	 * @param user
	 *            user to update
	 * @return true if successfully - false otherwise
	 */
	public boolean updateUser(User user) {
		return this.dbController.updateUser(user);
	}

	/**
	 * Function returns all functions for the registration view.
	 * 
	 * @return all functions for the registration view.
	 */
	public List<Function> getAllFunctions() {
		return this.dbController.selectAllFunctions();
	}
}
