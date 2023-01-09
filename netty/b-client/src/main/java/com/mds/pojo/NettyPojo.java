package com.mds.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/11/16 16:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NettyPojo implements Serializable {

    private static final long serialVersionUID = -609537276749705687L;

    private String info;
}
