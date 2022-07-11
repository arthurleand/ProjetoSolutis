package com.solutis.project.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.solutis.project.model.UserModel;
import com.solutis.project.model.form.UserRegisterForm;
import com.solutis.project.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

		Optional<UserModel> emailValidation = userRepository.findByEmail(userForm.getEmail());
		
		if (emailValidation.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use!");
		}
		
		if (cpfService.validateCpf(userForm.getCpf())) {
			UserModel newUser = new UserModel();
			newUser.setCpf(userForm.getCpf());
			newUser.setEmail(userForm.getEmail());
			newUser.setName(userForm.getName());
			newUser.setPassword(encryptPassword(userForm.getPassword()));
			newUser.setTypeUser(userForm.getTypeUser());
			
			log.info("User {} successfully registered", userForm.getEmail());		
			return ResponseEntity.status(201).body(userRepository.save(newUser));
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CPF!");
	}

	public Optional<UserModel> update(@Valid UserModel user) {
		Optional<UserModel> findUser = userRepository.findById(user.getId());
		
		if (findUser.isPresent()) {
			Optional<UserModel> cpfUsing = userRepository.findByCpf(user.getCpf());

			if (cpfUsing.isPresent()
					&& (!cpfUsing.get().getCpf().equals(user.getCpf()) 
							|| cpfUsing.get().getCpf().equals(user.getCpf()))
					&& !user.getId().equals(cpfUsing.get().getId())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF is already in use!");
			}
			Optional<UserModel> emailValidation = userRepository.findByEmail(user.getEmail());
			
			if (emailValidation.isPresent()
					&& (!emailValidation.get().getEmail().equals(user.getEmail())
							|| emailValidation.get().getEmail().equals(user.getEmail()))
					&& !user.getId().equals(emailValidation.get().getId())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use!");
			}
			
			if (cpfService.validateCpf(user.getCpf())) {
				user.setPassword(encryptPassword(user.getPassword()));
				log.info("User {} successfully updated", user.getEmail());
				return Optional.of(userRepository.save(user));
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CPF!");
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
	}
	
	private static String encryptPassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}

}