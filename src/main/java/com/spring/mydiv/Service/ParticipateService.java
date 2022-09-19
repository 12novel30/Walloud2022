package com.spring.mydiv.Service;

import com.spring.mydiv.Dto.ParticipateCreateDto;
import com.spring.mydiv.Dto.TravelCreateDto;
import com.spring.mydiv.Entity.User;
import com.spring.mydiv.Repository.ParticipateRepository;
import com.spring.mydiv.Repository.PersonRepository;
import com.spring.mydiv.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author 12nov
 */
@Service
@RequiredArgsConstructor
public class ParticipateService {

    @Transactional
    public ResponseEntity<ParticipateCreateDto> createParticipate(){
        return null;
    }
}
