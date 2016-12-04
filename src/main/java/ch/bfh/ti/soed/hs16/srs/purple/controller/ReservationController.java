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

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import ch.bfh.ti.soed.hs16.srs.purple.model.Reservation;
import ch.bfh.ti.soed.hs16.srs.purple.model.Room;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;
import ch.bfh.ti.soed.hs16.srs.purple.view.ReservationView;

public class ReservationController {
	private ReservationView reservationView;
	private User user;
	private DBController dbController;

	private List<User> hosts = new ArrayList<User>();

	public ReservationController(User actUser) {
		dbController = DBController.getInstance();
		hosts.add(actUser);
	}

	ClickListener clickListener = new ClickListener() {

		@Override
		public void buttonClick(ClickEvent event) {
			// Reservation uiRes = reservationView.getReservation();
			// switch (reservationView.getAction()) {
			// case ReservationView.Action.INSERT:
			// reservationView.setStatus(addReservation(uiRes.getStartDate(),
			// uiRes.getEndDate(), uiRes.getRoom(),
			// uiRes.getTitle(), uiRes.getDescription(),
			// uiRes.getParticipantList()));
			// break;
			// case ReservationView.Action.DELETE:
			// reservationView.setStatus(deleteReservation(uiRes.getReservationID()));
			// break;
			// case ReservationView.Action.EDIT:
			// reservationView.setStatus(editReservation(uiRes.getReservationID()));
			// break;
			// case ReservationView.Action.NONE:
			// break;
			// }
		}
	};

	/**
	 * Add a reservation to the DB
	 * 
	 * @param start
	 *            : startime of the reservation
	 * @param end
	 *            : endtime of the reservation
	 * @param room
	 *            : reservated room
	 * @param title
	 *            : title of the reservation
	 * @param description
	 *            : description of the reservation
	 * @param participants
	 *            : participants of the reservation
	 * @return true = success, false = fail
	 */
	public boolean addReservation(java.sql.Timestamp start, Timestamp end, Room room, String title, String description,
			List<User> participants) {
		if (this.dbController.insertNewReservation(start, end, room, this.hosts, participants, title, description)) {
			sendEmail(this.hosts, participants);
			getAllReservations();
			return true;
		}
		return false;
	}

	/**
	 * Delete a reservation from the DB
	 * 
	 * @param resID
	 *            : ReservationID
	 * @return true = success, false = fail
	 */
	public boolean deleteReservation(int resID) {
		return this.dbController.deleteReservation(resID);
	}

	/**
	 * Edit a reservation
	 * 
	 * @param resID
	 *            : ReservationID
	 */
	public void editReservation(int resID) {
		// // einfach löschen und neu erstellen? Oder zu gefährlich weil jemand
		// anderes dazwischen reservieren könnte?
	}

	/**
	 * Send an E-Mail to the hosts and participants of a reservation
	 * 
	 * @param hosts
	 *            : Hosts of the reservation
	 * @param participants
	 *            : Participants of the reservation
	 */
	public void sendEmail(List<User> hosts, List<User> participants) {
		// SimpleEmail mail = new SimpleEmail();
	}

	// /**
	// * Get All reservations from the DB
	// */
	public List<Reservation> getAllReservations() {
		// // vermutlich ists doch besser, immer alle Reservationen an die View
		// zu
		// // übergeben...
		return this.dbController.selectAllReservations();
	}

}
