package org.openbakery.racecontrol.service;

import org.openbakery.racecontrol.data.RaceEntry;
import org.openbakery.racecontrol.persistence.Persistence;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.openbakery.racecontrol.persistence.RCSVGenerator;
import org.openbakery.racecontrol.persistence.Transaction;
import org.openbakery.racecontrol.plugin.rcsv.web.RCSVPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rene
 * Date: 19.03.12
 * Time: 18:14
 * To change this template use File | Settings | File Templates.
 */
public class RaceEntryService {
  private static Logger log = LoggerFactory.getLogger(RCSVPage.class);

  @Autowired
  Persistence persistence;

  public List<RaceEntry> getRaceEntries()
  {
    try {
      return (List<RaceEntry>)persistence.query("SELECT raceEntry from RaceEntry raceEntry");
    } catch (PersistenceException e) {
      return Collections.emptyList();
    }
  }


  public String createRcsv(RaceEntry raceEntry) {
    RCSVGenerator generator = new RCSVGenerator();
    
    Transaction transaction =  persistence.createTransaction();

    try {
      raceEntry = transaction.refresh(raceEntry.getClass(), raceEntry.getId() );
      return generator.generate(raceEntry);
    } catch (PersistenceException e) {
      log.error(e.getMessage(), e);
      return "";
    }
    finally {
      transaction.commit();
    }
  }
}
