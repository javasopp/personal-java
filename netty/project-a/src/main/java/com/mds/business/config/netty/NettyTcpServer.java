package com.mds.business.config.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @author sopp netty版本后续处理 https://blog.csdn.net/yzh_1346983557/article/details/85327042
 * @version 1.0
 * @date 2021/9/6 11:31
 */
@Slf4j
@Component
public class NettyTcpServer {
    /**
     * boss事件轮询线程组
     * 处理Accept连接事件的线程，这里线程数设置为1即可，netty处理链接事件默认为单线程，过度设置反而浪费cpu资源
     */
    private EventLoopGroup boss = new NioEventLoopGroup(1);
    /**
     * worker事件轮询线程组
     * 处理hadnler的工作线程，其实也就是处理IO读写 。线程数据默认为 CPU 核心数乘以2
     */
    private EventLoopGroup worker = new NioEventLoopGroup();

    @Autowired
    ServerChannelInitializer serverChannelInitializer;

    @Value("${netty.tcp.client.port}")
    private Integer port;

    /**
     * 与客户端建立连接后得到的通道对象
     */
    private Channel channel;

    /**
     * 开启Netty tcp server服务
     *
     * @return
     */
    public ChannelFuture start() {
        // 启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 组配置，初始化ServerBootstrap的线程组
        serverBootstrap.group(boss, worker)
                // 构造channel通道工厂
                // bossGroup的通道，只是负责连接
                .channel(NioServerSocketChannel.class)
                // 设置通道处理者ChannelHandlerworkerGroup的处理器
                .childHandler(serverChannelInitializer)
                // socket参数，当服务器请求处理程全满时，用于临时存放已完成三次握手请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50。
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 启用心跳保活机制，tcp，默认2小时发一次心跳
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        // Future：异步任务的生命周期，可用来获取任务结果
        // 绑定端口，开启监听，同步等待
        ChannelFuture channelFuture1 = serverBootstrap.bind(port).syncUninterruptibly();
        if (channelFuture1 != null && channelFuture1.isSuccess()) {
            // 获取通道
            channel = channelFuture1.channel();
            log.info("Netty tcp server start success, port = {}", port);
        } else {
            log.error("Netty tcp server start fail");
        }
        return channelFuture1;
    }

    /**
     * 停止Netty tcp server服务
     */
    @PreDestroy
    public void destroy() {
        if (channel != null) {
            channel.close();
        }
        try {
            Future<?> future = worker.shutdownGracefully().await();
            if (!future.isSuccess()) {
                log.error("netty tcp workerGroup shutdown fail, {}", future.cause());
            }
            Future<?> future1 = boss.shutdownGracefully().await();
            if (!future1.isSuccess()) {
                log.error("netty tcp bossGroup shutdown fail, {}", future1.cause());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Netty tcp server shutdown success");
    }
}
