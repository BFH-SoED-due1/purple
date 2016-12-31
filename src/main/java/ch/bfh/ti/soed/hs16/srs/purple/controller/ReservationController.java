/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.controller;

import java.sql.Timestamp;
import java.util.List;

import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_Reservation;
import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_Room;
import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_User;
import ch.bfh.ti.soed.hs16.srs.purple.model.Reservation;
import ch.bfh.ti.soed.hs16.srs.purple.model.Room;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;
import ch.bfh.ti.soed.hs16.srs.purple.util.Email;

public class ReservationController {
	private DBController dbController;

	private static String DOMAIN = "Unsere Website";

	public ReservationController() {
		dbController = DBController.getInstance();
	}

	/**
	 * Add a reservation to the DB
	 *
	 * @param res - The reservation
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
	 *            - ReservationID
	 * @return true = success, false = fail
	 */
	public boolean deleteReservation(int resID) {
		return this.dbController.deleteReservation(resID);
	}


	/**
	 * Update a reservation
	 *
	 * @param reservation
	 *            - Reservation-Object
	 */
	public boolean updateReservation(Reservation reservation) {
		return this.dbController.updateReservation(reservation);
	}

	/**
	 * User accepts the reservation
	 * @param resID - ID of the accepted reservation
	 */
	public boolean acceptReservation(User user, Reservation reservation){
		return this.dbController.updateAcceptReservation(user, reservation, true);
	}

	/**
	 * User cancels the reservation
	 * @param resID - ID of the canceled reservation
	 */
	public boolean cancelReservation(User user, Reservation reservation){
		return this.dbController.updateAcceptReservation(user, reservation, false);
	}

	/**
	 * Get the room-object from the roomID
	 * @param roomID - ID of a room
	 * @return The specific room with the given ID
	 */
	public Room getRoom(int roomID)
	{
		return this.dbController.selectRoomBy(Table_Room.COLUMN_ID, roomID).get(0);
	}

	/**
	 * Get all rooms form the DB
	 * @return List of all rooms
	 */
	public List<Room> getAllRooms(){
		return this.dbController.selectAllRooms();
	}

	/**
	 * Returns all free rooms during the given Time
	 * @param startDate - Starttime of the timewindow
	 * @param endDate - Endtime of the timewindow
	 * @return list of free Rooms or an empty list, if no room is avaiable
	 */
	public List<Room> getAllFreeRooms(Timestamp startDate, Timestamp endDate){
		return this.dbController.selectFreeRooms(startDate, endDate);
	}

	/**
	 * Get all reservations form one specific room
	 * @param roomID - ID of a room
	 *
	 * @return List of all reservations from a specific room
	 */
	public List<Reservation> getAllReservationsFromRoom(int roomID){
		return this.dbController.selectReservationBy(Table_Reservation.COLUMN_ROOMID, roomID);
	}

	/**
	 * Get all reservations from one specific user
	 * @param user - ID of an user
	 * @return List of all reservations from a specific user
	 */
	public List<Reservation> getAllReservationsFromUser(User user){
		List<Reservation> tempList = this.dbController.selectReservationsForUser(user, true);
		tempList.addAll(this.dbController.selectReservationsForUser(user, false));
		return tempList;
	}
	/**
	 * Get all Users
	 * @return List of all users
	 */
	public List<User> getAllUsers(){
		return this.dbController.selectAllUsers();
	}

	/**
	 * Get VaadinSession-user from the DB
	 * @param user - String with a valid username
	 * @return Userobject from user with the name at parameter user
	 */
	public User getSessionUser(String user){
		return dbController.selectUserBy(Table_User.COLUMN_USERNAME, user).get(0);
	}

	/**
	 * Get all reservations
	 * @return List of all reservations on the system
	 */
	public List<Reservation> getAllReservations() {
		return this.dbController.selectAllReservations();
	}

	/**
	 * Sends an E-Mail to the hosts and participants of a reservation
	 * @param reservation - Object
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
			mail.send();
		}

		for (int i = 0; i < reservation.getParticipantList().size(); i++){
			String message =	"Hallo " + reservation.getParticipantList().get(i).getUsername() + "<br>" +
								reservation.getHostList().get(0).getUsername() + " hat dich als Teilnehmer für folgenden Termin hinzugefügt: <br>" +
								"Thema: " + reservation.getTitle() + "<br>" +
								"Raum: " + reservation.getRoom().getName() + "<br>" +
								"Beginn: " + reservation.getStartDate().toString() + "<br>" +
								"Ende: " + reservation.getEndDate().toString() + "<br>" +
								"Bitte logge dich auf " + DOMAIN + " ein und sage entweder zu oder ab.";
			Email mail = new Email(reservation.getParticipantList().get(i).getEmailAddress(), "Neuer Termin", message);
			mail.send();
		}
	}

}
