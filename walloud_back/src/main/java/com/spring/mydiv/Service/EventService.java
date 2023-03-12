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

    @Transactional
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

        if (ResponseEntity.ok(eventRepository.save(event))
                .getStatusCodeValue() == 200)
            return EventDto.Response.fromEntity(event);
        else throw new DefaultException(CREATE_FAIL);
    }

    @Transactional
    public EventDto.Response updateEvent(Long eventId,
                                         EventDto.Request eventUpdateRequest){
        Event event = getEventEntityById(eventId);

        if (eventUpdateRequest.getEvent_name() != null)
            event.setName(eventUpdateRequest.getEvent_name());
        if (eventUpdateRequest.getDate() != null)
            event.setDate(eventUpdateRequest.getDate());
        if (eventUpdateRequest.getPrice() != 0)
            event.setPrice(eventUpdateRequest.getPrice());
        if (eventUpdateRequest.getPayer_person_id() != null)
            event.setPayerPersonid(eventUpdateRequest.getPayer_person_id());

        return EventDto.Response.fromEntity(eventRepository.save(event));
    }
    @Transactional
    public S3Dto.ImageUrls updateEventImage(Long eventId, String imageURL){
        Event event = getEventEntityById(eventId);
        String prevImage = event.getImage();
        event.setImage(imageURL);
        return S3Dto.ImageUrls.builder()
                .newImage(eventRepository.save(event).getImage())
                .deleteImage(prevImage)
                .build();
    }

    public List<ParticipantDto.CRUDEvent> validatePayerInPartiList(
            EventDto.Request eventRequest) {
        List<ParticipantDto.CRUDEvent> partiList = eventRequest.getParti_list();
        Long payerId = eventRequest.getPayer_person_id();

        Boolean isPayerParticipated = false;
        for (ParticipantDto.CRUDEvent parti : partiList)
            if (parti.getPersonId().equals(payerId)) {
                isPayerParticipated = true;
                break;
            }
        // partiList 에 payer 가 없으면 -> partiList 에 추가해야한다.
        if (!isPayerParticipated) {
            partiList.add(ParticipantDto.CRUDEvent.builder()
                    .personId(payerId)
                    .role(true)
                    .spent(Double.valueOf(0))
                    .build());
        }

        return partiList;
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        List<Participant> participantList =
                participantRepository.findByEvent_Id(eventId);
        for(Participant participant : participantList)
            participantRepository.delete(participant);
        eventRepository.deleteById(eventId);
    }

    public int getEventPriceById(Long eventId) {
        return getEventEntityById(eventId).getPrice();
    }
    public String getEventImageURL(Long userId) {
        return getEventEntityById(userId).getImage();
    }
    @Transactional(readOnly = true)
    public String getTravelPeriod(Long travelId, int eventCount){
        if (eventCount == 0) return null;
        else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date latestDate =
                    eventRepository.findFirstByTravel_IdOrderByDateDesc(travelId)
                            .getDate();
            Date oldestDate =
                    eventRepository.findFirstByTravel_IdOrderByDateAsc(travelId)
                            .getDate();
            long diffSec = (latestDate.getTime() - oldestDate.getTime()) / 1000;
            long diffDays = diffSec / (24*60*60);
            String periodFormat = dateFormat.format(oldestDate) + " ~ " +
                    dateFormat.format(latestDate) + ", "
                    + diffDays + " days";
            return periodFormat;
        }
    }
    @Transactional(readOnly = true)
    public Long getSuperUser(Long travelId) {
        return personRepository.findByTravel_IdAndIsSuper(travelId, true)
                .orElseThrow(()-> new DefaultException(NO_SUPERUSER))
                .getId();
    }
    @Transactional(readOnly = true)
    public List<EventDto.Detail> getEventInfoInTravel(Long travelId){
        List<Event> list = eventRepository.findByTravel_Id(travelId);
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
    @Transactional(readOnly = true)
    public EventDto.Detail getEventDetail(Long userId){
        EventDto.Detail eventDto = EventDto.Detail.fromEntity(
                getEventEntityById(userId));
        eventDto.setPayerName(
                personRepository.findById(
                        eventDto.getPayerId())
                        .orElseThrow(() -> new DefaultException(NO_USER))
                        .getUser().getName());
        return eventDto;
    }
    @Transactional(readOnly = true)
    private Event getEventEntityById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new DefaultException(NO_EVENT));
    }
}
