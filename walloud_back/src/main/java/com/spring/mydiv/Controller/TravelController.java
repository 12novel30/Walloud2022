package com.spring.mydiv.Controller;

import com.spring.mydiv.Dto.S3Dto;
import com.spring.mydiv.Dto.TravelDto;
import com.spring.mydiv.Service.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.spring.mydiv.Code.S3Code.TRAVEL_FOLDER;

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
    public Long createNewTravelUserJoining(@PathVariable Long userId,
                                          @RequestBody String travel_name) {
        // create travel -> person entity & return travel id
        return personService.createPerson(
                personService.setPersonRequestDto(
                        userService.getUserResponseById(userId),
                        travelService.createTravel(travel_name)),
                        true)
                .getTravelId();
    }

    @PutMapping("/{travelId}/updateTravelName")
    public ResponseEntity<TravelDto.Response> updateTravelName(
            @PathVariable Long travelId, @RequestBody String travel_name) {
        return ResponseEntity.ok(
                travelService.updateTravelInfo(travelId, travel_name));
    }

    @PutMapping("/{travelId}/updateTravelImage")
    public String updateTravelImage(@PathVariable Long travelId,
                                    @RequestPart(value="file") MultipartFile file)
            throws IOException {
        S3Dto.ImageUrls urls = travelService.updateTravelImage(
                travelId, s3UploaderService.upload(file, TRAVEL_FOLDER.getDescription()));
        s3UploaderService.deleteImage(urls.getDeleteImage());
        return urls.getNewImage();
    }

    @GetMapping("/{travelId}/getTravelHomeView")
    public TravelDto.HomeView getTravelHomeView(@PathVariable Long travelId) {
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
    public String getTravelImage(@PathVariable Long travelId) {
        return travelService.getTravelImageURL(travelId);
    }

    @DeleteMapping("/{userId}/{travelId}/deleteTravel")
    public void deleteTravel(@PathVariable(value = "userId") Long userId,
                             @PathVariable(value = "travelId") Long travelId) {
        // TODO - userId -> personId 논의
        // if this person is not superUser for this travel, then throw Exception
        personService.validateIsUserSuperuser(travelId, userId);
        // delete travel
        travelService.deleteTravel(travelId);
    }

}
