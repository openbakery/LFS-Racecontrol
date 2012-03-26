package org.openbakery.racecontrol.plugin.admin.messages.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openbakery.racecontrol.gui.MessageSize;

@Entity
@Table(name = "racecontrol_admin_message")
public class AdminMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3316670676715217739L;

	@Id
	@GeneratedValue
	private int id;

	@Column(name = "key_name")
	public String key;

	@Column(name = "message")
	public String message;

	@Column(name = "duration")
	public int duration;

	@Column(name = "countdown")
	public boolean countdown;

	@Column(name = "size")
	public MessageSize size;

	public MessageSize getSize() {
		return size;
	}

	public void setSize(MessageSize size) {
		this.size = size;
	}

	public AdminMessage() {
	}

	AdminMessage(String key, String message) {
		this.key = key;
		this.message = message;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean getCountdown() {
		return countdown;
	}

	public void setCountdown(boolean countdown) {
		this.countdown = countdown;
	}

	public int getId() {
		return id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
