package com.solutis.project.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/schedule")
@Slf4j
public class ScheduleController {
	
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@GetMapping
	@Cacheable(value = "cacheSchedule")
	public ResponseEntity<List<ScheduleModel>> getAll() {
		log.info("Find all schedules");
		return ResponseEntity.ok(scheduleRepository.findAll());
	}
	
	@GetMapping("/{id}")
	@Cacheable(value = "cacheSchedule")
	public ResponseEntity<ScheduleModel> getById(@PathVariable Long id){
		log.info("Find schedule by id: {}", id);
		return scheduleRepository.findById(id)
				.map(ResponseEntity::ok)
		        .orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	@Transactional
	@CacheEvict(value = "cacheSchedule", allEntries = true)
	public ResponseEntity<ScheduleModel> createSchedule(@RequestBody @Valid ScheduleModel schedule){
		log.info("Create schedule");
		return ResponseEntity.status(HttpStatus.CREATED).body(scheduleRepository.save(schedule));
	}
	
	@PutMapping("/session")
	@Transactional
	@CacheEvict(value = "cacheSchedule", allEntries = true)
	public ResponseEntity<ScheduleModel> createSessions(@Valid @RequestBody ScheduleModel schedule){
		log.info("Open session for {} minute for schedule id: {}",
				schedule.getSessionMinute(),schedule.getId());
		return scheduleService.openSession(schedule)
				.map(resp -> ResponseEntity.status(HttpStatus.OK)
				.body(resp))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.build());
		
	}
	
	@PutMapping("/count")
	@Transactional
	@CacheEvict(value = "cacheSchedule", allEntries = true)
	public ResponseEntity<ScheduleModel> count(@RequestBody ScheduleModel schedule) {
		log.info("Forcing schedule accounting for id: {}", schedule.getId());
		return scheduleService.countVoteSchedule(schedule)
				.map(resp -> ResponseEntity.status(HttpStatus.OK)
				.body(resp))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.build());
	}
}
