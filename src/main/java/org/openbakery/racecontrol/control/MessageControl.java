package org.openbakery.racecontrol.control;

import net.sf.jinsim.response.InSimResponse;
import net.sf.jinsim.response.MessageResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openbakery.racecontrol.RaceControl;
import org.openbakery.racecontrol.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageControl extends AbstractControl {
	
  private static Logger log = LoggerFactory.getLogger(MessageControl.class);

	public MessageControl(RaceControl raceControl, Persistence persistence) {
		super(raceControl, persistence);
	}

	public void packetReceived(InSimResponse response) {
		if (response instanceof MessageResponse) {
    	processMessageResponse((MessageResponse)response); 
    }
	}
	
	

	private void processMessageResponse(MessageResponse response) {
		log.debug(response.getMessage());
		raceControl.getRace().addMessage(response.getMessage());
		log.info("Message: " + response.getMessage());
	}

	@Override
	public void destroy() {
	}

}
