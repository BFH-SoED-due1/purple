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
	private List<User> acceptedParticipantsList;
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
		acceptedParticipantsList = new ArrayList<User>();
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

	public boolean haveAllParticipantsAccepted() {
		return participantList.size() == acceptedParticipantsList.size();
	}

	public boolean hasParticipantAcceptedReservation(User user){
		boolean hasAccepted = false;
		for(User acceptedUser : acceptedParticipantsList){
			if(user.getUserID().equals(acceptedUser.getUserID())) hasAccepted = true;
		}
		return hasAccepted;
	}

	public void removeParticipant(User participant) {
		User toRemove = null;
		for(User user : participantList) {
			if(user.getUserID().equals(participant.getUserID())) toRemove = user;
		}
		participantList.remove(toRemove);
		for(User acceptedParticipant : acceptedParticipantsList) {
			if(acceptedParticipant.getUserID().equals(participant.getUserID())) toRemove = acceptedParticipant;
		}
		acceptedParticipantsList.remove(toRemove);
	}

	/**
	 * Removes a host from the reservation.
	 *
	 * @param host - The host do remove.
	 * @return true if the host could be removed - false if the host could not have been deleted because there would remain no host for the reservation.
	 * */
	public boolean removeHost(User host) {
		if(hostList.size() <= 1) {
			return false;
		} else {
			User toDelete = null;
			for(User user : hostList) {
				if(user.getUserID().equals(host.getUserID())) toDelete = user;
			}
			hostList.remove(toDelete);
			return true;
		}
	}

	public void removeAllParticipants() {
		participantList.clear();
		acceptedParticipantsList.clear();
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

	public void addAcceptedParticipant(User participant) {
		acceptedParticipantsList.add(participant);
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

	@Override
	public String getDescription() {
		return description;
	}

	public List<User> getAcceptedParticipantsList() {
		return acceptedParticipantsList;
	}

	public void changeRoom(Room room) {
		this.room = room;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}
}
