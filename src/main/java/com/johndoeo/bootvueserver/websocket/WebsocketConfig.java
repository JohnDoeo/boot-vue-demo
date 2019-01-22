package com.johndoeo.bootvueserver.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
@Configuration
@SuppressWarnings("ALL")
public class WebsocketConfig implements WebSocketConfigurer {

    @Autowired
    private MyWebsocketHandler handler;

    @Autowired
    private MyHandshakeInterceptor interceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry reg) {

        // websocket形式连接, client连接
        reg.addHandler(handler, "/websocket.do").setAllowedOrigins("*").addInterceptors(interceptor);

        // 不支持websocket的，采用sockjs
        reg.addHandler(handler, "/sockjs/info").setAllowedOrigins("*").addInterceptors(interceptor).withSockJS();
    }

}
