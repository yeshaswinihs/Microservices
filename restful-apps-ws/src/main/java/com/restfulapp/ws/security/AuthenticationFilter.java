package com.restfulapp.ws.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfulapp.ws.model.LoginRequestModel;
import com.restfulapp.ws.model.UserDto;
import com.restfulapp.ws.userservice.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private UserService userService;
	private Environment environment;

	public AuthenticationFilter(UserService userService, Environment environment,
			AuthenticationManager authenticationManager) {
		this.userService = userService;
		this.environment = environment;
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			LoginRequestModel credentials = new ObjectMapper().readValue(request.getInputStream(),
					LoginRequestModel.class);
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					credentials.getEmail(), credentials.getPassword(), new ArrayList<>());
			return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);

		} catch (IOException exception) {
			throw new RuntimeException();
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// username here is email
		String userName = ((User) authResult.getPrincipal()).getUsername();
		UserDto userDetails = userService.getUserDetailsByEmail(userName);

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		String token = Jwts.builder().setIssuedAt(now).setSubject(userDetails.getUserId())
				.setExpiration(new Date(
						System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"))))
				.signWith(SignatureAlgorithm.HS256, environment.getProperty("token.secret")).compact();
		response.addHeader("token", token);
		response.addHeader("userId", userDetails.getUserId());
	}

}
