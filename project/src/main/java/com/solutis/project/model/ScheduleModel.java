package com.solutis.project.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedule")
public class ScheduleModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String scheduleName;
	
	@NotBlank
	private String description;
	
	@OneToMany(mappedBy = "fkschedule")
	@JsonIgnoreProperties(value = "fkschedule")
	private List<VoteModel> vote;
	
	@Enumerated(EnumType.STRING)
	private SessionStatus session = SessionStatus.NEVEROPEN;

	private LocalDateTime sessionTime;
	
	private Long sessionMinute;
	
	private String winnerVote;
	
	private Long yesVote;
	
	private Long NoVote;
	
	private double yesPercent;
	
	private double noPercent;
}
