package com.spring.mydiv.Service;

import javax.transaction.Transactional;

import com.spring.mydiv.Dto.TravelDto;
import com.spring.mydiv.Entity.Travel;
import com.spring.mydiv.Exception.DefaultException;
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
                .build();
        personRepository.save(person);
        return PersonDto.basic.fromEntity(person);
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
        return personRepository.findByUser_IdAndTravel_Id(Long.valueOf(userId), Long.valueOf(travelId))
                .get().getIsSuper();
    }

    public boolean isPersonSuperuser(int personId){
        return personRepository.findById(Long.valueOf(personId)).get().getIsSuper();
    }

    public void updatePersonMoneyByCreating(Person p, int eventPrice, Double chargedPrice, Boolean p_role){
        if(p_role){
            Double takePrice = eventPrice - chargedPrice;
            p.setSumGet(p.getSumGet() + takePrice);
            p.setDifference(p.getDifference() + takePrice);
            personRepository.updateSumGetAndDifferenceById(p.getSumGet(), p.getDifference(), p.getId());
        }
        else{
            p.setSumSend(p.getSumSend() + chargedPrice);
            p.setDifference(p.getDifference() - chargedPrice);
            personRepository.updateSumSendAndDifferenceById(p.getSumSend(), p.getDifference(), p.getId());
        }
    }

    public void updatePersonMoneyByDeleting(Person p, int eventPrice, Double chargedPrice, Boolean p_role){
        if(p_role){
            Double takePrice = eventPrice - chargedPrice;
            p.setSumGet(p.getSumGet() - takePrice);
            p.setDifference(p.getDifference() - takePrice);
            personRepository.updateSumGetAndDifferenceById(p.getSumGet(), p.getDifference(), p.getId());
        }
        else{
            p.setSumSend(p.getSumSend() - chargedPrice);
            p.setDifference(p.getDifference() + chargedPrice);
            personRepository.updateSumSendAndDifferenceById(p.getSumSend(), p.getDifference(), p.getId());
        }
    }

    public void updatePersonMoney(Person p, PersonDto.MoneyUpdateRequest request){
        if(request.isPervEventRole()){
            Double prevTakePrice = request.getPrevPrice() - request.getPrevChargedPrice();
            p.setSumGet(p.getSumGet() - prevTakePrice);
            p.setDifference(p.getDifference() - prevTakePrice);
            personRepository.updateSumGetAndDifferenceById(p.getSumGet(), p.getDifference(), p.getId());
        }
        else{
            p.setSumSend(p.getSumSend() - request.getPrevChargedPrice());
            p.setDifference(p.getDifference() + request.getPrevChargedPrice());
            personRepository.updateSumSendAndDifferenceById(p.getSumSend(), p.getDifference(), p.getId());
        }

        if(request.isCurrEventRole()){
            Double currTakePrice = request.getCurrPrice() - request.getCurrChargedPrice();
            p.setSumGet(p.getSumGet() + currTakePrice);
            p.setDifference(p.getDifference() + currTakePrice);
            personRepository.updateSumGetAndDifferenceById(p.getSumGet(), p.getDifference(), p.getId());
        }
        else{
            p.setSumSend(p.getSumSend() + request.getCurrChargedPrice());
            p.setDifference(p.getDifference() - request.getCurrChargedPrice());
            personRepository.updateSumSendAndDifferenceById(p.getSumSend(), p.getDifference(), p.getId());
        }
    }


    public void updatePersonRole(int travelId){
        List<Person> People = personRepository.findByTravel_Id(Long.valueOf(travelId));
        Person currManager = personRepository.findByTravel_IdAndRole(Long.valueOf(travelId), true)
                .orElseThrow(()-> new DefaultException(NO_USER));
        personRepository.updateRoleById(FALSE, currManager.getId());
        Double maxDifference = currManager.getDifference();

        for(Person p : People) {
            Double currDifference = p.getDifference();
            if (currDifference > maxDifference) {
                maxDifference = currDifference;
                currManager = p;
            }
        }
        personRepository.updateRoleById(TRUE, currManager.getId());
    }

}
