/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ch.bfh.ti.soed.hs16.srs.purple.model.User;
import ch.bfh.ti.soed.hs16.srs.purple.model.User.UserRole;

public class UserTest {

	@Test
	public void testSetNewUser(){
		User user = new User("lastName", "firstName", "mail", "username", "password", User.UserRole.USER_ROLE_ADMIN);

		assertNotNull(user.getUserID());
		// TODO: Swap the arguments in all assertEquals methods:
		// assertEquals(user.getLastName(), "lastName");
		// assertEquals(user.getFirstName(), "firstName");
		// assertEquals(user.getEmailAddress(), "mail");
		// assertEquals(user.getUsername(), "username");
		// assertEquals(user.getPassword(), "password");
		// assertEquals(user.getUserRole(), User.UserRole.USER_ROLE_ADMIN);
	}

	@Test
	public void testChangePassword(){
		User user = new User("lastName", "firstName", "mail", "username", "password", User.UserRole.USER_ROLE_ADMIN);
		user.setPassword("newPassword");

		assertEquals(user.getPassword(), "newPassword");
	}

	@Test
	public void testGetterAndSetter()
	{
		User u = new User();
		u.setEmailAddress("test@test.ch");
		u.setFirstName("Max");
		u.setLastName("Mustermann");
		u.setPassword("sehrgeheim!!!");
		u.setUserID(53);
		u.setUsername("meinUsername");
		u.setUserRole(UserRole.USER_ROLE_ADMIN);
		assertEquals(u.getEmailAddress(), "test@test.ch");
		assertEquals(u.getFirstName(), "Max");
		assertEquals(u.getLastName(), "Mustermann");
		assertEquals(u.getPassword(), "sehrgeheim!!!");
		assertEquals(u.getUserID(), 53);
		assertEquals(u.getUsername(), "meinUsername");
		assertEquals(u.getUserRole(), UserRole.USER_ROLE_ADMIN);
	}

	@Test
	public void testDifferentRoles()
	{
		User u = new User();
		u.setUserRole(UserRole.USER_ROLE_HOST);
		assertEquals(u.getUserRole(), UserRole.USER_ROLE_HOST);
		u.setUserRole(UserRole.USER_ROLE_PARTICIPANT);
		assertEquals(u.getUserRole(), UserRole.USER_ROLE_PARTICIPANT);
		assertNotNull(UserRole.values());
		assertNotNull(UserRole.valueOf("USER_ROLE_ADMIN"));
	}
}
