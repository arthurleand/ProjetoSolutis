package com.solutis.project.config.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.solutis.project.model.UserModel;
import com.solutis.project.repository.UserRepository;
import com.solutis.project.service.UserService;

public class AuthenticationTokenFilter extends OncePerRequestFilter{

	private UserService userService;
	private UserRepository userRepository;
	
	
	public AuthenticationTokenFilter(UserService userService, UserRepository userRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {		
		
		String token = recoverToken(request);
		boolean valid = userService.isValidToken(token);
		if (valid) {
			authenticationClient(token);
		}
		
		filterChain.doFilter(request, response);
	}

	private void authenticationClient(String token) {
		Long idUser = userService.getIdUser(token);
		UserModel user = userRepository.findById(idUser).get();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String recoverToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
		return null;
		}
		return token.substring(7, token.length());
	}

}
