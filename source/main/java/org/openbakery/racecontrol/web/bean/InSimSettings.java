package org.openbakery.racecontrol.web.bean;

import java.io.Serializable;

public class InSimSettings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9109062541976287418L;

	private String name;

	private String hostname;

	private int port;

	private String adminPassword;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

}
