package com.spring.mydiv.Dto;

import com.spring.mydiv.Entity.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ParticipantDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class basic {
        private Person person;
        private Event event;
        private Boolean eventRole;

        public static basic fromEntity(Participant participant) {
            return basic.builder()
                    .person(participant.getPerson())
                    .event(participant.getEvent())
                    .eventRole(participant.getEventRole())
                    .build();
        }

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class response {
        private Long participantId;
        private Person person;
        private Event event;
        private Boolean eventRole;

        public static response fromEntity(Participant participant) {
            return response.builder()
                    .participantId(participant.getId())
                    .person(participant.getPerson())
                    .event(participant.getEvent())
                    .eventRole(participant.getEventRole())
                    .build();
        }

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class peopleList {
        @NotNull
        private List<Person> joinedPerson;
        @NotNull
        private Person payer;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class detailView {
        @NotNull
        private Long PersonId;
        @NotNull
        private String Name;
        @NotNull
        private boolean eventRole;
        @NotNull
        private Double chargedPrice;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotNull
        private Person person;
        @NotNull
        private Event event;
        @NotNull
        private Boolean role;
        /**Boolean eventRole
         * 1, true: payer
         * 0, false: -
         */
        @NotNull
        private Double chargedPrice;
    }

}