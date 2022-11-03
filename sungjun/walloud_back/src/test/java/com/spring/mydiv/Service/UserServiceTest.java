package com.spring.mydiv.Service;

import com.spring.mydiv.Dto.TravelDto;
import com.spring.mydiv.Dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;


@SpringBootTest
class UserServiceTest {
    @Autowired(required=true)
    private UserService userService;

//    @Test
//    @Commit
//    @DisplayName("회원가입")
//    void createUser() {
//        //given
//        UserDto.Request request = UserDto.Request.builder()
//                .Name("황장원")
//                .Email("star4007lg@gm.gist.ac.kr")
//                .Password("20205197")
//                .Account("1111111111")
//                .build();
//        //when
//        UserDto.Response response = userService.createUser(request);
//        //then
//        System.out.print("name = " + response.getName());
//        //fail("Not yet implemented"); // TODO
//    }
//
//    @Test
//    @Commit
//    @DisplayName("로그인")
//    void login() {
//        //given
//        UserDto.Login login = UserDto.Login.builder()
//                .Email("leehaeun@gm.gist.ac.kr")
//                .Password("20205149")
//                .build();
//        //when
//        int response = userService.login(login);
//        //then
//        System.out.println(response);
//        //fail("Not yet implemented"); // TODO
//    }
//
//    @Test
//    @Commit
//    @DisplayName("유저 정보 리턴")
//    void getUserInfo() {
//        //given
//        int userid = 11;
//        /** user id info
//         * 13: 이하은
//         * 14: 황장원
//         */
//
//        //when
//        UserDto.Response info = userService.getUserInfo(userid);
//
//        //then
//        System.out.println("Name: "+info.getName());
//        System.out.println("Email: "+info.getEmail());
//        System.out.println("Account: "+info.getAccount());
//    }
//
//    @Test
//    @Commit
//    @DisplayName("여행 리스트 리턴")
//    void getUserJoinedTravel() {
//        //given
//        //when
//        List<TravelDto.Response> list = userService.getUserJoinedTravel(13);
//
//        //then
//        for (TravelDto.Response t : list){
//            System.out.println("Name: "+ t.getName().toString());
//        }
//    }
//
//    @Test
//    @Commit
//    @DisplayName("유저 정보 리턴 + 여행리스트까지")
//    void getUserInfoWithTravel() {
//        //given
//        int userid = 13;
//
//        //when
//        UserDto.WithTravel info = userService.getUserInfoWithTravel(userid);
//
//        //then
//        System.out.println("Name: "+info.getName());
//        System.out.println("Email: "+info.getEmail());
//        System.out.println("Account: "+info.getAccount());
//        for(TravelDto.Response t : info.getTravelList()){
//            System.out.println("Travel Name: "+t.getName().toString());
//        }
//    }
}