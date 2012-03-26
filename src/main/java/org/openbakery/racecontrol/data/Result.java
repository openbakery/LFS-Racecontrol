package org.openbakery.racecontrol.data;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openbakery.racecontrol.persistence.FilePersistence;

@Entity
@Table(name = "racecontrol_result")
public class Result implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7159846241035255835L;

	private static final int DID_NOT_PIT = 64;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@Column(name = "laps_completed")
	private int lapsCompleted;

	@Column(name = "confirmation_flags")
	private int confirmationFlags;

	@Column(name = "pit_stops")
	private int pitStops;

	@Column(name = "race_time")
	private long raceTime;

	@Column(name = "best_lap_time")
	private long bestLapTime;

	@Column(name = "finish_position")
	private int position;

	public Result() {

	}

	public long getBestLapTime() {
		return bestLapTime;
	}

	public void setBestLapTime(long bestLapTime) {
		this.bestLapTime = bestLapTime;
	}

	public int getConfirmationFlags() {
		return confirmationFlags;
	}

	public void setConfirmationFlags(int confirmationFlags) {
		this.confirmationFlags = confirmationFlags;
	}

	public int getLapsCompleted() {
		return lapsCompleted;
	}

	public void setLapsCompleted(int lapsCompleted) {
		this.lapsCompleted = lapsCompleted;
	}

	public int getPitStops() {
		return pitStops;
	}

	public void setPitStops(int pitStops) {
		this.pitStops = pitStops;
	}

	public long getRaceTime() {
		return raceTime;
	}

	public void setRaceTime(long raceTime) {
		this.raceTime = raceTime;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int resultPosition) {
		this.position = resultPosition;
	}

	public String toString() {
		return "Result [ racetime: " + raceTime + ", bestLapTime: " + bestLapTime + ", lapsCompleted: " + lapsCompleted + ", pit stops: "
				+ pitStops + ", resultPosition: " + position + ", confirmation flags: " + confirmationFlags + "]";
	}

	public String toString(String lfsWorldName) {
		String result = null;
		if (lfsWorldName != null) {
			result = lfsWorldName;
			result += FilePersistence.DELIMITER;
			result += Integer.toString(position);
			result += FilePersistence.DELIMITER;
			result += Long.toString(raceTime);
			result += FilePersistence.DELIMITER;
			result += Long.toString(bestLapTime);
			result += FilePersistence.DELIMITER;
			result += Integer.toString(lapsCompleted);
			result += FilePersistence.DELIMITER;
			result += Integer.toString(pitStops);
			result += FilePersistence.DELIMITER;
			result += Integer.toString(confirmationFlags);
		}
		return result;
	}

	public void store(String lfsWorldName, Writer writer) throws IOException {
		if (lfsWorldName != null) {
			writer.write(toString(lfsWorldName));
			writer.write("\n");
		}
	}

	public static void storeHeader(Writer writer) throws IOException {
		writer.write("RACECONTROL-SECTION: RESULTS\n");
		writer.write("#lfsWorldName" + FilePersistence.DELIMITER + "position" + FilePersistence.DELIMITER + "racetime"
				+ FilePersistence.DELIMITER + "bestLapTime" + FilePersistence.DELIMITER + "lapsCompleted" + FilePersistence.DELIMITER
				+ "pitStops" + FilePersistence.DELIMITER + "confirmationFlags\n");

	}

	public boolean isDisqualified() {
		return ((confirmationFlags & DID_NOT_PIT) > 0);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
