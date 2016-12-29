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
import java.util.ArrayList;
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

/**
 * Tests the database controller class.
 * 
 * @author Elias Schildknecht
 * */
public class DBControllerTest {

	@BeforeClass
	public static void testSingleton() {
		assertNotNull(DBController.getInstance());
	}

	// --- SELECT ---
	@Test 
	public void testselectReservationsForUser() {
		DBController controller = DBController.getInstance();
		// Insert test user 1
		String fName = "f testselectReservationsForUser 1";
		String lName = "l testselectReservationsForUser 1";
		String email = "e testselectReservationsForUser 1";
		String uName = "u testselectReservationsForUser 1";
		String pw = "p testselectReservationsForUser 1";
		controller.insertNewUser(fName, lName, email, uName, pw, null, null);
		User testUser = controller.selectUserBy(Table_User.COLUMN_USERNAME, uName).get(0);
		ArrayList<User> hosts = new ArrayList<User>();
		hosts.add(testUser);
		
		// Insert test user 1
		fName = "f testselectReservationsForUser 2";
		lName = "l testselectReservationsForUser 2";
		email = "e testselectReservationsForUser 2";
		uName = "u testselectReservationsForUser 2";
		pw = "p testselectReservationsForUser 2";
		controller.insertNewUser(fName, lName, email, uName, pw, null, null);
		User testUser2 = controller.selectUserBy(Table_User.COLUMN_USERNAME, uName).get(0);
		ArrayList<User> hosts2 = new ArrayList<User>();
		hosts2.add(testUser2);
		
		// Insert test room
		controller.insertNewRoom(-1, "Test Room", 15);
		Room testRoom = controller.selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, -1).get(0);
		
		// Insert 3 reservations in which the test user is a host
		Timestamp startDate = Timestamp.valueOf("2016-12-08 08:00:00.000000");
		Timestamp endDate = Timestamp.valueOf("2016-12-08 09:00:00.000000");
		controller.insertNewReservation(startDate, endDate, testRoom, hosts, null, "Test Reservation Host 1", "Test Description Host 1");
		
		startDate = Timestamp.valueOf("2016-12-08 09:00:00.000000");
		endDate = Timestamp.valueOf("2016-12-08 10:00:00.000000");
		controller.insertNewReservation(startDate, endDate, testRoom, hosts, null, "Test Reservation Host 2", "Test Description Host 2");
		
		startDate = Timestamp.valueOf("2016-12-08 10:00:00.000000");
		endDate = Timestamp.valueOf("2016-12-08 11:00:00.000000");
		controller.insertNewReservation(startDate, endDate, testRoom, hosts, null, "Test Reservation Host 3", "Test Description Host 3");
		
		// Insert 2 reservations in which the test user is only a participant
		startDate = Timestamp.valueOf("2016-12-08 11:00:00.000000");
		endDate = Timestamp.valueOf("2016-12-08 12:00:00.000000");
		controller.insertNewReservation(startDate, endDate, testRoom, hosts2, hosts, "Test Reservation Participant 1", "Test Description Participant 1");
		
		startDate = Timestamp.valueOf("2016-12-08 13:00:00.000000");
		endDate = Timestamp.valueOf("2016-12-08 14:00:00.000000");
		controller.insertNewReservation(startDate, endDate, testRoom, hosts2, hosts, "Test Reservation Participant 2", "Test Description Participant 2");

		List<Reservation> hostReservations = controller.selectReservationsForUser(testUser, true);
		assertTrue(hostReservations.size() == 3);
		for (Reservation reservation : hostReservations) {
			assertTrue(reservation.getParticipantList().isEmpty());
			assertTrue(reservation.getHostList().size() == 1);
			assertTrue(reservation.getHostList().get(0).getUserID().equals(testUser.getUserID()));
		}

		List<Reservation> participantReservations = controller.selectReservationsForUser(testUser, false);
		assertTrue(participantReservations.size() == 2);
		for (Reservation reservation : participantReservations) {
			assertTrue(reservation.getHostList().size() == 1);
			assertTrue(reservation.getHostList().get(0).getUserID().equals(testUser2.getUserID()));
			assertTrue(reservation.getParticipantList().size() == 1);
			assertTrue(reservation.getParticipantList().get(0).getUserID().equals(testUser.getUserID()));
		}
		// Delete reservations
		for (Reservation reservation : controller.selectReservationsForUser(testUser, true)) {
			assertTrue(controller.deleteReservation(reservation.getReservationID()));
		}
		for (Reservation reservation : controller.selectReservationsForUser(testUser, false)) {
			assertTrue(controller.deleteReservation(reservation.getReservationID()));
		}
		
		// Delete room
		assertTrue(controller.deleteRoom(testRoom.getRoomID()));
		// Delete test user
		assertTrue(controller.deleteUser(testUser.getUserID()));
		assertTrue(controller.deleteUser(testUser2.getUserID()));
	}

	@Test
	public void testInsertNewRole() {
		DBController controller = DBController.getInstance();
		assertTrue(controller.insertNewRole("Test Role 999"));
		assertTrue(
				controller.deleteRole(controller.selectRoleBy(Table_Role.COLUMN_ROLE, "Test Role 999").get(0).getId()));
	}

	@Test
	public void testInsertNewFunction() {
		DBController controller = DBController.getInstance();
		assertTrue(controller.insertNewFunction("Test Function 999"));
		assertTrue(controller.deleteFunction(
				controller.selectFunctionBy(Table_Function.COLUMN_FUNCTION, "Test Function 999").get(0).getId()));
	}

	@Test
	public void testSelectFunctionByID() {
		DBController controller = DBController.getInstance();
		List<Function> functions = controller.selectFunctionBy(Table_Function.COLUMN_ID, 9);

		assertTrue(functions.size() == 1);
		assertTrue(functions.get(0).getId() == 9);
		assertTrue(functions.get(0).getFunction().equals("Dozent"));
	}

	@Test
	public void testSelectFunctionByFunction() {
		DBController controller = DBController.getInstance();
		List<Function> functions = controller.selectFunctionBy(Table_Function.COLUMN_FUNCTION, "Student");

		boolean hasStudentFunction = false;
		for (Function function : functions)
			if (function.getFunction().equals("Student"))
				hasStudentFunction = true;
		assertTrue(hasStudentFunction);
	}

	@Test
	public void testSelectRoleByID() {
		DBController controller = DBController.getInstance();
		List<Role> roles = controller.selectRoleBy(Table_Role.COLUMN_ID, 1);

		assertTrue(roles.get(0).getId() == 1);
		assertTrue(roles.get(0).getRole().equals("Admin"));
	}

	@Test
	public void testSelectRoleByRole() {
		DBController controller = DBController.getInstance();
		List<Role> roles = controller.selectRoleBy(Table_Role.COLUMN_ROLE, "Member");

		boolean hasMemberRole = false;
		for (Role role : roles)
			if (role.getRole().equals("Member"))
				hasMemberRole = true;
		assertTrue(hasMemberRole);
	}

	@Test
	public void testSelectRoomByID() {
		DBController controller = DBController.getInstance();
		List<Room> rooms = controller.selectRoomBy(Table_Room.COLUMN_ID, 2);

		assertTrue(rooms.size() == 1);
		assertTrue(rooms.get(0).getRoomID() == 2);
		assertTrue(rooms.get(0).getRoomNumber() == 11);
		assertTrue(rooms.get(0).getName().equals("Raum11"));
		assertTrue(rooms.get(0).getNumberOfSeats() == 17);
	}

	@Test
	public void testSelectRoomByRoomNumber() {
		DBController controller = DBController.getInstance();
		List<Room> rooms = controller.selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, 10);

		boolean hasRoomNumber = false;
		for (Room room : rooms)
			if (room.getRoomNumber() == 10)
				hasRoomNumber = true;
		assertTrue(hasRoomNumber);
	}

	@Test
	public void testSelectRoomByName() {
		DBController controller = DBController.getInstance();
		List<Room> rooms = controller.selectRoomBy(Table_Room.COLUMN_NAME, "Raum99");

		boolean hasRoomName = false;
		for (Room room : rooms)
			if (room.getName().equals("Raum99"))
				hasRoomName = true;
		assertTrue(hasRoomName);
	}

	@Test
	public void testSelectRoomByNumberOfSeats() {
		DBController controller = DBController.getInstance();
		List<Room> rooms = controller.selectRoomBy(Table_Room.COLUMN_NUMBEROFSEATS, 17);

		boolean hasNumberOfSeats = false;
		for (Room room : rooms)
			if (room.getNumberOfSeats() == 17)
				hasNumberOfSeats = true;
		assertTrue(hasNumberOfSeats);
		assertTrue(rooms.size() >= 2);
	}

	@Test
	public void testSelectUserByID() {
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectUserBy(Table_User.COLUMN_ID, 4);

		assertTrue(users.size() == 1);
		assertTrue(users.get(0).getClass().equals(HostUser.class));
		assertTrue(users.get(0).getFirstName().equals("Patrik"));
		assertTrue(users.get(0).getLastName().equals("Aebischer"));
		assertTrue(users.get(0).getEmailAddress().equals("patrik.aebischer@students.bfh.ch"));
		assertTrue(users.get(0).getUsername().equals("aebip1"));
		assertTrue(users.get(0).getPassword().equals("aebip1"));
		assertTrue(((HostUser) users.get(0)).getFunction().getId() == 8);
		assertTrue(((HostUser) users.get(0)).getFunction().getFunction().equals("Student"));
		assertTrue(users.get(0).getRole().getId() == 1);
		assertTrue(users.get(0).getRole().getRole().equals("Admin"));
	}

	@Test
	public void testSelectUserByFirstName() {
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectUserBy(Table_User.COLUMN_FIRSTNAME, "Elias");

		boolean hasFirstName = false;
		for (User user : users)
			if (user.getFirstName().equals("Elias"))
				hasFirstName = true;
		assertTrue(hasFirstName);
	}

	@Test
	public void testSelectUserByLastName() {
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectUserBy(Table_User.COLUMN_LASTNAME, "Gestach");

		boolean hasLastName = false;
		for (User user : users)
			if (user.getLastName().equals("Gestach"))
				hasLastName = true;
		assertTrue(hasLastName);
	}

	@Test
	public void testSelectUserByEmail() {
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectUserBy(Table_User.COLUMN_EMAIL, "elia.boesiger@students.bfh.ch");

		boolean hasEmail = false;
		for (User user : users)
			if (user.getEmailAddress().equals("elia.boesiger@students.bfh.ch"))
				hasEmail = true;
		assertTrue(hasEmail);
	}

	@Test
	public void testSelectUserByUsername() {
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectUserBy(Table_User.COLUMN_USERNAME, "boesi1");

		boolean hasUsername = false;
		for (User user : users)
			if (user.getUsername().equals("boesi1"))
				hasUsername = true;
		assertTrue(hasUsername);
	}

	@Test
	public void testSelectUserByPassword() {
		DBController controller = DBController.getInstance();
		controller.insertNewUser("ftestSelectUserByPasswordTest", "ltestSelectUserByPasswordTest", "eftestSelectUserByPasswordTest", "utestSelectUserByPasswordTest", "ptestSelectUserByPasswordTest", null, null);
		List<User> users = controller.selectUserBy(Table_User.COLUMN_PASSWORD, "ptestSelectUserByPasswordTest");

		boolean hasPassword = false;
		User testUser = null;
		for (User user : users) {
			if (user.getPassword().equals("ptestSelectUserByPasswordTest")) {
				hasPassword = true;
				assertTrue(user.getClass().equals(User.class));
				assertTrue(user.getRole() == null);
				testUser = user;
			}
		}
		assertTrue(hasPassword);
		controller.deleteUser(testUser.getUserID());
	}

	@Test
	public void testSelectUserByFunctionID() {
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectUserBy(Table_User.COLUMN_FUNCTIONID, 8);

		boolean hasFunctionID = false;
		for (User user : users) {
			assertTrue(user.getClass().equals(HostUser.class));
			if (((HostUser) user).getFunction().getId() == 8)
				hasFunctionID = true;
		}
		assertTrue(hasFunctionID);
		assertTrue(users.size() >= 4);

		users = controller.selectUserBy(Table_User.COLUMN_FUNCTIONID, null);
		for (User user : users) {
			assertTrue(user.getClass().equals(User.class));
			hasFunctionID = false;
		}
		assertFalse(hasFunctionID);
		assertTrue(users.size() >= 1);
	}

	@Test
	public void testSelectUserByRoleID() {
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectUserBy(Table_User.COLUMN_ROLEID, null);

		boolean hasRoleID = false;
		for (User user : users)
			if (user.getRole() == null)
				hasRoleID = true;
		assertTrue(hasRoleID);

		users = controller.selectUserBy(Table_User.COLUMN_ROLEID, 1);
		hasRoleID = false;
		for (User user : users)
			if (user.getRole().getId() == 1)
				hasRoleID = true;
		assertTrue(hasRoleID);
		assertTrue(users.size() >= 4);
	}

	@Test
	public void testSelectReservationByID() {
		DBController controller = DBController.getInstance();
		
		// Insert test host
		String fName = "ftestSelectReservationByID";
		String lName = "ltestSelectReservationByID";
		String email = "etestSelectReservationByID";
		String uName = "utestSelectReservationByID";
		String pw = "ptestSelectReservationByID";
		controller.insertNewUser(fName, lName, email, uName, pw, null, null);
		User testUser = controller.selectUserBy(Table_User.COLUMN_USERNAME, uName).get(0);
		ArrayList<User> hosts = new ArrayList<User>();
		hosts.add(testUser);
		
		// Insert test room
		assertTrue(controller.insertNewRoom(-2, "Test Room", 55));
		Room testRoom = controller.selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, -2).get(0);
		
		// Insert test reservation
		Timestamp startDate = Timestamp.valueOf("2016-12-08 08:00:00.000000");
		Timestamp endDate = Timestamp.valueOf("2016-12-08 08:00:00.000000");
		controller.insertNewReservation(startDate, endDate, testRoom, hosts, null, "testSelectReservationByID Title", "testSelectReservationByID Description");
		
		List<Reservation> reservations = controller.selectReservationBy(Table_Reservation.COLUMN_ROOMID, testRoom.getRoomID());
		assertTrue(reservations.size() == 1);
		Timestamp toCompareStart = Timestamp.valueOf("2016-12-08 08:00:00.000000");
		Timestamp toCompareEnd = Timestamp.valueOf("2016-12-08 08:00:00.000000");
		assertTrue(reservations.get(0).getStartDate().getTime() == toCompareStart.getTime());
		assertTrue(reservations.get(0).getEndDate().getTime() == toCompareEnd.getTime());
		assertTrue(reservations.get(0).getRoom().getRoomID().equals(testRoom.getRoomID()));
		assertTrue(reservations.get(0).getHostList().size() == 1);
		assertTrue(reservations.get(0).getParticipantList().size() == 0);
		assertTrue(reservations.get(0).getTitle().equals("testSelectReservationByID Title"));
		assertTrue(reservations.get(0).getDescription().equals("testSelectReservationByID Description"));
		
		// Delete test room (all reservations for this room will also be deleted)
		assertTrue(controller.deleteRoom(testRoom.getRoomID()));
		// Delete host
		assertTrue(controller.deleteUser(testUser.getUserID()));
	}

	@Test
	public void testSelectAllFunctions() {
		DBController controller = DBController.getInstance();
		List<Function> functions = controller.selectAllFunctions();

		assertTrue(functions.size() >= 2);
	}

	@Test
	public void testSelectAllRoles() {
		DBController controller = DBController.getInstance();
		List<Role> roles = controller.selectAllRoles();

		assertTrue(roles.size() >= 2);
	}

	@Test
	public void testSelectAllRooms() {
		DBController controller = DBController.getInstance();
		List<Room> rooms = controller.selectAllRooms();

		assertTrue(rooms.size() >= 4);
	}

	@Test
	public void testSelectAllUsers() {
		DBController controller = DBController.getInstance();
		List<User> users = controller.selectAllUsers();

		assertTrue(users.size() >= 5);
	}

	@Test
	public void testSelectAllReservations() {
		DBController controller = DBController.getInstance();
		List<Reservation> reservations = controller.selectAllReservations();

		assertTrue(reservations.size() >= 2);
	}
	
	@Test
	public void testSelectFreeRooms() {
		DBController controller = DBController.getInstance();
		
		// Insert test host for reservations
		String fName = "f testSelectFreeRooms 1";
		String lName = "l testSelectFreeRooms 1";
		String email = "e testSelectFreeRooms 1";
		String uName = "u testSelectFreeRooms 1";
		String pw = "p testSelectFreeRooms 1";
		controller.insertNewUser(fName, lName, email, uName, pw, null, null);
		User testUser = controller.selectUserBy(Table_User.COLUMN_USERNAME, uName).get(0);
		ArrayList<User> hosts = new ArrayList<User>();
		hosts.add(testUser);
		
		// Insert test room
		controller.insertNewRoom(-3, "Test Room", 55);
		Room testRoom1 = controller.selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, -3).get(0);
		controller.insertNewRoom(-4, "Test Room 2", 66);
		Room testRoom2 = controller.selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, -4).get(0);
		// Insert test reservations for test room
		// reservation from 08:00 - 12:00 (08.12.2018)
		Timestamp startDate1 = Timestamp.valueOf("2018-12-08 08:00:00.000000");
		Timestamp endDate1 = Timestamp.valueOf("2018-12-08 12:00:00.000000");
		assertTrue(controller.insertNewReservation(startDate1, endDate1, testRoom1, hosts, null, "Test free room 1", "Test free room des 1"));
		// reservation from 13:00 - 17:00 (08.12.2018)
		Timestamp startDate2 = Timestamp.valueOf("2018-12-08 15:00:00.000000");
		Timestamp endDate2 = Timestamp.valueOf("2018-12-08 17:00:00.000000");
		assertTrue(controller.insertNewReservation(startDate2, endDate2, testRoom1, hosts, null, "Test free room 1", "Test free room des 1"));
		// reservation from 08:00 - 08:00 (09.12.2018 - 10.12.2018)
		Timestamp startDate3 = Timestamp.valueOf("2018-12-09 08:00:00.000000");
		Timestamp endDate3 = Timestamp.valueOf("2018-12-10 08:00:00.000000");
		assertTrue(controller.insertNewReservation(startDate3, endDate3, testRoom1, hosts, null, "Test free room 1", "Test free room des 1"));
		// reservation from 08:00 - 12:00 (10.12.2018)
		Timestamp startDate4 = Timestamp.valueOf("2018-12-10 08:00:00.000000");
		Timestamp endDate4 = Timestamp.valueOf("2018-12-10 12:00:00.000000");
		assertTrue(controller.insertNewReservation(startDate4, endDate4, testRoom1, hosts, null, "Test free room 1", "Test free room des 1"));
		// reservation from 08:00 - 17:00 (12.12.2018)
		Timestamp startDate5 = Timestamp.valueOf("2018-12-12 08:00:00.000000");
		Timestamp endDate5 = Timestamp.valueOf("2018-12-12 17:00:00.000000");
		assertTrue(controller.insertNewReservation(startDate5, endDate5, testRoom1, hosts, null, "Test free room 1", "Test free room des 1"));
		
		// Select free rooms between 12:00 - 17:00 (09.12.2018) -> test room 1 should be occupied
		Timestamp from = Timestamp.valueOf("2018-12-09 12:00:00.000000");
		Timestamp to = Timestamp.valueOf("2018-12-09 17:00:00.000000");
		List<Room> freeRooms = controller.selectFreeRooms(from, to);
		boolean isTestRoom1Free = false;
		boolean isTestRoom2Free = false;
		for(Room room : freeRooms){
			if(room.getRoomID().equals(testRoom1.getRoomID())) isTestRoom1Free = true;
			if(room.getRoomID().equals(testRoom2.getRoomID())) isTestRoom2Free = true;
		}
		// Test room 1 should be occupied, test room 2 not
		assertFalse(isTestRoom1Free);
		assertTrue(isTestRoom2Free);
		
		// Select free rooms between 00:00 - 07:00 (08.12.2018) -> test room 1 and test room 2 should be free
		from = Timestamp.valueOf("2018-12-08 00:00:00.000000");
		to = Timestamp.valueOf("2018-12-08 07:00:00.000000");
		freeRooms = controller.selectFreeRooms(from, to);
		isTestRoom1Free = false;
		isTestRoom2Free = false;
		for(Room room : freeRooms){
			if(room.getRoomID().equals(testRoom1.getRoomID())) isTestRoom1Free = true;
			if(room.getRoomID().equals(testRoom2.getRoomID())) isTestRoom2Free = true;
		}
		// Test room 1 should be occupied, test room 2 not
		assertTrue(isTestRoom1Free);
		assertTrue(isTestRoom2Free);
		
		// Delete test room (reservations for this room will also be delete by the database cause of the foreign key)
		assertTrue(controller.deleteRoom(testRoom1.getRoomID()));
		assertTrue(controller.deleteRoom(testRoom2.getRoomID()));
		// Delete user
		assertTrue(controller.deleteUser(testUser.getUserID()));
	}

	// --- INSERT ---
	@Test
	public void testInsertNewReservation() {
		DBController controller = DBController.getInstance();
		// Insert test user without function and role
		String fName = "f testInsertNewReservation User";
		String lName = "l testInsertNewReservation User";
		String email = "e testInsertNewReservation User";
		String uname = "u testInsertNewReservation User";
		String pw = "p testInsertNewReservation User";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User user = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		List<User> hosts = new ArrayList<User>();
		hosts.add(user);
		
		// Insert test room
		controller.insertNewRoom(-5, "testInsertNewReservation Test Room", 55);
		Room testRoom = controller.selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, -5).get(0);
		
		// Insert test reservation
		Timestamp startDate = Timestamp.valueOf("2016-12-08 08:00:00.000000");
		Timestamp endDate = Timestamp.valueOf("2016-12-08 08:00:00.000000");
		String title = "testInsertNewReservation Title";
		String description = "testInsertNewReservation Description";
		assertTrue(controller.insertNewReservation(startDate, endDate, testRoom, hosts, null, title, description));
		
		// Insert reservation without host
		hosts.remove(user);
		assertFalse(controller.insertNewReservation(startDate, endDate, testRoom, hosts, null, title, description));
		
		// Insert reservation at same time for same room
		hosts.add(user);
		assertFalse(controller.insertNewReservation(startDate, endDate, testRoom, hosts, null, title, description));
		
		// Delete test reservation
		for (Reservation reservation : controller.selectReservationBy(Table_Reservation.COLUMN_TITLE, title)) {
			assertTrue(controller.deleteReservation(reservation.getReservationID()));
		}
		// Delete test room
		assertTrue(controller.deleteRoom(testRoom.getRoomID()));
		// Delete user
		assertTrue(controller.deleteUser(user.getUserID()));
	}

	@Test
	public void testInsertNewUser() {
		DBController controller = DBController.getInstance();
		assertTrue(controller.insertNewUser("fName", "lName", "email", "username", "", null, null));
		assertTrue(controller.deleteUser(controller.selectUserBy(Table_User.COLUMN_USERNAME, "username").get(0).getUserID()));
	}

	// --- DELETE ---
	@Test
	public void testDeleteReservation() {
		DBController controller = DBController.getInstance();

		// Insert test reservation
		Timestamp startDate = Timestamp.valueOf("2016-12-08 08:00:00.000000");
		Timestamp endDate = Timestamp.valueOf("2016-12-08 08:00:00.000000");
		
		// Insert test room
		controller.insertNewRoom(-6, "Test Room", 55);
		Room testRoom = controller.selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, -6).get(0);
		
		List<User> hosts = DBController.getInstance().selectUserBy(Table_User.COLUMN_ID, 1);
		List<User> participants = DBController.getInstance().selectUserBy(Table_User.COLUMN_ID, 2);
		String title = "testDeleteReservation Title";
		String description = "testDeleteReservation Description";
		assertTrue(controller.insertNewReservation(startDate, endDate, testRoom, hosts, participants, title, description));
		
		// Delete test reservation
		for (Reservation reservation : controller.selectReservationBy(Table_Reservation.COLUMN_TITLE, title)) {
			assertTrue(controller.deleteReservation(reservation.getReservationID()));
		}
		// Delete room
		assertTrue(controller.deleteRoom(testRoom.getRoomID()));
	}
	
	// --- UPDATE ---
	@Test
	public void testUpateHostReservation() {
		DBController controller = DBController.getInstance();
		
		// Insert test function
		assertTrue(controller.insertNewFunction("testUpateHostReservation Function"));
		Function testFunction = controller.selectFunctionBy(Table_Function.COLUMN_FUNCTION, "testUpateHostReservation Function").get(0);
		
		// Insert test role
		assertTrue(controller.insertNewRole("testUpateHostReservation Role"));
		Role testRole = controller.selectRoleBy(Table_Role.COLUMN_ROLE, "testUpateHostReservation Role").get(0);
		
		// Insert test host
		String fName = "ftestUpateHostReservation Host";
		String lName = "ltestUpateHostReservation Host";
		String email = "etestUpateHostReservation Host";
		String uname = "utestUpateHostReservation Host";
		String pw = "ptestUpateHostReservation Host";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, testFunction, testRole));
		User testHost = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		List<User> hosts = new ArrayList<User>();
		hosts.add(testHost);
		
		// Insert test participants
		fName = "ftestUpateHostReservation Participant 1";
		lName = "ltestUpateHostReservation Participant 1";
		email = "etestUpateHostReservation Participant 1";
		uname = "utestUpateHostReservation Participant 1";
		pw = "ptestUpateHostReservation Participant 1";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, testFunction, testRole));
		User testParticipant1 = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		
		fName = "ftestUpateHostReservation Participant 2";
		lName = "ltestUpateHostReservation Participant 2";
		email = "etestUpateHostReservation Participant 2";
		uname = "utestUpateHostReservation Participant 2";
		pw = "ptestUpateHostReservation Participant 2";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, testFunction, testRole));
		User testParticipant2 = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		
		List<User> participants = new ArrayList<User>();
		participants.add(testParticipant1);
		participants.add(testParticipant2);
		
		// Insert test room
		controller.insertNewRoom(-7, "Test Room", 55);
		Room testRoom = controller.selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, -7).get(0);
		
		// Insert test reservations in which the user 1 is the host
		Timestamp startDate = Timestamp.valueOf("2018-12-08 08:00:00.000000");
		Timestamp endDate = Timestamp.valueOf("2018-12-08 12:00:00.000000");
		assertTrue(controller.insertNewReservation(startDate, endDate, testRoom, hosts, participants, "Test Update Host", "Test Update Host Description"));
		
		Reservation testReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ROOMID, testRoom.getRoomID()).get(0);
		assertTrue(testReservation.getHostList().size() == 1);
		assertTrue(testReservation.getHostList().get(0).getUserID().equals(testHost.getUserID()));
		assertTrue(testReservation.getParticipantList().size() == 2);
		assertTrue(testReservation.getAcceptedParticipantsList().size() == 0);
		assertFalse(testReservation.hasParticipantAcceptedReservation(testParticipant1));
		assertFalse(testReservation.hasParticipantAcceptedReservation(testParticipant2));
		
		// Set host to participant (should not be able to because no host would remain for the reservation)
		assertFalse(controller.updateHostReservation(testHost, testReservation, false));
		assertTrue(testReservation.getHostList().size() == 1);
		assertTrue(testReservation.getHostList().get(0).getUserID().equals(testHost.getUserID()));
		assertTrue(testReservation.getParticipantList().size() == 2);
		assertTrue(testReservation.getAcceptedParticipantsList().size() == 0);
		assertFalse(testReservation.hasParticipantAcceptedReservation(testParticipant1));
		assertFalse(testReservation.hasParticipantAcceptedReservation(testParticipant2));
		// Check that the database was not updated
		testReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ROOMID, testRoom.getRoomID()).get(0);
		assertTrue(testReservation.getHostList().size() == 1);
		assertTrue(testReservation.getHostList().get(0).getUserID().equals(testHost.getUserID()));
		assertTrue(testReservation.getParticipantList().size() == 2);
		assertTrue(testReservation.getAcceptedParticipantsList().size() == 0);
		assertFalse(testReservation.hasParticipantAcceptedReservation(testParticipant1));
		assertFalse(testReservation.hasParticipantAcceptedReservation(testParticipant2));
		
		// Set participant 1 also to host
		assertTrue(controller.updateHostReservation(testParticipant1, testReservation, true));
		// Test if reservation object is updated
		assertTrue(testReservation.getHostList().size() == 2);
		assertTrue(testReservation.getParticipantList().size() == 1);
		assertTrue(testReservation.getHostList().contains(testParticipant1));
		assertFalse(testReservation.getParticipantList().contains(testParticipant1));
		assertTrue(testReservation.getAcceptedParticipantsList().size() == 0);
		// Test if the update method has updated the database
		Reservation testSelectReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, testReservation.getReservationID()).get(0);
		assertTrue(testSelectReservation.getHostList().size() == 2);
		assertTrue(testReservation.getParticipantList().size() == 1);
		assertTrue(testReservation.getHostList().get(1).getUserID().equals(testParticipant1.getUserID()));
		assertFalse(testReservation.getParticipantList().get(0).getUserID().equals(testParticipant1.getUserID()));
		
		// Set participant 2 also to host
		assertTrue(controller.updateHostReservation(testParticipant2, testReservation, true));
		// Test if reservation object is updated
		assertTrue(testReservation.getHostList().size() == 3);
		assertTrue(testReservation.getParticipantList().size() == 0);
		assertTrue(testReservation.getHostList().contains(testParticipant2));
		assertFalse(testReservation.getParticipantList().contains(testParticipant2));
		assertTrue(testReservation.getAcceptedParticipantsList().size() == 0);
		// Test if the update method has updated the database
		testSelectReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, testReservation.getReservationID()).get(0);
		assertTrue(testSelectReservation.getHostList().size() == 3);
		assertTrue(testReservation.getParticipantList().size() == 0);
		assertTrue(testReservation.getHostList().get(2).getUserID().equals(testParticipant2.getUserID()));
		
		// Revert host 3 to be a participant
		assertTrue(controller.updateHostReservation(testParticipant2, testReservation, false));
		// Test if reservation object is updated
		assertTrue(testReservation.getHostList().size() == 2);
		assertTrue(testReservation.getParticipantList().size() == 1);
		assertFalse(testReservation.getHostList().contains(testParticipant2));
		assertTrue(testReservation.getParticipantList().contains(testParticipant2));
		assertTrue(testReservation.getAcceptedParticipantsList().size() == 0);
		// Test if the update method has updated the database
		testSelectReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, testReservation.getReservationID()).get(0);
		assertTrue(testSelectReservation.getHostList().size() == 2);
		assertTrue(testReservation.getParticipantList().size() == 1);
		assertTrue(testReservation.getParticipantList().get(0).getUserID().equals(testParticipant2.getUserID()));
		
		// Delete function
		assertTrue(controller.deleteFunction(testFunction.getId()));
		// Delete role
		assertTrue(controller.deleteRole(testRole.getId()));
		// Delete room (Deletes also all reservations)
		assertTrue(controller.deleteRoom(testRoom.getRoomID()));
		// Delete users
		assertTrue(controller.deleteUser(testHost.getUserID()));
		assertTrue(controller.deleteUser(testParticipant1.getUserID()));
		assertTrue(controller.deleteUser(testParticipant2.getUserID()));
	}
	
	@Test
	public void testUpdateAcceptReservation() {
		DBController controller = DBController.getInstance();
		// Insert test host
		String fName = "ftestUpdateAcceptReservation Host";
		String lName = "ltestUpdateAcceptReservation Host";
		String email = "etestUpdateAcceptReservation Host";
		String uname = "utestUpdateAcceptReservation Host";
		String pw = "ptestUpdateAcceptReservation Host";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User testHost = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		List<User> hosts = new ArrayList<User>();
		hosts.add(testHost);
		
		// Insert test participants
		fName = "ftestUpdateAcceptReservation Participant 1";
		lName = "ltestUpdateAcceptReservation Participant 1";
		email = "etestUpdateAcceptReservation Participant 1";
		uname = "utestUpdateAcceptReservation Participant 1";
		pw = "ptestUpdateAcceptReservation Participant 1";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User testParticipant1 = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		
		fName = "ftestUpdateAcceptReservation Participant 2";
		lName = "ltestUpdateAcceptReservation Participant 2";
		email = "etestUpdateAcceptReservation Participant 2";
		uname = "utestUpdateAcceptReservation Participant 2";
		pw = "ptestUpdateAcceptReservation Participant 2";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User testParticipant2 = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		
		List<User> participants = new ArrayList<User>();
		participants.add(testParticipant1);
		participants.add(testParticipant2);
		
		// Insert test room
		controller.insertNewRoom(-8, "Test Room", 55);
		Room testRoom = controller.selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, -8).get(0);
		
		// Insert test reservations in which the user 1 is the host
		Timestamp startDate = Timestamp.valueOf("2018-12-08 08:00:00.000000");
		Timestamp endDate = Timestamp.valueOf("2018-12-08 12:00:00.000000");
		assertTrue(controller.insertNewReservation(startDate, endDate, testRoom, hosts, participants, "Test Update Accept", "Test Update Accept Description"));
		
		// Default nobody should have accepted the reservation
		Reservation testReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ROOMID, testRoom.getRoomID()).get(0);
		assertTrue(testReservation.getHostList().size() == 1);
		assertTrue(testReservation.getParticipantList().size() == 2);
		assertTrue(testReservation.getAcceptedParticipantsList().size() == 0);
		assertFalse(testReservation.hasParticipantAcceptedReservation(testParticipant1));
		assertFalse(testReservation.hasParticipantAcceptedReservation(testParticipant2));
		
		// Accept reservation for participant 2
		controller.updateAcceptReservation(testParticipant2, testReservation, true);
		// Test if user object is updated
		assertTrue(testReservation.getHostList().size() == 1);
		assertTrue(testReservation.getParticipantList().size() == 2);
		assertTrue(testReservation.getAcceptedParticipantsList().size() == 1);
		assertFalse(testReservation.hasParticipantAcceptedReservation(testParticipant1));
		assertTrue(testReservation.hasParticipantAcceptedReservation(testParticipant2));
		// Test if the update method has updated the database
		Reservation testSelectReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, testReservation.getReservationID()).get(0);
		assertTrue(testSelectReservation.getHostList().size() == 1);
		assertTrue(testSelectReservation.getParticipantList().size() == 2);
		assertTrue(testSelectReservation.getAcceptedParticipantsList().size() == 1);
		assertTrue(testSelectReservation.getAcceptedParticipantsList().get(0).getUserID().equals(testParticipant2.getUserID()));
		assertFalse(testSelectReservation.hasParticipantAcceptedReservation(testParticipant1));
		assertTrue(testSelectReservation.hasParticipantAcceptedReservation(testParticipant2));
		
		// Accept reservation for participant 1
		controller.updateAcceptReservation(testParticipant1, testReservation, true);
		// Test if user object is updated
		assertTrue(testReservation.getHostList().size() == 1);
		assertTrue(testReservation.getParticipantList().size() == 2);
		assertTrue(testReservation.getAcceptedParticipantsList().size() == 2);
		assertTrue(testReservation.hasParticipantAcceptedReservation(testParticipant1));
		assertTrue(testReservation.hasParticipantAcceptedReservation(testParticipant2));
		assertTrue(testReservation.haveAllParticipantsAccepted());
		// Test if the update method has updated the database
		testSelectReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, testReservation.getReservationID()).get(0);
		assertTrue(testSelectReservation.getHostList().size() == 1);
		assertTrue(testSelectReservation.getParticipantList().size() == 2);
		assertTrue(testSelectReservation.getAcceptedParticipantsList().size() == 2);
		assertTrue(testSelectReservation.getAcceptedParticipantsList().get(0).getUserID().equals(testParticipant1.getUserID()));
		assertTrue(testSelectReservation.getAcceptedParticipantsList().get(1).getUserID().equals(testParticipant2.getUserID()));
		assertTrue(testSelectReservation.hasParticipantAcceptedReservation(testParticipant1));
		assertTrue(testSelectReservation.hasParticipantAcceptedReservation(testParticipant2));
		assertTrue(testSelectReservation.haveAllParticipantsAccepted());
		
		// Cancel reservation for participant 1 & 2
		controller.updateAcceptReservation(testParticipant1, testReservation, false);
		controller.updateAcceptReservation(testParticipant2, testReservation, false);
		// Test if user object is updated
		assertTrue(testReservation.getHostList().size() == 1);
		assertTrue(testReservation.getParticipantList().size() == 2);
		assertTrue(testReservation.getAcceptedParticipantsList().size() == 0);
		assertFalse(testReservation.hasParticipantAcceptedReservation(testParticipant1));
		assertFalse(testReservation.hasParticipantAcceptedReservation(testParticipant2));
		// Test if the update method has updated the database
		testSelectReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, testReservation.getReservationID()).get(0);
		assertTrue(testSelectReservation.getHostList().size() == 1);
		assertTrue(testSelectReservation.getParticipantList().size() == 2);
		assertTrue(testSelectReservation.getAcceptedParticipantsList().size() == 0);
		assertFalse(testSelectReservation.hasParticipantAcceptedReservation(testParticipant1));
		assertFalse(testSelectReservation.hasParticipantAcceptedReservation(testParticipant2));
		
		// Create second reservation
		startDate = Timestamp.valueOf("2018-12-08 12:00:00.000000");
		endDate = Timestamp.valueOf("2018-12-08 14:00:00.000000");
		assertTrue(controller.insertNewReservation(startDate, endDate, testRoom, hosts, participants, "Test Update Accept 2", "Test Update Accept Description 2"));
		
		// Accept both reservations for participant 1
		Reservation testReservation2 = controller.selectReservationBy(Table_Reservation.COLUMN_TITLE, "Test Update Accept 2").get(0);
		List<Reservation> reservations = new ArrayList<Reservation>();
		reservations.add(testReservation);
		reservations.add(testReservation2);
		// No reservation should be accepted by participant 1 at beginning
		assertTrue(testReservation2.getHostList().size() == 1);
		assertTrue(testReservation2.getParticipantList().size() == 2);
		assertTrue(testReservation2.getAcceptedParticipantsList().size() == 0);
		assertFalse(testReservation.hasParticipantAcceptedReservation(testParticipant1));
		assertFalse(testReservation2.hasParticipantAcceptedReservation(testParticipant1));
		// Accept
		// Test if reservation objects are updated
		assertTrue(controller.updateAcceptReservation(testParticipant1, reservations, true));
		assertTrue(testReservation.getAcceptedParticipantsList().size() == 1);
		assertTrue(testReservation.hasParticipantAcceptedReservation(testParticipant1));
		assertTrue(testReservation2.getAcceptedParticipantsList().size() == 1);
		assertTrue(testReservation2.hasParticipantAcceptedReservation(testParticipant1));
		// Test if database is updated
		testSelectReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, testReservation.getReservationID()).get(0);
		Reservation testSelectReservation2 = controller.selectReservationBy(Table_Reservation.COLUMN_ID, testReservation2.getReservationID()).get(0);
		assertTrue(testSelectReservation.getAcceptedParticipantsList().size() == 1);
		assertTrue(testSelectReservation.hasParticipantAcceptedReservation(testParticipant1));
		assertTrue(testSelectReservation2.getAcceptedParticipantsList().size() == 1);
		assertTrue(testSelectReservation2.hasParticipantAcceptedReservation(testParticipant1));
		
		// Cancel both reservation for participant 1
		assertTrue(controller.updateAcceptReservation(testParticipant1, reservations, false));
		assertTrue(testReservation.getAcceptedParticipantsList().size() == 0);
		assertTrue(testReservation2.getAcceptedParticipantsList().size() == 0);
		assertFalse(testReservation.hasParticipantAcceptedReservation(testParticipant1));
		assertFalse(testReservation2.hasParticipantAcceptedReservation(testParticipant1));
		// Test if database is updated
		testSelectReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, testReservation.getReservationID()).get(0);
		testSelectReservation2 = controller.selectReservationBy(Table_Reservation.COLUMN_ID, testReservation2.getReservationID()).get(0);
		assertTrue(testSelectReservation.getAcceptedParticipantsList().size() == 0);
		assertFalse(testSelectReservation.hasParticipantAcceptedReservation(testParticipant1));
		assertTrue(testSelectReservation2.getAcceptedParticipantsList().size() == 0);
		assertFalse(testSelectReservation2.hasParticipantAcceptedReservation(testParticipant1));
		
		// Delete room (Deletes also all reservations)
		assertTrue(controller.deleteRoom(testRoom.getRoomID()));
		// Delete users
		assertTrue(controller.deleteUser(testHost.getUserID()));
		assertTrue(controller.deleteUser(testParticipant1.getUserID()));
		assertTrue(controller.deleteUser(testParticipant2.getUserID()));
	}
	
	@Test
	public void testUpdateReservationChangeReservation() {
		DBController controller = DBController.getInstance();
		// Insert test host
		String fName = "f testUpdateReservationChangeReservation Host 1";
		String lName = "l testUpdateReservationChangeReservation Host 1";
		String email = "e testUpdateReservationChangeReservation Host 1";
		String uname = "u testUpdateReservationChangeReservation Host 1";
		String pw = "p testUpdateReservationChangeReservation Host 1";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User host1 = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		List<User> hosts = new ArrayList<User>();
		hosts.add(host1);
		
		fName = "f testUpdateReservationChangeReservation Host 2";
		lName = "l testUpdateReservationChangeReservation Host 2";
		email = "e testUpdateReservationChangeReservation Host 2";
		uname = "u testUpdateReservationChangeReservation Host 2";
		pw = "p testUpdateReservationChangeReservation Host 2";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User host2 = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		hosts.add(host2);
		
		fName = "f testUpdateReservationChangeReservation Host 3";
		lName = "l testUpdateReservationChangeReservation Host 3";
		email = "e testUpdateReservationChangeReservation Host 3";
		uname = "u testUpdateReservationChangeReservation Host 3";
		pw = "p testUpdateReservationChangeReservation Host 3";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User host3 = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		hosts.add(host3);
		
		// Insert test participants
		fName = "f testUpdateReservationChangeReservation User 2";
		lName = "l testUpdateReservationChangeReservation User 2";
		email = "e testUpdateReservationChangeReservation User 2";
		uname = "u testUpdateReservationChangeReservation User 2";
		pw = "p testUpdateReservationChangeReservation User 2";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User user2 = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		
		fName = "f testUpdateReservationChangeReservation User 3";
		lName = "l testUpdateReservationChangeReservation User 3";
		email = "e testUpdateReservationChangeReservation User 3";
		uname = "u testUpdateReservationChangeReservation User 3";
		pw = "p testUpdateReservationChangeReservation User 3";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User user3 = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		
		List<User> participants = new ArrayList<User>();
		participants.add(user2);
		participants.add(user3);
		
		// Insert test room
		controller.insertNewRoom(-9, "Test Room", 55);
		Room testRoom = controller.selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, -9).get(0);
		
		// Insert test reservation
		Timestamp startDate = Timestamp.valueOf("2018-12-08 08:00:00.000000");
		Timestamp endDate = Timestamp.valueOf("2018-12-08 12:00:00.000000");
		String title = "testUpdateReservationChangeReservation Title";
		String description = "testUpdateReservationChangeReservation Description";
		assertTrue(controller.insertNewReservation(startDate, endDate, testRoom, hosts, participants, title, description));
		
		Reservation reservation = controller.selectReservationBy(Table_Reservation.COLUMN_ROOMID, testRoom.getRoomID()).get(0);
		assertTrue(reservation.getParticipantList().size() == 2);
		assertTrue(reservation.getAcceptedParticipantsList().size() == 0);
		assertTrue(reservation.getHostList().size() == 3);
		
		// --- EDIT RESERVATION TITLE AND DESCRIPTION ---
		// Get reservation and edit it
		// Edit title
		String newTitle = "testUpdateReservationChangeReservation new Title";
		reservation.setTitle(newTitle);
		assertTrue(reservation.getTitle().equals(newTitle));
		assertTrue(controller.updateReservation(reservation));
		// Check if database was updated
		Reservation dbReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, reservation.getReservationID()).get(0);
		assertTrue(dbReservation.getTitle().equals(newTitle));
		
		// Edit description
		String newDescription = "testUpdateReservationChangeReservation new Title";
		reservation.setDescription(newDescription);
		assertTrue(reservation.getDescription().equals(newDescription));
		assertTrue(controller.updateReservation(reservation));
		// Check if database was updated
		dbReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, reservation.getReservationID()).get(0);
		assertTrue(dbReservation.getDescription().equals(newDescription));
		
		// --- ADD/REMOVE HOSTS AND PARTICIPANTS ---
		// Add new participant
		fName = "f testUpdateReservationChangeReservation Added Participant";
		lName = "l testUpdateReservationChangeReservation Added Participant";
		email = "e testUpdateReservationChangeReservation Added Participant";
		uname = "u testUpdateReservationChangeReservation Added Participant";
		pw = "p testUpdateReservationChangeReservation Added Participant";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User addedparticipant = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		
		reservation.addParticipant(addedparticipant);
		assertTrue(reservation.getParticipantList().size() == 3);
		assertTrue(reservation.getAcceptedParticipantsList().size() == 0);
		assertTrue(controller.updateReservation(reservation));
		// Check if database was updated
		dbReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, reservation.getReservationID()).get(0);
		assertTrue(dbReservation.getAcceptedParticipantsList().size() == 0);
		assertTrue(dbReservation.getHostList().size() == 3);
		assertTrue(dbReservation.getParticipantList().size() == 3);
		assertTrue(dbReservation.getParticipantList().get(2).getUserID().equals(addedparticipant.getUserID()));
		
		// Remove host 2 and 3
		assertTrue(reservation.removeHost(host2));
		assertTrue(reservation.getHostList().size() == 2);
		assertTrue(reservation.removeHost(host3));
		assertTrue(reservation.getHostList().size() == 1);
		assertTrue(controller.updateReservation(reservation));
		
		// Remove host 1 (should not be able to because no host would remain for the reservation)
		assertFalse(reservation.removeHost(host1));
		assertTrue(reservation.getAcceptedParticipantsList().size() == 0);
		assertTrue(reservation.getHostList().size() == 1);
		assertTrue(reservation.getParticipantList().size() == 3);
		
		// Add new host
		fName = "f testUpdateReservationChangeReservation Added Host";
		lName = "l testUpdateReservationChangeReservation Added Host";
		email = "e testUpdateReservationChangeReservation Added Host";
		uname = "u testUpdateReservationChangeReservation Added Host";
		pw = "p testUpdateReservationChangeReservation Added Host";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User addedHost = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		
		reservation.addHost(addedHost);
		assertTrue(reservation.getAcceptedParticipantsList().size() == 0);
		assertTrue(reservation.getHostList().size() == 2);
		assertTrue(reservation.getParticipantList().size() == 3);
		assertTrue(controller.updateReservation(reservation));
		// Check if database was updated
		dbReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, reservation.getReservationID()).get(0);
		assertTrue(dbReservation.getAcceptedParticipantsList().size() == 0);
		assertTrue(dbReservation.getHostList().size() == 2);
		assertTrue(dbReservation.getParticipantList().size() == 3);
		
		// Now remove the original host
		reservation.removeHost(host1);
		assertTrue(reservation.getAcceptedParticipantsList().size() == 0);
		assertTrue(reservation.getHostList().size() == 1);
		assertTrue(reservation.getHostList().get(0).getUserID().equals(addedHost.getUserID()));
		assertTrue(reservation.getParticipantList().size() == 3);
		assertTrue(controller.updateReservation(reservation));
		// Check if database was updated
		dbReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, reservation.getReservationID()).get(0);
		assertTrue(dbReservation.getAcceptedParticipantsList().size() == 0);
		assertTrue(dbReservation.getHostList().size() == 1);
		assertTrue(dbReservation.getHostList().get(0).getUserID().equals(addedHost.getUserID()));
		assertTrue(dbReservation.getParticipantList().size() == 3);
		
		// Remove all participants
		reservation.removeAllParticipants();
		assertTrue(reservation.getParticipantList().size() == 0);
		assertTrue(reservation.getAcceptedParticipantsList().size() == 0);
		assertTrue(controller.updateReservation(reservation));
		// Check if database was updated
		dbReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, reservation.getReservationID()).get(0);
		assertTrue(dbReservation.getParticipantList().size() == 0);
		assertTrue(dbReservation.getAcceptedParticipantsList().size() == 0);
		assertTrue(dbReservation.getHostList().size() == 1);
		
		// Delete room (Deletes also all reservations)
		assertTrue(controller.deleteRoom(testRoom.getRoomID()));
		// Delete users
		assertTrue(controller.deleteUser(host1.getUserID()));
		assertTrue(controller.deleteUser(host2.getUserID()));
		assertTrue(controller.deleteUser(host3.getUserID()));
		assertTrue(controller.deleteUser(user2.getUserID()));
		assertTrue(controller.deleteUser(user3.getUserID()));
		assertTrue(controller.deleteUser(addedparticipant.getUserID()));
		assertTrue(controller.deleteUser(addedHost.getUserID()));
	}
	
	@Test
	public void testUpdateReservationChangeReservationTime() {
		DBController controller = DBController.getInstance();
		// Insert test host
		String fName = "f testUpdateReservationChangeReservationTime User 1";
		String lName = "l testUpdateReservationChangeReservationTime User 1";
		String email = "e testUpdateReservationChangeReservationTime User 1";
		String uname = "u testUpdateReservationChangeReservationTime User 1";
		String pw = "p testUpdateReservationChangeReservationTime User 1";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User user1 = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		List<User> hosts = new ArrayList<User>();
		hosts.add(user1);
		
		// Insert test participants
		fName = "f testUpdateReservationChangeReservationTime User 2";
		lName = "l testUpdateReservationChangeReservationTime User 2";
		email = "e testUpdateReservationChangeReservationTime User 2";
		uname = "u testUpdateReservationChangeReservationTime User 2";
		pw = "p testUpdateReservationChangeReservationTime User 2";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User user2 = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		
		fName = "f testUpdateReservationChangeReservationTime User 3";
		lName = "l testUpdateReservationChangeReservationTime User 3";
		email = "e testUpdateReservationChangeReservationTime User 3";
		uname = "u testUpdateReservationChangeReservationTime User 3";
		pw = "p testUpdateReservationChangeReservationTime User 3";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User user3 = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		
		List<User> participants = new ArrayList<User>();
		participants.add(user2);
		participants.add(user3);
		
		// Insert test room
		controller.insertNewRoom(-10, "Test Room", 55);
		Room testRoom = controller.selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, -10).get(0);
		
		// Insert test reservations
		Timestamp startDate = Timestamp.valueOf("2018-12-08 07:00:00.000000");
		Timestamp endDate = Timestamp.valueOf("2018-12-08 09:00:00.000000");
		String title = "testUpdateReservationChangeReservationTime Title 1";
		String description = "testUpdateReservationChangeReservationTime Description 1";
		assertTrue(controller.insertNewReservation(startDate, endDate, testRoom, hosts, participants, title, description));
		
		startDate = Timestamp.valueOf("2018-12-08 10:00:00.000000");
		endDate = Timestamp.valueOf("2018-12-08 12:00:00.000000");
		title = "testUpdateReservationChangeReservationTime Title 2";
		description = "testUpdateReservationChangeReservationTime Description 2";
		assertTrue(controller.insertNewReservation(startDate, endDate, testRoom, hosts, participants, title, description));
		Reservation reservation2 = controller.selectReservationBy(Table_Reservation.COLUMN_TITLE, title).get(0);
		
		startDate = Timestamp.valueOf("2018-12-08 13:00:00.000000");
		endDate = Timestamp.valueOf("2018-12-08 17:00:00.000000");
		title = "testUpdateReservationChangeReservationTime Title 3";
		description = "testUpdateReservationChangeReservationTime Description 3";
		assertTrue(controller.insertNewReservation(startDate, endDate, testRoom, hosts, participants, title, description));
		
		// Start earlier
		startDate = Timestamp.valueOf("2018-12-08 09:00:00.000000");
		reservation2.setStartDate(startDate);
		assertTrue(controller.updateReservation(reservation2));
		
		startDate = Timestamp.valueOf("2018-12-08 08:00:00.000000");
		reservation2.setStartDate(startDate);
		assertFalse(controller.updateReservation(reservation2));
		
		// Reset reservation
		startDate = Timestamp.valueOf("2018-12-08 10:00:00.000000");
		endDate = Timestamp.valueOf("2018-12-08 12:00:00.000000");
		reservation2.setStartDate(startDate);
		reservation2.setEndDate(endDate);
		assertTrue(controller.updateReservation(reservation2));
		
		// End later
		endDate = Timestamp.valueOf("2018-12-08 13:00:00.000000");
		reservation2.setEndDate(endDate);
		assertTrue(controller.updateReservation(reservation2));
		
		endDate = Timestamp.valueOf("2018-12-08 15:00:00.000000");
		reservation2.setEndDate(endDate);
		assertFalse(controller.updateReservation(reservation2));
		
		// Reset reservation
		startDate = Timestamp.valueOf("2018-12-08 10:00:00.000000");
		endDate = Timestamp.valueOf("2018-12-08 12:00:00.000000");
		reservation2.setStartDate(startDate);
		reservation2.setEndDate(endDate);
		assertTrue(controller.updateReservation(reservation2));
		
		// Start earlier and end later
		startDate = Timestamp.valueOf("2018-12-08 09:00:00.000000");
		endDate = Timestamp.valueOf("2018-12-08 13:00:00.000000");
		reservation2.setStartDate(startDate);
		reservation2.setEndDate(endDate);
		assertTrue(controller.updateReservation(reservation2));
		
		// Reset reservation
		startDate = Timestamp.valueOf("2018-12-08 10:00:00.000000");
		endDate = Timestamp.valueOf("2018-12-08 12:00:00.000000");
		reservation2.setStartDate(startDate);
		reservation2.setEndDate(endDate);
		assertTrue(controller.updateReservation(reservation2));
		
		// Free before but not after
		startDate = Timestamp.valueOf("2018-12-08 09:00:00.000000");
		endDate = Timestamp.valueOf("2018-12-08 15:00:00.000000");
		reservation2.setStartDate(startDate);
		reservation2.setEndDate(endDate);
		assertFalse(controller.updateReservation(reservation2));
		
		// Reset reservation
		startDate = Timestamp.valueOf("2018-12-08 10:00:00.000000");
		endDate = Timestamp.valueOf("2018-12-08 12:00:00.000000");
		reservation2.setStartDate(startDate);
		reservation2.setEndDate(endDate);
		assertTrue(controller.updateReservation(reservation2));
		
		// Free after but not before
		startDate = Timestamp.valueOf("2018-12-08 07:00:00.000000");
		endDate = Timestamp.valueOf("2018-12-08 13:00:00.000000");
		reservation2.setStartDate(startDate);
		reservation2.setEndDate(endDate);
		assertFalse(controller.updateReservation(reservation2));
		
		// Delete room (Deletes also all reservations)
		assertTrue(controller.deleteRoom(testRoom.getRoomID()));
		// Delete users
		assertTrue(controller.deleteUser(user1.getUserID()));
		assertTrue(controller.deleteUser(user2.getUserID()));
		assertTrue(controller.deleteUser(user3.getUserID()));
	}
	
	@Test
	public void testUpdateReservationChangeRoom() {
		DBController controller = DBController.getInstance();
		// Insert test host
		String fName = "f testUpdateReservationChangeRoom User 1";
		String lName = "l testUpdateReservationChangeRoom User 1";
		String email = "e testUpdateReservationChangeRoom User 1";
		String uname = "u testUpdateReservationChangeRoom User 1";
		String pw = "p testUpdateReservationChangeRoom User 1";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User user1 = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		List<User> hosts = new ArrayList<User>();
		hosts.add(user1);
		
		// Insert test participants
		fName = "f testUpdateReservationChangeRoom User 2";
		lName = "l testUpdateReservationChangeRoom User 2";
		email = "e testUpdateReservationChangeRoom User 2";
		uname = "u testUpdateReservationChangeRoom User 2";
		pw = "p testUpdateReservationChangeRoom User 2";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User user2 = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		
		fName = "f testUpdateReservationChangeRoom User 3";
		lName = "l testUpdateReservationChangeRoom User 3";
		email = "e testUpdateReservationChangeRoom User 3";
		uname = "u testUpdateReservationChangeRoom User 3";
		pw = "p testUpdateReservationChangeRoom User 3";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User user3 = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		
		List<User> participants = new ArrayList<User>();
		participants.add(user2);
		participants.add(user3);
		
		// Insert test rooms
		controller.insertNewRoom(-11, "Test Room 1", 55);
		Room room1 = controller.selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, -11).get(0);
		controller.insertNewRoom(-12, "Test Room 2", 66);
		Room room2 = controller.selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, -12).get(0);
		
		// Insert test reservations
		// Reservation 1 is assigned to room 1 (08:00 - 12:00)
		Timestamp startDate = Timestamp.valueOf("2018-12-08 08:00:00.000000");
		Timestamp endDate = Timestamp.valueOf("2018-12-08 12:00:00.000000");
		String title = "testUpdateReservationChangeRoom Title Reservation 1";
		String description = "testUpdateReservationChangeRoom Description Reservation 1";
		assertTrue(controller.insertNewReservation(startDate, endDate, room1, hosts, participants, title, description));
		Reservation reservation1 = controller.selectReservationBy(Table_Reservation.COLUMN_ROOMID, room1.getRoomID()).get(0);
		// Reservation 2 is assigned to room 2 (08:00 - 12:00)
		startDate = Timestamp.valueOf("2018-12-08 08:00:00.000000");
		endDate = Timestamp.valueOf("2018-12-08 12:00:00.000000");
		title = "testUpdateReservationChangeRoom Title Reservation 2";
		description = "testUpdateReservationChangeRoom Description Reservation 2";
		assertTrue(controller.insertNewReservation(startDate, endDate, room2, hosts, participants, title, description));
		// Reservation 3 is assigned to room 2 (12:00 - 17:00)
		startDate = Timestamp.valueOf("2018-12-08 14:00:00.000000");
		endDate = Timestamp.valueOf("2018-12-08 17:00:00.000000");
		title = "testUpdateReservationChangeRoom Title Reservation 3";
		description = "testUpdateReservationChangeRoom Description Reservation 3";
		assertTrue(controller.insertNewReservation(startDate, endDate, room2, hosts, participants, title, description));
		
		// Change room of reservation 1 to room 2 (should not be able because room 2 has a reservation for the time interval of reservation 1)
		reservation1.changeRoom(room2);
		assertTrue(reservation1.getRoom().getRoomID().equals(room2.getRoomID()));
		assertFalse(controller.updateReservation(reservation1));
		// Check if database is updated (should not)
		Reservation dbReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, reservation1.getReservationID()).get(0);
		assertTrue(dbReservation.getRoom().getRoomID().equals(room1.getRoomID()));
		reservation1.changeRoom(room1);
		
		// Change time of reservation 1 so it can be assigned to room 2
		startDate = Timestamp.valueOf("2018-12-08 12:00:00.000000");
		endDate = Timestamp.valueOf("2018-12-08 14:00:00.000000");
		reservation1.setStartDate(startDate);
		reservation1.setEndDate(endDate);
		reservation1.changeRoom(room2);
		assertTrue(controller.updateReservation(reservation1));
		// Check if database was updated
		dbReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, reservation1.getReservationID()).get(0);
		assertTrue(dbReservation.getRoom().getRoomID().equals(room2.getRoomID()));
		// Room 1 should have no reservations
		List<Reservation> reservations = controller.selectReservationBy(Table_Reservation.COLUMN_ROOMID, room1.getRoomID());
		assertTrue(reservations.size() == 0);
		
		// Re-change room of reservation 1 to room 1
		reservation1.changeRoom(room1);
		assertTrue(controller.updateReservation(reservation1));
		// Check if database was updated
		dbReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, reservation1.getReservationID()).get(0);
		assertTrue(dbReservation.getRoom().getRoomID().equals(room1.getRoomID()));
		
		// Change time of reservation 1 to start between the time of a reservation from room 2
		startDate = Timestamp.valueOf("2018-12-08 09:00:00.000000");
		endDate = Timestamp.valueOf("2018-12-08 13:00:00.000000");
		reservation1.setStartDate(startDate);
		reservation1.setEndDate(endDate);
		// Change room of reservation 1 to room 2 (should not be able because room 2 has a reservation for the time interval of reservation 1)
		reservation1.changeRoom(room2);
		assertFalse(controller.updateReservation(reservation1));
		// Check if database was updated
		dbReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, reservation1.getReservationID()).get(0);
		assertTrue(dbReservation.getRoom().getRoomID().equals(room1.getRoomID()));
		// Re-change room of reservation 1 to room 1
		reservation1.changeRoom(room1);
		
		// Change time of reservation 1 to end between the time of a reservation from room 2
		startDate = Timestamp.valueOf("2018-12-08 12:00:00.000000");
		endDate = Timestamp.valueOf("2018-12-08 15:00:00.000000");
		reservation1.setStartDate(startDate);
		reservation1.setEndDate(endDate);
		// Change room of reservation 1 to room 2 (should not be able because room 2 has a reservation for the time interval of reservation 1)
		reservation1.changeRoom(room2);
		assertFalse(controller.updateReservation(reservation1));
		// Check if database was updated
		dbReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, reservation1.getReservationID()).get(0);
		assertTrue(dbReservation.getRoom().getRoomID().equals(room1.getRoomID()));
		
		// Change reservation 3 of room 2 to start and end later so reservation 1 can be assigned to room 1
		Reservation reservation3 = controller.selectReservationBy(Table_Reservation.COLUMN_TITLE, title).get(0);
		startDate = Timestamp.valueOf("2018-12-08 15:00:00.000000");
		endDate = Timestamp.valueOf("2018-12-08 17:00:00.000000");
		reservation3.setStartDate(startDate);
		reservation3.setEndDate(endDate);
		assertTrue(controller.updateReservation(reservation3));
		// Check if database was updated
		dbReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, reservation3.getReservationID()).get(0);
		assertTrue(dbReservation.getStartDate().equals(reservation3.getStartDate()));
		assertTrue(dbReservation.getEndDate().equals(reservation3.getEndDate()));
		// Now update reservation 1 (should be able to because there is now free space for the time interval of reservation 1)
		assertTrue(controller.updateReservation(reservation1));
		// Check if database was updated
		dbReservation = controller.selectReservationBy(Table_Reservation.COLUMN_ID, reservation1.getReservationID()).get(0);
		assertTrue(dbReservation.getRoom().getRoomID().equals(room2.getRoomID()));
		assertTrue(dbReservation.getStartDate().equals(reservation1.getStartDate()));
		assertTrue(dbReservation.getEndDate().equals(reservation1.getEndDate()));
		
		// Delete room (Deletes also all reservations)
		assertTrue(controller.deleteRoom(room1.getRoomID()));
		assertTrue(controller.deleteRoom(room2.getRoomID()));
		// Delete users
		assertTrue(controller.deleteUser(user1.getUserID()));
		assertTrue(controller.deleteUser(user2.getUserID()));
		assertTrue(controller.deleteUser(user3.getUserID()));
	}
	
	@Test
	public void testUpdateUser() {
		DBController controller = DBController.getInstance();
		// Insert test user without function and role
		String fName = "f testUpdateUser User";
		String lName = "l testUpdateUser User";
		String email = "e testUpdateUser User";
		String uname = "u testUpdateUser User";
		String pw = "p testUpdateUser User";
		assertTrue(controller.insertNewUser(fName, lName, email, uname, pw, null, null));
		User user = controller.selectUserBy(Table_User.COLUMN_USERNAME, uname).get(0);
		
		// Change first name, last name, email, username and password
		fName = "f testUpdateUser User Changed";
		lName = "l testUpdateUser User Changed";
		email = "e testUpdateUser User Changed";
		uname = "u testUpdateUser User Changed";
		pw = "p testUpdateUser User Changed";
		user.setFirstName(fName);
		user.setLastName(lName);
		user.setEmailAddress(email);
		user.setUsername(uname);
		user.setPassword(pw);
		assertTrue(controller.updateUser(user));
		// Check if database was changed
		User dbUser = controller.selectUserBy(Table_User.COLUMN_ID, user.getUserID()).get(0);
		assertTrue(user.getFirstName().equals(dbUser.getFirstName()));
		assertTrue(user.getLastName().equals(dbUser.getLastName()));
		assertTrue(user.getEmailAddress().equals(dbUser.getEmailAddress()));
		assertTrue(user.getUsername().equals(dbUser.getUsername()));
		assertTrue(user.getPassword().equals(dbUser.getPassword()));
		
		// Test null function
		user.setFunction(null);
		assertTrue(controller.updateUser(user));
		dbUser = controller.selectUserBy(Table_User.COLUMN_ID, user.getUserID()).get(0);
		assertTrue(dbUser.getFunction() == null);
		
		// Insert test function
		assertTrue(controller.insertNewFunction("testUpdateUser Function"));
		Function testFunction = controller.selectFunctionBy(Table_Function.COLUMN_FUNCTION, "testUpdateUser Function").get(0);
		
		// Assign function to user
		user.setFunction(testFunction);
		assertTrue(controller.updateUser(user));
		dbUser = controller.selectUserBy(Table_User.COLUMN_ID, user.getUserID()).get(0);
		assertTrue(dbUser.getFunction().getId().equals(testFunction.getId()));
		assertTrue(dbUser.getFunction().getFunction().equals(testFunction.getFunction()));
		
		// Delete user
		assertTrue(controller.deleteUser(user.getUserID()));
		// Delete function
		assertTrue(controller.deleteFunction(testFunction.getId()));
	}

	// --- ROW ---
	@Test
	public void testNullResultSet() {
		DBController controller = DBController.getInstance();
		assertTrue(controller.selectFunctionBy(Table_Function.COLUMN_ID, -1).size() == 0);
	}

	// --- ENUMS ---
	@Test
	public void testDBEnums() {
		// Function
		assertTrue(Table_Function.values().length == 3);
		assertTrue(Table_Function.valueOf(Table_Function.class, "CLOUMN_ALL").equals(Table_Function.CLOUMN_ALL));
		assertTrue(Table_Function.valueOf("CLOUMN_ALL").equals(Table_Function.CLOUMN_ALL));
		assertTrue(Table_Function.valueOf(Table_Function.class, "COLUMN_ID").equals(Table_Function.COLUMN_ID));
		assertTrue(Table_Function.valueOf("COLUMN_ID").equals(Table_Function.COLUMN_ID));
		assertTrue(
				Table_Function.valueOf(Table_Function.class, "COLUMN_FUNCTION").equals(Table_Function.COLUMN_FUNCTION));
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
		assertTrue(
				Table_Room.valueOf(Table_Room.class, "COLUMN_NUMBEROFSEATS").equals(Table_Room.COLUMN_NUMBEROFSEATS));
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
		assertTrue(
				Table_Reservation.valueOf(Table_Reservation.class, "CLOUMN_ALL").equals(Table_Reservation.CLOUMN_ALL));
		assertTrue(Table_Reservation.valueOf("CLOUMN_ALL").equals(Table_Reservation.CLOUMN_ALL));
		assertTrue(Table_Reservation.valueOf(Table_Reservation.class, "COLUMN_ID").equals(Table_Reservation.COLUMN_ID));
		assertTrue(Table_Reservation.valueOf("COLUMN_ID").equals(Table_Reservation.COLUMN_ID));
		assertTrue(Table_Reservation.valueOf(Table_Reservation.class, "COLUMN_STARTDATE")
				.equals(Table_Reservation.COLUMN_STARTDATE));
		assertTrue(Table_Reservation.valueOf("COLUMN_STARTDATE").equals(Table_Reservation.COLUMN_STARTDATE));
		assertTrue(Table_Reservation.valueOf(Table_Reservation.class, "COLUMN_ENDDATE")
				.equals(Table_Reservation.COLUMN_ENDDATE));
		assertTrue(Table_Reservation.valueOf("COLUMN_ENDDATE").equals(Table_Reservation.COLUMN_ENDDATE));
		assertTrue(Table_Reservation.valueOf(Table_Reservation.class, "COLUMN_ROOMID")
				.equals(Table_Reservation.COLUMN_ROOMID));
		assertTrue(Table_Reservation.valueOf("COLUMN_ROOMID").equals(Table_Reservation.COLUMN_ROOMID));
		assertTrue(Table_Reservation.valueOf(Table_Reservation.class, "COLUMN_TITLE")
				.equals(Table_Reservation.COLUMN_TITLE));
		assertTrue(Table_Reservation.valueOf("COLUMN_TITLE").equals(Table_Reservation.COLUMN_TITLE));
		assertTrue(Table_Reservation.valueOf(Table_Reservation.class, "COLUMN_DESCRIPTION")
				.equals(Table_Reservation.COLUMN_DESCRIPTION));
		assertTrue(Table_Reservation.valueOf("COLUMN_DESCRIPTION").equals(Table_Reservation.COLUMN_DESCRIPTION));
	}

	@AfterClass
	public static void disconnect() throws SQLException {
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
