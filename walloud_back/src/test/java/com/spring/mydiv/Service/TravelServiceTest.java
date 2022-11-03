package com.spring.mydiv.Service;

import com.spring.mydiv.Dto.TravelDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@SpringBootTest
class TravelServiceTest {
    @Autowired(required=true)
    private TravelService travelService;
//    @Test
//    @Commit
//    @DisplayName("여행만 생성")
//    void createTravel() {
//        //given
//        TravelDto.Request travelInfo = TravelDto.Request.builder()
//                .Name("new_travel")
//                .build();
//        //when
//        TravelDto.Response response = travelService.createTravel(travelInfo);
//        //then
//    }
//
//    @Test
//    @Commit
//    @DisplayName("여행 정보 리턴")
//    void getTravelInfo() {
//        //given
//        int id = 57;
//
//        //when
//        TravelDto.Response info = travelService.getTravelInfo(id);
//
//        //then
//        System.out.println("Name: "+info.getName());
//    }
//
//    @Test
//    @Commit
//    @DisplayName("여행 자체를 삭제")
//    void deleteTravel() {
//        //given
//        int travelId = 57;
//
//        //when
//        travelService.deleteTravel(travelId);
//
//        //then
//        System.out.println("check DB please!");
//    }
}