package com.spring.mydiv.Controller;

import com.spring.mydiv.Dto.TravelDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@SpringBootTest
class TravelControllerTest {

    @Autowired(required = true)
    private TravelController travelController;

//    @Test
//    @Commit
//    @DisplayName("여행 메인 뷰")
//    void getTravelToMainView(){
//        //given
//        int travelId = 57;
//        String travelName = "서울 여행";
//
//        //when
//        TravelDto.HomeView homeView = travelController.getTravelToMainView(travelId);
//
//        //then
//        System.out.println(homeView.getPeriod());
//        System.out.println(homeView.getTravelId());
//    }
}