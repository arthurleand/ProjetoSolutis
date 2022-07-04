package com.solutis.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solutis.project.model.VoteModel;

public interface VoteRepository extends JpaRepository<VoteModel, Long>{

}
