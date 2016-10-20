package ch.bfh.ti.soed.hs16.srs.purple.model;

import java.util.List;

/**
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas, Schildknecht Elias
 *
 */
public class Room {
	private int roomID;
	private int roomNumber;
	private int numberOfSeats;
	private List<Equipment> equipmentList;
	private List<Reservation> reservationList;
}
