package org.openbakery.racecontrol.plugin.live.web;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.openbakery.racecontrol.util.LfsNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiveTimingListView extends ListView<LiveTiming> {

	private static Logger logger = LoggerFactory.getLogger(LiveTimingListView.class);

	private static final long serialVersionUID = 1L;

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("m:ss.SSS");

	private static SimpleDateFormat dateFormatShort = new SimpleDateFormat("s.SSS");

	private LiveTiming first;

	public LiveTimingListView(String id, List<LiveTiming> driverList) {
		super(id, driverList);
		if (driverList != null && driverList.size() > 0) {
			first = driverList.get(0);
		}
	}

	@Override
	protected void populateItem(ListItem<LiveTiming> item) {

		LiveTiming timing = item.getModelObject();
		Label nameLabel = new Label("name", LfsNames.getHtmlColoredName(timing.getDriverName()));
		nameLabel.setEscapeModelStrings(false);
		item.add(nameLabel);
		int lapNumber = timing.getLapsCompleted();
		String laptimeString;
		String lapNumberString;
		String totalTimeString;
		if (lapNumber > 0) {

			laptimeString = dateFormat.format(timing.getLastLapTime());
			laptimeString = laptimeString.substring(0, laptimeString.length() - 1);

			lapNumberString = Integer.toString(lapNumber);

			logger.debug("time {} - laps {} ", (timing.getTotalTime() - first.getTotalTime()), timing.getLapsCompleted() - first.getLapsCompleted());
			if (timing == first) {
				totalTimeString = dateFormat.format(timing.getTotalTime());
				totalTimeString = totalTimeString.substring(0, totalTimeString.length() - 1);
			} else if ((timing.getTotalTime() > first.getTotalTime()) && (timing.getLapsCompleted() < first.getLapsCompleted())) {
				totalTimeString = "+" + Integer.toString(first.getLapsCompleted() - timing.getLapsCompleted()) + " Laps";
			} else if (timing.getLapsCompleted() < (first.getLapsCompleted() - 1)) {
				totalTimeString = "+" + Integer.toString(first.getLapsCompleted() - timing.getLapsCompleted() - 1) + " Laps";
			} else {
				int value = timing.getTotalTime() - first.getTotalTime(lapNumber);
				if (value > 60000) {
					totalTimeString = dateFormat.format(value);
				} else {
					totalTimeString = dateFormatShort.format(value);
				}
				totalTimeString = "+" + totalTimeString.substring(0, totalTimeString.length() - 1);
			}

		} else {
			laptimeString = "-";
			totalTimeString = "-";
			lapNumberString = "0";
		}

		item.add(new Label("position", Integer.toString(timing.getPosition())));
		item.add(new Label("laptime", laptimeString));
		item.add(new Label("lapnumber", lapNumberString));
		item.add(new Label("totaltime", totalTimeString));
	}
}