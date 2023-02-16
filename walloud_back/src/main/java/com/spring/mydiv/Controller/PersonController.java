package com.spring.mydiv.Controller;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Exception.DefaultException;
import com.spring.mydiv.Service.ParticipantService;
import com.spring.mydiv.Service.PersonService;
import com.spring.mydiv.Service.TravelService;
import com.spring.mydiv.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/{userId}/{travelId}/createPerson2Travel")
    public void createPerson2Travel(@PathVariable int travelId,
                                    @RequestBody String user_email){
        UserDto.Response userDetailDto = userService.getUserInfoByEmail(user_email);

        if (personService.checkIsUserinTravel(userDetailDto.getUserId(), travelId))
            throw new DefaultException(ALREADY_EXISTED);
        PersonDto.Request request = new PersonDto.Request(
                userDetailDto,
                travelService.getTravelInfo(travelId));
        PersonDto.basic personDto = personService.createPerson(request, FALSE);
        if (personDto == null) throw new DefaultException(CREATE_FAIL);
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
        // TODO - isSettled 체크 안되어있으면 프론트단에서 정말 삭제하시겠습니까? 등의 문구 띄우도록 부탁
    }


    @GetMapping("{travelid}/{personid}/personDetail")
    public PersonDto.Detail getPersonToDetailView(@PathVariable("travelid") int travelid,
                                                        @PathVariable("personid") int personid){
        System.out.println(travelid);

        PersonDto.Detail detailView = personService.getPersonToDetailView(personid);
        List<EventDto.PersonView> EventList = participantService.getEventListThatPersonJoin(personid);
        detailView.setEventList(EventList);
        if (EventList.size()!=0) {
            //이 여행에서 해야하는 order 프린트를 위한 list(travelrole, diff에 따라)
            if (detailView.getTravelRole()) { // =총무 -> (여행 참여 전원) id, name, 이사람에게(받을/줄)돈
                detailView.setPersonInTravelList(personService.getPersonListToHomeView(travelid));
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

    @GetMapping("/{travelId}/getPersonListToHomeView")
    // TODO - personList 메소드가 분리되어있는데, getTravelHomeView 에서 삭제할지 고민
    public List<PersonDto.HomeView> getPersonListToHomeView(
            @PathVariable("travelId") int travelId){
        return personService.getPersonListToHomeView(travelId);
    }

    @PostMapping("{personId}/setSettle")
    public ResponseEntity<PersonDto.Detail> updateIsSettled(@PathVariable("personId") int personId,
                                @RequestBody Map map){
        if (map.containsKey("isSettled")){
            return null;
        } else {
            Boolean isSettled = Boolean.valueOf(map.get("isSettled").toString());
            return ResponseEntity.ok(personService.updateIsSettled(personId, isSettled));
        }
    }
    
    // TODO - superUser 변경할 수 있는 메소드 만들어둘것
}
