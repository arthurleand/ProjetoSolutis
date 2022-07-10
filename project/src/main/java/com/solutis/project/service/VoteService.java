package com.solutis.project.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.solutis.project.model.ScheduleModel;
import com.solutis.project.model.SessionStatus;
import com.solutis.project.model.UserModel;
import com.solutis.project.model.VoteModel;
import com.solutis.project.model.form.VoteForm;
import com.solutis.project.repository.ScheduleRepository;
import com.solutis.project.repository.UserRepository;
import com.solutis.project.repository.VoteRepository;

@Service
public class VoteService {

	@Autowired
	private VoteRepository voteRepository;
	@Autowired
	private ScheduleRepository scheduleRepository;
	@Autowired
	private UserRepository userRepository;

	public Optional<VoteModel> registerVote(@Valid VoteForm voteForm) {
		if (scheduleRepository.findById(voteForm.getFkschedule().getId()).isPresent()) {
			Optional<ScheduleModel> findSchedule = scheduleRepository
					.findById(voteForm.getFkschedule().getId());
			if (findSchedule.get().getSession() == SessionStatus.NEVEROPEN) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"This session never open!");
			}
			if (findSchedule.get().getSession() == SessionStatus.OPEN) {
				if (!voteRepository.findByFkuserIdAndFkscheduleId(voteForm.getFkuser().getId(),
						voteForm.getFkschedule().getId()).isPresent()) {
					Optional<UserModel> user = userRepository.findById(voteForm.getFkuser().getId());
					VoteModel vote = new VoteModel();
					voteForm.getFkuser().setTypeuser(user.get().getTypeuser());
					vote.setVote(voteForm.getVote());
					vote.setFkuser(voteForm.getFkuser());
					vote.setFkschedule(voteForm.getFkschedule());
					return Optional.of(voteRepository.save(vote));
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already voted!");
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This session is closed!");
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule does not exist!");
	}
}
