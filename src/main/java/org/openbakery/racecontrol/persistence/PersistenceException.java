package org.openbakery.racecontrol.persistence;

public class PersistenceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1384797714695320145L;

	public PersistenceException() {
		super();
	}

	public PersistenceException(String message, Throwable trowable) {
		super(message, trowable);
	}

	public PersistenceException(String message) {
		super(message);
	}

	public PersistenceException(Throwable trowable) {
		super(trowable);
	}

}
