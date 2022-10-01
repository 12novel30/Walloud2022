package com.spring.mydiv.Service;

import com.spring.mydiv.Dto.PersonDto;
import com.spring.mydiv.Dto.TravelDto;
import com.spring.mydiv.Entity.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

@SpringBootTest
class PersonServiceTest {
    @Autowired(required=true)
    private PersonService personService;
    @Autowired(required=true)
    private UserService userService;
    @Autowired(required=true)
    private TravelService travelService;
    @Autowired(required=true)
    private EventService eventService;

    @Test
    @Commit
    @DisplayName("여행 생성")
    void createPerson() {
        //given
        int userNo = 13;
        TravelDto.Request travelInfo = TravelDto.Request.builder()
                .Name("광주 여행")
                .build();
        PersonDto.Request request = PersonDto.Request.builder()
                .User(userService.getUserInfo(userNo))
                .Travel(travelService.createTravel(travelInfo))
                .build();

        //when
        PersonDto.basic person = personService.createPerson(request, FALSE);

        //then
        System.out.println("User name = " + person.getUser().getName());
        System.out.println("Travel name = " + person.getTravel().getName());
    }

    @Test
    @Commit
    @DisplayName("여행 참가자 리스트 리턴")
    void getPersonNameInTravel(){
        //given
        int travelId = 57; //서울 여행

        //when
        List<PersonDto.Simple> list = personService.getPersonNameInTravel(travelId);

        //then
        for (PersonDto.Simple p : list){
            System.out.println("Name: " + p.getName());
        }

    }

    @Test
    @Commit
    @DisplayName("여행에서 참가자 삭제")
    void deleteJoinTravel() {
        //given
        int personId = 100;

        //when
        personService.deleteJoinTravel(personId);

        //then
        System.out.println("check DB please!");
    }

    @Test
    @Commit
    @DisplayName("사람 뽑아오기")
    void getPersonEntityByPersonId() {
        //given
        Long person_id = Long.valueOf(50); //이하은
        //when
        Person person = personService.getPersonEntityByPersonId(person_id);
        //then
        System.out.println("id: " + person.getId());
        System.out.println("id: " + person.getSumSend());
        System.out.println("id: " + person.getSumGet());
        System.out.println("id: " + person.getDifference());
        System.out.println("id: " + person.getRole());
    }

    @Test
    @Commit
    @DisplayName("이벤트 생성으로 person 정보 업데이트")
    void updatePersonWithEvent() {
        //given
        List<Person> personList = new ArrayList<>();
        Person person1 = personService.getPersonEntityByPersonId(Long.valueOf(92));
        personList.add(person1);
        Person person2 = personService.getPersonEntityByPersonId(Long.valueOf(93));
        personList.add(person2);
        Person person3 = personService.getPersonEntityByPersonId(Long.valueOf(94));
        personList.add(person3);
        Long payer_person_id = Long.valueOf(94);
        Double dividePrice = 70000.0;
        Double takePrice = 140000.0;

        //when
        personService.updatePersonMoneyByCreating(personList, payer_person_id, dividePrice, takePrice, true);
        personService.updatePersonRole(91);

        //then
        System.out.println("check DB");
    }

    @Test
    @Commit
    @DisplayName("이벤트 삭제로 person 정보 업데이트")
    void updatePersonWithEventDelete() {
        //given
        List<Person> personList = new ArrayList<>();
        Person person1 = personService.getPersonEntityByPersonId(Long.valueOf(92));
        personList.add(person1);
        Person person2 = personService.getPersonEntityByPersonId(Long.valueOf(93));
        personList.add(person2);
        Person person3 = personService.getPersonEntityByPersonId(Long.valueOf(94));
        personList.add(person3);
        Long payer_person_id = Long.valueOf(94);
        Double dividePrice = 70000.0;
        Double takePrice = 140000.0;

        //when
        personService.updatePersonMoneyByDeleting(personList, payer_person_id, dividePrice, takePrice);
        personService.updatePersonRole(91);

        //then
        System.out.println("check DB");
    }

    @Test
    @Commit
    @DisplayName("사람 뽑아오기")
    void getPersonToDetailView() {
        //given
        int person_id = 50; //이하은
        //when
        PersonDto.Detail detail = personService.getPersonToDetailView(person_id);
        //then
        System.out.println("personID: " + detail.getPersonId());
        System.out.println("personID: " + detail.getUserName());
        System.out.println("personID: " + detail.getUserEmail());
        System.out.println("personID: " + detail.getUserAccount());
        System.out.println("personID: " + detail.getSumSend());
        System.out.println("personID: " + detail.getSumGet());
        System.out.println("personID: " + detail.getDifference());
        System.out.println("personID: " + detail.getTravelRole());
    }
    @Test
    @Commit
    @DisplayName("사람 뽑아오기")
    void getPayerInTravel() {
        //given
        int travelId = 57; // 서울 여행
        //when
        PersonDto.HomeView tmp = personService.getPayerInTravel(travelId);
        //then
        System.out.println("payer : " + tmp.getId());
        System.out.println("payer : " + tmp.getName());
    }

    @Test
    @Commit
    @DisplayName("역할 세팅 테스트")
    void setRoleTest() {
        //given
        int travelId = 91; // 베를린 여행
        //when
        personService.updatePersonRole(travelId);
        //then
        System.out.println("CheckOUT DB");
    }




//    @Test
//    @Commit
//    @DisplayName("사람 뽑아오기")
//    void getPersonEntityByPersonId() {
//        //given
//        //when
//        //then
//    }

}