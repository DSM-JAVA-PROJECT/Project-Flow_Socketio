package com.projectflow.projectflow.domain.plan.exceptions;

import com.projectflow.projectflow.global.exception.ErrorCode;
import com.projectflow.projectflow.global.exception.GlobalException;

public class PlanNotFoundException extends GlobalException {
    public static final GlobalException EXCEPTION = new PlanNotFoundException();

    private PlanNotFoundException() {
        super(ErrorCode.PLAN_NOT_FOUND);
    }
}
