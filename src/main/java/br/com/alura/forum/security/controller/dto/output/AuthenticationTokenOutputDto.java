package br.com.alura.forum.security.controller.dto.output;

public class AuthenticationTokenOutputDto {

	private String tokenType;
	private String token;

	public AuthenticationTokenOutputDto(String tokenType, String token) {
		this.tokenType = tokenType;
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public String getTokenType() {
		return tokenType;
	}

}
