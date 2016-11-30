/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

/**
 *
 * @author eliabosiger
 */
public class LoginView {

	//Membervariables
    HorizontalLayout loginLayout = new HorizontalLayout();
    TextField username = new TextField();
    PasswordField password = new PasswordField();
    Button loginButton = new Button("Anmelden");

    /**
     * Function returns the LoginView
     * @return
     */
    public HorizontalLayout initLoginView() {

        this.username.setInputPrompt("Benutzername");
        this.password.setInputPrompt("Passwort");

        this.loginLayout.addComponent(this.username);
        this.loginLayout.addComponent(this.password);
        this.loginLayout.addComponent(this.loginButton);
        this.loginLayout.setSpacing(true);
        this.loginLayout.setStyleName("login");

        return this.loginLayout;
    }
}
