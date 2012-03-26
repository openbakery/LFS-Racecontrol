package org.openbakery.racecontrol.plugin.rcsv.web;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.resource.StringResourceStream;
import org.openbakery.racecontrol.data.RaceEntry;
import org.openbakery.racecontrol.persistence.RCSVGenerator;
import org.openbakery.racecontrol.service.RaceEntryService;
import org.openbakery.racecontrol.service.ServiceLocateException;
import org.openbakery.racecontrol.service.ServiceLocator;
import org.openbakery.racecontrol.web.RaceControlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: rene
 * Date: 23.03.12
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
public class RCSVGenerateButton extends Button {

  private static Logger log = LoggerFactory.getLogger(RCSVPage.class);

  private static final String SEPARATOR = ";";
  private static final String NEWLINE = System.getProperty("line.separator");
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  private RCSV rcsv;

  public RCSVGenerateButton(String id, RCSV rcsv) {
    super(id, new ResourceModel(id));
    this.rcsv = rcsv;
  }

  public void onSubmit() {
    RaceEntry raceEntry = rcsv.getRaceEntry();
    log.debug("submit pressed for entry {}", raceEntry);
    RaceEntryService raceEntryService = getRaceEntryService();
    String rcsvData = raceEntryService.createRcsv(raceEntry);
    StringResourceStream resourceStream = new StringResourceStream(rcsvData);
    resourceStream.setCharset(Charset.forName("UTF-8"));
    ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(resourceStream);
    StringBuilder builder = new StringBuilder();

    builder.append(raceEntry.getTrack());
    builder.append(DATE_FORMAT.format(raceEntry.getStartTime()));
    builder.append(".rcsv");
    handler.setFileName(builder.toString());
    handler.setContentDisposition(ContentDisposition.ATTACHMENT);
    getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
  }


  // TODO: change to SpringBean
  public RaceEntryService getRaceEntryService() {
    ServiceLocator serviceLocator = ((RaceControlSession)getSession()).getServiceLocator();
    try {
      return (RaceEntryService) serviceLocator.getService(RaceEntryService.class);
    } catch (ServiceLocateException e) {
      error("Internal error!");
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
