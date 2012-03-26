package org.openbakery.racecontrol.gui;

public enum MessageSize {
	SMALL(4),
	MEDIUM(8),
	LARGE(12);

	private int size;

	private MessageSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}
}