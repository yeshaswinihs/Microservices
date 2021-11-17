package com.restfulapp.ws.model.response;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.restfulapp.ws.model.AlbumResponseModel;

public class UserRest {
	@Size(min = 2, message = "First name should not be 2 characters")
	@NotNull(message = "First name should not be null")
	private String firstName;
	@NotNull(message = "Last name should not be null")
	private String lastName;
	@NotNull(message = "Email should not be null")
	@Email
	private String email;
	private String userId;
	@NotNull
	@Size(min = 8, max = 16, message = "password must be greater than 8 and less than 16 characters")
	private String password;

	private List<AlbumResponseModel> albums;

	public List<AlbumResponseModel> getAlbums() {
		return albums;
	}

	public void setAlbums(List<AlbumResponseModel> albums) {
		this.albums = albums;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
