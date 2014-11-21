package org.openbakery.racecontrol.event;

import org.openbakery.racecontrol.data.Driver;

public class CameraEvent {
	
	public enum CameraType {
		FOLLOW(0),
		HELICOPTER(1),
		TV(2),
		COCKPIT(3),
		CUSTOM(4),
		MAX(5);
	
		private int insimType;
		CameraType(int insimType) {
			this.insimType = insimType;
		}
		
		public static CameraType getByInSimType(int type) {
			for (CameraType cameraType : CameraType.values()) {
				if (cameraType.insimType == type) {
					return cameraType;
				}
			}
			return MAX;
		}
	
	}
	
	private Driver driver;
	private CameraType type;
	
	public CameraEvent(Driver driver, CameraType type) {
		this.driver = driver;
		this.type = type;
	}

	public Driver getDriver() {
		return driver;
	}

	public CameraType getType() {
		return type;
	}
	
	public String toString() {
		return getClass().getName() + "[type=" + type + ", driver" + driver + "]";
	}

}
