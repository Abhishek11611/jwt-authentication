package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_table")
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "user_password")
	private String userPassword;

	@Column(name = "user_email")
	private String userEmail;

	@Column(name = "user_mobile_number")
	private String userMobileNumber;

	@Column(name = "otp")
	private String otp;

	@Column(name = "otp_attempts")
	private Integer otpAttempts;

	@Column(name = "otp_expired_at")
	private LocalDateTime otpExpiredAt;

	@Column(name = "user_lock_time")
	private LocalDateTime userLockTime;

	public Users() {
		// TODO Auto-generated constructor stub
	}

	public Users(Long id, String userName, String userPassword, String userEmail, String userMobileNumber) {
		super();
		this.id = id;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userEmail = userEmail;
		this.userMobileNumber = userMobileNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserMobileNumber() {
		return userMobileNumber;
	}

	public void setUserMobileNumber(String userMobileNumber) {
		this.userMobileNumber = userMobileNumber;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Integer getOtpAttempts() {
		return otpAttempts;
	}

	public void setOtpAttempts(Integer otpAttempts) {
		this.otpAttempts = otpAttempts;
	}

	public LocalDateTime getOtpExpiredAt() {
		return otpExpiredAt;
	}

	public void setOtpExpiredAt(LocalDateTime otpExpiredAt) {
		this.otpExpiredAt = otpExpiredAt;
	}

	public LocalDateTime getUserLockTime() {
		return userLockTime;
	}

	public void setUserLockTime(LocalDateTime userLockTime) {
		this.userLockTime = userLockTime;
	}

}
