package com.spring.mydiv.Service;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Exception.DefaultException;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import com.spring.mydiv.Entity.User;
import com.spring.mydiv.Repository.PersonRepository;
import com.spring.mydiv.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

import static com.spring.mydiv.Code.ErrorCode.*;
import static com.spring.mydiv.Code.S3FolderName.DEFAULT_IMAGE;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PersonRepository personRepository;

    @Transactional
    public UserDto.Response createUser(@NonNull UserDto.Request request) {
        if (!validateIsEmailRegistered(request.getUser_email()))
            return UserDto.Response.fromEntity(
                    userRepository.save(
                            User.builder()
                                    .name(request.getUser_name())
                                    .email(request.getUser_email())
                                    .password(request.getUser_password())
                                    .account(request.getUser_account())
                                    .bank(request.getUser_bank())
                                    .info(DEFAULT_IMAGE.getDescription())
                                    .build()));
        else throw new DefaultException(ALREADY_REGISTERED);
    }

    @Transactional(readOnly = true)
    public boolean validateIsEmailRegistered(@NonNull String email) {
        return userRepository.existsByEmail(email);
    }
    public Long login(@NonNull UserDto.Login loginUser) {
        User entity = getUserEntityByEmail(loginUser.getEmail());
        if (loginUser.getPassword().equals(entity.getPassword()))
            return entity.getId();
        else throw new DefaultException(WRONG_PASSWORD);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if(getUserJoinedTravel(userId).size() == 0)
            userRepository.deleteById(userId);
        else throw new DefaultException(INVALID_DELETE_TRAVEL_EXISTED);
    }

    @Transactional
    public UserDto.Response updateUserInfo(Long userId,
                                           @Nullable UserDto.Request updateRequest) {
        User user = getUserEntityById(userId);

        if (updateRequest.getUser_name() != null)
            user.setName(updateRequest.getUser_name());
        if (updateRequest.getUser_email() != null)
            // 변경하는 email 은 기존에 다른 user 가 등록한 적 없는 것이어야 한다.
            if (!validateIsEmailRegistered(updateRequest.getUser_email())
                    || updateRequest.getUser_email().equals(user.getEmail()))
                user.setEmail(updateRequest.getUser_email());
            else throw new DefaultException(ALREADY_REGISTERED);
        if (updateRequest.getUser_password() != null)
            user.setPassword(updateRequest.getUser_password());
        if (updateRequest.getUser_account() != null)
            user.setAccount(updateRequest.getUser_account());
        if (updateRequest.getUser_bank() != null)
            user.setBank(updateRequest.getUser_bank());

        return UserDto.Response.fromEntity(userRepository.save(user));
    }
    @Transactional
    public S3Dto.ImageUrls updateUserImage(Long userId, String imageURL) {
        User user = getUserEntityById(userId);
        String prevImage = user.getInfo();
        user.setInfo(imageURL);
        return S3Dto.ImageUrls.builder()
                .newImage(userRepository.save(user).getInfo())
                .deleteImage(prevImage)
                .build();
    }

    public String getUserImageURL(Long userId){
        return getUserEntityById(userId).getInfo();
    }
    @Transactional(readOnly = true)
    public List<TravelDto.Response> getUserJoinedTravel(Long userId) {
        return personRepository.findByUser_Id(userId)
                .stream()
                .map(TravelDto.Response::fromPersonEntity)
                .collect(Collectors.toList());
    }
    public UserDto.Response getUserResponseById(Long userId) {
        return UserDto.Response.fromEntity(getUserEntityById(userId));
    }
    public UserDto.Response getUserResponseByEmail(String email) {
        return UserDto.Response.fromEntity(getUserEntityByEmail(email));
    }
    @Transactional(readOnly = true)
    private User getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new DefaultException(WRONG_EMAIL));
    }
    @Transactional(readOnly = true)
    private User getUserEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DefaultException(NO_USER));
    }
}