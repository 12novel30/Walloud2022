package com.spring.mydiv.Service;

import javax.transaction.Transactional;

import com.spring.mydiv.Dto.TravelDto;
import com.spring.mydiv.Dto.UserDto;
import com.spring.mydiv.Entity.Travel;
import com.spring.mydiv.Exception.DefaultException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.mydiv.Dto.PersonDto;
import com.spring.mydiv.Entity.Person;
import com.spring.mydiv.Entity.User;
import com.spring.mydiv.Repository.PersonRepository;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.spring.mydiv.Code.ErrorCode.*;
import static java.lang.Boolean.*;

/**
 * @author 12nov
 */
@Service
@RequiredArgsConstructor
public class PersonService {
	private final PersonRepository personRepository;
    @Transactional
    public PersonDto.basic createPerson(PersonDto.Request request, boolean superUser) {
        Person person = Person.builder()
        		.user(User.builder()
                        .id(request.getUser().getUserId())
                        .name(request.getUser().getName())
                        .email(request.getUser().getEmail())
                        .password(request.getUser().getPassword())
                        .account(request.getUser().getAccount())
                        .build())
        		.travel(Travel.builder()
                        .id(request.getTravel().getTravelId())
                        .name(request.getTravel().getName())
                        .build())
                .sumGet(0.0)
                .sumSend(0.0)
                .difference(0.0)
                .role(superUser)
                .isSuper(superUser)
                .isSettled(false)
                .build();
        personRepository.save(person);
        return PersonDto.basic.fromEntity(person);
//        if (ResponseEntity.ok(personservice.createPerson(personRequest, TRUE))
//                .getStatusCodeValue() == 200)
//            return personRequest.getTravel().getTravelId().intValue();
//        else throw new DefaultException(CREATE_FAIL);
    } //fin

    @Transactional
    public void deleteJoinTravel(int personId) {
        personRepository.deleteById(Long.valueOf(personId));
    }

    public List<PersonDto.Simple> getPersonNameInTravel(int travelId){
        List<Person> list = personRepository.findByTravel_Id(Long.valueOf(travelId));
        List<PersonDto.Simple> result = new ArrayList<PersonDto.Simple>();
        for (Person p : list){
            PersonDto.Simple person = PersonDto.Simple.fromEntity(p);
            result.add(person);
        }
        return result;
    }

    public List<PersonDto.HomeView> getPersonBasicInTravel(int travelId){
        List<Person> list = personRepository.findByTravel_Id(Long.valueOf(travelId));
        List<PersonDto.HomeView> result = new ArrayList<>();
        for (Person p : list){
            PersonDto.HomeView person = PersonDto.HomeView.fromEntity(p);
            result.add(person);
        }
        return result;
    }

    public boolean checkIsUserinTravel(Long userId, int travelId){
        return personRepository.existsByUser_IdAndTravel_Id(userId, Long.valueOf(travelId));
    }

    public Person getPersonEntityByPersonId(Long id){
        return personRepository.findById(id)
                .orElseThrow(()-> new DefaultException(NO_USER));
    }

    public List<PersonDto.HomeView> getPersonInfoInTravel(int travelId){
        List<Person> list = personRepository.findByTravel_Id(Long.valueOf(travelId));
        List<PersonDto.HomeView> result = new ArrayList<>();
        for (Person p : list){
            PersonDto.HomeView person = PersonDto.HomeView.fromEntity(p);
            result.add(person);
        }
        return result;
    } //fin

    public int getPersonCountInTravel(int travelId){
        return personRepository.countDistinctByTravel_Id(Long.valueOf(travelId));
    } //fin

    public PersonDto.Detail getPersonToDetailView(int personId){
        //- 사용자 개인 정보 -> user(name, email, account)
        //- travel에서의 정보 -> person(sumsend, sumget, diff, travelrole)
        Person info = personRepository.findById(Long.valueOf(personId))
                .orElseThrow(()-> new DefaultException(NO_USER));
        return PersonDto.Detail.fromEntity(info);
    }

    public PersonDto.HomeView getPayerInTravel(int travelId){
        return personRepository.findByTravel_IdAndRole(Long.valueOf(travelId), true)
                .map(PersonDto.HomeView::fromEntity)
                .orElseThrow(()-> new DefaultException(NO_PAYER));
    }

    public boolean isUserSuperuser(int travelId, int userId){
        return personRepository.findByUser_IdAndTravel_Id(
                Long.valueOf(userId), Long.valueOf(travelId))
                .get().getIsSuper();
    }

    public boolean isPersonSuperuser(int personId){
        return personRepository.findById(Long.valueOf(personId)).get().getIsSuper();
    }

    public void updatePersonMoneyByCreating(Person p, int eventPrice, Double chargedPrice, Boolean p_role){

        Person person = personRepository.findById(p.getId())
                .orElseThrow(() -> new DefaultException(NO_USER));

        if(p_role){
            Double takePrice = eventPrice - chargedPrice;
            p.setSumGet(p.getSumGet() + takePrice);
            p.setDifference(p.getDifference() + takePrice);

            person.setSumGet(p.getSumGet());
            person.setDifference(p.getDifference());
            personRepository.save(person);
            //personRepository.updateSumGetAndDifferenceById(p.getSumGet(), p.getDifference(), p.getId());
        }
        else{
            p.setSumSend(p.getSumSend() + chargedPrice);
            p.setDifference(p.getDifference() - chargedPrice);
            person.setSumSend(p.getSumSend());
            person.setDifference(p.getDifference());
            personRepository.save(person);
            //personRepository.updateSumSendAndDifferenceById(p.getSumSend(), p.getDifference(), p.getId());
        }
    }

    public void updatePersonMoneyByDeleting(Person p, int eventPrice, Double chargedPrice, Boolean p_role){

        Person person = personRepository.findById(p.getId())
                .orElseThrow(() -> new DefaultException(NO_USER));

        if(p_role){
            Double takePrice = eventPrice - chargedPrice;
            p.setSumGet(p.getSumGet() - takePrice);
            p.setDifference(p.getDifference() - takePrice);

            person.setSumGet(p.getSumGet());
            person.setDifference(p.getDifference());
            personRepository.save(person);
            //personRepository.updateSumGetAndDifferenceById(p.getSumGet(), p.getDifference(), p.getId());
        }
        else{
            p.setSumSend(p.getSumSend() - chargedPrice);
            p.setDifference(p.getDifference() + chargedPrice);

            person.setSumSend(p.getSumSend());
            person.setDifference(p.getDifference());
            personRepository.save(person);
            //personRepository.updateSumSendAndDifferenceById(p.getSumSend(), p.getDifference(), p.getId());
        }
    }

    public void updatePersonMoney(Person p, PersonDto.MoneyUpdateRequest request){

        Person person = personRepository.findById(p.getId())
                .orElseThrow(() -> new DefaultException(NO_USER));

        if(request.isPervEventRole()){
            Double prevTakePrice = request.getPrevPrice() - request.getPrevChargedPrice();
            p.setSumGet(p.getSumGet() - prevTakePrice);
            p.setDifference(p.getDifference() - prevTakePrice);

            person.setSumGet(p.getSumGet());
            person.setDifference(p.getDifference());
            personRepository.save(person);
            //personRepository.updateSumGetAndDifferenceById(p.getSumGet(), p.getDifference(), p.getId());
        }
        else{
            p.setSumSend(p.getSumSend() - request.getPrevChargedPrice());
            p.setDifference(p.getDifference() + request.getPrevChargedPrice());

            person.setSumSend(p.getSumSend());
            person.setDifference(p.getDifference());
            personRepository.save(person);
            //personRepository.updateSumSendAndDifferenceById(p.getSumSend(), p.getDifference(), p.getId());
        }

        if(request.isCurrEventRole()){
            Double currTakePrice = request.getCurrPrice() - request.getCurrChargedPrice();
            p.setSumGet(p.getSumGet() + currTakePrice);
            p.setDifference(p.getDifference() + currTakePrice);

            person.setSumGet(p.getSumGet());
            person.setDifference(p.getDifference());
            personRepository.save(person);
            //personRepository.updateSumGetAndDifferenceById(p.getSumGet(), p.getDifference(), p.getId());
        }
        else{
            p.setSumSend(p.getSumSend() + request.getCurrChargedPrice());
            p.setDifference(p.getDifference() - request.getCurrChargedPrice());
            person.setSumSend(p.getSumSend());
            person.setDifference(p.getDifference());
            personRepository.save(person);
            //personRepository.updateSumSendAndDifferenceById(p.getSumSend(), p.getDifference(), p.getId());
        }
    }


    public void updatePersonRole(int travelId){
        List<Person> People = personRepository.findByTravel_Id(Long.valueOf(travelId));

        Person currManager = personRepository.findByTravel_IdAndRole(Long.valueOf(travelId), true)
                .orElseThrow(()-> new DefaultException(NO_USER));
        currManager.setRole(FALSE);
        personRepository.save(currManager);
        //personRepository.updateRoleById(FALSE, currManager.getId());

        Double maxDifference = currManager.getDifference();

        for(Person p : People) {
            Double currDifference = p.getDifference();
            if (currDifference > maxDifference) {
                maxDifference = currDifference;
                currManager = p;
            }
        }

        Person newManager = personRepository.findById(currManager.getId())
                .orElseThrow(()-> new DefaultException(NO_USER));
        newManager.setRole(TRUE);
        personRepository.save(newManager);
        //personRepository.updateRoleById(TRUE, currManager.getId());
    }

    public PersonDto.Detail updateIsSettled(int personId, boolean isSettled){
        Person person = personRepository.findById(Long.valueOf(personId))
                .orElseThrow(() -> new DefaultException(NO_USER));

        person.setIsSettled(isSettled);

        return PersonDto.Detail.fromEntity(personRepository.save(person));
    }

    public PersonDto.Request setPersonRequest(
            UserDto.Response userDto, TravelDto.Response travelDto) {
        return new PersonDto.Request(userDto, travelDto);
    }
}
