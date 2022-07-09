package com.solutis.project.model.form;

import javax.validation.constraints.NotBlank;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.Getter;

@Getter
public class UserLoginForm {
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String password;
	
	public UsernamePasswordAuthenticationToken convert() {
		return new UsernamePasswordAuthenticationToken(email, password);
	}
}
