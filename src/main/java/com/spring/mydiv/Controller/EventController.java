package com.spring.mydiv.Controller;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Entity.Event;
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

    @PostMapping("/{userId}/{travelId}/CreateEvent")
    public void createEvent(@PathVariable("travelId") int travelId, @RequestBody Map map) throws ParseException {
        // setting
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Map> partiDtoList = (List)map.get("parti_list");
        Long payerId = Long.valueOf(map.get("payer_person_id").toString());
        Boolean isPayerInParticipant = eventService.checkPayerInParticipant(partiDtoList, payerId);
        int partiCount = partiDtoList.size();
        int eventPrice = Integer.parseInt(map.get("price").toString());

        // create event
        EventDto.Request request = EventDto.Request.builder()
                .Name(map.get("event_name").toString())
                .Travel(travelService.getTravelInfo(travelId)) //orElseThrow
                .Date(simpleDateFormat.parse(map.get("event_date").toString()))
                .Price(eventPrice)
                .PayerPersonId(payerId)
                .build();
        EventDto.Response eventDto = eventService.createEvent(request);

        if (ResponseEntity.ok(eventDto).getStatusCodeValue() == 200){
            for (Map partiDto : partiDtoList){
                Person person = personService.getPersonEntityByPersonId(Long.valueOf(partiDto.get("personId").toString()));
                Double chargedPrice = Double.valueOf(partiDto.get("spent").toString());
                Boolean p_role = Boolean.valueOf(partiDto.get("role").toString());

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
            if (!isPayerInParticipant){
                Person person = personService.getPersonEntityByPersonId(payerId);
                ParticipantDto.Request partiRequest = ParticipantDto.Request.builder()
                        .person(personService.getPersonEntityByPersonId(payerId))
                        .event(eventService.getEventEntityByEventId(Long.valueOf(eventDto.getEventId().toString())))
                        .role(true)
                        .chargedPrice(0.0)
                        .build();
                participantService.createParticipant(partiRequest);
                personService.updatePersonMoneyByCreating(person, eventPrice, 0.0, true);
            }
            personService.updatePersonRole(travelId);
        } else throw new DefaultException(CREATE_EVENT_FAIL);
    }

    @DeleteMapping("/{userid}/{travelid}/{eventid}/deleteEvent")
    public void deleteEvent(@PathVariable("travelid") int travelId, @PathVariable("eventid") int event_id)
    {
        for(ParticipantDto.detailView DetailView : participantService.getParticipantInEvent(event_id)){
            personService.updatePersonMoneyByDeleting(personService.getPersonEntityByPersonId(DetailView.getPersonId()),
                    eventService.getEventPriceById(event_id),
                    DetailView.getChargedPrice(),
                    DetailView.isEventRole());
        }
        personService.updatePersonRole(travelId);
        eventService.deleteEvent(event_id);
    }

    @PostMapping("/{userid}/{travelid}/{eventid}/updateEvent")
    public void updateEvent(@PathVariable("travelid") int travel_id, @PathVariable("eventid") int event_id, @RequestBody Map map) throws ParseException{
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int prevPrice = eventService.getEventPriceById(event_id);
        int eventPrice = Integer.parseInt(map.get("price").toString());
        Long payerId = Long.valueOf(map.get("payer_person_id").toString());

        EventDto.Request request = EventDto.Request.builder()
                .Name(map.get("event_name").toString())
                .Date(simpleDateFormat.parse(map.get("event_date").toString()))
                .Price(eventPrice)
                .PayerPersonId(payerId)
                .build();
        eventService.updateEvent(event_id, request);

        List<ParticipantDto.detailView> prevParticipants = participantService.getParticipantInEvent(event_id);
        Map<Long, PersonDto.MoneyUpdateRequest> updateRequests = new HashMap<Long, PersonDto.MoneyUpdateRequest>();

        for(ParticipantDto.detailView prevParticipant : prevParticipants) {
            updateRequests.put(prevParticipant.getPersonId(),
                    PersonDto.MoneyUpdateRequest.builder()
                            .pervEventRole(prevParticipant.isEventRole())
                            .currEventRole(false)
                            .prevPrice(prevPrice)
                            .currPrice(eventPrice)
                            .prevChargedPrice(prevParticipant.getChargedPrice())
                            .currChargedPrice(-1.0).build());
        }

        List<Map> partiDtoList = (List)map.get("parti_list");
        for(Map partiDto : partiDtoList){
            Long currPersonId = Long.valueOf(partiDto.get("personId").toString());
            Person curr_p = personService.getPersonEntityByPersonId(currPersonId);
            Double chargedPrice = Double.valueOf(partiDto.get("spent").toString());
            Boolean eventRole = Boolean.parseBoolean(partiDto.get("role").toString());
            if (updateRequests.containsKey(currPersonId)){
                PersonDto.MoneyUpdateRequest currRequest = updateRequests.get(currPersonId);
                currRequest.setCurrEventRole(eventRole);
                currRequest.setCurrChargedPrice(chargedPrice);
                participantService.updateParticipant(eventRole, chargedPrice, curr_p);
                personService.updatePersonMoney(curr_p, currRequest);
            }
            else{
                ParticipantDto.Request partiRequest = ParticipantDto.Request.builder()
                        .person(curr_p)
                        .event(eventService.getEventEntityByEventId(Long.valueOf(event_id)))
                        .role(eventRole)
                        .chargedPrice(chargedPrice)
                        .build();
                participantService.createParticipant(partiRequest);
                personService.updatePersonMoneyByCreating(curr_p, eventPrice, chargedPrice, eventRole);
            }
        }
        if (!eventService.checkPayerInParticipant(partiDtoList, payerId)){
            Person payer = personService.getPersonEntityByPersonId(payerId);
            Boolean eventRole = true;

            if (updateRequests.containsKey(payerId)){
                PersonDto.MoneyUpdateRequest currRequest = updateRequests.get(payerId);
                currRequest.setCurrEventRole(eventRole);
                currRequest.setCurrChargedPrice(0.0);
                participantService.updateParticipant(eventRole, 0.0, payer);
                personService.updatePersonMoney(payer, currRequest);
            }
            else {
                ParticipantDto.Request partiRequest = ParticipantDto.Request.builder()
                        .person(payer)
                        .event(eventService.getEventEntityByEventId(Long.valueOf(event_id)))
                        .role(eventRole)
                        .chargedPrice(0.0)
                        .build();
                participantService.createParticipant(partiRequest);
                personService.updatePersonMoneyByCreating(payer, eventPrice, 0.0, eventRole);
            }
        }

        Event e = eventService.getEventEntityByEventId(Long.valueOf(event_id));
        for(Long personId : updateRequests.keySet()){
            PersonDto.MoneyUpdateRequest currRequest = updateRequests.get(personId);
            if(currRequest.getCurrChargedPrice().equals(-1.0)){
                personService.updatePersonMoneyByDeleting(personService.getPersonEntityByPersonId(personId),
                        eventService.getEventPriceById(event_id),
                        currRequest.getPrevChargedPrice(),
                        currRequest.isPervEventRole());
                Person p = personService.getPersonEntityByPersonId(personId);
                participantService.deleteParticipant(p, e);
            }
        }
        personService.updatePersonRole(travel_id);
    }

    @GetMapping("/{userid}/{travelid}/{eventid}/detail")
    public List<ParticipantDto.detailView> getDetailInEvent(@PathVariable("eventid") int eventid){
        return participantService.getParticipantInEvent(eventid);
    }

}
