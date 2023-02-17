package com.spring.mydiv.Controller;

import com.spring.mydiv.Code.WalloudCode;
import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.spring.mydiv.Code.S3FolderName.EVENT_FOLDER;
import static com.spring.mydiv.Code.WalloudCode.NEW_PARTICIPANT;
import static com.spring.mydiv.Code.WalloudCode.STILL_PARTICIPATED;

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

    @PostMapping("/{travelId}/createEvent")
    public Long createEvent(@PathVariable int travelId,
                            @RequestBody EventDto.Request eventRequest) {

        // get Travel Information
        eventRequest.setTravelDto(travelService.getTravelResponse(travelId));
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
            personService.updatePersonMoney(
                    PersonDto.tmp.builder()
                            .personId(partiDto.getPersonId())
                            .eventRole(partiDto.getRole())
                            .eventPrice(Double.valueOf(eventRequest.getPrice()))
                            .chargedPrice(partiDto.getSpent())
                            .isCreate(true)
                            .build()
            );
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
        List<ParticipantDto.Detail> partiList = participantService.getPartiDtoListInEvent(eventId);
        // change person(parti) sumSend etc.
        for (ParticipantDto.Detail partiDto : partiList){
            personService.updatePersonMoney(
                    PersonDto.tmp.builder()
                            .personId(partiDto.getPersonId())
                            .eventRole(partiDto.isEventRole())
                            .eventPrice(Double.valueOf(eventService.getEventPriceById(eventId)))
                            .chargedPrice(partiDto.getChargedPrice())
                            .isCreate(false)
                            .build()
            );
        }
        // change person(parti) role
        personService.updatePersonRole(travelId);
        // delete event
        eventService.deleteEvent(eventId);
    }
    @PostMapping("/{travelId}/{eventId}/updateEvent")
    public Long updateEvent(
            @PathVariable("travelId") int travelId, @PathVariable("eventId") int eventId,
            @RequestBody EventDto.Request eventUpdateRequest){
        // update Event
        EventDto.Response response = eventService.updateEvent(eventId, eventUpdateRequest);
        // if payer not in parti_list, then add payer to currPartiDtoList
        List<ParticipantDto.CreateEvent> currPartiDtoList =
                eventService.validatePayerInPartiList(eventUpdateRequest);

        // get updateRequests from prevPartiDtoList
        Map<Long, PersonDto.MoneyUpdate> updateRequests =
                participantService.setMoneyUpdateRequestMap(
                        participantService.getPartiDtoListInEvent(eventId),
                        eventService.getEventPriceById(eventId),
                        eventUpdateRequest.getPrice());

        // for each participant,
        for(ParticipantDto.CreateEvent partiDto : currPartiDtoList){
            WalloudCode isParticipatedChange = participantService.validateIsParticipated(updateRequests, partiDto);
            if (isParticipatedChange == STILL_PARTICIPATED){
                PersonDto.MoneyUpdate currRequest = updateRequests.get(partiDto.getPersonId());
                currRequest.setCurrEventRole(partiDto.getRole());
                currRequest.setCurrChargedPrice(partiDto.getSpent());
                participantService.updateParticipant(
                        partiDto.getRole(),
                        partiDto.getSpent(),
                        partiDto.getPersonId(),
                        Long.valueOf(eventId));
                personService.updatePersonMoney(
                        PersonDto.tmp.builder()
                                .personId(partiDto.getPersonId())
                                .eventRole(currRequest.isPrevEventRole())
                                .eventPrice(Double.valueOf(currRequest.getPrevPrice()))
                                .chargedPrice(currRequest.getPrevChargedPrice())
                                .isCreate(false)
                                .build()
                );
                personService.updatePersonMoney(
                        PersonDto.tmp.builder()
                                .personId(partiDto.getPersonId())
                                .eventRole(currRequest.isCurrEventRole())
                                .eventPrice(Double.valueOf(currRequest.getCurrPrice()))
                                .chargedPrice(currRequest.getCurrChargedPrice())
                                .isCreate(true)
                                .build()
                );
            }
            else if (isParticipatedChange == NEW_PARTICIPANT) {
                participantService.createParticipant(
                        participantService.setParticipantRequest(partiDto, response));
                personService.updatePersonMoney(
                        PersonDto.tmp.builder()
                                .personId(partiDto.getPersonId())
                                .eventRole(partiDto.getRole())
                                .eventPrice(Double.valueOf(eventUpdateRequest.getPrice()))
                                .chargedPrice(partiDto.getSpent())
                                .isCreate(true)
                                .build()
                );
            }
        }

        for(Long personId : updateRequests.keySet()){ // Deleted participant
            PersonDto.MoneyUpdate currRequest = updateRequests.get(personId);
            if(currRequest.getCurrChargedPrice().equals(-1.0)){
                personService.updatePersonMoney(
                        PersonDto.tmp.builder()
                                .personId(personId)
                                .eventRole(currRequest.isPrevEventRole())
                                .eventPrice(Double.valueOf(eventService.getEventPriceById(eventId)))
                                .chargedPrice(currRequest.getPrevChargedPrice())
                                .isCreate(false)
                                .build()
                );
                participantService.deleteParticipant(personId, Long.valueOf(eventId));
            }
        }

        // change person role in this Travel
        personService.updatePersonRole(travelId);
        // return created event id
        return response.getEventId();
    }















    @GetMapping("/{eventId}/detail")
    public List<ParticipantDto.Detail> getDetailInEvent(@PathVariable int eventId) {
        return participantService.getPartiDtoListInEvent(eventId);
    }

    @GetMapping("/{eventId}/getEventImage")
    public String getEventImage(@PathVariable int eventId){
        return eventService.getEventImageURL(eventId);
    }

    @PutMapping("/{eventId}/updateEventImage")
    public String updateEventImage(@PathVariable int eventId,
                                   @RequestPart(value="file") MultipartFile file)
            throws IOException {
        return eventService.updateEventImage(
                eventId,
                s3UploaderService.upload(file, EVENT_FOLDER.getDescription()));
    }
}
