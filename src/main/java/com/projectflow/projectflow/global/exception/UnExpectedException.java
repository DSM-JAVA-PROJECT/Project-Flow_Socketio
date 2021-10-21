package com.projectflow.projectflow.global.exception;

public class UnExpectedException extends GlobalException {
    public static final GlobalException EXCEPTION = new UnExpectedException();
    private UnExpectedException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
