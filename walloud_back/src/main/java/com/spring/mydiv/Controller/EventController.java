package com.spring.mydiv.Controller;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Entity.Event;
import com.spring.mydiv.Entity.Person;
import com.spring.mydiv.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.spring.mydiv.Code.S3FolderName.EVENT_FOLDER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EventController {
    private final EventService eventService;
    private final PersonService personService;
    private final TravelService travelService;
    private final ParticipantService participantService;
    private final S3UploaderService s3UploaderService;
    private final DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // TODO - dto 정리
    @PostMapping("/{travelId}/createEvent")
    public Long createEvent(@PathVariable int travelId,
                            @RequestBody EventDto.Request eventRequest) {

        // get Travel Information
        eventRequest.setTravel(travelService.getTravelInfo(travelId));
        // create event
        EventDto.Response eventResponse = eventService.createEvent(eventRequest);
        // if payer not in parti_list, then add payer to parti_list
        List<ParticipantDto.CreateEvent> partiDtoList =
                eventService.validatePayerInPartiList(eventRequest);

        // for each participant,
        for (ParticipantDto.CreateEvent partiDto : partiDtoList){
            // create participant
            participantService.createParticipant(
                    participantService.setParticipantRequest(partiDto, eventResponse));
            // change person(parti) sumSend etc.
            personService.updatePersonMoneyByCreating(partiDto, eventRequest);
        }

        // change person role in this Travel
        personService.updatePersonRole(travelId);
        // return created event id
        return eventResponse.getEventId();
    }

    @DeleteMapping("/{travelId}/{eventId}/deleteEvent")
    public void deleteEvent(@PathVariable("travelId") int travelId,
                            @PathVariable("eventId") int eventId)
    {
        List<ParticipantDto.detailView> partiList = participantService.getParticipantInEvent(eventId);
        // change person(parti) sumSend etc.
        for (ParticipantDto.detailView partiDto : partiList){
            personService.updatePersonMoneyByDeleting(
                    partiDto, eventService.getEventPriceById(eventId));
        }
        // change person(parti) role
        personService.updatePersonRole(travelId);
        // delete event
        eventService.deleteEvent(eventId);
    }

    @PostMapping("/{userid}/{travelid}/{eventid}/updateEvent")
    public void updateEvent(@PathVariable("travelid") int travel_id, @PathVariable("eventid") int event_id, @RequestBody Map map) throws ParseException{
        //setting for event update
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int prevPrice = eventService.getEventPriceById(event_id);
        int eventPrice = Integer.parseInt(map.get("price").toString());
        Long payerId = Long.valueOf(map.get("payer_person_id").toString());

        //update eventDB
        EventDto.Request request = EventDto.Request.builder()
                .event_name(map.get("event_name").toString())
                .Date(simpleDateFormat.parse(map.get("event_date").toString()))
                .Price(eventPrice)
                .PayerPersonId(payerId)
                .build();
        eventService.updateEvent(event_id, request);

        //setting for participant update
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
        partiDtoList = eventService.validatePayerInPartiList(partiDtoList, payerId);
        Event e = eventService.getEventEntityByEventId(Long.valueOf(event_id));

        for(Map partiDto : partiDtoList){
            Long currPersonId = Long.valueOf(partiDto.get("personId").toString());
            Person curr_p = personService.getPersonEntityByPersonId(currPersonId);
            Double chargedPrice = Double.valueOf(partiDto.get("spent").toString());
            Boolean eventRole = Boolean.parseBoolean(partiDto.get("role").toString());
            if (updateRequests.containsKey(currPersonId)){ // still participated
                PersonDto.MoneyUpdateRequest currRequest = updateRequests.get(currPersonId);
                currRequest.setCurrEventRole(eventRole);
                currRequest.setCurrChargedPrice(chargedPrice);
                participantService.updateParticipant(eventRole, chargedPrice, curr_p, e);
                personService.updatePersonMoney(curr_p, currRequest);
            }
            else{ // new participants
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

        for(Long personId : updateRequests.keySet()){ // Deleted participant
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

        //Update Person role
        personService.updatePersonRole(travel_id);
    }

    @GetMapping("/{eventId}/detail")
    public List<ParticipantDto.detailView> getDetailInEvent(@PathVariable int eventId){
        return participantService.getParticipantInEvent(eventId);
    }

    @GetMapping("/{eventId}/getEventImage")
    public String getEventImage(@PathVariable int eventId){
        return eventService.getEventImageURL(eventId);
    }

    @PutMapping("/{eventId}/updateEventImage")
    public String updateEventImage(@PathVariable int eventId,
                                   @RequestPart(value="file") MultipartFile file)
            throws IOException {
        return eventService.updateEventImage(eventId, s3UploaderService.upload(file, EVENT_FOLDER.getDescription()));
    }
}
