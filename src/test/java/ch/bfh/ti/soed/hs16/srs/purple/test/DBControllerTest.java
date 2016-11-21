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
import ch.bfh.ti.soed.hs16.srs.purple.model.Function;

public class DBControllerTest {
	
	@BeforeClass
	public static void testSingleton(){
		DBController.getInstance();
		
		assertNotNull(DBController.getInstance());
		assertNotNull(DBController.getInstance().getConnection());
	}
	
	@Test
	public void testCreateFunction(){
		String testFunction = "TestFunction";
		Function function = DBController.getInstance().createFunction(testFunction);
		assertEquals(function.getFunction(),testFunction);
	}
	
	@AfterClass
	public static void disconnect(){
		try {
			DBController.getInstance().disconnect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
