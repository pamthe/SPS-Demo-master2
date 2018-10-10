package com.capgemini.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.exceptionhandling.ResourceNotFoundException;
import com.capgemini.model.Subscriber;
import com.capgemini.model.UserFriandsListResponse;
import com.capgemini.repository.FriendMangmtRepo;
import com.capgemini.validation.FriendManagementValidation;

@Service
public class FrientMangmtService {

	FriendMangmtRepo friendMangmtRepo;
	
	@Autowired public FrientMangmtService(FriendMangmtRepo friendMangmtRepo) {
		this.friendMangmtRepo=friendMangmtRepo;
	}

	public boolean addNewFriendConnection(com.capgemini.model.UserRequest userReq) {
		System.out.println("-------333333-------------");
		boolean flag = friendMangmtRepo.addNewFriendConnection(userReq);
		return flag;
	}

	public FriendManagementValidation subscribeTargetFriend(com.capgemini.model.Subscriber subscriber)throws ResourceNotFoundException {

		//System.out.println("-------2222-------------");
		return friendMangmtRepo.subscribeTargetFriend(subscriber);

	}
	
	
	public UserFriandsListResponse retrieveFriendsEmails(String email) throws ResourceNotFoundException {
		
		return friendMangmtRepo.retrieveFriendsEmails(email);
	}

	public FriendManagementValidation unSubscribeTargetFriend(Subscriber subscriber) throws ResourceNotFoundException {
		// TODO Auto-generated method stub
		return friendMangmtRepo.unSubscribeTargetFriend(subscriber);
	}

}
