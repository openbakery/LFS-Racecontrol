package org.openbakery.racecontrol.plugin;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sf.jinsim.response.HiddenMessageResponse;
import net.sf.jinsim.response.InSimResponse;

import org.openbakery.racecontrol.gui.Button;
import org.openbakery.racecontrol.gui.Panel;
import org.openbakery.racecontrol.web.bean.MenuItem;

public class ButtonTest implements Plugin {

	private HashMap<Integer, ButtonRunner> runnerMap;

	public ButtonTest() {
		runnerMap = new HashMap<Integer, ButtonRunner>();
	}

	class ButtonRunner implements Runnable {

		private Panel panel;

		private boolean running = true;

		private int count = 0;

		public ButtonRunner(int connectionId) {

			Thread thread = new Thread(this);
			panel = new Panel(1, 60);
			panel.setColumns(5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5);

			for (int x = 0; x < 10; x++) {
				for (int y = 0; y < 10; y++) {
					Button button = new Button(connectionId, "" + ((count++) % 99));
					panel.add(button, x, y);
				}
			}
			try {
				panel.setVisible(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			thread.start();

		}

		public void run() {
			int count = 0;
			while (running) {
				for (int x = 0; x < 10; x++) {
					for (int y = 0; y < 10; y++) {

						Button button = panel.getButton(x, y);
						button.setText("" + ((count++) % 99));
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			}
			panel.destroy();
		}

		public void stop() {
			running = false;
		}

	}

	public void packetReceived(InSimResponse response) {
		if (response instanceof HiddenMessageResponse) {
			HiddenMessageResponse hiddenMessageResonse = (HiddenMessageResponse) response;
			if ("buttontest".equalsIgnoreCase(hiddenMessageResonse.getMessage())) {
				Integer connectionId = Integer.valueOf(hiddenMessageResonse.getConnectionId());

				ButtonRunner runner = runnerMap.get(connectionId);
				if (runner != null) {
					runner.stop();
					runnerMap.remove(connectionId);
				} else {
					runner = new ButtonRunner(connectionId);
					runnerMap.put(connectionId, runner);
				}
			}
		}
	}

	public String getHelp() {
		return "Displays 100 Buttons and updates the texts in it";
	}

	public String getName() {
		return "Button test";
	}

	public List<MenuItem> getMenuItems() {
		return Collections.emptyList();
	}

}
