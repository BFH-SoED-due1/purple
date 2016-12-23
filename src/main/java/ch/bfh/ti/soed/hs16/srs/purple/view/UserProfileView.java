/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.view;

import java.util.List;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController;
import ch.bfh.ti.soed.hs16.srs.purple.controller.LoginController;
import ch.bfh.ti.soed.hs16.srs.purple.controller.UserProfileController;
import ch.bfh.ti.soed.hs16.srs.purple.controller.ValidationController;
import ch.bfh.ti.soed.hs16.srs.purple.model.Function;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;

/**
 * 
 * @author eliabosiger
 *
 */
public class UserProfileView implements ViewTemplate {

	// membervariables
	private Label title = new Label("Benutzerprofil");
	private TextField lastName = new TextField("Name");
	private TextField firstName = new TextField("Vorname");
	private TextField email = new TextField("Email-Adresse");
	private NativeSelect function = new NativeSelect("Funktion");
	private TextField username = new TextField("Benutzername");
	private PasswordField password = new PasswordField("Passwort ändern");
	private PasswordField passwordReply = new PasswordField("Passwort bestätigen");
	private Button save = new Button("Speichern");
	private FormLayout userProfileLayout = new FormLayout();
	private UserProfileController userProfileController = new UserProfileController();
	private User currentDbUser;

	/**
	 * Function initlaizes the Components for the registrationview.
	 */
	@Override
	public void initView() {
		this.userProfileLayout.setSpacing(true);
		this.title.setStyleName("h2");

		this.lastName.setInputPrompt("Name");
		this.firstName.setInputPrompt("Vorname");
		this.email.setInputPrompt("Email-Adresse");
		this.username.setInputPrompt("Benutzername");
		this.password.setInputPrompt("Passwort");
		this.passwordReply.setInputPrompt("Passwort");
		this.function.setRequired(true);

		List<Function> functions = this.userProfileController.getAllFunctions();

		for (Function f : functions) {
			this.function.addItem(f);
		}

		// TODO: validator, passwordField if empty = ok
		ValidationController.setTextFieldRequired(this.lastName);
		ValidationController.setTextFieldRequired(this.firstName);
		ValidationController.setTextFieldRequired(this.email);
		ValidationController.checkIfEmail(this.email);
		ValidationController.setTextFieldRequired(this.username);
		setEventOnSave();

		this.userProfileLayout.addComponent(this.title);
		this.userProfileLayout.addComponent(this.lastName);
		this.userProfileLayout.addComponent(this.firstName);
		this.userProfileLayout.addComponent(this.email);
		this.userProfileLayout.addComponent(this.function);
		this.userProfileLayout.addComponent(this.username);
		this.userProfileLayout.addComponent(this.password);
		this.userProfileLayout.addComponent(this.passwordReply);
		this.userProfileLayout.addComponent(this.save);
	}

	/**
	 * Function displays the registration view on the contentpanel.
	 */
	@Override
	public void display(Component content) {
		fillForm();
		Panel contentPanel = (Panel) content;
		contentPanel.setContent(this.userProfileLayout);
	}

	/**
	 * Function fills form with current user.
	 */
	private void fillForm() {
		this.currentDbUser = this.userProfileController.getUserForView(
				VaadinSession.getCurrent().getAttribute(LoginController.USER_SESSION_ATTRIBUTE).toString());
		this.lastName.setValue(this.currentDbUser.getLastName());
		this.firstName.setValue(this.currentDbUser.getFirstName());
		this.email.setValue(this.currentDbUser.getEmailAddress());
		this.username.setValue(this.currentDbUser.getUsername());
		this.password.setValue("");
		this.passwordReply.setValue("");
		this.function.setValue(this.currentDbUser.getFunction());
	}

	/**
	 * Function sets the event for the register button
	 */
	private void setEventOnSave() {
		this.save.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					UserProfileView.this.firstName.validate();
					UserProfileView.this.lastName.validate();
					UserProfileView.this.email.validate();

					if (!currentDbUser.getUsername().equals(username.getValue())) {
						ValidationController.checkIfUserAlredyExist(username, DBController.getInstance());
						UserProfileView.this.username.validate();
					}
					if (password.getValue() != null && !password.getValue().isEmpty()
							&& passwordReply.getValue() != null && !passwordReply.getValue().isEmpty()) {

						ValidationController.setPasswordFielRequired(password);
						ValidationController.setPasswordFielRequired(passwordReply);
						ValidationController.checkIfPasswordIsEqualWithRepliedPassword(password, passwordReply);
						UserProfileView.this.password.validate();
						UserProfileView.this.passwordReply.validate();
					}

					User modifiedUser = new User(null, firstName.getValue(), lastName.getValue(), email.getValue(),
							username.getValue(), password.getValue(), null, (Function) function.getValue());

					UserProfileView.this.userProfileController.updateUser(modifiedUser, currentDbUser.getUserID());
					System.out.println("update user successfully");

				} catch (InvalidValueException ex) {
					System.out.println("Failed to update user");
				}
			}
		});
	}
}
