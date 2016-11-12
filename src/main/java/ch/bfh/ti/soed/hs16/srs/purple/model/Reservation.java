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
	private long startDate;
	private long endDate;
	private List<User> hostList;
	private List<User> participantList;

	public Reservation(Room r, long sd, long ed, List<User> hl, List<User> lp){

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
	public long getStartDate() {
		return startDate;
	}
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	public List<User> getHostList() {
		return hostList;
	}
	public void setHostList(List<User> hostList) {
		this.hostList = hostList;
	}
	public List<User> getParticipantList() {
		return participantList;
	}
	public void setParticipantList(List<User> participantList) {
		this.participantList = participantList;
	}
}
