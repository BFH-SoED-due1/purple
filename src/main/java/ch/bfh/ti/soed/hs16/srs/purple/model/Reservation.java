/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.model;

import java.sql.Timestamp;
import java.util.List;
/**
 * Eine Reservation definiert eine Reservation, die von einem Veranstalter erstellt wurde.
 * Eine Reservation beinhaltet einen Raum, ein Start- und End-Datum, sowie die Veranstalter und die Teilnehmer.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas, Schildknecht Elias
 * @date 20.10.2016
 * @version 1.0
 *
 */
public class Reservation {
	private int reservationID;
	private Room room;
	private Timestamp startDate;
	private Timestamp endDate;
	private List<User> hostList;
	private List<User> participantList;

	public Reservation(int reservationID, Room room, Timestamp startDate, Timestamp endDate){
		this.reservationID = reservationID;
		this.room = room;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Room getRoom()
	{
		return room;
	}
	public void setRoom(Room room)
	{
		this.room = room;
	}
	public int getReservationID() {
		return reservationID;
	}
	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	
	public void addHost(User host){
		hostList.add(host);
	}
	
	public void addParticipant(User participant){
		participantList.add(participant);
	}
}
