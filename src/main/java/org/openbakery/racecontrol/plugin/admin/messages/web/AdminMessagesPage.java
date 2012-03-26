package org.openbakery.racecontrol.plugin.admin.messages.web;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.openbakery.racecontrol.plugin.admin.messages.data.AdminMessage;
import org.openbakery.racecontrol.plugin.admin.messages.service.AdminMessagesService;
import org.openbakery.racecontrol.service.ServiceLocateException;
import org.openbakery.racecontrol.service.ServiceLocator;
import org.openbakery.racecontrol.web.RaceControlPage;
import org.openbakery.racecontrol.web.bean.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AdminMessagesPage extends RaceControlPage {

	public AdminMessagesPage(PageParameters parameters) {
		super(parameters);
	}

	private Logger log = LoggerFactory.getLogger(AdminMessagesPage.class);

	public AdminMessagesService getAdminMessageService() {
		ServiceLocator serviceLocator = getSession().getServiceLocator();
		try {
			return (AdminMessagesService) serviceLocator.getService(AdminMessagesService.class);
		} catch (ServiceLocateException e) {
			error("Internal error!");
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public void store(AdminMessage message) {
		try {
			getAdminMessageService().store(message);
		} catch (PersistenceException e) {
			error("Unable to store the message");
			log.error(e.getMessage(), e);
		}

	}

	public void delete(AdminMessage message) {
		try {
			getAdminMessageService().delete(message);
		} catch (PersistenceException e) {
			error("Unable to remove the message");
			log.error(e.getMessage(), e);
		}

	}

	public void sendToAllUsers(AdminMessage message) {

		if (isClientConnected()) {
			getAdminMessageService().showAdminMessage(message);
		} else {
			error("Client is not connected, so cannot send message");
		}

	}

	private boolean isClientConnected() {
		return getSession().getServiceLocator().getRaceService().getClient().isConnected();
	}

	@Override
	public Visibility getVisibility() {
		return Visibility.AUTHENTICATED;
	}

	public void hideAllMessages() {
		if (isClientConnected()) {
			getAdminMessageService().hideAllMessages();
		} else {
			error("Client is not connected, so cannot send message");
		}
	}
}
