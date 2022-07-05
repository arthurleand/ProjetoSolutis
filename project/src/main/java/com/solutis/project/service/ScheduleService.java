package com.solutis.project.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.solutis.project.model.ScheduleModel;
import com.solutis.project.model.SessionStatus;
import com.solutis.project.repository.ScheduleRepository;

@Service
public class ScheduleService {

	@Autowired
	private ScheduleRepository scheduleRepository;

	public Optional<ScheduleModel> openSession(@Valid ScheduleModel schedule) {
		if (scheduleRepository.findById(schedule.getId()).isPresent()) {
			Optional<ScheduleModel> findSchedule = scheduleRepository.findById(schedule.getId());
			if (findSchedule.isPresent()) {
				if (findSchedule.get().getSession() == SessionStatus.NEVEROPEN) {
					Timer timerClosed = new Timer();
					
					schedule.setSession(SessionStatus.OPEN);
					schedule.setSessionTime(LocalDateTime.now());
					if(schedule.getSessionMinute()==null) {
						schedule.setSessionMinute(1L);
					}
					
					LocalDateTime sessionClose = LocalDateTime.now()
							.plusMinutes(schedule.getSessionMinute());
					System.out.println(sessionClose);
					
					Date dateTimer = Date.from(sessionClose.atZone(ZoneId.systemDefault())
							.toInstant());
					System.out.println(dateTimer);
					
					timerClosed.schedule(new TimerTask() {
						@Override
						public void run() {
							System.out.println("Sessão Fechada as: "+LocalDateTime.now());
							schedule.setSession(SessionStatus.CLOSED);
							scheduleRepository.save(schedule);
						}
					}, dateTimer);
					
					return Optional.of(scheduleRepository.save(schedule));

				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This session is already open!");
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist!");
	}

}
