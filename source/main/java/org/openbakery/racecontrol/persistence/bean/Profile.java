package org.openbakery.racecontrol.persistence.bean;

public class Profile {

	private int uid;

	private String firstname;

	private String lastname;

	private String lfsworldName;

	public Profile(int uid, String firstname, String lastname, String lfsworldName) {
		this.uid = uid;
		this.firstname = firstname;
		this.lastname = lastname;
		this.lfsworldName = lfsworldName;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getLfsworldName() {
		return lfsworldName;
	}

	public void setLfsworldName(String lfsworldName) {
		this.lfsworldName = lfsworldName;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder("Profile[uid=");
		builder.append(uid);
		builder.append(", firstname=");
		builder.append(firstname);
		builder.append(", lastname=");
		builder.append(lastname);
		builder.append(", lfsworldName=");
		builder.append(lfsworldName);
		builder.append("]");
		return builder.toString();

	}

	public String getFullName() {
		StringBuilder builder = new StringBuilder();
		builder.append(getFirstname());
		builder.append(" '");
		builder.append(getLfsworldName());
		builder.append("' ");
		builder.append(getLastname());
		return builder.toString();

	}
}
