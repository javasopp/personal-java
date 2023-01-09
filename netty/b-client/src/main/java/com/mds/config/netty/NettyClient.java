package com.mds.config.netty;

import com.alibaba.fastjson.JSONObject;
import com.mds.util.Aes256Util;
import com.mds.util.SpringUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
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

//    private final ClientChannelInitializer clientChannelInitializer;

    private SocketChannel socketChannel = null;

    private Bootstrap client;

    private Channel channel;

    private ChannelFuture future;


    public void start() throws InterruptedException {
        // 首先，netty通过ServerBootstrap启动服务端
        client = new Bootstrap();
        ClientChannelInitializer clientChannelInitializer =
                (ClientChannelInitializer) springUtil.getBean("clientChannelInitializer");

        // 定义线程组，处理读写和链接事件，没有了accept事件
        // 绑定客户端通道
        // 给NIoSocketChannel初始化handler， 处理读写事件
        EventLoopGroup group = new NioEventLoopGroup();
        client.group(group).
                option(ChannelOption.TCP_NODELAY,true).
                channel(NioSocketChannel.class).
                handler(clientChannelInitializer);

        // 连接服务器
        connect();

        if (future.isSuccess()) {
            socketChannel = (SocketChannel) future.channel();
            sendInitInfoToServer(future);
        }
        // 发送数据给服务器
        // TODO 需要发送注册信息，不需要额外的信息
//        Map<String, Object> map = new HashMap<>(16);
//        map.put("test1", "test1");
//        // 发送信息到服务端
//        future.channel().writeAndFlush(JSONObject.toJSONString(map));


        // 当通道关闭了，就继续往下走
        future.channel().closeFuture().sync();

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

        String encodeInfo = aes256Util.encode(jsonObject.toJSONString());
        future.channel().writeAndFlush(encodeInfo);
    }

    /**
     * 在其他地方调用这个，进行发送。
     *
     * @return
     */
    public SocketChannel getSocketChannel() {
        return this.socketChannel;
    }

    public void connect() throws InterruptedException {
        if (channel != null && channel.isActive()) {
            return;
        }
        future = client.connect("localhost", 7001).sync();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture futureListener) throws Exception {
                if (futureListener.isSuccess()) {
                    channel = futureListener.channel();
                    log.info("连接到服务端成功!");
                } else {
                    log.info("连接失败，10秒后重试");

                    futureListener.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                connect();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 10, TimeUnit.SECONDS);
                }
            }
        });
    }

    /**
     * 发送消息到服务端
     *
     * @param json
     */
    public void sendInfoToServer(String json) {
        log.info("我是发送到服务器的数据:{}", json);
        socketChannel.writeAndFlush(json);
    }
}
