package com.solutis.project.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.solutis.project.config.external.CpfService;
import com.solutis.project.model.UserModel;
import com.solutis.project.model.form.CpfValidationForm;
import com.solutis.project.model.form.UserRegisterForm;
import com.solutis.project.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CpfService cpfService;
	
	public ResponseEntity<UserModel> register(@Valid UserRegisterForm userForm) {
		Optional<UserModel> user = userRepository.findByCpf(userForm.getCpf());
		if (user.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF is already in use!");
		}
		 else {
			CpfValidationForm cpfValidation = cpfService.CpfValidation(userForm.getCpf());
			if(cpfValidation.getIsValid()) {
			UserModel newUser = new UserModel();
			newUser.setCpf(userForm.getCpf());
			newUser.setEmail(userForm.getEmail());
			newUser.setName(userForm.getName());
			newUser.setPassword(userForm.getPassword());
			newUser.setTypeUser(userForm.getTypeUser());

			return ResponseEntity.status(201).body(userRepository.save(newUser));
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CPF!");
		}
	}

	public Optional<UserModel> update(@Valid UserModel user) {
		if (userRepository.findById(user.getId()).isPresent()) {
			Optional<UserModel> findUser = userRepository.findById(user.getId());
			if (findUser.isPresent())
				return Optional.of(userRepository.save(user));
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
	}

}