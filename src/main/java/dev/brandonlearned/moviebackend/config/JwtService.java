package dev.brandonlearned.moviebackend.config;

import java.security.Key; 
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${spring.application.security.jwt.secret-key}")
	private String secretKey;
	@Value("${spring.application.security.jwt.expiration}")
	private long jwtExpiration;
	@Value("${spring.application.security.jwt.refresh-token.expiration}")
	private long refreshExpiration;
	
	//extracts username from token
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	//extracts single claim
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	//TOKEN GENERATION METHODS
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}
	
	public String generateToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails
	) {
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}
	
	public String generateRefreshToken(
			UserDetails userDetails
	) {
		return buildToken(new HashMap<>(), userDetails, refreshExpiration);
	}
	
	private String buildToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails,
			long expiration
	) {
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis())) //sets start time
				.setExpiration(new Date(System.currentTimeMillis() + expiration)) //sets expirations
				.signWith(getSignInKey(), SignatureAlgorithm.HS256) //creates key with hashed signature for token
				.compact(); //generate and return token
	}
	
	//validate if token belongs to userDetails and is not expired
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}
	
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date()); //ensure date returns is before today's date
	}
	
	private Date extractExpiration(String token) {
		//extracts expiration date object from token
		return extractClaim(token, Claims::getExpiration);
	}
	
	//extracts all claims
	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
