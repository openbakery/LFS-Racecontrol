package org.openbakery.racecontrol.plugin.tracker.web;

import java.util.ArrayList;
import java.util.EnumSet;

import net.sf.jinsim.Car;
import net.sf.jinsim.Track;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.openbakery.racecontrol.plugin.tracker.data.TrackerSettings;
import org.openbakery.racecontrol.web.RaceControlPage;
import org.openbakery.racecontrol.web.bean.Visibility;
import org.openbakery.racecontrol.web.page.common.RedirectLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackerSettingsPage extends RaceControlPage {

	private Logger log = LoggerFactory.getLogger(TrackerSettingsPage.class);

	public TrackerSettingsPage(PageParameters parameters) {
		super(parameters);
		Form<TrackerSettings> form = new Form<TrackerSettings>("form");
		add(form);

		TrackerSettings trackerSettings = getSession().getServiceLocator().getSettingsService().getTrackerSettings();
		form.add(new CheckBoxMultipleChoice("cars", new PropertyModel(trackerSettings, "cars"), new ArrayList<Car>(EnumSet.allOf(Car.class))));

		form.add(new ListChoice("track", new PropertyModel(trackerSettings, "track"), new ArrayList<Track>(EnumSet.allOf(Track.class))));

		form.add(new RedirectLink("cancel", TrackerPage.class));
		form.add(new SaveButton(getSession()));

	}

	@Override
	public String getPageTitle() {
		return "Settings";
	}

	@Override
	public Visibility getVisibility() {
		return Visibility.AUTHENTICATED;
	}

}
