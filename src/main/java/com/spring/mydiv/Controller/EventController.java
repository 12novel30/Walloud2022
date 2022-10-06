package com.spring.mydiv.Controller;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Entity.Person;
import com.spring.mydiv.Exception.DefaultException;
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

import static com.spring.mydiv.Code.ErrorCode.*;

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
    public void createEvent(@PathVariable("travelId") int travelId, @RequestBody Map map) throws ParseException {
        // setting
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Map> partiDtoList = (List)map.get("parti_list");
        Boolean isPayerInParticipant = eventService.checkPayerInParticipant(partiDtoList, Long.valueOf(map.get("payer_person_id").toString()));
        int partiCount = partiDtoList.size();
        int eventPrice = Integer.parseInt(map.get("price").toString());
        Long payerId = Long.valueOf(map.get("payer_person_id").toString());

        // create event
        EventDto.Request request = EventDto.Request.builder()
                .Name(map.get("event_name").toString())
                .Travel(travelService.getTravelInfo(travelId)) //orElseThrow
                .Date(simpleDateFormat.parse(map.get("event_date").toString()))
                .Price(eventPrice)
                .PartiCount(partiCount)
                .isPayerInParticipant(isPayerInParticipant)
                .PayerPersonId(payerId)
                .build();
        EventDto.Response eventDto = eventService.createEvent(request);

        if (ResponseEntity.ok(eventDto).getStatusCodeValue() == 200){
            List<Long> peopleId = new ArrayList<>();
            for (Map partiDto : partiDtoList){
               peopleId.add(Long.valueOf(partiDto.get("id").toString()));
            }
            if (!isPayerInParticipant){
                peopleId.add(payerId);
            }

            for(Long personId : peopleId){
                System.out.println(personId);
                System.out.println(payerId);
                Person person = personService.getPersonEntityByPersonId(personId);
                Double chargedPrice = participantService.calculateChargedPrice(eventPrice, partiCount);
                Boolean p_role = false;
                if (personId.equals(payerId)){
                    p_role = true;
                    if(!isPayerInParticipant){
                        chargedPrice = 0.0;
                    }
                }

                ParticipantDto.Request partiRequest = ParticipantDto.Request.builder()
                        .person(person)
                        .event(eventService.getEventEntityByEventId(Long.valueOf(eventDto.getEventId().toString())))
                        .role(p_role)
                        .chargedPrice(chargedPrice)
                        .build();
                if (ResponseEntity.ok(participantService.createParticipant(partiRequest)).getStatusCodeValue() != 200)
                    throw new DefaultException(CREATE_PARTICIPANT_FAIL);

                personService.updatePersonMoneyByCreating(person, eventPrice, chargedPrice, p_role);
            }
            personService.updatePersonRole(travelId);
        } else throw new DefaultException(CREATE_EVENT_FAIL);
    }

    @DeleteMapping("/{userid}/{travelid}/{eventid}/deleteEvent")
    public void deleteEvent(@PathVariable("eventid") int event_id)
    {
        for(ParticipantDto.detailView DetailView : participantService.getParticipantInEvent(event_id)){
            personService.updatePersonMoneyByDeleting(personService.getPersonEntityByPersonId(DetailView.getPersonId()),
                    eventService.getEventPriceById(event_id),
                    DetailView.getChargedPrice(),
                    DetailView.isEventRole());
        }
        eventService.deleteEvent(event_id);
    }

    @PostMapping("/{userid}/{travelid}/{eventid}/updateEvent")
    public void updateEvent(@PathVariable("eventid") int event_id){
        // json을 똑같은 type 으로 받는가?
    }

    @GetMapping("/{userid}/{travelid}/{eventid}/detail")
    public List<ParticipantDto.detailView> getDetailInEvent(@PathVariable("eventid") int eventid){
        return participantService.getParticipantInEvent(eventid);
    }

}
