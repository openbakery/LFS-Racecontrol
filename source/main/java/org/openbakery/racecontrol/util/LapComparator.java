package org.openbakery.racecontrol.util;

import java.util.Comparator;

import org.openbakery.racecontrol.data.Lap;

public class LapComparator implements Comparator<Lap> {

	public int compare(Lap first, Lap second) {
		if (second == null) {
			return -1;
		}
		if (first == null) {
			return 1;
		}

		if (first.getTime() == 0) {
			return 1;
		}

		if (second.getTime() == 0) {
			return -1;
		}

		if (first.getTime() == second.getTime()) {
			return (int)(first.getCreatedAt().getTime() - second.getCreatedAt().getTime());
		}

		return first.getTime() - second.getTime();
	}

}
