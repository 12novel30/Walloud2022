package com.spring.mydiv.Code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NO_USER("해당되는 사용자가 없습니다."),
    NO_TRAVEL("해당되는 여행이 없습니다."),
    NO_EVENT("해당되는 이벤트가 없습니다."),
    NO_PAYER("결제자가 없는 이벤트입니다."),


    NO_DEVELOPER("해당되는 개발자가 없습니다."),
    DUPLICATED_MEMBER_ID("memberId가 중복됩니다."),
    LEVEL_AND_EXPERIENCE_YEARS_NOT_MATCH("개발자 레벨과 연차가 맞지 않습니다."),

    INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다.");

    private final String message;
}
