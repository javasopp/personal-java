package com.mds.business.config.start;

import com.mds.business.util.MyUtils;
import com.mds.business.config.netty.NettyTcpServer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MyApplicationRun
 * @Description: 程序启动过后的操作
 * @Author: Sopp
 * @Date: 2019/12/10 15:47
 **/
@Component
@RequiredArgsConstructor
public class MyApplicationRun implements ApplicationRunner {

    private final MyUtils myUtils;

    private final NettyTcpServer nettyTcpServer;

    @Override
    public void run(ApplicationArguments args) {
        nettyTcpServer.start();
    }
}