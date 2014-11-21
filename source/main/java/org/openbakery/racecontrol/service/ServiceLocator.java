package org.openbakery.racecontrol.service;

import java.util.Map;

import org.openbakery.racecontrol.web.RaceControlWebApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class ServiceLocator {

	private LoginService loginService;

	private RaceService raceService;

	private DatabaseService databaseService;

	private SettingsService settingsService;

	public SettingsService getSettingsService() {
		return settingsService;
	}

	@Autowired
	public void setSettingsService(SettingsService settingsService) {
		this.settingsService = settingsService;
	}

	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	@Autowired
	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	public RaceService getRaceService() {
		return raceService;
	}

	@Autowired
	public void setRaceService(RaceService raceService) {
		this.raceService = raceService;
	}

	public LoginService getLoginService() {
		return loginService;
	}

	@Autowired
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	public Object getService(Class<? extends Object> clazz) throws ServiceLocateException {
		RaceControlWebApplication application = (RaceControlWebApplication) RaceControlWebApplication.get();
		ApplicationContext context = application.getContext();
		Map beanMap = context.getBeansOfType(clazz);
		if (beanMap.size() == 1) {
			return beanMap.values().iterator().next();
		}
		throw new ServiceLocateException("Unable to locate Service " + clazz + ":" + beanMap);
	}
}
