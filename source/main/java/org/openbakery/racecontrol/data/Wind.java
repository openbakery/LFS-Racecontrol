package org.openbakery.racecontrol.data;

public enum Wind {
	NONE(0),
	LOW(1),
	HIGH(2);
	
	private int value;
	
	private Wind(int value) {
		this.value = value;
	}
	
	public static Wind getWind(int value) {
		for(Wind wind : Wind.values()) {
			if (wind.value == value) {
				return wind;
			}
		}
		throw new IllegalArgumentException("Given value is not a wind");
	}
	
	public int getValue() {
		return value;
	}

}
