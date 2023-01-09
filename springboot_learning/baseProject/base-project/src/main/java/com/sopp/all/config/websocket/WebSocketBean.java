package com.sopp.all.config.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.websocket.Session;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketBean {

    /**
     * 当前的session会话
     */
    private Session session;
}
