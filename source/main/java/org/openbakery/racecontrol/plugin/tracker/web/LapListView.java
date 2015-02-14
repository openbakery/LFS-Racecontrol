package org.openbakery.racecontrol.plugin.tracker.web;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.openbakery.jinsim.Track;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.openbakery.racecontrol.data.Lap;
import org.openbakery.racecontrol.persistence.bean.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LapListView extends ListView<Lap> {

	private static Logger log = LoggerFactory.getLogger(LapListView.class);

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("m:ss.SSS");

	private static SimpleDateFormat dateFormatShort = new SimpleDateFormat("s.SSS");

	private List<Profile> profiles;

	private Track track;

	public LapListView(String id, List<Lap> lapList, List<Profile> profiles, Track track) {
		super(id, lapList);
		this.profiles = profiles;
		this.track = track;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -9188867995949564845L;

	@Override
	protected void populateItem(ListItem<Lap> item) {
		if (!(item.getModelObject() instanceof Lap)) {
			log.info("is " + item.getModelObject().getClass().getName());
		}
		Lap lap = (Lap) item.getModelObject();

		final int index = item.getIndex();
		int gapPrevious = 0;
		int gapFirst = 0;
		if (index > 0) {
			gapPrevious = lap.getTime() - getList().get(index - 1).getTime();
			gapFirst = lap.getTime() - getList().get(0).getTime();
		}

		item.add(new Label("position", Integer.toString(index + 1)));

		String name = null;
		for (Profile profile : profiles) {
			if (profile.getLfsworldName().equalsIgnoreCase(lap.getDriver().getName())) {
				name = profile.getFullName();
			}
		}

		if (name == null) {
			name = lap.getDriver().getName();
		}

		item.add(new Label("name", name).setEscapeModelStrings(false));
		item.add(new Label("car", lap.getDriver().getCarName()));
		item.add(new Label("time", getTime(lap.getTime())));
		item.add(new Label("number", lap.getNumber() + "/" + lap.getAttempt()));

		if (lap.getTime() == 0) {
			item.add(new Label("gapPrevious", "-"));
			item.add(new Label("gapAll", "-"));
		} else {
			item.add(new Label("gapPrevious", getTime(gapPrevious)));
			item.add(new Label("gapAll", getTime(gapFirst)));
		}

		LinkedList<String> splits = new LinkedList<String>();
		for (int i = 0; i < track.getSplits(); i++) {
			StringBuilder text = new StringBuilder(getTime(lap.getSplit(i)));
			if (i > 0 && i < track.getSplits() - 1) {
				text.append(" (");
				int time = 0;
				for (int j = 0; j <= i; j++) {
					time += lap.getSplit(j);
				}

				text.append(getTime(time));
				text.append(")");
			}
			splits.add(text.toString());
		}

		item.add(new ListView<String>("splits", splits) {

			@Override
			protected void populateItem(ListItem<String> splitItem) {
				splitItem.add(new Label("split", splitItem.getModelObject()));
			}
		});



	}

	private String getTime(int time) {
		if (time == 0) {
			return "-";
		}
		if (time < 60000) {
			return dateFormatShort.format(time);
		}

		return dateFormat.format(time);
	}
}
