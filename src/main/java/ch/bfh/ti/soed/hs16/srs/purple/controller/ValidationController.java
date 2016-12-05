/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.controller;

import java.util.List;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_User;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;

public class ValidationController {

	/**
	 * Function checks if the value of a textfield is empty or not.
	 *
	 * @param textfield - The textfield to be checked.
	 */
	public static void setTextFieldRequired(TextField textfield) {
		textfield.setRequired(true);
		textfield.setRequiredError("Das Feld '" + textfield.getCaption() + "' darf nicht leer sein!");
	}

	/**
	 * Function checks if the value of a passwordfield is empty or not.
	 *
	 * @param passwordField - The passwordfield to be checked
	 */
	public static void setPasswordFielRequired(PasswordField passwordField) {
		passwordField.setRequired(true);
		passwordField.setRequiredError("Das Feld '" + passwordField.getCaption() + "' darf nicht leer sein!");
	}

	/**
	 * Function checks if the value of the textfield is an email address
	 *
	 * @param textfield - The textfield to be checked
	 */
	public static void checkIfEmail(TextField textfield) {
		textfield.addValidator(new EmailValidator("Ungültige E-Mail Adresse"));
	}

	/**
	 * Function checks if the password is equal with the replied password.
	 *
	 * @param passwordField - The passwordfield to be checked
	 * @param repliedPasswordField - The replied password
	 */
	public static void checkIfPasswordIsEqualWithRepliedPassword(PasswordField passwordField,
			PasswordField repliedPasswordField) {

		repliedPasswordField.addValidator(new Validator() {

			@Override
			public void validate(Object value) throws InvalidValueException {
				if (!value.equals(passwordField.getValue())) {
					throw new InvalidValueException("Passwort stimmt nicht überein!");
				}
			}
		});
	}

	/**
	 * Function checks in the database if the username is already used.
	 *
	 * @param username - The "username"-textfield
	 * @param dbController - Instance of DBController
	 */
	public static void checkIfUserAlredyExist(TextField username, DBController dbController) {
		username.addValidator(new Validator() {

			@Override
			public void validate(Object value) throws InvalidValueException {
				List<User> users = dbController.selectUserBy(Table_User.COLUMN_USERNAME, username.getValue());

				if (users.size() != 0) {
					throw new InvalidValueException("Username ist bereits vergeben!");
				}
			}
		});
	}
}
