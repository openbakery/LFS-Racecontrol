package org.openbakery.racecontrol.control;

import net.sf.jinsim.response.InSimListener;

import org.openbakery.racecontrol.RaceControl;
import org.openbakery.racecontrol.persistence.Persistence;

public abstract class AbstractControl implements InSimListener {

	protected RaceControl raceControl;
	protected Persistence persistence;
	
	public AbstractControl(RaceControl raceControl, Persistence persistence) {
		this.raceControl = raceControl;
		this.persistence = persistence;
	}
	
	
	/**
	 * the distroy method is call befor race control exits
	 */
	public abstract void destroy();
}
