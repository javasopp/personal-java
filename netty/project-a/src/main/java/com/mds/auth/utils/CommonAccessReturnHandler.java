package com.mds.auth.utils;

import com.alibaba.fastjson.JSON;
import com.mds.business.common.message.Message;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/9/8 15:50
 */
@Component
public class CommonAccessReturnHandler {

    public void callBackAccessProcess(HttpServletResponse response, Message message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(JSON.toJSONString(message).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
