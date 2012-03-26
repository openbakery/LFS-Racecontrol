package org.openbakery.racecontrol.plugin.rcsv.web;

import org.openbakery.racecontrol.data.RaceEntry;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: rene
 * Date: 23.03.12
 * Time: 16:01
 * To change this template use File | Settings | File Templates.
 */
public class RCSV implements Serializable {

  private RaceEntry raceEntry;

  public RaceEntry getRaceEntry() {
    return raceEntry;
  }

  public void setRaceEntry(RaceEntry raceEntry) {
    this.raceEntry = raceEntry;
  }
}
