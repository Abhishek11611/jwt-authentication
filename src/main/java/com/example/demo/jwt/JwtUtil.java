package com.example.demo.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {

    // Secure key - must be at least 32 characters for HS256
    private static final String SECRET = "my-super-secret-key-that-is-strong-123!";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    


    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private static long expiryDuration = 24L * 60L * 60L; // 24 hours 

    public String generateToken(Users users) {
    	
    	long milliTime = System.currentTimeMillis();
		long expiryTime = milliTime + expiryDuration * 1000;

		Date issuedAt = new Date(milliTime);
		Date expiryAt = new Date(expiryTime);
		
		String issuer = (users.getId() != null) ? users.getId() .toString() : "N/A";
		Claims claims = Jwts.claims().setIssuer(issuer).setIssuedAt(issuedAt).setExpiration(expiryAt);
		
		claims.put("userName", users.getUserName());
		claims.put("userEmail", users.getUserEmail());
		claims.put("userMobileNumber", users.getUserMobileNumber() );
		String token = Jwts.builder().setClaims(claims).signWith(SECRET_KEY, SignatureAlgorithm.HS256)
				.compact();
		
		return token;
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
  
         return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }
}