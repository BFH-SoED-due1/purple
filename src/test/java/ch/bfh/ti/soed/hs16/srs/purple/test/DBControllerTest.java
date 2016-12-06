/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController;
import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_Function;
import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_Reservation;
import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_Role;
import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_Room;
import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_User;
import ch.bfh.ti.soed.hs16.srs.purple.model.Function;
import ch.bfh.ti.soed.hs16.srs.purple.model.HostUser;
import ch.bfh.ti.soed.hs16.srs.purple.model.Reservation;
import ch.bfh.ti.soed.hs16.srs.purple.model.Role;
import ch.bfh.ti.soed.hs16.srs.purple.model.Room;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;

public class DBControllerTest {

	@BeforeClass
	public static void testSingleton() {
		assertNotNull(DBController.getInstance());
	}

	// --- SELECT ---
//	@Test
//	public void TestselectReservationsForUser(){
//		DBController controller = DBController.getInstance();
//		// Insert test user
//		controller.insertNewUser("fTest", "lTest", "eTest", "testUsername1", "pw", null, null);
//		User testUser = controller.selectUserBy(Table_User.COLUMN_USERNAME, "testUsername1").get(0);
//		// Insert test room
//		controller.insertNewRoom(999, "Test Room", 15);
//		Room testRoom = controller.selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, 999).get(0);
//		// Insert test reservations for test user
//		Timestamp startDate = Timestamp.valueOf("2016-12-08 08:00:00.000000");
//		Timestamp endDate = Timestamp.valueOf("2016-12-08 08:00:00.000000");
//		ArrayList<User> hosts = new ArrayList<User>();
//		hosts.add(testUser);
//		// Insert 3 reservations in which the test user is a host
//		controller.insertNewReservation(startDate, endDate, testRoom, hosts, null, "Test Reservation 777", "Test Description 777");
//		controller.insertNewReservation(startDate, endDate, testRoom, hosts, null, "Test Reservation 888", "Test Description 888");
//		controller.insertNewReservation(startDate, endDate, testRoom, hosts, null, "Test Reservation 999", "Test Description 999");
//		// Insert 2 reservations in which the test user is only a participant
//		controller.insertNewReservation(startDate, endDate, testRoom, null, hosts, "Test Reservation 888", "Test Description 888");
//		controller.insertNewReservation(startDate, endDate, testRoom, null, hosts, "Test Reservation 999", "Test Description 999");
//		
//		List<Reservation> hostReservations = controller.selectReservationsForUser(testUser, true);
//		assertTrue(hostReservations.size() == 3);
//		for(Reservation reservation : hostReservations){
//			assertTrue(reservation.getParticipantList().isEmpty());
//			assertTrue(reservation.getHostList().size() == 1);
//			assertTrue(reservation.getHostList().get(0).getUserID() == testUser.getUserID());
//		}
//		
//		List<Reservation> participantReservations = controller.selectReservationsForUser(testUser, false);
//		assertTrue(participantReservations.size() == 2);
//		for(Reservation reservation : participantReservations){
//			assertTrue(reservation.getHostList().isEmpty());
//			assertTrue(reservation.getParticipantList().size() == 1);
//			assertTrue(reservation.getParticipantList().get(0).getUserID() == testUser.getUserID());
//		}
//		// Delete reservations
//		for(Reservation reservation : controller.selectReservationsForUser(testUser, true)){
//			assertTrue(controller.deleteReservation(reservation.getReservationID()));
//		}
//		for(Reservation reservation : controller.selectReservationsForUser(testUser, false)){
//			assertTrue(controller.deleteReservation(reservation.getReservationID()));
//		}
//		// Delete room
//		assertTrue(controller.deleteRoom(testRoom.getRoomID()));
//		// Delete test user
//		assertTrue(controller.deleteUser(testUser.getUserID()));
//	}
	
	@Test
	public void testInsertNewRole(){
		DBController controller = DBController.getInstance();
		assertTrue(controller.insertNewRole("Test Role 999"));
		assertTrue(controller.deleteRole(controller.selectRoleBy(Table_Role.COLUMN_ROLE, "Test Role 999").get(0).getId()));
	}
	
	@Test
	public void testInsertNewFunction(){
		DBController controller = DBController.getInstance();
		assertTrue(controller.insertNewFunction("Test Function 999"));
		assertTrue(controller.deleteFunction(controller.selectFunctionBy(Table_Function.COLUMN_FUNCTION, "Test Function 999").get(0).getId()));
	}
	
	@Test
	public void testSelectFunctionByID(){
		DBController controller = DBController.getInstance();
		List<Function> functions = controller.selectFunctionBy(Table_Function.COLUMN_ID,9);

		assertTrue(functions.size() == 1);
		assertTrue(functions.get(0).getId() == 9);
		assertTrue(functions.get(0).getFunction().equals("Dozent"));
	}

	@Test
	public void testSelectFunctionByFunction(){
		DBController controller = DBController.getInstance();
		List<Function> functions = controller.selectFunctionBy(Table_Function.COLUMN_FUNCTION,"Student");

		boolean hasStudentFunction = false;
		for(Function function : functions) if(function.getFunction().equals("Student")) hasStudentFunction = true;
		assertTrue(hasStudentFunction);
	}

	@Test
	public void testSelectRoleByID(){
		DBController controller = DBController.getInstance();
		List<Role> roles = controller.selectRoleBy(Table_Role.COLUMN_ID, 1);

		assertTrue(roles.get(0).getId() == 1);
		assertTrue(roles.get(0).getRole().equals("Admin"));
	}

	@Test
	public void testSelectRoleByRole(){
		DBController controller = DBController.getInstance();
		List<Role> roles = controller.selectRoleBy(Table_Role.COLUMN_ROLE, "Member");

		boolean hasMemberRole = false;
		for(Role role : roles) if(role.getRole().equals("Member")) hasMemberRole = true;
		assertTrue(hasMemberRole);
	}

	@Test
	public void testSelectRoomByID(){
		DBController controller = DBController.getInstance();
		List<Room> rooms = controller.selectRoomBy(Table_Room.COLUMN_ID, 2);

		assertTrue(rooms.size() == 1);
		assertTrue(rooms.get(0).getRoomID() == 2);
		assertTrue(rooms.get(0).getRoomNumber() == 11);
		assertTrue(rooms.get(0).getName().equals("Raum11"));
		assertTrue(rooms.get(0).getNumberOfSeats() == 17);
	}

	@Test
	public void testSelectRoomByRoomNumber(){
		DBController controller = DBController.getInstance();
		List<Room> rooms = controller.selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, 10);

		boolean hasRoomNumber = false;
		for(Room room : rooms) if(room.getRoomNumber() == 10) hasRoomNumber = true;
		assertTrue(hasRoomNumber);
	}

	@Test
	public void testSelectRoomByName(){
		DBController controller = DBController.getInstance();
		List<Room> rooms = controller.selectRoomBy(Table_Room.COLUMN_NAME, "Raum99");

		boolean hasRoomName = false;
		for(Room room : rooms) if(room.getName().equals("Raum99")) hasRoomName = true;
		assertTrue(hasRoomName);
	}

	@Test
	public void testSelectRoomByNumberOfSeats(){
		DBController controller = DBController.getInstance();
		List<Room> rooms = controller.selectRoomBy(Table_Room.COLUMN_NUMBEROFSEATS, 17);

		boolean hasNumberOfSeats = false;
		for(Room room : rooms) if(room.getNumberOfSeats() == 17) hasNumberOfSeats = true;
		assertTrue(hasNumberOfSeats);
		assertTrue(rooms.size() >= 2);
	}

	@Test
	public void testSelectUserByID(){
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectUserBy(Table_User.COLUMN_ID, 4);

		assertTrue(users.size() == 1);
		assertTrue(users.get(0).getClass().equals(HostUser.class));
		assertTrue(users.get(0).getFirstName().equals("Patrik"));
		assertTrue(users.get(0).getLastName().equals("Aebischer"));
		assertTrue(users.get(0).getEmailAddress().equals("patrik.aebischer@students.bfh.ch"));
		assertTrue(users.get(0).getUsername().equals("aebip1"));
		assertTrue(users.get(0).getPassword().equals("aebip1"));
		assertTrue(((HostUser)users.get(0)).getFunction().getId() == 8);
		assertTrue(((HostUser)users.get(0)).getFunction().getFunction().equals("Student"));
		assertTrue(users.get(0).getRole().getId() == 1);
		assertTrue(users.get(0).getRole().getRole().equals("Admin"));
	}

	@Test
	public void testSelectUserByFirstName(){
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectUserBy(Table_User.COLUMN_FIRSTNAME, "Elias");

		boolean hasFirstName = false;
		for(User user : users) if(user.getFirstName().equals("Elias")) hasFirstName = true;
		assertTrue(hasFirstName);
	}

	@Test
	public void testSelectUserByLastName(){
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectUserBy(Table_User.COLUMN_LASTNAME, "Gestach");

		boolean hasLastName = false;
		for(User user : users) if(user.getLastName().equals("Gestach")) hasLastName = true;
		assertTrue(hasLastName);
	}

	@Test
	public void testSelectUserByEmail(){
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectUserBy(Table_User.COLUMN_EMAIL, "elia.boesiger@students.bfh.ch");

		boolean hasEmail = false;
		for(User user : users) if(user.getEmailAddress().equals("elia.boesiger@students.bfh.ch")) hasEmail = true;
		assertTrue(hasEmail);
	}

	@Test
	public void testSelectUserByUsername(){
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectUserBy(Table_User.COLUMN_USERNAME, "boesi1");

		boolean hasUsername = false;
		for(User user : users) if(user.getUsername().equals("boesi1")) hasUsername = true;
		assertTrue(hasUsername);
	}

	@Test
	public void testSelectUserByPassword(){
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectUserBy(Table_User.COLUMN_PASSWORD, "pTest");

		boolean hasPassword = false;
		for(User user : users){
			if(user.getPassword().equals("pTest")){
				hasPassword = true;
				assertTrue(user.getClass().equals(User.class));
				assertTrue(user.getRole() == null);
			}
		}
		assertTrue(hasPassword);
	}

	@Test
	public void testSelectUserByFunctionID(){
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectUserBy(Table_User.COLUMN_FUNCTIONID, 8);

		boolean hasFunctionID = false;
		for(User user : users){
			assertTrue(user.getClass().equals(HostUser.class));
			if(((HostUser)user).getFunction().getId() == 8) hasFunctionID = true;
		}
		assertTrue(hasFunctionID);
		assertTrue(users.size() >= 4);

		users = controller.selectUserBy(Table_User.COLUMN_FUNCTIONID, null);
		for(User user : users){
			assertTrue(user.getClass().equals(User.class));
			hasFunctionID = false;
		}
		assertFalse(hasFunctionID);
		assertTrue(users.size() >= 1);
	}

	@Test
	public void testSelectUserByRoleID(){
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectUserBy(Table_User.COLUMN_ROLEID, null);

		boolean hasRoleID = false;
		for(User user : users) if(user.getRole() == null) hasRoleID = true;
		assertTrue(hasRoleID);

		users = controller.selectUserBy(Table_User.COLUMN_ROLEID, 1);
		hasRoleID = false;
		for(User user : users) if(user.getRole().getId() == 1) hasRoleID = true;
		assertTrue(hasRoleID);
		assertTrue(users.size() >= 4);
	}

	@Test
	public void testSelectReservationByID(){
		DBController controller = DBController.getInstance();
		List<Reservation> reservations = controller.selectReservationBy(Table_Reservation.COLUMN_ID, 1);

		assertTrue(reservations.size() == 1);
		Timestamp toCompareStart = Timestamp.valueOf("2016-12-05 08:00:00.000000");
		Timestamp toCompareEnd = Timestamp.valueOf("2016-12-05 12:00:00.000000");
		assertTrue(reservations.get(0).getStartDate().getTime() == toCompareStart.getTime());
		assertTrue(reservations.get(0).getEndDate().getTime() == toCompareEnd.getTime());
		assertTrue(reservations.get(0).getRoom().getRoomID() == 1);
		assertTrue(reservations.get(0).getHostList().size() == 2);
		assertTrue(reservations.get(0).getParticipantList().size() == 2);
		assertTrue(reservations.get(0).getTitle().equals("Reservation 1"));
		assertTrue(reservations.get(0).getDescription().equals("Description 1"));
	}

	@Test
	public void testSelectAllFunctions(){
		DBController controller = DBController.getInstance();
		List<Function> functions = controller.selectAllFunctions();

		assertTrue(functions.size() >= 2);
	}

	@Test
	public void testSelectAllRoles(){
		DBController controller = DBController.getInstance();
		List<Role> roles = controller.selectAllRoles();

		assertTrue(roles.size() >= 2);
	}

	@Test
	public void testSelectAllRooms(){
		DBController controller = DBController.getInstance();
		List<Room> rooms = controller.selectAllRooms();

		assertTrue(rooms.size() >= 4);
	}

	@Test
	public void testSelectAllUsers(){
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectAllUsers();

		assertTrue(users.size() >= 5);
	}

	@Test
	public void testSelectAllReservations(){
		DBController controller = DBController.getInstance();
		List<Reservation> reservations = controller.selectAllReservations();

		assertTrue(reservations.size() >= 2);
	}

	// --- INSERT ---
	@Test
	public void testInsertNewReservation(){
		// TODO: dont insert new reservation for same date and room
		DBController controller = DBController.getInstance();
		Timestamp startDate = Timestamp.valueOf("2016-12-08 08:00:00.000000");
		Timestamp endDate = Timestamp.valueOf("2016-12-08 08:00:00.000000");
		Room room = DBController.getInstance().selectRoomBy(Table_Room.COLUMN_ID, 1).get(0);
		List<User> hosts = DBController.getInstance().selectUserBy(Table_User.COLUMN_ID, 1);
		List<User> participants = DBController.getInstance().selectUserBy(Table_User.COLUMN_ID, 2);
		String title = "Test Title";
		String description = "Test Description";
		assertTrue(controller.insertNewReservation(startDate, endDate, room, hosts, participants, title, description));
		// Delete test reservation
		for(Reservation reservation : controller.selectReservationBy(Table_Reservation.COLUMN_TITLE, "Test Title")){
			assertTrue(controller.deleteReservation(reservation.getReservationID()));
		}
		// Insert wrong reservation
		room.setRoomID(-1);
		assertFalse(controller.insertNewReservation(startDate, endDate, room, hosts, participants, title, description));
	}
	
	@Test
	public void testInsertNewUser(){
		DBController controller = DBController.getInstance();
		assertTrue(controller.insertNewUser("fName", "lName", "email", "username", "", null, null));
		assertTrue(controller.deleteUser(controller.selectUserBy(Table_User.COLUMN_USERNAME, "username").get(0).getUserID()));
	}
	
	// --- DELETE ---
	@Test
	public void testDeleteReservation(){
		DBController controller = DBController.getInstance();
		
		// Insert test reservation
		Timestamp startDate = Timestamp.valueOf("2016-12-08 08:00:00.000000");
		Timestamp endDate = Timestamp.valueOf("2016-12-08 08:00:00.000000");
		Room room = DBController.getInstance().selectRoomBy(Table_Room.COLUMN_ID, 1).get(0);
		List<User> hosts = DBController.getInstance().selectUserBy(Table_User.COLUMN_ID, 1);
		List<User> participants = DBController.getInstance().selectUserBy(Table_User.COLUMN_ID, 2);
		String title = "Test Title";
		String description = "Test Description";
		assertTrue(controller.insertNewReservation(startDate, endDate, room, hosts, participants, title, description));
		
		// Delete test reservation
		for(Reservation reservation : controller.selectReservationBy(Table_Reservation.COLUMN_TITLE, "Test Title")){
			assertTrue(controller.deleteReservation(reservation.getReservationID()));
		}
	}
	
	// --- ROW ---
	@Test
	public void testNullResultSet(){
		DBController controller = DBController.getInstance();
		assertTrue(controller.selectFunctionBy(Table_Function.COLUMN_ID, -1).size() == 0);
	}
	
	// --- ENUMS ---
	@Test
	public void testDBEnums(){
		// Function
		assertTrue(Table_Function.values().length == 3);
		assertTrue(Table_Function.valueOf(Table_Function.class, "CLOUMN_ALL").equals(Table_Function.CLOUMN_ALL));
		assertTrue(Table_Function.valueOf("CLOUMN_ALL").equals(Table_Function.CLOUMN_ALL));
		assertTrue(Table_Function.valueOf(Table_Function.class, "COLUMN_ID").equals(Table_Function.COLUMN_ID));
		assertTrue(Table_Function.valueOf("COLUMN_ID").equals(Table_Function.COLUMN_ID));
		assertTrue(Table_Function.valueOf(Table_Function.class, "COLUMN_FUNCTION").equals(Table_Function.COLUMN_FUNCTION));
		assertTrue(Table_Function.valueOf("COLUMN_FUNCTION").equals(Table_Function.COLUMN_FUNCTION));
		// Role
		assertTrue(Table_Role.values().length == 3);
		assertTrue(Table_Role.valueOf(Table_Role.class, "CLOUMN_ALL").equals(Table_Role.CLOUMN_ALL));
		assertTrue(Table_Role.valueOf("CLOUMN_ALL").equals(Table_Role.CLOUMN_ALL));
		assertTrue(Table_Role.valueOf(Table_Role.class, "COLUMN_ID").equals(Table_Role.COLUMN_ID));
		assertTrue(Table_Role.valueOf("COLUMN_ID").equals(Table_Role.COLUMN_ID));
		assertTrue(Table_Role.valueOf(Table_Role.class, "COLUMN_ROLE").equals(Table_Role.COLUMN_ROLE));
		assertTrue(Table_Role.valueOf("COLUMN_ROLE").equals(Table_Role.COLUMN_ROLE));
		// Room
		assertTrue(Table_Room.values().length == 5);
		assertTrue(Table_Room.valueOf(Table_Room.class, "CLOUMN_ALL").equals(Table_Room.CLOUMN_ALL));
		assertTrue(Table_Room.valueOf("CLOUMN_ALL").equals(Table_Room.CLOUMN_ALL));
		assertTrue(Table_Room.valueOf(Table_Room.class, "COLUMN_ID").equals(Table_Room.COLUMN_ID));
		assertTrue(Table_Room.valueOf("COLUMN_ID").equals(Table_Room.COLUMN_ID));
		assertTrue(Table_Room.valueOf(Table_Room.class, "COLUMN_ROOMNUMBER").equals(Table_Room.COLUMN_ROOMNUMBER));
		assertTrue(Table_Room.valueOf("COLUMN_ROOMNUMBER").equals(Table_Room.COLUMN_ROOMNUMBER));
		assertTrue(Table_Room.valueOf(Table_Room.class, "COLUMN_NAME").equals(Table_Room.COLUMN_NAME));
		assertTrue(Table_Room.valueOf("COLUMN_NAME").equals(Table_Room.COLUMN_NAME));
		assertTrue(Table_Room.valueOf(Table_Room.class, "COLUMN_NUMBEROFSEATS").equals(Table_Room.COLUMN_NUMBEROFSEATS));
		assertTrue(Table_Room.valueOf("COLUMN_NUMBEROFSEATS").equals(Table_Room.COLUMN_NUMBEROFSEATS));
		// User
		assertTrue(Table_User.values().length == 9);
		assertTrue(Table_User.valueOf(Table_User.class, "CLOUMN_ALL").equals(Table_User.CLOUMN_ALL));
		assertTrue(Table_User.valueOf("CLOUMN_ALL").equals(Table_User.CLOUMN_ALL));
		assertTrue(Table_User.valueOf(Table_User.class, "COLUMN_ID").equals(Table_User.COLUMN_ID));
		assertTrue(Table_User.valueOf("COLUMN_ID").equals(Table_User.COLUMN_ID));
		assertTrue(Table_User.valueOf(Table_User.class, "COLUMN_FIRSTNAME").equals(Table_User.COLUMN_FIRSTNAME));
		assertTrue(Table_User.valueOf("COLUMN_FIRSTNAME").equals(Table_User.COLUMN_FIRSTNAME));
		assertTrue(Table_User.valueOf(Table_User.class, "COLUMN_LASTNAME").equals(Table_User.COLUMN_LASTNAME));
		assertTrue(Table_User.valueOf("COLUMN_LASTNAME").equals(Table_User.COLUMN_LASTNAME));
		assertTrue(Table_User.valueOf(Table_User.class, "COLUMN_EMAIL").equals(Table_User.COLUMN_EMAIL));
		assertTrue(Table_User.valueOf("COLUMN_EMAIL").equals(Table_User.COLUMN_EMAIL));
		assertTrue(Table_User.valueOf(Table_User.class, "COLUMN_USERNAME").equals(Table_User.COLUMN_USERNAME));
		assertTrue(Table_User.valueOf("COLUMN_USERNAME").equals(Table_User.COLUMN_USERNAME));
		assertTrue(Table_User.valueOf(Table_User.class, "COLUMN_PASSWORD").equals(Table_User.COLUMN_PASSWORD));
		assertTrue(Table_User.valueOf("COLUMN_PASSWORD").equals(Table_User.COLUMN_PASSWORD));
		assertTrue(Table_User.valueOf(Table_User.class, "COLUMN_FUNCTIONID").equals(Table_User.COLUMN_FUNCTIONID));
		assertTrue(Table_User.valueOf("COLUMN_FUNCTIONID").equals(Table_User.COLUMN_FUNCTIONID));
		assertTrue(Table_User.valueOf(Table_User.class, "COLUMN_ROLEID").equals(Table_User.COLUMN_ROLEID));
		assertTrue(Table_User.valueOf("COLUMN_ROLEID").equals(Table_User.COLUMN_ROLEID));
		// Reservation
		assertTrue(Table_Reservation.values().length == 7);
		assertTrue(Table_Reservation.valueOf(Table_Reservation.class, "CLOUMN_ALL").equals(Table_Reservation.CLOUMN_ALL));
		assertTrue(Table_Reservation.valueOf("CLOUMN_ALL").equals(Table_Reservation.CLOUMN_ALL));
		assertTrue(Table_Reservation.valueOf(Table_Reservation.class, "COLUMN_ID").equals(Table_Reservation.COLUMN_ID));
		assertTrue(Table_Reservation.valueOf("COLUMN_ID").equals(Table_Reservation.COLUMN_ID));
		assertTrue(Table_Reservation.valueOf(Table_Reservation.class, "COLUMN_STARTDATE").equals(Table_Reservation.COLUMN_STARTDATE));
		assertTrue(Table_Reservation.valueOf("COLUMN_STARTDATE").equals(Table_Reservation.COLUMN_STARTDATE));
		assertTrue(Table_Reservation.valueOf(Table_Reservation.class, "COLUMN_ENDDATE").equals(Table_Reservation.COLUMN_ENDDATE));
		assertTrue(Table_Reservation.valueOf("COLUMN_ENDDATE").equals(Table_Reservation.COLUMN_ENDDATE));
		assertTrue(Table_Reservation.valueOf(Table_Reservation.class, "COLUMN_ROOMID").equals(Table_Reservation.COLUMN_ROOMID));
		assertTrue(Table_Reservation.valueOf("COLUMN_ROOMID").equals(Table_Reservation.COLUMN_ROOMID));
		assertTrue(Table_Reservation.valueOf(Table_Reservation.class, "COLUMN_TITLE").equals(Table_Reservation.COLUMN_TITLE));
		assertTrue(Table_Reservation.valueOf("COLUMN_TITLE").equals(Table_Reservation.COLUMN_TITLE));
		assertTrue(Table_Reservation.valueOf(Table_Reservation.class, "COLUMN_DESCRIPTION").equals(Table_Reservation.COLUMN_DESCRIPTION));
		assertTrue(Table_Reservation.valueOf("COLUMN_DESCRIPTION").equals(Table_Reservation.COLUMN_DESCRIPTION));
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
		// Reconnect for further tests
		try {
			DBController.getInstance().connect();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
