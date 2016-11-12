/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ch.bfh.ti.soed.hs16.srs.purple.MyUI;
import ch.bfh.ti.soed.hs16.srs.purple.MyUI.MyUIServlet;

public class MyUIViewTest
{

	@Test
	public void MyUIViewTest1() //Damit es nicht gleich Konstruktor ist
	{
		MyUI m = new MyUI();
		MyUIServlet s = new MyUIServlet();
		assertNotNull(m);
		assertNotNull(s);
	}

}