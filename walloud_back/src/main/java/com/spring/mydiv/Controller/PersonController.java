package com.spring.mydiv.Controller;

import com.spring.mydiv.Code.WalloudCode;
import com.spring.mydiv.Dto.*;
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
@RequestMapping("/api")
public class PersonController {
    private final UserService userService;
    private final TravelService travelService;
    private final PersonService personService;
    private final ParticipantService participantService;

    @PostMapping("/{travelId}/createPerson2Travel")
    public PersonDto.HomeView createPerson2Travel(@PathVariable Long travelId,
                                                  @RequestBody String user_email) {
        // get User Information
        System.out.println("!@(!@($@$(!");
        System.out.println(user_email);
        user_email = user_email.substring(1, user_email.length()-1);
        UserDto.Response userDto = userService.getUserResponseByEmail(user_email);
        // if user not in travel then throw Exception
        personService.validateIsUserNotInTravel(
                userDto.getUserId(), travelId);
        // create person entity with user & travel information
        // and return person id
        return personService.createPerson(
                personService.setPersonRequestDto(userDto,
                        travelService.getTravelResponse(travelId)),
                        false); // this person is not superUser
    }

    @DeleteMapping("/{personId}/deletePerson2Travel")
    public void deletePerson2Travel(@PathVariable(value = "personId") Long person_id) {
        // if this person joined any event, then throw Exception
        participantService.validateDoesPersonNotJoinedAnyEvent(person_id);
        // if this person is superUser for this travel, then throw Exception
        personService.validateIsPersonNotSuperUser(person_id);
        // delete person
        personService.deletePerson(person_id);
    }

    @GetMapping("/{travelId}/{personId}/getPersonDetailView")
    public PersonDto.Detail getPersonDetailView(
            @PathVariable("travelId") Long travelId,
            @PathVariable("personId") Long personId) {
        // get person info
        PersonDto.Detail detailView = personService.getPersonDetail(personId);
        // get event list that this person joined
        detailView.setEventList(
                participantService.getEventDtoListThatPersonJoin(personId));

        // set order list (by person role)
        WalloudCode orderCode = personService.validateIsManager(detailView);
        if (orderCode == MANAGER) // set all member
            detailView.setPersonInTravelList(
                    personService.getOthersOrderMessageList(travelId));
        else if (orderCode == OTHERS) { // set only manager
            PersonDto.OrderMessage manager =
                    personService.getManagerOrderMessage(travelId);
            manager.setDifference(detailView.getDifference()); // change manager diff
            detailView.setPersonInTravelList(List.of(manager));
        }
        return detailView;
    }

    @GetMapping("/{travelId}/getPersonListToHomeView")
    public List<PersonDto.HomeView> getPersonListToHomeView(
            @PathVariable Long travelId) {
        return personService.getPersonHomeViewList(travelId);
    }

    @PutMapping("/{personId}/updateIsSettled")
    public int updateIsSettled(@PathVariable Long personId,
                               @RequestBody Boolean isSettled) {
        return personService.updateIsSettled(personId, isSettled);
    }
}
