package com.spring.mydiv.Service;

import com.spring.mydiv.Dto.PersonCreateDto;
import com.spring.mydiv.Dto.PersonDto;
import com.spring.mydiv.Dto.TravelCreateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TravelServiceTest {
    @Autowired(required=true)
    private TravelService travelService;
    @Test
    @Commit
    @DisplayName("여행만 생성")
    void createTravel() {
        //given
        TravelCreateDto.Request travelInfo = TravelCreateDto.Request.builder()
                .Name("new_travel")
                .build();
        //when
        TravelCreateDto.Response response = travelService.createTravel(travelInfo);
        //then
        System.out.println("id = " + response.getId());
    }
}