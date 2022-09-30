package com.spring.mydiv.Controller;

import com.spring.mydiv.Dto.TravelDto;
import com.spring.mydiv.Service.EventService;
import com.spring.mydiv.Service.PersonService;
import com.spring.mydiv.Service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
        TravelDto.HomeView homeView = travelservice.getTravelToMainView(travelId);
        homeView.setPersonList(personService.getPersonInfoInTravel(travelId));
        homeView.setPersonCount(personService.getPersonCountInTravel(travelId));
        homeView.setEventList(eventService.getEventInfoInTravel(travelId));
        homeView.setEventCount(eventService.getEventCountInTravel(travelId));
        homeView.setPeriod(eventService.getTravelPeriod(travelId, homeView.getEventCount()));
        return homeView;
    }
}
