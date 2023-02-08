package com.spring.mydiv.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.spring.mydiv.Dto.IdTokenRequestDto;
import com.spring.mydiv.Dto.UserDto;
import com.spring.mydiv.Entity.SecurityAccount;
import com.spring.mydiv.Entity.User;
import com.spring.mydiv.Exception.DefaultException;
import com.spring.mydiv.Repository.SecurityRepository;
import com.spring.mydiv.Repository.UserRepository;
import com.spring.mydiv.config.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import static com.spring.mydiv.Code.ErrorCode.WRONG_EMAIL;
import static com.spring.mydiv.Code.ErrorCode.WRONG_PASSWORD;

@Service
public class UserSecurityService {
//    private final UserRepository userRepository;
//
//    public boolean existsByEmail(final String email){
//        return userRepository.existsByEmail(email);
//    }
//    @Transactional
//    public void requestRegistration(
//            final String name,
//            final String email
//    ){
//        /* 회원가입
//        * 구글에서 name, email 을 리턴
//        * 1. 아직 등록하지 않았으면 나머지 정보를 받도록
//        * 2. 이미 등록되어있으면 "이미 등록되어있습니다!"를 리턴, 메인 페이지로
//        * */
//        final boolean exists = existsByEmail(email);
//        if (exists == false){ // 1. newbie
//            final User user = User.builder()
//                    .name(name)
//                    .email(email)
//                    .password("request.getPassword()")
//                    .account("request.getAccount()")
//                    .bank("request.getBank()")
//                    .build();
//            System.out.println(user.getEmail());;
//        } else { // 2. already registered
//
//        }
//    }


    /*------------------------------------------------------*/
    private final SecurityRepository securityRepository;
    private final JWTUtils jwtUtils;
    private final GoogleIdTokenVerifier verifier;
    public UserSecurityService(@Value("${app.googleClientId}") String clientId,
                               SecurityRepository securityRepository,
                               JWTUtils jwtUtils) {
        this.securityRepository = securityRepository;
        this.jwtUtils = jwtUtils;
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public SecurityAccount getAccount(Long id) {
        return securityRepository.findById(id).orElse(null);
    }

    public String loginOAuthGoogle(IdTokenRequestDto requestBody) {
        SecurityAccount account = verifyIDToken(requestBody.getIdToken());
        if (account == null) {
            throw new IllegalArgumentException();
        }
        account = createOrUpdateUser(account);
        return jwtUtils.createToken(account, false);
    }

    @Transactional
    public SecurityAccount createOrUpdateUser(SecurityAccount account) {
        SecurityAccount existingAccount = securityRepository.findByEmail(account.getEmail()).orElse(null);
        if (existingAccount == null) {
            account.setRoles("ROLE_USER");
            securityRepository.save(account);
            return account;
        }
        existingAccount.setFirstName(account.getFirstName());
        existingAccount.setLastName(account.getLastName());
        existingAccount.setPictureUrl(account.getPictureUrl());
        securityRepository.save(existingAccount);
        return existingAccount;
    }

    private SecurityAccount verifyIDToken(String idToken) {
        try {
            GoogleIdToken idTokenObj = verifier.verify(idToken);
            if (idTokenObj == null) {
                return null;
            }
            GoogleIdToken.Payload payload = idTokenObj.getPayload();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");
            String email = payload.getEmail();
            String pictureUrl = (String) payload.get("picture");

            return new SecurityAccount(firstName, lastName, email, pictureUrl);
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }
    }
}
