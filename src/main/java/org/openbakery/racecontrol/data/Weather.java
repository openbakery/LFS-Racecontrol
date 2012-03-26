package org.openbakery.racecontrol.data;


public enum Weather {

	BRIGHT_CLEAR(0),
	OVERCAST(1),
	CLOUDY_SUNSET_DUSK(2);

	private int value;

	private Weather(int value) {
		this.value = value;
	}

	public static Weather getWeather(int insimValue) {
		for (Weather weather : Weather.values()) {
			if (weather.value == insimValue) {
				return weather;
			}
		}
		throw new IllegalArgumentException("Given value is not a weather");
	}

	public int getValue() {
		return value;
	}

}
