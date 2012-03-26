package org.openbakery.racecontrol.gui;

import java.io.IOException;
import java.util.LinkedList;

import org.openbakery.racecontrol.gui.util.DurationObserver;
import org.openbakery.racecontrol.gui.util.DurationRunner;
import org.openbakery.racecontrol.gui.util.HideRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ButtonMessageHelper implements DurationObserver {

	private static Logger log = LoggerFactory.getLogger(ButtonMessageHelper.class);

	private static ButtonMessageHelper instance;

	private LinkedList<Button> buttonList = new LinkedList<Button>();

	public static ButtonMessageHelper getInstance() {
		if (instance == null) {
			instance = new ButtonMessageHelper();
		}
		return instance;
	}

	protected ButtonMessageHelper() {
		// buttonList = new LinkedList<Button>();
	}

	public synchronized void sendButtonMessage(String text) {
		sendButtonMessage(text, 5000, MessageSize.SMALL);
	}

	public synchronized void sendButtonMessage(int connectionId, String text, int sleep, MessageSize size) {
		Button button = new Button(connectionId, text, 50, 5, 100, size.getSize());
		button.setHideTime(sleep * 1000);
		buttonList.add(button);
		showNextButton();
	}

	public synchronized void sendButtonMessage(String text, int sleep, MessageSize size) {
		sendButtonMessage(255, text, sleep, size);
	}

	private void showNextButton() {

		Button button = null;
		int positionSum = 0;
		for (int i = 0; i < 3; i++) {
			button = buttonList.get(i);
			log.debug("buttons: {}", buttonList);

			if (!button.isVisible()) {
				int hideTime = button.getHideTime();
				button.setHideTime(0);

				if (button.getText().contains("{0}")) {
					DurationRunner runner = new DurationRunner(button, button.getText(), hideTime);
					runner.setObserver(this);
					Thread thread = new Thread(runner);
					thread.start();
				} else {
					HideRunner runner = new HideRunner(button, hideTime);
					runner.setObserver(this);
					Thread thread = new Thread(runner);
					thread.start();
				}

				log.debug("button.y: {}", button.getY());
				if (positionSum == 5 || positionSum == 30) {
					// postionSum == 5 -> one button is displayed on the top
					// positionSum == 30 -> 2 buttons are displayed on the top and bottom
					button.setY(15);
				} else if (positionSum == 20) {
					// two buttons are displayed so place on the bottom
					button.setY(25);
				} else {
					button.setY(5);
				}
				log.debug("button.y: {}", button.getY());
				try {
					button.setVisible(true);
				} catch (IOException e) {
					log.error("Unable to set button visible!", e);
				}
				return;
			} else {
				positionSum += button.getY();
			}
		}

	}

	public synchronized Button sendStaticButtonMessage(String text) {
		Button button = new Button(255, text, 50, 5, 100, 5);
		try {
			button.setVisible(true);
			// buttonList.add(button);
		} catch (IOException e) {
			log.error("Unable to set button visible!", e);
		}
		return button;
	}

	public void timeExpired(Button button) {
		buttonList.remove(button);
		if (buttonList.size() > 0) {
			showNextButton();
		}
	}

	public synchronized void hideAll() {
		for (Button button : buttonList) {
			button.destroy();
		}
		buttonList.clear();
	}
}
