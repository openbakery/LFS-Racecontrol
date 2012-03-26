package org.openbakery.racecontrol.gui;

import java.util.HashMap;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ButtonIdHelper {

	private static Logger log = LoggerFactory.getLogger(ButtonIdHelper.class);

	private static ButtonIdHelper instance;

	private HashMap<Integer, Stack<Byte>> stackMap;

	private ButtonIdHelper() {
		stackMap = new HashMap<Integer, Stack<Byte>>();
	}

	public static ButtonIdHelper getInstance() {
		if (instance == null) {
			instance = new ButtonIdHelper();
		}
		return instance;
	}

	public byte popId(int connectionId) {
		synchronized (instance) {
			Stack<Byte> stack = stackMap.get(Integer.valueOf(connectionId));
			if (stack == null) {
				stack = new Stack<Byte>();
				if (connectionId == 255) {
					for (int i = 0; i < 50; i++) {
						stack.push(Byte.valueOf((byte) i));
					}
				} else {
					for (int i = 50; i < 239; i++) {
						stack.push(Byte.valueOf((byte) i));
					}
				}
				stackMap.put(Integer.valueOf(connectionId), stack);
			}
			return stack.pop().byteValue();
		}
	}

	public synchronized void pushId(int connectionId, byte id) {
		Stack<Byte> stack = stackMap.get(Integer.valueOf(connectionId));
		if (stack != null) {
			stack.push(Byte.valueOf(id));
		}
	}

	public void debug() {

		for (Integer connectionId : stackMap.keySet()) {

			Stack<Byte> stack = stackMap.get(connectionId);
			if (connectionId.equals(Integer.valueOf(255))) {
				for (int i = 0; i < 50; i++) {
					if (!stack.contains(Byte.valueOf((byte) i))) {
						log.debug("connectionId {}: in use: {}", connectionId, i);
					}
				}
			} else {
				for (int i = 50; i < 239; i++) {
					if (!stack.contains(Byte.valueOf((byte) i))) {
						log.debug("connectionId {}: in use: {}", connectionId, i);
					}
				}
			}
		}

	}
}
