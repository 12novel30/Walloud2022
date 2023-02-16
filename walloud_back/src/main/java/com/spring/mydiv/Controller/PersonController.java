package com.spring.mydiv.Controller;

import com.spring.mydiv.Code.WalloudCode;
import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Service.ParticipantService;
import com.spring.mydiv.Service.PersonService;
import com.spring.mydiv.Service.TravelService;
import com.spring.mydiv.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.spring.mydiv.Code.WalloudCode.MANAGER;
import static com.spring.mydiv.Code.WalloudCode.OTHERS;
import static java.lang.Boolean.FALSE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PersonController {
    private final UserService userService;
    private final TravelService travelService;
    private final PersonService personService;
    private final ParticipantService participantService;

    @PostMapping("/{travelId}/createPerson2Travel")
    public int createPerson2Travel(@PathVariable int travelId,
                                   @RequestBody String user_email){
        // get User Information
        UserDto.Response userDto = userService.getUserResponseByEmail(user_email);
        // if user not in travel then throw Exception
        personService.validateUserInTravel(userDto.getUserId(), Long.valueOf(travelId));
        // return created person id
        return personService.getPersonIdFromPersonDto(
                personService.createPerson( // create person
                        personService.setPersonRequest( // set person Dto
                                userDto,
                                // get Travel Information
                                travelService.getTravelInfo(travelId)),
                        FALSE) // this person is not superUser
        );
    }

    @DeleteMapping("/{personId}/deletePerson2Travel")
    public void deletePerson2Travel(@PathVariable int person_id){
        // if this person joined any event, then throw Exception
        participantService.validatePersonNotJoinedAnyEvent(person_id);
        // if this person is superUser for this travel, then throw Exception
        personService.validatePersonNotSuperuser(person_id);
        // TODO - isSettled 체크 안되어있으면 프론트단에서 정말 삭제하시겠습니까? 등의 문구 띄우도록 부탁
        // delete person
        personService.deletePerson2Travel(person_id);
    }

    @GetMapping("{travelId}/{personId}/getPersonDetailView")
    public PersonDto.Detail getPersonDetailView(
            @PathVariable("travelId") int travelId,
            @PathVariable("personId") int personId){
        // get person info
        PersonDto.Detail detailView = personService.getPersonToDetailView(personId);
        // get event list that this person joined
        detailView.setEventList(participantService.getEventListThatPersonJoin(personId));
        // set order list (by person role)
        WalloudCode orderCode = personService.getOrderCodeFromDetailView(detailView);
        if (orderCode == MANAGER) // set all member
            detailView.setPersonInTravelList(personService.getPersonListForOrderMessage(travelId));
        else if (orderCode == OTHERS) { // set only manager
            PersonDto.OrderMessage manager = personService.getManagerInTravel(travelId);
            manager.setDifference(detailView.getDifference()); // change manager diff
            detailView.setPersonInTravelList(List.of(manager));
        }
        return detailView;
    }

    @GetMapping("/{travelId}/getPersonListToHomeView")
    // TODO - personList 메소드가 분리되어있는데, getTravelHomeView 에서 삭제할지 고민
    public List<PersonDto.HomeView> getPersonListToHomeView(
            @PathVariable int travelId){
        return personService.getPersonListToHomeView(travelId);
    }

    @PutMapping("{personId}/updateIsSettled")
    public ResponseEntity<PersonDto.Simple> updateIsSettled(
            @PathVariable int personId, @RequestBody String isSettled){
        return ResponseEntity.ok(
                personService.updateIsSettled(personId,Boolean.valueOf(isSettled)));
    }

    // TODO - superUser 변경할 수 있는 메소드 만들어둘지 논의
}
