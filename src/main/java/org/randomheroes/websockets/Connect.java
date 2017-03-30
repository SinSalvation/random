package org.randomheroes.websockets;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.randomheroes.bean.Bike;
import org.randomheroes.bean.Order;
import org.randomheroes.bean.Response;
import org.randomheroes.bean.User;
import org.randomheroes.dao.BikeMapper;
import org.randomheroes.dao.OrderMapper;
import org.randomheroes.dao.UserMapper;
import org.randomheroes.util.UserUtil;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;

public class Connect extends TextWebSocketHandler {

    private Logger loginLog = Logger.getLogger("login");
    private Logger connectLog = Logger.getLogger("connect");
    private Logger logoutLog = Logger.getLogger("logout");
    private Logger errorLog = Logger.getLogger("error");

    private UserMapper userMapper = (UserMapper)ContextLoader.getCurrentWebApplicationContext().getBean("userMapper");
    private BikeMapper bikeMapper = (BikeMapper)ContextLoader.getCurrentWebApplicationContext().getBean("bikeMapper");
//    private OrderMapper orderMapper = (OrderMapper)ContextLoader.getCurrentWebApplicationContext().getBean("orderMapper");

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
                    json.setMethod("login");
                    if(UserUtil.login(user.getUser_id(),user.getPassword(),user.getUsername())){
                        userMapper.insert(user);
                        json.setCode("200");
                        json.setMessage("login success");
                        json.setT(user);
                    } else {
                        json.setCode("500");
                        json.setMessage("login fail");
                    }
                    session.sendMessage(new TextMessage(JSON.toJSONString(json)));
                    break;
                }
                case "find":{
                    ArrayList<Bike> bikes = (ArrayList)bikeMapper.selectAll();
                    Response<ArrayList<Bike>> json = new Response<>();
                    json.setMethod("find");
                    json.setCode("200");
                    json.setMessage("find success");
                    json.setT(bikes);
                    session.sendMessage(new TextMessage(JSON.toJSONString(json)));
                    break;
                }
                case "start":{
                    Bike bike = JSONObject.toJavaObject((JSON) accept.get("bike"), Bike.class);
                    bike = bikeMapper.selectByPrimaryKey(bike.getBike_id());
                    bike.setAddrx("0");
                    bike.setAddry("0");
                    bikeMapper.updateByPrimaryKey(bike);
                    Response<Bike> json = new Response<>();
                    json.setMethod("start");
                    json.setCode("200");
                    json.setMessage("start success");
                    json.setT(bike);
                    session.sendMessage(new TextMessage(JSON.toJSONString(json)));
                    break;
                }
                case "end":{
                    Bike bike = JSONObject.toJavaObject((JSON) accept.get("bike"), Bike.class);
                    bikeMapper.updateByPrimaryKey(bike);
                    Response<Object> json = new Response<>();
                    json.setMethod("end");
                    json.setCode("200");
                    json.setMessage("end success");
                    session.sendMessage(new TextMessage(JSON.toJSONString(json)));
                    break;
                }
            }
        } catch (Exception e){
            loginLog.info("login error: " + e.getMessage());
            e.printStackTrace();
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
