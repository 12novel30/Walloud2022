package com.spring.mydiv.Controller;
import java.util.List;
import java.util.Map;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Exception.DefaultException;
import com.spring.mydiv.Service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.mydiv.Service.TravelService;
import com.spring.mydiv.Service.UserService;

import lombok.RequiredArgsConstructor;
import static com.spring.mydiv.Code.ErrorCode.*;
import static java.lang.Boolean.TRUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userservice;
    private final TravelService travelservice;
    private final PersonService personservice;

    @PostMapping(value = "/register")
    public ResponseEntity<UserDto.Response> createUser(UserDto.Request request) {
        if (!userservice.checkIsEmailRegistered(request.getEmail())) {
            return ResponseEntity.ok(userservice.createUser(request));
        } else throw new DefaultException(ALREADY_REGISTERED);
    }

    @PostMapping(value = "/login")
    public UserDto.Response login(UserDto.Login loginUser) {
        return userservice.login(loginUser);
    }

    @DeleteMapping("/{userId}/deregister")
    public void deregister(@PathVariable("userId") int user_id){
        if(userservice.getUserJoinedTravel(user_id).size() == 0){
            userservice.deleteUser(user_id);
        } else throw new DefaultException(INVALID_DELETE_TRAVELEXISTED);
    }

    @GetMapping("/{userId}")
    public List<TravelDto.Response> getUserInfo(@PathVariable int userId){
        return userservice.getUserJoinedTravel(userId);
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