package com.kma.engfinity.enums;

import lombok.Getter;

@Getter
public enum EError {
    BAD_REQUEST("Bad request!"),
    EXISTED_BY_USERNAME("Username is existed!"),
    EXISTED_BY_EMAIL("Email is existed!"),
    EXISTED_BY_PHONE_NUMBER("Phone number is existed!"),
    EXISTED_BY_IDENTIFICATION_NUMBER("Phone number is existed!"),
    USER_NOT_EXISTED("User is not existed!"),
    INCORRECT_PASSWORD("Password is not correct!"),
    UNAUTHENTICATED("Account is not authenticated!"),
    UNAUTHORIZED("Account is not authenticated!"),
    ROLE_NOT_FOUND("Role not found!"),
    TOPIC_NOT_EXISTED("User is not existed!"),
    USER_IN_CALL("Người dùng đang có cuộc gọi khác!"),
    NOT_ENOUGH_MONEY("User don't have enough money!"),
    TAG_NOT_EXISTED("Tag is not existed!"),
    COURSE_NOT_EXISTED("Course is not existed!"),
    INVALID_QUESTION_TYPE("Invalid question type"),
    NOT_FOUND_QUESTION("Not found question"),
    MEMBER_IN_THIS_COURSE("Member already enrolled in this course"),
    NOT_FOUND_TOPIC("Not found topic"),
    OPEN_API_ERROR("Open API error!"),
    CALL_ENDED("Cuộc gọi đã kết thúc!");

    private final String message;

    EError(String message) {
        this.message = message;
    }

}


