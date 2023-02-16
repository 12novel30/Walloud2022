package com.spring.mydiv.Service;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Entity.Event;
import com.spring.mydiv.Entity.Participant;
import com.spring.mydiv.Entity.Person;
import com.spring.mydiv.Exception.DefaultException;
import com.spring.mydiv.Repository.EventRepository;
import com.spring.mydiv.Repository.ParticipantRepository;
import com.spring.mydiv.Repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.spring.mydiv.Code.ErrorCode.*;

/**
 * @author 12nov
 */
@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;
    private final PersonRepository personRepository;

    @Transactional
    public ParticipantDto.basic createParticipant(ParticipantDto.Request request){
        Participant participant = Participant.builder()
                .person(request.getPerson())
                .event(request.getEvent())
                .eventRole(request.getRole())
                .chargedPrice(request.getChargedPrice())
                .build();
        participantRepository.save(participant);
        return ParticipantDto.basic.fromEntity(participant);
    }

    public Double calculateChargedPrice(int eventPrice, int partiSize){
            return (double) eventPrice / partiSize; // 추후 업데이트에 따라 로직 변경 가능
    }

    public List<EventDto.PersonView> getEventListThatPersonJoin(int personId){
        List<EventDto.PersonView> result = new ArrayList<>();
        // personid가 있는 parti를 전부 찾아서 -> 그 parti의 event id, event role를 get
        List<Participant> partiList = participantRepository.findByPerson_Id(Long.valueOf(personId));
        for (Participant p : partiList){
            Long eventId = p.getEvent().getId();
            Boolean myEventRole = p.getEventRole();
            // -> event id를 통해 나머지 값 get(EventId, EventName, Date, Price, DividePrice, TakePrice)
            EventDto.PersonView tmpEvent = EventDto.PersonView.fromEntity(eventRepository.findById(eventId).get());
            // -> if event role = payer
            if (myEventRole == true){
                //      -> PayerId는 자기 것으로,
                tmpEvent.setPayerId(Long.valueOf(personId));
                //      -> PayerName은 person db에서 get
                tmpEvent.setPayerName(personRepository.findById(Long.valueOf(personId))
                        .get().getUser().getName());
            } else {
                //      -> event id & event role==1 을 조건으로
                Participant payer = participantRepository.findByEvent_IdAndEventRole(eventId, true).get(); // 에러 발생
                //          -> in parti db) 결제자의 person id
                tmpEvent.setPayerId(payer.getId());
                //          -> in person db) 결제자의 name
                tmpEvent.setPayerName(personRepository.findById(Long.valueOf(payer.getPerson().getId()))
                        .get().getUser().getName());
            }
            result.add(tmpEvent);
        }
        return result;
    }

    public int getSizeOfPersonJoinedEventList(int personId){
        return participantRepository.findByPerson_Id(Long.valueOf(personId)).size();
    }

    public List<ParticipantDto.detailView> getParticipantInEvent(int eventId){
        List<Participant> participantList = participantRepository.findByEvent_Id(Long.valueOf(eventId));
        List<ParticipantDto.detailView> participantDetailList = new ArrayList<>();

        for(Participant participant : participantList){
            Long personId = participant.getPerson().getId();
            String participantName = participant.getPerson().getUser().getName();
            Boolean eventRole = participant.getEventRole();
            Double chargedPrice = participant.getChargedPrice();
            ParticipantDto.detailView participantDetail = new ParticipantDto.detailView(personId, participantName, eventRole, chargedPrice);
            participantDetailList.add(participantDetail);
        }

        return participantDetailList;
    }

    public void updateParticipant(Boolean eventRole, Double chargedPrice, Person person, Event event){
        Participant participant = participantRepository.findByPersonAndEvent(person, event)
                .orElseThrow(() -> new DefaultException(NO_USER));

        participant.setEventRole(eventRole);
        participant.setChargedPrice(chargedPrice);

        participantRepository.save(participant);
    }

    public void deleteParticipant(Person p, Event e){
        participantRepository.deleteByPersonAndEvent(p, e);
    }

    public void validatePersonNotJoinedAnyEvent(int personId) {
        if (participantRepository.findByPerson_Id(Long.valueOf(personId)).size() > 0)
            throw new DefaultException(INVALID_DELETE_EVENTEXISTED);
    }
}
