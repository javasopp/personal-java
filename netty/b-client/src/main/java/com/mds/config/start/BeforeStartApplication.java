package com.mds.config.start;


import com.mds.config.netty.NettyClient;
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

    @Override
    public void run(String... args) throws Exception {
        startNettyClient();
    }

    /**
     * 启动netty的A 客户端
     * @throws InterruptedException
     */
    public void startNettyClient() throws InterruptedException {
        nettyClient.start();
    }
}
