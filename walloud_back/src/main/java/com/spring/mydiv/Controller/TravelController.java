package com.spring.mydiv.Controller;

import com.spring.mydiv.Code.ErrorCode;
import com.spring.mydiv.Dto.TravelDto;
import com.spring.mydiv.Exception.DefaultException;
import com.spring.mydiv.Service.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.TRUE;

/**
 * @author 12nov
 */
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
    public int createNewTravelUserJoining(
            @PathVariable int userId, @RequestBody String travel_name){
        return travelService.getTravelIdFromPersonDto(
                personService.createPerson(
                        personService.setPersonRequest(
                                userService.getUserInfo(userId),
                                travelService.createTravel(travel_name)),
                        TRUE)
        );
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
                travelId, s3UploaderService.upload(file, "test"));
        // TODO - 업로드될 사진 폴더 이름 enum 화
    }

    @GetMapping("/{travelId}/getTravelMainView")
    public TravelDto.HomeView getTravelMainView(@PathVariable int travelId){
        TravelDto.HomeView homeView = travelService.getTravelToMainView(travelId);

        homeView.setPersonList(personService.getPersonInfoInTravel(travelId));
        homeView.setPersonCount(personService.getPersonCountInTravel(travelId));
        homeView.setEventList(eventService.getEventInfoInTravel(travelId));
        homeView.setEventCount(eventService.getEventCountInTravel(travelId));
        homeView.setPeriod(eventService.getTravelPeriod(travelId, homeView.getEventCount()));
        homeView.setSuperUser(eventService.getSuperUser(travelId));

        return homeView;
    }

    @GetMapping("/{travelId}/getTravelImage")
    public String getTravelImage(@PathVariable int travelId){
        return travelService.getTravelImageURL(travelId);
    }

    @PostMapping("/{userId}/{travelId}/deleteTravel") // TODO - delete Travel
    public void deleteTravel(@PathVariable(value = "userId") int userId,
                             @PathVariable(value = "travelId") int travelId) {
        if (personService.isUserSuperuser(travelId, userId))
            travelService.deleteTravel(travelId);
        else throw new DefaultException(ErrorCode.INVALID_DELETE_NOTSUPERUSER);
    }

}
