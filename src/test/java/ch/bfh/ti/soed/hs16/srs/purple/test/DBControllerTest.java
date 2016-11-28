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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController;

public class DBControllerTest {
	
	@BeforeClass
	public static void testSingleton() {
		assertNotNull(DBController.getInstance());
	}
	
	@Test
	public void testConnection(){
		boolean connected = false;
		try {
			DBController.getInstance().connect();
			connected = true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		assertTrue(connected);
	}
	
	// TODO: Test (Elias)
	
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
