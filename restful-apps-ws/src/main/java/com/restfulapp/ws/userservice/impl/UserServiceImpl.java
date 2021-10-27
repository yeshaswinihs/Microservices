package com.restfulapp.ws.userservice.impl;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.restfulapp.ws.domain.UserEntity;
import com.restfulapp.ws.model.UserDto;
import com.restfulapp.ws.model.response.UserRest;
import com.restfulapp.ws.repository.UsersRepository;
import com.restfulapp.ws.shared.Utils;
import com.restfulapp.ws.userservice.UserService;

@Service
public class UserServiceImpl implements UserService {
	Map<String, UserRest> users;
	Utils utils;
	BCryptPasswordEncoder bCryptPasswordEncoder;

	UsersRepository usersRepository;

	@Autowired
	public UserServiceImpl(UsersRepository usersRepository, Utils utils, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.usersRepository = usersRepository;
		this.utils = utils;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public UserDto createUser(UserDto userDetails) {

		userDetails.setUserId(UUID.randomUUID().toString());

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		// userEntity.setEncryptedPassword("test");
		UserEntity userEntityOut = usersRepository.save(userEntity);
		/*
		 * UserRest user = new UserRest(); user.setFirstName(userRest.getFirstName());
		 * user.setLastName(userRest.getLastName()); user.setEmail(userRest.getEmail());
		 * user.setPassword(userRest.getPassword()); String userId =
		 * utils.generateUserId(); user.setUserId(userId); if (users == null) users =
		 * new HashMap<>(); users.put(userId, user); return user;
		 */
		UserDto userDto = modelMapper.map(userEntityOut, UserDto.class);
		return userDto;

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = usersRepository.findByEmail(username);
		if (userEntity == null)
			throw new UsernameNotFoundException(username);
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true,
				new ArrayList<>());
	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserEntity userEntity = usersRepository.findByEmail(email);
		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		return new ModelMapper().map(userEntity, UserDto.class);
	}

}
