package com.solutis.project.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.solutis.project.model.ScheduleModel;
import com.solutis.project.repository.ScheduleRepository;

@Service
public class ScheduleService {
	
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	public Optional<ScheduleModel> openSession(@Valid ScheduleModel schedule) {
		if(scheduleRepository.findById(schedule.getId()).isPresent()){
			Optional<ScheduleModel> findSchedule = scheduleRepository.findById(schedule.getId());
			if(findSchedule.isPresent()) {
				if(findSchedule.get().getSession()== false) {
					schedule.setSession(true);
					schedule.setSessionTime(LocalDateTime.now());
					return Optional.of(scheduleRepository.save(schedule));
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This session is already open!");
			}
	}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist!");
	}

}
