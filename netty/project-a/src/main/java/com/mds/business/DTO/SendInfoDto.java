package com.mds.business.DTO;

import lombok.Data;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/11/3 14:34
 */
@Data
public class SendInfoDto {
    /**
     * 标识当前知识库最终的结果。
     */
    private int type;

    /**
     * A客户端的redis key
     */
    private String redisKey;

    /**
     * 最终发送到设备中的json参数
     */
    private String sendInfo;
}
