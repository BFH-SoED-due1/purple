/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ch.bfh.ti.soed.hs16.srs.purple.util.Email;

public class EmailTest {

	@Test
	public void TestEmail() {
		Email mail = new Email("", "", "");
		mail.setTo("info@sds-ranking.ch");
		assertEquals(mail.getTo(), "info@sds-ranking.ch");
		mail.setSubject("TestCase");
		assertEquals(mail.getSubject(), "TestCase");
		mail.setMessage("Die Mailklasse muss getestet werden");
		assertEquals(mail.getMessage(), "Die Mailklasse muss getestet werden");
		assertTrue(mail.send());
	}

}
