/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import ch.bfh.ti.soed.hs16.srs.purple.model.HostFunction;
import ch.bfh.ti.soed.hs16.srs.purple.model.HostUser;

public class HostUserTest
{

	@Test
	public void HostUserTest1() //Damit es nicht gleich heisst wie der Konstruktor
	{
		HostUser hu = new HostUser();
		HostFunction hf = new HostFunction();
		ArrayList<HostFunction> lhf = new ArrayList<HostFunction>();
		hf.setHostFunction("Master");
		hf.setHostFunctionID(1);
		lhf.add(hf);
		hu.setHostFunctionList(lhf);
		assertNotNull(hu);
		assertEquals(hu.getHostFunctionList().get(0).getHostFunction(), "Master");
		assertEquals(hu.getHostFunctionList().get(0).getHostFunctionID(), 1);
	}

}
