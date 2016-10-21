package ch.bfh.ti.soed.hs16.srs.purple.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ch.bfh.ti.soed.hs16.srs.purple.model.User;

public class UserTest {

	@Test
	public void testSetNewUser(){
		User user = new User("lastName", "firstName", "mail", "username", "password", User.UserRole.USER_ROLE_ADMIN);
		
		assertNotNull(user.getUserID());
		assertEquals(user.getLastName(), "lastName");
		assertEquals(user.getFirstName(), "firstName");
		assertEquals(user.getEmailAddress(), "mail");
		assertEquals(user.getUsername(), "username");
		assertEquals(user.getPassword(), "password");
		assertEquals(user.getUserRole(), User.UserRole.USER_ROLE_ADMIN);
	}
	
	@Test
	public void testChangePassword(){
		User user = new User("lastName", "firstName", "mail", "username", "password", User.UserRole.USER_ROLE_ADMIN);
		user.setPassword("newPassword");
		
		assertEquals(user.getPassword(), "newPassword");
	}
}
