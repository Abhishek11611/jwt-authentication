package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long>{
	
	boolean existsByUserName(String userName);
	
	Optional<Users> findByUserName(String userName);
	Optional<Users> findByUserEmail(String userEmail);
	Optional<Users> findByUserMobileNumber(String userMobileNumber);

}
