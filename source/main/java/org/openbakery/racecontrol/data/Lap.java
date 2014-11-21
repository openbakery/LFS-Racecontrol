/*
 * Created on Feb 4, 2006
 *
 */
package org.openbakery.racecontrol.data;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

import org.openbakery.racecontrol.persistence.FilePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "racecontrol_lap")
public class Lap implements Comparable<Lap>, Cloneable, Serializable {

    private static Logger log = LoggerFactory.getLogger(Lap.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 4789495926121897235L;

	@Id
	@GeneratedValue
	private int id;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "driver_id")
	private Driver driver;

	@ManyToOne
	@JoinColumn(name = "performed_by_driver_id")
	private Driver performedByDriver;

	@Column(name = "split1")
	private int split1;

	@Column(name = "split2")
	private int split2;

	@Column(name = "split3")
	private int split3;

	@Column(name = "split4")
	private int split4;

	private int time;

	private int number;

	private boolean pit;

	@Column(name = "driver_position")
	private int position;

	@Column(name = "total_time")
	private int totalTime;

	private boolean finished;

	private int penalty = -1;

	@Column(name = "number_stops")
	private int numberStops = -1;

	@Embedded
	@AttributeOverrides( { @AttributeOverride(name = "rearLeft", column = @Column(name = "tyre_rear_left")), @AttributeOverride(name = "rearRight", column = @Column(name = "tyre_rear_right")),
			@AttributeOverride(name = "frontLeft", column = @Column(name = "tyre_front_left")), @AttributeOverride(name = "frontRight", column = @Column(name = "tyre_front_right")) })
	private Tyres tyres = new Tyres();

	private int work = -1;

	@Column(name = "pit_stop_time")
	private long pitStopTime = -1L;

	@Column(name = "old_penalty")
	private int oldPenalty;

	@Column(name = "new_penalty")
	private int newPenalty;

	@Transient
	private LinkedList<Flag> flags = new LinkedList<Flag>();

	private int attempt;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Lap() {
		this(1, 0);
	}

	public Lap(int attempt, int number) {
		this.attempt = attempt;
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isPit() {
		return pit;
	}

	public void setPit(boolean pit) {
		this.pit = pit;
	}

	public int getSplit(int i) {
		switch (i) {
		case 0:
			return split1;
		case 1:
			return split2;
		case 2:
			return split3;
		case 3:
			return split4;
		}
		return 0;
	}

	public int getTime() {
		return time;
	}

	public boolean isFinished() {
		return totalTime > 0;
	}

	public void addSplit(int split, int time) {
		int sumSplits = 0;
		if (split > 1) {
			sumSplits = split1 + split2 + split3 + split4;
		}
		setSplitTime(split, time - sumSplits);
	}

	private void setSplitTime(int split, int time) {
		switch (split) {
		case 1:
			split1 = time;
			break;
		case 2:
			split2 = time;
			break;
		case 3:
			split3 = time;
			break;
		case 4:
			split4 = time;
			break;
		}
	}

	public String toString() {
		String result = "Lap [number: " + number + ", time: " + getTime();
		result += ", split1: " + split1;
		result += ", split2: " + split2;
		result += ", split3: " + split3;
		result += ", split4: " + split4;
		result += ", pit: " + pit + "]";
		return result;
	}

	public String toString(String lfsWorldName) {
		String result = null;
		if (lfsWorldName != null) {
			result = lfsWorldName;
			result += FilePersistence.DELIMITER;
			result += Integer.toString(number);
			result += FilePersistence.DELIMITER;
			result += time;
			result += FilePersistence.DELIMITER;
			result += Integer.toString(split1);
			result += FilePersistence.DELIMITER;
			result += Integer.toString(split2);
			result += FilePersistence.DELIMITER;
			result += Integer.toString(split3);
			result += FilePersistence.DELIMITER;
			result += Integer.toString(split4);
			result += FilePersistence.DELIMITER;
			result += totalTime;
			result += FilePersistence.DELIMITER;
			result += Integer.toString(position);
			result += FilePersistence.DELIMITER;
			result += Boolean.toString(pit);
			result += FilePersistence.DELIMITER;
			if (penalty > -1) {
				result += Integer.toString(penalty);
			}
			result += FilePersistence.DELIMITER;
			if (numberStops > -1) {
				result += Integer.toString(numberStops);
			}
			result += FilePersistence.DELIMITER;
			if (tyres != null) {
				result += Integer.toString(tyres.getRearLeft());
				result += FilePersistence.DELIMITER;
				result += Integer.toString(tyres.getRearRight());
				result += FilePersistence.DELIMITER;
				result += Integer.toString(tyres.getFrontLeft());
				result += FilePersistence.DELIMITER;
				result += Integer.toString(tyres.getFrontRight());
			} else {
				result += FilePersistence.DELIMITER;
				result += FilePersistence.DELIMITER;
				result += FilePersistence.DELIMITER;
			}
			result += FilePersistence.DELIMITER;
			if (work > -1) {
				result += Integer.toString(work);
			}
			result += FilePersistence.DELIMITER;
			if (pitStopTime > -1) {
				result += Long.toString(pitStopTime);
			}
			result += FilePersistence.DELIMITER;
			if (performedByDriver != null) {
				result += performedByDriver.getName();
			}
			result += FilePersistence.DELIMITER;
			if (oldPenalty > -1) {
				result += Integer.toString(oldPenalty);
			}
			result += FilePersistence.DELIMITER;
			if (newPenalty > -1) {
				result += Integer.toString(newPenalty);
			}
		}
		return result;
	}

	public void store(String lfsWorldName, Writer writer) throws IOException {
        log.debug("lfsWorldName {}, isFinished {}, position {}", new Object[] {lfsWorldName, isFinished(), position});
		if (lfsWorldName != null && isFinished() && position > 0) {
			writer.write(toString(lfsWorldName));
			writer.write("\n");
		}
	}

	public static void storeHeader(Writer writer) throws IOException {
		writer.write("RACECONTROL-SECTION: LAPS\n");
		writer.write("#number" + FilePersistence.DELIMITER + "time" + FilePersistence.DELIMITER + "split1" + FilePersistence.DELIMITER + "split2" + FilePersistence.DELIMITER + "split3"
				+ FilePersistence.DELIMITER + "split4" + FilePersistence.DELIMITER + "totalTime" + FilePersistence.DELIMITER + "position" + FilePersistence.DELIMITER + "pit" + FilePersistence.DELIMITER
				+ "penalty" + FilePersistence.DELIMITER + "numberStops" + FilePersistence.DELIMITER + "rearLeft" + FilePersistence.DELIMITER + "rearRight" + FilePersistence.DELIMITER + "frontLeft"
				+ FilePersistence.DELIMITER + "frontRight" + FilePersistence.DELIMITER + "work" + FilePersistence.DELIMITER + "pitStopTime" + FilePersistence.DELIMITER + "performedByDriver"
				+ FilePersistence.DELIMITER + "oldPenalty" + FilePersistence.DELIMITER + "newPenalty\n");
	}

	public int compareTo(Lap lap) {
		return (totalTime - lap.getTotalTime());
	}

	public Lap clone() {
		Lap lap = new Lap(attempt, number);
		lap.split1 = split1;
		lap.split2 = split2;
		lap.split3 = split3;
		lap.split4 = split4;
		lap.pit = pit;
		lap.position = position;
		lap.totalTime = totalTime;
		lap.penalty = penalty;
		lap.numberStops = numberStops;
		lap.tyres = tyres.clone();
		lap.work = work;
		lap.pitStopTime = -pitStopTime;
		lap.performedByDriver = performedByDriver;
		lap.oldPenalty = oldPenalty;
		lap.newPenalty = newPenalty;
		return lap;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public int getNumberStops() {
		return numberStops;
	}

	public void setNumberStops(int numberStops) {
		this.numberStops = numberStops;
	}

	public int getPenalty() {
		return penalty;
	}

	public void setPenalty(int penalty) {
		this.penalty = penalty;
	}

	public int getWork() {
		return work;
	}

	public void setWork(int work) {
		this.work = work;
	}

	public long getPitStopTime() {
		return pitStopTime;
	}

	public void setPitStopTime(long pitStopTime) {
		this.pitStopTime = pitStopTime;
	}

	public Flag getCurrentFlag() {
		if (flags.size() > 0) {
			return flags.getLast();
		}
		return null;
	}

	public void addFlag(Flag flag) {
		flags.add(flag);
	}

	public List<Flag> getFlags() {
		return flags;
	}

	public int getNewPenalty() {
		return newPenalty;
	}

	public void setNewPenalty(int newPenalty) {
		this.newPenalty = newPenalty;
	}

	public int getOldPenalty() {
		return oldPenalty;
	}

	public void setOldPenalty(int oldPenalty) {
		this.oldPenalty = oldPenalty;
	}

	public void setTime(int time) {
		this.time = time;
		int sumSplits = split1 + split2 + split3 + split4;
		if (split2 == 0) {
			split2 = time - sumSplits;
		} else if (split3 == 0) {
			split3 = time - sumSplits;
		} else if (split4 == 0) {
			split4 = time - sumSplits;
		}
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	public Tyres getTyres() {
		return tyres;
	}

	public void setTyres(Tyres tyres) {
		this.tyres = tyres;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAttempt() {
		return attempt;
	}

	public void setAttempt(int joins) {
		this.attempt = joins;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + attempt;
		result = prime * result + ((driver == null) ? 0 : driver.hashCode());
		result = prime * result + (finished ? 1231 : 1237);
		result = prime * result + ((flags == null) ? 0 : flags.hashCode());
		result = prime * result + id;
		result = prime * result + newPenalty;
		result = prime * result + number;
		result = prime * result + numberStops;
		result = prime * result + oldPenalty;
		result = prime * result + penalty;
		result = prime * result + (pit ? 1231 : 1237);
		result = prime * result + (int) (pitStopTime ^ (pitStopTime >>> 32));
		result = prime * result + position;
		result = prime * result + split1;
		result = prime * result + split2;
		result = prime * result + split3;
		result = prime * result + split4;
		result = prime * result + time;
		result = prime * result + totalTime;
		result = prime * result + ((tyres == null) ? 0 : tyres.hashCode());
		result = prime * result + work;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Lap other = (Lap) obj;
		if (attempt != other.attempt)
			return false;
		if (driver == null) {
			if (other.driver != null)
				return false;
		} else if (!driver.equals(other.driver))
			return false;
		if (finished != other.finished)
			return false;
		if (flags == null) {
			if (other.flags != null)
				return false;
		} else if (!flags.equals(other.flags))
			return false;
		if (id != other.id)
			return false;
		if (newPenalty != other.newPenalty)
			return false;
		if (number != other.number)
			return false;
		if (numberStops != other.numberStops)
			return false;
		if (oldPenalty != other.oldPenalty)
			return false;
		if (penalty != other.penalty)
			return false;
		if (pit != other.pit)
			return false;
		if (pitStopTime != other.pitStopTime)
			return false;
		if (position != other.position)
			return false;
		if (split1 != other.split1)
			return false;
		if (split2 != other.split2)
			return false;
		if (split3 != other.split3)
			return false;
		if (split4 != other.split4)
			return false;
		if (time != other.time)
			return false;
		if (totalTime != other.totalTime)
			return false;
		if (tyres == null) {
			if (other.tyres != null)
				return false;
		} else if (!tyres.equals(other.tyres))
			return false;
		if (work != other.work)
			return false;
		return true;
	}

	public boolean isEmpty() {
		return (split1 == 0 && time == 0);
	}

	public Driver getPerformedByDriver() {
		return performedByDriver;
	}

	public void setPerformedByDriver(Driver performedByDriver) {
		this.performedByDriver = performedByDriver;
	}

}
