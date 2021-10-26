package com.restfulapp.ws.userservice.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restfulapp.ws.model.response.UserRest;
import com.restfulapp.ws.shared.Utils;
import com.restfulapp.ws.userservice.UserService;

@Service
public class UserServiceImpl implements UserService {
	Map<String, UserRest> users;
	Utils utils;

	@Autowired
	public UserServiceImpl(Utils utils) {
		this.utils = utils;
	}

	@Override
	public UserRest createUser(UserRest userRest) {

		UserRest user = new UserRest();
		user.setFirstName(userRest.getFirstName());
		user.setLastName(userRest.getLastName());
		user.setEmail(userRest.getEmail());
		user.setPassword(userRest.getPassword());
		String userId = utils.generateUserId();
		user.setUserId(userId);
		if (users == null)
			users = new HashMap<>();
		users.put(userId, user);
		return user;
	}

}
