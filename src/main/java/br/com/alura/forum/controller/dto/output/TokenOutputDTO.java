package br.com.alura.forum.controller.dto.output;

import lombok.Getter;

@Getter
public class TokenOutputDTO {

	 private String tokenType;
	    private String token;

	    public TokenOutputDTO(String type, String token) {
	        this.tokenType = type;
	        this.token = token;
	    }
}
