package com.spring.mydiv.Service;

import com.spring.mydiv.Dto.PersonDto;
import com.spring.mydiv.Repository.PersonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Commit;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;
    @InjectMocks
    private PersonService personService;

    private String printPersonDtoDetail(PersonDto.Detail detailView) {
        return "PersonId;\n" + detailView.getPersonId() +
                "SumSend;\n" + detailView.getSumSend() +
                "SumGet;\n" + detailView.getSumGet() +
                "Difference;\n" + detailView.getDifference() +
                "TravelRole;\n" + detailView.getTravelRole() +
                "isSettled;\n" + " " +
                "User;\n" + detailView.getPersonId() +
                "EventList;\n" + detailView.getPersonId() +
                "PersonInTravelList;" + detailView.getPersonId();
    }
    @Test
    void getPersonDetail(){
        //given
        int personId = 510;

        //when
        PersonDto.Detail detailView = personService.getPersonDetail(personId);

        //then
        System.out.println(printPersonDtoDetail(detailView));

    }

//    @Test
//    @Commit
//    @DisplayName("여행 생성")
//    void createPerson() {
//        //given
//        int userNo = 13;
//        TravelDto.Request travelInfo = TravelDto.Request.builder()
//                .Name("광주 여행")
//                .build();
//        PersonDto.Request request = PersonDto.Request.builder()
//                .User(userService.getUserInfo(userNo))
//                .Travel(travelService.createTravel(travelInfo))
//                .build();
//
//        //when
//        PersonDto.basic person = personService.createPerson(request, FALSE);
//
//        //then
//        System.out.println("User name = " + person.getUser().getName());
//        System.out.println("Travel name = " + person.getTravel().getName());
//    }
//
//    @Test
//    @Commit
//    @DisplayName("여행 참가자 리스트 리턴")
//    void getPersonNameInTravel(){
//        //given
//        int travelId = 57; //서울 여행
//
//        //when
//        List<PersonDto.Simple> list = personService.getPersonNameInTravel(travelId);
//
//        //then
//        for (PersonDto.Simple p : list){
//            System.out.println("Name: " + p.getName());
//        }
//
//    }
//
//    @Test
//    @Commit
//    @DisplayName("여행에서 참가자 삭제")
//    void deleteJoinTravel() {
//        //given
//        int personId = 100;
//
//        //when
//        personService.deleteJoinTravel(personId);
//
//        //then
//        System.out.println("check DB please!");
//    }
//
//    @Test
//    @Commit
//    @DisplayName("사람 뽑아오기")
//    void getPersonEntityByPersonId() {
//        //given
//        Long person_id = Long.valueOf(50); //이하은
//        //when
//        Person person = personService.getPersonEntityByPersonId(person_id);
//        //then
//        System.out.println("id: " + person.getId());
//        System.out.println("id: " + person.getSumSend());
//        System.out.println("id: " + person.getSumGet());
//        System.out.println("id: " + person.getDifference());
//        System.out.println("id: " + person.getRole());
//    }
//
//    @Test
//    @Commit
//    @DisplayName("이벤트 생성으로 person 정보 업데이트")
//    void updatePersonWithEvent() {
//    }
//
//    @Test
//    @Commit
//    @DisplayName("슈퍼 유저인지 확인하기")
//    void isSuperUser() {
//        System.out.println(personService.isPersonSuperuser(292));
//    }
//
//    @Test
//    @Commit
//    @DisplayName("사람 뽑아오기")
//    void getPersonToDetailView() {
//        //given
//        int person_id = 50; //이하은
//        //when
//        PersonDto.Detail detail = personService.getPersonToDetailView(person_id);
//        //then
//        System.out.println("personID: " + detail.getPersonId());
//        System.out.println("personID: " + detail.getUserName());
//        System.out.println("personID: " + detail.getUserEmail());
//        System.out.println("personID: " + detail.getUserAccount());
//        System.out.println("personID: " + detail.getSumSend());
//        System.out.println("personID: " + detail.getSumGet());
//        System.out.println("personID: " + detail.getDifference());
//        System.out.println("personID: " + detail.getTravelRole());
//    }
//    @Test
//    @Commit
//    @DisplayName("사람 뽑아오기")
//    void getPayerInTravel() {
//        //given
//        int travelId = 57; // 서울 여행
//        //when
//        PersonDto.HomeView tmp = personService.getPayerInTravel(travelId);
//        //then
//        System.out.println("payer : " + tmp.getPersonId());
//        System.out.println("payer : " + tmp.getName());
//    }
//
//    @Test
//    @Commit
//    @DisplayName("역할 세팅 테스트")
//    void setRoleTest() {
//        //given
//        int travelId = 91; // 베를린 여행
//        //when
//        personService.updatePersonRole(travelId);
//        //then
//        System.out.println("CheckOUT DB");
//    }
//
//
////    @Test
////    @Commit
////    @DisplayName("사람 뽑아오기")
////    void getPersonEntityByPersonId() {
////        //given
////        //when
////        //then
////    }

}