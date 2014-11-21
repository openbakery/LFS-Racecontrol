package org.openbakery.racecontrol.plugin.live.web;

import java.util.List;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.openbakery.racecontrol.plugin.live.service.LiveService;
import org.openbakery.racecontrol.service.ServiceLocateException;
import org.openbakery.racecontrol.service.ServiceLocator;
import org.openbakery.racecontrol.web.RaceControlPage;
import org.openbakery.racecontrol.web.bean.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LivePage extends RaceControlPage {

	private static Logger log = LoggerFactory.getLogger(LivePage.class);

	public LivePage(PageParameters parameters) {
		super(parameters);

		List<LiveTiming> liveTimingList = getLiveService().getLiveTiming();
		log.debug("live timing: {}", liveTimingList);
		add(new LiveTimingListView("liveTiming", liveTimingList));
	}

	@Override
	public String getPageTitle() {
		return "Live Tracker";
	}

	@Override
	public Visibility getVisibility() {
		return Visibility.ALWAYS;
	}

	public LiveService getLiveService() {
		ServiceLocator serviceLocator = getSession().getServiceLocator();
		try {
			return (LiveService) serviceLocator.getService(LiveService.class);
		} catch (ServiceLocateException e) {
			error("Internal error!");
			log.error(e.getMessage(), e);
		}
		return null;
	}

}
