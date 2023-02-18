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
import static com.spring.mydiv.Code.WalloudCode.*;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;
    private final PersonRepository personRepository;

    @Transactional // TODO - fin
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

    public ParticipantDto.Request setParticipantRequest(ParticipantDto.CRUDEvent partiDto, // TODO - fin
                                                        EventDto.Response eventResponse) {
        return ParticipantDto.Request.builder()
                .person(getPersonEntityByPersonId(partiDto.getPersonId()))
                .event(getEventEntityById(eventResponse.getEventId()))
                .role(partiDto.getRole())
                .chargedPrice(partiDto.getSpent())
                .build();
    }

    public Double calculateChargedPrice(int eventPrice, int partiSize) // TODO - 추후 업데이트에 따라 로직 변경 가능
    {
        return (double) eventPrice / partiSize;
    }

    @Transactional // TODO - testing
    public void updateParticipant(ParticipantDto.CRUDEvent partiDto, Long eventId){
        Participant participant = getPartiEntityByPersonIdAndEventId(partiDto.getPersonId(), eventId);

        participant.setEventRole(partiDto.getRole());
        participant.setChargedPrice(partiDto.getSpent());

        participantRepository.save(participant);
    }

    @Transactional // TODO - testing
    public void deleteParticipant(Long personId, Long eventId){
        participantRepository.delete(getPartiEntityByPersonIdAndEventId(personId, eventId));
    }

    public void validateDoesPersonNotJoinedAnyEvent(int personId) // TODO - fin
    {
        if (getPartiListByPersonId(personId).size() > 0)
            throw new DefaultException(INVALID_DELETE_EVENT_EXISTED);
    }
    public Map<Long, ParticipantDto.CRUDEvent> validateParticipatedChange(EventDto.Response response,
                                                                          List<ParticipantDto.CRUDEvent> currPartiDtoList, // TODO - testing
                                                                          List<ParticipantDto.CRUDEvent> prevPartiDtoList) {
        Map<Long, ParticipantDto.CRUDEvent> participatedChangeMap = new HashMap<>();

        // 1st. 변경된(curr) participant 리스트를 map 에 추가
        for (ParticipantDto.CRUDEvent currDto : currPartiDtoList){
            currDto.setIsParticipatedChange(NEW_PARTICIPANT);
            participatedChangeMap.put(currDto.getPersonId(), currDto);
            // create entity
            createParticipant(setParticipantRequest(currDto, response));
        }
        // 2nd. 기존(prev) participant 리스트를 map 에 추가/수정
        for (ParticipantDto.CRUDEvent prevDto : prevPartiDtoList){
            // 2-1. curr list 에 없는 parti 일 경우, NOW_NOT_PARTICIPATED
            if (!participatedChangeMap.containsKey(prevDto.getPersonId())){
                prevDto.setIsParticipatedChange(NOW_NOT_PARTICIPATED);
                participatedChangeMap.put(prevDto.getPersonId(), prevDto);
                // delete entity
                deleteParticipant(prevDto.getPersonId(), response.getEventId());

            }
            // 2-2. curr list 에 있는 parti 일 경우, STILL_PARTICIPATED & spent 변경
            else {
                ParticipantDto.CRUDEvent changing = participatedChangeMap.get(prevDto.getPersonId());
                changing.setIsParticipatedChange(STILL_PARTICIPATED);
                changing.setSpent(changing.getSpent() - prevDto.getSpent());
                changing.setRole(prevDto.getRole());
                participatedChangeMap.replace(prevDto.getPersonId(), changing);
                // update entity
                updateParticipant(prevDto, response.getEventId());
            }
        }
        return participatedChangeMap;
    }

    @Transactional(readOnly = true) // TODO - testing
    public List<EventDto.Detail> getEventDtoListThatPersonJoin(int personId){
        List<EventDto.Detail> result = new ArrayList<>();
        // get participant list (= person)
        List<Participant> partiList = getPartiListByPersonId(personId);
        for (Participant parti : partiList) {
            // get eventDto that this participant join
            EventDto.Detail eventDto = EventDto.Detail.fromEntity(
                    getEventEntityById(parti.getEvent().getId()));
            eventDto.setPayerName(
                    getPersonEntityByPersonId(Long.valueOf(eventDto.getPayerId()))
                            .getUser().getName());
            // TODO 근데 여기에서 가져온 parti->eventDto 에 이미 정보가 다 있는거 아닌가 .....???
//            // if participant Event Role == Manager
//            if (parti.getEventRole() == true){
//                // set eventDto payer information
//                eventDto.setPayerId(Long.valueOf(personId));
//                eventDto.setPayerName(getPersonEntityByPersonId(Long.valueOf(personId))
//                        .getUser().getName()
//                );
//            }
//            // if participant Event Role == others
//            else {
//                // set payer that
//                Participant payer = participantRepository.findByEvent_IdAndEventRole(
//                        eventDto.getEventId(), true).get();
//                eventDto.setPayerId(payer.getId());
//                eventDto.setPayerName(payer.getPerson().getUser().getName());
//            }
            result.add(eventDto);
        }
        return result;
    }
    @Transactional(readOnly = true) // TODO - testing
    public List<ParticipantDto.CRUDEvent> getPartiCRUDEventDtoListInEvent(int eventId){
        return participantRepository.findByEvent_Id(Long.valueOf(eventId))
                .stream()
                .map(ParticipantDto.CRUDEvent::fromEntity)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true) // TODO - fin
    public List<ParticipantDto.Detail> getPartiDetailDtoListInEvent(int eventId){
        return participantRepository.findByEvent_Id(Long.valueOf(eventId))
                .stream()
                .map(ParticipantDto.Detail::fromEntity)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true) // TODO - fin
    private List<Participant> getPartiListByPersonId(int personId) {
        return participantRepository.findByPerson_Id(Long.valueOf(personId));
    }
    @Transactional(readOnly = true) // TODO - testing
    private Person getPersonEntityByPersonId(Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new DefaultException(NO_USER)); // TODO - no person 으로 바꿔야 하는지?
    }
    @Transactional(readOnly = true) // TODO - fin
    private Event getEventEntityById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new DefaultException(NO_EVENT));
    }
    @Transactional(readOnly = true) // TODO - fin
    private Participant getPartiEntityByPersonIdAndEventId(Long personId, Long eventId) {
        return participantRepository.findByPerson_IdAndEvent_Id(personId, eventId)
                .orElseThrow(() -> new DefaultException(NO_USER));
    }
}
