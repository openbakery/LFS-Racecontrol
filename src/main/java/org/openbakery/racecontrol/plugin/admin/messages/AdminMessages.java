package org.openbakery.racecontrol.plugin.admin.messages;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jinsim.response.ButtonClickedResponse;
import net.sf.jinsim.response.HiddenMessageResponse;
import net.sf.jinsim.response.InSimResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openbakery.racecontrol.RaceControl;
import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.gui.Button;
import org.openbakery.racecontrol.gui.MessageSize;
import org.openbakery.racecontrol.gui.Panel;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.openbakery.racecontrol.plugin.Plugin;
import org.openbakery.racecontrol.plugin.admin.messages.data.AdminMessage;
import org.openbakery.racecontrol.plugin.admin.messages.service.AdminMessagesService;
import org.openbakery.racecontrol.plugin.admin.messages.web.AdminMessagesOverviewPage;
import org.openbakery.racecontrol.web.bean.MenuItem;
import org.openbakery.racecontrol.web.bean.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminMessages implements Plugin {

  private static Logger log = LoggerFactory.getLogger(AdminMessages.class);

	private RaceControl raceControl;

	private Map<Integer, Panel> adminMessagePanel = new HashMap<Integer, Panel>();

	private Button cancelButton;

	private Button clearButton;

	@Autowired
	private AdminMessagesService adminMessagesService;

	public AdminMessages() {
	}

	public void packetReceived(InSimResponse response) {
		if (response instanceof HiddenMessageResponse) {
			HiddenMessageResponse hiddenMessageResponse = (HiddenMessageResponse) response;
			int connectionId = hiddenMessageResponse.getConnectionId();
			Driver driver = raceControl.getRace().getDriver(connectionId, "");
			if (connectionId == 0 || driver.isAdmin()) {

				String message = hiddenMessageResponse.getMessage();

				if (message.startsWith("bmsg ")) {
					if (message.length() > 4) {
						adminMessagesService.sendButtonMessage(message.substring(5, message.length()), 5, MessageSize.LARGE);
					}
				} else if (message.startsWith("msg ")) {
					if (message.length() > 4) {
						adminMessagesService.sendButtonMessage(message.substring(4, message.length()), 5, MessageSize.MEDIUM);
					}
				} else if (message.startsWith("smsg")) {
					if (message.length() > 3) {
						adminMessagesService.sendStaticButtonMessage(message.substring(4, message.length()));
					}
				} else if (message.startsWith("amsg")) {
					if (message.length() > 4) {
						String key = message.substring(5, message.length());
						showAdminMessage(key);
					} else {
						showMessagePanel(driver.getConnectionId());
					}
				} else if (message.startsWith("cmsg")) {
					adminMessagesService.hideAllMessages();
				}

			}

		} else if (response instanceof ButtonClickedResponse) {
			processClickResponse((ButtonClickedResponse) response);
		}
	}

	private void showAdminMessage(String key) {
		try {
			for (AdminMessage m : adminMessagesService.getAllMessages()) {
				if (key.equals(m.getKey())) {
					adminMessagesService.showAdminMessage(m);
					return;
				}
			}
		} catch (PersistenceException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void processClickResponse(ButtonClickedResponse response) {
		int id = response.getClickId();
		Panel panel = adminMessagePanel.get(Integer.valueOf(response.getConnectionId()));
		if (panel != null) {

			Button button = panel.getButton(id);

			if (button != null && button != cancelButton && button != clearButton) {
				showAdminMessage(((AdminMessage) button.getObject()).getKey());
			} else if (button != null && button == clearButton) {
				adminMessagesService.hideAllMessages();
			}
			panel.destroy();
		}

	}

	public void setRaceControl(RaceControl raceControl) {
		this.raceControl = raceControl;
	}

	public String getHelp() {
		return "Provided commands by the '" + getName() + "' plugin are: bmsg - big message, msg - message, smsg - static message";
	}

	public String getName() {
		return "Admin Messages";
	}

	private void showMessagePanel(int connectionId) {

		Panel panel = adminMessagePanel.get(Integer.valueOf(connectionId));
		if (panel != null) {
			panel.destroy();
			adminMessagePanel.remove(Integer.valueOf(connectionId));
		}

		panel = new Panel(20, 40);
		panel.setColumns(30, 100);

		panel.add(new Button(connectionId, "Key"), 0);
		panel.add(new Button(connectionId, "Message"), 1);

		try {
			for (AdminMessage message : adminMessagesService.getAllMessages()) {
				Button button = new Button(connectionId, message.getKey());
				button.setClickable(this);
				button.setObject(message);
				panel.add(button);
				Button text = new Button(connectionId, message.getMessage());
				panel.add(text, 1);
			}
		} catch (PersistenceException persistenceException) {
			log.error(persistenceException.getMessage(), persistenceException);
		}

		cancelButton = new Button(connectionId, "Cancel");
		cancelButton.setClickable(this);
		panel.add(cancelButton);

		clearButton = new Button(connectionId, "Clear");
		clearButton.setClickable(this);
		panel.add(clearButton, 1);

		adminMessagePanel.put(Integer.valueOf(connectionId), panel);
		try {
			panel.setVisible(true);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

	}

	public AdminMessage getAdminMessage(String key) {
		try {
			List<AdminMessage> messageList = adminMessagesService.getAllMessages();
			for (AdminMessage message : messageList) {
				if (message.getKey().equals(key)) {
					return message;
				}
			}
		} catch (PersistenceException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public List<MenuItem> getMenuItems() {
		return Arrays.asList(new MenuItem("Admin Messages", AdminMessagesOverviewPage.class, Visibility.AUTHENTICATED));
	}

}
