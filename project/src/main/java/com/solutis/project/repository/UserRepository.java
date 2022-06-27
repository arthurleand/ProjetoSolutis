package com.solutis.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.solutis.project.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long>{
	
	public Optional<UserModel> findByCpf(String cpf);
}
