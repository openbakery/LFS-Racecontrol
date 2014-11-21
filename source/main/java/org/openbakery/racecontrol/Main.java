package org.openbakery.racecontrol;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String dir = "spring-config.xml";
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { dir });

		try {
			final RaceControl main = (RaceControl) context.getBean("raceControl");
			main.start();

			String lock = "";
			synchronized (lock) {
				lock.wait();
			}

		} catch (IllegalArgumentException ex) {
			System.out.println("usage: org.openbakery.racecontrol.Main <hostname> <port> <admin password>");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
