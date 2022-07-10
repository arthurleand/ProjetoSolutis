package com.solutis.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solutis.project.model.ScheduleModel;
import com.solutis.project.model.SessionStatus;

public interface ScheduleRepository extends JpaRepository<ScheduleModel, Long>{
	
	Optional<List<ScheduleModel>> findAllBySession(SessionStatus session);
}
