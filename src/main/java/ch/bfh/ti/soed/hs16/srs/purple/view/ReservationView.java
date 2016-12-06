/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */
package ch.bfh.ti.soed.hs16.srs.purple.view;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.RangeSelectEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.RangeSelectHandler;
import com.vaadin.ui.components.calendar.event.CalendarEvent;

import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController;
import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_Room;
import ch.bfh.ti.soed.hs16.srs.purple.controller.DBController.Table_User;
import ch.bfh.ti.soed.hs16.srs.purple.controller.ReservationController;
import ch.bfh.ti.soed.hs16.srs.purple.model.Reservation;
import ch.bfh.ti.soed.hs16.srs.purple.model.Role;
import ch.bfh.ti.soed.hs16.srs.purple.model.Room;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;
import ch.bfh.ti.soed.hs16.srs.purple.util.ReservationAction;

public class ReservationView implements ViewTemplate {

	private ReservationController resCont = new ReservationController();
	
	// membervariables
	private List<User> participant, hostList;
	private ClickListener clButton;
	private Reservation res;
	private ReservationAction reservationAction = ReservationAction.NONE;

	// UI Components
	private final Calendar cal = new Calendar();
	private Label siteTitle = new Label("Reservation");
	private DateField startDate;
	private DateField endDate;
	private TextField title;
	private TextArea description;
	private ListSelect hosts;
	private ListSelect participantList;
	private Button saveButton, deleteButton;
	private Window popUpWindow;
	private VerticalLayout layout = new VerticalLayout();
	
	private static String USER_SESSION_ATTRIBUTE = "user";
	
	/**
	 * Constructor: ReservationView
	 */
	@SuppressWarnings("serial")
	public ReservationView() {
		// TODO: Vom Kontroller nehmen
		ArrayList<User> users = new ArrayList<User>();
		users.add(new User(3, "Gestach", "Lukas", "lukas@gestach.ch", "gestachl", "passwort", new Role(1, "Admin")));
		users.add(new User(4, "Aebischer", "Patrik", "ges@gestach.ch", "aebip1", "passwort", new Role(1, "Wollschaf")));
		ArrayList<User> participant = new ArrayList<User>();
		participant.add(
				new User(3, "Gestach", "Lukas", "lukas@gestach.ch", "teilnehmer", "passwort", new Role(1, "Admin")));
		participant.add(
				new User(4, "Aebischer", "Patrik", "ges@gestach.ch", "boesie", "passwort", new Role(1, "Wollschaf")));
		this.hostList = users;
		this.participant = participant;
	}

	/**
	 * Constructor: ReservationViews
	 *
	 * @param participant - participant list
	 * @param hostList - List of possible hosts
	 */
	public ReservationView(List<User> participant, List<User> hostList) {
		this.participant = participant;
		this.hostList = hostList;
		if (participant == null || hostList == null)
			throw new NullPointerException();
	}

	/**
	 * Function initalizes the reservation view
	 */
	@SuppressWarnings("serial")
	@Override
	public void initView() {

		this.siteTitle.addStyleName("h2");
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		end.add(GregorianCalendar.DAY_OF_YEAR, 40);

		cal.setSizeFull();

		cal.setStartDate(start.getTime());
		cal.setEndDate(end.getTime());
		cal.setVisible(true);

		// Handler opens a new reservation pop-up
		cal.setHandler(new RangeSelectHandler() {

			@Override
			public void rangeSelect(RangeSelectEvent event) {
				showPopup(new Reservation(-1, new Timestamp(event.getStart().getTime()), new Timestamp(event.getEnd().getTime()), new Room(0, 400, "TestRoom", 160), "", ""));
			}
		});

		clButton = new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if(event.getButton() == saveButton) //God save the queen
				{
					if(title.isReadOnly())
					{
						setEditable(true);
						return;
					}
					Timestamp startTime = new Timestamp(startDate.getValue().getTime());
					Timestamp endTime = new Timestamp(endDate.getValue().getTime());
					Room room = DBController.getInstance().selectRoomBy(Table_Room.COLUMN_ROOMNUMBER, 10).get(0); //TODO vom Kontroller nehmen
					List<User> hosts = new ArrayList<User>();
					hosts.add(DBController.getInstance().selectUserBy(Table_User.COLUMN_USERNAME, "aep").get(0)); //TODO vom Kontroller nehmen
					if(resCont.addReservation(new Reservation(-1, startTime, endTime, room, title.getValue(), description.getValue(), hosts))){
						popUpWindow.close();
						calendarUpdate();
					}
				}
				if(event.getButton() == deleteButton) //Move the reservation into the trash
				{
					//Confirm
					ConfirmDialog.show(UI.getCurrent(), "Löschen bestätigen", "Die Reservation wirklich löschen?", "Ja", "Abbrechen", new ConfirmDialog.Listener() {
						
						@Override
						public void onClose(ConfirmDialog arg0) {
							if(arg0.isConfirmed())
							{
								resCont.deleteReservation(res.getReservationID());
								cal.removeEvent(res);
								res = null;
								popUpWindow.close();
							}
						}
					});
				}
			}
		};
		
		cal.setHandler(new EventClickHandler() {
			@Override
			public void eventClick(EventClick event) {
				//show the reservation details if clicked on it
				Reservation e = (Reservation) event.getCalendarEvent();

				showPopup(e);
			}
		});

		calendarUpdate();
		this.layout.addComponent(siteTitle);
		this.layout.addComponents(cal);
		this.layout.setMargin(true);
		this.layout.setSpacing(true);
	}
	
	/**
	 * Shows the popup window where a reservation can be modified, deleted or inserted
	 * @param res The Reservation Object (for a new reservation, fill the startDate with the current Timestamp!)
	 */
	private void showPopup(Reservation res)
	{
		this.res = res; //Update the member
		boolean newRes = res.getReservationID() > 0 ? false : true;
		final GridLayout gridLayout = new GridLayout(3, 5);
		popUpWindow = new Window();
		popUpWindow.center();
		popUpWindow.setModal(true);
		startDate = new DateField("Startdatum");
		startDate.setLocale(VaadinSession.getCurrent().getLocale());
		startDate.setDateFormat("dd.MM.yyyy HH:mm");
		startDate.setValue(res.getStart());
		startDate.setResolution(Resolution.HOUR);
		endDate = new DateField("Enddatum");
		endDate.setValue(res.getEnd());
		endDate.setDateFormat("dd.MM.yyyy HH:mm");
		endDate.setLocale(VaadinSession.getCurrent().getLocale());
		endDate.setResolution(Resolution.HOUR);
		title = new TextField("Titel");
		title.setValue(res.getTitle());
		title.setWidth(100, Unit.PERCENTAGE);
		description = new TextArea("Beschreibung");
		description.setValue(res.getDescription());
		description.setRows(3);
		description.setWidth(100, Unit.PERCENTAGE);
		hosts = new ListSelect("Reservierender");
		hosts.setMultiSelect(true);
		hosts.clear();
		List<User> resHosts = null;
		if(!newRes)
			resHosts = res.getHostList();
		for (int i = 0; i < hostList.size(); i++) {
			hosts.addItem(i);
			hosts.setItemCaption(i, hostList.get(i).getUsername());
			if(!newRes && resHosts != null)
				if(contain(hostList.get(i), resHosts))
					hosts.select(i);
		}
		hosts.select(0);
		hosts.setRows(hostList.size() > 5 ? 5 : hostList.size());
		participantList = new ListSelect("Teilnehmer");
		participantList.setMultiSelect(true);
		participantList.clear();
		List<User> resPart = null;
		if(!newRes)
			resPart = res.getParticipantList();
		for (int i = 0; i < participant.size(); i++) {
			participantList.addItem(i);
			participantList.setItemCaption(i, participant.get(i).getUsername());
			if(!newRes && resPart != null)
				if(contain(hostList.get(i), resPart))
					hosts.select(i);
		}
		participantList.setRows(participant.size() > 5 ? 5 : participant.size());
		saveButton = new Button("Speichern");
		saveButton.addClickListener(clButton);
		deleteButton = new Button("Löschen");
		deleteButton.addClickListener(clButton);
		deleteButton.setVisible(res.getReservationID() > 0 ? true : false);
		if(!newRes)
			setEditable(false);
		gridLayout.addComponent(startDate, 0, 0);
		gridLayout.addComponent(endDate, 0, 1);
		gridLayout.addComponent(hosts, 1, 0, 1, 2);
		gridLayout.addComponent(participantList, 2, 0, 2, 2);
		gridLayout.addComponent(title, 0, 2);
		gridLayout.addComponent(description, 0, 3, 1, 3);
		gridLayout.addComponent(saveButton, 0, 4);
		gridLayout.addComponent(deleteButton, 1, 4);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(new MarginInfo(false, false, false, true));
		gridLayout.setWidth(100, Unit.PERCENTAGE);
		popUpWindow.setContent(gridLayout);
		popUpWindow.setWidth("600px");
		popUpWindow.setHeight("450px");
		popUpWindow.setCaption(res.getReservationID() > 0 ? "Reservierungsdetails" : "Neue Reservierung");
		UI.getCurrent().addWindow(popUpWindow);
		// cal.addEvent(new BasicEvent("Aebischers Wule", "Aebischer hat
		// immer eine Wule!", event.getStart()));
	}
	
	/**
	 * compare the userList in the reservation with a single user
	 * @param inList user for comparing
	 * @param inRes userList of users in the reservation
	 * @return true if the user is in the userList, false otherwise
	 */
	private boolean contain(User inList, List<User> inRes)
	{
		for(int x = 0;x < inRes.size();x++)
			if(inRes.contains(inList))
				return true;
		return false;
	}
	
	private void setEditable(boolean editable)
	{
		saveButton.setCaption(editable ? "Speichern" : "Bearbeiten");
		popUpWindow.setCaption(editable ? "Reservierung bearbeiten" : "Reservierungsdetails");
		editable = !editable;
		title.setReadOnly(editable);
		description.setReadOnly(editable);
		startDate.setReadOnly(editable);
		endDate.setReadOnly(editable);
		participantList.setReadOnly(editable);
		hosts.setReadOnly(editable);
	}
	
	/**
	 * Function shows the view on the content panel
	 */
	@Override
	public void display(Component content) {
		Panel contentPanel = (Panel) content;
		contentPanel.setContent(this.layout);
	}

	/**
	 * Rückgabeparameter vom Kontroller an die View, damit die geeignete Ausgabe
	 * für den Benutzer ausgegeben werden kann
	 *
	 * @param status - Der Status der SQL Abfrage
	 */
	public void setStatus(boolean status) {
		// Statusmeldung vom Controller
		// Fenster schliessen oä. bei Success, Fehlermeldung anzeigen sonst
		// TODO
	}

	/**
	 * Gibt die Aktion zurück, für welche ein ButtonClickEvent ausgelöst wurde
	 *
	 * @return Die Aktion
	 */
	public ReservationAction getAction() {
		return reservationAction;
	}

	/**
	 * Die Daten zum ButtonClickEvent werden als Reservation Objekt ausgegeben
	 *
	 * @return Die Daten zur Reservation
	 */
	public Reservation getReservation() {
		// TODO
		// res = new Reservation(0, startDate, endDate, room, title,
		// description)
		return res;
	}

	/**
	 * Updates the calendar
	 */
	public void calendarUpdate(){
		List<CalendarEvent> tmp = cal.getEvents(cal.getStartDate(), cal.getEndDate());
		for(int i = 0;i < tmp.size();i++)
			cal.removeEvent(tmp.get(i));
		List<Reservation> res = resCont.getAllReservations();
		for(int i = 0; i < res.size(); i++){
			//cal.addEvent(new BasicEvent(res.get(i).getTitle(), res.get(i).getDescription(), res.get(i).getStartDate(), res.get(i).getEndDate()));
			cal.addEvent(res.get(i));
		}
	}
	
	/**
	 * Die Teilehmerliste setzen (falls nicht schon im Konstruktor geschehen)
	 *
	 * @param participant - A list of participants
	 */
	public void setParticipant(List<User> participant) {
		this.participant = participant;
	}
}
