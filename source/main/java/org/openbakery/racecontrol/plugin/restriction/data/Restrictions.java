package org.openbakery.racecontrol.plugin.restriction.data;

public class Restrictions {

	private int intake;

	private int mass;

	public Restrictions() {
	}

	public Restrictions(Restrictions restrictions) {
		this.intake = restrictions.intake;
		this.mass = restrictions.mass;
	}

	public int getIntake() {
		return intake;
	}

	public void setIntake(int intake) {
		this.intake = intake;
	}

	public int getMass() {
		return mass;
	}

	public void setMass(int mass) {
		this.mass = mass;
	}
}
