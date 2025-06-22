package com.example.demo.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.demo.events.OtpEmailEvent;
import com.example.demo.service.EmailService;

@Component
public class OtpEmailListener {

	@Autowired
	private EmailService emailService;
	
	 @Async("taskExecutor")
	 @EventListener
	public void handleOtpEmailEvent(OtpEmailEvent event) {
		emailService.sendEmailAsync(event.getEmail(), "Your OTP Code", event.getOtp());
	}
}
