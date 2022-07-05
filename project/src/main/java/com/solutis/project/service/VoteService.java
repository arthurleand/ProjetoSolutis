package com.solutis.project.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.solutis.project.model.ScheduleModel;
import com.solutis.project.model.UserModel;
import com.solutis.project.model.VoteModel;
import com.solutis.project.model.form.VoteForm;
import com.solutis.project.repository.ScheduleRepository;
import com.solutis.project.repository.VoteRepository;

@Service
public class VoteService {

	@Autowired
	private VoteRepository voteRepository;

	@Autowired
	private ScheduleRepository scheduleRepository;

	public Optional<VoteModel> registerVote(@Valid VoteForm voteForm) {
		if (voteForm.getFkschedule().getSessionTime()
				.isBefore(voteForm.getFkschedule().getSessionTime().plusMinutes(3))) {
			if (!voteRepository.findByFkuserIdAndFkscheduleId(voteForm.getFkuser().getId(), 
					voteForm.getFkschedule().getId()).isPresent()) {
				System.out.println(voteForm.getFkuser().getName());
				VoteModel vote = new VoteModel();
				vote.setVote(voteForm.getVote());
				vote.setFkuser(voteForm.getFkuser());
				vote.setFkschedule(voteForm.getFkschedule());
				return Optional.of(voteRepository.save(vote));
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already voted!");
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This session is closed");
/*		Optional<ScheduleModel> schedule = scheduleRepository.findById(voteForm.getFkschedule().getId());
		if(schedule.isPresent()){
			VoteModel vote = new VoteModel();
			vote.setVote(voteForm.getVote());
			vote.setFkuser(voteForm.getFkuser());
			vote.setFkschedule(voteForm.getFkschedule());
			if(schedule.get().getVote().stream().filter(s -> s.getFkuser().getId()== vote.getFkuser().getId()) == null){
				return Optional.of(voteRepository.save(vote));
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already voted!");
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Schedule dont exist!");
	*/
//		return null;
	}	
}
