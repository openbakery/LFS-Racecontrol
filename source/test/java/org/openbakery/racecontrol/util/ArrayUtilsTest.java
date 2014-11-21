package org.openbakery.racecontrol.util;

import java.util.Arrays;

import org.openbakery.racecontrol.util.ArrayUtils;
import org.testng.annotations.Test;



public class ArrayUtilsTest {

	@Test
	public void test() {
		byte[] source = new byte[] { (byte)1, (byte)2, (byte)3, (byte)4 };
		byte[] test = new byte[] { (byte)2 };
		
		byte[] destination = ArrayUtils.copy(source, 1, 2, 1);
		
		assert Arrays.equals(destination, test);
		
		test = new byte[] { (byte)2, (byte)0, (byte)0 };
		destination = ArrayUtils.copy(source, 1, 2, 3);
		assert Arrays.equals(destination, test);
		
		test = new byte[] { (byte)2, (byte)3, (byte)4 };
		destination = ArrayUtils.copy(source, 1, 5, 3);
		assert Arrays.equals(destination, test);
	}
}
