package com.spring.mydiv.Service;

import javax.transaction.Transactional;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Entity.Person;
import com.spring.mydiv.Exception.DefaultException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.mydiv.Entity.User;
import com.spring.mydiv.Repository.PersonRepository;
import com.spring.mydiv.Repository.TravelRepository;
import com.spring.mydiv.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.spring.mydiv.Code.ErrorCode.*;
import static java.lang.Boolean.TRUE;

/**
 * @author 12nov
 */
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PersonRepository personRepository;
	private final TravelRepository travelRepository;
    private final S3UploaderService s3UploaderService;
    private final TravelService travelservice;
    private final PersonService personservice;

    @Transactional
    public UserDto.Response createUser(UserDto.Request request) {
        if (!checkIsEmailRegistered(request.getUser_email())){
            User user = User.builder()
                    .name(request.getUser_name())
                    .email(request.getUser_email())
                    .password(request.getUser_password())
                    .account(request.getUser_account())
                    .bank(request.getUser_bank())
                    .build();
            userRepository.save(user);
            return UserDto.Response.fromEntity(user);
        }
        else
            throw new DefaultException(ALREADY_REGISTERED);
    }

    public boolean checkIsEmailRegistered(String email){
        return userRepository.existsByEmail(email);
    }
    public UserDto.Response login(UserDto.Login loginUser) {
        User entity = userRepository.findByEmail(loginUser.getEmail())
                .orElseThrow(() -> new DefaultException(WRONG_EMAIL));
        if (loginUser.getPassword().equals(entity.getPassword()))
            return UserDto.Response.fromEntity(entity);
        else throw new DefaultException(WRONG_PASSWORD);
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
                    .IsSuper(p.getIsSuper())
                    .build();
            result.add(travel);
        }
        return result;
    }



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

        if (updateRequest.getUser_name() != null) user.setName(updateRequest.getUser_name());
        if (updateRequest.getUser_email() != null) user.setEmail(updateRequest.getUser_email());
        // TODO 이메일 중복인지 확인해야할 것 같음
        if (updateRequest.getUser_password() != null) user.setPassword(updateRequest.getUser_password());
        if (updateRequest.getUser_account() != null) user.setAccount(updateRequest.getUser_account());
        if (updateRequest.getUser_bank() != null) user.setBank(updateRequest.getUser_bank());

        return UserDto.Response.fromEntity(userRepository.save(user));
    }

    @Transactional
    public UserDto.ResponseWithImage updateUserImage(int userId, String imageURL){
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new DefaultException(NO_USER));
        // TODO - deleteUserImage(user);
        user.setInfo(imageURL);
        return UserDto.ResponseWithImage.fromEntity(userRepository.save(user));
        userRepository.findById(Long.valueOf(userId))
                .map(UserDto.ResponseWithImage::fromEntity)
                .orElseThrow(()-> new DefaultException(NO_USER))
                .getImageurl();
    }

    public void deleteUserImage(User user){
        String userExistingImage = user.getInfo();
        s3UploaderService.deleteImage(userExistingImage);
    }

    public String getUserImageURL(int userId){
        return userRepository.findById(Long.valueOf(userId))
                .map(UserDto.ResponseWithImage::fromEntity)
                .orElseThrow(()-> new DefaultException(NO_USER))
                .getImageurl();
    }

    public void deleteUser(int userId){
        if(getUserJoinedTravel(userId).size() == 0)
            // can delete this user - not participated some travel
            userRepository.deleteById(Long.valueOf(userId));
        else
            throw new DefaultException(INVALID_DELETE_TRAVEL_EXISTED);
    }

    public int createNewTravelUserJoining(int userId, String travelName) {
        TravelDto.Request travelRequest = new TravelDto.Request(travelName);
        PersonDto.Request personRequest = new PersonDto.Request(
                getUserInfo(userId),
                travelservice.createTravel(travelRequest));
        if (ResponseEntity.ok(personservice.createPerson(personRequest, TRUE)).getStatusCodeValue() == 200)
            return personRequest.getTravel().getTravelId().intValue();
        else throw new DefaultException(CREATE_FAIL);
    }
}

//