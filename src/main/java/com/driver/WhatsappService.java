package com.driver;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class WhatsappService
{


    WhatsappRepository whatsappRepository=new WhatsappRepository();

    public String createUser(String name, String mobile) throws Exception {
        //If the mobile number exists in database, throw "User already exists" exception

        HashSet<String>userMobile=whatsappRepository.getUserMobile();
        if(userMobile.contains(mobile))
        {
            throw new Exception("User already exists");
        }
        //Otherwise, create the user and return "SUCCESS"

           User user=new User(name,mobile);

        userMobile.add(mobile);


        whatsappRepository.getUserName().add(name);


        whatsappRepository.getUserList().add(user);
        return "SUCCESS";

    }

    public Group createGroup(List<User> users){
        // The list contains at least 2 users where the first user is the admin. A group has exactly one admin.
        // If there are only 2 users, the group is a personal chat and the group name should be kept as the name of the second user(other than admin)
        // If there are 2+ users, the name of group should be "Group count". For example, the name of first group would be "Group 1", second would be "Group 2" and so on.
        // Note that a personal chat is not considered a group and the count is not updated for personal chats.
        // If group is successfully created, return group.

        //For example: Consider userList1 = {Alex, Bob, Charlie}, userList2 = {Dan, Evan}, userList3 = {Felix, Graham, Hugh}.
        //If createGroup is called for these userLists in the same order, their group names would be "Group 1", "Evan", and "Group 2" respectively.

       Group group;
        int usercount=users.size();

        String groupName;
        User admin=users.get(0);

       if(usercount==2)
       {
           groupName=users.get(1).getName();
       }
       else
       {
           int prevCustomGroupcount = whatsappRepository.getCustomGroupCount() ;
           whatsappRepository.setCustomGroupCount(prevCustomGroupcount+1);
           groupName = "Group "+whatsappRepository.getCustomGroupCount();
       }

        group=new Group(groupName, usercount);

        //updating groupUserMap

        HashMap groupUserMap=whatsappRepository.getGroupUserMap();
        groupUserMap.put(group, users);

        //updating adminMap
        HashMap adminMap=whatsappRepository.getAdminMap();
        adminMap.put(group, admin);

        return group;
    }

    public int createMessage(String content){
        // The 'i^th' created message has message id 'i'.
        // Return the message id.
        int id= whatsappRepository.getMessageId()+1;

        whatsappRepository.setMessageId(id);
        Message message=new Message(id,content);
        return id;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "You are not allowed to send message" if the sender is not a member of the group
        //If the message is sent successfully, return the final number of messages in that group.

        if(whatsappRepository.getGroupUserMap().containsKey(group)==false)
        {
            throw new Exception("Group does not exis");
        }

        if(userExistsInGroup(sender,group)==false)
        {
            throw new Exception("You are not allowed to send message");
        }

        HashMap<Message, User> senderMap=whatsappRepository.getSenderMap();
        senderMap.put(message, sender);

        whatsappRepository.getGroupMessageMap().get(group).add(message);

        return whatsappRepository.getGroupUserMap().get(group).size();
    }

    public  boolean userExistsInGroup(User user, Group group){
        HashMap<Group, List<User> > groupUserMap=whatsappRepository.getGroupUserMap();
        List<User> users=groupUserMap.get(group);
        for (User u:users) {
            if(u.equals(user)){
                return true;
            }
        }
        return false;
    }
    public String changeAdmin(User approver, User user, Group group) throws Exception{
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "Approver does not have rights" if the approver is not the current admin of the group
        //Throw "User is not a participant" if the user is not a part of the group
        //Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only one admin and the admin rights are transferred from approver to user.

        HashMap<Group, List<User>> groupUserMap=whatsappRepository.getGroupUserMap();
        if(!groupUserMap.containsKey(group)){
            //groupUserMap doesn't contain group
            throw new Exception("Group does not exist");
        }
        //Throw "User is not a participant" if the user is not a part of the group
        if(!userExistsInGroup(user, group)){
            throw new Exception("User is not a participant");
        }
        //Throw "Approver does not have rights" if the approver is not the current admin of the group
        if(!isUserAdminOfGroup(approver, group)){
            //if approver is not admin
            throw new Exception("Approver does not have rights");
        }

        //Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only one admin and the admin rights are transferred from approver to user.
        HashMap<Group, User> adminMap=whatsappRepository.getAdminMap();
        adminMap.put(group, user);
        return "SUCCESS";
    }
    public boolean isUserAdminOfGroup(User user, Group group){
        HashMap<Group, User> adminMap=whatsappRepository.getAdminMap();
        User currAdmin=adminMap.get(group);
        if(currAdmin==(user)){
            return true;
        }
        return false;
    }

    public int removeUser(User user) throws Exception{
        //This is a bonus problem and does not contains any marks
        //A user belongs to exactly one group
        //If user is not found in any group, throw "User not found" exception
        //If user is found in a group and it is the admin, throw "Cannot remove admin" exception
        //If user is not the admin, remove the user from the group, remove all its messages from all the databases, and update relevant attributes accordingly.
        //If user is removed successfully, return (the updated number of users in the group + the updated number of messages in group + the updated number of overall messages)

        return Integer.MIN_VALUE;
    }

    public String findMessage(Date start, Date end, int K) throws Exception{
        //This is a bonus problem and does not contains any marks
        // Find the Kth latest message between start and end (excluding start and end)
        // If the number of messages between given time is less than K, throw "K is greater than the number of messages" exception

        return "K is greater than the number of messages";
    }
}
