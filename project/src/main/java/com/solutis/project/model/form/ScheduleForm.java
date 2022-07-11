package com.solutis.project.model.form;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class ScheduleForm {
	
	@NotBlank
	private String scheduleName;
	@NotBlank
	private String description;

}
