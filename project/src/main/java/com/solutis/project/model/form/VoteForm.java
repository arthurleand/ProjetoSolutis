package com.solutis.project.model.form;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.solutis.project.model.ScheduleModel;
import com.solutis.project.model.UserModel;
import com.solutis.project.model.VoteUser;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteForm {
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private VoteUser vote;
	
	@NotNull
	private ScheduleModel fkschedule;
	
	@NotNull
	private UserModel fkuser;
}
