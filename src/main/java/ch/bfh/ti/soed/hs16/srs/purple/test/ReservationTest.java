package ch.bfh.ti.soed.hs16.srs.purple.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import ch.bfh.ti.soed.hs16.srs.purple.model.Reservation;
import ch.bfh.ti.soed.hs16.srs.purple.model.Room;

public class ReservationTest
{

	@Test
	public void testNewReservation()
	{
		Reservation reservation = new Reservation();
		Room r = reservation.getRoom();
		assertNull(r);
	}
	
	@Test
	public void testRoomOfReservation()
	{
		Reservation res = new Reservation();
		Room r = new Room();
		res.setRoom(r);
		assertEquals(res.getRoom(), r);
	}

}
