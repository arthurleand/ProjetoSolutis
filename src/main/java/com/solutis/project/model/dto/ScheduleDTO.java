package com.solutis.project.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.solutis.project.model.SessionStatus;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Setter
@Getter
public class ScheduleDTO implements Serializable{
	
	private Long id;
	
	private String scheduleName;
	
	private String description;
	
	@Enumerated(EnumType.STRING)
	private SessionStatus session = SessionStatus.NEVEROPEN;

	private LocalDateTime sessionTime;
	
	private Long sessionMinute;
	
	private String winnerVote;
	
	private Long yesVote;
	
	private Long noVote;
	
	private double yesPercent;
	
	private double noPercent;
}
