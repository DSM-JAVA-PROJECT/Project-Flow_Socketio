package com.projectflow.projectflow.global.websocket.exception;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SocketExceptionResponse {

    private Integer status;
    private String message;

}
