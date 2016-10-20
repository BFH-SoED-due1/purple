package ch.bfh.ti.soed.hs16.srs.purple.model;

import java.util.List;
/**
 * Eine Reservation definiert eine Reservation, die von einem Veranstalter erstellt wurde. 
 * Eine Reservation beinhaltet einen Raum, ein Start- und End-Datum, sowie die Veranstalter und die Teilnehmer. 
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas, Schildknecht Elias
 * @date 20.10.2016
 * @version 1.0
 *
 */
public class Reservation {
	private int reservationID;
	private Room room;
	private long startDate;
	private long endDate;
	private List<User> hostList;
	private List<User> participantList;
	
	public Room getRoom()
	{
		return room;
	}
	public void setRoom(Room room)
	{
		this.room = room;
	}
}
