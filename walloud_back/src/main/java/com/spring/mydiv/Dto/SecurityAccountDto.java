package com.spring.mydiv.Dto;

import com.spring.mydiv.Entity.SecurityAccount;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityAccountDto {

    private String firstName;

    private String lastName;

    private String email;

    public static final SecurityAccountDto convertToDto(SecurityAccount account) {
        return SecurityAccountDto.builder()
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .build();
    }
}
