package org.openbakery.racecontrol.plugin.penalty.data;

import org.openbakery.racecontrol.data.Driver;

public class Penalty {

	private Driver driver;

	private Type type;

	private Reason reason;

	private boolean isPending;

	private long time;

	public enum Reason {
		CHAT,
		ADMIN;
	}

	public enum Type {
		DRIVE_THOUGH,
		STOP_AND_GO,
		CLEAR;
	}

	public Penalty(Driver driver, Type type, Reason reason) {
		super();
		this.driver = driver;
		this.type = type;
		this.reason = reason;
		isPending = false;
		time = System.currentTimeMillis();
	}

	public Driver getDriver() {
		return driver;
	}

	public Type getType() {
		return type;
	}

	public Reason getReason() {
		return reason;
	}

	public String getPenaltyCommando() {
		switch (type) {
		case DRIVE_THOUGH:
			return "/p_dt " + driver.getName();
		case STOP_AND_GO:
			return "/p_sg " + driver.getName();
		case CLEAR:
			return "/p_clear " + driver.getName();
		}
		return null;
	}

	public boolean isPending() {
		return isPending;
	}

	public void setPending(boolean isPending) {
		this.isPending = isPending;
	}

	public long getTime() {
		return time;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Penalty[driver=");
		builder.append(driver.getPlayerName());
		builder.append(", type=");
		builder.append(type);
		builder.append(", reason=");
		builder.append(reason);
		builder.append("]");
		return builder.toString();
	}
}
