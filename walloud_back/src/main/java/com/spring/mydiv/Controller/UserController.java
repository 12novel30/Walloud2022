package com.spring.mydiv.Controller;
import java.io.IOException;
import java.util.Map;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Exception.DefaultException;
import com.spring.mydiv.Service.PersonService;
import com.spring.mydiv.Service.S3UploaderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.mydiv.Service.TravelService;
import com.spring.mydiv.Service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import static com.spring.mydiv.Code.ErrorCode.*;
import static java.lang.Boolean.TRUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userservice;
    private final TravelService travelservice;
    private final PersonService personservice;
    private final S3UploaderService s3UploaderService;

    @PostMapping(value = "/login")
    public UserDto.Response login(UserDto.Login loginUser) {
        return userservice.login(loginUser);
    }
    @PostMapping("/register")
    public ResponseEntity<UserDto.Response> createUser(
            @RequestBody UserDto.Request request) {
        return ResponseEntity.ok(userservice.createUser(request));
    }
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") int userId){
        userservice.deleteUser(userId);
    }
    @PostMapping("/{userId}/createTravel")
    public int createNewTravelUserJoining( // TODO - not yet
            @PathVariable int userId, String travel_name){
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
        return ResponseEntity.ok(userservice.updateUserImage(
                userId,
                s3UploaderService.upload(file, "test")));
    }
    @GetMapping("/{userId}/getUserImage")
    public String getUserImage(@PathVariable int userId){
        return userservice.getUserImageURL(userId);
    }

}