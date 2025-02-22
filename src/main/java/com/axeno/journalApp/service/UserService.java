package com.axeno.journalApp.service;

import com.axeno.journalApp.DuplicateUserException;
import com.axeno.journalApp.entity.UserEntity;
import com.axeno.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //get all users
    public List<UserEntity> getAllUser() {
       return userRepository.findAll();
    }

    //search user by id
    public UserEntity getUserById(String uniqueName) {
        return userRepository.findByUserName(uniqueName).orElse(null);
    }

    //insert user
    public UserEntity insertUser(UserEntity user) {
        UserEntity userEntity = userRepository.findByUserName(user.getUserName()).orElse(null);
        if (userEntity != null) {
            throw new DuplicateUserException("User already exists with username: " + user.getUserName());
        }
        return userRepository.save(user);
    }

    //delete user
    public UserEntity deleteUser(String userName) {
       UserEntity userEntity = userRepository.findByUserName(userName).orElse(null);
       if (userEntity != null) {
           userRepository.delete(userEntity);
           return userEntity;
       }
       return null;
    }

    //update data in user
    public UserEntity updateUser(UserEntity user) {
        try {
            UserEntity userEntity = userRepository.findByUserName(user.getUserName()).orElse(null);
            if (userEntity == null) return null;
            userEntity.setUserName(!user.getUserName().equals(userEntity.getUserName()) ? user.getUserName() : userEntity.getUserName());
            userEntity.setPassWord(!user.getPassWord().equals(userEntity.getUserName()) ? user.getPassWord() : userEntity.getPassWord());
            userRepository.save(userEntity);
            return userEntity;
        } catch (Exception e) {
            return user;
        }
    }


}
