package com.restfulapp.ws.shared;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class Utils {

	public String generateUserId() {
		String userId = UUID.randomUUID().toString();
		return userId;
	}

}
