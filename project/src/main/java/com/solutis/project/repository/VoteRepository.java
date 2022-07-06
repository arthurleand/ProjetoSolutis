package com.solutis.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solutis.project.model.VoteModel;
import com.solutis.project.model.VoteUser;

public interface VoteRepository extends JpaRepository<VoteModel, Long>{
	Optional<VoteModel> findByFkuserIdAndFkscheduleId(Long idUser, Long idSchedule);
	Optional<List<VoteModel>> findAllByFkscheduleIdAndVote(Long idSchedule, VoteUser vote);
}
