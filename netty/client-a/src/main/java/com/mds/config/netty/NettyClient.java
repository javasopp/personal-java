package com.mds.config.netty;

import com.alibaba.fastjson.JSONObject;
import com.mds.util.Aes256Util;
import com.mds.util.SpringUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * tcp客户端的实现代码。
 *
 * @author sopp
 * @version 1.0
 * @date 2021/11/1 09:54
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NettyClient {

    @Value("${client.type}")
    private int type;

    @Value("${client.name}")
    private String name;

    @Value("${client.status}")
    private int status;

    @Value("${client.knowledge}")
    private int knowledge;

    private final Aes256Util aes256Util;

    private final SpringUtil springUtil;

    private SocketChannel socketChannel;

    private Bootstrap bootstrap;

    private ChannelFuture channelFuture;

    private EventLoopGroup group;

    private ClientChannelInitializer clientChannelInitializer;

    public void connect() {
        // 首先，netty通过ServerBootstrap启动服务端
        bootstrap = new Bootstrap();

        group = new NioEventLoopGroup();
        clientChannelInitializer =
                (ClientChannelInitializer) springUtil.getBean("clientChannelInitializer");

        bootstrap.group(group).
                channel(NioSocketChannel.class).
                handler(clientChannelInitializer);

        try {


            channelFuture = bootstrap.connect("localhost", 9000);
            channelFuture.addListener((ChannelFutureListener) channelFuture -> {
                if (!channelFuture.isSuccess()) {
                    log.info("连接到服务器失败，10s后开始重连");
                    group.schedule(() -> {
                        log.info("正在尝试重连。。。。。。。");
                        reconnect();
                    }, 10, TimeUnit.SECONDS);
                } else {
                    log.info("连接成功。。。。。。。。");
                    socketChannel = (SocketChannel) channelFuture.channel();
                    sendInitInfoToServer(channelFuture);
                }
            });
            // 当通道关闭了，就继续往下走
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {

        }
    }

    /**
     * 在其他地方调用这个，进行发送。
     *
     * @return
     */
    public SocketChannel getSocketChannel() {
        return this.socketChannel;
    }

    /**
     * 重连策略
     */
    public void reconnect() {
        log.info("开始重连。。。。");
        connect();
    }


    /**
     * 发送消息到服务端
     *
     * @param json
     */
    public void sendInfoToServer(String json) {
        this.socketChannel.writeAndFlush(json);
    }


    /**
     * 连接成功过后，发送初始化信息到服务端
     *
     * @param future
     */
    private void sendInitInfoToServer(ChannelFuture future) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", 1);
        jsonObject.put("clientType", type);
        jsonObject.put("clientName", name);
        jsonObject.put("status", status);
        jsonObject.put("knowledgeType", knowledge);
        jsonObject.put("data", null);

        String info = aes256Util.encode(jsonObject.toJSONString());
        log.info("我是发送的初始化信息：{}", info);
        future.channel().writeAndFlush(info);
    }
}
