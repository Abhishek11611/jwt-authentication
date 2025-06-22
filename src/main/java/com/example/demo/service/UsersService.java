package com.example.demo.service;

import com.example.demo.dto.AuthenticationRequest;

import jakarta.mail.MessagingException;

public interface UsersService {
	public String register(AuthenticationRequest authenticationRequest);
	
	public String login(String anyValue, String userPassword);
		
	public String generateOTP();
	
	public String encrypt(String input, String key) throws Exception;
	public String decrypt(String input, String key) throws Exception;
	
	public String verifyOtp(String anyValue,String otp)throws Exception;
}
