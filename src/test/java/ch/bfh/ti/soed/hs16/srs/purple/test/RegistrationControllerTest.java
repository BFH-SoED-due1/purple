/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController;
import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_User;
import ch.bfh.ti.soed.hs16.srs.purple.controller.RegistrationController;
import ch.bfh.ti.soed.hs16.srs.purple.model.Function;
import ch.bfh.ti.soed.hs16.srs.purple.model.Role;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;
import ch.bfh.ti.soed.hs16.srs.purple.view.RegistrationView;

public class RegistrationControllerTest {

	@Test
	public void RCTest() {
		DBController dbController = DBController.getInstance();

		RegistrationView registrationView = new RegistrationView();

		RegistrationController rc = new RegistrationController(registrationView);
		assertNotNull(rc);
	}

	@Test
	public void testRegisterNewUser() {
		RegistrationView registrationView = new RegistrationView();
		DBController dbCont = DBController.getInstance();
		RegistrationController rc = new RegistrationController(registrationView);

		rc.registerNewUser(new User(46, "Testaep3", "Testaep3", "Testaep3", "Testaep3", "Testaep3", new Role(1, "Test"),
				new Function(45, "TestFunction")));
		assertNotNull(dbCont.selectUserBy(Table_User.COLUMN_FIRSTNAME, "Testaep3"));
		dbCont.deleteUser(dbCont.selectUserBy(Table_User.COLUMN_USERNAME, "Testaep3").get(0).getUserID());
	}

	@Test
	public void testRegistration() {
		String username = "username";
		String password = "password";
		String lastname = "lastname";
		String firstname = "firstname";
		String emailAddress = "email";

		DBController dbController = DBController.getInstance();

		RegistrationView registrationView = new RegistrationView();

		RegistrationController rc = new RegistrationController(registrationView);

		// TODO
		// User registeredUser = rc.registerNewUser(lastname, firstname,
		// emailAddress, username, password, role);

		// assertNotNull(registeredUser);
		// assertEquals(registeredUser.getFirstName(),firstname);
		// assertEquals(registeredUser.getLastName(),lastname);
		// assertEquals(registeredUser.getUsername(),username);
		// assertEquals(registeredUser.getPassword(),password);
		// assertEquals(registeredUser.getEmailAddress(),emailAddress);
	}

}
