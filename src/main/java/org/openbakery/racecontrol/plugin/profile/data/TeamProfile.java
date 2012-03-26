package org.openbakery.racecontrol.plugin.profile.data;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class TeamProfile implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	private String name;
}
