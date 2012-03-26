package org.openbakery.racecontrol.plugin.live.web;

import java.util.List;

import org.openbakery.racecontrol.data.Lap;

public class LiveTiming {

	private String driverName;

	private int totalTime;

	private int lastLapTime;

	private int split1;

	private int split2;

	private int split3;

	private int split4;

	private int position;

	private int lapsCompleted;

	private int startingPosition;

	private List<Lap> laps;

	public int getStartingPosition() {
		return startingPosition;
	}

	public void setStartingPosition(int startingPosition) {
		this.startingPosition = startingPosition;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public int getTotalTime(int lapNumber) {
		if (laps.size() > lapNumber - 1) {
			return laps.get(lapNumber - 1).getTotalTime();
		}
		return 0;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	public int getLastLapTime() {
		return lastLapTime;
	}

	public void setLastLapTime(int lastLapTime) {
		this.lastLapTime = lastLapTime;
	}

	public int getSplit1() {
		return split1;
	}

	public void setSplit1(int split1) {
		this.split1 = split1;
	}

	public int getSplit2() {
		return split2;
	}

	public void setSplit2(int split2) {
		this.split2 = split2;
	}

	public int getSplit3() {
		return split3;
	}

	public void setSplit3(int split3) {
		this.split3 = split3;
	}

	public int getSplit4() {
		return split4;
	}

	public void setSplit4(int split4) {
		this.split4 = split4;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getLapsCompleted() {
		return lapsCompleted;
	}

	public void setLapsCompleted(int lapsCompleted) {
		this.lapsCompleted = lapsCompleted;
	}

	public void setLaps(List<Lap> completedLaps) {
		this.laps = completedLaps;
	}

}
