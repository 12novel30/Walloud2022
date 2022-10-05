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
    WRONG_PASSWORD(500, "LOGIN-ERR-500","Invalid password."),
    ALREADY_EXISTED(500, "CREATE-PERSON-ERR-500","Users using this email have already been invited."),
    CREATE_FAIL(500, "CREATE-ERR-500", "Failed to create entity on request."),
    CREATE_EVENT_FAIL(500, "CREATE-ERR-500", "Failed to create event entity on request."),
    CREATE_PARTICIPANT_FAIL(500, "CREATE-ERR-500", "Failed to create participant entity on request."),
    DELETE_FAIL(500, "DELETE-ERR-500", "Failed to delete entity on request.");


    private int status;
    private String errorCode;
    private String message;
}
