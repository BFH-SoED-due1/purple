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

import ch.bfh.ti.soed.hs16.srs.purple.model.Role;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;

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
		users.add(new User(3, "Gestach", "Lukas", "lukas@gestach.ch", "gestachl", "passwort", new Role(1, "Admin")));
		users.add(new User(4, "Aebischer", "Patrik", "ges@gestach.ch", "aebip1", "passwort", new Role(1, "Wollschaf")));
		ArrayList<User> teilnehmer = new ArrayList<User>();
		teilnehmer.add(new User(3, "Gestach", "Lukas", "lukas@gestach.ch", "teilnehmer", "passwort", new Role(1, "Admin")));
		teilnehmer.add(new User(4, "Aebischer", "Patrik", "ges@gestach.ch", "boesie", "passwort", new Role(1, "Wollschaf")));
		this.hostList = users;
		this.teilnehmer = teilnehmer;
	}

	/**
	 *
	 * @param teilnehmer Die Teilnehmerliste
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
				l.setMargin(true);
				Window w = new Window();
				//w.setPosition((int) UI.getCurrent().getWidth() / 2 - 150, (int) UI.getCurrent().getHeight() / 2 - 200);
				//System.out.println(layout.getWidth());
				DateField start = new DateField("Startdatum", event.getStart());
				start.setLocale(getLocale());
				DateField ende = new DateField("Enddatum", event.getEnd());
				ende.setLocale(getLocale());
				TextField titel = new TextField("Titel");
				TextField beschr = new TextField("Beschreibung");
				ListSelect hosts = new ListSelect("Reservierender");
				hosts.setMultiSelect(true);
				hosts.clear();
				for(int i = 0;i < hostList.size();i++)
				{
					hosts.addItem(i);
					hosts.setItemCaption(i, hostList.get(i).getUsername());
				}
				hosts.select(0);
				hosts.setRows(hostList.size() > 5 ? 5 : hostList.size());
				ListSelect tnListe = new ListSelect("Teilnehmer");
				tnListe.setMultiSelect(true);
				tnListe.clear();
				for(int i = 0;i < teilnehmer.size();i++)
				{
					tnListe.addItem(i);
					tnListe.setItemCaption(i, teilnehmer.get(i).getUsername());
				}
				tnListe.setRows(teilnehmer.size() > 5 ? 5 : teilnehmer.size());
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
