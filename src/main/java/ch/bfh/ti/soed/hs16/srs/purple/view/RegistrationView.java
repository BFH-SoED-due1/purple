/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.view;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController;
import ch.bfh.ti.soed.hs16.srs.purple.controller.RegistrationController;
import ch.bfh.ti.soed.hs16.srs.purple.controller.ValidationController;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;

/**
 *
 * @author eliabosiger
 */
public class RegistrationView implements ViewTemplate {

	// Membervariable
	Label successFullRegistrationTitel = new Label("Erfolgreiche Registration");
	Label successFullRegistration = new Label();
	Label successFullLayout = new Label();

	Label title = new Label("Registrieren");
	TextField lastName = new TextField("Name");
	TextField firstName = new TextField("Vorname");
	TextField email = new TextField("Email-Adresse");
	TextField username = new TextField("Benutzername");
	PasswordField password = new PasswordField("Passwort");
	PasswordField passwordReply = new PasswordField("Passwort bestätigen");
	Button register = new Button("Registrieren");
	FormLayout registrationLayout = new FormLayout();
	RegistrationController registrationController = new RegistrationController(this);

	/**
	 * Function initlaizes the Components for the registrationview.
	 */
	@Override
	public void initView() {
		this.registrationLayout.setSpacing(true);
		this.title.setStyleName("h2");
		this.successFullRegistrationTitel.setStyleName("h2");

		this.lastName.setInputPrompt("Name");
		this.firstName.setInputPrompt("Vorname");
		this.email.setInputPrompt("Email-Adresse");
		this.username.setInputPrompt("Benutzername");
		this.password.setInputPrompt("Passwort");
		this.passwordReply.setInputPrompt("Passwort");

		ValidationController.setTextFieldRequired(this.lastName);
		ValidationController.setTextFieldRequired(this.firstName);
		ValidationController.setTextFieldRequired(this.email);
		ValidationController.checkIfEmail(this.email);
		ValidationController.setTextFieldRequired(this.username);
		ValidationController.checkIfUserAlredyExist(username, DBController.getInstance());
		ValidationController.setPasswordFielRequired(this.password);
		ValidationController.setPasswordFielRequired(this.passwordReply);
		ValidationController.checkIfPasswordIsEqualWithRepliedPassword(this.password, this.passwordReply);

		setDefaultView();

		setEventOnRegistration();
	}

	/**
	 * Function displays the registration view on the contentpanel.
	 */
	@Override
	public void display(Component content) {
		setDefaultView();
		Panel contentPanel = (Panel) content;
		contentPanel.setContent(this.registrationLayout);
	}

	/**
	 * Function sets the event for the register button
	 */
	private void setEventOnRegistration() {
		this.register.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					RegistrationView.this.firstName.validate();
					RegistrationView.this.lastName.validate();
					RegistrationView.this.email.validate();
					RegistrationView.this.username.validate();
					RegistrationView.this.password.validate();
					RegistrationView.this.passwordReply.validate();

					User newRegisteredUser = new User(null, firstName.getValue(), lastName.getValue(), email.getValue(),
							username.getValue(), password.getValue(), null);

					RegistrationView.this.registrationController.registerNewUser(newRegisteredUser, null);
					setAfterRegistrationView();
				} catch (InvalidValueException ex) {
					System.out.println("Failed to register");
				}
			}
		});
	}

	/**
	 * Function shows the registration view after a successful registration.
	 */
	private void setAfterRegistrationView() {
		this.registrationLayout.removeAllComponents();

		this.successFullRegistration.setValue(
				"Sie haben sich erfolgreich mit dem  Benutzername '" + this.username.getValue() + "' registriert.");

		this.registrationLayout.addComponent(this.successFullRegistrationTitel);
		this.registrationLayout.addComponent(this.successFullRegistration);

	}

	/**
	 * Function add the components for the default view.
	 */
	private void setDefaultView() {
		this.registrationLayout.removeAllComponents();

		this.lastName.setValue("");
		this.firstName.setValue("");
		this.email.setValue("");
		this.username.setValue("");
		this.password.setValue("");
		this.passwordReply.setValue("");
		this.registrationLayout.addComponent(this.title);
		this.registrationLayout.addComponent(this.lastName);
		this.registrationLayout.addComponent(this.firstName);
		this.registrationLayout.addComponent(this.email);
		this.registrationLayout.addComponent(this.username);
		this.registrationLayout.addComponent(this.password);
		this.registrationLayout.addComponent(this.passwordReply);
		this.registrationLayout.addComponent(this.register);
	}
}
