package com.spring.mydiv.Service;

import javax.transaction.Transactional;

import com.spring.mydiv.Code.ErrorCode;
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
                .bank(request.getBank())
                .build();
        userRepository.save(user);
        return UserDto.Response.fromEntity(user);
    } //fin

    public boolean checkIsEmailRegistered(String email){
        return userRepository.existsByEmail(email);
    }
    public int login(UserDto.Login loginUser) {
        int result = 0;
        User entity = userRepository.findByEmail(loginUser.getEmail())
                .orElseThrow(() -> new DefaultException(WRONG_EMAIL));
        if (loginUser.getPassword().equals(entity.getPassword()))
            result = entity.getId().intValue();
        else throw new DefaultException(WRONG_PASSWORD);
        return result;
    } //ing

    public UserDto.Response getUserInfo(int no){
        return userRepository.findById(Long.valueOf(no))
                .map(UserDto.Response::fromEntity)
                .orElseThrow(()-> new DefaultException(NO_USER));
    } //fin

    public List<TravelDto.Response> getUserJoinedTravel(int userId){
        List<Person> list = personRepository.findByUser_Id(Long.valueOf(userId));
        List<TravelDto.Response> result = new ArrayList<>();
        for (Person p : list){
            TravelDto.Response travel = TravelDto.Response.builder()
                    .TravelId(p.getTravel().getId())
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
                .UserId(user.get().getId())
                .Name(user.get().getName())
                .Email(user.get().getEmail())
                .Account(user.get().getAccount())
                .Password(user.get().getPassword())
                .Bank(user.get().getBank())
                .build();
        return dto;
    }

    @Transactional
    public UserDto.Response updateUserInfo(int userId, UserDto.Request updateRequest){
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new DefaultException(NO_USER));

        if (updateRequest.getName() != null) user.setName(updateRequest.getName());
        if (updateRequest.getEmail() != null) user.setEmail(updateRequest.getEmail());
        if (updateRequest.getPassword() != null) user.setPassword(updateRequest.getPassword());
        if (updateRequest.getAccount() != null) user.setAccount(updateRequest.getAccount());
        if (updateRequest.getBank() != null) user.setBank(updateRequest.getBank());

        return UserDto.Response.fromEntity(userRepository.save(user));
    }

    public void deleteUser(int userId){
        userRepository.deleteById(Long.valueOf(userId));
    }

}

//