package com.restfulapp.ws.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restfulapp.ws.model.Exceptions.UserServiceException;
import com.restfulapp.ws.model.response.UserRest;
import com.restfulapp.ws.userservice.UserService;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {

	@Autowired
	private Environment env;

	Map<String, UserRest> users;

	@Autowired
	UserService userService;

	@GetMapping(path = "/{userId}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<UserRest> getUser(@PathVariable String userId) {

//		String firstName=null;
//		int length= firstName.length();
		// throw a custom exception
		if (true)
			throw new UserServiceException("A user service exception is thrown");

		if (users.containsKey(userId)) {
			return new ResponseEntity<>(users.get(userId), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

	}

	@GetMapping()
	public String getUsers(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			@RequestParam(value = "limit", defaultValue = "50") int limit,
			@RequestParam(value = "sort", defaultValue = "desc", required = false) String sort) {
		return "get" + page + limit + sort;
	}

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserRest> createUser(@Valid @RequestBody UserRest userRest) {

		UserRest user = userService.createUser(userRest);
		return new ResponseEntity<UserRest>(user, HttpStatus.CREATED);
	};

	@PutMapping(path = "/{userId}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserRest> updateUser(@PathVariable String userId, @RequestBody UserRest userRest) {

		if (users.containsKey(userId)) {
			UserRest user = users.get(userId);
			user.setFirstName(userRest.getFirstName());
			user.setLastName(userRest.getLastName());
			user.setEmail(userRest.getEmail());
			user.setPassword(userRest.getPassword());
			users.put(userId, user);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

	}

	@DeleteMapping(path = "/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
		if (users.containsKey(userId)) {
			users.remove(userId);
		}
		return ResponseEntity.noContent().build();
	}

	@GetMapping(path = "/status/check")
	public String status() {
		return "working on port " +env.getProperty("local.server.port");
	}

}
