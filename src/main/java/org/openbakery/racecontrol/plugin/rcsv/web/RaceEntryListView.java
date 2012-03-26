package org.openbakery.racecontrol.plugin.rcsv.web;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.openbakery.racecontrol.data.RaceEntry;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rene
 * Date: 19.03.12
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
public class RaceEntryListView extends ListView<RaceEntry> {

  public RaceEntryListView(String id, List<RaceEntry> raceEntryList) {
    super(id, raceEntryList);
  }

  @Override
  protected void populateItem(ListItem<RaceEntry> item) {
    RaceEntry entry = item.getModelObject();
    Label nameLabel = new Label("entryName", entry.getTrack() + " " + entry.getStartTime() );
    item.add(nameLabel);
    //item.add(new RCSVGenerateButton("generate", entry));
  }
}
