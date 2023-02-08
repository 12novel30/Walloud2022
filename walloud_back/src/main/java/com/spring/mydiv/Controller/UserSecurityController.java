package com.spring.mydiv.Controller;

import com.spring.mydiv.Dto.IdTokenRequestDto;
import com.spring.mydiv.Dto.UserDto;
import com.spring.mydiv.Entity.SecurityAccount;
import com.spring.mydiv.Service.UserSecurityService;
import com.spring.mydiv.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import com.google.common.net.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

import static com.spring.mydiv.Dto.SecurityAccountDto.convertToDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oauth")
public class UserSecurityController {
//    private final UserService userservice;
//
//    @GetMapping("/token")
//    public OAuth2AuthenticationToken home(final OAuth2AuthenticationToken token) {
//        return token;
//    }
//    @PostMapping(value = "/loginSuccess")
//    public UserDto.ResponseWithImage login(UserDto.Login loginUser) {
//        return userservice.login(loginUser);
//    }


    /*------------------------------------------------------*/
    @Autowired
    UserSecurityService userSecurityService;
    @GetMapping("/user/info")
    public ResponseEntity getUserInfo(Principal principal) {
        SecurityAccount account = userSecurityService.getAccount(Long.valueOf(principal.getName()));
        return ResponseEntity.ok().body(convertToDto(account));
    }
    @PostMapping("/login")
    public ResponseEntity LoginWithGoogleOauth2(@RequestBody IdTokenRequestDto requestBody, HttpServletResponse response) {
        String authToken = userSecurityService.loginOAuthGoogle(requestBody);
        final ResponseCookie cookie = ResponseCookie.from("AUTH-TOKEN", authToken)
                .httpOnly(true)
                .maxAge(7 * 24 * 3600)
                .path("/")
                .secure(false)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/tmptmp")
    public String tmp() {
        return "ResponseEntity.ok().body(convertToDto(account))";
    }
}
