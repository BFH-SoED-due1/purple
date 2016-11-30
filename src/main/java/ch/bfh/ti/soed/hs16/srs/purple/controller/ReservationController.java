/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.controller;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ch.bfh.ti.soed.hs16.srs.purple.model.Room;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;
import ch.bfh.ti.soed.hs16.srs.purple.view.ReservationView;

public class ReservationController {
	private ReservationView reservationView;
	private User user;
	private DBController dbController;
	
	private List<User> hosts = new ArrayList<User>();
	
	public ReservationController(User actUser){
		dbController = DBController.getInstance();
		hosts.add(actUser);
	}
	
	// start, end welcher user titel beschreibung tilnehmerliste
	public boolean addReservation(java.sql.Timestamp start, Timestamp end, Room room, String title, String description, List<User> participants){
		if (this.dbController.insertNewReservation(start, end, room, this.hosts, participants, title, description)){
			sendEmail(this.hosts, participants);
			getAllReservations();
			return true;
		}
		return false;
	}
	
	public void sendEmail(List<User> hosts, List<User> participants){
		
	}
	
	public void getAllReservations(){
		
	}
	
}
