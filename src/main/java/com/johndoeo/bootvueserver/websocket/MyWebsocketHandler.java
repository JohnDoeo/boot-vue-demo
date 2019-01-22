package com.johndoeo.bootvueserver.websocket;

import com.alibaba.fastjson.JSONObject;
import com.johndoeo.bootvueserver.module.User;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class MyWebsocketHandler extends TextWebSocketHandler {


    //在线用户列表
    private static final Map<Integer, WebSocketSession> users;
    private static final Map<Integer, User> loginUsers;
    //用户标识
    private static final String CLIENT_USER = "user";

    static {
        users = new HashMap<>();
        loginUsers = new HashMap();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Integer userId = getClientId(session);
        User staff = getUser(session);
        if (userId != null) {
            WebSocketSession sessionExist = users.get(userId);
            if (sessionExist != null) {
                //不登出
                JSONObject result = new JSONObject();
                result.put("logout", "logout");
                TextMessage textMessage = new TextMessage(result.toJSONString());
                sessionExist.sendMessage(textMessage);
                users.remove(userId);
                sessionExist.close();
            }
            users.put(userId, session);
            loginUsers.put(userId, staff);
            JSONObject json = new JSONObject();
            json.put("result", "success");
            session.sendMessage(new TextMessage(json.toJSONString()));
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        WebSocketMessage message1 = new TextMessage("server:" + message);
        try {
            session.sendMessage(message1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        users.remove(getClientId(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Integer userId = getClientId(session);
        users.remove(userId);
        loginUsers.remove(userId);
        super.afterConnectionClosed(session, status);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 获取用户标识
     *
     * @param session
     * @return
     */
    private Integer getClientId(WebSocketSession session) {
        try {
            User staff = (User) session.getAttributes().get(CLIENT_USER);
            Integer clientId = staff.getId();
            return clientId;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取用户标识
     *
     * @param session
     * @return
     */
    private User getUser(WebSocketSession session) {
        try {
            User user = (User) session.getAttributes().get(CLIENT_USER);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 广播信息
     *
     * @param message
     * @return
     */
    public boolean sendMessageToAllUsers(TextMessage message) {
        boolean allSendSuccess = true;
        Set<Integer> clientIds = users.keySet();
        WebSocketSession session = null;
        for (Integer clientId : clientIds) {
            try {
                session = users.get(clientId);
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                allSendSuccess = false;
            }
        }

        return allSendSuccess;
    }
}
