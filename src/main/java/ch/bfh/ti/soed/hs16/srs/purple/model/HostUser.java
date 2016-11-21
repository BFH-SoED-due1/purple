/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.model;

import java.util.List;
/**
 * HostUser ist von der User-Klasse abgeleitet.
 * Sie verfügt zusätzlich über die hostFunctionList, welche die Funktionen des Veranstalters definiert.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas, Schildknecht Elias
 * @date 20.10.2016
 * @version 1.0
 *
 */
public class HostUser {
	private List<Function> hostFunctionList;

	public List<Function> getHostFunctionList() {
		return hostFunctionList;
	}

	public void setHostFunctionList(List<Function> hostFunctionList) {
		this.hostFunctionList = hostFunctionList;
	}
}
