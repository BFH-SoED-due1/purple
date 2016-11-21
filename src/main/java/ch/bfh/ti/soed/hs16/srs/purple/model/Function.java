/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.model;
/**
 * Function definiert eine Funktion eines Benutzers.
 * Ein Beispiel für eine Funktion eines Benutzers ist Physikprofessor.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas, Schildknecht Elias
 * @date 20.10.2016
 * @version 1.0
 *
 */
public class Function {
	private int id;
	private String function;
	
	public Function(int id, String function){
		this.id = id;
		this.function = function;
	}
	
	public String getFunction() {
		return function;
	}
	
	public void setFunction(String function) {
		this.function = function;
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
}
