package com.spring.mydiv.Dto;

import com.spring.mydiv.Code.WalloudCode;
import com.spring.mydiv.Entity.*;
import lombok.*;

import javax.validation.constraints.NotNull;

public class ParticipantDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CRUDEvent {
        private Long personId;
        private Boolean role;
        private Double spent;
        private String Name;
        public static CRUDEvent fromEntity(Participant participant) {
            return CRUDEvent.builder()
                    .personId(participant.getPerson().getId())
                    .role(participant.getEventRole())
                    .spent(participant.getChargedPrice())
                    .build();
        }
        public static CRUDEvent fromEntityDetailVer(Participant participant) {
            return CRUDEvent.builder()
                    .personId(participant.getPerson().getId())
                    .role(participant.getEventRole())
                    .spent(participant.getChargedPrice())
                    .Name(participant.getPerson().getUser().getName())
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