package com.spring.mydiv.Service;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Entity.*;
import com.spring.mydiv.Exception.DefaultException;
import com.spring.mydiv.Repository.EventRepository;
import com.spring.mydiv.Repository.ParticipantRepository;
import com.spring.mydiv.Repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public EventDto.Response createEvent(EventDto.Request request) {
        Event event = Event.builder()
                .name(request.getName())
                .date(request.getDate())
                .price(request.getPrice())
                .travel(Travel.builder()
                        .id(request.getTravel().getTravelId())
                        .name(request.getTravel().getName())
                        .build())
                .payerPersonid(request.getPayerPersonId())
                .build();
        eventRepository.save(event);
        return EventDto.Response.fromEntity(event);
    }

    public void updateEvent(int eventId, EventDto.Request request){
        Event event = eventRepository.findById(Long.valueOf(eventId))
                .orElseThrow(() -> new DefaultException(NO_EVENT));

        eventRepository.updateNameAndDateAndPriceAndPayerPersonidById(request.getName(),
                request.getDate(),
                request.getPrice(),
                request.getPayerPersonId(),
                Long.valueOf(eventId));
    }

    public List<Map> checkPayerInParticipant(List<Map> partiList, Long payerId){
        List<Long> partiIds = new ArrayList<>();
        for (Map parti : partiList){
            partiIds.add(Long.valueOf(parti.get("personId").toString()));
        }
        if(!partiIds.contains(payerId)){
            Map<String, String> payerMap = new HashMap<>();
            payerMap.put("role", "true");
            payerMap.put("personId", String.valueOf(payerId));
            payerMap.put("spent", "0");
            partiList.add(payerMap);
        }
        return partiList;
    }

    public List<EventDto.HomeView> getEventInfoInTravel(int travelId){
        List<Event> list = eventRepository.findByTravel_Id(Long.valueOf(travelId));
        List<EventDto.HomeView> result = new ArrayList<>();
        for (Event e : list){
            EventDto.HomeView event = EventDto.HomeView.fromEntity(e);
            Person payer = personRepository.findById(e.getPayerPersonid())
                    .orElseThrow(()-> new DefaultException(NO_PAYER)); // Error 발생
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

}
