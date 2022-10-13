package com.spring.mydiv.Code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_FOUND(404,"COMMON-ERR-404","PAGE NOT FOUND"),
    INTER_SERVER_ERROR(500,"COMMON-ERR-500","INTER SERVER ERROR"),

    NO_USER(500, "USER-ERR-500", "There is no corresponding USER."),
    NO_TRAVEL(500, "TRAVEL-ERR-500","There is no corresponding TRAVEL."),
    NO_EVENT(500, "EVENT-ERR-500","There is no corresponding EVENT."),
    NO_PAYER(500, "EVENT-ERR-500","There is no PAYER for the event."),
    NO_SUPERUSER(500, "TRAVEL-SUPERUSER-ERROR", "There is no SuperUser on this trip."),
    NO_PRICE(500, "EVENT-PRICE-ERROR", "There is no Price on this event."),

    WRONG_EMAIL(500,"LOGIN-ERR-500","There is no such email information."),
    WRONG_PASSWORD(500, "LOGIN-ERR-500","Invalid password."),

    ALREADY_EXISTED(500, "CREATE-PERSON-ERR-500","have already been invited."),
    ALREADY_REGISTERED(500, "CREATE-USER-ERR-500","is already registered."),


    CREATE_FAIL(500, "CREATE-ERR-500", "Failed to create entity on request."),
    CREATE_EVENT_FAIL(500, "CREATE-ERR-500", "Failed to create event entity on request."),
    CREATE_PARTICIPANT_FAIL(500, "CREATE-ERR-500", "Failed to create participant entity on request."),

    DELETE_FAIL(500, "DELETE-ERR-500", "Failed to delete entity on request."),

    INVALID_DELETE_SUPERUSER(500, "INVALID-ERR-500", "SuperUser cannot be deleted from Travel."),
    INVALID_DELETE_NOTSUPERUSER(500, "INVALID-ERR-500", "Only SuperUser can delete the Travel."),
    INVALID_DELETE_EVENTEXISTED(500, "INVALID-ERR-500", "This Person has participated Events."),

    INVALID_DELETE_TRAVELEXISTED(500, "INVALID-ERR-500", "This Person has participated Travel.");


    private int status;
    private String errorCode;
    private String message;
}
