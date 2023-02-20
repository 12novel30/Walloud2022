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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.spring.mydiv.Code.ErrorCode.*;
import static com.spring.mydiv.Code.WalloudCode.*;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;
    private final PersonRepository personRepository;


    @Transactional
    public void createParticipant(ParticipantDto.Request request){
        Participant participant = Participant.builder()
                .person(request.getPerson())
                .event(request.getEvent())
                .eventRole(request.getRole())
                .chargedPrice(request.getChargedPrice())
                .build();

        if (ResponseEntity.ok(participantRepository.save(participant))
                .getStatusCodeValue() != 200)
            throw new DefaultException(CREATE_PARTICIPANT_FAIL);
    }

    public Map<Long, ParticipantDto.forUpdateEvent> setPartiChangeMap(
            List<ParticipantDto.CRUDEvent> currPartiDtoList,
            List<ParticipantDto.CRUDEvent> prevPartiDtoList) {
        Map<Long, ParticipantDto.forUpdateEvent> map = new HashMap<>();

        // 1st. 변경된(curr) participant 리스트를 map 에 추가
        for (ParticipantDto.CRUDEvent currDto : currPartiDtoList)
            map.put(currDto.getPersonId(),
                    ParticipantDto.forUpdateEvent.builder()
                            .curr(currDto)
                            .isParticipatedChange(NEW_PARTICIPANT)
                            .build());
        // 2nd. 기존(prev) participant 리스트를 map 에 추가/수정
        for (ParticipantDto.CRUDEvent prevDto : prevPartiDtoList){
            // 2-1. curr list 에 없는 parti 일 경우, NOW_NOT_PARTICIPATED
            if (!map.containsKey(prevDto.getPersonId())) {
                map.put(prevDto.getPersonId(),
                        ParticipantDto.forUpdateEvent.builder()
                                .prev(prevDto)
                                .isParticipatedChange(NOW_NOT_PARTICIPATED)
                                .build());
            }
            // 2-2. curr list 에 있는 parti 일 경우, STILL_PARTICIPATED & spent 변경
            else {
                ParticipantDto.forUpdateEvent changing =
                        map.get(prevDto.getPersonId());
                changing.setPrev(prevDto);
                changing.setIsParticipatedChange(STILL_PARTICIPATED);
                map.replace(prevDto.getPersonId(), changing);
            }
        }

        return map;
    }
    public ParticipantDto.Request setPartiRequest(
            ParticipantDto.CRUDEvent partiDto,
            EventDto.Response eventResponse) {
        return ParticipantDto.Request.builder()
                .person(getPersonEntityByPersonId(partiDto.getPersonId()))
                .event(getEventEntityById(eventResponse.getEventId()))
                .role(partiDto.getRole())
                .chargedPrice(partiDto.getSpent())
                .build();
    }

    public Double calculateChargedPrice(int eventPrice, int partiSize) {
        // TODO - 추후 업데이트에 따라 로직 변경 가능
        return (double) eventPrice / partiSize;
    }

    @Transactional
    public void updateParticipant(ParticipantDto.CRUDEvent partiDto, Long eventId) {
        Participant participant =
                getPartiEntityByPersonIdAndEventId(partiDto.getPersonId(), eventId);

        participant.setEventRole(partiDto.getRole());
        participant.setChargedPrice(partiDto.getSpent());

        participantRepository.save(participant);
    }

    @Transactional
    public void deleteParticipant(Long personId, Long eventId) {
        participantRepository.delete(
                getPartiEntityByPersonIdAndEventId(personId, eventId));
    }

    public void validateDoesPersonNotJoinedAnyEvent(Long personId) {
        if (getPartiListByPersonId(personId).size() > 0)
            throw new DefaultException(INVALID_DELETE_EVENT_EXISTED);
    }

    @Transactional(readOnly = true)
    public List<EventDto.Detail> getEventDtoListThatPersonJoin(Long personId) {
        List<EventDto.Detail> result = new ArrayList<>();
        // get participant list (= person)
        List<Participant> partiList = getPartiListByPersonId(personId);
        for (Participant parti : partiList) {
            // get eventDto that this participant join
            // +) parti->eventDto 과정에서 role 에 따른 payer 설정이 완료되어있기 때문에
            // +) 별도로 role 에 따라 새로 payer 를 재설정할 필요 없다.
            EventDto.Detail eventDto = EventDto.Detail.fromEntity(
                    getEventEntityById(parti.getEvent().getId()));
            eventDto.setPayerName(getPersonEntityByPersonId(eventDto.getPayerId())
                    .getUser().getName());
            result.add(eventDto);
        }
        return result;
    }
    @Transactional(readOnly = true)
    public List<ParticipantDto.CRUDEvent> getPartiDtoListInEvent(Long eventId){
        return participantRepository.findByEvent_Id(eventId)
                .stream()
                .map(ParticipantDto.CRUDEvent::fromEntity)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<ParticipantDto.CRUDEvent> getPartiDtoDetailVerListInEvent(Long eventId){
        return participantRepository.findByEvent_Id(eventId)
                .stream()
                .map(ParticipantDto.CRUDEvent::fromEntityDetailVer)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    private List<Participant> getPartiListByPersonId(Long personId) {
        return participantRepository.findByPerson_Id(personId);
    }
    @Transactional(readOnly = true)
    private Person getPersonEntityByPersonId(Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new DefaultException(NO_USER));
        // TODO - no person 으로 바꿔야 하는지?
    }
    @Transactional(readOnly = true)
    private Event getEventEntityById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new DefaultException(NO_EVENT));
    }
    @Transactional(readOnly = true)
    private Participant getPartiEntityByPersonIdAndEventId(Long personId,
                                                           Long eventId) {
        return participantRepository.findByPerson_IdAndEvent_Id(personId, eventId)
                .orElseThrow(() -> new DefaultException(NO_USER));
    }
}
