package com.axeno.journalApp.controller;

import com.axeno.journalApp.DuplicateUserException;
import com.axeno.journalApp.entity.UserEntity;
import com.axeno.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserEntity>> getAllUser(){
        List<UserEntity> users = userService.getAllUser();
        if(users.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //search user by id
    @GetMapping("/getUser/{uniqueName}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable String uniqueName){
        UserEntity userEntity = userService.getUserById(uniqueName);
        if(userEntity == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok(userEntity);
    }

    //insert user
    @PostMapping("/insertUser")
    public ResponseEntity<UserEntity> insertUser(@RequestBody UserEntity user) {
        try {
            UserEntity savedUser = userService.insertUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (DuplicateUserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //delete user
    @DeleteMapping("/deleteUser/{userName}")
    public ResponseEntity<UserEntity> deleteUser(@PathVariable String userName){
        UserEntity entity = userService.deleteUser(userName);
        if (entity == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.noContent().build();
    }

    //update data in user
    @PutMapping("/updateUser")
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity user){
        UserEntity userEntity = userService.updateUser(user);
        if (userEntity == null){
            return ResponseEntity.notFound().build();
        }
        if(userEntity.equals(user))
            return ResponseEntity.internalServerError().build();

        return ResponseEntity.noContent().build();
    }
}
