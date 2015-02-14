package org.openbakery.racecontrol.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.apache.wicket.protocol.http.WebSession;
import org.openbakery.racecontrol.bean.User;
import org.openbakery.racecontrol.plugin.Plugin;
import org.openbakery.racecontrol.service.ServiceLocateException;
import org.openbakery.racecontrol.service.ServiceLocator;
import org.openbakery.racecontrol.service.exception.LoginFailedException;
import org.openbakery.racecontrol.web.bean.MenuItem;
import org.openbakery.racecontrol.web.bean.Visibility;

public class RaceControlSession extends AuthenticatedWebSession {

	/**
	 * 
	 */
	private static final long serialVersionUID = -130649300021164196L;

	private User user;

	private List<MenuItem> menuItems;

	public RaceControlSession(Request request) {
		super(request);
		user = new User();
	}

	@Override
	public Roles getRoles() {
		return null;
	}

	@Override
	public boolean authenticate(String username, String password) {
		try {
			getServiceLocator().getLoginService().login(username, password);
			return true;
		} catch (LoginFailedException e) {
			return false;
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	/*
	 * public Object getBean(String name) {
	 * 
	 * RaceControlWebApplication application = (RaceControlWebApplication) getApplication();
	 * 
	 * return application.getContext().getBean(name); }
	 */
	public ServiceLocator getServiceLocator() {
		RaceControlWebApplication application = (RaceControlWebApplication) getApplication();
		return (ServiceLocator) application.getContext().getBean("serviceLocator");
	}

	public <T extends Object> T getService(Class<T> clazz) {
		ServiceLocator serviceLocator = getServiceLocator();
		try {
			return (T) serviceLocator.getService(clazz);
		} catch (ServiceLocateException e) {
			error("Internal error!");
		}
		return null;
	}

	public List<MenuItem> getMenuItems() {
		// if (menuItems == null) {
		menuItems = new ArrayList<MenuItem>();

		menuItems.add(new MenuItem("Settings", SettingsPage.class, Visibility.AUTHENTICATED, 9));
		menuItems.add(new MenuItem("Logout", LogoutPage.class, Visibility.AUTHENTICATED, 10));

		for (Plugin plugin : getServiceLocator().getRaceService().getRaceControl().getPlugins()) {
			menuItems.addAll(plugin.getMenuItems());
		}

		Collections.sort(menuItems);

		// }
		return menuItems;
	}

}
