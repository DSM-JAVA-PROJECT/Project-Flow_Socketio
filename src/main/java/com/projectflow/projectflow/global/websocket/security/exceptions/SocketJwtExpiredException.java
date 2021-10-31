package com.projectflow.projectflow.global.websocket.security.exceptions;

import com.projectflow.projectflow.global.exception.SocketErrorCode;
import com.projectflow.projectflow.global.exception.SocketException;

public class SocketJwtExpiredException extends SocketException {
    public static final SocketException EXCEPTION = new SocketJwtExpiredException();

    private SocketJwtExpiredException() {
        super(SocketErrorCode.TOKEN_EXPIRED);
    }

}
