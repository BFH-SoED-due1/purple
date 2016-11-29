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
 * Controls the connection for the database.
 * Every operation performed on the database by this application must go through this controller.
 * */
public class DBController {
	
	private String dbHost;
	private String db;
	private String dbUser;
	private String dbUserPassword;
	
	private Connection connection;
	
	private static DBController instance;
	
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
		if(instance == null){
			instance = new DBController();
		}
		return instance;
	}
	
	/**
	 * Connects to the database through jdbc and the underlining mysql-jdbc driver.
	 * 
	 * @throws ClassNotFoundException if the mysql-jdbc driver class cannot be located.
	 * @throws SQLException if a database access error occurs.
	 * */
	private void connect() throws ClassNotFoundException, SQLException {
		// Load mysql-jdbc driver
		Class.forName("com.mysql.jdbc.Driver");
		// Connect to database
		connection = DriverManager.getConnection("jdbc:mysql://"+dbHost+"/"+db+"",dbUser,dbUserPassword);
	}
	
	/**
	 * Disconnects from the database.
	 * It is strongly recommended that an application explicitly commits or rolls back an active transaction prior to calling the disconnect method.
	 * 
	 * @throws SQLException if a database access error occurs.
	 * */
	public void disconnect() throws SQLException {
		connection.close();
	}
	
	// --- INSERT METHODS ---
	public boolean insertNewReservation(Timestamp startDate, Timestamp endDate, Room room){
		String insertReservation = "INSERT INTO reservation(IDReservation, StartDate, EndDate, RoomID) "
				+ "VALUES(null,'"+startDate+"','"+endDate+"','"+room.getRoomID()+"')";
		return executeUpdate(insertReservation);
	}
	
	public boolean insertNewRole(String role){
		String insertRole = "INSERT INTO role(IDRole, Role) VALUES(null,'"+role+"')";
		return executeUpdate(insertRole);
	}
	
	public boolean insertNewRoom(int roomNumber, String name, int numberOfSeats){
		String insertRoom = "INSERT INTO room(IDRoom, RoomNumber, Name, NumberOfSeats) "
				+ "VALUES(null,'"+roomNumber+"','"+name+"','"+numberOfSeats+"')";
		return executeUpdate(insertRoom);
	}
	
	public boolean insertNewUser(String firstName, String lastName, String email, String username, String password, Function function, Role role){
		String insertUser = "INSERT INTO user(IDUser, FirstName, LastName, Email, Username, Password, FunctionID, RoleID) "
				+ "VALUES(null,'"+firstName+"','"+lastName+"','"+email+"','"+username+"','"+password+"',"+function.getId()+","+role.getId()+")";
		return executeUpdate(insertUser);
	}
	
	public boolean insertNewUserReservation(Reservation reservation, User user, boolean host){
		String insertUserReservation = "INSERT INTO userreservation(ReservationID, UserID, Host) "
				+ "VALUES("+reservation.getReservationID()+","+user.getUserID()+","+host+")";
		return executeUpdate(insertUserReservation);
	}
	
	/**
	 * Stores a new function in the database.
	 * 
	 * @param function - The function name
	 * @return true if the function was successfully inserted into the database or false otherwise.
	 * */
	public boolean insertFunction(String function) {
		String insertFunction = "INSERT INTO function(IDFunction, Function) VALUES(null,'"+function+"')";
		return executeUpdate(insertFunction);
	}
	
	// --- SELECT METHODS ---
	public List<Function> selectAllFunctions(){
		ArrayList<Function> functions = new ArrayList<Function>();
		String selectFunction = "SELECT * FROM function;";
		for(Row row : executeSelect(selectFunction)){
			Integer id = row.getRow().get(0).getValue() == Integer.class ? (Integer) row.getRow().get(0).getKey() : null;
			String function = row.getRow().get(1).getValue() == String.class ? (String) row.getRow().get(1).getKey() : null;
			functions.add(new Function(id,function));
		}
		return functions;
	}
	
	public List<Role> selectAllRoles(){
		ArrayList<Role> roles = new ArrayList<Role>();
		String selectRole = "SELECT * FROM role";
		for(Row row : executeSelect(selectRole)){
			Integer id = row.getRow().get(0).getValue() == Integer.class ? (Integer) row.getRow().get(0).getKey() : null;
			String role = row.getRow().get(1).getValue() == String.class ? (String) row.getRow().get(1).getKey() : null;
			roles.add(new Role(id,role));
		}
		return roles;
	}
	
	public List<Room> selectAllRooms(){
		ArrayList<Room> rooms = new ArrayList<Room>();
		String selectRoom = "SELECT idroom FROM room";
		for(Row row : executeSelect(selectRoom)){
			Integer idRoom = row.getRow().get(0).getValue() == Integer.class ? (Integer) row.getRow().get(0).getKey() : null;
			rooms.add(this.selectRoomByID(idRoom));
		}
		return rooms;
	}
	
	public List<User> selectAllUsers(){
		ArrayList<User> users = new ArrayList<User>();
		String selectUser = "SELECT iduser FROM user";
		for(Row row : executeSelect(selectUser)){
			Integer idUser = row.getRow().get(0).getValue() == Integer.class ? (Integer) row.getRow().get(0).getKey() : null;
			users.add(selectUserByID(idUser));
		}
		return users;
	}
	
	public List<Reservation> selectAllReservations(){
		ArrayList<Reservation> reservations = new ArrayList<Reservation>();
		String selectReservation = "SELECT idreservation FROM reservation";
		for(Row row : executeSelect(selectReservation)){
			Integer id = row.getRow().get(0).getValue() == Integer.class ? (Integer) row.getRow().get(0).getKey() : null;
			reservations.add(selectReservationByID(id));
		}
		return reservations;
	}
	
	public Reservation selectReservationByID(Integer id){
		// Select reservations from reservation table
		String selectReservation = "SELECT * FROM reservation WHERE idreservation = "+id;
		ArrayList<Room> rooms = new ArrayList<Room>();
		ArrayList<User> users = new ArrayList<User>();
		for(Row row : executeSelect(selectReservation)){
			Integer idReservation = row.getRow().get(0).getValue() == Integer.class ? (Integer) row.getRow().get(0).getKey() : null;
			Timestamp startDate = row.getRow().get(1).getValue() == Timestamp.class ? (Timestamp) row.getRow().get(1).getKey() : null;
			Timestamp endDate = row.getRow().get(2).getValue() == Timestamp.class ? (Timestamp) row.getRow().get(2).getKey() : null;
			Integer idroom = row.getRow().get(3).getValue() == Integer.class ? (Integer) row.getRow().get(3).getKey() : null;
			
			// Check if a room was already loaded before
			Room roomObject = null;
			for(Room existingRoom : rooms) if(existingRoom.getRoomID() == idroom) roomObject = existingRoom;
			if(roomObject == null) rooms.add(roomObject = selectRoomByID(idroom));
			
			Reservation reservation = new Reservation(idReservation,startDate,endDate,roomObject);
			
			// Select users for reservation from userreservation table
			String selectUsersForReservation = "SELECT userid,host FROM userreservation ur INNER JOIN user u ON ur.userid = u.iduser "
					+ "WHERE ur.reservationid = "+idReservation;
			for(Row urRow : executeSelect(selectUsersForReservation)){
				Integer userId = urRow.getRow().get(0).getValue() == Integer.class ? (Integer) urRow.getRow().get(0).getKey() : null;
				boolean host = urRow.getRow().get(1).getValue() == Boolean.class ? (Boolean) urRow.getRow().get(1).getKey() : null;
				
				User user = null;
				// Check if a specific user was already loaded before
				for(User existingUser : users) if(existingUser.getUserID() == userId) user = existingUser;
				// If not, load user and store him in the list
				if(user == null){
					user = selectUserByID(userId);
					users.add(user);
				}
				
				if(host) {
					reservation.addHost(user);
				}else{
					reservation.addParticipant(user);
				}
			}
			return reservation;
		}
		return null;
	}
	
	public Function selectFunctionByID(Integer id){
		String selectFunction = "SELECT * FROM function WHERE idfunction = "+id;
		
		for(Row row : executeSelect(selectFunction)){
			Integer idFunction = row.getRow().get(0).getValue() == Integer.class ? (Integer) row.getRow().get(0).getKey() : null;
			String function = row.getRow().get(1).getValue() == String.class ? (String) row.getRow().get(1).getKey() : null;
			return new Function(idFunction,function);
		}
		return null;
	}
	
	public Role selectRoleByID(Integer id){
		String selectRole = "SELECT * FROM role WHERE idrole = "+id;
		
		for(Row row : executeSelect(selectRole)){
			Integer idRole = row.getRow().get(0).getValue() == Integer.class ? (Integer) row.getRow().get(0).getKey() : null;
			String role = row.getRow().get(1).getValue() == String.class ? (String) row.getRow().get(1).getKey() : null;
			return new Role(idRole,role);
		}
		return null;
	}
	
	public Room selectRoomByID(Integer id){
		String selectRoom = "SELECT * FROM room WHERE idroom = "+id;
		
		for(Row row : executeSelect(selectRoom)){
			Integer idRoom = row.getRow().get(0).getValue() == Integer.class ? (Integer) row.getRow().get(0).getKey() : null;
			Integer roomNumber = row.getRow().get(1).getValue() == Integer.class ? (Integer) row.getRow().get(1).getKey() : null;
			String name = row.getRow().get(2).getValue() == String.class ? (String) row.getRow().get(2).getKey() : null;
			Integer numberOfSeats = row.getRow().get(3).getValue() == Integer.class ? (Integer) row.getRow().get(3).getKey() : null;
			return new Room(idRoom,roomNumber,name,numberOfSeats);
		}
		return null;
	}
	
	public User selectUserByID(Integer id){
		String selectUser = "SELECT * FROM user WHERE iduser = "+id;
		
		ArrayList<Function> functions = new ArrayList<Function>();
		ArrayList<Role> roles = new ArrayList<Role>();
		for(Row row : executeSelect(selectUser)){
			Integer idUser = row.getRow().get(0).getValue() == Integer.class ? (Integer) row.getRow().get(0).getKey() : null;
			String firstName = row.getRow().get(1).getValue() == String.class ? (String) row.getRow().get(1).getKey() : null;
			String lastName = row.getRow().get(2).getValue() == String.class ? (String) row.getRow().get(2).getKey() : null;
			String email = row.getRow().get(3).getValue() == String.class ? (String) row.getRow().get(3).getKey() : null;
			String username = row.getRow().get(4).getValue() == String.class ? (String) row.getRow().get(4).getKey() : null;
			String password = row.getRow().get(5).getValue() == String.class ? (String) row.getRow().get(5).getKey() : null;
			Integer idFunction = row.getRow().get(6).getValue() == Integer.class ? (Integer) row.getRow().get(6).getKey() : null;
			Integer idRole = row.getRow().get(7).getValue() == Integer.class ? (Integer) row.getRow().get(7).getKey() : null;
			
			Function functionObject = null;
			Role roleObject = null;
			// Check if a function was already loaded before
			for(Function existingFunction : functions) if(existingFunction.getId() == idFunction) functionObject = existingFunction;
			// If not, load function and add it to the list
			if(functionObject == null){
				functionObject = selectFunctionByID(idFunction);
				functions.add(functionObject);
			}
			// Same for role
			for(Role existingRole : roles) if(existingRole.getId() == idRole) roleObject = existingRole;
			if(roleObject == null){
				roleObject = selectRoleByID(idRole);
				roles.add(roleObject);
			}
			
			// User or HostUser
			if(idFunction == null){
				return new User(idUser,firstName,lastName,email,username,password,roleObject);
			}else{
				return new HostUser(idUser,firstName,lastName,email,username,password,roleObject,functionObject);
			}
		}
		return null;
	}
	
	public User selectUserByUsername(String username){
		String selectUser = "SELECT * FROM user WHERE username = '"+username+"'";
		
		ArrayList<Function> functions = new ArrayList<Function>();
		ArrayList<Role> roles = new ArrayList<Role>();
		for(Row row : executeSelect(selectUser)){
			Integer idUser = row.getRow().get(0).getValue() == Integer.class ? (Integer) row.getRow().get(0).getKey() : null;
			String firstName = row.getRow().get(1).getValue() == String.class ? (String) row.getRow().get(1).getKey() : null;
			String lastName = row.getRow().get(2).getValue() == String.class ? (String) row.getRow().get(2).getKey() : null;
			String email = row.getRow().get(3).getValue() == String.class ? (String) row.getRow().get(3).getKey() : null;
			String u_name = row.getRow().get(4).getValue() == String.class ? (String) row.getRow().get(4).getKey() : null;
			String password = row.getRow().get(5).getValue() == String.class ? (String) row.getRow().get(5).getKey() : null;
			Integer idFunction = row.getRow().get(6).getValue() == Integer.class ? (Integer) row.getRow().get(6).getKey() : null;
			Integer idRole = row.getRow().get(7).getValue() == Integer.class ? (Integer) row.getRow().get(7).getKey() : null;
			
			Function functionObject = null;
			Role roleObject = null;
			// Check if a function was already loaded before
			for(Function existingFunction : functions) if(existingFunction.getId() == idFunction) functionObject = existingFunction;
			// If not, load function and add it to the list
			if(functionObject == null){
				functionObject = selectFunctionByID(idFunction);
				functions.add(functionObject);
			}
			// Same for role
			for(Role existingRole : roles) if(existingRole.getId() == idRole) roleObject = existingRole;
			if(roleObject == null){
				roleObject = selectRoleByID(idRole);
				roles.add(roleObject);
			}
			
			// User or HostUser
			if(idFunction == null){
				return new User(idUser,firstName,lastName,email,u_name,password,roleObject);
			}else{
				return new HostUser(idUser,firstName,lastName,email,u_name,password,roleObject,functionObject);
			}
		}
		return null;
	}
	
	// --- UTIL METHODS ---
	// Select and update methods are synchronized because the database isolation level may not be strong enough to lock these operations
	// on the database itself.
	// Can be changed if the database isolation level is high enough or the performance is too low .
	// see -> https://en.wikipedia.org/wiki/Isolation_(database_systems)#Read_uncommitted and JAVA Monitoring and synchronizing.
	
	/**
	 * Performs an INSERT, UPDATE, or DELETE statement on the database.
	 * 
	 * @param query - The query to be executed.
	 * @return true if the operation succeeded or false otherwise.
	 * */
	private synchronized boolean executeUpdate(String query){
		Statement updateStmt = null;
		
		try {
			updateStmt = connection.createStatement();
			updateStmt.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				// Release resources
				if(updateStmt != null) updateStmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * Executes a select statement on the database.
	 * 
	 * @param query - The query to be executed.
	 * @return A list containing the rows returned from the select statement or an empty list.
	 * */
	private synchronized ArrayList<Row> executeSelect(String query){
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
				if(selectStmt != null) selectStmt.close();
				if(result != null) result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
}
