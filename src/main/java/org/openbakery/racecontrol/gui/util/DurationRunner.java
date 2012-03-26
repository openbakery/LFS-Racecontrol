package org.openbakery.racecontrol.gui.util;

import java.text.MessageFormat;

import org.openbakery.racecontrol.gui.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DurationRunner implements Runnable {

	public void setObserver(DurationObserver observer) {
		this.observer = observer;
	}

	private static Logger log = LoggerFactory.getLogger(DurationRunner.class);

	private static int SLEEP = 1000;

	private Button button;

	private String buttonText;

	private int timeout;

	private long startTime;

	private boolean running;

	public DurationObserver observer;

	public DurationRunner(Button button, String buttonText, int timeout) {
		this.button = button;
		this.buttonText = buttonText;
		this.timeout = timeout;
		startTime = System.currentTimeMillis();
		running = true;
	}

	public void run() {
		String replaceText = getReplaceText(timeout);
		button.setText(MessageFormat.format(buttonText, replaceText));
		log.debug("change the message button {}", button.getText());
		while (running) {
			try {
				Thread.sleep(SLEEP);
				long currentTime = System.currentTimeMillis();
				if ((startTime + timeout - currentTime) < 0) {
					running = false;
				} else {
					String newText = getReplaceText((int) (startTime + timeout - currentTime));
					if (!newText.equals(replaceText)) {
						replaceText = newText;
						button.setText(MessageFormat.format(buttonText, replaceText));
						log.debug("change the message button {}", button.getText());
					}
				}

			} catch (InterruptedException e) {
				log.error(e.getMessage(), e);
			}
		}
		log.debug("hide the message button {}", button.getText());

		button.destroy();
		if (observer != null) {
			observer.timeExpired(button);
		}
	}

	private String getReplaceText(int time) {
		// log.debug("get replace text for {}", time);
		if (time > 60000) {
			return Integer.toString(time / 60000) + " Minuten";
		}
		if (time > 10000) {
			return Integer.toString(((int) Math.ceil(time / 10000.0)) * 10) + " Sekunden";
		}
		return Integer.toString((int) Math.ceil(time / 1000.0)) + " Sekunden";
	}

	public static void main(String[] args) {
		Thread thread = new Thread(new DurationRunner(new Button(0, ""), "{0} Pause", 30000));
		thread.start();
	}
}
