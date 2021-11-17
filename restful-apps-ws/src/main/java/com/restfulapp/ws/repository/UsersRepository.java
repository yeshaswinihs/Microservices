package com.restfulapp.ws.repository;

import org.springframework.data.repository.CrudRepository;

import com.restfulapp.ws.domain.UserEntity;

public interface UsersRepository extends CrudRepository<UserEntity, Long> {

	UserEntity findByEmail(String email);

	UserEntity findByUserId(String userId);
}
