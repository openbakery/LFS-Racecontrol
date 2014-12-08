package org.openbakery.racecontrol.plugin;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.openbakery.jinsim.response.InSimResponse;
import org.openbakery.jinsim.response.NewConnectionResponse;

import org.openbakery.racecontrol.gui.Button;
import org.openbakery.racecontrol.web.bean.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WelcomeMessage implements Plugin {

	private static Logger log = LoggerFactory.getLogger(WelcomeMessage.class);

	private String message;

	public WelcomeMessage(String message) {
		this.message = message;
	}

	public void packetReceived(InSimResponse response) {
		if (response instanceof NewConnectionResponse) {
			processNewConnectionResponse((NewConnectionResponse) response);
		}
	}

	private void processNewConnectionResponse(NewConnectionResponse response) {
		Button button = new Button(response.getConnectionId(), "", 50, 5, 100, 6);
		log.debug("Show message to {} - {}", response.getConnectionId(), response.getPlayerName());
		MessageRunner runner = new MessageRunner(button, 6000);
		Thread thread = new Thread(runner);
		thread.start();
	}

	private class MessageRunner implements Runnable {
		private Button button;

		private int time;

		public MessageRunner(Button button, int time) {
			this.button = button;
			this.time = time;
		}

		public void run() {
			try {
				StringTokenizer tokens = new StringTokenizer(message, "\n");
				while (tokens.hasMoreTokens()) {
					log.debug("Show button text {}", button);
					button.setText(tokens.nextToken());
					button.setVisible(true);
					Thread.sleep(time);
				}
			} catch (Exception e) {
				log.error("Unable so set button visible", e);
			} finally {
				try {
					button.setVisible(false);
				} catch (IOException e) {
					log.error("Unable so set button invisible", e);
				}
			}
		}
	}

	public String getHelp() {
		return "";
	}

	public String getName() {
		return "Welcome Message";
	}

	public List<MenuItem> getMenuItems() {
		return Collections.emptyList();
	}

}
