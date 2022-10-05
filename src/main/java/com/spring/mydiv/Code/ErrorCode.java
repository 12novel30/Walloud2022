package com.spring.mydiv.Code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_FOUND(404,"COMMON-ERR-404","PAGE NOT FOUND"),
    INTER_SERVER_ERROR(500,"COMMON-ERR-500","INTER SERVER ERROR"),
    NO_USER(400, "USER-ERR-400", "There is no corresponding USER."),
    NO_TRAVEL(400, "TRAVEL-ERR-400","There is no corresponding TRAVEL."),
    NO_EVENT(400, "EVENT-ERR-400","There is no corresponding EVENT."),
    NO_PAYER(500, "EVENT-ERR-500","There is no PAYER for the event."),

    WRONG_EMAIL(500,"LOGIN-ERR-500","There is no such email information."),
    WRONG_PASSWORD(500, "LOGIN-ERR-500","Invalid password.");

    private int status;
    private String errorCode;
    private String message;
}
