package com.spring.mydiv.Controller;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Entity.Person;
import com.spring.mydiv.Service.EventService;
import com.spring.mydiv.Service.ParticipantService;
import com.spring.mydiv.Service.PersonService;
import com.spring.mydiv.Service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 12nov
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EventController {
    private final EventService eventService;
    private final PersonService personService;
    private final TravelService travelService;
    private final ParticipantService participantService;

//    @GetMapping("/{userid}/{travelid}/createEvent") //don't use this
//    public List<PersonDto.Simple> getPersonNameInTravel(@PathVariable("travelid") int travelid){
//        return personService.getPersonNameInTravel(travelid);
//    }

    @PostMapping("/{userId}/{travelId}/CreateEvent")
    public int createEvent(@PathVariable("travelId") int travelId, @RequestBody Map map) throws ParseException {
        // setting
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Map> partiDtoList = (List)map.get("parti_list");
        Boolean isPayerInParticipant = eventService.checkPayerInParticipant(partiDtoList, Long.valueOf(map.get("payer_person_id").toString()));
        int partiCount = partiDtoList.size();

        // create event
        EventDto.Request request = EventDto.Request.builder()
                .Name(map.get("event_name").toString())
                .Travel(travelService.getTravelInfo(travelId)) //orElseThrow
                .Date(simpleDateFormat.parse(map.get("event_date").toString()))
                .Price(Integer.parseInt(map.get("price").toString()))
                .PartiCount(partiCount)
                .isPayerInParticipant(isPayerInParticipant)
                .PayerPersonId(Long.valueOf(map.get("payer_person_id").toString()))
                .build();
        EventDto.Response eventDto = eventService.createEvent(request);

        // if success to create Event
        if (ResponseEntity.ok(eventDto).getStatusCodeValue() == 200){
            // setting
            List<Person> personList = new ArrayList<>();
            System.out.println(partiDtoList.size());
            for (Map partiDto : partiDtoList){
                Person person = personService.getPersonEntityByPersonId(
                        Long.valueOf(partiDto.get("id").toString())); //orElseThrow // person_id가 아닌 id 입니다...
                personList.add(person);

                // create participant
                ParticipantDto.Request partiRequest = ParticipantDto.Request.builder()
                        .person(person)
                        .event(eventService.getEventEntityByEventId(
                                Long.valueOf(eventDto.getId().toString()))) //orElseThrow
                        .role(Boolean.valueOf(partiDto.get("role").toString()))
                        .build();
                if (ResponseEntity.ok(participantService.createParticipant(partiRequest)).getStatusCodeValue() != 200)
                    return -2; //fail to create participate
            }

            // if success to create Participant
            // update person
            System.out.println(eventDto.getDividePrice());
            personService.updatePersonMoneyByCreating(personList,
                    Long.valueOf(map.get("payer_person_id").toString()),
                    eventDto.getDividePrice(),
                    eventDto.getTakePrice(),
                    isPayerInParticipant);
            personService.updatePersonRole(travelId);

            return 200; //success all
        } else return -1; //fail to create event
    }

    @DeleteMapping("/{userid}/{travelid}/{eventid}/deleteEvent")
    public void deleteEvent(@PathVariable("eventid") int event_id)
    {
        EventDto.deleteRequest DeleteRequest = eventService.getEventDetailforDelete(event_id);
        personService.updatePersonMoneyByDeleting(DeleteRequest.getJoinedPerson(),
                DeleteRequest.getPayerId(),
                DeleteRequest.getDividePrice(),
                DeleteRequest.getTakePrice());
        eventService.deleteEvent(event_id);
    }

    @GetMapping("/{userid}/{travelid}/{eventid}/detail")
    public List<ParticipantDto.detailView> getDetailInEvent(@PathVariable("eventid") int eventid){
        return participantService.getParticipantInEvent(eventid);
    }

}
