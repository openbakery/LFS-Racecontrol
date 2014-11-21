package org.openbakery.racecontrol.gui.util;

import org.openbakery.racecontrol.gui.Button;

public class HideRunner implements Runnable {
	public void setObserver(DurationObserver observer) {
		this.observer = observer;
	}

	private Button button;

	private int timeout;

	public DurationObserver observer;

	public HideRunner(Button button, int timeout) {
		this.button = button;
		this.timeout = timeout;
	}

	public void run() {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		button.destroy();
		if (observer != null) {
			observer.timeExpired(button);
		}
	}
}