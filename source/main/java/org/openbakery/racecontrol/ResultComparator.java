package org.openbakery.racecontrol;

import java.util.Comparator;

import org.openbakery.racecontrol.data.Result;


public class ResultComparator implements Comparator<Result> {

	public int compare(Result firstResult, Result secondResult) {
		
		if (firstResult.isDisqualified() && !secondResult.isDisqualified()) {
			return 1;
		}
		
		if (!firstResult.isDisqualified() && secondResult.isDisqualified()) {
			return -1;
		}
		
		int result = secondResult.getLapsCompleted() - firstResult.getLapsCompleted();
		if (result != 0)
			return result;
		
		long firstRaceTime = firstResult.getRaceTime();
		long secondRaceTime = secondResult.getRaceTime();
		
		if (firstRaceTime == 0 && secondRaceTime == 0) {
			// qualifing result
			return (int) ((firstResult.getBestLapTime() - secondResult.getBestLapTime())%Integer.MAX_VALUE);
		}
		
		int raceTime = (int)(firstRaceTime - secondRaceTime);
		
		if (raceTime != 0) {
			return raceTime;
		}
		
		// when qual result
		long firstBestLap = firstResult.getBestLapTime();
		long secondBestLap = secondResult.getBestLapTime();
		
		return (int)(firstBestLap - secondBestLap);
	}

}
