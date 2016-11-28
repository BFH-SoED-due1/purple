/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.model;

/**
 * Ein User ist ein Benutzer des Systems.
 * Ein User registriert sich mit den folgenden Attributen:
 * - Nachname
 * - Vorname
 * - Email-Adresse
 * - Username
 * - Passwort
 * Die Rolle des Benutzers wird in der Klasse UserRole definiert. Die Reservations-Applikation erlaubt die folgenden
 * zwei Benutzerrollen:
 * - USER_ROLE_MEMBER: Member.
 * - USER_ROLE_ADMIN: Admin.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas, Schildknecht Elias
 * @date 20.10.2016
 * @version 1.0
 *
 */
public class User {
	private int userID;
	private String lastName;
	private String firstName;
	private String emailAddress;
	private String username;
	// TODO: encrypt password
	private String password;
	private Role role;

	public User(int userID, String lastname, String firstname, String emailAddress, String username, String password, Role role){
		this.userID = userID;
		this.lastName = lastname;
		this.firstName = firstname;
		this.emailAddress = emailAddress;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public User() {
		// Only for Testing
	}

	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
