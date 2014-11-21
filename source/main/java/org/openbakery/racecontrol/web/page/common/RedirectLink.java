package org.openbakery.racecontrol.web.page.common;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

public class RedirectLink extends Link {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4676092910363316963L;

	private Class<? extends WebPage> pageClass;

	public RedirectLink(String id, Class<? extends WebPage> pageClass) {
		super(id);
		this.pageClass = pageClass;
	}

	public void onClick() {
		setResponsePage(pageClass);
	}

}
