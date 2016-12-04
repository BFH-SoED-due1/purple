/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.view;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

import ch.bfh.ti.soed.hs16.srs.purple.controller.LoginController;

/**
 *
 * @author eliabosiger
 */
public class LoginView implements ViewTemplate {

	// Membervariables
	private HorizontalLayout loginLayout = new HorizontalLayout();
	private HorizontalLayout logoutLayout = new HorizontalLayout();
	private TextField username = new TextField();
	private PasswordField password = new PasswordField();
	private Button loginButton = new Button("Anmelden");
	private Button logoutButton = new Button("Abmelden");
	private LoginController loginController;
	private ViewStructure viewStructure;

	/**
	 * Constructor: LoginView
	 * 
	 * @param viewStructure
	 * @param loginController
	 */
	public LoginView(ViewStructure viewStructure, LoginController loginController) {
		this.viewStructure = viewStructure;
		this.loginController = loginController;
	}

	/**
	 * Function initalizes the LoginView
	 */
	@Override
	public void initView() {
		this.username.setInputPrompt("Benutzername");
		this.password.setInputPrompt("Passwort");
		
		this.loginLayout.addComponent(this.username);
		this.loginLayout.addComponent(this.password);
		this.loginLayout.addComponent(this.loginButton);
		this.loginLayout.setSpacing(true);
		setEventOnLogin();
		this.loginLayout.setStyleName("login");

		this.logoutLayout.addComponent(this.logoutButton);
		setEventOnLogout();
		this.logoutLayout.setStyleName("login");
		
		this.username.setImmediate(true);
		this.password.setImmediate(true);

	}

	/**
	 * Function sets the content to display the view after
	 */
	@Override
	public void display(Component content) {
		HorizontalLayout component = (HorizontalLayout) content;
		if (!this.loginController.isUserLoggedInOnSession()) {
			component.addComponent(this.loginLayout);
		} else {
			component.addComponent(this.logoutLayout);
		}
	}

	//TODO: textfield red does not always show
	/**
	 * Function sets the event for the login button
	 */
	private void setEventOnLogin() {

		this.loginButton.setClickShortcut(KeyCode.ENTER);
		this.loginButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (LoginView.this.loginController.loginWithUser(LoginView.this.username.getValue(),
						LoginView.this.password.getValue())) {
					System.out.println("login with user " + LoginView.this.username.getValue()); //TODO: remove this 
					LoginView.this.viewStructure.refreshLoginLogoutContent(LoginView.this.logoutLayout, true);
					LoginView.this.username.addStyleName("normalTextField");
					LoginView.this.password.addStyleName("normalTextField");
				} else {
					// TODO: failed to login
					LoginView.this.username.addStyleName("errorTextField");
					LoginView.this.password.addStyleName("errorTextField");
					System.out.println("failed to login in with user " + LoginView.this.username.getValue());
				}
			}
		});
	}

	/**
	 * Function sets the event for the logout button
	 */
	private void setEventOnLogout() {
		this.logoutButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				LoginView.this.loginController.logout();
				LoginView.this.viewStructure.refreshLoginLogoutContent(LoginView.this.loginLayout, false);
			}
		});
	}
}
