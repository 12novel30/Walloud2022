package com.spring.mydiv.Dto;

import com.spring.mydiv.Code.WalloudCode;
import com.spring.mydiv.Entity.*;
import lombok.*;

import javax.validation.constraints.NotNull;

public class ParticipantDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CRUDEvent {
        private Long personId;
        private Boolean role;
        private Double spent;
//        private WalloudCode isParticipatedChange = null;
        public static CRUDEvent fromEntity(Participant participant) {
            return CRUDEvent.builder()
                    .personId(participant.getPerson().getId())
                    .role(participant.getEventRole())
                    .spent(participant.getChargedPrice())
                    .build();
        }
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class forUpdateEvent {
        private CRUDEvent prev;
        private CRUDEvent curr;
        private WalloudCode isParticipatedChange = null;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private Long partiId;
        private Person person;
        private Event event;
        private Boolean eventRole;

        public static Response fromEntity(Participant participant) {
            return Response.builder()
                    .partiId(participant.getId())
                    .person(participant.getPerson())
                    .event(participant.getEvent())
                    .eventRole(participant.getEventRole())
                    .build();
        }

    }

    @Getter // TODO - controller
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Detail {
        private Long PersonId;
        private String Name;
        private boolean eventRole;
        private Double chargedPrice;
        public static Detail fromEntity(Participant participant) {
            return Detail.builder()
                    .PersonId(participant.getPerson().getId())
                    .Name(participant.getPerson().getUser().getName())
                    .eventRole(participant.getEventRole())
                    .chargedPrice(participant.getChargedPrice())
                    .build();
        }
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
        private Boolean role;
        /**Boolean eventRole
         * 1, true: payer
         * 0, false: -
         */
        @NotNull
        private Double chargedPrice;
        @NotNull
        private Event event;
    }

}