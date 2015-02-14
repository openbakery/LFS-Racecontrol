package org.openbakery.racecontrol.web;

import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Response;
import org.openbakery.racecontrol.plugin.profile.web.DriverProfilePage;
import org.openbakery.racecontrol.plugin.profile.web.TeamProfileOverviewPage;
import org.openbakery.racecontrol.plugin.tracker.Tracker;
import org.openbakery.racecontrol.plugin.tracker.web.TrackerPage;
import org.openbakery.racecontrol.plugin.tracker.web.TrackerSettingsPage;
import org.openbakery.racecontrol.service.SettingsLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RaceControlWebApplication extends AuthenticatedWebApplication {

	private ApplicationContext context;

	private static Logger log = LoggerFactory.getLogger(RaceControlWebApplication.class);

	@Override
	public Class<? extends Page> getHomePage() {
		return TrackerPage.class;
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


		mountPage("/tracker", TrackerPage.class);

		String adminPath = "/admin";
		mountPage(adminPath + "/login", LoginPage.class);
		mountPage(adminPath + "/logout", LogoutPage.class);
		mountPage(adminPath + "/driver", DriverProfilePage.class);
		mountPage(adminPath + "/team", TeamProfileOverviewPage.class);
		mountPage(adminPath + "/tracker", TrackerSettingsPage.class);
		mountPage(adminPath + "/settings", SettingsPage.class);

	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return LoginPage.class;
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new RaceControlSession(request);
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return RaceControlSession.class;
	}

	public ApplicationContext getContext() {
		return context;
	}

}
