/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController;
import ch.bfh.ti.soed.hs16.srs.purple.controller.RegistrationController;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;
import ch.bfh.ti.soed.hs16.srs.purple.model.User.UserRole;
import ch.bfh.ti.soed.hs16.srs.purple.view.RegistrationView;

public class RegistrationControllerTest
{

	@Test
	public void RCTest()
	{
		DBController dbController = new DBController();
		RegistrationView registrationView = new RegistrationView();
		
		RegistrationController rc = new RegistrationController(registrationView,dbController);
		assertNotNull(rc);
	}
	
	@Test
	public void testRegistration(){
		String username = "username";
		String password = "password";
		String lastname = "lastname";
		String firstname = "firstname";
		String emailAddress = "email";
		UserRole role = UserRole.USER_ROLE_ADMIN;
		
		DBController dbController = new DBController();
		RegistrationView registrationView = new RegistrationView();
		
		RegistrationController rc = new RegistrationController(registrationView,dbController);
		
		User registeredUser = rc.registerNewUser(lastname, firstname, emailAddress, username, password, role);
		
		assertNotNull(registeredUser);
		assertEquals(registeredUser.getFirstName(),firstname);
		assertEquals(registeredUser.getLastName(),lastname);
		assertEquals(registeredUser.getUsername(),username);
		assertEquals(registeredUser.getPassword(),password);
		assertEquals(registeredUser.getUserRole(),role);
		assertEquals(registeredUser.getEmailAddress(),emailAddress);
	}

}
