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
 * Ein Raum kann reserviert werden.
 * Ein Raum besitzt eine Raumnummer, eine Anzahl an Sitzplätzen sowie unterschiedliche Gegenstände.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas, Schildknecht Elias
 * @date 20.10.2016
 * @version 1.0
 *
 */
public class Room {
	private int roomID;
	private String name;
	private int roomNumber;
	private int numberOfSeats;
	private List<Equipment> equipmentList;
	private List<Reservation> reservationList;
	
	public Room(int roomID, int roomNumber, String name, int numberOfSeats){
		this.roomID = roomID;
		this.name = name;
		this.roomNumber = roomNumber;
		this.numberOfSeats = numberOfSeats;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRoomID() {
		return roomID;
	}
	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}
	public int getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	public int getNumberOfSeats() {
		return numberOfSeats;
	}
	public void setNumberOfSeats(int numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}
	public List<Equipment> getEquipmentList() {
		return equipmentList;
	}
	public void setEquipmentList(List<Equipment> equipmentList) {
		this.equipmentList = equipmentList;
	}
	public List<Reservation> getReservationList() {
		return reservationList;
	}
	public void setReservationList(List<Reservation> reservationList) {
		this.reservationList = reservationList;
	}
	
	@Override
	public boolean equals(Object o){
		return ((Room)o).getRoomID() == this.roomID;
	}
}
