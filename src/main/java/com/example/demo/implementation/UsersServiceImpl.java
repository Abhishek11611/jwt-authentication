package com.example.demo.implementation;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthenticationRequest;
import com.example.demo.entity.Users;
import com.example.demo.events.OtpEmailEvent;
import com.example.demo.jwt.JwtUtil;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.UsersService;

@Service
public class UsersServiceImpl implements UsersService {
	
	public static final String SECRET_KEY_STRING = "0123456789abcdef0123456789abcdef"; // 32 chars for AES-256
    private static final String AES_ALGORITHM = "AES";
    private static final String ENCODING = "UTF-8";
	
	String messsage = "The Email OTP method enables you to authenticate using the"
			+ " one-time password 234090 that is sent to the registered email"
			+ " address. When you try to authenticate on any device, the server ";
			

	private static final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	
	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private EmailService emailService;

	@Override
	public String register(AuthenticationRequest authenticationRequest) {

		String userName = authenticationRequest.getUserName();
		String userPassword = authenticationRequest.getUserPassword();
		Long userMobileNumber = authenticationRequest.getUserMobileNumber();
		String userEmail = authenticationRequest.getUserEmail();

		if (userName == null || userPassword == null || userMobileNumber == null || userEmail == null) {
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
		users.setUserMobileNumber(userMobileNumber + "");
		users.setUserEmail(userEmail);

		usersRepository.save(users);

		return "User Added Successfully!!!";
	}

	@Override
	public String login(String anyvalue, String userPassword) {
		long start = System.currentTimeMillis();
		Optional<Users> users;

		if (anyvalue.contains("@")) {
			users = usersRepository.findByUserEmail(anyvalue);
		} else if (anyvalue.matches("\\d{10}")) {
			users = usersRepository.findByUserMobileNumber(anyvalue);
		} else {
			users = usersRepository.findByUserName(anyvalue);
		}

		if (users.isEmpty()) {
			throw new IllegalArgumentException(" Empty Invalid Credential!!!!");
		}
		   Users users2 = users.get();
		   String storedPassword = users2.getUserPassword();
		   
		   String otp = generateOTP();

		if (passwordEncoder.matches(userPassword, storedPassword)) {

			try {
				
//			emailService.sendEmailAsync(users2.getUserEmail(), "SuccessFully !!!!!",  otp);
				eventPublisher.publishEvent(new OtpEmailEvent(this, users2.getUserEmail(), otp));
				System.out.println(otp);
					String encrytpotp = encrypt(otp,SECRET_KEY_STRING);	
					
				users2.setOtp(encrytpotp);
				users2.setOtpExpiredAt(LocalDateTime.now().plusMinutes(5));
				usersRepository.save(users2);
				
				long end = System.currentTimeMillis(); // 🕒 End time
		        logger.info((end - start) / 1000.0 + " seconds.");


				
			} catch (Exception e) {
				
				e.printStackTrace();
				throw new IllegalArgumentException("Otp Failed!!!!!!!!!");
			}
		}else {
		throw new IllegalArgumentException(" Wrong Password Invalid Credential");
		}
		return "OTP Sent SuccessFully !!!!!!!";
	}

	
	
	 @Override
	    public String generateOTP() {
	        SecureRandom random = new SecureRandom();
	        int otpValue = 100000 + random.nextInt(900000);
	        return String.valueOf(otpValue);
	    }
	 
	 @Override
	    public String encrypt(String input, String key) throws Exception {
	        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
	        byte[] keyBytes = key.getBytes(ENCODING);
	        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, AES_ALGORITHM);
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	        byte[] encryptedBytes = cipher.doFinal(input.getBytes(ENCODING));
	        return Base64.getEncoder().encodeToString(encryptedBytes);
	    }
	 
	 @Override
	 public String decrypt(String input, String key) throws Exception {
	     Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
	     byte[] keyBytes = key.getBytes(ENCODING);
	     SecretKeySpec secretKey = new SecretKeySpec(keyBytes, AES_ALGORITHM);
	     cipher.init(Cipher.DECRYPT_MODE, secretKey);
	     byte[] decodedBytes = Base64.getDecoder().decode(input);
	     byte[] decryptedBytes = cipher.doFinal(decodedBytes);
	     return new String(decryptedBytes, ENCODING);
	 }


	@Override
	public String verifyOtp(String anyValue, String otp) throws Exception {
		
		Optional<Users> users;

		if (anyValue.contains("@")) {
			users = usersRepository.findByUserEmail(anyValue);
		} else if (anyValue.matches("\\d{10}")) {
			users = usersRepository.findByUserMobileNumber(anyValue);
		} else {
			users = usersRepository.findByUserName(anyValue);
		}

		if (users.isEmpty()) {
			throw new IllegalArgumentException(" Empty Invalid Credential!!!!");
		}
		
		Users storedUser = users.get();
		String hashedOtp = storedUser.getOtp();
		String storedotp = decrypt(hashedOtp, SECRET_KEY_STRING);
		
		if(storedUser.getOtpExpiredAt().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException(" OTP has been Expired");
		}

		if(storedotp.equals(otp)) {
			return jwtUtil.generateToken(storedUser);
		}
		else {
			throw new IllegalArgumentException(" Wrong OTP !!!!");

		}
	}

}


//		Users users = usersRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
//		
//		authenticationManager.authenticate(
//    			new UsernamePasswordAuthenticationToken(userName, userPassword)
//    			);
//		
//		 final UserDetails userDetails = new org.springframework.security.core.userdetails.User(
//	    	        userName, userPassword, new ArrayList<>()
//	    	    );
//	    	    return jwtUtil.generateToken(users);
