package com.solutis.project.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.solutis.project.model.UserModel;
import com.solutis.project.model.dto.UserTokenDto;
import com.solutis.project.model.form.UserLoginForm;
import com.solutis.project.model.form.UserRegisterForm;
import com.solutis.project.repository.UserRepository;
import com.solutis.project.service.TokenService;
import com.solutis.project.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private TokenService tokenService;

	@GetMapping
	@Cacheable(value = "cacheUser")
	public ResponseEntity<List<UserModel>> getAll() {
		log.info("Find all users");
		return ResponseEntity.ok(userRepository.findAll());
	}
	
	@GetMapping("/{id}")
	@Cacheable(value = "cacheUser")
	public ResponseEntity<UserModel> getById(@PathVariable Long id){
		log.info("Find user by id: {}", id);
		return userRepository.findById(id)
				.map(ResponseEntity::ok)
		        .orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping("/register")
	@Transactional
	@CacheEvict(value = "cacheUser", allEntries = true)
	public ResponseEntity<UserModel> register(@Valid @RequestBody UserRegisterForm user){
		log.info("Register user");
		return userService.register(user);
	}
	
	@PutMapping("/update")
	@Transactional
	@CacheEvict(value = "cacheUser", allEntries = true)
	public ResponseEntity<UserModel> update(@Valid @RequestBody UserModel user){
		log.info("Update user by id: {}", user.getId());
		return userService.update(user)
				.map(resp -> ResponseEntity.status(HttpStatus.OK)
				.body(resp))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.build());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "cacheUser", allEntries = true)
	public void delete (@PathVariable Long id) {
		Optional<UserModel> user = userRepository.findById(id);
		if(user.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User Not Found!");
		}
		log.info("Deleting user by id: {}",id);
		userRepository.deleteById(id);
	}
	
	@PostMapping("/login")
	@Transactional
	@Cacheable(value = "cacheUser")
	public ResponseEntity<UserTokenDto> login(@RequestBody @Valid UserLoginForm form){
		log.info("Login with: {}", form.getEmail());
		UsernamePasswordAuthenticationToken login = form.convert();	
		try {
			Authentication authentication = authManager.authenticate(login);
			String token = tokenService.generateToken(authentication);
			return ResponseEntity.ok(new UserTokenDto(token, "Bearer"));	
		} catch (AuthenticationCredentialsNotFoundException e) {
			return ResponseEntity.badRequest().build();
		}
					
	}
}
