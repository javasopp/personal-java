package com.sopp.all.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@CrossOrigin(origins = "*")
@ServerEndpoint("/websocket/{username}")
public class MyWebSocket {


    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketBean> map = new ConcurrentHashMap<>();

    /**
     * 当前的session会话
     */
    private Session session;

    public Session getSession() {
        return session;
    }


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        if (StringUtils.isNotBlank(username)) {
            log.info("我是username：{}", username);
            WebSocketBean webSocketBean = new WebSocketBean(session);
            map.remove(username);
            map.put(username, webSocketBean);
            addOnlineCount();
            log.info("新增一个连接，当前连接数为：{}", getOnlineCount());
            sendMessage("连接成功");
        }else{
            log.error("前端传递了null");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("username") String username) {
        map.remove(username);
        subOnlineCount();
        log.info("有一连接关闭！当前连接数为:{}", getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("收到来自信息: {}", message);
        //群发消息
        this.sendMessage(message);
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
        log.error("发生错误" + error.getMessage());
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) {
        try {
            Iterator<Map.Entry<String, WebSocketBean>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                WebSocketBean webSocketBean = (WebSocketBean) entry.getValue();
                if (webSocketBean != null && webSocketBean.getSession() != null) {
                    Session session = webSocketBean.getSession();
                    synchronized (this) {
                        session.getBasicRemote().sendText(message);
                        log.info("发送成功。。。。。。。。。。。。。。。。。。");
                    }
                }
            }

        } catch (Exception e) {
            log.error("websocket已经断开，暂时无法发送。{}", e.getMessage());
        }

    }

    /**
     * 群发自定义消息
     */
    public void sendInfo(String message) {
        log.info("推送消息,推送内容: {}", message);
        //这里可以设定只推送给这个sid的，为null则全部推送
        sendMessage(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 在线人数+1
     */
    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }


    /**
     * 在线人数-1
     */
    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }

}
