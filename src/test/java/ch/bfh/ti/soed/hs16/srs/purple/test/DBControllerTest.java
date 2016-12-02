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

	@Test
	public void testSelectFunctionByID(){
		DBController controller = DBController.getInstance();
		List<Function> functions = controller.selectFunctionsBy(Table_Function.COLUMN_ID,9);

		assertTrue(functions.size() == 1);
		assertTrue(functions.get(0).getId() == 9);
		assertTrue(functions.get(0).getFunction().equals("Dozent"));
	}
	
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

	@Test
	public void testSelectFunctionByFunction(){
		DBController controller = DBController.getInstance();
		List<Function> functions = controller.selectFunctionsBy(Table_Function.COLUMN_FUNCTION,"Student");

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

	@Test
	public void testInsertNewReservation(){
		// TODO: dont insert new reservation for same date and room
//		DBController controller = DBController.getInstance();
//		Timestamp startDate = Timestamp.valueOf("2016-12-08 08:00:00.000000");
//		Timestamp endDate = Timestamp.valueOf("2016-12-08 08:00:00.000000");
//		Room room = DBController.getInstance().selectRoomBy(Table_Room.COLUMN_ID, 1).get(0);
//		List<User> hosts = DBController.getInstance().selectUserBy(Table_User.COLUMN_ID, 1);
//		List<User> participants = DBController.getInstance().selectUserBy(Table_User.COLUMN_ID, 2);
//		String title = "Test Title";
//		String description = "Test Description";
//		assertTrue(controller.insertNewReservation(startDate, endDate, room, hosts, participants, title, description));
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
