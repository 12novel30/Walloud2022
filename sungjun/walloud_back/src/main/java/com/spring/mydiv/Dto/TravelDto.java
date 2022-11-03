package com.spring.mydiv.Dto;

import com.spring.mydiv.Entity.Travel;
import com.spring.mydiv.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

public class TravelDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        private String Name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        @NotNull
        private Long TravelId;
        @NotNull
        private String Name;
        public static Response fromEntity(Travel travel) {
            return Response.builder()
                    .TravelId(travel.getId())
                    .Name(travel.getName())
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

        private List<PersonDto.HomeView> PersonList;
        private int PersonCount;

        private List<EventDto.HomeView> EventList;
        private int EventCount;
        private String Period;

        private Long SuperUser;

        public static HomeView fromEntity(Travel travel) {
            return HomeView.builder()
                    .TravelId(travel.getId())
                    .TravelName(travel.getName())
                    .build();
        }
    }

}
