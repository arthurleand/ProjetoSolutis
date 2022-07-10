package com.solutis.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.solutis.project.repository.ScheduleRepository;
import com.solutis.project.repository.UserRepository;
import com.solutis.project.repository.VoteRepository;

@SpringBootTest
@AutoConfigureMockMvc
class VoteServiceTest {
	
//	VoteForm vote;
//	
//	UserModel user;
//	
//	ScheduleModel schedule;
	
	@MockBean
	VoteRepository voteRepository;
	@MockBean
	ScheduleRepository scheduleRepository;
	@MockBean
	UserRepository userRepository;
	
	@Autowired
	VoteService voteService;
	
//	@BeforeEach
//	public void setup() {
//		schedule = new ScheduleModel();
//		schedule.setId(1L);
//		user= new UserModel();
//		user.setId(1L);
//		vote = new VoteForm(VoteUser.YES,schedule,user);
//	}
	
//	@Test
//	void deveComputarVoto() {
//		String scheduleid = "id-mock";
//		
//		voteService.registerVote(vote);
//		
//		Mockito.when(scheduleRepository
//				.findById(ArgumentMatchers
//						.eq(scheduleid))
//		.thenReturn(null);
//	}

}
