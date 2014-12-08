package org.openbakery.racecontrol.data;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.*;

import javax.persistence.*;

import org.openbakery.racecontrol.ResultComparator;
import org.openbakery.racecontrol.persistence.FilePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "racecontrol_race_entry")
public class RaceEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(RaceEntry.class);

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@Column(name = "laps")
	private int laps;

	@Column(name = "hours")
	private int hours;

	@Column(name = "qualifyingMinutes")
	private int qualifyingMinutes;

	@Column(name = "racers")
	private int racers;

	@Column(name = "track")
	private String track;

	@Column(name = "weather")
	private Weather weather;

	@Column(name = "wind")
	private Wind wind;

	@Transient
	List<Result> results;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "race_entry_id")
	private List<Driver> drivers;

	@Column(name = "start_time")
	private Date startTime;

	@Column(name = "server_name")
	private String serverName;

	public RaceEntry() {
		log.debug("new race entry");
		startTime = new Date();
		drivers = new ArrayList<Driver>();
		results = new ArrayList<Result>();
	}

	public int getLaps() {
		return laps;
	}

	public void setLaps(int laps) {
		this.laps = laps;
	}

	public int getQualifyingMinutes() {
		return qualifyingMinutes;
	}

	public void setQualifyingMinutes(int qualifyingMinutes) {
		this.qualifyingMinutes = qualifyingMinutes;
	}

	public int getRacers() {
		return racers;
	}

	public void setRacers(int racers) {
		this.racers = racers;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public Weather getWeather() {
		return weather;
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
	}

	public Wind getWind() {
		return wind;
	}

	public void setWind(Wind wind) {
		this.wind = wind;
	}

	public String toString() {
		return track.trim() + FilePersistence.DELIMITER + Integer.toString(laps) + FilePersistence.DELIMITER + Integer.toString(hours) + FilePersistence.DELIMITER + Integer.toString(qualifyingMinutes)
				+ FilePersistence.DELIMITER + Integer.toString(racers) + FilePersistence.DELIMITER + weather + FilePersistence.DELIMITER + wind;
	}

	public void store(Writer writer) throws IOException {
		writer.write(toString());
		writer.write("\n");
	}

	public static void storeHeader(Writer writer) throws IOException {
		writer.write("RACECONTROL-SECTION: RACE\n");
		writer.write("#Track" + FilePersistence.DELIMITER + " Laps" + FilePersistence.DELIMITER + " Hours" + FilePersistence.DELIMITER + " QualifyingMinutes" + FilePersistence.DELIMITER + " NumberRacers"
				+ FilePersistence.DELIMITER + " Weather" + FilePersistence.DELIMITER + " Wind\n");
	}

	public void addDriver(Driver driver) {
		log.debug("ADD Driver: {}", driver);
		Driver duplicatedDriver = null;
		for (Driver currentDrivers : drivers) {
			if (driver.getId() == currentDrivers.getId()) {
				// driver already existes
				log.debug("driver already exists");
				return;
			}
			if (driver.getName().equals(currentDrivers.getName())) {
				log.debug("driver with name already exists {}", driver);
				duplicatedDriver = currentDrivers;
				break;
			}
		}
		drivers.remove(duplicatedDriver);
		drivers.add(driver);
		log.debug("Drivers: {}", drivers);
	}

	public List<Driver> getDrivers() {
		return drivers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public boolean isQualifying() {
		return qualifyingMinutes > 0;
	}

	public boolean isPractice() {
		return laps == 0;
	}

	public void addResult(Result newResult, Driver driver) {
		if (isQualifying()) {
			Result oldResult = driver.getResult();

			if (oldResult == null || newResult.getBestLapTime() < oldResult.getBestLapTime()) {

				if (oldResult != null) {
					results.remove(oldResult);
				}

				ResultComparator comparator = new ResultComparator();
				int position = 0;
				for (Result result : results) {

					if (comparator.compare(result, newResult) > 0) {
						break;
					}
					position++;
				}

				newResult.setPosition(position + 1);
				results.add(position, newResult);
				position = 1;
				for (Result result : results) {
					result.setPosition(position++);
				}
				driver.setResult(newResult);
			}
		} else {
			driver.setResult(newResult);
		}
	}

	public List<Result> getResults() {
		return results;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

}
