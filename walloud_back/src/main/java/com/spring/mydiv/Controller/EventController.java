package com.spring.mydiv.Controller;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.spring.mydiv.Code.S3Code.EVENT_FOLDER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EventController {
    private final EventService eventService;
    private final PersonService personService;
    private final TravelService travelService;
    private final ParticipantService participantService;
    private final S3UploaderService s3UploaderService;

    @PostMapping("/{travelId}/createEvent")
    public Long createEvent(@PathVariable Long travelId,
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
                    participantService.setPartiRequest(partiDto, eventResponse));
            // update person(parti) sumSend etc.
            personService.updatePersonMoneyFromDto(
                    personService.setUpdateEntity(partiDto,
                            Double.valueOf(eventRequest.getPrice()),
                            true));
        }

        // update person role for this Travel
        personService.updatePersonRole(travelId);
        // return created event id
        return eventResponse.getEventId();
    }
    @DeleteMapping("/{travelId}/{eventId}/deleteEvent")
    public void deleteEvent(@PathVariable("travelId") Long travelId,
                            @PathVariable("eventId") Long eventId) {
        // get participant list in this event
        List<ParticipantDto.CRUDEvent> partiList =
                participantService.getPartiDtoListInEvent(eventId);
        // update all person(participant) sumSend etc. in this travel before deleting
        for (ParticipantDto.CRUDEvent partiDto : partiList){
            personService.updatePersonMoneyFromDto(
                    personService.setUpdateEntity(partiDto,
                            Double.valueOf(eventService.getEventPriceById(eventId)),
                            false));
        }
        // update person role for this Travel
        personService.updatePersonRole(travelId);
        // delete event
        eventService.deleteEvent(eventId);
    }
    @PostMapping("/{travelId}/{eventId}/updateEvent")
    public Long updateEvent(@PathVariable("travelId") Long travelId,
                            @PathVariable("eventId") Long eventId,
                            @RequestBody EventDto.Request eventUpdateRequest) {
        // get prev & curr price of event
        int prevEventPrice = eventService.getEventPriceById(eventId);
        int currEventPrice = eventUpdateRequest.getPrice();

        // update event
        EventDto.Response response =
                eventService.updateEvent(eventId, eventUpdateRequest);

        // if new payer not in parti_list, then add payer to currPartiDtoList
        Map<Long, ParticipantDto.forUpdateEvent> participatedChangeMap =
                participantService.setPartiChangeMap(
                        eventService.validatePayerInPartiList(eventUpdateRequest),
                        participantService.getPartiDtoListInEvent(eventId)
                );
        // delete NOW_NOT_PARTICIPATED, create NEW_PARTICIPANT, update STILL_PARTICIPATED
        // and update person(parti) sumSend etc.
        personService.updatePersonAndParticipant(
                response, participatedChangeMap,
                Double.valueOf(prevEventPrice), Double.valueOf(currEventPrice));

        // change person role in this Travel
        personService.updatePersonRole(travelId);
        // return created event id
        return response.getEventId();
    }
    @PutMapping("/{eventId}/updateEventImage")
    public String updateEventImage(@PathVariable Long eventId,
                                   @RequestPart(value="file") MultipartFile file)
            throws IOException {
        S3Dto.ImageUrls urls = eventService.updateEventImage(
                eventId, s3UploaderService.upload(file, EVENT_FOLDER.getDescription()));
        s3UploaderService.deleteImage(urls.getDeleteImage());
        return urls.getNewImage();
    }

    @GetMapping("/{eventId}/getPartiListInEvent")
    public List<ParticipantDto.CRUDEvent> getPartiListInEvent(
            @PathVariable Long eventId) {
        return participantService.getPartiDtoDetailVerListInEvent(eventId);
    }
    @GetMapping("/{eventId}/getEventDetail")
    public EventDto.Detail getEventDetail(@PathVariable Long eventId) {
        return eventService.getEventDetail(eventId);
    }
    @GetMapping("/{eventId}/getEventImage")
    public String getEventImage(@PathVariable Long eventId){
        return eventService.getEventImageURL(eventId);
    }
}
