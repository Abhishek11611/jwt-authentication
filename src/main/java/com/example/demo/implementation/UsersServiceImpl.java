package com.example.demo.implementation;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthenticationRequest;
import com.example.demo.entity.Users;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.UsersService;

@Service
public class UsersServiceImpl implements UsersService{
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public String register(AuthenticationRequest authenticationRequest) {
		
		String userName = authenticationRequest.getUserName();
		String userPassword = authenticationRequest.getUserPassword();
		Long userMobileNumber = authenticationRequest.getUserMobileNumber();
		String userEmail = authenticationRequest.getUserEmail();
		
		if(userName==null || userPassword == null || userMobileNumber == null || userEmail == null) {
			throw new NullPointerException("Please all Fields are Required !!");
		}
		  boolean existsByUserName = usersRepository.existsByUserName(userName);
		if (existsByUserName) {
			throw new IllegalArgumentException("Username Already exist !!!");
		}
		
		String encode = passwordEncoder.encode(userPassword);
		Users users = new Users();
		users.setUserName(userName);
		users.setUserPassword(encode);
		users.setUserMobileNumber(userMobileNumber);
		users.setUserEmail(userEmail);
		
		usersRepository.save(users);
		
		
		return "User Added Successfully!!!";
	}

	@Override
	public String login(String userName, String userPassword) {
		
		
		Users users = usersRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
		
		authenticationManager.authenticate(
    			new UsernamePasswordAuthenticationToken(userName, userPassword)
    			);
		
		 final UserDetails userDetails = new org.springframework.security.core.userdetails.User(
	    	        userName, userPassword, new ArrayList<>()
	    	    );
	    	    return jwtUtil.generateToken(users);
		
	}

}
