package org.openbakery.racecontrol.gui;


import org.openbakery.racecontrol.gui.ButtonIdHelper;
import org.testng.annotations.ExpectedExceptions;
import org.testng.annotations.Test;

public class ButtonIdHelperTest {

	@Test
	public void testIdHelper() {
		ButtonIdHelper helper = ButtonIdHelper.getInstance();
		for (int i=50; i<239; i++) {
			helper.popId(Integer.valueOf(1));
		}
		for (int i=50; i<239; i++) {
			helper.popId(Integer.valueOf(2));
		}
		for (int i=50; i<239; i++) {
			helper.pushId(Integer.valueOf(2), (byte)i);
		}
		for (int i=50; i<239; i++) {
			helper.popId(Integer.valueOf(2));
		}
	}
	
	@Test
	@ExpectedExceptions ({ java.util.EmptyStackException.class })
	public void testIdHelperFailed() {
		ButtonIdHelper helper = ButtonIdHelper.getInstance();
		for (int i=0; i<239; i++) {
			helper.popId(Integer.valueOf(2));
		}
	}
	
}


