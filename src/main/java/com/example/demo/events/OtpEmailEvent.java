package com.example.demo.events;

import org.springframework.context.ApplicationEvent;

public class OtpEmailEvent extends ApplicationEvent{

	private final String email;
    private final String otp;
    
    public OtpEmailEvent(Object source, String email, String otp) {
        super(source);
        this.email = email;
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }
    
    

    
    

}
