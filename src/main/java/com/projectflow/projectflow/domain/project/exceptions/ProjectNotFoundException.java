package com.projectflow.projectflow.domain.project.exceptions;

import com.projectflow.projectflow.global.exception.ErrorCode;
import com.projectflow.projectflow.global.exception.GlobalException;

public class ProjectNotFoundException extends GlobalException {

    public static final GlobalException EXCEPTION = new ProjectNotFoundException();

    private ProjectNotFoundException() {
        super(ErrorCode.PROJECT_NOT_FOUND);
    }
}
