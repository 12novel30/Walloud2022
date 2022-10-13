package com.spring.mydiv.Service;

import com.spring.mydiv.Dto.EventDto;
import com.spring.mydiv.Entity.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Commit;

import java.util.Date;

@SpringBootTest
class EventServiceTest {

    @Autowired(required = true)
    private EventService eventService;
    @Autowired(required = true)
    private TravelService travelService;

//    @Test
//    @Commit
//    @DisplayName("이벤트 생성")
//    void createEvent() {
//        //given
//        Date date = new Date(22, 9, 1);
//        EventDto.Request request = EventDto.Request.builder()
//                .Name("우버 (포츠담 -> 호텔) ")
//                .Travel(travelService.getTravelInfo(78)) //서울 여행
//                .Date(date)
//                .Price(65000)
//                .build();
//        //when
//        EventDto.Response eventDto = eventService.createEvent(request);
//
//        //then
//        System.out.println("status: " + ResponseEntity.ok(eventDto).toString());
//        /**result
//         * status: <200 OK OK,com.spring.mydiv.Dto.EventCreateDto$Response@6d2693f,[]>
//         * event id: 2
//         * dividePrice: 12500.0
//         * takePrice: 12500.0
//         */
//    }
//
//    /*
//    @Test
//    @Commit
//    @DisplayName("이벤트에 참여한 사람들 return")
//    void getEventInfoInTravel() {
//        //given
//        int event_id = 78;
//        //when
//        List<Person> People = eventService.getJoinedPeopleInEvent(event_id);
//        //then
//        for (Person p : People) {
//            System.out.println(p.getId());
//        }
//    }
//    */
//
//    @Test
//    @Commit
//    @DisplayName("이벤트 업데이트")
//    void eventUpdateTest() {
//        //given
//        int event_id = 155;
//        int payer_id = 154;
//        Date date = new Date(22, 10, 1);
//        EventDto.Request request = EventDto.Request.builder()
//                .Name("구의역")
//                .Date(date)
//                .Price(160000)
//                .PayerPersonId((long) payer_id)
//                .build();
//        //when
//        eventService.updateEvent(event_id, request);
//        //then
//        System.out.println("check DB");
//    }
//
//    @Test
//    @Commit
//    @DisplayName("이벤트 생성")
//    void getTravelPeriod() {
//        //given
//
//        //when
//
//        //then
//    }
//
//    @Test
//    @Commit
//    @DisplayName("이벤트 뽑아오기")
//    void getEventEntityByEventId() {
//        //given
//        Long event_id = Long.valueOf(1); //대치동
//        //when
//        Event event = eventService.getEventEntityByEventId(event_id);
//        //then
//        System.out.println("name: " + event.getName());
//    }
//
//    @Test
//    @Commit
//    @DisplayName("이벤트 삭제하기")
//    void deleteEvent() {
//        //given
//        int event_id = 6;
//        //when
//        eventService.deleteEvent(event_id);
//        //then
//        System.out.println("check DB please!");
//    }

}