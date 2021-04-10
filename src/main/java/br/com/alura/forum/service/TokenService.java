package br.com.alura.forum.service;

import br.com.alura.forum.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {

	@Value("${auth.token.expiration}")
	private long expirationTimeInMilliseconds;

	@Value("${auth.token.key}")
	private String passwordKey;
	private Optional<String> token;

	public String createToken(User user) {

		long now = new Date().getTime();
		Date expirationTime = new Date(now + expirationTimeInMilliseconds);

		String token = Jwts.builder().setExpiration(expirationTime).setSubject(user.getId().toString())
				.setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, passwordKey).compact();
		return token;
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(this.passwordKey).parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException ex) {
			return false;
		}
	}

	public Long getUserIdFromToken(String token) {
		Claims body = Jwts.parser().setSigningKey(this.passwordKey).parseClaimsJws(token).getBody();
		return Long.parseLong(body.getSubject());
	}
}