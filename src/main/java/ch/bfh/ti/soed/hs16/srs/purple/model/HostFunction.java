/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.model;
/**
 * HostFunction definiert eine Funktion eines Veranstalters.
 * Ein Beispiel für eine Funktion eines Veranstalters ist Physikprofessor.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas, Schildknecht Elias
 * @date 20.10.2016
 * @version 1.0
 *
 */
public class HostFunction {
	private int hostFunctionID;
	private String hostFunction;
	public String getHostFunction() {
		return hostFunction;
	}
	public void setHostFunction(String hostFunction) {
		this.hostFunction = hostFunction;
	}
	public int getHostFunctionID() {
		return hostFunctionID;
	}
	public void setHostFunctionID(int hostFunctionID) {
		this.hostFunctionID = hostFunctionID;
	}
}
