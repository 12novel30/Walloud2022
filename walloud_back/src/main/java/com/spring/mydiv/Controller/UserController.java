package com.spring.mydiv.Controller;
import java.io.IOException;
import java.util.List;
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
    @PostMapping(value = "/register")
    public ResponseEntity<UserDto.Response> createUser(UserDto.Request request) {
        if (!userservice.checkIsEmailRegistered(request.getEmail())) {
            return ResponseEntity.ok(userservice.createUser(request));
        } else throw new DefaultException(ALREADY_REGISTERED);
    }

    @PostMapping(value = "/login")
    public UserDto.ResponseWithImage login(UserDto.Login loginUser) {
        return userservice.login(loginUser);
    }

    @DeleteMapping("/{userId}/deregister")
    public void deregister(@PathVariable("userId") int user_id){
        if(userservice.getUserJoinedTravel(user_id).size() == 0){
            userservice.deleteUser(user_id);
        } else throw new DefaultException(INVALID_DELETE_TRAVELEXISTED);
    }
    
    @GetMapping("/{userId}/getImage")
    public String getUserImage(@PathVariable int userId){
        return userservice.getUserImageURL(userId);
    }

    @PostMapping("/{userId}/createTravel")
    public int createTravel(@PathVariable int userId, String travel_name){
        TravelDto.Request travelRequest = new TravelDto.Request(travel_name);
        PersonDto.Request personRequest = new PersonDto.Request(
                userservice.getUserInfo(userId),
                travelservice.createTravel(travelRequest));
        if (ResponseEntity.ok(personservice.createPerson(personRequest, TRUE)).getStatusCodeValue() == 200)
            return personRequest.getTravel().getTravelId().intValue();
        else throw new DefaultException(CREATE_FAIL);
    }

    @PutMapping("/{userId}/updateUserInfo")
    public ResponseEntity<UserDto.Response> updateUser(@PathVariable int userId, @RequestBody Map map) {
        if (map.containsKey("user_info")){
            return null;
        } else {
            UserDto.Request updateRequest = new UserDto.Request(
                    map.get("user_name").toString(),
                    map.get("user_email").toString(),
                    map.get("user_password").toString(),
                    map.get("user_account").toString(),
                    map.get("user_bank").toString());
            return ResponseEntity.ok(userservice.updateUserInfo(userId, updateRequest)
            );
        }
    }

    @PutMapping("/{userId}/updateUserImage")
    public ResponseEntity<UserDto.ResponseWithImage> uploadUserImage(@PathVariable int userId,
                                                                     @RequestPart(value="file",required = false) MultipartFile file)
                                                    throws IOException {
        String objectURL = s3UploaderService.upload(file, "test");
        System.out.println(objectURL);
        return ResponseEntity.ok(userservice.updateUserImage(userId, objectURL));
    }

}