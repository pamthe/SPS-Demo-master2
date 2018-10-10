CREATE TABLE friendManagement(
Id integer not null, 
email varchar(255) not null, 
friend_list varchar(255), 
subscription varchar(255), 
text_message varchar(255), 
updated_timestamp timestamp, primary key(Id)); 

CREATE TABLE friendManagementHistory(
email varchar(255) not null, 
subscription varchar(255), 
status varchar(255), 
updated_timestamp timestamp); 

