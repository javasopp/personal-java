package com.mds.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用下拉菜单信息封装
 *
 * @author sopp
 * @version 1.0
 * @date 2021/9/14 10:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckLable {
    private long key;
    private String value;
}
