package com.solutis.project.service;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.solutis.project.config.external.CpfService;
import com.solutis.project.model.UserModel;
import com.solutis.project.model.form.CpfValidationForm;
import com.solutis.project.model.form.UserRegisterForm;
import com.solutis.project.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CpfService cpfService;
	@Value("${forum.jwt.expiration}")
	private String expiration;
	@Value("${forum.jwt.secret}")
	private String secret;
	
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

	public String generateToken(Authentication authentication) {
		UserModel user = (UserModel) authentication.getPrincipal();
		Date nowDate = new Date();
		Date dateExpiration = new Date(nowDate.getTime()+ Long.parseLong(expiration));
		return Jwts.builder()
				.setIssuer("API Solutis")
				.setSubject(user.getId().toString())
				.setIssuedAt(nowDate)
				.setExpiration(dateExpiration)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

	public boolean isValidToken(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}	
	}

	public Long getIdUser(String token) {
		Claims clain = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long.parseLong(clain.getSubject());
		
	}

}