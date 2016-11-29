/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController;
import ch.bfh.ti.soed.hs16.srs.purple.model.Function;
import ch.bfh.ti.soed.hs16.srs.purple.model.Reservation;
import ch.bfh.ti.soed.hs16.srs.purple.model.Role;
import ch.bfh.ti.soed.hs16.srs.purple.model.Room;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;

public class DBControllerTest {
	
	@BeforeClass
	public static void testSingleton() {
		assertNotNull(DBController.getInstance());
	}
	
	@Test
	public void testSelectAllFunctions(){
		DBController controller = DBController.getInstance();
		List<Function> functions = controller.selectAllFunctions();
		
		assertTrue(functions.get(0).getFunction().equals("Dozent"));
		assertTrue(functions.size() == 2);
	}
	
	@Test
	public void testSelectAllRoles(){
		DBController controller = DBController.getInstance();
		List<Role> roles = controller.selectAllRoles();
		
		assertNotNull(roles);
		assertTrue(roles.size() == 2);
	}
	
	@Test
	public void testSelectAllRooms(){
		DBController controller = DBController.getInstance();
		List<Room> rooms = controller.selectAllRooms();
		
		assertNotNull(rooms);
		assertTrue(rooms.size() == 3);
	}
	
	@Test
	public void testSelectAllUsers(){
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectAllUsers();
		
		assertNotNull(users);
		assertTrue(users.size() == 5);
	}
	
	@Test
	public void testSelectAllReservations(){
		DBController controller = DBController.getInstance();
		List<Reservation> reservations = controller.selectAllReservations();
		
		assertNotNull(reservations);
		assertTrue(reservations.size() == 2);
		assertTrue(reservations.get(0).getParticipantList().size() == 2);
		assertTrue(reservations.get(0).getHostList().size() == 2);
		assertTrue(reservations.get(0).getHostList().get(0).getFirstName().equals("Elias"));
	}
	
	@Test
	public void testSelectFunctionByID(){
		DBController controller = DBController.getInstance();
		Function function = controller.selectFunctionByID(9);
		
		assertTrue(function.getId() == 9);
		assertTrue(function.getFunction().equals("Dozent"));
	}
	
	@Test
	public void testSelectRoleByID(){
		DBController controller = DBController.getInstance();
		Role role = controller.selectRoleByID(1);
		
		assertTrue(role.getId() == 1);
		assertTrue(role.getRole().equals("Admin"));
	}
	
	@Test
	public void testSelectRoomByID(){
		DBController controller = DBController.getInstance();
		Room room = controller.selectRoomByID(2);
		
		assertTrue(room.getRoomID() == 2);
		assertTrue(room.getRoomNumber() == 11);
		assertTrue(room.getName().equals("Raum11"));
		assertTrue(room.getNumberOfSeats() == 17);
	}
	
	@Test
	public void testSelectUserByID(){
		DBController controller = DBController.getInstance();
		User user = controller.selectUserByID(1);
		
		assertTrue(user.getUserID() == 1);
		assertTrue(user.getFirstName().equals("Elias"));
		assertTrue(user.getLastName().equals("Schildknecht"));
		assertTrue(user.getEmailAddress().equals("elias.schildknecht@students.bfh.ch"));
		assertTrue(user.getUsername().equals("schie5"));
		assertTrue(user.getPassword().equals("schie5"));
		assertTrue(user.getRole().getId() == 1);
		assertTrue(user.getRole().getRole().equals("Admin"));
		// TODO: HostUser needed?
//		((HostUser)user).getFunction().getId() == 8;
	}
	
	@Test
	public void testSelectUserByUsername(){
		DBController controller = DBController.getInstance();
		User user = controller.selectUserByUsername("schie5");
		
		assertTrue(user.getUserID() == 1);
		assertTrue(user.getFirstName().equals("Elias"));
		assertTrue(user.getLastName().equals("Schildknecht"));
		assertTrue(user.getEmailAddress().equals("elias.schildknecht@students.bfh.ch"));
		assertTrue(user.getUsername().equals("schie5"));
		assertTrue(user.getPassword().equals("schie5"));
		assertTrue(user.getRole().getId() == 1);
		assertTrue(user.getRole().getRole().equals("Admin"));
		// TODO: HostUser needed?
//		((HostUser)user).getFunction().getId() == 8;
	}
	
	@AfterClass
	public static void disconnect() throws SQLException{
		boolean disconnected = false;
		try {
			DBController.getInstance().disconnect();
			disconnected = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertTrue(disconnected);
	}

}
