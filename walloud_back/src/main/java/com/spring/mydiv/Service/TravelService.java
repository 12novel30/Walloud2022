package com.spring.mydiv.Service;

import javax.transaction.Transactional;

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

import java.util.ArrayList;
import java.util.List;

import static com.spring.mydiv.Code.ErrorCode.*;

/**
 * @author 12nov
 */
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
        return travelRepository.findById(Long.valueOf(no))
                .map(TravelDto.Response::fromEntity)
                .orElseThrow(()-> new DefaultException(NO_TRAVEL));
    }

    public TravelDto.HomeView getTravelToMainView(int travelId){
        return travelRepository.findById(Long.valueOf(travelId))
                .map(TravelDto.HomeView::fromEntity)
                .orElseThrow(()-> new DefaultException(NO_TRAVEL));
    }

    @Transactional
    public void deleteTravel(int travelId){
        List<Event> eventList = eventRepository.findByTravel_Id(Long.valueOf(travelId));
        List<Person> personList = personRepository.findByTravel_Id(Long.valueOf(travelId));
        for(Event event : eventList){
            eventService.deleteEvent(event.getId().intValue());
        }
        for(Person person : personList){
            personRepository.delete(person);
        }
        travelRepository.deleteById(Long.valueOf(travelId));
    }

    @Transactional
    public TravelDto.Response updateTravelInfo(int travelId, String travelName){
        Travel travel = travelRepository.findById(Long.valueOf(travelId))
                .orElseThrow(() -> new DefaultException(NO_TRAVEL));
        if (travelName != null) travel.setName(travelName);
        return TravelDto.Response.fromEntity(travelRepository.save(travel));
    }

    public List<TravelDto.Response> getSuperUserTravelList(Long userId){
        List<Person> personList = personRepository.findByUser_IdAndIsSuper(userId, true);
        List<TravelDto.Response> result = new ArrayList<>();
        for (Person p : personList){
            TravelDto.Response tmp = new TravelDto.Response(
                    p.getTravel().getId(),
                    p.getTravel().getName(),
                    p.getIsSuper()
            );
            result.add(tmp);
        }
        return result;
    }
    
    public String getTravelImageURL(int travelId){
        return travelRepository.findById(Long.valueOf(travelId))
                .map(TravelDto.ResponseWithImage::fromEntity)
                .orElseThrow(()-> new DefaultException(NO_TRAVEL))
                .getImageurl();
    }

    @Transactional
    public String updateTravelImage(int userId, String imageURL){
        Travel travel = travelRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new DefaultException(NO_TRAVEL));
        // TODO - deleteTravelImage(travel);
        travel.setImage(imageURL);
        return travelRepository.save(travel).getImage();
    }

    public void deleteTravelImage(Travel travel){
        String travelExistingImage = travel.getImage();
        s3UploaderService.deleteImage(travelExistingImage);
    }

    public int getTravelIdFromPersonDto(PersonDto.basic personDto) {
        return personDto.getTravel().getId().intValue();
    }
}
