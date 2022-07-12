package com.solutis.project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserTokenDto {
	
	private String token;
	private String type;
	
}
