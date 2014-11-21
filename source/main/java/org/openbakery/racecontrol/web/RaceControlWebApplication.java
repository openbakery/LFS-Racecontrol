package org.openbakery.racecontrol.web;

import org.apache.wicket.Page;
import org.apache.wicket.request.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Response;
import org.openbakery.racecontrol.service.SettingsLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RaceControlWebApplication extends WebApplication {

	private ApplicationContext context;

	private static Logger log = LoggerFactory.getLogger(RaceControlWebApplication.class);

	@Override
	public Class<? extends Page> getHomePage() {
		return LoginPage.class;
	}

	@Override
	protected void init() {
		super.init();

		// context = new GenericApplicationContext();
		// XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(context);
		// xmlReader.loadBeanDefinitions(new ClassPathResource("spring-config.xml"));

		String dir = "spring-config.xml";
		context = new ClassPathXmlApplicationContext(new String[] { dir });

		SettingsLoader loader = (SettingsLoader) context.getBean("settingsLoader");
		loader.setBasePath(getServletContext().getRealPath("WEB-INF"));
		log.debug("WEB-INF path {}", getServletContext().getRealPath("WEB-INF"));

		log.info("Init is called...");
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new RaceControlSession(request);
	}

	public ApplicationContext getContext() {
		return context;
	}

}
