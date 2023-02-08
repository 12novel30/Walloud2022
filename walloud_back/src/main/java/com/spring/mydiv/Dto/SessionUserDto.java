package com.spring.mydiv.Dto;

import com.spring.mydiv.Entity.User;
import lombok.Getter;

@Getter
public class SessionUserDto {
    private String name;
    private String email;

    public SessionUserDto(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
