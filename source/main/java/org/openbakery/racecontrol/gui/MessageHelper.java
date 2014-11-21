package org.openbakery.racecontrol.gui;


import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openbakery.racecontrol.gui.Button.TextAlign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHelper implements Runnable {
	
  private static Logger log = LoggerFactory.getLogger(MessageHelper.class);


	public static synchronized void sendMessage(byte connectionId, String message) {
		if (log.isDebugEnabled()) {
			log.debug("Show message to " + connectionId + " - " + message);
		}
		MessageHelper helper = new MessageHelper(message, connectionId);
		Thread thread = new Thread(helper);
		thread.start();
	}
	
	private Button button;

	private MessageHelper(String message, int connectionId) {
		button = new Button(connectionId, message, 50, 5, 100, 6);
		button.setTextAlign(TextAlign.center);
	}
	
	public void run() {
		try {
			button.setVisible(true);
			Thread.sleep(6000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
				button.destroy();
				//button.setVisible(false);
		}
	}
	
	public static synchronized Button sendStaticButtonMessage(String message) {
		Button button = new Button(255, message, 50, 5, 100, 6);
		try {
			button.setVisible(true);
		} catch (IOException e) {
			log.error("Unable to set button visible!", e);
		}
		return button;
	}
}
