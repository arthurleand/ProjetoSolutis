package com.solutis.project.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.solutis.project.model.UserModel;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void deveriaCarregarUmUsuarioPeloCpf() {
		String cpf = "10733686452";
		Optional<UserModel> user = userRepository.findByCpf(cpf);
		assertNotNull(user);
		assertEquals(cpf, user.get().getCpf());
	}
	
	@Test
	public void naodeveriaCarregarUmUsuarioPeloCpf() {
		String cpf = "10733686459";
		Optional<UserModel> user = userRepository.findByCpf(cpf);
		assertTrue(user.isEmpty());
	}

}
