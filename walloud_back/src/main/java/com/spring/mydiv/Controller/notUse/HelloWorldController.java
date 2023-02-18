
package com.spring.mydiv.Controller.notUse;

import com.spring.mydiv.Code.WalloudCode;
import com.spring.mydiv.Dto.PersonDto;
import com.spring.mydiv.Dto.UserDto;
import com.spring.mydiv.Service.ParticipantService;
import com.spring.mydiv.Service.PersonService;
import com.spring.mydiv.Service.TravelService;
import com.spring.mydiv.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.spring.mydiv.Code.WalloudCode.MANAGER;
import static com.spring.mydiv.Code.WalloudCode.OTHERS;

@RestController
@RequiredArgsConstructor
public class HelloWorldController {
    private final UserService userService;
    private final TravelService travelService;
    private final PersonService personService;
    private final ParticipantService participantService;
    @GetMapping("/api/hello")
    public String hello() {
        return "Hello, world!!";
    }





    @PostMapping("/post-test")
    public void post_test(@PathVariable int travelId,
                          @RequestBody String user_email){
    }

    @DeleteMapping("/delete-test")
    public void delete_test(@PathVariable int person_id){
    }

    @PutMapping("/put-test")
    public void put_test(@PathVariable int personId, @RequestBody String isSettled){
    }
    @GetMapping("/get-test")
    public PersonDto.Detail get_test() {
        return personService.getPersonDetail(510);
    }

}