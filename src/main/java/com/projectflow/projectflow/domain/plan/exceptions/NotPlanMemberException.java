package com.projectflow.projectflow.domain.plan.exceptions;

import com.projectflow.projectflow.global.exception.ErrorCode;
import com.projectflow.projectflow.global.exception.GlobalException;

public class NotPlanMemberException extends GlobalException {
    public static final GlobalException EXCEPTION = new NotPlanMemberException();

    private NotPlanMemberException() {
        super(ErrorCode.NOT_PLAN_MEMBER);
    }
}
