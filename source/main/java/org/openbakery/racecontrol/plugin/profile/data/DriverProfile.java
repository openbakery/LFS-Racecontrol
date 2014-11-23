package org.openbakery.racecontrol.plugin.profile.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openbakery.racecontrol.persistence.bean.Profile;

@Entity
@Table(name = "racecontrol_driver_profile")
public class DriverProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int getId() {
		return id;
	}

	@Id
	@GeneratedValue
	private int id;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "lastname")
	private String lastname;

	@Column(name = "lfsworldname")
	private String lfsworldName;

	@Column(name ="signedUp")
	private boolean signedUp;

	public DriverProfile() {
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getLfsworldName() {
		return lfsworldName;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setLfsworldName(String lfsworldName) {
		this.lfsworldName = lfsworldName;
	}

	public boolean isSignedUp() {
		return signedUp;
	}

	public void setSignedUp(boolean signedUp) {
		this.signedUp = signedUp;
	}

	public Profile getProfile() {
		return new Profile(0, firstname, lastname, lfsworldName);
	}
}
