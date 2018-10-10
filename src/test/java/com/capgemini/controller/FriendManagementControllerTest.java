package com.capgemini.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;

import com.capgemini.exceptionhandling.ResourceNotFoundException;
import com.capgemini.model.Subscriber;
import com.capgemini.repository.FriendMangmtRepo;
import com.capgemini.service.FrientMangmtService;
import com.capgemini.validation.FriendManagementValidation;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FriendManagementControllerTest {
	
	private FriendManagementController friendManagementController;
	private Subscriber subscriber;
	@Mock
	private BindingResult result;
	@Mock 
	FriendManagementValidation fmError;
	@Mock 
	FrientMangmtService frndMngtServc;
	@Mock 
	FriendMangmtRepo friendMangmtRepo;
	@Mock
	JdbcTemplate jdbcTemplate;

	
	 @Before
	    public void setUp() throws Exception {
		 subscriber=new Subscriber();
		 FriendMangmtRepo friendMangmtRepo=new FriendMangmtRepo(fmError,jdbcTemplate);
		 frndMngtServc=new FrientMangmtService(friendMangmtRepo);
		 friendManagementController=new FriendManagementController(frndMngtServc,fmError);
	 }
	
	@Test
	public void test_null_requestor_unsubscribe() throws ResourceNotFoundException {
		subscriber.setTarget("ravi@gmail.com");
		subscriber.setRequestor(null);
		when(this.result.hasErrors()).thenReturn(false);
		ResponseEntity<FriendManagementValidation> responseEntity=friendManagementController.unSubscribeFriend(subscriber, result);
		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void test_null_subscriber_unsubscribe() throws ResourceNotFoundException {
		when(this.result.hasErrors()).thenReturn(false);
		subscriber.setRequestor("ravi@gmail.com");
		subscriber.setTarget(null);
		ResponseEntity<FriendManagementValidation> responseEntity=friendManagementController.unSubscribeFriend(subscriber, result);
		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void test_samesubreq_unsubscribe() throws ResourceNotFoundException {
		subscriber.setRequestor("ravi@gmail.com");
		subscriber.setTarget("ravi@gmail.com");
		when(this.result.hasErrors()).thenReturn(false);
		ResponseEntity<FriendManagementValidation> responseEntity=friendManagementController.unSubscribeFriend(subscriber, result);
		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void test_unsubscribe_success() throws ResourceNotFoundException {
		subscriber.setRequestor("ravi@gmail.com");
		subscriber.setTarget("arvi@gmail.com");
		when(this.result.hasErrors()).thenReturn(false);
		List<Object> obj=new ArrayList<Object>();
		String subscribers="ravi@gmail.com,arvi@gmail.com";
		when(this.jdbcTemplate.queryForObject("",obj.toArray(),String.class)).thenReturn(subscribers);
		ResponseEntity<FriendManagementValidation> responseEntity=friendManagementController.unSubscribeFriend(subscriber,result);
		assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
	}
}
