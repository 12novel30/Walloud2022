package com.spring.mydiv.config;

import java.util.Arrays;
import java.util.List;

import com.spring.mydiv.Service.GoogleOAuth2Service;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Environment environment;
    private final String registration = "spring.security.oauth2.client.registration.";
    private final GoogleOAuth2Service googleOAuth2Service;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/api/login", "/api/register",
                                "/api/index", "api/OAuth2").permitAll()
                        // TODO 여기 뭐 허용해야하는지 논의 필!
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
//                        .clientRegistrationRepository(clientRegistrationRepository())
//                        .authorizedClientService(authorizedClientService())
                        .userInfoEndpoint( user -> user
                                .oidcUserService(googleOAuth2Service)
                                // google 인증, OpenID Connect 1.0)
                        )
                )
        ;
    }

//    @Bean
//    public OAuth2AuthorizedClientService authorizedClientService() {
//        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
//    }

//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        final List<ClientRegistration> clientRegistrations = Arrays.asList(
//                googleClientRegistration()
////                facebookClientRegistration()
//        );
//
//        return new InMemoryClientRegistrationRepository(clientRegistrations);
//    }

//    private ClientRegistration googleClientRegistration() {
//        final String clientId = environment.getProperty(registration + "google.client-id");
//        final String clientSecret = environment.getProperty(registration + "google.client-secret");
//
//        return CommonOAuth2Provider
//                .GOOGLE
//                .getBuilder("google")
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .build();
//    }

//    private ClientRegistration facebookClientRegistration() {
//        final String clientId = environment.getProperty(registration + "facebook.client-id");
//        final String clientSecret = environment.getProperty(registration + "facebook.client-secret");
//
//        return CommonOAuth2Provider
//                .FACEBOOK
//                .getBuilder("facebook")
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .scope(
//                        "public_profile",
//                        "email",
//                        "user_birthday",
//                        "user_gender"
//                )
//                .userInfoUri("https://graph.facebook.com/me?fields=id,name,email,picture,gender,birthday")
//                .build();
//    }
}