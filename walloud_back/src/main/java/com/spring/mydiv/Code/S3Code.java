package com.spring.mydiv.Code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum S3Code {
    USER_FOLDER("user_test"),
    TRAVEL_FOLDER("travel_test"),
    EVENT_FOLDER("event_test"),

    DEFAULT_IMAGE("https://walloud-bucket-ver2.s3.ap-northeast-2.amazonaws.com/test/ac28ab47-ad36-49ba-84ab-0398f3324ee9gang.jpg"),
    ;

    private final String description;
}