/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController;
import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_Reservation;
import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_Room;
import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_User;
import ch.bfh.ti.soed.hs16.srs.purple.controller.ReservationController;
import ch.bfh.ti.soed.hs16.srs.purple.model.Reservation;
import ch.bfh.ti.soed.hs16.srs.purple.model.Room;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;

public class ReservationControllerTest {

	private static ReservationController reservationController = new ReservationController();
	private Timestamp startTime1 = new Timestamp(new Date().getTime());
	private Timestamp endTime1 = new Timestamp(new Date().getTime()+3600000);
	private static Room testRoom1;
	private static Room testRoom2;
	private static User testUserHost;
	private static User testUserPart;
	private static List<User> testUserHostList = new ArrayList<User>();
	private static List<User> testUserPartList = new ArrayList<User>();


	@BeforeClass
	public static void beginTestReservationController(){
		DBController.getInstance().insertNewRoom(333, "ResContTest1", 333);
		testRoom1 = DBController.getInstance().selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, 333).get(0);
		DBController.getInstance().insertNewRoom(334, "ResContTest2", 334);
		testRoom2 = DBController.getInstance().selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, 334).get(0);
		DBController.getInstance().insertNewUser("Test", "User", "patrik.aebischer@hotmail.com", "TestUser", "1234", DBController.getInstance().selectAllFunctions().get(0), DBController.getInstance().selectAllRoles().get(0));
		testUserHost = DBController.getInstance().selectUserBy(Table_User.COLUMN_USERNAME, "TestUser").get(0);
		testUserHostList.add(testUserHost);
		DBController.getInstance().insertNewUser("Test", "Participant", "patrik.aebischer@hotmail.com", "TestUserPart", "1234", DBController.getInstance().selectAllFunctions().get(0), DBController.getInstance().selectAllRoles().get(0));
		testUserPart = DBController.getInstance().selectUserBy(Table_User.COLUMN_USERNAME, "TestUserPart").get(0);
		testUserPartList.add(testUserHost);
	}

	@Test
	public void testAddReservation(){
		assertTrue(reservationController.addReservation(new Reservation(-1, startTime1, endTime1, testRoom1, "Test add Reservation", "Reservation added!", testUserHostList)));
		for(Reservation reservation : DBController.getInstance().selectReservationBy(DBController.Table_Reservation.COLUMN_ROOMID, testRoom1.getRoomID())){
			assertTrue(reservationController.deleteReservation(reservation.getReservationID()));
		}
	}

	@Test
	public void testDeleteReservation(){
		reservationController.addReservation(new Reservation(-1, startTime1, endTime1, testRoom2, "Test add Reservation", "Reservation added!", testUserHostList));
		int resID = DBController.getInstance().selectReservationBy(Table_Reservation.COLUMN_ROOMID, testRoom2.getRoomID()).get(0).getReservationID();
		assertTrue(reservationController.deleteReservation(resID));
	}
/*
	@Test
	public void testAcceptReservation(){
		Reservation res = new Reservation(-1, startTime1, endTime1, testRoom1, "Test add Reservation", "Reservation added!", testUserHostList, testUserPartList);
		reservationController.addReservation(res);
		res = DBController.getInstance().selectReservationBy(Table_Reservation.COLUMN_ROOMID, testRoom1.getRoomID()).get(0);
		reservationController.acceptReservation(testUserPart, res);
		res = DBController.getInstance().selectReservationBy(Table_Reservation.COLUMN_ROOMID, testRoom1.getRoomID()).get(0);
		assertEquals(res.getAcceptedParticipantsList().get(0).getUsername(), "TestUserPart");
	}*/

	@Ignore
	public void testCancelReservation(){

	}

	// TODO
	@Ignore
	public void testGetAllRooms(){
		//TODO: Räume hinzufügen und wieder löschen
		assertNotNull(reservationController.getAllRooms());
	}

	@Ignore
	public void testGetAllReservationsFromRoom(){

		//assertNotNull(reservationController.getAllReservationsFromRoom(roomID));
	}

	@Ignore
	public void testGetAllReservationsFromUser(){

	}

	@Ignore
	public void testGetAllUsers(){

	}

	// TODO
	@Ignore
	public void testGetSessionUser(){
		assertEquals(reservationController.getSessionUser(DBController.getInstance().selectAllUsers().get(0).toString()), DBController.getInstance().selectAllUsers().get(0).toString());
	}

	@Ignore
	public void testGetAllReservations(){

	}

	// TODO
	@Ignore
	public void testWrongReservation(){
		assertFalse(reservationController.addReservation(null));
	}

	@AfterClass
	public static void endTestReservationController(){
		DBController.getInstance().deleteUser(DBController.getInstance().selectUserBy(Table_User.COLUMN_USERNAME, "TestUser").get(0).getUserID());
		DBController.getInstance().deleteUser(DBController.getInstance().selectUserBy(Table_User.COLUMN_USERNAME, "TestUserPart").get(0).getUserID());
		DBController.getInstance().deleteRoom(DBController.getInstance().selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, 333).get(0).getRoomID());
		DBController.getInstance().deleteRoom(DBController.getInstance().selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, 334).get(0).getRoomID());
	}
}
