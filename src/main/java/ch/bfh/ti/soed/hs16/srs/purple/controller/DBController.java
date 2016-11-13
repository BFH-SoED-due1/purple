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

public class DBController {

	/**
	 * Creates a new user in the database or updates an existing one.
	 *
	 * @param username - The name the user likes to have.
	 * @param password - The password associated with the given username.
	 * @param emailAddress - The email of the user.
	 * @param role - The specific role this user should have.
	 * @param firstname - Firstname
	 * @param lastname - Lastname
	 *
	 * @return User - The user stored in the database
	 * */
	public User createUser(String lastname, String firstname, String emailAddress, String username, String password, UserRole role){
		// TODO: Create database and connect it via jdbc and return new User only if the user is in the database
		return new User(lastname,firstname,emailAddress,username,password,role);
	}

}
