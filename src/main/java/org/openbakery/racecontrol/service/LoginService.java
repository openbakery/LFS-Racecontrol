package org.openbakery.racecontrol.service;

import org.openbakery.racecontrol.bean.User;
import org.openbakery.racecontrol.service.exception.LoginFailedException;

public class LoginService {

	private String username;

	private String password;

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void login(User user) throws LoginFailedException {
		if (user.getName().equals(username) && user.getPassword().equals(password)) {
			user.setLoggedIn(true);
			return;
		}
		throw new LoginFailedException("Unknown username/password");
	}
}
