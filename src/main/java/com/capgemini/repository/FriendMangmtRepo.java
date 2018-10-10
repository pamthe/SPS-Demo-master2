package com.capgemini.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.capgemini.validation.FriendManagementValidation;
import com.capgemini.exceptionhandling.ResourceNotFoundException;
import com.capgemini.model.UserEmail;
import com.capgemini.model.UserFriandsListResponse;

@Repository
public class FriendMangmtRepo {

	//@Autowired
	FriendManagementValidation fmError;

	//@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired public FriendMangmtRepo(FriendManagementValidation fmError,JdbcTemplate jdbcTemplate) {
		this.fmError=fmError;
		this.jdbcTemplate=jdbcTemplate;
	}

	//public FriendManagementValidation addNewRequest(String requestor , String target){
	
			public boolean addNewFriendConnection(com.capgemini.model.UserRequest userReq){
			List<UserEmail> friends = userReq.getFriends();
			
			String requestor = friends.get(0).getEmail();
			String target = friends.get(1).getEmail();
				
				System.out.println("In addNewRequest");
			/*CREATE TABLE friendManagement(
					Id integer not null, 
					email varchar(255) not null, 
					friend_list varchar(255), 
					subscription varchar(255), 
					text_message varchar(255), 
					updated_timestamp timestamp, primary key(Id));*/
			
			//--check if email1 and email2 is alraedy present
			
			
			// if not then add email1 and email2
			
			       
	        String query = "SELECT email FROM friendmanagement";
	        
	        List<String> emails =jdbcTemplate.queryForList(query,String.class);
	        
	       // String sql = "SELECT * FROM CUSTOMER WHERE CUST_ID = ?";
	        int result;
	        if(emails.contains(requestor) && emails.contains(target)){
	        	//
	/*        	String requestorfriendListSQL = "SELECT friend_list FROM friendmanagement where email=?";
	        	String requestorFriends = (String) jdbcTemplate.queryForObject(
	        			requestorfriendListSQL, new Object[] { requestor }, String.class);
	        	String[] reqFriends = requestorFriends.split(",");
	        	ArrayList reqFriendsAL = new ArrayList(Arrays.asList(reqFriends));
	        	
	        	//
	        	String targetfriendListSQL = "SELECT friend_list FROM friendmanagement where email=?";
	        	String targetFriends = (String) jdbcTemplate.queryForObject(
	        			targetfriendListSQL, new Object[] { target }, String.class);
	        	String[] tarFriends = targetFriends.split(",");
	        	ArrayList tarFriendsAL = new ArrayList(Arrays.asList(tarFriends));
	        	
	        	if(reqFriendsAL.size()==0 && tarFriendsAL.size()==0){
	*/        		connectFriends(requestor, target);
	/*        	}else if(reqFriendsAL.size()>0 && tarFriendsAL.size()>0){
	        		
	        	}else{
	        		
	        	}
	*/        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        }else if(emails.contains(requestor)){
	        	
	        	insertNewFriend(requestor, target);
	        	
	        }else{
	        	
	        }
	        
	        return true;
			
		}

	public FriendManagementValidation subscribeTargetFriend(com.capgemini.model.Subscriber subscriber)throws ResourceNotFoundException {
		String requestor = subscriber.getRequestor();
		String target = subscriber.getTarget();


		String query = "SELECT email FROM friendmanagement";

		List<String> emails =jdbcTemplate.queryForList(query,String.class);

		if(emails.contains(target) && emails.contains(requestor)) {
			String sql = "SELECT subscription FROM friendmanagement WHERE email=?";

			String subscribers = (String) jdbcTemplate.queryForObject(
					sql, new Object[] { requestor }, String.class);



			int result;
			if(subscribers.isEmpty()) {
				result = jdbcTemplate.update("update friendmanagement " + " set subscription = ? " + " where email = ?",
						new Object[] {
								target, requestor
				});
			}else {

				String[] subs = subscribers.split(",");
				ArrayList al = new ArrayList(Arrays.asList(subs));
				System.out.println("al "+al);
				if(!al.contains(target)) {
					target= subscribers +", "+ target;
					result = jdbcTemplate.update("update friendmanagement " + " set subscription = ? " + " where email = ?",
							new Object[] {
									target, requestor
					});
				}else {
					fmError.setStatus("Failed");
					fmError.setErrorDescription("Target already subscribed");
					return fmError;     
				}
			}
			//		String[] subscriberList = subscribers.split(",");






			if(result==1) {
				fmError.setStatus("Success");
				//fmError.setErrorDescription("Subscribed successfully");
				return fmError;
			}else {
				fmError.setStatus("Failed");
				fmError.setErrorDescription("");
				return fmError;
			}
		}else {
			fmError.setStatus("Failed");
			fmError.setErrorDescription("Check Target or Requestor email id");
			return fmError;
		}
	}

	
	public FriendManagementValidation unSubscribeTargetFriend(com.capgemini.model.Subscriber subscriber)throws ResourceNotFoundException {
		String requestor = subscriber.getRequestor();
		String target = subscriber.getTarget();
		
		String sql = "SELECT subscription FROM friendmanagement WHERE email=?";

		String subscribers = (String) jdbcTemplate.queryForObject(
				sql, new Object[] { requestor }, String.class);


		int result;
		if(subscribers==null || subscribers.isEmpty()) {
			//throw some error
			fmError.setStatus("Failed");
			fmError.setErrorDescription("");
			return fmError;
		}else {

			String[] subs = subscribers.split(",");
			ArrayList al = new ArrayList(Arrays.asList(subs));
			System.out.println("al "+al);
			if(al.contains(target)) {
			//check whether they are friends
			String friendListQuery = "SELECT friend_list FROM friendmanagement WHERE email=?";

			String friends = (String) jdbcTemplate.queryForObject(
					friendListQuery, new Object[] { requestor }, String.class);
			if(!friends.isEmpty()) {
				String[] frds = friends.split(",");
				ArrayList friendList = new ArrayList(Arrays.asList(friends));
				System.out.println("friendList"+friendList);
				if(friendList.contains(target)) {
					//unsubscribe
					al.remove(target);
					//prepare csv
					String joinedString = al.toString().replaceAll("[\\[.\\]]", "");
					System.out.println("listString ="+joinedString);
					//update db
					result = jdbcTemplate.update("update friendmanagement " + " set subscription = ? " + " where email = ?",
							new Object[] {
									joinedString, requestor
									
					
									
					});
					//Now add entry in history table
					updateHistoryTable(requestor,target,"removed");
					if(result==1) {
						fmError.setStatus("Success");
						fmError.setErrorDescription("UnSubscribed successfully");
						return fmError;
					}else {
						fmError.setStatus("Failed");
						fmError.setErrorDescription("");
						return fmError;
					}
				}
				
			}
			}
		}
		
		
		/*String friendListQuery = "SELECT friend_list FROM friendmanagement where requestor='"+requestor+"'";
		List<String> friendList =jdbcTemplate.queryForList(friendListQuery,String.class);
		for(String friend:friendList)
			System.out.println(friend);
		if(friendList.contains(target)) {
			//unsubscribe
			 String subscriptionQuery = "SELECT subscription FROM friendmanagement where requestor='"+requestor+"'";
			 List<String> subscriptionList=jdbcTemplate.queryForList(subscriptionQuery,String.class);
			 if(subscriptionList.contains(target)) {
				 subscriptionList.remove(target);
				 int result = jdbcTemplate.update("update friendmanagement " + " set subscription = ? " + " where email = ?",
							new Object[] {
									subscriptionList, requestor
					});
			 }
		}*/
		
		return fmError;
	}
	
	
	
	public UserFriandsListResponse retrieveFriendsEmails(String email) throws ResourceNotFoundException {	
	
		UserFriandsListResponse emailListresponse = new UserFriandsListResponse();
		emailListresponse.setStatus("success");
		emailListresponse.setCount(new Integer(2));
		emailListresponse.getFriends().add("som1@gmail.com");
		emailListresponse.getFriends().add("som2@gmail.com");
		System.out.println("########## " +emailListresponse.getStatus());
		System.out.println("########## " +emailListresponse.getCount());
		System.out.println("########## " +emailListresponse.getFriends().get(0));
		System.out.println("########## " +emailListresponse.getFriends().get(1));
		return emailListresponse;
		
	}
	
	private void insertNewFriend(String requestor, String target){
		
		System.out.println("in request");
    	String sql = "INSERT into friendmanagement(Id, email, friend_list, subscription, text_message, updated_timestamp) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {
			

			@Override
			public void setValues(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, 1009);
                stmt.setString(2, target);
                stmt.setString(3, requestor);
                stmt.setString(4, "");
                stmt.setString(5, "");
                stmt.setTimestamp(6, new java.sql.Timestamp(new Date().getTime()));
				
			}
        });
        
         jdbcTemplate.update("update friendmanagement " + " set friend_list = ?" + " where email = ?",
				new Object[] {
						target, requestor
		 });
	}

	
	private void updateHistoryTable(String requestor, String target,String status) {
		System.out.println("in request");
    	String sql = "INSERT into friendmanagementhistory(email, subscription, status, updated_timestamp) VALUES (?, ?, ?, ?, )";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement stmt) throws SQLException {
                stmt.setString(1, requestor);
                stmt.setString(2, target);
                stmt.setString(3, status);
                stmt.setTimestamp(4, new java.sql.Timestamp(new Date().getTime()));
				
			}
        });
	}
	
 private void connectFriends(String requestor, String target){
	 int result;
	 result = jdbcTemplate.update("update friendmanagement " + " set friend_list = ?" + " where email = ?",
				new Object[] {
						target, requestor
 	});
 	if(result==1){
 	   result = jdbcTemplate.update("update friendmanagement " + " set friend_list = ?" + " where email = ?",
				new Object[] {
						requestor, target
 	   });
 	}
 }
	
	
	

}
