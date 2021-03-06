package com.solutis.project.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.solutis.project.model.ScheduleModel;
import com.solutis.project.model.SessionStatus;
import com.solutis.project.model.VoteModel;
import com.solutis.project.model.VoteUser;

import com.solutis.project.model.form.ScheduleForm;
import com.solutis.project.repository.ScheduleRepository;
import com.solutis.project.repository.VoteRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Profile("prod")
public class ScheduleServiceprod {

	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired 
	private VoteRepository voteRepository;
	
	public ResponseEntity<ScheduleModel> saveSchedule(@Valid ScheduleForm scheduleForm) {
		ScheduleModel schedule = new ScheduleModel();
		schedule.setDescription(scheduleForm.getDescription());
		schedule.setScheduleName(scheduleForm.getScheduleName());
		
		return ResponseEntity.status(201).body(scheduleRepository.save(schedule));
	}
	
	public Optional<ScheduleModel> openSession(@Valid ScheduleModel schedule) {
		Optional<ScheduleModel> findSchedule = scheduleRepository.findById(schedule.getId());

		if (findSchedule.isPresent()) {
			if (findSchedule.get().getSession() == SessionStatus.NEVEROPEN) {
				Timer timerClosed = new Timer();

				schedule.setScheduleName(findSchedule.get().getScheduleName());
				schedule.setDescription(findSchedule.get().getDescription());
				schedule.setSession(SessionStatus.OPEN);
				schedule.setSessionTime(LocalDateTime.now());
				if (schedule.getSessionMinute() == null || schedule.getSessionMinute() == 0) {
					schedule.setSessionMinute(1L);
				}

				LocalDateTime sessionClose = LocalDateTime.now()
						.plusMinutes(schedule.getSessionMinute());

				Date dateTimer = Date.from(sessionClose
						.atZone(ZoneId.systemDefault()).toInstant());

				timerClosed.schedule(new TimerTask() {
					@Override
					public void run() {
						schedule.setSession(SessionStatus.CLOSED);
						scheduleRepository.save(schedule);
					}
				}, dateTimer);

				return Optional.of(scheduleRepository.save(schedule));

			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This session is already open!");
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist!");
	}
	
	public Optional<ScheduleModel> countVoteSchedule(ScheduleModel schedule) {
		if (scheduleRepository.findById(schedule.getId()).isPresent()) {
			Optional<ScheduleModel> findSchedule = scheduleRepository.findById(schedule.getId());

			if (findSchedule.get().getSession() == SessionStatus.CLOSED) {
				Optional<List<VoteModel>> yesVoteList = voteRepository
						.findAllByFkscheduleIdAndVote(schedule.getId(),VoteUser.YES);
				Long yesVote = yesVoteList.get().stream().map(y -> y).count();

				Optional<List<VoteModel>> noVoteList = voteRepository
						.findAllByFkscheduleIdAndVote(schedule.getId(),VoteUser.NO);
				Long noVote = noVoteList.get().stream().map(y -> y).count();

				double yesPercent = (yesVote * 100) / (yesVote + noVote);
				double noPercent = (noVote * 100) / (yesVote + noVote);

				if (yesVote > noVote) {
					schedule.setWinnerVote("YES");
				} else {
					schedule.setWinnerVote("NO");
				}
				if (yesVote == noVote) {
					schedule.setWinnerVote("DRAW");
				}
				
				schedule.setScheduleName(findSchedule.get().getScheduleName());
				schedule.setDescription(findSchedule.get().getDescription());
				schedule.setSessionMinute(findSchedule.get().getSessionMinute());
				schedule.setSessionTime(findSchedule.get().getSessionTime());
				schedule.setSession(SessionStatus.CLOSED);
				schedule.setYesPercent(yesPercent);
				schedule.setNoPercent(noPercent);
				schedule.setYesVote(yesVote);
				schedule.setNoVote(noVote);

				return Optional.of(scheduleRepository.save(schedule));

			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session not closed!");
			
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist!");
	}
	
	@Scheduled(fixedDelay = 60000)
	public void autCount() {
		log.info("Accounting for closed sessions!");
		Optional<List<ScheduleModel>> listSchedule = scheduleRepository
				.findAllBySessionAndWinnerVote(SessionStatus.CLOSED, null);

		listSchedule.get().stream().forEach(s -> {
			Optional<List<VoteModel>> yesVoteList = voteRepository
					.findAllByFkscheduleIdAndVote(s.getId(),VoteUser.YES);
			Long yesVote = yesVoteList.get().stream().map(y -> y).count();

			Optional<List<VoteModel>> noVoteList = voteRepository
					.findAllByFkscheduleIdAndVote(s.getId(), VoteUser.NO);
			Long noVote = noVoteList.get().stream().map(y -> y).count();

			double yesPercent = (yesVote * 100) / (yesVote + noVote);
			double noPercent = (noVote * 100) / (yesVote + noVote);

			if (yesVote > noVote) {
				s.setWinnerVote("YES");
			} else {
				s.setWinnerVote("NO");
			}
			if (yesVote == noVote) {
				s.setWinnerVote("DRAW");
			}

			s.setSessionMinute(s.getSessionMinute());
			s.setSessionTime(s.getSessionTime());
			s.setSession(SessionStatus.CLOSED);
			s.setYesPercent(yesPercent);
			s.setNoPercent(noPercent);
			s.setYesVote(yesVote);
			s.setNoVote(noVote);
			
			scheduleRepository.save(s);
		});
	}
}
