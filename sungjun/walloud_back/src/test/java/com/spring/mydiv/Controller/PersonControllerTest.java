package com.spring.mydiv.Controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class PersonControllerTest {

    @Autowired(required=true)
    private PersonController personController;

//    @Test
//    @Commit
//    @DisplayName("회원 초대")
//    void createPerson2Travel() {
//        //given
//        int travelId = 57;
//        Map<String, Object> map = new HashMap<>();
//        map.put("user_email", "star4007lg@gm.gist.ac.kr");
//
//        //when
//        personController.createPerson2Travel(travelId, map);
//
//        //then
//        System.out.println("fin");
//    }
}