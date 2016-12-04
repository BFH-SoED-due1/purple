/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.view;

import com.vaadin.ui.Component;

/**
 *
 * @author eliabosiger
 */
public interface ViewTemplate {
	
	/**
	 * Function inits the view
	 */
    public void initView();
    
    /**
     * Function displays the initialized view on the content
     * 
     * @param content component on wich the view will be displayed
     */
    public void display(Component content);
}
