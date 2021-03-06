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
import java.util.Set;

import org.vaadin.dialogs.ConfirmDialog;

import ch.bfh.ti.soed.hs16.srs.purple.controller.ReservationController;
import ch.bfh.ti.soed.hs16.srs.purple.model.Reservation;
import ch.bfh.ti.soed.hs16.srs.purple.model.Room;
import ch.bfh.ti.soed.hs16.srs.purple.model.User;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.DateClickEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.DateClickHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.RangeSelectEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.RangeSelectHandler;

public class ReservationView implements ViewTemplate {

	private final ReservationController resCont = new ReservationController();

	// membervariables
	private final List<User> participant, hostList;
	private final List<Room> roomList;
	private List<Reservation> resList;
	private ClickListener clButton;
	private Reservation res;
	private User actualUser;
	private Room actualRoom;
	private int actualView = -1;

	// UI Components
	private final Calendar cal = new Calendar();
	private final Label siteTitle = new Label("Reservation");
	private DateField startDate;
	private DateField endDate;
	private TextField title;
	private TextArea description;
	private ListSelect hosts;
	private ListSelect participantList;
	private NativeSelect viewSelect;
	private NativeSelect rooms;
	private Button saveButton, deleteButton, acceptButton, rejectButton;
	private Window popUpWindow;
	private final GridLayout layout = new GridLayout(2, 2);

	//Container for calendar
	private final BeanItemContainer<Reservation> calEvents = new BeanItemContainer<>(Reservation.class);

	private static String USER_SESSION_ATTRIBUTE = "user";

	/**
	 * Constructor: ReservationView
	 */
	public ReservationView() {
		hostList = resCont.getAllUsers();
		participant = resCont.getAllUsers();
		roomList = resCont.getAllRooms();
		resList = resCont.getAllReservations();
	}

	/**
	 * Function initalizes the reservation view
	 */
	@SuppressWarnings("serial")
	@Override
	public void initView() {

		siteTitle.addStyleName("h2");
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
				Timestamp start = new Timestamp(event.getStart().getTime());
				Timestamp ende = new Timestamp(event.getEnd().getTime());
				if(start.compareTo(ende) == 0)
					ende.setTime(ende.getTime() + 3600 * 1000); //if start and end are the same, add 1 hour to the end
				showPopup(new Reservation(-1, start, ende, null, "", ""));
			}
		});

		/**
		 * DateClickHandler
		 * switches between daily and monthly view
		 */
		cal.setHandler(new DateClickHandler() {

			@Override
			public void dateClick(DateClickEvent event) {
				if(cal.getEndDate().getTime() - cal.getStartDate().getTime() == 0)
				{
					cal.setStartDate(start.getTime());
					cal.setEndDate(end.getTime());
				}
				else
				{
					cal.setStartDate(event.getDate());
					cal.setEndDate(event.getDate());
				}
			}
		});

		clButton = new ClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(ClickEvent event) {
				if(event.getButton() == saveButton) //God save the queen
				{
					if(title.isReadOnly())
					{
						setEditable(true);
						return;
					}
					//Validating checks
					if(title.getValue().length() < 3)
						title.setStyleName("errorTextField");
					if(rooms.getValue() == null)
						return;
					Timestamp startTime = new Timestamp(startDate.getValue().getTime());
					Timestamp endTime = new Timestamp(endDate.getValue().getTime());
					List<User> hosts = new ArrayList<User>();
					//Hosts auslesen
					Set<Integer> temp = (Set<Integer>) ReservationView.this.hosts.getValue();
					for(int id : temp)
						for(int y = 0;y < hostList.size();y++)
							if(hostList.get(y).getUserID() == id)
								hosts.add(hostList.get(y));
					if(hosts.size() == 0) //Wenn kein User als Host ist, wird der aktuelle User als Host genommen
						hosts.add(actualUser);
					List<User> participants = new ArrayList<User>();
					//Teilnehmer auslesen
					Set<Integer> temp2 = (Set<Integer>) ReservationView.this.participantList.getValue();
					for(int id : temp2)
						for(int y = 0;y < hostList.size();y++)
							if(hostList.get(y).getUserID() == id && !isHost(hostList.get(y), hosts)) //Teilnehmer nur speichern, wenn er nicht bereits Host ist
								participants.add(hostList.get(y));

					System.out.println(hosts.get(0).getUsername());
					if(res.getReservationID() > 0) //Edit
					{
						Reservation newRes = new Reservation(res.getReservationID(), startTime, endTime, resCont.getRoom((int) rooms.getValue()), title.getValue(), description.getValue(), hosts, participants);
						resCont.updateReservation(newRes);
						popUpWindow.close();
						calendarUpdate();
					}
					else //Neu
					{
						if(resCont.addReservation(new Reservation(-1, startTime, endTime, resCont.getRoom((int) rooms.getValue()), title.getValue(), description.getValue(), hosts, participants))){
							popUpWindow.close();
							calendarUpdate();
						}
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
				if(event.getButton() == acceptButton) //Accept reservation as participant
				{
					if(resCont.acceptReservation(actualUser, res))
						popUpWindow.close();
				}
				if(event.getButton() == rejectButton) //reject reservation as participant
				{
					if(resCont.cancelReservation(actualUser, res))
						popUpWindow.close();
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

		ValueChangeListener vcl = new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(viewSelect.getValue() == null)
					return;
				int val = (int) viewSelect.getValue();
				if(val != 0)
					actualView = val;
				calendarUpdate();
			}
		};

		viewSelect = new NativeSelect();
		viewSelect.addItem(-1);
		viewSelect.setItemCaption(-1, "Übersicht");
		viewSelect.addItem(-2);
		viewSelect.setItemCaption(-2, "Eigene Reservationen");
		viewSelect.select(-1);
		viewSelect.addValueChangeListener(vcl);
		viewSelect.addItem(0);
		viewSelect.setItemCaption(0, "--- Räume ---");
		for(int i = 0;i < roomList.size();i++)
		{
			viewSelect.addItem(roomList.get(i).getRoomID());
			viewSelect.setItemCaption(roomList.get(i).getRoomID(), roomList.get(i).getName() + "(" + roomList.get(i).getNumberOfSeats() + " Plätze)");
		}

		layout.addComponent(siteTitle, 0, 0);
		layout.addComponent(cal, 0, 1, 1, 1);
		layout.addComponent(viewSelect, 1, 0);
		layout.setComponentAlignment(viewSelect, Alignment.MIDDLE_RIGHT);
		layout.setRowExpandRatio(0, 0.1f);
		layout.setRowExpandRatio(1, 20);
		//layout.setMargin(true);
		//layout.setSpacing(true);
		layout.setSizeFull();
	}

	/**
	 * Shows the popup window where a reservation can be modified, deleted or inserted
	 * @param res The Reservation Object (for a new reservation, fill the startDate with the current Timestamp!)
	 */
	@SuppressWarnings("serial")
	private void showPopup(Reservation res)
	{
		this.res = res; //Update the member
		boolean newRes = res.getReservationID() > 0 ? false : true;
		boolean isHost = isHost(actualUser, res.getHostList());
		boolean isParticipant = isHost(actualUser, res.getParticipantList());
		final GridLayout gridLayout = new GridLayout(3, 5);
		ValueChangeListener vcl = new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				List<Room> roomList = resCont.getAllFreeRooms(new Timestamp(startDate.getValue().getTime()), new Timestamp(endDate.getValue().getTime()));
				if(ReservationView.this.res.getReservationID() > 0)
				{
					System.out.println("Aktuellen Raum");
					rooms.addItem(ReservationView.this.res.getRoom().getRoomID());
					rooms.setItemCaption(ReservationView.this.res.getRoom().getRoomID(), ReservationView.this.res.getRoom().getName() + " (" + ReservationView.this.res.getRoom().getNumberOfSeats() + " Plätze)");
					rooms.select(ReservationView.this.res.getRoom().getRoomID());
				}
				for(int i = 0;i < roomList.size();i++)
				{
					rooms.addItem(roomList.get(i).getRoomID());
					String caption = roomList.get(i).getName() + " (" + roomList.get(i).getNumberOfSeats() + " Plätze)";
					rooms.setItemCaption(roomList.get(i).getRoomID(), caption);
				}
				if(ReservationView.this.res.getReservationID() <= 0 && actualRoom != null)
					rooms.select(actualRoom.getRoomID());
			}
		};
		popUpWindow = new Window();
		popUpWindow.center();
		popUpWindow.setModal(true);
		startDate = new DateField("Startzeit");
		startDate.setLocale(VaadinSession.getCurrent().getLocale());
		startDate.setValue(res.getStart());
		startDate.addValueChangeListener(vcl);
		startDate.setDateFormat("dd.MM.yyyy HH:mm");
		startDate.setResolution(Resolution.HOUR);
		endDate = new DateField("Endzeit");
		endDate.setValue(res.getEnd());
		endDate.addValueChangeListener(vcl);
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
		for (int i = 0; i < hostList.size(); i++) {
			hosts.addItem(hostList.get(i).getUserID());
			hosts.setItemCaption(hostList.get(i).getUserID(), hostList.get(i).getUsername());
		}
		//select the hosts in list
		if(!newRes)
		{
			List<User> resHosts = res.getHostList();
			for(int i = 0;i < resHosts.size();i++)
				for(int y = 0;y < hostList.size();y++)
					if(hostList.get(y).getUserID() == resHosts.get(i).getUserID())
						hosts.select(resHosts.get(i).getUserID());
		}
		else
			hosts.select(actualUser.getUserID());
		hosts.select(0);
		hosts.setRows(hostList.size() > 5 ? 5 : hostList.size());
		participantList = new ListSelect("Teilnehmer");
		participantList.setMultiSelect(true);
		participantList.clear();

		for (int i = 0; i < participant.size(); i++) {
			participantList.addItem(participant.get(i).getUserID());
			participantList.setItemCaption(participant.get(i).getUserID(), participant.get(i).getUsername());
		}
		//select the participants in list
		if(!newRes)
		{
			List<User> resPart = res.getParticipantList();
			for(int i = 0;i < resPart.size();i++)
				for(int y = 0;y < participant.size();y++)
					if(participant.get(y).getUserID() == resPart.get(i).getUserID())
						participantList.select(resPart.get(i).getUserID());
		}
		participantList.setRows(participant.size() > 5 ? 5 : participant.size());
		rooms = new NativeSelect("Raum");
		rooms.setNullSelectionAllowed(false);
		rooms.removeAllItems();
		List<Room> roomList = resCont.getAllFreeRooms(new Timestamp(startDate.getValue().getTime()), new Timestamp(endDate.getValue().getTime()));
		if(!newRes)
		{
			rooms.addItem(res.getRoom().getRoomID());
			rooms.setItemCaption(res.getRoom().getRoomID(), res.getRoom().getName() + " (" + res.getRoom().getNumberOfSeats() + " Plätze)");
			rooms.select(res.getRoom().getRoomID());
		}
		for(int i = 0;i < roomList.size();i++)
		{
			rooms.addItem(roomList.get(i).getRoomID());
			String caption = roomList.get(i).getName() + " (" + roomList.get(i).getNumberOfSeats() + " Plätze)";
			rooms.setItemCaption(roomList.get(i).getRoomID(), caption);
		}
		if(newRes && actualRoom != null)
			rooms.select(actualRoom.getRoomID());
		saveButton = new Button("Speichern");
		saveButton.addClickListener(clButton);
		deleteButton = new Button("Löschen");
		deleteButton.addClickListener(clButton);
		deleteButton.setVisible(res.getReservationID() > 0 ? true : false);
		if(!newRes)
			setEditable(false);
		gridLayout.addComponent(startDate, 0, 0);
		gridLayout.addComponent(endDate, 0, 1);
		gridLayout.addComponent(hosts, 1, 0, 1, 1);
		gridLayout.addComponent(participantList, 2, 0, 2, 1);
		gridLayout.addComponent(title, 0, 2);
		gridLayout.addComponent(rooms, 1, 2, 2, 2);
		gridLayout.addComponent(description, 0, 3, 1, 3);
		if(roomList.size() == 0)
			saveButton.setEnabled(false);
		if(isHost || newRes) //show buttons for edit and delete only if the user is host or its a new reservation
		{
			gridLayout.addComponent(saveButton, 0, 4);
			gridLayout.addComponent(deleteButton, 1, 4);
		}
		if(isParticipant) //show buttons for accept oder decline a reservation
		{
			acceptButton = new Button("Zusagen");
			rejectButton = new Button("Absagen");
			acceptButton.addClickListener(clButton);
			rejectButton.addClickListener(clButton);
			if(isHost(actualUser, res.getAcceptedParticipantsList()))
			{
				acceptButton.setEnabled(false);
				rejectButton.setEnabled(true);
			}
			else
			{
				rejectButton.setEnabled(false);
				acceptButton.setEnabled(true);
			}
			gridLayout.addComponent(acceptButton, 0, 4);
			gridLayout.addComponent(rejectButton, 1, 4);
		}
		gridLayout.setSpacing(true);
		gridLayout.setMargin(new MarginInfo(false, false, false, true));
		gridLayout.setWidth(100, Unit.PERCENTAGE);
		popUpWindow.setContent(gridLayout);
		popUpWindow.setWidth("600px");
		popUpWindow.setHeight("450px");
		popUpWindow.setCaption(res.getReservationID() > 0 ? "Reservierungsdetails" : "Neue Reservierung");
		UI.getCurrent().addWindow(popUpWindow);
	}

	/**
	 * Checks if the user is in the hostList
	 * @param user the user to be checked
	 * @param hostList the hostList of the reservation
	 * @return true if the user is in the hostList, false otherwise
	 */
	private boolean isHost(User user, List<User> hostList)
	{
		for(int i = 0;i < hostList.size();i++)
			if(hostList.get(i).getUserID() == user.getUserID())
				return true;
		return false;
	}

	/**
	 * enable or disable input components.
	 * @param editable true if the inputs should be editable, false otherwise
	 */
	private void setEditable(boolean editable)
	{
		saveButton.setCaption(editable ? "Speichern" : "Bearbeiten");
		popUpWindow.setCaption(editable ? "Reservierung bearbeiten" : "Reservierungsdetails");
		editable = !editable;
		title.setReadOnly(editable);
		rooms.setReadOnly(editable);
		description.setReadOnly(editable);
		startDate.setReadOnly(editable);
		endDate.setReadOnly(editable);
		participantList.setReadOnly(editable);
		hosts.setReadOnly(editable);
	}

	/**
	 * Function shows the view on the content panel
	 * @param content - the content to be displayed.
	 */
	@Override
	public void display(Component content) {
		actualUser = resCont.getSessionUser((String) VaadinSession.getCurrent().getAttribute(USER_SESSION_ATTRIBUTE));
		calendarUpdate();
		Panel contentPanel = (Panel) content;
		contentPanel.setContent(layout);
	}

	/**
	 * Updates the calendar
	 */
	private void calendarUpdate(){
		calEvents.removeAllItems();
		if(actualView == -1) //Alle Reservationen
		{
			resList = resCont.getAllReservations();
		}
		if(actualView == -2) //Eigene Reservationen
		{
			resList = resCont.getAllReservationsFromUser(actualUser);
		}
		if(actualView > 0) //Raum
		{
			for(int i = 0;i < roomList.size();i++)
				if(roomList.get(i).getRoomID() == actualView)
					actualRoom = roomList.get(i);
			resList = resCont.getAllReservationsFromRoom(actualRoom.getRoomID());
		}
		for(int i = 0; i < resList.size(); i++)
		{
			if(isHost(actualUser, resList.get(i).getHostList())) //Reservation as host
				resList.get(i).setStyleName("violett");
			if(isHost(actualUser, resList.get(i).getAcceptedParticipantsList())) //accepted reservation
				resList.get(i).setStyleName("gruen");
			if(!isHost(actualUser, resList.get(i).getAcceptedParticipantsList()) && isHost(actualUser, resList.get(i).getParticipantList())) //rejected reservation
				resList.get(i).setStyleName("rot");
		}
		calEvents.addAll(resList);
		calEvents.sort(new Object[]{"start"}, new boolean[]{true});

		cal.setContainerDataSource(calEvents);
	}
}
