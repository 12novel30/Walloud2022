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


import javax.validation.Valid;

import static com.spring.mydiv.Code.S3Code.USER_FOLDER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final S3UploaderService s3UploaderService;

    @PostMapping(path = "/register") // consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    public ResponseEntity<UserDto.Response> createUser(
            @Valid @RequestBody UserDto.Request request) {
        System.out.println("!!!!!!");
        System.out.println(request);
        return ResponseEntity.ok(userService.createUser(request));
    }

    @DeleteMapping("/{userId}/deleteUser")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}/updateUserInfoExceptImage")
    public ResponseEntity<UserDto.Response> updateUserInfoExceptImage(
            @PathVariable Long userId,
            @RequestBody UserDto.Request request) {
        return ResponseEntity.ok(userService.updateUserInfo(userId, request));
    }

    @PutMapping("/{userId}/updateUserImage")
    public String updateUserImage(@PathVariable Long userId,
                                  @RequestPart(value="file") MultipartFile file)
            throws IOException {
        S3Dto.ImageUrls urls = userService.updateUserImage(
                userId, s3UploaderService.upload(file, USER_FOLDER.getDescription()));
        s3UploaderService.deleteImage(urls.getDeleteImage());
        return urls.getNewImage();
    }

    @GetMapping("/{userId}/getUserImage")
    public String getUserImage(@PathVariable Long userId) {
        return userService.getUserImageURL(userId);
    }

    @GetMapping("/{userId}/getUserInfoExceptImage")
    public UserDto.Response getUserInfoExceptImage(@PathVariable("userId") Long userId) {
        return userService.getUserResponseById(userId);
    }

    @GetMapping("/{userId}/getTravelListUserJoined")
    public List<TravelDto.Response> getTravelListUserJoined(
            @PathVariable Long userId) {
        return userService.getUserJoinedTravel(userId);
    }

    @PostMapping(value = "/login")
    public UserDto.Response login(@RequestBody UserDto.Login loginUser) {
        return userService.login(loginUser);
    }
}