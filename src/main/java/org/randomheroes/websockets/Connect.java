package org.randomheroes.websockets;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.randomheroes.bean.Response;
import org.randomheroes.bean.User;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class Connect extends TextWebSocketHandler {
    //接收到客户端消息时调用
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try{
            System.out.println("session message: " + message.getPayload());
            JSONObject accept = JSON.parseObject(message.getPayload());
            switch (accept.get("method").toString()){
                case "login" : {
                    User user = JSONObject.toJavaObject((JSON) accept.get("user"), User.class);
                    Response<User> json = new Response<>();
                    if(user.getUsername().equals("111")){
                        System.out.println("session id: " + session.getId());   //id与帐号存redis
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
            System.out.println("登陆错误: "+e.getMessage());
        }
    }

    // 与客户端完成连接后调用
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
//        System.out.println("afterConnectionEstablished");
//        System.out.println("getId:" + session.getId());
//        System.out.println("getLocalAddress:" + session.getLocalAddress().toString());
//        System.out.println("getTextMessageSizeLimit:" + session.getTextMessageSizeLimit());
//        System.out.println("getUri:" + session.getUri().toString());
        try {
            Response json = new Response<>();
            json.setMethod("connect");
            json.setCode("200");
            session.sendMessage(new TextMessage(JSON.toJSONString(json)));
        } catch (Exception e){
            System.out.println("连接错误:" +e.getMessage());
        }
    }

    // 消息传输出错时调用
    @Override
    public void handleTransportError(WebSocketSession session,
                                     Throwable exception) throws Exception {
        System.out.println("handleTransportError");
    }

    // 一个客户端连接断开时关闭
    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus closeStatus) throws Exception {
        System.out.println("afterConnectionClosed");
    }

    @Override
    public boolean supportsPartialMessages() {
        // TODO Auto-generated method stub
        return false;
    }
}
