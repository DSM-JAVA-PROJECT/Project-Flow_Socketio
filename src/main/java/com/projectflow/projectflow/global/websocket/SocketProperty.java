package com.projectflow.projectflow.global.websocket;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SocketProperty {

    public static final String MESSAGE_KEY = "message";

    public static final String JOIN_KEY = "join";

    public static final String RESIGN_KEY = "resign";

    public static final String ERROR_KEY = "error";

    public static final String CREATE_PLAN_KEY = "plan.create";

    public static final String JOIN_PLAN_KEY = "plan.join";

    public static final String RESIGN_PLAN_KEY = "plan.resign";

}
