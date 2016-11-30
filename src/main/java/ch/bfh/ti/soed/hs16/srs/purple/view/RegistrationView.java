/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

/**
 *
 * @author eliabosiger
 */
public class RegistrationView implements ViewTemplate {

    //Membervariable
    Label title = new Label("Registrieren");
    TextField lastName = new TextField("Name");
    TextField firstName = new TextField("Vorname");
    TextField email = new TextField("Email-Adresse");
    TextField username = new TextField("Benutzername");
    PasswordField password = new PasswordField("Passwort");
    PasswordField passwordReply = new PasswordField("Passwort bestätigen");
    Button register = new Button("Registrieren");
    FormLayout registrationLayout = new FormLayout();
    
    @Override
    public void initView() {
        this.registrationLayout.setSpacing(true);
        this.title.setStyleName("h2");
        
        this.lastName.setInputPrompt("Name");
        this.firstName.setInputPrompt("Vorname");
        this.email.setInputPrompt("Email-Adresse");
        this.username.setInputPrompt("Benutzername");
        this.password.setInputPrompt("Passwort");
        this.passwordReply.setInputPrompt("Passwort");
        
        this.registrationLayout.addComponent(this.title);
        this.registrationLayout.addComponent(this.lastName);
        this.registrationLayout.addComponent(this.firstName);
        this.registrationLayout.addComponent(this.email);
        this.registrationLayout.addComponent(this.username);
        this.registrationLayout.addComponent(this.password);
        this.registrationLayout.addComponent(this.passwordReply);
        this.registrationLayout.addComponent(this.register);
    }
    
    @Override
    public void display(Panel contentPanel) {
        contentPanel.setContent(this.registrationLayout);
    }
}
