package org.openbakery.racecontrol.web;

import org.apache.wicket.AttributeModifier;
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
    add(createMenu());
  }

	public RaceControlPage(PageParameters parameters) {
    add(new Label("pageTitle", getPageTitle()));
    add(new FeedbackPanel("feedback"));
		if (parameters.getNamedKeys().contains("hideMenu")) {
			add(new RepeatingView("menu"));
			return;
		}



		add(createMenu());
	}



	private RepeatingView createMenu() {

		RepeatingView menu = new RepeatingView("menu");
		if (!getSession().isLoggedIn()) {
			return menu;
		}

		for (MenuItem item : getSession().getMenuItems()) {
			if (!getSession().isLoggedIn() && item.getVisibility() == Visibility.AUTHENTICATED) {
				continue;
			}
			// if (item.getDestination() == getClass()) {
			// continue;
			// }

			WebMarkupContainer container = new WebMarkupContainer(menu.newChildId());
			menu.add(container);
			BookmarkablePageLink link = new BookmarkablePageLink("link", item.getDestination());
			container.add(link);
			link.add(new Label("caption", item.getCaption()));

			if (item.getDestination() == getClass()) {
				link.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
					public Object getObject() {
						return "active";
					}
				}));

			}
		}
		return menu;

	}

	@Override
	public RaceControlSession getSession() {
		return (RaceControlSession) super.getSession();
	}

	public abstract String getPageTitle();

	public abstract Visibility getVisibility();

}
