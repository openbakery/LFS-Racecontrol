package org.openbakery.racecontrol.plugin.penalty.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.sf.jinsim.request.MessageRequest;

import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.gui.ButtonMessageHelper;
import org.openbakery.racecontrol.gui.MessageSize;
import org.openbakery.racecontrol.plugin.penalty.data.Penalty;
import org.openbakery.racecontrol.service.RaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PenaltyService {

	private static Logger logger = LoggerFactory.getLogger(PenaltyService.class);

	@Autowired
	private RaceService raceService;

	private LinkedList<Penalty> penaltyToConfirm = new LinkedList<Penalty>();

	private PenaltyExpireRunner penaltyExpireRunner;

	private boolean penaltyActive;

	public synchronized void confirmChatPenalty(int connectionId) {
		Driver driver = raceService.getRaceControl().getRace().getDriver(connectionId, "");
		Penalty penalty = new Penalty(driver, Penalty.Type.DRIVE_THOUGH, Penalty.Reason.CHAT);
		penaltyToConfirm.add(penalty);

		sendConfirmQuestion();
	}

	public synchronized void sendConfirmQuestion() {

		for (Penalty p : penaltyToConfirm) {
			if (p.isPending()) {
				return;
			}
		}

		if (penaltyToConfirm.size() > 0) {
			Penalty penalty = penaltyToConfirm.getFirst();
			penalty.setPending(true);
			sendMessageToAdmins("Confirm penalty for " + penalty.getDriver().getName() + " with '/i confirm'");
		}
	}

	public synchronized void penaltyConfirmed() {
		Penalty penalty = penaltyToConfirm.removeFirst();
		sendMessageToAdmins("Penalty confirmed for " + penalty.getDriver().getPlayerName() + "!");
		executePenalty(penalty);

	}

	public synchronized void sendPenalty(Driver driver, Penalty.Type type) {
		executePenalty(new Penalty(driver, type, Penalty.Reason.ADMIN));
	}

	private void executePenalty(Penalty penalty) {
		String command = penalty.getPenaltyCommando();
		if (command != null) {
			MessageRequest msgRequest = new MessageRequest();
			msgRequest.setMessage(command);
			try {
				raceService.getClient().send(msgRequest);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}

	}

	public void deleteAllPenalties() {
		penaltyToConfirm = new LinkedList<Penalty>();
	}

	public void sendMessageToAdmins(String message) {
		List<Driver> admins = raceService.getRaceControl().getRace().getAdmins();
		for (Driver admin : admins) {
			ButtonMessageHelper.getInstance().sendButtonMessage(admin.getConnectionId(), message, 5, MessageSize.SMALL);
		}
	}

	public void setPenaltyActive(boolean active) {
		penaltyActive = active;
		if (penaltyActive) {
			sendMessageToAdmins("Penalties on");
			if (penaltyExpireRunner == null) {
				penaltyExpireRunner = new PenaltyExpireRunner(this);
				Thread expireThread = new Thread(penaltyExpireRunner);
				expireThread.start();
			}
		} else {
			sendMessageToAdmins("Penalties off");
			deleteAllPenalties();
			if (penaltyExpireRunner != null) {
				penaltyExpireRunner.stop();
			}
			penaltyExpireRunner = null;
		}

	}

	public LinkedList<Penalty> getPenaltyToConfirm() {
		return penaltyToConfirm;
	}

	public void penaltyExpired(Penalty penalty) {
		penaltyToConfirm.remove(penalty);
		sendMessageToAdmins("Penalty expired: " + penalty.getDriver().getPlayerName());
	}

	public boolean isPenaltyActive() {
		return penaltyActive;
	}

	public void penaltyDeclined() {
		Penalty penalty = penaltyToConfirm.removeFirst();
		sendMessageToAdmins("Penalty declined for " + penalty.getDriver().getPlayerName() + "!");
	}

	public List<Driver> getDrivers() {
		return raceService.getRaceControl().getRace().getDrivers();
	}

}
