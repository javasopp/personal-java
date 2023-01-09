package com.mds.business.config.netty;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/9/6 09:54
 */
@Slf4j
@Getter
@Component
public class TcpConnector {
    /**
     * 存储client的channel
     * key:ip，value:Channel
     */
    private Map<String, Channel> map = new ConcurrentHashMap<String, Channel>();


    /**
     * 保存netty通道
     *
     * @param key     ： redis 的key的格式：客户端类型：A/B + 知识库类型 + 客户端名称 + 当前是否临时
     * @param channel
     */
    public void addChannel(String key, Channel channel) {
        this.map.put(key, channel);
    }

    /**
     * 移除netty通道
     *
     * @param key
     */
    public void removeChannel(String key) {
        this.map.remove(key);
    }

    /**
     * 清空channel通过channel，即通过value删除value
     *
     * @param channel
     */
    public void removeChannelByChannel(Channel channel) {
        for (String key : this.map.keySet()) {
            if (map.get(key) == channel) {
                map.remove(key);
                break;
            }
        }
    }

    /**
     * 清空所有的map集合内容
     */
    public void cleanAllChannel() {
        map.clear();
    }

    /**
     * 发送到其他的客户端
     *
     * @param listClient
     * @param info
     */
    public void sendToOtherClient(List<String> listClient, String info) {
        for (String clientKey : listClient) {
            Channel channel = map.get(clientKey);
            // TODO syncUninterruptibly这个方法可能有问题，这个是同步等待当前channel才继续下一步，需要测试
            channel.writeAndFlush(info).syncUninterruptibly();
        }
    }

    /**
     * 发送到其他的客户端-通过reidskey
     *
     * @param redisKey
     * @param info
     */
    public void sendToOtherClientByRedisKey(String redisKey, String info) {
        Channel channel = map.get(redisKey);
        channel.writeAndFlush(info).syncUninterruptibly();
    }
}
