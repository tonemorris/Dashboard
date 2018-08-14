package com.tivo.ui.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tivo.ui.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
	
}
