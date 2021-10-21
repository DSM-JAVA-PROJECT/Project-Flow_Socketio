package com.projectflow.projectflow.global.websocket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.global.websocket.annotations.Payload;
import com.projectflow.projectflow.global.websocket.annotations.SocketMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public record WebSocketMappingArgumentResolver(
        SocketIOServer socketIOServer) implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasMethodAnnotation(SocketMapping.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Class<?> payload = getPayloadClass(parameter);

        Method method = parameter.getMethod();

        SocketMapping payloadAnnotation = parameter.getMethodAnnotation(SocketMapping.class);
        String endpoint = payloadAnnotation.endpoint();

        socketIOServer.addEventListener(endpoint, payload, ((client, data, ackSender) ->
                invokeServiceMethod(method, client, data)));
        return socketIOServer;
    }

    private Class<?> getPayloadClass(MethodParameter parameter) {
        return Arrays.stream(parameter.getMethod().getParameterTypes())                     // 전체 파라미터들 조회
                .filter(aClass -> aClass.isAnnotationPresent(Payload.class))                // Payload 어노테이션이 붙어있는지 검사
                .findFirst()                                                                // 하나 반환
                .orElse(null);                                                        // 없으면 null
    }

    private void invokeServiceMethod(Method method, SocketIOClient client, Object data) throws InvocationTargetException, IllegalAccessException {
        List<Object> args = new ArrayList<>();
        for (Class<?> params : method.getParameterTypes()) {
            if (params.equals(SocketIOServer.class)) args.add(socketIOServer);
            else if (params.equals(SocketIOClient.class)) args.add(client);
            else if (params.equals(Payload.class)) args.add(data);
        }
        method.invoke(method, args.toArray());
    }

}
