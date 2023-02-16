package com.spring.mydiv.Controller;
import java.io.IOException;
import java.util.List;

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
    private final UserService userService;
    private final S3UploaderService s3UploaderService;

    @PostMapping(value = "/login")
    public Long login(@RequestBody UserDto.Login loginUser) {
        return userService.login(loginUser);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto.Response> createUser(
            @RequestBody UserDto.Request request) {
        return ResponseEntity.ok(userService.createUser(request));
        // TODO - discuss; userId만 리턴할지 정할 것
    }

    @GetMapping("/{userId}/getUserInfoExceptImage")
    public UserDto.Response getUserInfoExceptImage(
            @PathVariable("userId") int userId){
        return userService.getUserInfo(userId);
    }

    @DeleteMapping("/{userId}/deleteUser")
    public void deleteUser(@PathVariable("userId") int userId){
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}/updateUserInfoExceptImage")
    public ResponseEntity<UserDto.Response> updateUserInfoExceptImage(
            @PathVariable int userId, @RequestBody UserDto.Request request) {
        return ResponseEntity.ok(userService.updateUserInfo(userId, request));
    }

    @PutMapping("/{userId}/updateUserImage")
    public String updateUserImage(@PathVariable int userId,
                                  @RequestPart(value="file") MultipartFile file)
            throws IOException {
        return userService.updateUserImage(
                userId, s3UploaderService.upload(file, "test"));
    }

    @GetMapping("/{userId}/getUserImage")
    public String getUserImage(@PathVariable int userId){
        return userService.getUserImageURL(userId);
    }

    @GetMapping("/{userId}/getTravelListUserJoined") // TODO - NEW
    public List<TravelDto.Response> getTravelListUserJoined(@PathVariable int userId){
        return userService.getUserJoinedTravel(userId);
    }
}