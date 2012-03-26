package org.openbakery.racecontrol.plugin.admin.messages.service;

import java.util.List;

import org.openbakery.racecontrol.gui.ButtonMessageHelper;
import org.openbakery.racecontrol.gui.MessageSize;
import org.openbakery.racecontrol.persistence.Persistence;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.openbakery.racecontrol.plugin.admin.messages.data.AdminMessage;

public class AdminMessagesService {

	private Persistence persistence;

	private ButtonMessageHelper buttonMessageHelper;

	public AdminMessagesService() {
		buttonMessageHelper = ButtonMessageHelper.getInstance();
	}

	public void setPersistence(Persistence persistence) {
		this.persistence = persistence;
	}

	public List<AdminMessage> getAllMessages() throws PersistenceException {
		return (List<AdminMessage>) persistence.query("Select adminMessage from AdminMessage as adminMessage");
	}

	public void store(AdminMessage message) throws PersistenceException {
		persistence.store(message);
	}

	public void delete(AdminMessage message) throws PersistenceException {
		persistence.delete(message);
	}

	public void sendButtonMessage(String text, int sleep, MessageSize size) {
		buttonMessageHelper.sendButtonMessage(text, sleep, size);
	}

	public void sendStaticButtonMessage(String substring) {
		buttonMessageHelper.sendButtonMessage(substring);
	}

	public void showAdminMessage(AdminMessage message) {
		if (message.getDuration() > 0) {
			buttonMessageHelper.sendButtonMessage(message.getMessage(), message.getDuration(), message.getSize());
		} else {
			buttonMessageHelper.sendStaticButtonMessage(message.getMessage());
		}
	}

	public void hideAllMessages() {
		buttonMessageHelper.hideAll();
	}

}
