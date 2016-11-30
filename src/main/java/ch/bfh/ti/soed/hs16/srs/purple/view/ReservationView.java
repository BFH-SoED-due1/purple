/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import ch.bfh.ti.soed.hs16.srs.purple.model.User;
import ch.bfh.ti.soed.hs16.srs.purple.model.User.UserRole;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.DateField;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.RangeSelectEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.RangeSelectHandler;
import com.vaadin.ui.components.calendar.event.BasicEvent;

@SuppressWarnings("serial")
public class ReservationView extends UI {

	private Date start, ende;
	private String titel, beschr;
	private List<User> teilnehmer, hostList;

	public ReservationView()
	{
		ArrayList<User> users = new ArrayList<User>();
		users.add(new User("Gestach", "Lukas", "lukas@gestach.ch", "gestachl", "passwort", UserRole.USER_ROLE_HOST));
	}

	/**
	 *
	 * @param teilnehmer Die Teilnehmerliste
	 * @param host Der eingeloggte User
	 * @param hostList Die Liste der möglichen Hosts
	 */
	public ReservationView(List<User> teilnehmer, List<User> hostList)
	{
		this.teilnehmer = teilnehmer;
		this.hostList = hostList;
		if(teilnehmer == null || hostList == null)
			throw new NullPointerException();
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();

		final Calendar cal = new Calendar();
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		end.add(GregorianCalendar.DAY_OF_YEAR, 40);

		cal.setSizeFull();

		cal.setStartDate(start.getTime());
		cal.setEndDate(end.getTime());
		cal.setVisible(true);

		cal.setHandler(new RangeSelectHandler()
		{

			@Override
			public void rangeSelect(RangeSelectEvent event)
			{
				System.out.println("Range selected");
				final VerticalLayout l = new VerticalLayout();
				Window w = new Window();
				DateField start = new DateField("Startdatum", event.getStart());
				DateField ende = new DateField("Enddatum", event.getEnd());
				TextField titel = new TextField("Titel");
				TextField beschr = new TextField("Beschreibung");
				ListSelect hosts = new ListSelect("Reservierender");
				for(int i = 0;i < hostList.size();i++)
				{
					hosts.addItem(i);
					hosts.setItemCaption(i, hostList.get(i).getUsername());
				}
				ListSelect tnListe = new ListSelect("Teilnehmer");
				for(int i = 0;i < teilnehmer.size();i++)
				{
					hosts.addItem(i);
					hosts.setItemCaption(i, teilnehmer.get(i).getUsername());
				}
				l.addComponents(start, ende, titel, beschr, hosts, tnListe);
				w.setContent(l);
				w.setWidth("300px");
				w.setHeight("400px");
				w.setCaption("Neue Reservierung");
				UI.getCurrent().addWindow(w);
				//cal.addEvent(new BasicEvent("Aebischers Wule", "Aebischer hat immer eine Wule!", event.getStart()));
			}
		});

		cal.setHandler(new EventClickHandler() {
		    @Override
			public void eventClick(EventClick event) {
		        BasicEvent e = (BasicEvent) event.getCalendarEvent();

		        // Do something with it
		        new Notification("Event clicked: " + e.getCaption(),
		            e.getDescription()).show(Page.getCurrent());
		    }
		});

		layout.addComponents(cal);
		layout.setMargin(true);
		layout.setSpacing(true);

		setContent(layout);
	}

	public void setTeilnehmer(List<User> teilnehmer)
	{
		this.teilnehmer = teilnehmer;
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = ReservationView.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
