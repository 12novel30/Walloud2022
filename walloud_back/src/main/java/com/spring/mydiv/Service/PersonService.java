package com.spring.mydiv.Service;

import com.spring.mydiv.Code.ErrorCode;
import com.spring.mydiv.Code.WalloudCode;
import com.spring.mydiv.Dto.*;
import com.spring.mydiv.Entity.Travel;
import com.spring.mydiv.Exception.DefaultException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.mydiv.Entity.Person;
import com.spring.mydiv.Entity.User;
import com.spring.mydiv.Repository.PersonRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.spring.mydiv.Code.ErrorCode.*;
import static com.spring.mydiv.Code.WalloudCode.*;
import static java.lang.Boolean.*;

@Service
@RequiredArgsConstructor
public class PersonService {
	private final PersonRepository personRepository;
    @Transactional
    public PersonDto.basic createPerson(PersonDto.Request request, boolean superUser) {
        Person person = Person.builder() // TODO - checking
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
                .sumGet(Double.valueOf(0))
                .sumSend(Double.valueOf(0))
                .difference(Double.valueOf(0))
                .role(superUser) // 생성할 때만 1
                .isSuper(superUser)
                .isSettled(false)
                .build();

        if (ResponseEntity.ok(personRepository.save(person)).getStatusCodeValue() == 200)
            return PersonDto.basic.fromEntity(person);
        else throw new DefaultException(CREATE_FAIL);
    }

    @Transactional
    public void deletePerson2Travel(int personId) {// TODO - checking
        personRepository.deleteById(Long.valueOf(personId));
    }
    @Transactional(readOnly = true)
    public void validateUserInTravel(Long userId, Long travelId){ // TODO - checking
        if (personRepository.existsByUser_IdAndTravel_Id(userId, Long.valueOf(travelId)))
            throw new DefaultException(ALREADY_EXISTED);
    }

    public Person getPersonEntityByPersonId(Long id){
        return personRepository.findById(id)
                .orElseThrow(()-> new DefaultException(NO_USER)); // TODO - no person 으로 바꿔야 하는지?
    }

    public List<PersonDto.HomeView> getPersonListToHomeView(int travelId){// TODO - checking
        List<Person> list = personRepository.findByTravel_Id(Long.valueOf(travelId));
        List<PersonDto.HomeView> result = new ArrayList<>();
        for (Person p : list)
            result.add(PersonDto.HomeView.fromEntity(p));
        return result;
    }
    public List<PersonDto.OrderMessage> getPersonListForOrderMessage(int travelId){// TODO - checking
        List<Person> list = personRepository.findByTravel_Id(Long.valueOf(travelId));
        List<PersonDto.OrderMessage> result = new ArrayList<>();
        for (Person p : list)
            result.add(PersonDto.OrderMessage.fromEntity(p));
        return result;
    }

    public int getPersonCountInTravel(int travelId){
        return personRepository.countDistinctByTravel_Id(Long.valueOf(travelId));
    } //fin

    public PersonDto.Detail getPersonToDetailView(int personId){// TODO - checking
        return PersonDto.Detail.fromEntity(getPersonEntity(personId));
    }

    private Person getPersonEntity(int personId) {
        return personRepository.findById(Long.valueOf(personId))
                .orElseThrow(() -> new DefaultException(NO_USER));
    }

    public PersonDto.OrderMessage getManagerInTravel(int travelId){// TODO - checking
        return personRepository.findByTravel_IdAndRole(Long.valueOf(travelId), true)
                .map(PersonDto.OrderMessage::fromEntity)
                .orElseThrow(()-> new DefaultException(NO_MANAGER));
    }

    public void validateUserSuperuser(int travelId, int userId){
        if (!personRepository.findByUser_IdAndTravel_Id(
                Long.valueOf(userId), Long.valueOf(travelId)).get().getIsSuper())
            throw new DefaultException(ErrorCode.INVALID_DELETE_NOTSUPERUSER);
    }

    public void validatePersonNotSuperuser(int personId){// TODO - checking
        if (personRepository.findById(Long.valueOf(personId)).get().getIsSuper())
            throw new DefaultException(INVALID_DELETE_SUPERUSER);
    }

    public void updatePersonMoneyByCreating(
            ParticipantDto.CreateEvent partiDto,
            EventDto.Request eventRequest){

        Person person = personRepository.findById(partiDto.getPersonId())
                .orElseThrow(() -> new DefaultException(NO_USER));
        Double chargedPrice = partiDto.getSpent();
        if(partiDto.getRole()){ // 결제자라면
            Double takePrice = eventRequest.getPrice() - chargedPrice;
            person.setSumGet(person.getSumGet() + takePrice);
            person.setDifference(person.getDifference() + takePrice);
            personRepository.save(person);
        }
        else { // 일반 참가자라면
            person.setSumSend(person.getSumSend() + chargedPrice);
            person.setDifference(person.getDifference() - chargedPrice);
            personRepository.save(person);
        }
    }

    public void updatePersonMoneyByDeleting(
            ParticipantDto.detailView partiDto, int eventPrice){

        Person person = personRepository.findById(partiDto.getPersonId())
                .orElseThrow(() -> new DefaultException(NO_USER));
        Double chargedPrice = partiDto.getChargedPrice();
        if (partiDto.isEventRole()){ // 결제자라면
            Double takePrice = eventPrice - chargedPrice;
            person.setSumGet(person.getSumGet() - takePrice);
            person.setDifference(person.getDifference() - takePrice);
            personRepository.save(person);
        }
        else { // 일반 참가자라면
            person.setSumSend(person.getSumSend() - chargedPrice);
            person.setDifference(person.getDifference() + chargedPrice);
            personRepository.save(person);
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

    public PersonDto.Simple updateIsSettled(int personId, boolean isSettled){// TODO - checking
        Person person = getPersonEntity(personId);
        person.setIsSettled(isSettled);
        return PersonDto.Simple.fromEntity(personRepository.save(person));
    }

    public PersonDto.Request setPersonRequest( // TODO - checking
            UserDto.Response userDto, TravelDto.Response travelDto) {
        return new PersonDto.Request(userDto, travelDto);
    }

    public WalloudCode getOrderCodeFromDetailView(PersonDto.Detail detailView) {// TODO - checking
        if (detailView.getEventList().size() != 0)
            if (detailView.getTravelRole()) return MANAGER;
            else return OTHERS;
        else return NO_EVENTS;
    }
}
