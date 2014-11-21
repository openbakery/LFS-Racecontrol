package org.openbakery.racecontrol.control;

import net.sf.jinsim.response.ConnectionLeaveResponse;
import net.sf.jinsim.response.InSimResponse;
import net.sf.jinsim.response.NewConnectionResponse;
import net.sf.jinsim.response.NewPlayerResponse;
import net.sf.jinsim.response.TakeOverCarResponse;

import org.apache.commons.lang.StringUtils;
import org.openbakery.racecontrol.DriverNotFoundException;
import org.openbakery.racecontrol.Race;
import org.openbakery.racecontrol.RaceControl;
import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.event.RaceEvent;
import org.openbakery.racecontrol.event.RaceEvent.Type;
import org.openbakery.racecontrol.persistence.Persistence;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerControl extends AbstractControl {

  private static Logger log = LoggerFactory.getLogger(PlayerControl.class);


	public PlayerControl(RaceControl raceControl, Persistence persistence) {
		super(raceControl, persistence);
	}

	public void packetReceived(InSimResponse response) {
		try {
			if (response instanceof NewConnectionResponse) {
				processNewConnectionResponse((NewConnectionResponse) response);
			} else if (response instanceof NewPlayerResponse) {
				processNewPlayerResponse((NewPlayerResponse) response);
			} else if (response instanceof ConnectionLeaveResponse) {
				removeOldDriver(((ConnectionLeaveResponse) response).getConnectionId());
			} else if (response instanceof TakeOverCarResponse) {
				processTakeOverCarResponse((TakeOverCarResponse) response);
			}
		} catch (PersistenceException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void processNewConnectionResponse(NewConnectionResponse response) {
		if (response.getConnectionId() != 0) {
			removeOldDriver(response.getConnectionId());
			Driver driver = raceControl.getRace().getDriver(response.getConnectionId(), response.getUsername());
			log.debug(response.getPlayerName());
			driver.setName(response.getUsername());
			driver.setPlayerName(response.getPlayerName());
			driver.setAdmin(response.isAdmin());
		}
	}

	private void processNewPlayerResponse(NewPlayerResponse response) throws PersistenceException {
		Race race = raceControl.getRace();

		Driver driver = race.getDriver(response.getConnectionId());
		if (!race.hasFinished(driver)) {
			if (StringUtils.isNotEmpty(driver.getCarName()) && !response.getCar().toString().equals(driver.getCarName())) {
				// car has changed so create a new driver entry
				driver.setId(0);

				if (!race.getRaceEntry().isQualifying()) {
					log.debug("driver changed car during the race therefor this does not count: " + driver);
					return;
				}

			}
			driver.setData(response);
			if (driver.getName() == null) {
				log.debug("driver name is missing, therefor racecontrol connected during a race: " + driver);
				return;
			}
			Driver newDriver = persistence.store(driver);
			driver.setId(newDriver.getId());
			driver.newLap();

			// when running a replay the new player response is send after the race
			// has started
			// so also update the drivers at the race entry
			if (race.hasRaceEntry()) {
				boolean driverFound = false;
				for (Driver d : race.getRaceDrivers()) {
					if (d.getPlayerId() == response.getPlayerId() || d.getConnectionId() == response.getConnectionId()) {
						driver.setData(response);
						d.setName(driver.getName());
						d.setPlayerName(driver.getPlayerName());
						d.setAdmin(driver.isAdmin());
						d.setConnectionId(driver.getConnectionId());
						d.newLap();
						driverFound = true;
						break;
					}
				}
				// if a driver joins the race from the pits
				if (!driverFound) {
					driver.newLap();
					race.addRaceDriver(driver);
					persistence.store(race.getRaceEntry());
				}
			}
			raceControl.notifyRaceEventListener(new RaceEvent(Type.NEW_DRIVER, race, driver));
			if (log.isDebugEnabled()) {
				log.debug("new player: " + driver);
			}
		}
	}

	protected void removeOldDriver(int connectionId) {
		if (log.isDebugEnabled()) {
			log.debug("remove driver with connection id: " + connectionId);
		}
		Driver oldDriver = null;
		for (Driver driver : raceControl.getRace().getDrivers()) {
			if (driver.getConnectionId() == connectionId) {
				oldDriver = driver;
				break;
			}
		}
		if (oldDriver != null) {
			if (log.isDebugEnabled()) {
				log.debug("removing driver: " + oldDriver);
			}
			raceControl.getRace().setDriverInactive(oldDriver);
		}
	}

	private void processTakeOverCarResponse(TakeOverCarResponse response) {
		Driver driver;
		Race race = raceControl.getRace();
		try {
			driver = race.getRaceDriver(response);
			if (!race.hasFinished(driver)) {
				Driver newDriver = race.getDriver(response.getNewConnectionId(), "");
				if (driver == newDriver) {
					driver.setCurrentDriver(null);
				} else if (newDriver != null) {
					driver.setCurrentDriver(newDriver);
				}
			}
		} catch (DriverNotFoundException e) {
			log.warn(e.getMessage());
		}
	}

	@Override
	public void destroy() {
	}

}
