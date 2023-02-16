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

import javax.transaction.Transactional;
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
    private final ParticipantService participantService;
    private final S3UploaderService s3UploaderService;

    public EventDto.Response createEvent(EventDto.Request request) {
        Event event = Event.builder()
                .name(request.getEvent_name())
                .date(request.getDate())
                .price(request.getPrice())
                .travel(Travel.builder()
                        .id(request.getTravel().getTravelId())
                        .name(request.getTravel().getName())
                        .build())
                .payerPersonid(request.getPayer_person_id())
                .build();

        if (ResponseEntity.ok(eventRepository.save(event)).getStatusCodeValue() == 200)
            return EventDto.Response.fromEntity(event);
        else throw new DefaultException(CREATE_EVENT_FAIL);
    }

    public EventDto.Response updateEvent(int eventId, EventDto.Request updateRequest){
        Event event = eventRepository.findById(Long.valueOf(eventId))
                .orElseThrow(() -> new DefaultException(NO_EVENT));

        if (updateRequest.getEvent_name() != null) event.setName(updateRequest.getEvent_name());
        if (updateRequest.getDate() != null) event.setDate(updateRequest.getDate());
        if (updateRequest.getPrice() != 0) event.setPrice(updateRequest.getPrice());
        if (updateRequest.getPayer_person_id() != null) event.setPayerPersonid(updateRequest.getPayer_person_id());

        return EventDto.Response.fromEntity(eventRepository.save(event));
    }

    public List<ParticipantDto.CreateEvent> validatePayerInPartiList(EventDto.Request eventRequest){
        List<ParticipantDto.CreateEvent> partiList = eventRequest.getParti_list();
        Long payerId = eventRequest.getPayer_person_id();

        // partiList 에 payer 가 없으면 -> partiList 에 추가할 필요가 있음
        for (ParticipantDto.CreateEvent parti : partiList)
            if (parti.getPersonId().equals(payerId)) {
                partiList.add(ParticipantDto.CreateEvent.builder()
                        .personId(payerId)
                        .role(true)
                        .spent(Double.valueOf(0))
                        .build());
                break;
            }
        return partiList;
    }

    public List<EventDto.HomeView> getEventInfoInTravel(int travelId){
        List<Event> list = eventRepository.findByTravel_Id(Long.valueOf(travelId));
        List<EventDto.HomeView> result = new ArrayList<>();
        for (Event e : list){
            EventDto.HomeView event = EventDto.HomeView.fromEntity(e);
            Person payer = personRepository.findById(e.getPayerPersonid())
                    .orElseThrow(()-> new DefaultException(NO_MANAGER)); // Error 발생
            event.setPayerName(payer.getUser().getName());
            result.add(event);
        }
        return result;
    }

    public int getEventCountInTravel(int travelId){
        return eventRepository.countByTravel_Id(Long.valueOf(travelId));
    } //fin

    public int getEventPriceById(int eventId){
        return eventRepository.findById(Long.valueOf(eventId))
                .orElseThrow(() -> new DefaultException(NO_PRICE))
                .getPrice();
    }

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
            String periodFormat = dateFormat.format(format2) + " ~ " + dateFormat.format(format1)
                    + ", " + diffDays + " days";
            return periodFormat;
        }
    } //fin

    public Event getEventEntityByEventId(Long id){
        return eventRepository.findById(id)
                .orElseThrow(()-> new DefaultException(NO_EVENT));
    }

    public Long getSuperUser(int travelId){
        return personRepository.findByTravel_IdAndIsSuper(Long.valueOf(travelId), true)
                .orElseThrow(()-> new DefaultException(NO_SUPERUSER))
                .getId();
    }
    public void deleteEvent(int eventId){
        List<Participant> participantList = participantRepository.findByEvent_Id(Long.valueOf(eventId));
        for(Participant participant : participantList){
            participantRepository.delete(participant);
        }
        eventRepository.deleteById(Long.valueOf(eventId));
    }

    /************image************/
    public String getEventImageURL(int userId){
        return eventRepository.findById(Long.valueOf(userId))
                .map(EventDto.ResponseWithImage::fromEntity)
                .orElseThrow(()-> new DefaultException(NO_EVENT))
                .getImageurl();
    }

    @Transactional
    public String updateEventImage(int eventId, String imageURL){
        Event event = eventRepository.findById(Long.valueOf(eventId))
                .orElseThrow(() -> new DefaultException(NO_EVENT));
        // TODO - deleteEventImage(event);
        event.setImage(imageURL);
        return eventRepository.save(event).getImage();
    }

    public void deleteEventImage(Event event){
        String eventExistingImage = event.getImage();
        s3UploaderService.deleteImage(eventExistingImage);
    }
}
