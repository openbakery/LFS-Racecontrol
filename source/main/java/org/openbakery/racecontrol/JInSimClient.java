package org.openbakery.racecontrol;

import java.io.IOException;

import org.openbakery.jinsim.QueueClient;
import org.openbakery.jinsim.TCPChannel;
import org.openbakery.jinsim.request.InitRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openbakery.racecontrol.web.bean.InSimSettings;

public class JInSimClient extends QueueClient {

	private static Log log = LogFactory.getLog(JInSimClient.class);

	private static JInSimClient instance;

	private InSimSettings settings;

	public JInSimClient(String name, String hostname, int port, String adminPassword) {

		settings = new InSimSettings();
		settings.setName(name);
		settings.setHostname(hostname);
		settings.setPort(port);
		settings.setAdminPassword(adminPassword);
	}

	public static JInSimClient getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Client was not inialized");
		}
		return instance;
	}

	public void connect() throws IOException {
		short flags = (short) (InitRequest.RECEIVE_NODE_LAP & InitRequest.RECEIVE_MULTI_CAR_INFO);
		if (log.isDebugEnabled()) {
			log.debug("Connect to " + settings.getHostname() + ":" + settings.getPort());
		}
		instance = this;
		connect(new TCPChannel(settings.getHostname(), settings.getPort()), settings.getAdminPassword(), settings.getName(), flags, 0, 0);
	}

	public InSimSettings getSettings() {
		return settings;
	}

	public void setSettings(InSimSettings settings) {
		this.settings = settings;
	}

}
