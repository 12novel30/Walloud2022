package com.spring.mydiv.Dto;

import com.spring.mydiv.Entity.Event;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;

public class EventDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        private String event_name;
        @NotNull
        private java.util.Date Date;
        @NotNull
        private int price;
        @NotNull
        private Long payer_person_id;
        private TravelDto.Response TravelDto;
        private List<ParticipantDto.CRUDEvent> parti_list;
        private String Image;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        @NotNull
        private Long EventId;
        @NotNull
        private String Name;

        public static Response fromEntity(Event event) {
            return Response.builder()
                    .EventId(event.getId())
                    .Name(event.getName())
                    .build();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Detail {
        @NotNull
        private Long EventId;
        @NotNull
        private String EventName;
        @NotNull
        private java.util.Date Date;
        @NotNull
        private int Price;
        @NotNull
        private Long PayerId;
        @Nullable
        private String PayerName;
        @Nullable
        private String ImageUrl;

        public static Detail fromEntity(Event event) {
            return Detail.builder()
                    .EventId(event.getId())
                    .EventName(event.getName())
                    .Date(event.getDate())
                    .Price(event.getPrice())
                    .PayerId(event.getPayerPersonid())
                    .build();
        }
    }

}
