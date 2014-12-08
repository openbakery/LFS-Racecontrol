package org.openbakery.racecontrol.web;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.openbakery.racecontrol.web.bean.MenuItem;
import org.openbakery.racecontrol.web.bean.Visibility;

public abstract class RaceControlPage extends WebPage {


  public RaceControlPage() {
    add(new Label("pageTitle", getPageTitle()));
    add(new FeedbackPanel("feedback"));
  }

	public RaceControlPage(PageParameters parameters) {
    add(new Label("pageTitle", getPageTitle()));
    add(new FeedbackPanel("feedback"));

		MenuPanel menu = new MenuPanel(getClass());
		add(menu);

		menu.setVisible(getSession().isLoggedIn());
	}


	@Override
	public RaceControlSession getSession() {
		return (RaceControlSession) super.getSession();
	}

	public abstract String getPageTitle();

	public abstract Visibility getVisibility();

}
