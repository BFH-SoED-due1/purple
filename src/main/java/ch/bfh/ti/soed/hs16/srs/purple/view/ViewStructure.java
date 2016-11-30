/*
 * Copyright (c) 2016 Berner Fachhochschule, Switzerland.
 *
 * Project Smart Reservation System.
 *
 * Distributable under GPL license. See terms of license at gnu.org.
 */

package ch.bfh.ti.soed.hs16.srs.purple.view;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.Reindeer;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
public class ViewStructure extends UI {

    //static
    private static String OVERVIEW_TITLE = "Übersicht";
    private static String RESERVATION_TITLE = "Reservation";
    private static String[] MENU_ITEMS = {OVERVIEW_TITLE, RESERVATION_TITLE};
    private static int MENU_HEIGHT = 35;

    //member variables
    GridLayout fullSite = new GridLayout(2, 3);
    Label logo = new Label("ReservationsTool");
    HorizontalLayout header = new HorizontalLayout();
    HorizontalLayout middle = new HorizontalLayout();
    MenuBar menu = new MenuBar();
    Panel contentPanel = new Panel();

    LoginView loginView = new LoginView();
    RegistrationView registrationView = new RegistrationView();

    /**
     * Function inits the components of the graphical userinterface (GUI).
     *
     * @param vaadinRequest
     */
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        
        this.contentPanel.setStyleName(Reindeer.PANEL_LIGHT);
        this.contentPanel.setSizeFull();
        this.contentPanel.addStyleName("contenPanel");
        this.logo.addStyleName("logo");

        initMenu();

        //TODO: Is user logged in?
        HorizontalLayout login = new LoginView().initLoginView();

        this.registrationView.initView();
        this.registrationView.display(this.contentPanel);

        this.fullSite.addComponent(this.logo, 0, 0);
        this.fullSite.setComponentAlignment(this.logo, Alignment.MIDDLE_LEFT);
        this.fullSite.addComponent(login, 1, 0);
        this.fullSite.setComponentAlignment(login, Alignment.MIDDLE_RIGHT);
        this.fullSite.setRowExpandRatio(0, 0);

        this.fullSite.addComponent(this.menu, 0, 1, 1, 1);
        this.fullSite.setComponentAlignment(this.menu, Alignment.TOP_LEFT);

        this.fullSite.addComponent(this.contentPanel, 0, 2, 1, 2);
        this.fullSite.setComponentAlignment(this.contentPanel, Alignment.TOP_LEFT);
        this.fullSite.setRowExpandRatio(2, 10);

        this.fullSite.setSizeFull();
        this.setContent(this.fullSite);
    }

    /**
     * Function initialize the menubar (navifation).
     */
    private void initMenu() {
        // Define a common menu command for all the menu items.
        MenuBar.Command menuSelected = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                System.out.println("Menu selected: " + selectedItem.getText()); //TODO: Whats to do when menu is clicked
            }
        };

        this.menu.setWidth(100, Unit.PERCENTAGE);
        this.menu.setHeight(MENU_HEIGHT, Unit.PIXELS);

        for (String menu : MENU_ITEMS) {
            this.menu.addItem(menu, null, menuSelected);
        }
    }

    /**
     * Servlet
     */
    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ViewStructure.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
