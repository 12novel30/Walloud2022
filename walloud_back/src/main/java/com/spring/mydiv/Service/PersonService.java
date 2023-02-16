package com.spring.mydiv.Service;

import javax.transaction.Transactional;

import com.spring.mydiv.Code.ErrorCode;
import com.spring.mydiv.Code.WalloudCode;
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

import static com.spring.mydiv.Code.ErrorCode.*;
import static com.spring.mydiv.Code.WalloudCode.*;
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


        if (ResponseEntity.ok(personRepository.save(person)).getStatusCodeValue() == 200)
            return PersonDto.basic.fromEntity(person);
        else throw new DefaultException(CREATE_FAIL);
    }

    @Transactional
    public void deletePerson2Travel(int personId) {
        personRepository.deleteById(Long.valueOf(personId));
    }

    public void validateUserInTravel(Long userId, Long travelId){
        if (personRepository.existsByUser_IdAndTravel_Id(userId, Long.valueOf(travelId)))
            throw new DefaultException(ALREADY_EXISTED);
    }

    public Person getPersonEntityByPersonId(Long id){
        return personRepository.findById(id)
                .orElseThrow(()-> new DefaultException(NO_USER));
    }

    public List<PersonDto.HomeView> getPersonListToHomeView(int travelId){
        List<Person> list = personRepository.findByTravel_Id(Long.valueOf(travelId));
        List<PersonDto.HomeView> result = new ArrayList<>();
        for (Person p : list)
            result.add(PersonDto.HomeView.fromEntity(p));
        return result;
    }
    public List<PersonDto.OrderMessage> getPersonListForOrderMessage(int travelId){
        List<Person> list = personRepository.findByTravel_Id(Long.valueOf(travelId));
        List<PersonDto.OrderMessage> result = new ArrayList<>();
        for (Person p : list)
            result.add(PersonDto.OrderMessage.fromEntity(p));
        return result;
    }

    public int getPersonCountInTravel(int travelId){
        return personRepository.countDistinctByTravel_Id(Long.valueOf(travelId));
    } //fin

    public PersonDto.Detail getPersonToDetailView(int personId){
        return PersonDto.Detail.fromEntity(getPersonEntity(personId));
    }

    private Person getPersonEntity(int personId) {
        return personRepository.findById(Long.valueOf(personId))
                .orElseThrow(() -> new DefaultException(NO_USER));
    }

    public PersonDto.OrderMessage getManagerInTravel(int travelId){
        return personRepository.findByTravel_IdAndRole(Long.valueOf(travelId), true)
                .map(PersonDto.OrderMessage::fromEntity)
                .orElseThrow(()-> new DefaultException(NO_MANAGER));
    }

    public void validateUserSuperuser(int travelId, int userId){
        if (!personRepository.findByUser_IdAndTravel_Id(
                Long.valueOf(userId), Long.valueOf(travelId)).get().getIsSuper())
            throw new DefaultException(ErrorCode.INVALID_DELETE_NOTSUPERUSER);
    }

    public void validatePersonNotSuperuser(int personId){
        if (personRepository.findById(Long.valueOf(personId)).get().getIsSuper())
            throw new DefaultException(INVALID_DELETE_SUPERUSER);
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

    public PersonDto.Simple updateIsSettled(int personId, boolean isSettled){
        Person person = getPersonEntity(personId);
        person.setIsSettled(isSettled);
        return PersonDto.Simple.fromEntity(personRepository.save(person));
    }

    public PersonDto.Request setPersonRequest(
            UserDto.Response userDto, TravelDto.Response travelDto) {
        return new PersonDto.Request(userDto, travelDto);
    }

    public int getPersonIdFromPersonDto(PersonDto.basic personDto) {
        return personDto.getPersonId().intValue();
    }

    public WalloudCode getOrderCodeFromDetailView(PersonDto.Detail detailView) {
        if (detailView.getEventList().size() != 0)
            if (detailView.getTravelRole()) return MANAGER;
            else return OTHERS;
        else return NO_EVENTS;
    }
}
