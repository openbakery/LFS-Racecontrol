package org.openbakery.racecontrol.web;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.openbakery.racecontrol.web.bean.MenuItem;
import org.openbakery.racecontrol.web.bean.Visibility;

/**
 * User: rene
 * Date: 24/11/14
 */
public class MenuPanel extends Panel {

	public MenuPanel(Class currentPage) {
		super("menuPanel");
		RepeatingView menu = new RepeatingView("menuItems");

		for (MenuItem item : getSession().getMenuItems()) {
			if (!getSession().isLoggedIn() && item.getVisibility() == Visibility.AUTHENTICATED) {
				continue;
			}


			WebMarkupContainer container = new WebMarkupContainer(menu.newChildId());
			menu.add(container);
			BookmarkablePageLink link = new BookmarkablePageLink("link", item.getDestination());
			container.add(link);
			link.add(new Label("caption", item.getCaption()));

			if (item.getDestination() == currentPage) {
				link.add(new AttributeModifier("class", new AbstractReadOnlyModel() {
					public Object getObject() {
						return "active";
					}
				}));

			}
		}
		add(menu);

	}

	public RaceControlSession getSession() {
		return (RaceControlSession) super.getSession();
	}

}
