package com.spring.mydiv.Code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NO_USER("There is no corresponding USER."),
    NO_TRAVEL("There is no corresponding TRAVEL."),
    NO_EVENT("There is no corresponding EVENT."),
    NO_PAYER("There is no PAYER for the event."),

    WRONG_EMAIL("There is no such email information."),
    WRONG_PASSWORD("Invalid password."),

    NO_DEVELOPER("해당되는 개발자가 없습니다."),
    DUPLICATED_MEMBER_ID("memberId가 중복됩니다."),
    LEVEL_AND_EXPERIENCE_YEARS_NOT_MATCH("개발자 레벨과 연차가 맞지 않습니다."),

    INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다.");

    private final String message;
}
