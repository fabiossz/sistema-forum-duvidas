package br.com.alura.forum.controller.dto.input;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class LoginInputDTO {

	private String email;
    private String password;

    public UsernamePasswordAuthenticationToken tokenCredentials() {
        return new UsernamePasswordAuthenticationToken(this.email, this.password);
    }
}
