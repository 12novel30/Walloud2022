package com.spring.mydiv.Controller;

import com.spring.mydiv.Dto.TravelDto;
import com.spring.mydiv.Dto.UserDto;
import com.spring.mydiv.Service.EventService;
import com.spring.mydiv.Service.PersonService;
import com.spring.mydiv.Service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author 12nov
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TravelController {
    private final TravelService travelservice;
    private final PersonService personService;
    private final EventService eventService;

    @GetMapping("/{userId}/{travelId}") //return empty
    public TravelDto.HomeView getTravelToMainView(@PathVariable int travelId){
        System.out.println("_________________________1");
        TravelDto.HomeView homeView = travelservice.getTravelToMainView(travelId);
        System.out.println("_________________________2");
        homeView.setPersonList(personService.getPersonInfoInTravel(travelId));
        System.out.println("_________________________3");
        homeView.setPersonCount(personService.getPersonCountInTravel(travelId));
        System.out.println("_________________________4");
        homeView.setEventList(eventService.getEventInfoInTravel(travelId));
        System.out.println("_________________________5");
        homeView.setEventCount(eventService.getEventCountInTravel(travelId));
        System.out.println("_________________________6");
        homeView.setPeriod(eventService.getTravelPeriod(travelId, homeView.getEventCount()));
        System.out.println("_________________________7");
        homeView.setSuperUser(eventService.getSuperUser(travelId));
        return homeView;
    }

    @PutMapping("/{userId}/{travelId}/updateTravelInfo")
    public ResponseEntity<TravelDto.Response> updateTravel(@PathVariable int travelId, @RequestBody Map map) {
        TravelDto.Request updateRequest = new TravelDto.Request(map.get("travel_name").toString());
        return ResponseEntity.ok(travelservice.updateTravelInfo(travelId, updateRequest)
        );
    }
}
