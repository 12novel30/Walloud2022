package com.spring.mydiv.Controller;

import java.util.List;
import java.util.Map;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.mydiv.Entity.Travel;
import com.spring.mydiv.Service.TravelService;
import com.spring.mydiv.Service.UserService;

import lombok.RequiredArgsConstructor;

import static java.lang.Boolean.TRUE;

/**
 * @author 12nov
 */
//@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userservice;
    private final TravelService travelservice;
    private final PersonService personservice;

    @GetMapping("/halo") //just for test
    public String ddd() {
        return "He";
    }

    @PostMapping(value = "/Register")
    public ResponseEntity<UserDto.Response> createUser(@RequestBody Map map) {
        UserDto.Request request = new UserDto.Request(
                map.get("user_name").toString(),
                map.get("user_email").toString(),
                map.get("user_password").toString(),
                map.get("user_account").toString());
        return ResponseEntity.ok(userservice.createUser(request));
    }

    @PostMapping(value = "/login")
    public int login(@RequestBody Map map) {
        UserDto.Login loginUser = new UserDto.Login(
                map.get("input_id").toString(),
                map.get("input_password").toString());
        return userservice.login(loginUser);
        /**success -> return [user id]
         * wrong email -> -2
         * wrong pw -> -1
         */
    }

    @GetMapping("/{userId}")
    public UserDto.WithTravel getUserInfo(@PathVariable int userId){
        return userservice.getUserInfoWithTravel(userId);
    }

    @PostMapping("/{userId}/createTravel")
    public int joinTravel(@PathVariable int userId, @RequestBody Map map){
        TravelDto.Request travelRequest = new TravelDto.Request(map.get("travel_name").toString());
        PersonDto.Request personRequest = new PersonDto.Request(
                userservice.getUserInfo(userId),
                travelservice.createTravel(travelRequest));
        if (ResponseEntity.ok(personservice.createPerson(personRequest, TRUE)).getStatusCodeValue() == 200)
            return personRequest.getTravel().getId().intValue();
        else return -1;
    }

    // 여행을 생성한 user가 여행 자체를 삭제하는 메소드
    @DeleteMapping("/{userId}/{travelId}/delete")
    public void deleteTravel(@PathVariable int travelId) {
        travelservice.deleteTravel(travelId);
    }

    @PutMapping("/{userId}/updateUserInfo")
    public ResponseEntity<UserDto.Response> updateUser(@PathVariable int userId, @RequestBody Map map) {
        UserDto.Request updateRequest = new UserDto.Request(
                map.get("user_name").toString(),
                map.get("user_email").toString(),
                map.get("user_password").toString(),
                map.get("user_account").toString());
        return ResponseEntity.ok(userservice.updateUserInfo(userId, updateRequest)
        );
    }
}