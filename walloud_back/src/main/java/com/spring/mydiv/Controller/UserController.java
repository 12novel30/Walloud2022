package com.spring.mydiv.Controller;
import java.io.IOException;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Service.S3UploaderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.mydiv.Service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userservice;
    private final S3UploaderService s3UploaderService;

    @PostMapping(value = "/login")
    public Long login(@RequestBody UserDto.Login loginUser) {
        return userservice.login(loginUser);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto.Response> createUser(
            @RequestBody UserDto.Request request) {
        return ResponseEntity.ok(userservice.createUser(request));
    }

    @GetMapping("/{userId}/getUserInfoExceptImage")
    public UserDto.Response getUserInfoExceptImage(
            @PathVariable("userId") int userId){
        return userservice.getUserInfo(userId);
    }

    @DeleteMapping("/{userId}/deleteUser")
    public void deleteUser(@PathVariable("userId") int userId){
        userservice.deleteUser(userId);
    }

    @PostMapping("/{userId}/createNewTravelUserJoining")
    public int createNewTravelUserJoining( // TODO - not yet
            @PathVariable int userId, @RequestBody String travel_name){
        return userservice.createNewTravelUserJoining(userId, travel_name);
    }

    @PutMapping("/{userId}/updateUserInfoExceptImage")
    public ResponseEntity<UserDto.Response> updateUserInfoExceptImage(
            @PathVariable int userId, @RequestBody UserDto.Request request) {
        return ResponseEntity.ok(userservice.updateUserInfo(userId, request));
    }

    @PutMapping("/{userId}/updateUserImage")
    public String updateUserImage(@PathVariable int userId,
                                  @RequestPart(value="file") MultipartFile file)
            throws IOException {
        return userservice.updateUserImage(
                userId, s3UploaderService.upload(file, "test"));
    }

    @GetMapping("/{userId}/getUserImage")
    public String getUserImage(@PathVariable int userId){
        return userservice.getUserImageURL(userId);
    }

}