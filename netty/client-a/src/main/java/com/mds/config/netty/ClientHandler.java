package com.mds.config.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mds.common.Constants;
import com.mds.config.netty.server.TcpConnector;
import com.mds.util.Aes256Util;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 保持心跳通信的代码。
 *
 * @author sopp
 * @version 1.0
 * @date 2021/11/2 10:58
 */
@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClientHandler extends SimpleChannelInboundHandler {

    @Value("${client.type}")
    private int type;

    @Value("${client.name}")
    private String name;

    @Value("${client.status}")
    private int status;

    @Value("${client.knowledge}")
    private int knowledge;

    private final Aes256Util aes256Util;

    private final TcpConnector tcpConnector;

    private final NettyClient nettyClient;

    /**
     * 发送心跳信息
     * @return
     */
    private String sendHeartInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clientType", type);
        jsonObject.put("key", 2);
        jsonObject.put("clientName", name);
        jsonObject.put("status", status);
        jsonObject.put("knowledgeType", knowledge);
        jsonObject.put("data", null);
        return aes256Util.encode(jsonObject.toJSONString());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                // 发送了心跳
                ctx.writeAndFlush(sendHeartInfo());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * 客户端收到的服务端相应
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = aes256Util.decode((String) msg);
        if (StringUtils.isNotBlank(message)) {

            JSONObject json = JSON.parseObject(message);
            int key = json.getIntValue("key");
            if (Constants.TWO == key) {
                log.info("收到了服务端返回心跳");
            }
            if (Constants.THREE == key) {
                // 主要处理 请求的key事件
                log.info("我是收到的msg.................:{}", msg);
                String redisKey = json.getString("redisKey");
                JSONObject data = json.getJSONObject("data");
                log.info("我是key,{}", redisKey);
                // TODO 需要进行推送到设备中。
                tcpConnector.sendToClient(redisKey, data);
            }
        }
    }

//    /**
//     * 如果4s没有收到写请求，则向服务端发送心跳请求
//     *
//     * @param ctx
//     * @param evt
//     * @throws Exception
//     */
//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("clientType", type);
//        jsonObject.put("key", 2);
//        jsonObject.put("clientName", name);
//        jsonObject.put("status", status);
//        jsonObject.put("knowledgeType", knowledge);
//        jsonObject.put("data", null);
//
//        if (evt instanceof IdleStateEvent) {
//            IdleStateEvent event = (IdleStateEvent) evt;
//            if (IdleState.WRITER_IDLE.equals(event.state())) {
//                ctx.writeAndFlush(aes256Util.encode(jsonObject.toJSONString())).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
//            }
//        }
//
//        super.userEventTriggered(ctx, evt);
//    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("连接存活");
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("我被断开连接了");
        ctx.close();
        nettyClient.reconnect();
    }


    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
        nettyClient.connect();
    }
}
