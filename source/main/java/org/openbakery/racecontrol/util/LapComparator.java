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

		return first.getTime() - second.getTime();
	}

}
