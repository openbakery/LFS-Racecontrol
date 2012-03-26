package org.openbakery.racecontrol.event;

public interface LapEventListener {

	public void lapFinished(LapEvent event);

	public void lapSplit(LapEvent event);
}
