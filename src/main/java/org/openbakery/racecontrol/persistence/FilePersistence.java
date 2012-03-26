package org.openbakery.racecontrol.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.Flag;
import org.openbakery.racecontrol.data.Lap;
import org.openbakery.racecontrol.data.RaceEntry;
import org.openbakery.racecontrol.data.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilePersistence implements Persistence {

	public static final String DELIMITER = ";";

	private static Logger log = LoggerFactory.getLogger(FilePersistence.class);

	RaceEntry raceEntry;

	public FilePersistence() {
	}

	private void storeRaceEntry(RaceEntry race) throws PersistenceException {

		if (race == null) {
			throw new IllegalArgumentException("Given race is null");
		}
		FileWriter writer = null;
		try {
			String filename = getFilename(race);
			writer = new FileWriter(filename);
			log.info("Store results to: " + filename);

			RaceEntry.storeHeader(writer);
			race.store(writer);

			Driver.storeHeader(writer);
			for (Driver driver : raceEntry.getDrivers()) {
				driver.store(writer);
			}

			Result.storeHeader(writer);
			for (Driver driver : raceEntry.getDrivers()) {
				Result result = driver.getResult();
				if (result != null)
					result.store(driver.getName(), writer);
			}

			Lap.storeHeader(writer);
			for (Driver driver : raceEntry.getDrivers()) {
				if (driver.getResult() != null) {
					for (Lap lap : driver.getCompletedLaps(driver.getLongestAttempt())) {
						lap.store(driver.getName(), writer);
					}
				}
			}

			Flag.storeHeader(writer);
			for (Driver driver : raceEntry.getDrivers()) {
				Result result = driver.getResult();
				if (result != null) {
					int numberLaps = result.getLapsCompleted();
					for (Lap lap : driver.getAllLaps()) {
						for (Flag flag : lap.getFlags()) {
							if (lap.getNumber() <= numberLaps) {
								flag.store(driver.getName(), lap.getNumber(), writer);
							}
						}
					}
				}
			}
			writer.close();
		} catch (IOException ex) {
			throw new PersistenceException(ex);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException ex) {
					throw new PersistenceException(ex);
				}
			}
		}
	}

	private String getFilename(RaceEntry race) {
		return getFilename(race, 0);
	}

	private String getFilename(RaceEntry race, int i) {
		String filename = race.getTrack().trim();
		if (!race.isQualifying()) {
			filename += "_race";
		} else {
			filename += "_qual";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddkkmm");
		filename += "_" + sdf.format(new Date());
		if (i > 0) {
			filename += "_" + i;
		}
		filename += ".rcsv";
		File file = new File(filename);
		if (file.exists()) {
			return getFilename(race, i + 1);
		}

		return filename;
	}

	public void close() throws PersistenceException {
		flush();
	}

	public <T> T store(T object) throws PersistenceException {
		if (object instanceof RaceEntry) {
			this.raceEntry = (RaceEntry) object;
		}
		// else if (object instanceof Driver) {
		// Driver driver = (Driver) object;
		// ListIterator<Driver> listIterator = drivers.listIterator();
		// while (listIterator.hasNext()) {
		// Driver d = listIterator.next();
		// if (d.getPlayerId() == driver.getPlayerId()) {
		// listIterator.remove();
		// }
		// }
		// drivers.add(driver);
		// }

		return object;
	}

	public void flush() throws PersistenceException {
		if (raceEntry != null) {
			storeRaceEntry(raceEntry);
		}
		raceEntry = null;
	}

	public List<? extends Object> query(String query) throws PersistenceException {
		return Collections.emptyList();
	}

	public List<? extends Object> queryNative(String query, String name) throws PersistenceException {
		return Collections.emptyList();
	}

	public List<? extends Object> queryNative(String query, Class<? extends Object> clazz) throws PersistenceException {
		return Collections.emptyList();
	}

	public <T> T delete(T object) throws PersistenceException {
		log.info("delete is not suppored in the file peristence");
		return object;
	}

  @Override
  public Transaction createTransaction() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
