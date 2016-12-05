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

import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController;
import ch.bfh.ti.soed.hs16.srs.purple.controller.ValidationController;

public class ValidationControllerTest {
	
	@Test
	public void testInstance(){
		ValidationController controller = new ValidationController();
		assertNotNull(controller);
	}
	
	@Test
	public void testSetTextFieldRequired(){
		TextField textField = new TextField();
		ValidationController.setTextFieldRequired(textField);
		assertTrue(textField.isRequired());
	}
	
	@Test
	public void setPasswordFieldRequired(){
		PasswordField passwordField = new PasswordField();
		ValidationController.setPasswordFielRequired(passwordField);
		assertTrue(passwordField.isRequired());
	}
	
	@Test
	public void testCheckIfUserAlreadyExists(){
		TextField username = new TextField();
		username.setValue("schie5");
		ValidationController.checkIfUserAlredyExist(username, DBController.getInstance());
		username.setValue("Doesnt exist");
		ValidationController.checkIfUserAlredyExist(username, DBController.getInstance());
	}
	
	@Test
	public void testIfEmail(){
		TextField email = new TextField();
		ValidationController.checkIfEmail(email);
		assertTrue(!email.getValidators().isEmpty());
	}
	
	@Test
	public void testCheckIfPasswordIsEqualsWithRepliedPassword(){
		PasswordField input1 = new PasswordField();
		PasswordField input2 = new PasswordField();
		ValidationController.checkIfPasswordIsEqualWithRepliedPassword(input1, input2);
	}
}
