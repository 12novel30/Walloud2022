package com.spring.mydiv.Controller;

import com.spring.mydiv.Service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * @author 12nov
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ParticipantController {
    private final ParticipantService participantService;
}
