package com.spring.mydiv.Controller;

import com.spring.mydiv.Code.ErrorCode;
import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Exception.DefaultException;
import com.spring.mydiv.Service.ParticipantService;
import com.spring.mydiv.Service.PersonService;
import com.spring.mydiv.Service.TravelService;
import com.spring.mydiv.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.spring.mydiv.Code.ErrorCode.*;
import static java.lang.Boolean.FALSE;

/**
 * @author 12nov
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PersonController {
    private final UserService userService;
    private final TravelService travelService;
    private final PersonService personService;
    private final ParticipantService participantService;

    @PostMapping("/{userId}/{travelId}/createUser")
    public void createPerson2Travel(@PathVariable int travelId,
                                      @RequestBody Map map){
        String user_email = map.get("user_email").toString();
        UserDto.Response userDetailDto = userService.getUserInfoByEmail(user_email);
        if (userDetailDto == null) throw new DefaultException(WRONG_EMAIL);
        else {
            if (personService.checkIsUserinTravel(userDetailDto.getUserId(), travelId))
                throw new DefaultException(ALREADY_EXISTED);
            PersonDto.Request request = new PersonDto.Request(
                    userDetailDto,
                    travelService.getTravelInfo(travelId));
            PersonDto.basic personDto = personService.createPerson(request, FALSE);
            if (personDto == null) throw new DefaultException(CREATE_FAIL);
        }
    }

    @DeleteMapping("/{userId}/{travelId}/{personId}/deleteUser")
    public void deletePerson2Travel(@PathVariable("personId") int person_id){
        if (participantService.getSizeOfJoinedEventList(person_id) == 0) {
            if (!personService.isPersonSuperuser(person_id)) {
                personService.deleteJoinTravel(person_id);
            }
            else throw new DefaultException(INVALID_DELETE_SUPERUSER);
        }
        else throw new DefaultException(INVALID_DELETE_EVENTEXISTED);
    }

    @GetMapping("/{userid}/{travelid}/{personid}/personDetail")
    public PersonDto.Detail getPersonToDetailView(@PathVariable("travelid") int travelid,
                                                        @PathVariable("personid") int personid){
        PersonDto.Detail detailView = personService.getPersonToDetailView(personid);
        List<EventDto.PersonView> EventList = participantService.getEventListThatPersonJoin(personid);
        detailView.setEventList(EventList);
        if (EventList.size()!=0) {
            //이 여행에서 해야하는 order 프린트를 위한 list(travelrole, diff에 따라)
            if (detailView.getTravelRole()) { // =총무 -> (여행 참여 전원) id, name, 이사람에게(받을/줄)돈
                detailView.setPersonInTravelList(personService.getPersonInfoInTravel(travelid));
            } else { // ~총무 -> 총무id, 총무name, 내가총무에게(받을/줄)돈
                List<PersonDto.HomeView> PersonInTravelList = new ArrayList<>();
                PersonDto.HomeView tmp = personService.getPayerInTravel(travelid);
                tmp.setDifference(detailView.getDifference());
                PersonInTravelList.add(tmp);
                detailView.setPersonInTravelList(PersonInTravelList);
            }
        }
        return detailView;
    }

    @GetMapping("/{userId}/{travelId}/getPersonList")
    public List<PersonDto.HomeView> getAllPersonListInTravel(@PathVariable("travelId") int travelId){
        return personService.getPersonBasicInTravel(travelId);
    } //List<PersonDto.HomeView> PersonList;
}
