package com.spring.mydiv.Service;

import com.spring.mydiv.Dto.PersonDto;
import com.spring.mydiv.Dto.TravelDto;
import com.spring.mydiv.Entity.Event;
import com.spring.mydiv.Entity.Person;
import com.spring.mydiv.Exception.DefaultException;
import com.spring.mydiv.Repository.EventRepository;
import com.spring.mydiv.Repository.PersonRepository;
import org.springframework.stereotype.Service;

import com.spring.mydiv.Entity.Travel;
import com.spring.mydiv.Repository.TravelRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.spring.mydiv.Code.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class TravelService {
    private final EventService eventService;
	private final TravelRepository travelRepository;
    private final EventRepository eventRepository;
    private final PersonRepository personRepository;
    private final S3UploaderService s3UploaderService;

    @Transactional
    public TravelDto.Response createTravel(String travelName) {
        Travel travel = Travel.builder()
                .name(travelName)
                .build();
        travelRepository.save(travel);
        return TravelDto.Response.fromEntity(travel);
    }

    public TravelDto.Response getTravelInfo(int no){
        return TravelDto.Response.fromEntity(getTravelEntity(no));
    }
    public TravelDto.HomeView getTravelHomeView(int travelId){
        return TravelDto.HomeView.fromEntity(getTravelEntity(travelId));
    }
    public String getTravelImageURL(int travelId){
        return getTravelEntity(travelId).getImage();
    }


    @Transactional
    public void deleteTravel(int travelId){
        // delete event
        List<Event> eventList = eventRepository.findByTravel_Id(Long.valueOf(travelId));
        for(Event event : eventList) eventService.deleteEvent(event.getId().intValue());
        // delete person
        List<Person> personList = personRepository.findByTravel_Id(Long.valueOf(travelId));
        for(Person person : personList) personRepository.delete(person);
        // delete travel
        travelRepository.deleteById(Long.valueOf(travelId));
    }

    @Transactional
    public TravelDto.Response updateTravelInfo(int travelId, String travelName){
        Travel travel = getTravelEntity(travelId);
        if (travelName != null) travel.setName(travelName);
        return TravelDto.Response.fromEntity(travelRepository.save(travel));
    }
    @Transactional(readOnly = true)
    public List<TravelDto.Response> getSuperUserTravelList(Long userId){ // TODO - 아직 확인 안했음
        List<Person> personList = personRepository.findByUser_IdAndIsSuper(userId, true);
        List<TravelDto.Response> result = new ArrayList<>();
        for (Person p : personList)
            result.add(TravelDto.Response.builder()
                            .TravelId(p.getTravel().getId())
                            .Name(p.getTravel().getName())
                            .IsSuper(p.getIsSuper())
                            .build()
            );
        return result;
    }

    @Transactional
    public String updateTravelImage(int userId, String imageURL){
        Travel travel = getTravelEntity(userId);
        // TODO - deleteTravelImage(travel);
        travel.setImage(imageURL);
        return travelRepository.save(travel).getImage();
    }
    public void deleteTravelImage(Travel travel){ // TODO - deleteTravelImage(travel);
        s3UploaderService.deleteImage(travel.getImage());
    }

    @Transactional(readOnly = true)
    private Travel getTravelEntity(int userId) {
        return travelRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new DefaultException(NO_TRAVEL));
    }

    public int getTravelIdFromPersonDto(PersonDto.basic personDto) {
        return personDto.getTravel().getId().intValue();
    }
}
