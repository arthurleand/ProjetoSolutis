package com.solutis.project.model.form;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.solutis.project.model.UserType;

import lombok.Getter;

@Getter
public class UserRegisterForm {

	@NotBlank
	private String name;

	@NotBlank
	private String cpf;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	@Size(min = 6, max = 8)
	private String password;

	@NotNull
	@Enumerated(EnumType.STRING)
	private UserType typeUser;

}
