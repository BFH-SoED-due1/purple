/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController;
import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_Room;
import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_User;
import ch.bfh.ti.soed.hs16.srs.purple.controller.ReservationController;
import ch.bfh.ti.soed.hs16.srs.purple.model.Room;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;

public class ReservationControllerTest
{

	/*
	@Test
	public void RCTest()
	{
		ReservationController rc = new ReservationController();
		assertNotNull(rc);
	}*/
	
	static ReservationController reservationController;
	
	@BeforeClass
	public static void testReservationController(){
		User aebip1 = DBController.getInstance().selectUserBy(Table_User.COLUMN_USERNAME, "aebip1").get(0);
		reservationController = new ReservationController(aebip1);
		assertNotNull(reservationController);
	}
	
	@Test
	public void testAddReservation(){
		Room room10 = DBController.getInstance().selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, 10).get(0);
		assertTrue(reservationController.addReservation(new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()+3600000), room10, "test add Reservation", "Reservation Added!", DBController.getInstance().selectUserBy(Table_User.COLUMN_USERNAME, "gestl1")));
	}

}
