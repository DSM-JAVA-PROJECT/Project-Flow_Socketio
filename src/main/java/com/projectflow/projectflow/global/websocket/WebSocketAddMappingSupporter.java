package com.projectflow.projectflow.global.websocket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.projectflow.projectflow.global.websocket.annotations.Payload;
import com.projectflow.projectflow.global.websocket.annotations.SocketController;
import com.projectflow.projectflow.global.websocket.annotations.SocketMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class WebSocketAddMappingSupporter {

    private final ConfigurableListableBeanFactory beanFactory;
    private SocketIOServer socketIOServer;

    public void addListeners(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
        final List<Class<?>> classes = beanFactory.getBeansWithAnnotation(SocketController.class).values()
                .stream().map(Object::getClass)
                .collect(Collectors.toList());

        for (Class<?> cls : classes) {
            List<Method> methods = findSocketMappingAnnotatedMethods(cls);
            addSocketServerEventListener(methods);
        }

    }

    private void addSocketServerEventListener(List<Method> methods) {
        for (Method method : methods) {
            SocketMapping socketMapping = method.getAnnotation(SocketMapping.class);
            String endpoint = socketMapping.endpoint();
            Class<?> payload = getPayloadClass(method);

            socketIOServer.addEventListener(endpoint, payload, ((client, data, ackSender) ->
                    invokeServiceMethod(method, client, data)));
        }
    }

    private List<Method> findSocketMappingAnnotatedMethods(Class<?> cls) {
        return Arrays.stream(cls.getMethods())
                .filter(method -> method.getAnnotation(SocketMapping.class) != null)
                .collect(Collectors.toList());
    }

    private Class<?> getPayloadClass(Method method) {
        return Arrays.stream(method.getParameterTypes())                                    // 전체 파라미터들 조회
                .filter(aClass -> aClass.isAnnotationPresent(Payload.class))                // Payload 어노테이션이 붙어있는지 검사
                .findFirst()                                                                // 하나 반환
                .orElse(null);                                                        // 없으면 null
    }

    private void invokeServiceMethod(Method method, SocketIOClient client, Object data) throws InvocationTargetException, IllegalAccessException {
        List<Object> args = new ArrayList<>();
        for (Class<?> params : method.getParameterTypes()) {                        // Controller 메소드의 파라미터들
            if (params.equals(SocketIOServer.class)) args.add(socketIOServer);      // SocketIOServer 면 주입
            else if (params.equals(SocketIOClient.class)) args.add(client);         // 마찬가지
            else if (params.getAnnotation(Payload.class).annotationType().equals(Payload.class)) args.add(data);
        }
        method.invoke(method, args.toArray());
    }

}
