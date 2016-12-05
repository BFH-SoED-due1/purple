/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ch.bfh.ti.soed.hs16.srs.purple.util.ReservationAction;

public class ReservationActionTest {
	
	@Test
	public void testEnums(){
		assertTrue(ReservationAction.values().length == 4);
		assertTrue(ReservationAction.valueOf(ReservationAction.class, "INSERT").equals(ReservationAction.INSERT));
		assertTrue(ReservationAction.valueOf("INSERT").equals(ReservationAction.INSERT));
		assertTrue(ReservationAction.valueOf(ReservationAction.class, "DELETE").equals(ReservationAction.DELETE));
		assertTrue(ReservationAction.valueOf("DELETE").equals(ReservationAction.DELETE));
		assertTrue(ReservationAction.valueOf(ReservationAction.class, "NONE").equals(ReservationAction.NONE));
		assertTrue(ReservationAction.valueOf("NONE").equals(ReservationAction.NONE));
		assertTrue(ReservationAction.valueOf(ReservationAction.class, "EDIT").equals(ReservationAction.EDIT));
		assertTrue(ReservationAction.valueOf("EDIT").equals(ReservationAction.EDIT));
	}
}
