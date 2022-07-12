package com.solutis.project.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solutis.project.model.VoteModel;
import com.solutis.project.model.form.VoteForm;
import com.solutis.project.service.VoteService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/vote")
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VoteController {
	
	@Autowired
	private VoteService voteService;
	
	@PostMapping
	@Transactional
	public ResponseEntity<VoteModel> vote(@RequestBody @Valid VoteForm voteForm){
	   log.info("Registring vote by userId= {} in scheduleId: {}",
			   voteForm.getFkuser().getId(),
			   voteForm.getFkschedule().getId());
		return voteService.registerVote(voteForm)
			   	.map(resp -> ResponseEntity.status(HttpStatus.OK)
				.body(resp))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.build());
	}
	
}
