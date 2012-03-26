package org.openbakery.racecontrol.web.bean;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebPage;

public class MenuItem implements Serializable, Comparable<MenuItem> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3051626737847509412L;

	private String caption;

	private Class<? extends WebPage> destination;

	private Visibility visibility;

	private int weight;

	public MenuItem(String caption, Class<? extends WebPage> destination) {
		this(caption, destination, Visibility.ALWAYS, 0);
	}

	public MenuItem(String caption, Class<? extends WebPage> destination, int weight) {
		this(caption, destination, Visibility.ALWAYS, weight);
	}

	public MenuItem(String caption, Class<? extends WebPage> destination, Visibility visibility) {
		this(caption, destination, visibility, 0);
	}

	public MenuItem(String caption, Class<? extends WebPage> destination, Visibility visibility, int weight) {
		this.caption = caption;
		this.destination = destination;
		this.visibility = visibility;
		this.weight = weight;
	}

	public String getCaption() {
		return caption;
	}

	public Class<? extends WebPage> getDestination() {
		return destination;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public int compareTo(MenuItem o) {
		if (o != null) {
			return weight - o.weight;
		}
		return -1;
	}
}
