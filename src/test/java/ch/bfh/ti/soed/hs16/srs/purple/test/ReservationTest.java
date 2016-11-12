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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.bfh.ti.soed.hs16.srs.purple.model.Reservation;
import ch.bfh.ti.soed.hs16.srs.purple.model.Room;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;

public class ReservationTest
{

	@Test
	public void testSetNewReservation(){
		Room room = new Room();
		List<User> userList = new ArrayList<>();
		userList.add(new User());
		List<User> partList = new ArrayList<>();
		partList.add(new User());
		Reservation reservation = new Reservation(room, 1600, 1700, userList, partList);

		assertNotNull(reservation.getReservationID());
		// TODO: Swap the arguments in all assertEquals methods:
		// assertEquals(reservation.getRoom(), room);
		// assertEquals(reservation.getStartDate(), 1600);
		// assertEquals(reservation.getEndDate(), 1700);
		// assertEquals(reservation.getHostList(), userList);
		// assertEquals(reservation.getParticipantList(), partList);
	}

	@Test
	public void testChangeRoomOfReservation()
	{
		Room room = new Room();
		List<User> userList = new ArrayList<>();
		userList.add(new User());
		List<User> partList = new ArrayList<>();
		partList.add(new User());
		Reservation reservation = new Reservation(room, 1600, 1700, userList, partList);
		Room r = new Room();
		reservation.setRoom(r);

		assertEquals(reservation.getRoom(), r);
	}

}
