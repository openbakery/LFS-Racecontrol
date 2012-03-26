package org.openbakery.racecontrol.plugin.live.service;

import java.util.Comparator;

import org.openbakery.racecontrol.plugin.live.web.LiveTiming;

public class LiveTimingComparator implements Comparator<LiveTiming> {

	public int compare(LiveTiming first, LiveTiming second) {

		int result = second.getLapsCompleted() - first.getLapsCompleted();
		if (result != 0) {
			return result;
		}

		result = first.getTotalTime() - second.getTotalTime();
		if (result != 0) {
			return result;
		}

		return first.getStartingPosition() - second.getStartingPosition();
	}

}
