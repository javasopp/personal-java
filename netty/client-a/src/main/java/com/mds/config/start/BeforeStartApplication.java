package com.mds.config.start;

import com.mds.config.netty.NettyClient;
import com.mds.config.netty.server.NettyServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动类，启动netty客户端的监听
 * @author sopp
 * @version 1.0
 * @date 2021/11/8 15:04
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BeforeStartApplication implements CommandLineRunner {

    private final NettyClient nettyClient;

    private final NettyServer nettyServer;

    @Override
    public void run(String... args) {
        try {
            startNettyServer();
            startNettyClient();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("启动netty客户端失败，无法连接服务端，服务端未启动。尝试一直自动重连。");
        }
    }

    /**
     * 启动netty的A 服务端
     */
    public void startNettyServer() {
        nettyServer.start();
    }

    /**
     * 启动netty的A 客户端
     */
    public void startNettyClient() {
        nettyClient.connect();
    }


}
