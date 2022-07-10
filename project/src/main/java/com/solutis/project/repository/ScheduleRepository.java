package com.solutis.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solutis.project.model.ScheduleModel;

public interface ScheduleRepository extends JpaRepository<ScheduleModel, Long>{
}
