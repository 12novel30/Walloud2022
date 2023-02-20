package com.spring.mydiv.Service;

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

import java.util.List;
import java.util.stream.Collectors;

import static com.spring.mydiv.Code.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class TravelService {
    private final EventService eventService;
	private final TravelRepository travelRepository;
    private final EventRepository eventRepository;
    private final PersonRepository personRepository;

    @Transactional
    public TravelDto.Response createTravel(String travelName) {
        return TravelDto.Response.fromEntity(
                travelRepository.save(
                        Travel.builder()
                                .name(travelName)
                                .build()));
    }

    @Transactional
    public void deleteTravel(int travelId){
        // delete event
        List<Event> eventList =
                eventRepository.findByTravel_Id(Long.valueOf(travelId));
        for(Event event : eventList)
            eventService.deleteEvent(event.getId().intValue());

        // delete person
        List<Person> personList =
                personRepository.findByTravel_Id(Long.valueOf(travelId));
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
    @Transactional
    public String updateTravelImage(int userId, String imageURL){
        Travel travel = getTravelEntity(userId);
        /* TODO - deleteTravelImage(travel);
        *
        * public void deleteTravelImage(Travel travel){
        * s3UploaderService.deleteImage(travel.getImage());
        * }
        * */
        travel.setImage(imageURL);
        return travelRepository.save(travel).getImage();
    }

    public String getTravelImageURL(int travelId) {
        return getTravelEntity(travelId).getImage();
    }
    public TravelDto.Response getTravelResponse(int travelId){
        return TravelDto.Response.fromEntity(getTravelEntity(travelId));
    }
    public TravelDto.HomeView getTravelHomeView(int travelId){
        return TravelDto.HomeView.fromEntity(getTravelEntity(travelId));
    }
    @Transactional(readOnly = true) // TODO - 쓸 곳 있는지 논의할 것
    public List<TravelDto.Response> getSuperUserTravelList(Long userId){
        return personRepository.findByUser_IdAndIsSuper(userId, true)
                .stream()
                .map(TravelDto.Response::fromPersonEntity)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    private Travel getTravelEntity(int travelId) {
        return travelRepository.findById(Long.valueOf(travelId))
                .orElseThrow(() -> new DefaultException(NO_TRAVEL));
    }
}
