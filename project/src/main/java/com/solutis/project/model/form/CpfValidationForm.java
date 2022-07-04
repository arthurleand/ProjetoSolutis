package com.solutis.project.model.form;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class CpfValidationForm {
	
	private String cpf;
	
	private Boolean isValid;
}
