/*
 * Created on Feb 4, 2006
 *
 */
package org.openbakery.racecontrol.data;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.openbakery.jinsim.response.NewPlayerResponse;

import org.openbakery.racecontrol.persistence.FilePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "racecontrol_driver")
public class Driver implements Cloneable, Serializable {

    private static Logger log = LoggerFactory.getLogger(Driver.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 7254452113176832760L;

	private static final Pattern PATTERN_REMOVE_COLOR_INFO = Pattern.compile("\\^\\d");

	@Id
	@GeneratedValue
	private int id;

	/*
	 * @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	 * 
	 * @JoinColumn(name="race_entry_id") private RaceEntry raceEntry;
	 */

	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "result_id")
	private Result result;

	@Column(name = "name")
	private String name;

	@Transient
	private String playerNameUncolored;

	@Column(name = "player_name")
	private String playerName;

	@Column(name = "car_name")
	private String carName;

	@Column(name = "number")
	private int number;

	@Transient
	private int connectionId;

	@Column(name = "starting_position")
	private int startingPosition;

	@Column(name = "top_speed")
	private double topSpeed;

	@Column(name = "is_admin")
	private boolean isAdmin;

	@Transient
	private int playerId;

	@Transient
	private LinkedList<Lap> allLaps;

	@OneToMany(mappedBy = "driver", fetch=FetchType.EAGER)
	private List<Lap> completedLaps;

	@Transient
	private int playerType;

	@Column(name = "flags")
	private int flags;

	@Column(name = "number_plate")
	private String numberPlate;

	@Column(name = "skin_name")
	private String skinName;

	@Embedded
	@AttributeOverrides( { @AttributeOverride(name = "rearLeft", column = @Column(name = "tyre_rear_left")), @AttributeOverride(name = "rearRight", column = @Column(name = "tyre_rear_right")),
			@AttributeOverride(name = "frontLeft", column = @Column(name = "tyre_front_left")), @AttributeOverride(name = "frontRight", column = @Column(name = "tyre_front_right")) })
	private Tyres tyres = new Tyres();

	@Column(name = "added_mass")
	private int addedMass;

	@Column(name = "intake_restriction")
	private int intakeRestriction;

	@Column(name = "passengers")
	private int passengers;

    @Column(name = "joins")
	private int joins;

	@Transient
	private Driver currentDriver;

	public Driver getCurrentDriver() {
		return currentDriver;
	}

	public void setCurrentDriver(Driver currentDriver) {
		this.currentDriver = currentDriver;
	}

	public Driver() {
		this(-1);
	}

	public Driver(int connectionId) {
		setConnectionId(connectionId);
		resetRaceData();
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		if (name == null) {
			return "";
		}
		return name;
	}

	public Lap getCurrentLap() {
		Lap lap = null;
		if (allLaps.size() > 0) {
			lap = allLaps.getLast();
			if (!lap.isFinished()) {
				return lap;
			}
		}
		lap = new Lap(joins, allLaps.size() + 1);
		allLaps.add(lap);
		if (currentDriver != null) {
			lap.setPerformedByDriver(currentDriver);
		} else {
			lap.setPerformedByDriver(this);
		}
		lap.setDriver(this);

		return lap;
	}

	public void newLap() {
		/*
		 * int completed = 0; for (Lap lap : allLaps) { if (lap.isFinished()) completed++; } allLaps.add(new Lap(joins, completed+1));
		 */

		Lap lap = getCurrentLap();
		if (lap.isEmpty()) {
			return;
		}
		allLaps.add(new Lap(joins, allLaps.size() + 1));
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String nickname) {
		this.playerName = nickname;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int playerNum) {
		this.number = playerNum;
	}

	public int getConnectionId() {
		return connectionId;
	}

	public void 	setConnectionId(int id) {
		log.debug("--------------> set connection id to: {}", id);
		this.connectionId = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlayerNameUncolored() {
		if (playerNameUncolored == null) {
			Matcher matcher = PATTERN_REMOVE_COLOR_INFO.matcher(playerName);
			playerNameUncolored = matcher.replaceAll("");
		}
		return playerNameUncolored;
	}

	public String toCsvString() {
		String result = "";
		if (name != null) {
			result += name;
			result += FilePersistence.DELIMITER;
			result += playerName;
			result += FilePersistence.DELIMITER;
			result += carName;
			result += FilePersistence.DELIMITER;
			result += Integer.toString(startingPosition);
			result += FilePersistence.DELIMITER;
			result += numberPlate;
		}
		return result;
	}

	public String toString() {
		return "Driver [playerId=" + playerId + ", connectionId=" + connectionId + ", name=" + name + ", playerName=" + playerName + "]";
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public List<Lap> getAllLaps() {
		return allLaps;
	}

	public Lap getCompletedLap(int position) {
		if (position < completedLaps.size() && position >= 0) {
			return completedLaps.get(position);
		}
		return null;
	}

	public int getCompletedLapCount() {
		return completedLaps.size();
	}

	public void store(Writer writer) throws IOException {
		if (name != null) {
			writer.write(toCsvString());
			writer.write("\n");
		}
	}

	public static void storeHeader(Writer writer) throws IOException {
		writer.write("RACECONTROL-SECTION: DRIVER\n");
		writer.write("#LFSWorldName" + FilePersistence.DELIMITER + "Nickname" + FilePersistence.DELIMITER + "CarName" + FilePersistence.DELIMITER + "startingPosition" + FilePersistence.DELIMITER
				+ "Plate\n");
	}

	public int getStartingPosition() {
		return startingPosition;
	}

	public void setStartingPosition(int startingPosition) {
		this.startingPosition = startingPosition;
	}

	public void addCompletedLap(Lap lap) {
		/*
		 * if (!hasRejoined() && result == null) { completedLaps.add(lap); log.debug("added completed lap: " + lap); }
		 */
		if (result == null) {
			completedLaps.add(lap);
		}
	}

	public boolean hasRejoined() {
		return joins > 0;
	}

	public void addJoin() {
		joins++;
		getCurrentLap().setAttempt(joins);
	}

	public List<Lap> getCompletedLaps(int attempt) {
		LinkedList<Lap> laps = new LinkedList<Lap>();
		for (Lap lap : completedLaps) {
			if (lap.getAttempt() == attempt) {
				laps.add(lap);
			}
		}
		return laps;
	}

	public List<Lap> getCompletedLaps() {
		return getCompletedLaps(0);
	}

	public double getTopSpeed() {
		return topSpeed;
	}

	public void setTopSpeed(double topSpeed) {
		this.topSpeed = topSpeed;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public void setPlayerType(int playerType) {
		this.playerType = playerType;
	}

	public void setFlags(int playerFlags) {
		this.flags = playerFlags;

	}

	public void setNumber(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public void setSkinName(String skinName) {
		this.skinName = skinName;
	}

	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public int getPlayerType() {
		return playerType;
	}

	public int getFlags() {
		return flags;
	}

	public String getSkinName() {
		return skinName;
	}

	public void setTyres(Tyres tyres) {
		this.tyres = tyres;

	}

	public void setIntakeRestriction(int intakeRestriction) {
		this.intakeRestriction = intakeRestriction;
	}

	public int getIntakeRestriction() {
		return intakeRestriction;
	}

	public Tyres getTyres() {
		return tyres;
	}

	public void setPlayerNameUncolored(String playerNameUncolored) {
		this.playerNameUncolored = playerNameUncolored;
	}

	public int getPassengers() {
		return passengers;
	}

	public void setPassengers(int passengers) {
		this.passengers = passengers;
	}

	public int getAddedMass() {
		return addedMass;
	}

	public void setAddedMass(int addedMass) {
		this.addedMass = addedMass;
	}

	public void resetRaceData() {
		allLaps = new LinkedList<Lap>();
		completedLaps = new LinkedList<Lap>();
		joins = 0;
		result = null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public Driver clone() throws CloneNotSupportedException {
		Driver driver = new Driver(connectionId);

		driver.name = name;
		driver.playerNameUncolored = playerNameUncolored;
		driver.playerName = playerName;
		driver.carName = carName;
		driver.number = number;
		driver.startingPosition = startingPosition;
		driver.topSpeed = topSpeed;
		driver.isAdmin = isAdmin;
		driver.playerId = playerId;
		driver.playerType = playerType;
		driver.flags = flags;
		driver.numberPlate = numberPlate;
		driver.skinName = skinName;
		driver.tyres = tyres.clone();
		driver.addedMass = addedMass;
		driver.intakeRestriction = intakeRestriction;
		driver.passengers = passengers;
		return driver;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + connectionId;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (object == null) {
			return false;
		}
		if (getClass() != object.getClass()) {
			return false;
		}

		Driver other = (Driver) object;
		if (id != other.id) {
			return false;
		}

		if (!other.name.equals(this.name)) {
			return false;
		}

		return false;
		/*
		if (connectionId != other.connectionId)
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
			*/
	}

	public int getJoins() {
		return joins;
	}

	public int getLongestAttempt() {
		int laps = 0;
		int attempt = 0;
		for (int i = 0; i <= joins; i++) {
            List<Lap> attemptLaps = getCompletedLaps(i);
            log.debug("attemptLaps.size {} ", attemptLaps.size());
			if (laps < attemptLaps.size()) {
				laps = attemptLaps.size();
				attempt = i;
			}
		}
        log.debug("longest attempt: {}", attempt);
		return attempt;
	}

    public List<Lap> getLongestAttemptLaps() {
        List<Lap> longestAttemptLaps = Collections.emptyList();
        for (int i = 0; i <= joins; i++) {
            List<Lap> attemptLaps = getCompletedLaps(i);
            log.debug("attemptLaps.size {} ", attemptLaps.size());
            if (longestAttemptLaps.size() < attemptLaps.size()) {
                longestAttemptLaps = attemptLaps;
            }
        }
        return longestAttemptLaps;
    }


	public boolean hasResult() {
		return result != null;
	}

	public void setData(NewPlayerResponse response) {
		setPlayerId(response.getPlayerId());
		setPlayerType(response.getPlayerType());
		setFlags(response.getPlayerFlags());
		setNumber(response.getNumberPlate());
		setCarName(response.getCar().toString());
		setSkinName(response.getSkinName());

		org.openbakery.jinsim.types.Tyres insimTyres = response.getTyres();
		setTyres(new Tyres(insimTyres.getRearLeft(), insimTyres.getRearRight(), insimTyres.getFrontLeft(), insimTyres.getFrontRight()));
		setAddedMass(response.getAddedMass());
		setIntakeRestriction(response.getIntakeRestriction());
		setPassengers(response.getPassengers());
		setNumberPlate(response.getNumberPlate());
	}

}
