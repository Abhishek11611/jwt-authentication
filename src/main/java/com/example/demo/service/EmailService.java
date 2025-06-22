package com.example.demo.service;

import java.util.concurrent.CompletableFuture;

public interface EmailService {
	
	 CompletableFuture<String> sendEmailAsync(String to, String subject, String body);

}
