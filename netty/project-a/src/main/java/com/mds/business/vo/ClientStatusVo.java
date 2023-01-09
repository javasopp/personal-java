package com.mds.business.vo;

import lombok.Data;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/11/22 10:00
 */
@Data
public class ClientStatusVo {
    /**
     * 客户端名字
     */
    private String name;

    /**
     * 客户端类型
     */
    private int type;

    /**
     * 客户端上线时间
     */
    private long onlineTime;

    /**
     * 保存后端发送
     */
    private String redisKey;
}
