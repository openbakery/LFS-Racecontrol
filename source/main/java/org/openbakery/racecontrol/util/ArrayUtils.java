package org.openbakery.racecontrol.util;

public class ArrayUtils {
	
	public static byte[] copy(byte[] source, int start, int end, int destinationLength) {
		byte[] destination = new byte[destinationLength];
		System.out.println("copy...");
		for(int i = start; i<end && (i-start)<(destinationLength); i++) {
			System.out.println("destination[" + (i-start) + "] = " + source[i]);
			destination[i-start] = source[i];
		}
		return destination;
	}

}
