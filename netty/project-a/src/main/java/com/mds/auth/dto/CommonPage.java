package com.mds.auth.dto;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/9/9 16:23
 */
@Data
public class CommonPage {

    @Min(value = 1, message = "必须传递分页信息")
    private int current;

    @Min(value = 1, message = "必须传递分页信息")
    private int size;

    private String searchInfo;
}
