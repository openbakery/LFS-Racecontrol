package org.openbakery.racecontrol;

public class DriverNotFoundException extends Exception {

	private static final long serialVersionUID = -7067259120765870015L;

	public DriverNotFoundException() {
		super();
	}

	public DriverNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public DriverNotFoundException(String message) {
		super(message);
	}

	public DriverNotFoundException(Throwable cause) {
		super(cause);
	}

}
