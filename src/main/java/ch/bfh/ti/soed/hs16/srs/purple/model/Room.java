package ch.bfh.ti.soed.hs16.srs.purple.model;

import java.util.List;

/**
 * Ein Raum kann reserviert werden.
 * Ein Raum besitzt eine Raumnummer, eine Anzahl an Sitzplätzen sowie unterschiedliche Gegenstände. 
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas, Schildknecht Elias
 * @date 20.10.2016
 * @version 1.0
 *
 */
public class Room {
	private int roomID;
	private int roomNumber;
	private int numberOfSeats;
	private List<Equipment> equipmentList;
	private List<Reservation> reservationList;
}
