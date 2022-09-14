package com.spring.mydiv.Service;

import javax.transaction.Transactional;

import com.spring.mydiv.Dto.UserDetailDto;
import org.springframework.stereotype.Service;

import com.spring.mydiv.Dto.TravelDto;
import com.spring.mydiv.Dto.UserCreateDto;
import com.spring.mydiv.Dto.UserDto;
import com.spring.mydiv.Entity.Travel;
import com.spring.mydiv.Entity.User;
import com.spring.mydiv.Repository.PersonRepository;
import com.spring.mydiv.Repository.TravelRepository;
import com.spring.mydiv.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

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
    public UserCreateDto.Response createUser(UserCreateDto.Request request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .account(request.getAccount())
                .build();
        userRepository.save(user);
        return UserCreateDto.Response.fromEntity(user);
    }
    
    UserCreateDto.Response answer = null;
    String result = "";
//    public UserCreateDto.Response login(UserCreateDto.Login loginUser) { //ver1. return info
//    	Optional<User> info = userRepository.findByEmail(loginUser.getEmail());
//    	info.ifPresent(user ->
//    					{if (loginUser.getPassword().toString().equals(user.getPassword().toString())) {
//    						answer = UserCreateDto.Response.fromEntity(user);}
//    					}
//    					);
//    	return answer;
//    }
    public String login(UserCreateDto.Login loginUser) { //ver2. return id
        Optional<User> info = userRepository.findByEmail(loginUser.getEmail());
        info.ifPresentOrElse(user ->
                                {if (loginUser.getPassword().toString().equals(user.getPassword().toString())) {
                                    result = "Login Success!";}
                                else{result = "Wrong Password!";}},
                                    ()-> {if(loginUser.getEmail()!=null){result = "Wrong Email!";}}
        );
        return result;
    }

    public UserDetailDto getUserInfo(int no){
        Optional<User> info = userRepository.findById(Long.valueOf(no));
        return UserDetailDto.fromEntity(info.get());
    }

    List<String> travelList = null;
    public List<String> getUserJoinedTravel(int no){
    	List<Integer> travelIdList = personRepository.findTravelIdByUserId(no);
    	for(int id : travelIdList) {
    		travelList.add(travelRepository.findNameIdById(id));
    	}
    	return travelList;
    }
    
    @Transactional
    public UserCreateDto.Response createPerson(UserCreateDto.Request userinfo, TravelDto travelinfo) {
        User user = User.builder()
                .name(userinfo.getName())
                .email(userinfo.getEmail())
                .password(userinfo.getPassword())
                .account(userinfo.getAccount())
                .build();
        Travel travel = Travel.builder()
                .name(travelinfo.getName())
                .build();
        
        userRepository.save(user);
        return UserCreateDto.Response.fromEntity(user);
    }
    
    
}
