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

import ch.bfh.ti.soed.hs16.srs.purple.view.RegistrationView;

public class RegistrationViewTest
{

	@Test
	public void RVTest()
	{
		RegistrationView rv = new RegistrationView();
		assertNotNull(rv);
	}

}
