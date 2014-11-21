package org.openbakery.racecontrol.plugin.penalty;

import java.util.Arrays;
import java.util.List;

import net.sf.jinsim.response.HiddenMessageResponse;
import net.sf.jinsim.response.InSimResponse;
import net.sf.jinsim.response.MessageResponse;

import org.openbakery.racecontrol.RaceControl;
import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.plugin.Plugin;
import org.openbakery.racecontrol.plugin.penalty.service.PenaltyService;
import org.openbakery.racecontrol.plugin.penalty.web.PenaltyPage;
import org.openbakery.racecontrol.web.bean.MenuItem;
import org.openbakery.racecontrol.web.bean.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PenaltyPlugin implements Plugin {

	private static Logger logger = LoggerFactory.getLogger(PenaltyPlugin.class);

	public static final String MESSAGE_CONFIRM = "confirm";

	public static final String MESSAGE_DECLINE = "decline";

	@Autowired
	private PenaltyService penaltyService;

	@Autowired
	private RaceControl raceControl;

	public String getHelp() {
		return "Plugin to manage penalties";
	}

	public List<MenuItem> getMenuItems() {
		return Arrays.asList(new MenuItem("Penalty", PenaltyPage.class, Visibility.AUTHENTICATED, 0));
	}

	public String getName() {
		return "Penalty";
	}

	public void packetReceived(InSimResponse response) {
		if (response instanceof MessageResponse) {
			if (penaltyService.isPenaltyActive()) {
				MessageResponse messageResponse = (MessageResponse) response;
				if (messageResponse.getConnectionId() > 0) {
					logger.debug("chat penalty to {}", messageResponse.getConnectionId());
					penaltyService.confirmChatPenalty(messageResponse.getConnectionId());
				}
			}
		} else if (response instanceof HiddenMessageResponse) {
			HiddenMessageResponse hiddenMessageResponse = (HiddenMessageResponse) response;

			Driver driver = raceControl.getRace().getDriver(hiddenMessageResponse.getConnectionId(), "");
			if (driver.isAdmin()) {
				String message = hiddenMessageResponse.getMessage().toLowerCase();
				logger.debug("process command: {}", message);
				if (message.startsWith(MESSAGE_CONFIRM)) {
					penaltyService.penaltyConfirmed();
				} else if (message.startsWith(MESSAGE_DECLINE)) {
					penaltyService.penaltyDeclined();
				} else if (message.startsWith("penalty on")) {
					penaltyService.setPenaltyActive(true);
				} else if (message.startsWith("penalty off")) {
					penaltyService.setPenaltyActive(false);
				}
			}
		}
	}
}
