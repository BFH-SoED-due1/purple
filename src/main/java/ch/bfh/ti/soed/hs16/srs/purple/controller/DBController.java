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
			e.printStackTrace();
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
	private void connect() throws ClassNotFoundException, SQLException {
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
		String deleteUserReservation = "DELETE FROM userreservation WHERE reservationid =" + id;
		success = executeUpdate(deleteUserReservation).isSuccess();
		String deleteReservation = "DELETE FROM reservation WHERE idreservation =" + id;
		success = executeUpdate(deleteReservation).isSuccess();
		return success;
	}

	// --- INSERT METHODS ---
	public boolean insertNewReservation(Timestamp startDate, Timestamp endDate, Room room, List<User> hosts,
			List<User> participants, String title, String description) {
		String insertReservation = "INSERT INTO reservation(IDReservation, StartDate, EndDate, RoomID, Title, Description) "
				+ "VALUES(null,'" + startDate + "','" + endDate + "','" + room.getRoomID() + "','" + title + "','"
				+ description + "')";
		ExecuteResult resultReservation = executeUpdate(insertReservation);
		if (resultReservation.isSuccess()) {
			for (Integer reservationID : resultReservation.getGeneratedIDs()) {
				for (User user : hosts) {
					String insertHosts = "INSERT INTO userreservation(reservationid,userid,host) " + "VALUES ("
							+ reservationID + "," + user.getUserID() + ",1)";
					resultReservation.setSuccess(executeUpdate(insertHosts).isSuccess());
				}
				for (User user : participants) {
					String insertParticipants = "INSERT INTO userreservation(reservationid,userid,host) " + "VALUES ("
							+ reservationID + "," + user.getUserID() + ",0)";
					resultReservation.setSuccess(executeUpdate(insertParticipants).isSuccess());
				}
			}
		}
		return resultReservation.isSuccess();
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

	public boolean insertNewUser(String firstName, String lastName, String email, String username, String password, Function function, Role role){
		String idFunction = (function == null) ? "NULL" : function.getId().toString();
		String idRole = (role == null) ? "NULL" : role.getId().toString();
		String insertUser = "INSERT INTO user(IDUser, FirstName, LastName, Email, Username, Password, FunctionID, RoleID) "
				+ "VALUES(null,'"+firstName+"','"+lastName+"','"+email+"','"+username+"','"+password+"',"+idFunction+","+idRole+")";
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
	public boolean insertFunction(String function) {
		String insertFunction = "INSERT INTO function(IDFunction, Function) VALUES(null,'" + function + "')";
		return executeUpdate(insertFunction).isSuccess();
	}

	// --- SELECT METHODS ---
	public List<Function> selectAllFunctions() {
		return selectFunctionsBy(Table_Function.CLOUMN_ALL, null);
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

	public List<Reservation> selectHostReservationsForUser(User user) {
		List<Reservation> reservations = new ArrayList<Reservation>();
		// TODO
		return reservations;
	}

	public List<Reservation> selectParticipantReservationsForUser(User user) {
		List<Reservation> reservations = new ArrayList<Reservation>();
		// TODO
		return reservations;
	}

	public <T> List<Function> selectFunctionsBy(Table_Function column, T value) {
		String selectStmt = "SELECT * FROM function WHERE " + column.getValue();
		if (value != null)
			selectStmt += " = '" + value + "'";
		else
			selectStmt += " IS NULL";
		if (column.equals(Table_Function.CLOUMN_ALL))
			selectStmt = "SELECT * FROM function";
		List<Function> functions = new ArrayList<Function>();

		for (Row row : executeSelect(selectStmt)) {
			Integer idFunction = row.getRow().get(0).getValue() == Integer.class
					? (Integer) row.getRow().get(0).getKey() : null;
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
			Integer idRole = row.getRow().get(0).getValue() == Integer.class ? (Integer) row.getRow().get(0).getKey()
					: null;
			String role = row.getRow().get(1).getValue() == String.class ? (String) row.getRow().get(1).getKey() : null;
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
			Integer idRoom = row.getRow().get(0).getValue() == Integer.class ? (Integer) row.getRow().get(0).getKey()
					: null;
			Integer roomNumber = row.getRow().get(1).getValue() == Integer.class
					? (Integer) row.getRow().get(1).getKey() : null;
			String name = row.getRow().get(2).getValue() == String.class ? (String) row.getRow().get(2).getKey() : null;
			Integer numberOfSeats = row.getRow().get(3).getValue() == Integer.class
					? (Integer) row.getRow().get(3).getKey() : null;
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
			Integer idUser = row.getRow().get(0).getValue() == Integer.class ? (Integer) row.getRow().get(0).getKey()
					: null;
			String firstName = row.getRow().get(1).getValue() == String.class ? (String) row.getRow().get(1).getKey()
					: null;
			String lastName = row.getRow().get(2).getValue() == String.class ? (String) row.getRow().get(2).getKey()
					: null;
			String email = row.getRow().get(3).getValue() == String.class ? (String) row.getRow().get(3).getKey()
					: null;
			String username = row.getRow().get(4).getValue() == String.class ? (String) row.getRow().get(4).getKey()
					: null;
			String password = row.getRow().get(5).getValue() == String.class ? (String) row.getRow().get(5).getKey()
					: null;
			Integer idFunction = row.getRow().get(6).getValue() == Integer.class
					? (Integer) row.getRow().get(6).getKey() : null;
			Integer idRole = row.getRow().get(7).getValue() == Integer.class ? (Integer) row.getRow().get(7).getKey()
					: null;

			// Check if user has a function
			Function functionObject = null;
			if (idFunction != null) {
				// Check if a function was already loaded before
				for (Function existingFunction : functions)
					if (existingFunction.getId() == idFunction)
						functionObject = existingFunction;
				// If not, create new function and add it to the list
				if (functionObject == null) {
					functionObject = selectFunctionsBy(Table_Function.COLUMN_ID, idFunction).get(0);
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
				users.add(new User(idUser, firstName, lastName, email, username, password, roleObject));
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
			Integer idReservation = row.getRow().get(0).getValue() == Integer.class
					? (Integer) row.getRow().get(0).getKey() : null;
			Timestamp startDate = row.getRow().get(1).getValue() == Timestamp.class
					? (Timestamp) row.getRow().get(1).getKey() : null;
			Timestamp endDate = row.getRow().get(2).getValue() == Timestamp.class
					? (Timestamp) row.getRow().get(2).getKey() : null;
			Integer idroom = row.getRow().get(3).getValue() == Integer.class ? (Integer) row.getRow().get(3).getKey()
					: null;
			String title = row.getRow().get(4).getValue() == String.class ? (String) row.getRow().get(4).getKey()
					: null;
			String description = row.getRow().get(5).getValue() == String.class ? (String) row.getRow().get(5).getKey()
					: null;

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
			String selectUsersForReservation = "SELECT userid,host FROM userreservation ur INNER JOIN user u ON ur.userid = u.iduser "
					+ "WHERE ur.reservationid = " + idReservation;
			for (Row urRow : executeSelect(selectUsersForReservation)) {
				Integer userId = urRow.getRow().get(0).getValue() == Integer.class
						? (Integer) urRow.getRow().get(0).getKey() : null;
				boolean host = urRow.getRow().get(1).getValue() == Boolean.class
						? (Boolean) urRow.getRow().get(1).getKey() : null;

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
				}
			}
			// Add reservation to the list
			reservations.add(reservation);
		}
		return reservations;
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
	 * @return true if the operation succeeded or false otherwise.
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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// Release resources
				if (updateStmt != null)
					updateStmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		result.setSuccess(true);
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
			e.printStackTrace();
		} finally {
			try {
				// Release resources
				if (selectStmt != null)
					selectStmt.close();
				if (result != null)
					result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return results;
	}

	/**
	 * Used to get the result of an execute statement.
	 */
	class ExecuteResult {
		private boolean success;
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
