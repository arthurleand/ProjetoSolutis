package com.solutis.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solutis.project.model.ScheduleModel;
import com.solutis.project.model.VoteModel;

public interface VoteRepository extends JpaRepository<VoteModel, Long>{
	Optional<VoteModel> findByFkuserIdAndFkscheduleId(Long idUser, Long idSchedule);
}
