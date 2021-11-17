package com.restfulapp.ws.userservice;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.restfulapp.ws.model.UserDto;

public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto userDto);

	UserDto getUserDetailsByEmail(String email);

	UserDto getUserByUserId(String userId);
}
