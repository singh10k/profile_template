package com.profile.in.repository;


import com.profile.in.model.UserDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDetailsRepo extends CrudRepository<UserDetails, Integer> {
	public Optional<UserDetails> findByUname(String uname);
}
