package com.projectflow.projectflow.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    AUTH_USER_NOT_FOUND(404, "Auth User Not Found"),
    NOT_CHATROOM_MEMBER(403, "User Not Member of Project"),
    ALREADY_PARTICIPATE(409, "User Already Participated"),
    USER_NOT_FOUND(404, "User Not Found"),
    NOT_PROJECT_MEMBER(403, "Not Project Member"),
    CHATROOM_NOT_FOUND(404, "Chat Room Not Found"),
    PROJECT_NOT_FOUND(404, "Project Not Found"),
    JWT_EXPIRED(401, "Jwt Token Expired"),
    NOT_MESSAGE_OWNER(403, "Not Message Owner"),
    INTERNAL_SERVER_ERROR(500, "Unexpected Error"),
    JWT_VALIDATE_FAILED(401, "Jwt Validate Failed"),
    INVALID_TOKEN(401, "Invalid Token");

    private final int status;
    private final String message;
}
