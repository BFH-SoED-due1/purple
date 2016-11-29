/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.model;

/**
 * TODO: Really used?
 * HostUser ist von der User-Klasse abgeleitet.
 * Sie verfügt zusätzlich über die hostFunctionList, welche die Funktionen des Veranstalters definiert.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas, Schildknecht Elias
 * @date 20.10.2016
 * @version 1.0
 *
 */
public class HostUser extends User {
	
	private Function function;
	
	public HostUser(Integer userID, String lastname, String firstname, String emailAddress, String username, String password, Role role, Function function){
		super(userID, lastname, firstname, emailAddress, username, password, role);
		this.function = function;
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}
}
