/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.controller;

import java.util.List;

import ch.bfh.ti.soed.hs16.srs.purple.model.Reservation;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;
import ch.bfh.ti.soed.hs16.srs.purple.util.Email;
import ch.bfh.ti.soed.hs16.srs.purple.view.ReservationView;

public class ReservationController {
	private ReservationView reservationView;
	private User user;
	private DBController dbController;

	private static String DOMAIN = "Unsere Website";

	public ReservationController() {
		dbController = DBController.getInstance();
	}

//	private ClickListener clickListener = new ClickListener() {
//
//		@Override
//		public void buttonClick(ClickEvent event) {
//			// Reservation uiRes = reservationView.getReservation();
//			// switch (reservationView.getAction()) {
//			// case ReservationView.Action.INSERT:
//			// reservationView.setStatus(addReservation(uiRes.getStartDate(),
//			// uiRes.getEndDate(), uiRes.getRoom(),
//			// uiRes.getTitle(), uiRes.getDescription(),
//			// uiRes.getParticipantList()));
//			// break;
//			// case ReservationView.Action.DELETE:
//			// reservationView.setStatus(deleteReservation(uiRes.getReservationID()));
//			// break;
//			// case ReservationView.Action.EDIT:
//			// reservationView.setStatus(editReservation(uiRes.getReservationID()));
//			// break;
//			// case ReservationView.Action.NONE:
//			// break;
//			// }
//		}
//	};

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
	public boolean addReservation(Reservation res) {
		if (this.dbController.insertNewReservation(res.getStartDate(), res.getEndDate(), res.getRoom(), res.getHostList(), res.getParticipantList(), res.getTitle(), res.getDescription())) {
			sendEmail(res);
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

//	/**
//	 * Edit a reservation
//	 *
//	 * @param resID
//	 *            : ReservationID
//	 */
//	public void editReservation(int resID) {
//		// // einfach löschen und neu erstellen? Oder zu gefährlich weil jemand
//		// anderes dazwischen reservieren könnte?
//	}

	/*
	 * accept a reservation
	 */
	public void acceptReservation(int resID){
		
	}
	
	/*
	 * cancel a reservation
	 */
	public void canelReservation(int resID){
		
	}
	
	/**
	 * Send an E-Mail to the hosts and participants of a reservation
	 *
	 * @param hosts
	 *            : Hosts of the reservation
	 * @param participants
	 *            : Participants of the reservation
	 */
	public void sendEmail(Reservation reservation) {
		for (int i = 0; i < reservation.getHostList().size(); i++){
			String message =	"Hallo " + reservation.getHostList().get(i).getUsername() + "<br>" + 
								reservation.getHostList().get(0).getUsername() + " hat dich als Veranstalter für folgenden Termin hinzugefügt: <br>" + 
								"Thema: " + reservation.getTitle() + "<br>" +
								"Raum: " + reservation.getRoom().getName() + "<br>" +
								"Beginn: " + reservation.getStartDate().toString() + "<br>" +
								"Ende: " + reservation.getEndDate().toString() + "<br>" +
								"Bitte logge dich auf " + DOMAIN + " ein und sage entweder zu oder ab.";
			Email mail = new Email(reservation.getHostList().get(i).getEmailAddress(), "Neuer Termin", message);
		//	Email mail = new Email("christianwenger@hotmail.com", "Neuer Termin", message);
			mail.send();
		}
	}

	// /**
	// * Get All reservations from the DB
	// */
	public List<Reservation> getAllReservations() {
		return this.dbController.selectAllReservations();
	}

}
