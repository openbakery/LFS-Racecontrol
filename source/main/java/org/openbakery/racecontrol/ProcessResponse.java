package org.openbakery.racecontrol;

import org.openbakery.jinsim.response.InSimResponse;


public interface ProcessResponse<T extends InSimResponse> {

	public void process(T response, Race race);
}
