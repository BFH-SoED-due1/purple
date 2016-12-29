/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.model;

/**
 * Ein User ist ein Benutzer des Systems. Ein User registriert sich mit den
 * folgenden Attributen: - Nachname - Vorname - Email-Adresse - Username -
 * Passwort Die Rolle des Benutzers wird in der Klasse UserRole definiert. Die
 * Reservations-Applikation erlaubt die folgenden zwei Benutzerrollen: -
 * USER_ROLE_MEMBER: Member. - USER_ROLE_ADMIN: Admin.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas, Schildknecht Elias
 * @date 20.10.2016
 * @version 1.0
 *
 */
public class User {
	private Integer userID;
	private String lastName;
	private String firstName;
	private String emailAddress;
	private String username;
	// TODO: encrypt password
	private String password;

	private Role role;
	private Function function;

	public User(Integer userID, String firstname, String lastname, String emailAddress, String username,
			String password, Role role, Function function) {
		this.userID = userID;
		this.lastName = lastname;
		this.firstName = firstname;
		this.emailAddress = emailAddress;
		this.username = username;
		this.password = password;
		this.role = role;
		this.function = function;
	}

	public User() {
		// Only for Testing
	}

	public Integer getUserID() {
		return userID;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Role getRole() {
		return role;
	}

	public Function getFunction() {
		return function;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

}
