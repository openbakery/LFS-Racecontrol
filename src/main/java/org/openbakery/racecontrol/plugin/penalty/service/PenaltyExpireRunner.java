package org.openbakery.racecontrol.plugin.penalty.service;

import java.util.List;

import org.openbakery.racecontrol.plugin.penalty.data.Penalty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PenaltyExpireRunner implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(PenaltyExpireRunner.class);

	private static final long TIMEOUT = 120000;

	private boolean active;

	private PenaltyService service;

	public PenaltyExpireRunner(PenaltyService service) {
		this.service = service;
	}

	public void run() {
		active = true;
		while (active) {
			List<Penalty> clonedList = (List<Penalty>) service.getPenaltyToConfirm().clone();
			for (Penalty penalty : clonedList) {
				if (penalty.getTime() + TIMEOUT < System.currentTimeMillis()) {
					service.penaltyExpired(penalty);
				}
			}
			service.sendConfirmQuestion();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}

	}

	public void stop() {
		active = false;
	}

}
