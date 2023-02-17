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

import java.util.List;
import java.util.stream.Collectors;

import static com.spring.mydiv.Code.ErrorCode.*;
import static com.spring.mydiv.Code.WalloudCode.*;
import static java.lang.Boolean.*;

@Service
@RequiredArgsConstructor
public class PersonService {
	private final PersonRepository personRepository;

    @Transactional
    public PersonDto.basic createPerson(PersonDto.Request request, boolean superUser) {
        Person person = Person.builder()
        		.user(User.builder()
                        .id(request.getUserDto().getUserId())
                        .name(request.getUserDto().getName())
                        .email(request.getUserDto().getEmail())
                        .password(request.getUserDto().getPassword())
                        .account(request.getUserDto().getAccount())
                        .build())
        		.travel(Travel.builder()
                        .id(request.getTravelDto().getTravelId())
                        .name(request.getTravelDto().getName())
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
    public void deletePerson(int personId) {
        personRepository.deleteById(Long.valueOf(personId));
    }


    @Transactional(readOnly = true)
    public void validateIsUserNotInTravel(Long userId, Long travelId){
        if (personRepository.existsByUser_IdAndTravel_Id(userId, Long.valueOf(travelId)))
            throw new DefaultException(ALREADY_EXISTED);
    }
    @Transactional(readOnly = true)
    public void validateIsUserSuperuser(int travelId, int userId){
        if (!personRepository.findByUser_IdAndTravel_Id(
                Long.valueOf(userId), Long.valueOf(travelId)).get().getIsSuper())
            throw new DefaultException(ErrorCode.INVALID_DELETE_NOTSUPERUSER);
    }
    public void validateIsPersonNotSuperUser(int personId){
        if (getPersonEntityByPersonId(Long.valueOf(personId)).getIsSuper())
            throw new DefaultException(INVALID_DELETE_SUPERUSER);
    }
    public WalloudCode validateIsManager(PersonDto.Detail detailView) {
        if (detailView.getEventList().size() != 0)
            if (detailView.getTravelRole()) return MANAGER;
            else return OTHERS;
        else return NO_EVENTS;
    }

    public void updatePersonMoney(PersonDto.tmp tmp){
        Person person = getPersonEntityByPersonId(tmp.getPersonId());
        Double chargedPrice = tmp.getChargedPrice();
        Double takePrice = tmp.getEventPrice() - chargedPrice;
        if (tmp.isCreate()){
            if (tmp.isEventRole()){ // MANAGER
                person.setSumGet(person.getSumGet() + takePrice);
                person.setDifference(person.getDifference() + takePrice);
            }
            else { // OTHERS
                person.setSumSend(person.getSumSend() + chargedPrice);
                person.setDifference(person.getDifference() - chargedPrice);
            }
        } else {
            if (tmp.isEventRole()){ // MANAGER
                person.setSumGet(person.getSumGet() - takePrice);
                person.setDifference(person.getDifference() - takePrice);
            }
            else { // OTHERS
                person.setSumSend(person.getSumSend() - chargedPrice);
                person.setDifference(person.getDifference() + chargedPrice);
            }
        }
        personRepository.save(person);

    }
    public void updatePersonRole(int travelId){
        // update current MANAGER to OTHERS
        Person currManager = getManagerEntityByTravelId(travelId);
        updatePersonRoleToWhat(currManager, OTHERS);

        // set new MANAGER
        List<Person> People = getPersonListByTravelId(travelId);
        Double maxDifference = currManager.getDifference();
        Long newManagerId = Long.valueOf(0);
        for(Person p : People) {
            Double currDifference = p.getDifference();
            if (currDifference > maxDifference) {
                maxDifference = currDifference;
                newManagerId = p.getId();
            }
        }

        // update new MANAGER
        updatePersonRoleToWhat(getPersonEntityByPersonId(newManagerId), MANAGER);
    }
    @Transactional
    private void updatePersonRoleToWhat(Person updatePerson, WalloudCode newRole){
        if (newRole == MANAGER) updatePerson.setRole(TRUE);
        else if (newRole == OTHERS) updatePerson.setRole(FALSE);
        personRepository.save(updatePerson);
    }
    @Transactional
    public int updateIsSettled(int personId, boolean isSettled){
        Person person = getPersonEntityByPersonId(Long.valueOf(personId));
        person.setIsSettled(isSettled);
        return ResponseEntity.ok(personRepository.save(person)).getStatusCodeValue();
    }


    public PersonDto.Request setPersonRequestDto(UserDto.Response userDto,
                                                 TravelDto.Response travelDto) {
        return new PersonDto.Request(userDto, travelDto);
    }

    public PersonDto.Detail getPersonDetail(int personId){
        return PersonDto.Detail.fromEntity(getPersonEntityByPersonId(Long.valueOf(personId)));
    }
    public PersonDto.OrderMessage getManagerOrderMessage(int travelId){
        return PersonDto.OrderMessage.fromEntity(getManagerEntityByTravelId(travelId));
    }
    public List<PersonDto.OrderMessage> getOthersOrderMessageList(int travelId){
        return getPersonListByTravelId(travelId)
                .stream()
                .map(PersonDto.OrderMessage::fromEntity)
                .collect(Collectors.toList());
    }
    public List<PersonDto.HomeView> getPersonHomeViewList(int travelId){
        return getPersonListByTravelId(travelId)
                .stream()
                .map(PersonDto.HomeView::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    private Person getPersonEntityByPersonId(Long personId){
        return personRepository.findById(personId)
                .orElseThrow(()-> new DefaultException(NO_USER)); // TODO - no person 으로 바꿔야 하는지?
    }
    @Transactional(readOnly = true)
    private Person getManagerEntityByTravelId(int travelId) {
        return personRepository.findByTravel_IdAndRole(Long.valueOf(travelId), true)
                .orElseThrow(() -> new DefaultException(NO_MANAGER)); // manager role is true
    }
    @Transactional(readOnly = true)
    private List<Person> getPersonListByTravelId(int travelId) {
        return personRepository.findByTravel_Id(Long.valueOf(travelId));
    }
}
