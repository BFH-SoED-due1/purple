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

import ch.bfh.ti.soed.hs16.srs.purple.controller.LoginController;

public class LoginControllerTest{

	@Test
	public void testCheckIfUserExists(){
		LoginController controller = new LoginController();
		assertTrue(controller.loginWithUser("schie5", "schie5"));
		assertFalse(controller.loginWithUser("Doesnt exist", null));
		assertFalse(controller.loginWithUser(null, "Doesnt exist"));
		assertFalse(controller.loginWithUser("", "Doesnt exist"));
		assertFalse(controller.loginWithUser("Doesnt exist", ""));
		assertFalse(controller.loginWithUser("Doesnt exist", "Doesnt exist"));
		assertFalse(controller.loginWithUser(null, null));
		// Wrong password
		assertFalse(controller.loginWithUser("schie5", "Doesnt exist"));
	}
	
	@Test
	public void testIfUserIsLoggedIn(){
		LoginController controller = new LoginController();
		assertFalse(controller.isUserLoggedInOnSession());
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testSessionAttribute(){
		LoginController controller = new LoginController();
		assertTrue(controller.getUSER_SESSION_ATTRIBUTE().equals("user"));
	}
}
