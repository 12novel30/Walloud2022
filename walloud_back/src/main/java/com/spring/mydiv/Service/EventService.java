package com.spring.mydiv.Service;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Entity.*;
import com.spring.mydiv.Exception.DefaultException;
import com.spring.mydiv.Repository.EventRepository;
import com.spring.mydiv.Repository.ParticipantRepository;
import com.spring.mydiv.Repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.spring.mydiv.Code.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final PersonRepository personRepository;
    private final S3UploaderService s3UploaderService;
    @Transactional // TODO - fin
    public EventDto.Response createEvent(EventDto.Request request) {
        Event event = Event.builder()
                .name(request.getEvent_name())
                .date(request.getDate())
                .price(request.getPrice())
                .travel(Travel.builder()
                        .id(request.getTravelDto().getTravelId())
                        .name(request.getTravelDto().getName())
                        .build())
                .payerPersonid(request.getPayer_person_id())
                .build();
        if (ResponseEntity.ok(eventRepository.save(event)).getStatusCodeValue() == 200)
            return EventDto.Response.fromEntity(event);
        else throw new DefaultException(CREATE_EVENT_FAIL);
    }
    @Transactional // TODO - fin
    public EventDto.Response updateEvent(int eventId, EventDto.Request eventUpdateRequest){
        Event event = getEventEntityById(eventId);
        if (eventUpdateRequest.getEvent_name() != null) event.setName(eventUpdateRequest.getEvent_name());
        if (eventUpdateRequest.getDate() != null) event.setDate(eventUpdateRequest.getDate());
        if (eventUpdateRequest.getPrice() != 0) event.setPrice(eventUpdateRequest.getPrice());
        if (eventUpdateRequest.getPayer_person_id() != null) event.setPayerPersonid(eventUpdateRequest.getPayer_person_id());
        return EventDto.Response.fromEntity(eventRepository.save(event));
    }

    public List<ParticipantDto.CRUDEvent> validatePayerInPartiList( // TODO - fin
                                                                    EventDto.Request eventRequest) {
        List<ParticipantDto.CRUDEvent> partiList = eventRequest.getParti_list();
        Long payerId = eventRequest.getPayer_person_id();
        // partiList 에 payer 가 없으면 -> partiList 에 추가할 필요가 있음
        for (ParticipantDto.CRUDEvent parti : partiList)
            if (parti.getPersonId().equals(payerId)) {
                partiList.add(
                        ParticipantDto.CRUDEvent.builder()
                                .personId(payerId)
                                .role(true)
                                .spent(Double.valueOf(0))
                                .build());
                break;
            }
        return partiList;
    }
    @Transactional(readOnly = true)
    public List<EventDto.Detail> getEventInfoInTravel(int travelId){
        List<Event> list = eventRepository.findByTravel_Id(Long.valueOf(travelId));
        List<EventDto.Detail> result = new ArrayList<>();
        for (Event e : list){
            EventDto.Detail event = EventDto.Detail.fromEntity(e);
            event.setPayerName(
                    personRepository.findById(e.getPayerPersonid())
                            .orElseThrow(()-> new DefaultException(NO_MANAGER))
                            .getUser().getName());
            result.add(event);
        }
        return result;
    }

    public int getEventPriceById(int eventId){ // TODO - fin
        return getEventEntityById(eventId).getPrice();
    }
    @Transactional(readOnly = true)
    public String getTravelPeriod(int travelId, int eventCount){
        if (eventCount == 0) return null;
        else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date format1 = eventRepository.findFirstByTravel_IdOrderByDateDesc(Long.valueOf(travelId))
                    .getDate(); //latest
            Date format2 = eventRepository.findFirstByTravel_IdOrderByDateAsc(Long.valueOf(travelId))
                    .getDate(); //oldest
            long diffSec = (format1.getTime() - format2.getTime()) / 1000;
            long diffDays = diffSec / (24*60*60);
            String periodFormat =
                    dateFormat.format(format2) + " ~ " + dateFormat.format(format1) + ", "
                    + diffDays + " days";
            return periodFormat;
        }
    }
    @Transactional(readOnly = true)
    public Long getSuperUser(int travelId){
        return personRepository.findByTravel_IdAndIsSuper(Long.valueOf(travelId), true)
                .orElseThrow(()-> new DefaultException(NO_SUPERUSER))
                .getId();
    }
    @Transactional // TODO - fin
    public void deleteEvent(int eventId){
        List<Participant> participantList = participantRepository.findByEvent_Id(Long.valueOf(eventId));
        for(Participant participant : participantList)
            participantRepository.delete(participant);
        eventRepository.deleteById(Long.valueOf(eventId));
    }

    public String getEventImageURL(int userId){ // TODO - fin
        return getEventEntityById(userId).getImage();
    }
    @Transactional(readOnly = true)
    public EventDto.Detail getEventDetail(int userId){ // TODO - fin
        EventDto.Detail eventDto = EventDto.Detail.fromEntity(getEventEntityById(userId));
        eventDto.setPayerName(
                personRepository.findById(Long.valueOf(eventDto.getPayerId()))
                        .orElseThrow(() -> new DefaultException(NO_USER))
                        .getUser().getName());
        return eventDto;
    }
    @Transactional // TODO - fin
    public String updateEventImage(int eventId, String imageURL){
        Event event = getEventEntityById(eventId);
        /* TODO - deleteEventImage(event);
        *
        * public void deleteEventImage(Event event){
        * String eventExistingImage = event.getImage();
        * s3UploaderService.deleteImage(eventExistingImage);
        * }
        * */
        event.setImage(imageURL);
        return eventRepository.save(event).getImage();
    }
    @Transactional(readOnly = true)
    private Event getEventEntityById(int eventId) {
        return eventRepository.findById(Long.valueOf(eventId))
                .orElseThrow(() -> new DefaultException(NO_EVENT));
    }
}
