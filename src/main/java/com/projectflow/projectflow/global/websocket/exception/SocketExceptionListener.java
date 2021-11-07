package com.projectflow.projectflow.global.websocket.exception;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ExceptionListener;
import com.projectflow.projectflow.global.exception.ErrorResponse;
import com.projectflow.projectflow.global.exception.GlobalException;
import com.projectflow.projectflow.global.websocket.SocketProperty;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SocketExceptionListener implements ExceptionListener {

    @Override
    public void onEventException(Exception e, List<Object> args, SocketIOClient client) {
        runExceptionHandling(e, client);
    }

    @Override
    public void onDisconnectException(Exception e, SocketIOClient client) {
        runExceptionHandling(e, client);
    }

    @Override
    public void onConnectException(Exception e, SocketIOClient client) {
        runExceptionHandling(e, client);
        client.disconnect();
    }

    @Override
    public void onPingException(Exception e, SocketIOClient client) {
        runExceptionHandling(e, client);
    }

    @Override
    public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        return false;
    }

    private void runExceptionHandling(Exception e, SocketIOClient client) {
        final ErrorResponse message;

        if (e.getCause() instanceof GlobalException exception) {
            message = ErrorResponse.builder()
                    .message(exception.getErrorCode().getMessage())
                    .status(exception.getErrorCode().getStatus())
                    .build();
        } else {
            message = ErrorResponse.builder()
                    .status(500)
                    .message(e.getCause().getMessage())
                    .build();
        }

        client.sendEvent(SocketProperty.ERROR_KEY, message);

    }

}
