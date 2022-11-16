package com.spring.mydiv.Controller;

import com.spring.mydiv.Code.ErrorCode;
import com.spring.mydiv.Dto.TravelDto;
import com.spring.mydiv.Dto.UserDto;
import com.spring.mydiv.Exception.DefaultException;
import com.spring.mydiv.Service.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 12nov
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TravelController {
    private final UserService userservice;
    private final TravelService travelservice;
    private final PersonService personService;
    private final EventService eventService;
    private final S3UploaderService s3UploaderService;

    @GetMapping("/{userId}/{travelId}") //return empty
    public TravelDto.HomeView getTravelToMainView(@PathVariable int travelId){
        System.out.println("_________________________1");
        TravelDto.HomeView homeView = travelservice.getTravelToMainView(travelId);
        System.out.println("_________________________2");
        homeView.setPersonList(personService.getPersonInfoInTravel(travelId));
        System.out.println("_________________________3");
        //homeView.setPersonCount(personService.getPersonCountInTravel(travelId));
        //System.out.println("_________________________4");
        homeView.setEventList(eventService.getEventInfoInTravel(travelId));
        System.out.println("_________________________5");
        //homeView.setEventCount(eventService.getEventCountInTravel(travelId));
        //System.out.println("_________________________6");
        homeView.setPeriod(eventService.getTravelPeriod(travelId, homeView.getEventCount()));
        System.out.println("_________________________7");
        //homeView.setSuperUser(eventService.getSuperUser(travelId));
        return homeView;
    }

    @GetMapping("/{userId}/getUserInfoTmp")
    public List<TravelDto.Response> getUserInfo(@PathVariable int userId){
        return userservice.getUserJoinedTravel(userId);
    }

    @GetMapping("/{userId}/{travelId}/getImage")
    public String getTravelImage(@PathVariable int travelId){
        return travelservice.getTravelImageURL(travelId);
    }
    @PutMapping("/{userId}/{travelId}/updateTravelImage")
    public ResponseEntity<TravelDto.ResponseWithImage> uploadTravelImage(@PathVariable int travelId,
                                                                         @RequestPart(value="file",required = false) MultipartFile file)
            throws IOException {
        String objectURL = s3UploaderService.upload(file, "test");
        System.out.println(objectURL);
        return ResponseEntity.ok(travelservice.updateTravelImage(travelId, objectURL));
    }
    @PutMapping("/{userId}/{travelId}/updateTravelInfo")
    public ResponseEntity<TravelDto.Response> updateTravel(@PathVariable int travelId, @RequestBody Map map) {
        TravelDto.Request updateRequest = new TravelDto.Request(map.get("travel_name").toString());
        return ResponseEntity.ok(travelservice.updateTravelInfo(travelId, updateRequest)
        );
    }


    // 여행을 생성한 user가 여행 자체를 삭제하는 메소드 --> 안 사용
    @PostMapping("/{userId}/deleteTravel")
    public void deleteTravel(@PathVariable int userId, @RequestBody Map map) {
        int travelId = Integer.parseInt(map.get("travel_id").toString());
        if (personService.isUserSuperuser(travelId, userId)) {
            travelservice.deleteTravel(travelId);
        }
        else throw new DefaultException(ErrorCode.INVALID_DELETE_NOTSUPERUSER);
    }

    //NEW
    @DeleteMapping("/{travelId}/delete")
    public void deleteTravel2(@PathVariable int travelId) {
        travelservice.deleteTravel(travelId);
    }

}
