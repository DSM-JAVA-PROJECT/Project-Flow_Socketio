package com.projectflow.projectflow.global.websocket.security.exceptions;

import com.projectflow.projectflow.global.exception.SocketErrorCode;
import com.projectflow.projectflow.global.exception.SocketException;

public class SocketJwtValidatingFailedException extends SocketException {

    public static final SocketException EXCEPTION = new SocketJwtValidatingFailedException();

    private SocketJwtValidatingFailedException() {
        super(SocketErrorCode.JWT_VALIDATE_FAILED);
    }

}
