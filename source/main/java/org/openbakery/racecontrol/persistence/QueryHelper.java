package org.openbakery.racecontrol.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import net.sf.jinsim.Car;
import net.sf.jinsim.Track;

import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.Lap;
import org.openbakery.racecontrol.persistence.bean.Profile;
import org.openbakery.racecontrol.util.LapComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryHelper {

	private static Logger log = LoggerFactory.getLogger(QueryHelper.class);

	static Pattern lfsWorldNamePattern = Pattern.compile("profile_lfsworldusername\";s:\\d*:\"([^\"]*)");

	private Persistence persistence;

	public QueryHelper(Persistence persistence) {
		this.persistence = persistence;
	}

	public Lap getFastestLapOnServer(Track track, Car car) {

		/*
		 * SELECT lap. FROM racecontrol_lap as lap, racecontrol_driver AS driver WHERE driver.id = lap.driver_id AND driver.name = 'Brilwing' AND lap.time = ( SELECT min(lap.time) from racecontrol_driver
		 * AS driver, racecontrol_lap AS lap, racecontrol_race_entry AS entry WHERE driver.id = lap.driver_id AND driver.race_entry_id = entry.id AND entry.track = 'KY2R' AND driver.name = 'Brilwing'
		 * ORDER BY entry.start_time )
		 */

		StringBuilder query = new StringBuilder();
		query.append("SELECT lap.* FROM racecontrol_lap as lap, racecontrol_driver AS driver");
		query.append(" WHERE driver.id = lap.driver_id");
		query.append(" AND driver.car_name = '");
		query.append(car.toString());
		query.append("'");
		query.append(" AND lap.time = (");
		query.append("SELECT min(lap.time) from racecontrol_driver AS driver, racecontrol_lap AS lap, racecontrol_race_entry AS entry");
		query.append(" WHERE driver.id = lap.driver_id ");
		query.append(" AND driver.race_entry_id = entry.id");
		query.append(" AND entry.track = '");
		query.append(track.getShortname());
		query.append("'");
		query.append(" ORDER BY entry.start_time)");

		if (log.isDebugEnabled()) {
			log.debug("Query: " + query);
		}

		try {
			List<Lap> laps = (List<Lap>) persistence.queryNative(query.toString(), Lap.class);
			if (!laps.isEmpty()) {
				return (Lap) laps.get(0);
			}
		} catch (PersistenceException e) {
			log.error("Cannot perform query!", e);
		}
		return null;
	}

	public Lap getFastestLapOnServerForDriver(Track track, Driver driver) {

		/*
		 * SELECT lap. FROM racecontrol_lap as lap, racecontrol_driver AS driver WHERE driver.id = lap.driver_id AND driver.name = 'Brilwing' AND driver.car_name = '' AND lap.time = ( SELECT min(lap.time)
		 * from racecontrol_driver AS driver, racecontrol_lap AS lap, racecontrol_race_entry AS entry WHERE driver.id = lap.driver_id AND driver.race_entry_id = entry.id AND entry.track = 'KY2R' AND
		 * driver.name = 'Brilwing' AND driver.car_name = '' ORDER BY entry.start_time )
		 */
		StringBuilder query = new StringBuilder();
		query.append("SELECT lap.* FROM racecontrol_lap as lap, racecontrol_driver AS driver");
		query.append(" WHERE driver.id = lap.driver_id");
		query.append(" AND driver.name = '");
		query.append(driver.getName());
		query.append("'");
		query.append(" AND driver.car_name = '");
		query.append(driver.getCarName());
		query.append("'");
		query.append(" AND lap.time = (");
		query.append("SELECT min(lap.time) from racecontrol_driver AS driver, racecontrol_lap AS lap, racecontrol_race_entry AS entry");
		query.append(" WHERE driver.id = lap.driver_id ");
		query.append(" AND driver.race_entry_id = entry.id");
		query.append(" AND entry.track = '");
		query.append(track.getShortname());
		query.append("'");
		query.append(" AND driver.name = '");
		query.append(driver.getName());
		query.append("'");
		query.append(" AND driver.car_name = '");
		query.append(driver.getCarName());
		query.append("'");
		query.append(" ORDER BY entry.start_time)");

		if (log.isDebugEnabled()) {
			log.debug("Query: " + query);
		}

		try {
			List<Lap> laps = (List<Lap>) persistence.queryNative(query.toString(), Lap.class);
			if (!laps.isEmpty()) {
				return (Lap) laps.get(0);
			}
		} catch (PersistenceException e) {
			log.error("Cannot perform query!", e);
		}
		return null;
	}

	public Lap getFastestPossibleLapOnServer(Track track, Driver driver) {

		StringBuilder query = new StringBuilder();
		query.append("SELECT min(lap.time) AS time");
		for (int i = 1; i <= track.getSplits(); i++) {
			query.append(" min(lap.split");
			query.append(i);
			query.append(") as split");
			query.append(i);
		}
		query.append(" FROM racecontrol_driver AS driver, racecontrol_lap AS lap, racecontrol_race_entry AS entry");
		query.append(" WHERE driver.id = lap.driver_id  AND driver.race_entry_id = entry.id AND entry.track = '");
		query.append(track.getShortname());
		query.append("' AND driver.name = '");
		query.append(driver.getName());
		query.append("'");
		query.append(" AND driver.car_name = '");
		query.append(driver.getCarName());
		query.append("'");
		for (int i = 1; i <= track.getSplits(); i++) {
			query.append(" AND lap.split");
			query.append(i);
			query.append(" > 0 ");
		}
		if (log.isDebugEnabled()) {
			log.debug("Query: " + query);
		}
		/*
		 * try { List<Lap> laps = (List<Lap>)persistence.queryNative(query.toString(), Lap.class); if (!laps.isEmpty()) { return (Lap)laps.get(0); } } catch (PersistenceException e) { log.error("Cannot
		 * perform query!", e); }
		 */
		return null;
	}

	public List<Lap> getFastestsLaps(List<Car> cars, Track track, List<Profile> profiles) throws PersistenceException {
		if (profiles.size() == 0) {
			return Collections.emptyList();
		}

		StringBuilder query = new StringBuilder();

		// SELECT driver.*, lap.* FROM racecontrol_lap lap
		// INNER JOIN racecontrol_driver AS driver ON lap.driver_id = driver.id
		// INNER JOIN (
		// SELECT driver_inner.name as name, min(lap_inner.time) as time
		// FROM racecontrol_lap as lap_inner
		// INNER JOIN racecontrol_driver as driver_inner ON lap_inner.driver_id = driver_inner.id
		// INNER JOIN racecontrol_race_entry as race_entry_inner ON race_entry_inner.id = driver_inner.race_entry_id
		// WHERE driver_inner.car_name = 'BF1'
		// AND race_entry_inner.track = 'KY2R'
		// GROUP BY driver_inner.name) as fastest_lap ON lap.time = fastest_lap.time AND driver.name = fastest_lap.name
		// INNER JOIN racecontrol_race_entry as entry ON entry.id = driver.race_entry_id
		// WHERE entry.track = 'KY2R' AND driver.car_name IN ('BF1')
		// AND lower(driver.name) IN ('brilwing', 'mensafest', 'chirpydriver', 'schrauberherz', 'bberger', 'severin kapplmüller', 'isehwurscht', 'gpmp', 'nob2', 'gunger40', 'climbs', 'theblacklion',
		// 'bbman', 'jatino', 'masterlooser15', 're-offender', 'naviracer', 'huhu_', 'masahara', 'paracelsus', 'chrizu')

		query.append("SELECT driver.*, lap.* ");
		query.append("FROM racecontrol_lap lap ");
		query.append("INNER JOIN racecontrol_driver AS driver ON lap.driver_id = driver.id ");
		query.append("INNER JOIN ( ");
		query.append("	SELECT driver_inner.name as name, min(lap_inner.time) as time ");
		query.append("	FROM racecontrol_lap as lap_inner ");
		query.append("	INNER JOIN racecontrol_driver as driver_inner ON lap_inner.driver_id = driver_inner.id ");
		query.append("  INNER JOIN racecontrol_race_entry as race_entry_inner ON race_entry_inner.id = driver_inner.race_entry_id ");
		query.append(" WHERE driver_inner.car_name IN (");
		boolean firstCar = true;
		for (Car car : cars) {
			if (!firstCar) {
				query.append(", ");
			} else {
				firstCar = false;
			}
			query.append("'");
			query.append(car.toString());
			query.append("'");
		}
		query.append(") AND race_entry_inner.track = '");
		query.append(track.getShortname());
		query.append("'	GROUP BY driver_inner.name) as fastest_lap ");
		query.append("ON lap.time = fastest_lap.time AND driver.name = fastest_lap.name ");
		query.append("INNER JOIN racecontrol_race_entry as entry ON entry.id = driver.race_entry_id ");
		query.append("WHERE entry.track = '");
		query.append(track.getShortname());
		query.append("' AND driver.car_name IN (");
		firstCar = true;
		for (Car car : cars) {
			if (!firstCar) {
				query.append(", ");
			} else {
				firstCar = false;
			}
			query.append("'");
			query.append(car.toString());
			query.append("'");
		}
		query.append(") AND lower(driver.name) IN (");
		boolean first = true;
		for (Profile profile : profiles) {
			if (profile.getLastname() != null) {
				if (!first) {
					query.append(", ");
				} else {
					first = false;
				}
				query.append("'");
				query.append(profile.getLfsworldName().toLowerCase());
				query.append("'");
			}
		}
		query.append(") ORDER BY driver.name");

		log.debug("query: {}", query.toString());
		List<Object[]> queryResult = (List<Object[]>) persistence.queryNative(query.toString(), "fastestLap");

		List<Lap> lapList = new ArrayList<Lap>(queryResult.size());

		Driver previous = null;
		for (Object[] entry : queryResult) {
			Driver driver = (Driver) entry[0];
			if (previous != null && previous.getName().equalsIgnoreCase(driver.getName())) {
				// if a driver has the same time driven multiple times then skip this
				// time
				continue;
			} else {
				previous = driver;
			}
			Lap lap = (Lap) entry[1];
			lap.setDriver(driver);

			log.info("Lap {}", lap);

			lapList.add(lap);
		}


		Collections.sort(lapList, new LapComparator());
		return lapList;
	}
}
