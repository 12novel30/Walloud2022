package com.spring.mydiv.Controller;

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

    @PostMapping("/{travelId}/createEvent") // TODO check
    public Long createEvent(@PathVariable int travelId,
                            @RequestBody EventDto.Request eventRequest) {
        // get Travel Information
        eventRequest.setTravelDto(travelService.getTravelResponse(travelId));
        // create event
        EventDto.Response eventResponse = eventService.createEvent(eventRequest);

        // if payer not in parti_list, then add payer to parti_list
        List<ParticipantDto.CRUDEvent> partiDtoList =
                eventService.validatePayerInPartiList(eventRequest);
        // for each participant,
        for (ParticipantDto.CRUDEvent partiDto : partiDtoList){
            // create participant
            participantService.createParticipant(
                    participantService.setParticipantRequest(partiDto, eventResponse));
            // update person(parti) sumSend etc.
            personService.updatePersonMoneyFromDto( // TODO check
                    PersonDto.tmp.builder()
                            .personId(partiDto.getPersonId())
                            .eventRole(partiDto.getRole())
                            .eventPrice(Double.valueOf(eventRequest.getPrice()))
                            .chargedPrice(partiDto.getSpent())
                            .isCreate(true)
                            .build()
            );
        }

        // update person role for this Travel
        personService.updatePersonRole(travelId);
        // return created event id
        return eventResponse.getEventId();
    }

    @DeleteMapping("/{travelId}/{eventId}/deleteEvent") // TODO check
    public void deleteEvent(@PathVariable("travelId") int travelId,
                            @PathVariable("eventId") int eventId)
    {
        // get participant list in this event
        List<ParticipantDto.CRUDEvent> partiList =
                participantService.getPartiCRUDEventDtoListInEvent(eventId);
        // update all person(participant) sumSend etc. in this travel before deleting
        for (ParticipantDto.CRUDEvent partiDto : partiList){
            personService.updatePersonMoneyFromDto(
                    PersonDto.tmp.builder() // TODO check
                            .personId(partiDto.getPersonId())
                            .eventRole(partiDto.getRole())
                            .eventPrice(Double.valueOf(
                                    eventService.getEventPriceById(eventId)))
                            .chargedPrice(partiDto.getSpent())
                            .isCreate(false)
                            .build()
            );
        }
        // update person role for this Travel
        personService.updatePersonRole(travelId);
        // delete event
        eventService.deleteEvent(eventId);
    }
    @PostMapping("/{travelId}/{eventId}/updateEvent")
    public Long updateEvent(
            @PathVariable("travelId") int travelId, @PathVariable("eventId") int eventId,
            @RequestBody EventDto.Request eventUpdateRequest){
        // get prev & curr price of event
        int prevEventPrice = eventService.getEventPriceById(eventId);
        int currEventPrice = eventUpdateRequest.getPrice();

        // update event
        EventDto.Response response =
                eventService.updateEvent(eventId, eventUpdateRequest);

        // if new payer not in parti_list, then add payer to currPartiDtoList
        // and delete NOW_NOT_PARTICIPATED, create NEW_PARTICIPANT, update STILL_PARTICIPATED
        Map<Long, ParticipantDto.CRUDEvent> participatedChangeMap =
                participantService.validateParticipatedChange(response,
                        eventService.validatePayerInPartiList(eventUpdateRequest),
                        participantService.getPartiCRUDEventDtoListInEvent(eventId)
                );
        // update person(parti) sumSend etc.
        personService.updatePersonMoneyAllType(participatedChangeMap, prevEventPrice, currEventPrice);

        // change person role in this Travel
        personService.updatePersonRole(travelId);
        // return created event id
        return response.getEventId();
    }






    @GetMapping("/{eventId}/getPartiListInEvent") // TODO - fin
    public List<ParticipantDto.Detail> getPartiListInEvent(@PathVariable int eventId) {
        return participantService.getPartiDetailDtoListInEvent(eventId);
    }
    @GetMapping("/{eventId}/getEventDetail") // TODO - fin
    public EventDto.Detail getEventDetail(@PathVariable int eventId) {
        return eventService.getEventDetail(eventId);
    }

    @GetMapping("/{eventId}/getEventImage") // TODO - fin
    public String getEventImage(@PathVariable int eventId){
        return eventService.getEventImageURL(eventId);
    }

    // TODO - image 관련 메소드 하나로 합치기
    @PutMapping("/{eventId}/updateEventImage") // TODO - fin
    public String updateEventImage(@PathVariable int eventId,
                                   @RequestPart(value="file") MultipartFile file)
            throws IOException {
        return eventService.updateEventImage(
                eventId,
                s3UploaderService.upload(file, EVENT_FOLDER.getDescription()));
    }
}
