/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.model;

/**
 * Ein Raum kann reserviert werden.
 * Ein Raum besitzt eine Raumnummer, eine Anzahl an Sitzplätzen sowie unterschiedliche Gegenstände.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas, Schildknecht Elias
 * @date 20.10.2016
 * @version 1.0
 *
 */
public class Room {
	private Integer roomID;
	private String name;
	private Integer roomNumber;
	private Integer numberOfSeats;

	public Room(Integer roomID, Integer roomNumber, String name, Integer numberOfSeats){
		this.roomID = roomID;
		this.name = name;
		this.roomNumber = roomNumber;
		this.numberOfSeats = numberOfSeats;
	}

	public void setRoomID(Integer roomID) {
		this.roomID = roomID;
	}

	public String getName() {
		return name;
	}

	public Integer getRoomID() {
		return roomID;
	}

	public Integer getRoomNumber() {
		return roomNumber;
	}

	public Integer getNumberOfSeats() {
		return numberOfSeats;
	}
}
