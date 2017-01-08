/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
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
	private Timestamp startTime2 = new Timestamp(new Date().getTime()+1000000);
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
		testUserPartList.add(testUserPart);
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

	@Test
	public void testUpdateReservation(){
		reservationController.addReservation(new Reservation(-1, startTime1, endTime1, testRoom2, "Test add Reservation", "Reservation added!", testUserHostList));
		Reservation res = DBController.getInstance().selectReservationBy(Table_Reservation.COLUMN_ROOMID, testRoom2.getRoomID()).get(0);
		res.setStart(startTime2);
		assertTrue(reservationController.updateReservation(res));
	}

	@Test
	public void testAcceptReservation(){
		Reservation res = new Reservation(-1, startTime1, endTime1, testRoom1, "Test add Reservation", "Reservation added!", testUserHostList, testUserPartList);
		reservationController.addReservation(res);
		res = DBController.getInstance().selectReservationBy(Table_Reservation.COLUMN_ROOMID, testRoom1.getRoomID()).get(0);
		reservationController.acceptReservation(testUserPart, res);
		res = DBController.getInstance().selectReservationBy(Table_Reservation.COLUMN_ROOMID, testRoom1.getRoomID()).get(0);
		assertEquals(res.getAcceptedParticipantsList().get(0).getUsername(), "TestUserPart");
		reservationController.deleteReservation(res.getReservationID());
	}

	@Test
	public void testCancelReservation(){
		Reservation res = new Reservation(-1, startTime1, endTime1, testRoom1, "Test add Reservation", "Reservation added!", testUserHostList, testUserPartList);
		reservationController.addReservation(res);
		res = DBController.getInstance().selectReservationBy(Table_Reservation.COLUMN_ROOMID, testRoom1.getRoomID()).get(0);
		reservationController.cancelReservation(testUserPart, res);
		res = DBController.getInstance().selectReservationBy(Table_Reservation.COLUMN_ROOMID, testRoom1.getRoomID()).get(0);
		assertEquals(res.getAcceptedParticipantsList().size(), 0);
		reservationController.deleteReservation(res.getReservationID());
	}

	@Test
	public void testGetAllRooms(){
		assertNotNull(reservationController.getAllRooms());
	}

	@Test
	public void testGetAllReservationsFromRoom(){
		reservationController.addReservation(new Reservation(-1, startTime1, endTime1, testRoom1, "Test add Reservation", "Reservation added!", testUserHostList, testUserPartList));
		assertEquals(reservationController.getAllReservationsFromRoom(testRoom1.getRoomID()).size(), 1);
		//reservationController.deleteReservation(DBController.getInstance().selectReservationBy(Table_Reservation.COLUMN_ROOMID, testRoom1.getRoomID()).get(0).getReservationID());
	}

	@Test
	public void testGetAllReservationsFromUser(){
		assertEquals(reservationController.getAllReservationsFromUser(testUserHost).size(),1);
	}

	@Test
	public void testGetAllUsers(){
		assertNotNull(reservationController.getAllUsers());
	}

	@Test
	public void testGetSessionUser(){
		assertEquals(reservationController.getSessionUser("TestUser").getUsername(), DBController.getInstance().selectUserBy(Table_User.COLUMN_ID, testUserHost.getUserID()).get(0).getUsername());
	}

	@Test
	public void testGetAllReservations(){
		assertNotNull(reservationController.getAllReservations());
		
		List<Reservation> reservations = DBController.getInstance().selectReservationBy(Table_Reservation.COLUMN_ROOMID, testRoom1.getRoomID());
		
		for(Reservation reservation : reservations) {
			assertTrue(reservationController.deleteReservation(reservation.getReservationID()));
		}
	}

	@Test
	public void testGetAllFreeRooms(){
		reservationController.addReservation(new Reservation(-1, startTime1, endTime1, testRoom1, "Test add Reservation", "Reservation added!", testUserHostList));
		assertNotNull(reservationController.getAllFreeRooms(startTime1, endTime1));
		reservationController.deleteReservation(DBController.getInstance().selectReservationBy(Table_Reservation.COLUMN_ROOMID, testRoom1.getRoomID()).get(0).getReservationID());
	}

	@Test
	public void testGetRoom(){
		assertNotNull(reservationController.getRoom(testRoom1.getRoomID()));
	}

	@AfterClass
	public static void endTestReservationController(){
		DBController.getInstance().deleteUser(DBController.getInstance().selectUserBy(Table_User.COLUMN_USERNAME, "TestUser").get(0).getUserID());
		DBController.getInstance().deleteUser(DBController.getInstance().selectUserBy(Table_User.COLUMN_USERNAME, "TestUserPart").get(0).getUserID());
		DBController.getInstance().deleteRoom(DBController.getInstance().selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, 333).get(0).getRoomID());
		DBController.getInstance().deleteRoom(DBController.getInstance().selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, 334).get(0).getRoomID());
	}
}
