package org.randomheroes.websockets;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.randomheroes.bean.Response;
import org.randomheroes.bean.User;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class Connect extends TextWebSocketHandler {
    private Logger loginLog = Logger.getLogger("login");
    private Logger connectLog = Logger.getLogger("connect");
    private Logger logoutLog = Logger.getLogger("logout");
    private Logger errorLog = Logger.getLogger("error");

    //接收到客户端消息时调用
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try{
            loginLog.info("session message: " + message.getPayload());
            JSONObject accept = JSON.parseObject(message.getPayload());
            switch (accept.get("method").toString()){
                case "login" : {
                    User user = JSONObject.toJavaObject((JSON) accept.get("user"), User.class);
                    Response<User> json = new Response<>();
                    if(user.getUsername().equals("111")){
                        loginLog.info("session id: " + session.getId());   //id与帐号存redis
                        user .setUid("1");
                        json.setMethod("login");
                        json.setCode("200");
                        json.setMessage("login success");
                        json.setT(user);
                        session.sendMessage(new TextMessage(JSON.toJSONString(json)));
                    } else {
                        json.setMethod("login");
                        json.setCode("500");
                        json.setMessage("login fail");
                        session.sendMessage(new TextMessage(JSON.toJSONString(json)));
                    }
                }
            }
        } catch (Exception e){
            loginLog.info("login error: " + e.getMessage());
        }
    }

    // 与客户端完成连接后调用
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        connectLog.info("getId:" + session.getId());
        connectLog.info("getLocalAddress:" + session.getLocalAddress());
        try {
            Response json = new Response<>();
            json.setMethod("connect");
            json.setCode("200");
            session.sendMessage(new TextMessage(JSON.toJSONString(json)));
        } catch (Exception e){
            connectLog.info("connect error:" + e.getMessage());
        }
    }

    // 消息传输出错时调用
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        errorLog.info("error: session id: " + session.getId() + "exception: " + exception.getMessage());
    }

    // 一个客户端连接断开时关闭
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        logoutLog.info("session id: " + session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
