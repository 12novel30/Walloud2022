package com.spring.mydiv.Dto;

import com.spring.mydiv.Entity.Person;
import com.spring.mydiv.Entity.Travel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;

public class TravelDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        @NotNull
        private Long TravelId;
        @NotNull
        private String Name;
        @Nullable
        private Boolean IsSuper;

        public static Response fromEntity(Travel travel) {
            return Response.builder()
                    .TravelId(travel.getId())
                    .Name(travel.getName())
                    .build();
        }
        public static Response fromPersonEntity(Person person) {
            return Response.builder()
                    .TravelId(person.getTravel().getId())
                    .Name(person.getTravel().getName())
                    .IsSuper(person.getIsSuper())
                    .build();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class HomeView {
        @NotNull
        private Long TravelId;
        @NotNull
        private String TravelName;
        // TODO - homeView 에 image 넣을지 논의해야 함
//        @Nullable
//        private String Imageurl;
        @Nullable
        private List<PersonDto.HomeView> PersonList;
        @Nullable
        private int PersonCount;
        @Nullable
        private List<EventDto.Detail> EventList;
        @Nullable
        private int EventCount;
        @Nullable
        private String Period;
        @Nullable
        private Long SuperUserPersonId;

        public static HomeView fromEntity(Travel travel) {
            return HomeView.builder()
                    .TravelId(travel.getId())
                    .TravelName(travel.getName())
//                    .Imageurl(travel.getImage())
                    .build();
        }
    }

}
