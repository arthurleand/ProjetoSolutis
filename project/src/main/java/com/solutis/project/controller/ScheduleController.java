package com.solutis.project.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solutis.project.model.ScheduleModel;
import com.solutis.project.repository.ScheduleRepository;
import com.solutis.project.service.ScheduleService;

import lombok.extern.java.Log;

@RestController
@RequestMapping("/schedule")
@EnableScheduling
@Log
public class ScheduleController {
	
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@GetMapping
	public ResponseEntity<List<ScheduleModel>> getAll() {
		return ResponseEntity.ok(scheduleRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ScheduleModel> getById(@PathVariable Long id){
		return scheduleRepository.findById(id)
				.map(resp -> ResponseEntity.ok(resp))
		        .orElse(ResponseEntity.notFound().build());
	}
	@PostMapping
	@Transactional
	public ResponseEntity<ScheduleModel> createSchedule(@RequestBody @Valid ScheduleModel schedule){
		return ResponseEntity.status(HttpStatus.CREATED).body(scheduleRepository.save(schedule));
	}
	
	@PutMapping("/session")
	@Transactional
	public ResponseEntity<ScheduleModel> createSessions(@Valid @RequestBody ScheduleModel schedule){
		return scheduleService.openSession(schedule)
				.map(resp -> ResponseEntity.status(HttpStatus.OK)
				.body(resp))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.build());
		
	}
	
	@PutMapping("/count")
	@Transactional
	public ResponseEntity<ScheduleModel> count(@RequestBody ScheduleModel schedule) {
		return scheduleService.countVoteSchedule(schedule)
				.map(resp -> ResponseEntity.status(HttpStatus.OK)
				.body(resp))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.build());
	}
	
//	@Scheduled(fixedDelay = 60000)
	void automaticCount(){
		log.info("Accounting for closed sessions!");
		scheduleService.autCount();
	}
}
