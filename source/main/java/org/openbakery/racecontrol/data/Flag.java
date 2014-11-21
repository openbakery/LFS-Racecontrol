package org.openbakery.racecontrol.data;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import org.openbakery.racecontrol.persistence.FilePersistence;

public class Flag implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7798292675442416682L;

	public static final int CAUSING_YELLOW = 2;

	public static final int GIVEN_BLUE = 1;

	private int type;

	private long startTime;

	private long duratation = 0;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getDuratation() {
		return duratation;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(long endTime) {
		duratation = startTime - startTime;
		startTime = 0;
	}

	public String toString(String lfsWorldName, int lapNumber) {
		String result = null;
		if (lfsWorldName != null) {
			result = lfsWorldName;
			result += FilePersistence.DELIMITER;
			result += Integer.toString(lapNumber);
			result += FilePersistence.DELIMITER;
			result += Integer.toString(type);
			result += FilePersistence.DELIMITER;
			result += Long.toString(duratation);
		}
		return result;
	}

	public void store(String lfsWorldName, int lapNumber, Writer writer) throws IOException {
		if (lfsWorldName != null) {
			writer.write(toString(lfsWorldName, lapNumber));
			writer.write("\n");
		}
	}

	public static void storeHeader(Writer writer) throws IOException {
		writer.write("RACECONTROL-SECTION: FLAGS\n");
		writer.write("#lfsworldName" + FilePersistence.DELIMITER + "lapNumber " + FilePersistence.DELIMITER + "type"
				+ FilePersistence.DELIMITER + "duration\n");

	}

}
