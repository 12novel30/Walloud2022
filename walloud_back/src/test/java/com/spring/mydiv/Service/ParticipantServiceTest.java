package com.spring.mydiv.Service;

import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Entity.Event;
import com.spring.mydiv.Entity.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ParticipantServiceTest {

    @Autowired(required = true)
    private ParticipantService participantService;
    @Autowired(required = true)
    private PersonService personService;
    @Autowired(required = true)
    private EventService eventService;

//    @Test
//    @Commit
//    @DisplayName("참가자 생성")
//    void createParticipant() {
//        //given
//        Long person_id = Long.valueOf(50); //이하은
//        Long event_id = Long.valueOf(2); //대치동
//        ParticipantDto.Request partiRequest = ParticipantDto.Request.builder()
//                .person(personService.getPersonEntityByPersonId(person_id))
//                .event(eventService.getEventEntityByEventId(event_id))
//                .role(false)
//                .build();
//
//        //when
//        ParticipantDto.basic dto = participantService.createParticipant(partiRequest);
//
//        //then
//        System.out.println("status: " + ResponseEntity.ok(dto).toString());
//        System.out.println("role: " + dto.getEventRole());
//    }
//
//    @Test
//    @Commit
//    @DisplayName("참가자 생성")
//    void getEventListThatPersonJoin() {
//        //given
//        int person_id = 50; //이하은
//        //when
//        List<EventDto.PersonView> result = participantService.getEventListThatPersonJoin(person_id);
//        //then
//        for (EventDto.PersonView e : result){
//            System.out.println(">> event:");
//            System.out.println("event id: " + e.getEventId());
//            System.out.println("event name: " + e.getEventName());
//            System.out.println("date: " + e.getDate());
//            System.out.println("total price: " + e.getPrice());
//            System.out.println("payer id: " + e.getPayerId());
//            System.out.println("payer name: " + e.getPayerName());
//            System.out.println();
//        }
//    }
//
//    @Test
//    @Commit
//    @DisplayName("이벤트에 참가한 참여자 디테일 보기")
//    void findParticipantDetail() {
//        //given
//        int event_id = 90;
//        //when
//        List<ParticipantDto.detailView> result = participantService.getParticipantInEvent(event_id);
//        //then
//        for (ParticipantDto.detailView detail : result){
//            System.out.println(detail.getName());
//            System.out.println(detail.isEventRole());
//        }
//    } // checked
}