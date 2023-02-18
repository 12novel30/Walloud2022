package com.spring.mydiv.Controller;

import com.spring.mydiv.Dto.TravelDto;
import com.spring.mydiv.Service.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.spring.mydiv.Code.S3FolderName.TRAVEL_FOLDER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TravelController {
    private final UserService userService;
    private final TravelService travelService;
    private final PersonService personService;
    private final EventService eventService;
    private final S3UploaderService s3UploaderService;

    @PostMapping("/{userId}/createNewTravelUserJoining")
    public int createNewTravelUserJoining(@PathVariable int userId,
                                          @RequestBody String travel_name){
        /* return created travel id
        * - set person Dto
        * - get User Information
        * - create Travel
        * - this person is superUser = TRUE */
        return personService.createPerson(
                personService.setPersonRequestDto(
                        userService.getUserResponseById(userId),
                        travelService.createTravel(travel_name)),
                        true)
                .getTravel().getId().intValue();
    }

    @PutMapping("/{travelId}/updateTravelName")
    public ResponseEntity<TravelDto.Response> updateTravelName(
            @PathVariable int travelId, @RequestBody String travel_name) {
        return ResponseEntity.ok(travelService.updateTravelInfo(travelId, travel_name));
    }

    @PutMapping("/{travelId}/updateTravelImage")
    public String updateTravelImage(@PathVariable int travelId,
                                    @RequestPart(value="file") MultipartFile file)
            throws IOException {
        return travelService.updateTravelImage(
                travelId,
                s3UploaderService.upload(file, TRAVEL_FOLDER.getDescription()));
    }

    @GetMapping("/{travelId}/getTravelHomeView")
    public TravelDto.HomeView getTravelHomeView(@PathVariable int travelId){
        // TODO - path variable 을 long 으로 변경할 수 있는지 확인할 것
        TravelDto.HomeView homeView = travelService.getTravelHomeView(travelId);

        homeView.setPersonList(personService.getPersonHomeViewList(travelId));
        homeView.setPersonCount(homeView.getPersonList().size());
        homeView.setEventList(eventService.getEventInfoInTravel(travelId));
        homeView.setEventCount(homeView.getEventList().size());
        homeView.setPeriod(eventService.getTravelPeriod(travelId, homeView.getEventCount()));
        homeView.setSuperUserPersonId(eventService.getSuperUser(travelId));

        return homeView;
    }

    @GetMapping("/{travelId}/getTravelImage")
    public String getTravelImage(@PathVariable int travelId){
        return travelService.getTravelImageURL(travelId);
    }

    @DeleteMapping("/{userId}/{travelId}/deleteTravel")
    public void deleteTravel(@PathVariable(value = "userId") int userId,
                             @PathVariable(value = "travelId") int travelId) {
        // TODO - userId -> personId 논의
        // if this person is not superUser for this travel, then throw Exception
        personService.validateIsUserSuperuser(travelId, userId);
        // delete travel
        travelService.deleteTravel(travelId);
    }

}
