package com.projectflow.projectflow.domain.plan.exceptions;

import com.projectflow.projectflow.global.exception.ErrorCode;
import com.projectflow.projectflow.global.exception.GlobalException;

public class AlreadyPlanParticipateException extends GlobalException {
    public static final GlobalException EXCEPTION = new AlreadyPlanParticipateException();

    private AlreadyPlanParticipateException() {
        super(ErrorCode.PLAN_ALREADY_PARTICIPATE);
    }
}
