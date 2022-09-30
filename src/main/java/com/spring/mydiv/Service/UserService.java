package com.spring.mydiv.Service;

import javax.transaction.Transactional;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Entity.Person;
import com.spring.mydiv.Exception.DefaultException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.spring.mydiv.Entity.Travel;
import com.spring.mydiv.Entity.User;
import com.spring.mydiv.Repository.PersonRepository;
import com.spring.mydiv.Repository.TravelRepository;
import com.spring.mydiv.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.spring.mydiv.Code.ErrorCode.*;

/**
 * @author 12nov
 */
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PersonRepository personRepository;
	private final TravelRepository travelRepository;
	
    @Transactional
    public UserDto.Response createUser(UserDto.Request request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .account(request.getAccount())
                .build();
        userRepository.save(user);
        return UserDto.Response.fromEntity(user);
    } //fin

    int result = 0;
    public int login(UserDto.Login loginUser) {
        //ver1. int return
        Optional<User> info = userRepository.findByEmail(loginUser.getEmail());
        info.ifPresentOrElse(
            user -> //"Wrong Password!"
                {if (loginUser.getPassword().toString().equals(user.getPassword().toString())) {
                    result = user.getId().intValue();}
                else{result = -1;}},
            ()-> {if(loginUser.getEmail()!=null){result = -2;}} //"Wrong Email!"
        );
        return result;
    } //ing

    public UserDto.Response getUserInfo(int no){
        return userRepository.findById(Long.valueOf(no))
                .map(UserDto.Response::fromEntity)
                .orElseThrow(()-> new DefaultException(NO_USER));
    } //fin

    public List<TravelDto.Response> getUserJoinedTravel(int no){
        List<Person> list = personRepository.findByUser_Id(Long.valueOf(no));
        List<TravelDto.Response> result = new ArrayList<>();
        for (Person p : list){
            TravelDto.Response travel = TravelDto.Response.builder()
                    .Id(p.getTravel().getId())
                    .Name(p.getTravel().getName())
                    .build();
            result.add(travel);
        }
        return result;
    } //fin

    public UserDto.WithTravel getUserInfoWithTravel(int no){
        User entity = userRepository.findById(Long.valueOf(no))
                .orElseThrow(()-> new DefaultException(NO_USER));
        UserDto.WithTravel dto = UserDto.WithTravel.fromEntity(entity);
        dto.setTravelList(getUserJoinedTravel(no));
        return dto;
    } //fin

    public UserDto.Response getUserInfoByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) return null;

        UserDto.Response dto = UserDto.Response.builder()
                .Id(user.get().getId())
                .Name(user.get().getName())
                .Email(user.get().getEmail())
                .Account(user.get().getAccount())
                .Password(user.get().getPassword())
                .build();
        return dto;
    }

}
