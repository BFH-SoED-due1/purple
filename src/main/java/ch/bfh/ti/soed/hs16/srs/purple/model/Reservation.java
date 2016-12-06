/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.components.calendar.event.BasicEvent;
/**
 * Eine Reservation definiert eine Reservation, die von einem Veranstalter erstellt wurde.
 * Eine Reservation beinhaltet einen Raum, ein Start- und End-Datum, sowie die Veranstalter und die Teilnehmer.
 *
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas, Schildknecht Elias
 * @date 20.10.2016
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class Reservation extends BasicEvent{
	private Integer reservationID;
	private Room room;
	private Timestamp startDate;
	private Timestamp endDate;
	private List<User> hostList;
	private List<User> participantList;
	private String title;
	private String description;

	public Reservation(Integer reservationID, Timestamp startDate, Timestamp endDate, Room room, String title, String description){
		super();
		this.reservationID = reservationID;
		this.room = room;
		this.startDate = startDate;
		setStart(startDate);
		this.endDate = endDate;
		setEnd(endDate);
		this.title = title;
		setCaption(title);
		this.description = description;
		setDescription(description);
		hostList = new ArrayList<User>();
		participantList = new ArrayList<User>();
	}
	
	public Reservation(Integer reservationID, Timestamp startDate, Timestamp endDate, Room room, String title, String description, List<User> hosts){
		super();
		this.reservationID = reservationID;
		this.room = room;
		this.startDate = startDate;
		setStart(startDate);
		this.endDate = endDate;
		setEnd(endDate);
		this.title = title;
		setCaption(title);
		this.description = description;
		setDescription(description);
		hostList = hosts;
		participantList = new ArrayList<User>();
	}
	
	public Reservation(Integer reservationID, Timestamp startDate, Timestamp endDate, Room room, String title, String description, List<User> hosts, List<User> participants){
		super();
		this.reservationID = reservationID;
		this.room = room;
		this.startDate = startDate;
		setStart(startDate);
		this.endDate = endDate;
		setEnd(endDate);
		this.title = title;
		setCaption(title);
		this.description = description;
		setDescription(description);
		hostList = hosts;
		participantList = participants;
	}

	public Room getRoom() {
		return room;
	}

	public Integer getReservationID() {
		return reservationID;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void addHost(User host){
		hostList.add(host);
	}

	public void addParticipant(User participant){
		participantList.add(participant);
	}

	public List<User> getHostList() {
		return hostList;
	}

	public List<User> getParticipantList() {
		return participantList;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
}
