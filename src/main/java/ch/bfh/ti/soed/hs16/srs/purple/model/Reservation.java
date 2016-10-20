package ch.bfh.ti.soed.hs16.srs.purple.model;

import java.util.List;

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
