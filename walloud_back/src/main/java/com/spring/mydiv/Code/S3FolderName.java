package com.spring.mydiv.Code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum S3FolderName {
    USER_FOLDER("user_test"),
    TRAVEL_FOLDER("travel_test"),
    EVENT_FOLDER("event_test"),
    ;

    private final String description;
}