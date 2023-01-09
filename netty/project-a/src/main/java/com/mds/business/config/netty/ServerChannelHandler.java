package com.mds.business.config.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mds.business.common.Constants;
import com.mds.business.util.Aes256Util;
import com.mds.business.util.RedisUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * tcp 逻辑处理
 *
 * @author sopp
 * @version 1.0
 * @date 2021/9/6 14:15
 */
@Slf4j
@ChannelHandler.Sharable
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServerChannelHandler extends SimpleChannelInboundHandler {

    private final TcpConnector tcpConnector;

    private final RedisUtil redisUtil;

    private final Aes256Util aes256Util;

    /**
     * 接收消息，开始处理
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        try {
            handleChannelReadEvent(ctx, msg);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("我是错误的信息:{}", msg);
        }
    }

    /**
     * 分离处理消息读取事件的每个类型
     *
     * @param msg
     */
    private void handleChannelReadEvent(ChannelHandlerContext ctx, Object msg) {
        String message = aes256Util.decode((String) msg);
        // TODO 这里消息后续需要解密
        JSONObject json = JSON.parseObject(message);
        JSONObject resultJSON = new JSONObject();
        // key表示当前的逻辑
        int key = json.getIntValue("key");
        // 线路请求的时候发送的其他json参数。一并提交给知识库接口。由他处理。
        JSONObject data = json.getJSONObject("data");
        // 客户端类型
        int clientType = json.getInteger("clientType");
        // 客户端名称
        String clientName = json.getString("clientName");
        // 当前是否临时连接：0-否；1-是；
        int status = json.getInteger("status");
        int knowledgeType = json.getInteger("knowledgeType");
        // redis 的key的格式：客户端类型：A/B + 知识库类型 + 客户端名称 + 当前是否临时
        String redisKey = clientType + "-" + knowledgeType + "-" + clientName + "-" + status;
        if (Constants.ONE == key) {
            // 注册逻辑
            handleRegister(ctx, redisKey, resultJSON);
        } else if (Constants.TWO == key) {
            // 心跳逻辑
            handleHeart(ctx, resultJSON);
        } else if (Constants.THREE == key) {
            // 请求线路逻辑
            handleRequest(ctx, json, resultJSON);
        } else {
            log.info("我是谁？");
        }
    }

    /**
     * 封装本地返回
     */
    private void writeAndFlush(ChannelHandlerContext ctx, JSONObject resultJSON) {
        String content = aes256Util.encode(resultJSON.toJSONString());
        ctx.channel().writeAndFlush(content).syncUninterruptibly();
    }

    /**
     * 处理注册
     *
     * @param ctx
     * @param redisKey
     * @param resultJSON
     */
    private void handleRegister(ChannelHandlerContext ctx, String redisKey, JSONObject resultJSON) {
        // TODO 获取所有的当前注册上来的客户端的信息参数：包括以下信息：
        /**
         * 1. 当前需要的是怎样的知识库参数
         * 2. 当前是否为临时端口
         * 3. 请求线路的具体json
         */
//            log.info("我是当前的json：{}", json.toJSONString());
        // 存入redis，后期模糊查询的时候，比内存快。
        redisUtil.set(redisKey, 1);
        // 往channel map中添加channel信息，添加的格式，redis的key，channel
        tcpConnector.addChannel(redisKey, ctx.channel());
        resultJSON.put("key", 1);
        resultJSON.put("status", 1);
        writeAndFlush(ctx, resultJSON);
    }

    /**
     * 处理心跳
     *
     * @param ctx
     * @param resultJSON
     */
    private void handleHeart(ChannelHandlerContext ctx, JSONObject resultJSON) {
        resultJSON.put("key", 2);
        resultJSON.put("status", 1);
        writeAndFlush(ctx, resultJSON);
    }

    /**
     * 处理请求
     *
     * @param ctx
     * @param json
     * @param resultJSON
     */
    private void handleRequest(ChannelHandlerContext ctx, JSONObject json, JSONObject resultJSON) {
        // 请求线路逻辑
//            resultJSON = null;
        // 表示下面的客户端的下面的B的redisKey。方便检索。
        String bClientKey = json.getString("redisKey");
        // TODO 在这里调用何威的返回接口。需要传递redisKey，和bClientKey
        // TODO 两种方式处理，第一种，何威接口返回很快，处理完成当前线程直接返回。第二种，通过redisKey，然后调用我这里下发返回。
        // 调用完了，返回设备
        resultJSON.put("key", 3);
        resultJSON.put("status", "ok");
        resultJSON.put("redisKey", bClientKey);
        JSONObject myData = new JSONObject();
        myData.put("test1", "test1");
        resultJSON.put("data", myData);
        writeAndFlush(ctx, resultJSON);
    }


    /**
     * 活跃的、有效的通道
     * 第一次连接成功后进入的方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("新增了一个tcp client " + getRemoteAddress(ctx) + " connect success");
    }

    /**
     * 不活动的通道
     * 连接丢失后执行的方法（client端可据此实现断线重连）
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("我先收到消息-断开连接");
        // 删除Channel Map中的失效Client
        log.info("当前连接关闭:{}", ctx.channel().remoteAddress().toString());
        tcpConnector.removeChannelByChannel(ctx.channel());
        ctx.close();
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
        log.error("引擎 {} 的通道发生异常，即将断开连接", getRemoteAddress(ctx));
        ctx.disconnect();
        ctx.close();
    }

    /**
     * 获取client对象：ip+port
     *
     * @param ctx
     * @return
     */
    public String getRemoteAddress(ChannelHandlerContext ctx) {
        String socketString = "";
        socketString = ctx.channel().remoteAddress().toString();
        return socketString;
    }

    /**
     * 获取client的ip
     *
     * @param ctx
     * @return
     */
    public String getIPString(ChannelHandlerContext ctx) {
        String ipString = "";
        String socketString = ctx.channel().remoteAddress().toString();
        int colonAt = socketString.indexOf(":");
        ipString = socketString.substring(1, colonAt);
        return ipString;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("我先收到消息，心跳超时");
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String socketString = ctx.channel().remoteAddress().toString();
            if (event.state() == IdleState.READER_IDLE) {
                // 断开
//                ctx.writeAndFlush("heartbeat").addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("Client: " + socketString + " WRITER_IDLE 写超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("Client: " + socketString + " ALL_IDLE 总超时");
                ctx.disconnect();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
