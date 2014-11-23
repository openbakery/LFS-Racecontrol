package org.openbakery.racecontrol.plugin;

import java.util.List;

import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.openbakery.racecontrol.plugin.result.ResultDataAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ResultDataAccessTest {

	private static Logger logger = LoggerFactory.getLogger(ResultDataAccessTest.class);

	private ApplicationContext context;

	@Test
	public void testResult() throws PersistenceException {
		ResultDataAccess resultDataAccess = (ResultDataAccess) context.getBean("resultDataAccess");
		List<Driver> result = resultDataAccess.getResult(713);
		logger.debug("Drivers: {}", result);
	}

	@BeforeClass
	public void initSpring() {
		String dir = "spring-config.xml";
		context = new ClassPathXmlApplicationContext(new String[] { dir });

	}
}
