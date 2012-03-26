package org.openbakery.racecontrol;

import net.sf.jinsim.response.InSimResponse;


public interface ProcessResponse<T extends InSimResponse> {

	public void process(T response, Race race);
}
