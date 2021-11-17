package com.restfulapp.ws.userservice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.restfulapp.ws.domain.UserEntity;
import com.restfulapp.ws.feignClient.AlbumsServiceClient;
import com.restfulapp.ws.model.AlbumResponseModel;
import com.restfulapp.ws.model.UserDto;
import com.restfulapp.ws.model.Exceptions.UserServiceException;
import com.restfulapp.ws.model.response.UserRest;
import com.restfulapp.ws.repository.UsersRepository;
import com.restfulapp.ws.shared.Utils;
import com.restfulapp.ws.userservice.UserService;

@Service
public class UserServiceImpl implements UserService {

	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	Map<String, UserRest> users;
	Utils utils;
	BCryptPasswordEncoder bCryptPasswordEncoder;

	UsersRepository usersRepository;

	AlbumsServiceClient albumsServiceClient;

	// RestTemplate restTemplate;

	Environment environment;

	@Autowired
	public UserServiceImpl(UsersRepository usersRepository, Utils utils, BCryptPasswordEncoder bCryptPasswordEncoder,
			Environment environment, AlbumsServiceClient albumsServiceClient) {
		this.usersRepository = usersRepository;
		this.utils = utils;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		// this.restTemplate = restTemplate;
		this.environment = environment;
		this.albumsServiceClient = albumsServiceClient;
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

	@Override
	public UserDto getUserByUserId(String userId) {
		UserEntity userEntity = usersRepository.findByUserId(userId);
		if (userEntity == null)
			throw new UserServiceException("User not found");
		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
//      The rest template will ask Eureka for all the addresses that it knows about Albums microservice, 
//		and rest template will use this list of addresses to load balance requests that it sends to Albums microservice
		String albumsUrl = String.format(environment.getProperty("albums.url"), userId);
//		ResponseEntity<List<AlbumResponseModel>> response = restTemplate.exchange(albumsUrl, HttpMethod.GET, null,
//				new ParameterizedTypeReference<List<AlbumResponseModel>>() {
//				});
//
//		List<AlbumResponseModel> albumsList = response.getBody();

		logger.info("Before calling albumns microservices");

		List<AlbumResponseModel> albumsList = null;
//		try {
		albumsList = albumsServiceClient.getAlbums(userId);
//		} catch (FeignException ex) {
//			logger.error(ex.getLocalizedMessage());
//		}
		
		logger.info("After calling albumns microservices");
		userDto.setAlbumsList(albumsList);
		return userDto;

	}

}
