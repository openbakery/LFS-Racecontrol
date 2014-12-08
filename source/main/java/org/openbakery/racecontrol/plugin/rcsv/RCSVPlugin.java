package org.openbakery.racecontrol.plugin.rcsv;

import org.openbakery.jinsim.response.InSimResponse;
import org.openbakery.racecontrol.plugin.Plugin;
import org.openbakery.racecontrol.plugin.live.web.LivePage;
import org.openbakery.racecontrol.plugin.rcsv.web.RCSVPage;
import org.openbakery.racecontrol.web.bean.MenuItem;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rene
 * Date: 19.03.12
 * Time: 18:08
 * To change this template use File | Settings | File Templates.
 */
public class RCSVPlugin implements Plugin {

  @Override
  public String getName() {
    return "RCSV";
  }

  @Override
  public String getHelp() {
    return "Plugin to create RCVS files for the drupal league module";
  }

  @Override
  public List<MenuItem> getMenuItems() {
    return Arrays.asList(new MenuItem("RCSV", RCSVPage.class, -1));
  }

  @Override
  public void packetReceived(InSimResponse inSimResponse) {
  }
}
