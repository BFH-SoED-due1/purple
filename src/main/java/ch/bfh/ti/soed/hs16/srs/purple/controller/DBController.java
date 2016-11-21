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

import ch.bfh.ti.soed.hs16.srs.purple.model.Function;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;
import ch.bfh.ti.soed.hs16.srs.purple.model.User.UserRole;

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
		
		connect();
	}
	
	public static synchronized DBController getInstance() {
		if(instance == null){
			instance = new DBController();
		}
		return instance;
	}
	
	private void connect() {
		// Load mysql driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			// Connect to database
			connection = DriverManager.getConnection("jdbc:mysql://"+dbHost+"/"+db+"",dbUser,dbUserPassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void disconnect() throws SQLException{
		connection.close();
	}
	
	/**
	 * Stores a new function in the database.
	 * 
	 * @param function - The function name
	 * 
	 * @return null if the insert operation failed or the newly created Function.
	 * */
	public Function createFunction(String function) {
		int id = -1;
		
		// Insert new function into database
		String insertFunction = "INSERT INTO function(IDFunction, Function) VALUES(null,'"+function+"')";
		
		Statement insertStmt = null;
		try {
			insertStmt = connection.createStatement();
			insertStmt.executeUpdate(insertFunction);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(insertStmt != null) insertStmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		// Get the new created functionID
		String selectFunction = "SELECT IDFunction FROM function WHERE Function = '"+function+"'";
		
		Statement selectStmt = null;
		ResultSet result = null;
		try {
			selectStmt = connection.createStatement();
			result = selectStmt.executeQuery(selectFunction);
			while(result.next()){
				id = result.getInt("IDFunction");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(selectStmt != null) selectStmt.close();
				if(result != null) result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(id == -1) return null ; return new Function(id,function);
	}

	/**
	 * Creates a new user in the database or updates an existing one.
	 *
	 * @param username - The name the user likes to have.
	 * @param password - The password associated with the given username.
	 * @param emailAddress - The email of the user.
	 * @param role - The specific role this user should have.
	 * @param firstname - Firstname
	 * @param lastname - Lastname
	 *
	 * @return User - The user stored in the database
	 * */
	public User createUser(String lastname, String firstname, String emailAddress, String username, String password, UserRole role){
		// TODO: Create database and connect it via jdbc and return new User only if the user is in the database
		return new User(lastname,firstname,emailAddress,username,password,role);
	}

	public Connection getConnection() {
		return connection;
	}

}
