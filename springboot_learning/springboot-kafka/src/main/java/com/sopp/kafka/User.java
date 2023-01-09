package com.sopp.kafka;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/6/17 10:00
 */
@Data
@Accessors(chain = true)
public class User {
    private String username;

    private String userId;

    private String state;
}
