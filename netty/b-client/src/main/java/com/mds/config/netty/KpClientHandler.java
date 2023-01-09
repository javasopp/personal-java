package com.mds.config.netty;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mds.common.Constants;
import com.mds.util.Aes256Util;
import io.netty.channel.ChannelFutureListener;
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
public class KpClientHandler extends SimpleChannelInboundHandler {

    @Value("${client.type}")
    private int type;

    @Value("${client.name}")
    private String name;

    @Value("${client.status}")
    private int status;

    @Value("${client.knowledge}")
    private int knowledge;

    private final Aes256Util aes256Util;

    private final NettyClient nettyClient;

    /**
     * 客户端收到的服务端相应
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            
            String message = aes256Util.decode((String) msg);
            if (StringUtils.isNotBlank(message)) {
                JSONObject json = JSON.parseObject(message);
            log.info("我是参数:{}", message);
                int key = json.getIntValue("key");
                if (Constants.TWO == key) {
                    // 心跳逻辑
//                log.info("收到了服务端回复的心跳");
                } else if (Constants.THREE == key) {
                    log.info("我是当前测试的返回结果解密:{}", json.toJSONString());
                }
            }
        } catch (Exception e) {
            log.error("error info:{}",msg);
            e.printStackTrace();

        }
    }

    /**
     * 如果4s没有收到写请求，则向服务端发送心跳请求
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clientType", type);
        jsonObject.put("key", 2);
        jsonObject.put("clientName", name);
        jsonObject.put("status", status);
        jsonObject.put("knowledgeType", knowledge);
        jsonObject.put("data", null);

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (IdleState.WRITER_IDLE.equals(event.state())) {
                ctx.writeAndFlush(aes256Util.encode(jsonObject.toJSONString())).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        }

        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接存活");
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Client is close");
        ctx.close();
    }

    @Override
    public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
        // 取消从服务器注册。此时重新注册。
        nettyClient.connect();
    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        // 发生异常，关闭连接
//        log.error("引擎 {} 的通道发生异常，即将断开连接", getRemoteAddress(ctx));
        // 再次建议close
        ctx.close();
        nettyClient.connect();
    }
}
