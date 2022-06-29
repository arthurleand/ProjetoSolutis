package com.solutis.project.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solutis.project.model.ScheduleModel;
import com.solutis.project.repository.ScheduleRepository;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
	
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@PostMapping
	public ResponseEntity<ScheduleModel> createSchedule(@RequestBody @Valid ScheduleModel schedule){
		return ResponseEntity.status(HttpStatus.CREATED).body(scheduleRepository.save(schedule));
	}
	
}
