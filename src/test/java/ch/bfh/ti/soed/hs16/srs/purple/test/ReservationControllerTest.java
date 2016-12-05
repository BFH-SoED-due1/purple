/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.test;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	
	static ReservationController reservationController;
	private Timestamp startTime1 = new Timestamp(new Date().getTime());
	private Timestamp endTime1 = new Timestamp(new Date().getTime()+3600000);
	
	@BeforeClass
	public static void testReservationController(){
		User aebip1 = DBController.getInstance().selectUserBy(Table_User.COLUMN_USERNAME, "aebip1").get(0);
		reservationController = new ReservationController(aebip1);
		assertNotNull(reservationController);
	}
	
	@Test
	public void testAddReservation(){
		Room room10 = DBController.getInstance().selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, 10).get(0);
		assertTrue(reservationController.addReservation(startTime1, endTime1, room10, "test add Reservation", "Reservation Added!", DBController.getInstance().selectUserBy(Table_User.COLUMN_USERNAME, "gestl1")));
		for(Reservation reservation : DBController.getInstance().selectReservationBy(DBController.Table_Reservation.COLUMN_TITLE, "test add Reservation")){
			assertTrue(reservationController.deleteReservation(reservation.getReservationID()));
		}
	}
	
	@Test
	public void testDeleteReservation(){
		int resID = 0;
		List<Reservation> resList = new ArrayList<Reservation>(DBController.getInstance().selectReservationBy(Table_Reservation.COLUMN_ROOMID, "room10"));
		for(int i = 0; i < resList.size(); i++){
			if(resList.get(i).getStartDate() == startTime1){
				resID = resList.get(i).getReservationID();
			}
		}
		assertTrue(reservationController.deleteReservation(resID));
	}
	
	@Test
	public void testWrongReservation(){
		assertFalse(reservationController.addReservation(null, null, null, null, null, null));
	}
}
