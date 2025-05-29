package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthenticationRequest;
import com.example.demo.response.ResponseHandler;
import com.example.demo.service.UsersService;

@RestController
@RequestMapping("/auth")
public class UsersController {
	
	
	@Autowired
	private UsersService usersService;
	
	@PostMapping("/register")
	public ResponseHandler register(@RequestBody AuthenticationRequest request ) {
		ResponseHandler response = new ResponseHandler();

		try {
			String register = usersService.register(request);
			
			response.setStatus(true);
			response.setMessage("success");
			response.setData(register);
		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage("Failed" + e.getMessage());
			response.setData(null);
		}
		return response;

	}
	
	@PostMapping("/login")
	public ResponseHandler login(@RequestParam String username,@RequestParam String password) {

		ResponseHandler response = new ResponseHandler();

		try {
			String login = usersService.login(username, password);
			response.setStatus(true);
			response.setMessage(" successfully");
			response.setData(login);

		} catch (IllegalArgumentException e) {
			response.setStatus(false);
			response.setMessage("Failed" + e.getMessage());
			response.setData(null);
		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage("Failed" + e.getMessage());
			response.setData(null);
		}

		return response;

	}

}
