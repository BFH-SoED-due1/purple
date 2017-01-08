/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ch.bfh.ti.soed.hs16.srs.purple.model.Function;
import ch.bfh.ti.soed.hs16.srs.purple.model.HostUser;
import ch.bfh.ti.soed.hs16.srs.purple.model.Reservation;
import ch.bfh.ti.soed.hs16.srs.purple.model.Role;
import ch.bfh.ti.soed.hs16.srs.purple.model.Room;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;
import ch.bfh.ti.soed.hs16.srs.purple.util.Row;

/**
 * Controls the connection for the database. Every operation performed on the
 * database by this application must go through this controller.
 *
 * @author Elias Schildknecht
 */
public class DBController {

	private String dbHost;
	private String db;
	private String dbUser;
	private String dbUserPassword;

	private Connection connection;

	private static DBController instance;

	public enum Table_Function {
		COLUMN_ID("idfunction"), COLUMN_FUNCTION("function"), CLOUMN_ALL("*");

		private final String column;

		Table_Function(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}

	public enum Table_Role {
		COLUMN_ID("idrole"), COLUMN_ROLE("role"), CLOUMN_ALL("*");

		private final String column;

		Table_Role(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}

	public enum Table_Room {
		COLUMN_ID("idroom"), COLUMN_ROOMNUMBER("roomnumber"), COLUMN_NAME("name"), COLUMN_NUMBEROFSEATS(
				"numberofseats"), CLOUMN_ALL("*");

		private final String column;

		Table_Room(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}

	public enum Table_User {
		COLUMN_ID("iduser"), COLUMN_FIRSTNAME("firstname"), COLUMN_LASTNAME("lastname"), COLUMN_EMAIL(
				"email"), COLUMN_USERNAME("username"), COLUMN_PASSWORD(
						"password"), COLUMN_FUNCTIONID("functionid"), COLUMN_ROLEID("roleid"), CLOUMN_ALL("*");

		private final String column;

		Table_User(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}

	public enum Table_Reservation {
		COLUMN_ID("idreservation"), COLUMN_STARTDATE("startdate"), COLUMN_ENDDATE("enddate"), COLUMN_ROOMID(
				"roomid"), COLUMN_TITLE("title"), COLUMN_DESCRIPTION("description"), CLOUMN_ALL("*");

		private final String column;

		Table_Reservation(String column) {
			this.column = column;
		}

		public String getValue() {
			return column;
		}
	}

	private DBController() {
		dbHost = "mysql22.webland.ch";
		db = "brave_res_tool";
		dbUser = "brave_res_tool";
		dbUserPassword = "SoED-purple1";

		try {
			connect();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO: log exceptions
		}
	}

	public static synchronized DBController getInstance() {
		if (instance == null) {
			instance = new DBController();
		}
		return instance;
	}

	/**
	 * Connects to the database through jdbc and the underlining mysql-jdbc
	 * driver.
	 *
	 * @throws ClassNotFoundException
	 *             if the mysql-jdbc driver class cannot be located.
	 * @throws SQLException
	 *             if a database access error occurs.
	 */
	public void connect() throws ClassNotFoundException, SQLException {
		// Load mysql-jdbc driver
		Class.forName("com.mysql.jdbc.Driver");
		// Connect to database
		connection = DriverManager.getConnection("jdbc:mysql://" + dbHost + "/" + db + "", dbUser, dbUserPassword);
	}

	/**
	 * Disconnects from the database. It is strongly recommended that an
	 * application explicitly commits or rolls back an active transaction prior
	 * to calling the disconnect method.
	 *
	 * @throws SQLException
	 *             if a database access error occurs.
	 */
	public void disconnect() throws SQLException {
		connection.close();
	}

	// --- DELETE METHODS ---
	public boolean deleteReservation(Integer id) {
		boolean success = false;
		String deleteUserReservation = "DELETE FROM userreservation WHERE reservationid = " + id;
		success = executeUpdate(deleteUserReservation).isSuccess();
		String deleteReservation = "DELETE FROM reservation WHERE idreservation = " + id;
		success = executeUpdate(deleteReservation).isSuccess();
		return success;
	}

	public boolean deleteFunction(Integer id) {
		String deleteFunction = "DELETE FROM function WHERE idfunction = " + id;
		return executeUpdate(deleteFunction).isSuccess();
	}

	public boolean deleteRole(Integer id) {
		String deleteRole = "DELETE FROM role WHERE idrole = " + id;
		return executeUpdate(deleteRole).isSuccess();
	}

	/**
	 * Deletes a room and all reservations for this room.
	 *
	 * @param id
	 *            - The id of the room
	 * @return true if the deletion was successful - false otherwise
	 */
	public boolean deleteRoom(Integer id) {
		String deleteRoom = "DELETE FROM room WHERE idroom = " + id;
		return executeUpdate(deleteRoom).isSuccess();
	}

	public boolean deleteUser(Integer id) {
		// Delete user
		String deleteUser = "DELETE FROM user WHERE iduser = " + id;
		return executeUpdate(deleteUser).isSuccess();
	}

	// --- INSERT METHODS ---
	public boolean insertNewReservation(Timestamp startDate, Timestamp endDate, Room room, List<User> hosts, List<User> participants, String title, String description){
		if(room == null) return false;
		// Check if the room is free for the given time interval
		List<Room> freeRooms = selectFreeRooms(startDate, endDate);
		boolean isFree = false;
		for(Room freeRoom : freeRooms) {
			if(freeRoom.getRoomID().equals(room.getRoomID())) isFree = true;
		}

		// Check if a host is assigned (at least one host must be assigned)
		boolean hasHost = false;
		if(hosts != null && !hosts.isEmpty()) hasHost = true;

		// If room is free for the given time interval and a host is assigned -> insert new reservation
		if(isFree && hasHost) {
			String insertReservation = "INSERT INTO reservation(IDReservation, StartDate, EndDate, RoomID, Title, Description) "
					+ "VALUES(null,'" + startDate + "','" + endDate + "','" + room.getRoomID() + "','" + title + "','"
					+ description + "')";
			ExecuteResult resultReservation = executeUpdate(insertReservation);
			for (Integer reservationID : resultReservation.getGeneratedIDs()) {
				if (hosts != null) {
					for (User user : hosts) {
						String insertHosts = "INSERT INTO userreservation(reservationid,userid,host,accept) " + "VALUES ("
								+ reservationID + "," + user.getUserID() + ",1,1)";
						resultReservation.setSuccess(executeUpdate(insertHosts).isSuccess());
					}
				}
				if (participants != null) {
					for (User user : participants) {
						String insertParticipants = "INSERT INTO userreservation(reservationid,userid,host,accept) " + "VALUES ("
								+ reservationID + "," + user.getUserID() + ",0,0)";
						resultReservation.setSuccess(executeUpdate(insertParticipants).isSuccess());
					}
				}
			}
			return resultReservation.isSuccess();
		}
		return false;
	}

	public boolean insertNewRole(String role) {
		String insertRole = "INSERT INTO role(IDRole, Role) VALUES(null,'" + role + "')";
		return executeUpdate(insertRole).isSuccess();
	}

	public boolean insertNewRoom(int roomNumber, String name, int numberOfSeats) {
		String insertRoom = "INSERT INTO room(IDRoom, RoomNumber, Name, NumberOfSeats) " + "VALUES(null,'" + roomNumber
				+ "','" + name + "','" + numberOfSeats + "')";
		return executeUpdate(insertRoom).isSuccess();
	}

	public boolean insertNewUser(String firstName, String lastName, String email, String username, String password,
			Function function, Role role) {
		String idFunction = (function == null) ? "NULL" : function.getId().toString();
		String idRole = (role == null) ? "NULL" : role.getId().toString();
		String insertUser = "INSERT INTO user(IDUser, FirstName, LastName, Email, Username, Password, FunctionID, RoleID) "
				+ "VALUES(null,'" + firstName + "','" + lastName + "','" + email + "','" + username + "','" + password
				+ "'," + idFunction + "," + idRole + ")";
		return executeUpdate(insertUser).isSuccess();
	}

	/**
	 * Stores a new function in the database.
	 *
	 * @param function
	 *            - The function name
	 * @return true if the function was successfully inserted into the database
	 *         or false otherwise.
	 */
	public boolean insertNewFunction(String function) {
		String insertFunction = "INSERT INTO function(IDFunction, Function) VALUES(null,'" + function + "')";
		return executeUpdate(insertFunction).isSuccess();
	}

	// --- SELECT METHODS ---
	public List<Function> selectAllFunctions() {
		return selectFunctionBy(Table_Function.CLOUMN_ALL, null);
	}

	public List<Role> selectAllRoles() {
		return selectRoleBy(Table_Role.CLOUMN_ALL, null);
	}

	public List<Room> selectAllRooms() {
		return selectRoomBy(Table_Room.CLOUMN_ALL, null);
	}

	public List<User> selectAllUsers() {
		return selectUserBy(Table_User.CLOUMN_ALL, null);
	}

	public List<Reservation> selectAllReservations() {
		return selectReservationBy(Table_Reservation.CLOUMN_ALL, null);
	}

	/**
	 * Gets all reservations associated with the given user.
	 *
	 * @param user
	 *            - The user who takes part of a reservation
	 * @param host
	 *            - Select reservations in which the user is a host or not
	 * @return A list containing all the reservations
	 */
	public List<Reservation> selectReservationsForUser(User user, boolean host) {
		Integer isHost = (host) ? 1 : 0;
		List<Reservation> reservations = new ArrayList<Reservation>();
		// Get reservations by host
		String selectHostreservationsForUser = "SELECT reservationid FROM userreservation WHERE host = " + isHost
				+ " AND userid = " + user.getUserID();
		List<Row> hostReservations = executeSelect(selectHostreservationsForUser);
		for (Row row : hostReservations) {
			Integer reservationID = row.getRow().get(0).getValue() == Integer.class
					? (Integer) row.getRow().get(0).getKey() : null;
			// Add "host"-reservation to list
			reservations.add(this.selectReservationBy(Table_Reservation.COLUMN_ID, reservationID).get(0));
		}
		return reservations;
	}

	public <T> List<Function> selectFunctionBy(Table_Function column, T value) {
		String selectStmt = "SELECT * FROM function WHERE " + column.getValue();
		if (value != null)
			selectStmt += " = '" + value + "'";
		else
			selectStmt += " IS NULL";
		if (column.equals(Table_Function.CLOUMN_ALL))
			selectStmt = "SELECT * FROM function";
		List<Function> functions = new ArrayList<Function>();

		for (Row row : executeSelect(selectStmt)) {
			Integer idFunction = (Integer) row.getRow().get(0).getKey();
			String function = row.getRow().get(1).getValue() == String.class ? (String) row.getRow().get(1).getKey()
					: null;
			functions.add(new Function(idFunction, function));
		}
		return functions;
	}

	public <T> List<Role> selectRoleBy(Table_Role column, T value) {
		String selectStmt = "SELECT * FROM role WHERE " + column.getValue();
		if (value != null)
			selectStmt += " = '" + value + "'";
		else
			selectStmt += " IS NULL";
		if (column.equals(Table_Role.CLOUMN_ALL))
			selectStmt = "SELECT * FROM role";
		List<Role> roles = new ArrayList<Role>();

		for (Row row : executeSelect(selectStmt)) {
			Integer idRole = (Integer) row.getRow().get(0).getKey();
			String role = (String) row.getRow().get(1).getKey();
			roles.add(new Role(idRole, role));
		}
		return roles;
	}

	public <T> List<Room> selectRoomBy(Table_Room column, T value) {
		String selectStmt = "SELECT * FROM room WHERE " + column.getValue();
		if (value != null)
			selectStmt += " = '" + value + "'";
		else
			selectStmt += " IS NULL";
		if (column.equals(Table_Room.CLOUMN_ALL))
			selectStmt = "SELECT * FROM room";
		List<Room> rooms = new ArrayList<Room>();

		for (Row row : executeSelect(selectStmt)) {
			Integer idRoom = (Integer) row.getRow().get(0).getKey();
			Integer roomNumber = (Integer) row.getRow().get(1).getKey();
			String name = (String) row.getRow().get(2).getKey();
			Integer numberOfSeats = (Integer) row.getRow().get(3).getKey();
			rooms.add(new Room(idRoom, roomNumber, name, numberOfSeats));
		}
		return rooms;
	}

	public <T> List<User> selectUserBy(Table_User column, T value) {
		String selectStmt = "SELECT * FROM user WHERE " + column.getValue();
		if (value != null)
			selectStmt += " = '" + value + "'";
		else
			selectStmt += " IS NULL";
		if (column.equals(Table_User.CLOUMN_ALL))
			selectStmt = "SELECT * FROM user";
		List<User> users = new ArrayList<User>();

		ArrayList<Function> functions = new ArrayList<Function>();
		ArrayList<Role> roles = new ArrayList<Role>();
		for (Row row : executeSelect(selectStmt)) {
			Integer idUser = (Integer) row.getRow().get(0).getKey();
			String firstName = (String) row.getRow().get(1).getKey();
			String lastName = (String) row.getRow().get(2).getKey();
			String email = (String) row.getRow().get(3).getKey();
			String username = (String) row.getRow().get(4).getKey();
			String password = (String) row.getRow().get(5).getKey();
			Integer idFunction = (Integer) row.getRow().get(6).getKey();
			Integer idRole = (Integer) row.getRow().get(7).getKey();

			// Check if user has a function
			Function functionObject = null;
			if (idFunction != null) {
				// Check if a function was already loaded before
				for (Function existingFunction : functions)
					if (existingFunction.getId() == idFunction)
						functionObject = existingFunction;
				// If not, create new function and add it to the list
				if (functionObject == null) {
					functionObject = selectFunctionBy(Table_Function.COLUMN_ID, idFunction).get(0);
					functions.add(functionObject);
				}
			}

			// Check if user has a role
			Role roleObject = null;
			if (idRole != null) {
				// Check if a role was already loaded before
				for (Role existingRole : roles)
					if (existingRole.getId() == idRole)
						roleObject = existingRole;
				// If not, create new role and add it to the list
				if (roleObject == null) {
					roleObject = selectRoleBy(Table_Role.COLUMN_ID, idRole).get(0);
					roles.add(roleObject);
				}
			}

			// TODO: User or HostUser needed?
			if (idFunction == null) {
				users.add(new User(idUser, firstName, lastName, email, username, password, roleObject, functionObject));
			} else {
				users.add(new HostUser(idUser, firstName, lastName, email, username, password, roleObject,
						functionObject));
			}
		}
		return users;
	}

	public <T> List<Reservation> selectReservationBy(Table_Reservation column, T value) {
		String selectStmt = "SELECT * FROM reservation WHERE " + column.getValue();
		if (value != null)
			selectStmt += " = '" + value + "'";
		else
			selectStmt += " IS NULL";
		if (column.equals(Table_Reservation.CLOUMN_ALL))
			selectStmt = "SELECT * FROM reservation";
		List<Reservation> reservations = new ArrayList<Reservation>();

		ArrayList<Room> rooms = new ArrayList<Room>();
		ArrayList<User> users = new ArrayList<User>();
		for (Row row : executeSelect(selectStmt)) {
			Integer idReservation = (Integer) row.getRow().get(0).getKey();
			Timestamp startDate = (Timestamp) row.getRow().get(1).getKey();
			Timestamp endDate = (Timestamp) row.getRow().get(2).getKey();
			Integer idroom = (Integer) row.getRow().get(3).getKey();
			String title = (String) row.getRow().get(4).getKey();
			String description = (String) row.getRow().get(5).getKey();

			// Check if a room was already loaded before
			Room roomObject = null;
			for (Room existingRoom : rooms)
				if (existingRoom.getRoomID() == idroom)
					roomObject = existingRoom;
			// If not, create a new room and add it to the list (room is foreign
			// key and cannot be null)
			if (roomObject == null)
				rooms.add(roomObject = selectRoomBy(Table_Room.COLUMN_ID, idroom).get(0));

			Reservation reservation = new Reservation(idReservation, startDate, endDate, roomObject, title,
					description);

			// Select users for reservation from userreservation table
			String selectUsersForReservation = "SELECT userid,host,accept FROM userreservation ur INNER JOIN user u ON ur.userid = u.iduser "
					+ "WHERE ur.reservationid = " + idReservation;
			for (Row urRow : executeSelect(selectUsersForReservation)) {
				Integer userId = (Integer) urRow.getRow().get(0).getKey();
				boolean host = (Boolean) urRow.getRow().get(1).getKey();
				boolean accept = (Boolean) urRow.getRow().get(2).getKey();

				User user = null;
				// Check if a specific user was already loaded before
				for (User existingUser : users)
					if (existingUser.getUserID() == userId)
						user = existingUser;
				// If not, load user and store him in the list (user is primary
				// key with reservationid and cannot be null)
				if (user == null)
					users.add(user = selectUserBy(Table_User.COLUMN_ID, userId).get(0));

				// Add the user to the reservation
				if (host) {
					reservation.addHost(user);
				} else {
					reservation.addParticipant(user);
					// if the user has accepted the reservation -> add him to the accepted participants list
					if(accept) reservation.addAcceptedParticipant(user);
				}
			}
			// Add reservation to the list
			reservations.add(reservation);
		}
		return reservations;
	}

	/**
	 * Returns all free rooms between a given time interval.
	 *
	 * @param startDate - Interval begin.
	 * @param endDate - Interval end.
	 * @return A list containing all free rooms between the given time interval.
	 * */
	public List<Room> selectFreeRooms(Timestamp startDate, Timestamp endDate) {
		List<Room> freeRooms = new ArrayList<Room>();
		// Get all reservations before and after the given time interval

		String selectStmt = "SELECT distinct(roomid) "
				+ "FROM reservation "
				+ "WHERE roomid "
				+ "NOT IN("
					+ "SELECT roomid "
					+ "FROM reservation "
					+ "WHERE (startdate <= '"+startDate+"' AND enddate >= '"+endDate+"') "
						+ "OR (startdate > '"+startDate+"' AND startdate < '"+endDate+"') "
						+ "OR (enddate > '"+startDate+"' AND enddate < '"+endDate+"'))";
		for (Row row : executeSelect(selectStmt)) {
			Integer roomid = (Integer) row.getRow().get(0).getKey();
			// get room java-instances
			freeRooms.add(selectRoomBy(Table_Room.COLUMN_ID, roomid).get(0));
		}

		// Get free rooms without assigned reservations
		String selectFreeRoomsStmt = "SELECT idroom FROM room WHERE idroom NOT IN (SELECT distinct(idroom) "
				+ "FROM room JOIN reservation ON room.idroom = reservation.roomid)";
		for (Row row : executeSelect(selectFreeRoomsStmt)) {
			Integer idRoom = (Integer) row.getRow().get(0).getKey();
			// get room java-instances
			freeRooms.add(selectRoomBy(Table_Room.COLUMN_ID, idRoom).get(0));
		}
		return freeRooms;
	}

	// --- UPDATE METHODS ---
	/**
	 * Sets a user to be the host or not for a specific reservation.
	 * User must be assigned to the reservation.
	 *
	 * @param user - user which should be host or not
	 * @param reservation - the reservation for which the user should be host or not
	 * @param isHost - if the user should be host or not
	 *
	 * @return true if the update was successfully - false otherwise.
	 * */
	public boolean updateHostReservation(User user, Reservation reservation, boolean isHost) {
		boolean hasAccepted = reservation.hasParticipantAcceptedReservation(user);
		if(isHost) {
			String updateStmt = "UPDATE userreservation SET host = "+isHost+", accept = "+hasAccepted+" WHERE userid = "+user.getUserID()+" AND reservationid = "+reservation.getReservationID();
			if(executeUpdate(updateStmt).isSuccess()) {
				reservation.addHost(user);
				reservation.removeParticipant(user);
				return true;
			}
			return false;
		} else {
			if(reservation.getHostList().size() > 1) {
				String updateStmt = "UPDATE userreservation SET host = "+isHost+", accept = "+hasAccepted+" WHERE userid = "+user.getUserID()+" AND reservationid = "+reservation.getReservationID();
				if(executeUpdate(updateStmt).isSuccess()) {
					reservation.removeHost(user);
					reservation.addParticipant(user);
					if(hasAccepted) reservation.getAcceptedParticipantsList().add(user);
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Accepts or cancels a specific reservation for a user.
	 * User must be assigned to the reservation.
	 *
	 * @param user - user which should accept the reservation or not
	 * @param reservation - the reservation which the user should accept or not
	 * @param accept - if the user accepts the reservation or not
	 *
	 * @return true if the update was successfully - false otherwise.
	 * */
	public boolean updateAcceptReservation(User user, Reservation reservation, boolean accept) {
		String updateStmt = "UPDATE userreservation SET accept = "+accept+" WHERE userid = "+user.getUserID()+" AND reservationid = "+reservation.getReservationID();
		boolean updateSucces = executeUpdate(updateStmt).isSuccess();
		if(updateSucces) {
			if(accept) {
				reservation.addAcceptedParticipant(user);
			} else {
				User toDelete = null;
				for(User acceptedParticipant : reservation.getAcceptedParticipantsList()) {
					if(acceptedParticipant.getUserID().equals(user.getUserID())) toDelete = acceptedParticipant;
				}
				reservation.getAcceptedParticipantsList().remove(toDelete);
			}
		}
		return updateSucces;
	}

	/**
	 * Accepts or cancels all reservations from the list for a user.
	 * User must be assigned to the reservation.
	 *
	 * @param user - user which should accept the reservation or not
	 * @param reservations - the reservations which the user should accept or not
	 * @param accept - if the user accepts the reservation or not
	 *
	 * @return true if the update was successfully - false otherwise.
	 * */
	public boolean updateAcceptReservation(User user, List<Reservation> reservations, boolean accept) {
		String updateStmt = "UPDATE userreservation SET accept = "+accept+" WHERE ";
		for(Reservation reservation : reservations)  {
			updateStmt += "(userid = "+user.getUserID()+" AND reservationid = "+reservation.getReservationID()+")";
			if(reservations.indexOf(reservation) < reservations.size() - 1) updateStmt +=" OR ";
		}
		boolean success = executeUpdate(updateStmt).isSuccess();
		if(success) {
			for(Reservation reservation : reservations)  {
				if(accept) {
					reservation.addAcceptedParticipant(user);
				} else {
					User toDelete = null;
					for(User acceptedParticipant : reservation.getAcceptedParticipantsList()) {
						if(acceptedParticipant.getUserID().equals(user.getUserID())) toDelete = acceptedParticipant;
					}
					reservation.getAcceptedParticipantsList().remove(toDelete);
				}
			}
		}
		return success;
	}

	/**
	 * Updates a reservation in the database.
	 * Only updates a reservation if there are no conflicts with other reservations/times and rooms.
	 *
	 * @param editedReservation - The reservation which was edited
	 * @return true if the reservation has been updated successfully in the database - false otherwise.
	 * */
	public boolean updateReservation(Reservation editedReservation) {
		// Get reservation before update
		Reservation reservation = selectReservationBy(Table_Reservation.COLUMN_ID, editedReservation.getReservationID()).get(0);
		Timestamp oldStartDate = reservation.getStartDate();
		Timestamp oldEndDate = reservation.getEndDate();
		Timestamp newStartDate = editedReservation.getStartDate();
		Timestamp newEndDate = editedReservation.getEndDate();
		Room oldRoom = reservation.getRoom();
		Room newRoom = editedReservation.getRoom();
		// Check if the reservation has a new room or not and if the room is free for the new time interval
		boolean isNewRoomFree = false;
		boolean isOldRoomFree = false;
		if(oldRoom.getRoomID().equals(newRoom.getRoomID())) {
			boolean isFreeBefore = false;
			boolean isFreeAfter = false;
			// If the reservation begins earlier -> check if room is free before
			if(newStartDate.getTime() < oldStartDate.getTime()) {
				for(Room room : selectFreeRooms(newStartDate, oldStartDate)) {
					if(room.getRoomID().equals(oldRoom.getRoomID())) isFreeBefore = true;
				}
			}
			// If the reservation ends later -> check if room is free after
			if(newEndDate.getTime() > oldEndDate.getTime()) {
				for(Room room : selectFreeRooms(oldEndDate, newEndDate)) {
					if(room.getRoomID().equals(oldRoom.getRoomID())) isFreeAfter = true;
				}
			}

			// If reservation only starts earlier
			if(newStartDate.getTime() < oldStartDate.getTime() && newEndDate.getTime() <= oldEndDate.getTime()) {
				if(isFreeBefore) isOldRoomFree = true;
			} else
				// If reservation only ends later
				if(newStartDate.getTime() >= oldStartDate.getTime() && newEndDate.getTime() > oldEndDate.getTime()) {
					if(isFreeAfter) isOldRoomFree = true;
			} else
				// If reservation starts earlier and ends later
				if(newStartDate.getTime() < oldStartDate.getTime() && newEndDate.getTime() > oldEndDate.getTime()) {
					if(isFreeBefore && isFreeAfter) isOldRoomFree = true;
			}

			// If the new time interval is between the old time interval -> reservation can be updated
			if(newStartDate.getTime() >= oldStartDate.getTime() && newEndDate.getTime() <= oldEndDate.getTime()) {
				isOldRoomFree = true;
			}
		} else {
			// Check if new room is free for the time interval
			for(Room room : selectFreeRooms(newStartDate, newEndDate)) {
				if(room.getRoomID().equals(newRoom.getRoomID())) isNewRoomFree = true;
			}
		}

		// If the new room or the old room is free for the new time interval -> update reservation
		boolean successUpdate = false;
		boolean successDeleteHosts = true;
		boolean successDeleteParticipants = true;
		boolean successInsertHosts = true;
		boolean successInsertparticipants = true;
		if(isNewRoomFree || isOldRoomFree) {
			// Update reservation
			String updateStmt = "UPDATE reservation SET startdate = '"+editedReservation.getStartDate()+"', enddate = '"+editedReservation.getEndDate()+"', "
					+ "roomid = "+editedReservation.getRoom().getRoomID()+", title = '"+editedReservation.getTitle()+"', description = '"+editedReservation.getDescription()+"' "
							+ "WHERE idreservation = "+editedReservation.getReservationID();
			successUpdate = executeUpdate(updateStmt).isSuccess();

			// Update assigned hosts and participants
			List<User> oldHosts = reservation.getHostList();
			List<User> newHosts = editedReservation.getHostList();
			List<User> oldParticipants = reservation.getParticipantList();
			List<User> newParticipants = editedReservation.getParticipantList();

			List<User> hostsToAdd = getLeftDiffUsers(newHosts, oldHosts);
			List<User> hostsToDelete = getLeftDiffUsers(oldHosts, newHosts);
			List<User> participantsToAdd = getLeftDiffUsers(newParticipants, oldParticipants);
			List<User> participantsToDelete = getLeftDiffUsers(oldParticipants, newParticipants);

			// Add and Delete hosts and participants
			if(hostsToDelete.size() > 0) {
				updateStmt = "DELETE FROM userreservation WHERE ";
				for(User hostToDelete : hostsToDelete) {
					updateStmt += "(reservationid = "+editedReservation.getReservationID()+" AND userid = "+hostToDelete.getUserID()+")";
					if(hostsToDelete.indexOf(hostToDelete) < hostsToDelete.size() - 1) updateStmt +=" OR ";
				}
				successDeleteHosts = executeUpdate(updateStmt).isSuccess();
			}

			if(participantsToDelete.size() > 0) {
				updateStmt = "DELETE FROM userreservation WHERE ";
				for(User participantToDelete : participantsToDelete) {
					updateStmt += "(reservationid = "+editedReservation.getReservationID()+" AND userid = "+participantToDelete.getUserID()+")";
					if(participantsToDelete.indexOf(participantToDelete) < participantsToDelete.size() - 1) updateStmt +=" OR ";
				}
				successDeleteParticipants = executeUpdate(updateStmt).isSuccess();
			}

			for(User hostToAdd : hostsToAdd) {
				updateStmt = "INSERT INTO userreservation(reservationid, userid, host, accept) "
						+ "VALUES("+editedReservation.getReservationID()+", "+hostToAdd.getUserID()+",1,1)";
				successInsertHosts = executeUpdate(updateStmt).isSuccess();
			}

			for(User participantToAdd : participantsToAdd) {
				updateStmt = "INSERT INTO userreservation(reservationid, userid, host, accept) "
						+ "VALUES("+editedReservation.getReservationID()+", "+participantToAdd.getUserID()+",0,0)";
				successInsertparticipants = executeUpdate(updateStmt).isSuccess();
			}
		}
		return successUpdate && successDeleteHosts && successDeleteParticipants && successInsertHosts && successInsertparticipants;
	}

	/**
	 * Gets the users from the left list which aren't in the right list.
	 *
	 * @param leftList - List with users.
	 * @param rightList - List with users.
	 *
	 * @return A list containing all the users from the left list which aren't in the right list.
	 * */
	private List<User> getLeftDiffUsers(List<User> leftList, List<User> rightList) {
		List<User> diffUsers = new ArrayList<User>();
		for(User leftUser : leftList) {
			boolean diff = true;
			for(User rightUser : rightList) {
				if(leftUser.getUserID().equals(rightUser.getUserID())) diff = false;
			}
			if(diff) diffUsers.add(leftUser);
		}
		return diffUsers;
	}

	/**
	 * Function updates an user. Does not update a new role.
	 *
	 * @param user - user to update
	 * @return true if the update was successfully - false otherwise
	 */
	public boolean updateUser(User user) {
		Integer functionID = null;
		if(user.getFunction() != null) functionID = user.getFunction().getId();

		String updateUser = "UPDATE user SET " + Table_User.COLUMN_FIRSTNAME.getValue() + " = '" + user.getFirstName()
				+ "'," + Table_User.COLUMN_LASTNAME.getValue() + " = '" + user.getLastName() + "',"
				+ Table_User.COLUMN_EMAIL.getValue() + " = '" + user.getEmailAddress() + "', "
				+ Table_User.COLUMN_PASSWORD.getValue() + " = '" + user.getPassword() + "',"
				+ Table_User.COLUMN_FUNCTIONID.getValue() + " = " + functionID + ","
				// TODO: check if new username already exists (username must be unique)
				+ Table_User.COLUMN_USERNAME.getValue() + " = '" + user.getUsername() + "' " + "WHERE "
				+ Table_User.COLUMN_ID.getValue() + " = " + user.getUserID() + ";";

		return executeUpdate(updateUser).isSuccess();
	}

	// --- UTIL METHODS ---
	// Select and update methods are synchronized because the database isolation
	// level may not be strong enough to lock these operations
	// on the database itself.
	// Can be changed if the database isolation level is high enough or the
	// performance is too low .
	// see ->
	// https://en.wikipedia.org/wiki/Isolation_(database_systems)#Read_uncommitted
	// and JAVA Monitoring and synchronizing.

	/**
	 * Performs an INSERT, UPDATE, or DELETE statement on the database.
	 *
	 * @param query
	 *            - The query to be executed.
	 * @return An ExecuteResult containing the result of the update operation.
	 */
	private synchronized ExecuteResult executeUpdate(String query) {
		Statement updateStmt = null;
		ExecuteResult result = new ExecuteResult();

		try {
			updateStmt = connection.createStatement();
			updateStmt.executeUpdate(query);
			ResultSet generatedKeys = updateStmt.getGeneratedKeys();
			int i = 1;
			while (generatedKeys.next()) {
				result.addID(generatedKeys.getInt(i));
				i++;
			}
			result.setSuccess(true);
		} catch (SQLException e) {
			// TODO: log exceptions
			System.out.println("SQlException: " + e.getMessage());
		} finally {
			try {
				// Release resources
				if (updateStmt != null)
					updateStmt.close();
			} catch (SQLException e) {
				// TODO: log exceptions
				System.out.println("SQlException: " + e.getMessage());
			}
		}
		return result;
	}

	/**
	 * Executes a select statement on the database.
	 *
	 * @param query
	 *            - The query to be executed.
	 * @return A list containing the rows returned from the select statement or
	 *         an empty list.
	 */
	private synchronized ArrayList<Row> executeSelect(String query) {
		ArrayList<Row> results = new ArrayList<Row>();

		Statement selectStmt = null;
		ResultSet result = null;
		try {
			selectStmt = connection.createStatement();
			result = selectStmt.executeQuery(query);
			// Fetch rows
			results = Row.formTable(result);
		} catch (SQLException e) {
			// TODO: log exceptions
		} finally {
			try {
				// Release resources
				if (selectStmt != null)
					selectStmt.close();
				if (result != null)
					result.close();
			} catch (SQLException e) {
				// TODO: log exceptions
			}
		}
		return results;
	}

	/**
	 * Used to get the result of an execute statement.
	 */
	class ExecuteResult {
		private boolean success = false;
		private List<Integer> generatedIDs;

		public ExecuteResult() {
			generatedIDs = new ArrayList<Integer>();
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public boolean isSuccess() {
			return success;
		}

		public void addID(Integer id) {
			generatedIDs.add(id);
		}

		public List<Integer> getGeneratedIDs() {
			return generatedIDs;
		}
	}
}
