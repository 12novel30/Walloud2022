package com.spring.mydiv.Dto;

import com.spring.mydiv.Entity.Event;
import com.spring.mydiv.Entity.Person;
import com.spring.mydiv.Entity.Travel;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class EventDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class basic {
        private Travel Travel;
        private String Name;
        private Date Date;
        private int Price;
        private Double DividePrice;
        private Double TakePrice;

        public static basic fromEntity(Event event){
            return basic.builder()
                    .Travel(event.getTravel())
                    .Name(event.getName())
                    .Date(event.getDate())
                    .Price(event.getPrice())
                    .DividePrice(event.getDividePrice())
                    .TakePrice(event.getTakePrice())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class deleteRequest {
        @NotNull
        private List<Person> joinedPerson;
        @NotNull
        private Long payerId;
        private Double DividePrice;
        private Double TakePrice;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        private String Name;
        @NotNull
        private TravelDto.Response Travel;
        @NotNull
        private java.util.Date Date;
        @NotNull
        private int Price;
        private int PartiCount;
        private String Image;
        private boolean isPayerInParticipant;
        @NotNull
        private Long PayerPersonId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        @NotNull
        private Long Id;
        @NotNull
        private String Name;
        private Double DividePrice;
        private Double TakePrice;

        public static Response fromEntity(Event event) {
            return Response.builder()
                    .Id(event.getId())
                    .Name(event.getName())
                    .DividePrice(event.getDividePrice())
                    .TakePrice(event.getTakePrice())
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
        private Long Id;
        @NotNull
        private String Name;
        @NotNull
        private java.util.Date Date;
        @NotNull
        private int Price;
        private String PayerName;

        public static HomeView fromEntity(Event event) {
            return HomeView.builder()
                    .Id(event.getId())
                    .Name(event.getName())
                    .Date(event.getDate())
                    .Price(event.getPrice())
                    .build();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PersonView {
        @NotNull
        private Long EventId;
        @NotNull
        private String EventName;
        @NotNull
        private java.util.Date Date;
        @NotNull
        private int Price;
        @NotNull
        private Double DividePrice;
        @NotNull
        private Double TakePrice;
        private Long PayerId;
        private String PayerName;
        public static PersonView fromEntity(Event event) {
            return PersonView.builder()
                    .EventId(event.getId())
                    .EventName(event.getName())
                    .Date(event.getDate())
                    .Price(event.getPrice())
                    .DividePrice(event.getDividePrice())
                    .TakePrice(event.getTakePrice())
                    .build();
        }
    }

}
