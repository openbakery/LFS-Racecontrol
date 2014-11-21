package org.openbakery.racecontrol.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Tyres implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6283985115252332017L;

	public static final int TYRE_R1 = 0;

	public static final int TYRE_R2 = 1;

	public static final int TYRE_R3 = 2;

	public static final int TYRE_R4 = 3;

	public static final int TYRE_ROAD_SUPER = 4;

	public static final int TYRE_ROAD_NORMAL = 5;

	public static final int TYRE_HYBRID = 6;

	public static final int TYRE_KNOBBLY = 7;

	public static final int NOT_CHANGED = 255;

	@Column(name = "rearLeft")
	int rearLeft;

	@Column(name = "rearRight")
	int rearRight;

	@Column(name = "frontLeft")
	int frontLeft;

	@Column(name = "frontRight")
	int frontRight;

	public Tyres() {
		rearLeft = NOT_CHANGED;
		rearRight = NOT_CHANGED;
		frontLeft = NOT_CHANGED;
		frontRight = NOT_CHANGED;
	}

	public Tyres(int rearLeft, int rearRight, int frontLeft, int frontRight) {
		this.rearLeft = rearLeft;
		this.rearRight = rearRight;
		this.frontLeft = frontLeft;
		this.frontRight = frontRight;
	}

	public int getFrontLeft() {
		return frontLeft;
	}

	public void setFrontLeft(int tyreFrontLeft) {
		this.frontLeft = tyreFrontLeft;
	}

	public int getFrontRight() {
		return frontRight;
	}

	public void setFrontRight(int tyreFrontRight) {
		this.frontRight = tyreFrontRight;
	}

	public int getRearLeft() {
		return rearLeft;
	}

	public void setRearLeft(int tyreRearLeft) {
		this.rearLeft = tyreRearLeft;
	}

	public int getRearRight() {
		return rearRight;
	}

	public void setRearRight(int tyreRearRight) {
		this.rearRight = tyreRearRight;
	}

	public String toString() {
		return "Tyres [rearLeft=" + rearLeft + ", rearRight=" + rearRight + ", frontLeft=" + frontLeft + ", frontRight=" + frontRight
				+ "]";
	}

	public Tyres clone() {
		return new Tyres(rearLeft, rearRight, frontLeft, frontRight);
	}

}
