/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.view;

import com.vaadin.ui.Panel;

/**
 *
 * @author eliabosiger
 */
public interface ViewTemplate {
    public void initView();
    public void display(Panel contentPanel);
}
