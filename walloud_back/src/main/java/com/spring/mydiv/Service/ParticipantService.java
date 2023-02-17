package com.spring.mydiv.Service;

import com.spring.mydiv.Code.WalloudCode;
import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Entity.Event;
import com.spring.mydiv.Entity.Participant;
import com.spring.mydiv.Entity.Person;
import com.spring.mydiv.Exception.DefaultException;
import com.spring.mydiv.Repository.EventRepository;
import com.spring.mydiv.Repository.ParticipantRepository;
import com.spring.mydiv.Repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.spring.mydiv.Code.ErrorCode.*;
import static com.spring.mydiv.Code.WalloudCode.NEW_PARTICIPANT;
import static com.spring.mydiv.Code.WalloudCode.STILL_PARTICIPATED;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;
    private final PersonRepository personRepository;

    @Transactional
    public ParticipantDto.Response createParticipant(ParticipantDto.Request request){
        Participant participant = Participant.builder()
                .person(request.getPerson())
                .event(request.getEvent())
                .eventRole(request.getRole())
                .chargedPrice(request.getChargedPrice())
                .build();

        if (ResponseEntity.ok(participantRepository.save(participant)).getStatusCodeValue() == 200)
            return ParticipantDto.Response.fromEntity(participant);
        else throw new DefaultException(CREATE_PARTICIPANT_FAIL);
    }

    public ParticipantDto.Request setParticipantRequest(ParticipantDto.CreateEvent partiDto,
                                                        EventDto.Response eventResponse) {
        return ParticipantDto.Request.builder()
                .person(getPersonEntityByPersonId(partiDto.getPersonId()))
                .event(getEventEntityById(eventResponse.getEventId()))
                .role(partiDto.getRole())
                .chargedPrice(partiDto.getSpent())
                .build();
    }
    public Map<Long, PersonDto.MoneyUpdate> setMoneyUpdateRequestMap(
            List<ParticipantDto.Detail> prevPartiDtoList,
            int prevPrice, int currPrice) {
        Map<Long, PersonDto.MoneyUpdate> updateRequests = new HashMap<>();
        for(ParticipantDto.Detail prevParticipant : prevPartiDtoList) {
            updateRequests.put(
                    prevParticipant.getPersonId(), // key
                    PersonDto.MoneyUpdate.builder() // value
                            .prevEventRole(prevParticipant.isEventRole())
                            .currEventRole(false)
                            .prevPrice(prevPrice)
                            .currPrice(currPrice)
                            .prevChargedPrice(prevParticipant.getChargedPrice())
                            .currChargedPrice(-1.0)
                            .build());
        }
        return updateRequests;
    }

    public Double calculateChargedPrice(int eventPrice, int partiSize){
        return (double) eventPrice / partiSize; // 추후 업데이트에 따라 로직 변경 가능
    }

    @Transactional
    public void updateParticipant(Boolean eventRole, Double chargedPrice, Long personId, Long eventId){
        Participant participant = getPartiEntityByPersonIdAndEventId(personId, eventId);

        participant.setEventRole(eventRole);
        participant.setChargedPrice(chargedPrice);

        participantRepository.save(participant);
    }

    @Transactional
    public void deleteParticipant(Long personId, Long eventId){
        participantRepository.delete(getPartiEntityByPersonIdAndEventId(personId, eventId));
    }

    public void validateDoesPersonNotJoinedAnyEvent(int personId) {
        if (getPartiListByPersonId(personId).size() > 0)
            throw new DefaultException(INVALID_DELETE_EVENT_EXISTED);
    }
    public WalloudCode validateIsParticipated(Map<Long, PersonDto.MoneyUpdate> updateRequests,
                                              ParticipantDto.CreateEvent partiDto){
        if (updateRequests.containsKey(partiDto.getPersonId())) return STILL_PARTICIPATED;
        else return NEW_PARTICIPANT;
    }

    @Transactional(readOnly = true)
    public List<EventDto.Detail> getEventDtoListThatPersonJoin(int personId){
        List<EventDto.Detail> result = new ArrayList<>();
        List<Participant> partiList = getPartiListByPersonId(personId);
        for (Participant parti : partiList) {
            EventDto.Detail eventDto = EventDto.Detail.fromEntity(
                    getEventEntityById(parti.getEvent().getId())
            );

            if (parti.getEventRole() == true){ // Manager
                eventDto.setPayerId(Long.valueOf(personId));
                eventDto.setPayerName(getPersonEntityByPersonId(Long.valueOf(personId))
                        .getUser().getName()
                );
            }
            else { // others
                Participant payer = participantRepository.findByEvent_IdAndEventRole(
                        eventDto.getEventId(), true).
                        get();
                eventDto.setPayerId(payer.getId());
                eventDto.setPayerName(payer.getPerson().getUser().getName());
            }
            result.add(eventDto);
        }
        return result;
    }
    @Transactional(readOnly = true)
    public List<ParticipantDto.Detail> getPartiDtoListInEvent(int eventId){
        return participantRepository.findByEvent_Id(Long.valueOf(eventId))
                .stream()
                .map(ParticipantDto.Detail::fromEntity)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    private List<Participant> getPartiListByPersonId(int personId) {
        return participantRepository.findByPerson_Id(Long.valueOf(personId));
    }
    @Transactional(readOnly = true)
    private Person getPersonEntityByPersonId(Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new DefaultException(NO_USER)); // TODO - no person 으로 바꿔야 하는지?
    }
    @Transactional(readOnly = true)
    private Event getEventEntityById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new DefaultException(NO_EVENT));
    }
    @Transactional(readOnly = true)
    private Participant getPartiEntityByPersonIdAndEventId(Long personId, Long eventId) {
        return participantRepository.findByPerson_IdAndEvent_Id(personId, eventId)
                .orElseThrow(() -> new DefaultException(NO_USER));
    }
}
