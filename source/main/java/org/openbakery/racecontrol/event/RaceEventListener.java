package org.openbakery.racecontrol.event;

public interface RaceEventListener {

	
	public void raceStartEvent(RaceEvent event);
	
	public void raceEndEvent(RaceEvent event);
	
	public void raceNewDriverEvent(RaceEvent event);
}
