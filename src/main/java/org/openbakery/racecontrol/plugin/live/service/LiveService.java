package org.openbakery.racecontrol.plugin.live.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.Lap;
import org.openbakery.racecontrol.plugin.live.web.LiveTiming;
import org.openbakery.racecontrol.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;

public class LiveService {

	@Autowired
	private RaceService raceService;

	public List<LiveTiming> getLiveTiming() {
		List<Driver> driverList = raceService.getRaceControl().getRace().getDrivers();
		ArrayList<LiveTiming> result = new ArrayList<LiveTiming>(driverList.size());

		for (Driver driver : driverList) {
			LiveTiming timing = new LiveTiming();
			timing.setDriverName(driver.getPlayerName());
			timing.setStartingPosition(driver.getStartingPosition());

			timing.setLaps(driver.getCompletedLaps());

			int lapNumber = driver.getCompletedLapCount();
			if (lapNumber > 0) {
				timing.setLapsCompleted(driver.getCompletedLapCount());
				Lap lap = driver.getCompletedLap(lapNumber - 1);
				timing.setLastLapTime(lap.getTime());
				timing.setTotalTime(lap.getTotalTime());
			}
			if (driver.getPlayerId() > 0) {
				result.add(timing);
			}
		}

		Collections.sort(result, new LiveTimingComparator());

		int i = 1;
		for (LiveTiming timing : result) {
			timing.setPosition(i++);
		}

		return result;
	}
}
