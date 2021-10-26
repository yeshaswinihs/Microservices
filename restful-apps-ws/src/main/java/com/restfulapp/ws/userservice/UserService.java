package com.restfulapp.ws.userservice;

import com.restfulapp.ws.model.response.UserRest;

public interface UserService {
	UserRest createUser(UserRest userRest);
}
