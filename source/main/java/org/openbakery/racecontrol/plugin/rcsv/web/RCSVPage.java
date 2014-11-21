package org.openbakery.racecontrol.plugin.rcsv.web;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.PropertyModel;
import org.openbakery.racecontrol.data.RaceEntry;
import org.openbakery.racecontrol.service.RaceEntryService;
import org.openbakery.racecontrol.service.ServiceLocateException;
import org.openbakery.racecontrol.service.ServiceLocator;
import org.openbakery.racecontrol.web.RaceControlPage;
import org.openbakery.racecontrol.web.bean.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rene
 * Date: 19.03.12
 * Time: 18:10
 * To change this template use File | Settings | File Templates.
 */
public class RCSVPage extends RaceControlPage {
  private static Logger log = LoggerFactory.getLogger(RCSVPage.class);

  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  public RCSVPage() {
    this(new RCSV());
  }


  public RCSVPage(RCSV rcsv) {

    Form<RaceEntry> form = new Form<RaceEntry>("form");
    add(form);

    List<RaceEntry> raceEntryList = getRaceEntryService().getRaceEntries();

    DropDownChoice<RaceEntry> userChoice = new DropDownChoice<RaceEntry>("raceEntry", new PropertyModel<RaceEntry>(rcsv, "RaceEntry"), raceEntryList, createRaceEntryRenderer());
    form.add(userChoice);
    if (raceEntryList.size() == 1)
    {
      rcsv.setRaceEntry(raceEntryList.get(0));
    }
    form.add(new RCSVGenerateButton("generate", rcsv));


    log.debug("race entries: {}", raceEntryList);
  }

  @Override
  public String getPageTitle() {
    return "RCSV Export";
  }

  @Override
  public Visibility getVisibility() {
    return Visibility.AUTHENTICATED;
  }


  public RaceEntryService getRaceEntryService() {
    ServiceLocator serviceLocator = getSession().getServiceLocator();
    try {
      return (RaceEntryService) serviceLocator.getService(RaceEntryService.class);
    } catch (ServiceLocateException e) {
      error("Internal error!");
      log.error(e.getMessage(), e);
    }
    return null;
  }


  public IChoiceRenderer<RaceEntry> createRaceEntryRenderer() {
    return new IChoiceRenderer<RaceEntry>() {
      private static final long serialVersionUID = 1L;

      public String getDisplayValue(RaceEntry raceEntry) {
        return raceEntry.getTrack() + "-" + DATE_FORMAT.format(raceEntry.getStartTime());
      }

      public String getIdValue(RaceEntry raceEntry, int index) {
        return Integer.toString(raceEntry.getId());
      }
    };
  }
}
