package com.spring.mydiv.Service;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Entity.Person;
import com.spring.mydiv.Exception.DefaultException;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import com.spring.mydiv.Entity.User;
import com.spring.mydiv.Repository.PersonRepository;
import com.spring.mydiv.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.spring.mydiv.Code.ErrorCode.*;
import static com.spring.mydiv.Code.S3FolderName.DEFAULT_IMAGE;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PersonRepository personRepository;
    private final S3UploaderService s3UploaderService;

    @Transactional
    public UserDto.Response createUser(@NonNull UserDto.Request request) {
        if (!checkIsEmailRegistered(request.getUser_email())){
            User user = User.builder()
                    .name(request.getUser_name())
                    .email(request.getUser_email())
                    .password(request.getUser_password())
                    .account(request.getUser_account())
                    .bank(request.getUser_bank())
                    .info(DEFAULT_IMAGE.getDescription())
                    .build();
            userRepository.save(user);
            return UserDto.Response.fromEntity(user);
        }
        else
            throw new DefaultException(ALREADY_REGISTERED);
    }

    @Transactional(readOnly = true)
    public boolean checkIsEmailRegistered(@NonNull String email){
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Long login(@NonNull UserDto.Login loginUser) {
        User entity = userRepository.findByEmail(loginUser.getEmail())
                .orElseThrow(() -> new DefaultException(WRONG_EMAIL));

        if (loginUser.getPassword().equals(entity.getPassword()))
            return entity.getId();
        else throw new DefaultException(WRONG_PASSWORD);
    }

    public UserDto.Response getUserResponseFromEntity(int no){
        return UserDto.Response.fromEntity(getUserEntity(no));
    }

    @Transactional(readOnly = true)
    public List<TravelDto.Response> getUserJoinedTravel(int userId){
        List<Person> list = personRepository.findByUser_Id(Long.valueOf(userId));
        List<TravelDto.Response> result = new ArrayList<>();
        for (Person p : list)
            result.add(TravelDto.Response.builder()
                    .TravelId(p.getTravel().getId())
                    .Name(p.getTravel().getName())
                    .IsSuper(p.getIsSuper())
                    .build());
        return result;
    }

    @Transactional(readOnly = true)
    private User getUserEntity(int no) {
        return userRepository.findById(Long.valueOf(no))
                .orElseThrow(() -> new DefaultException(NO_USER));
    }
    @Transactional(readOnly = true)
    public UserDto.Response getUserResponseByEmail(String email){
        return userRepository.findByEmail(email)
                .map(UserDto.Response::fromEntity)
                .orElseThrow(() -> new DefaultException(WRONG_EMAIL));
    }

    @Transactional
    public UserDto.Response updateUserInfo(int userId, UserDto.Request updateRequest){
        User user = getUserEntity(userId);

        if (updateRequest.getUser_name() != null) user.setName(updateRequest.getUser_name());
        if (updateRequest.getUser_email() != null) {
            if (!checkIsEmailRegistered(updateRequest.getUser_email()) ||
                    updateRequest.getUser_email().equals(user.getEmail()))
                user.setEmail(updateRequest.getUser_email());
            else
                throw new DefaultException(ALREADY_REGISTERED);
        }
        if (updateRequest.getUser_password() != null) user.setPassword(updateRequest.getUser_password());
        if (updateRequest.getUser_account() != null) user.setAccount(updateRequest.getUser_account());
        if (updateRequest.getUser_bank() != null) user.setBank(updateRequest.getUser_bank());

        return UserDto.Response.fromEntity(userRepository.save(user));
    }

    @Transactional
    public String updateUserImage(int userId, String imageURL){
        User user = getUserEntity(userId);
        // TODO - deleteUserImage(user);
        user.setInfo(imageURL);
        return userRepository.save(user).getInfo();
    }

    public void deleteUserImage(User user){
        String userExistingImage = user.getInfo();
        s3UploaderService.deleteImage(userExistingImage);
    } // TODO - 아직 구현 안했음

    public String getUserImageURL(int userId){
        return getUserEntity(userId).getInfo();
    }
    @Transactional
    public void deleteUser(int userId){
        if(getUserJoinedTravel(userId).size() == 0)
            userRepository.deleteById(Long.valueOf(userId));
        else
            throw new DefaultException(INVALID_DELETE_TRAVEL_EXISTED);
    }
}